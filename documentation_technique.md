# Documentation Technique — Gestion des Cours de Soutien

> **Projet Java · JavaFX · Persistance JSON**
> Réalisé dans le cadre d'une formation en alternance.

---

## Table des matières

1. [Vue d'ensemble](#1-vue-densemble)
2. [Architecture en couches](#2-architecture-en-couches)
3. [Modèle de domaine](#3-modèle-de-domaine)
4. [Patrons de conception](#4-patrons-de-conception)
5. [Couche services](#5-couche-services)
6. [Couche repositories](#6-couche-repositories)
7. [Interface utilisateur (JavaFX)](#7-interface-utilisateur-javafx)
8. [Persistance des données (JSON)](#8-persistance-des-données-json)
9. [Gestion des exceptions](#9-gestion-des-exceptions)
10. [Guide d'installation](#10-guide-dinstallation)
11. [Diagrammes UML](#11-diagrammes-uml)

---

## 1. Vue d'ensemble

L'application permet :

- L'**authentification** des parents et des administrateurs.
- L'**inscription d'enfants** à des créneaux de cours de soutien selon la capacité des salles.
- La **gestion des paiements** en une seule fois ou en plusieurs versements (6 maximum).
- Le **suivi des notifications** envoyées aux parents après chaque inscription.

### Stack technique

| Composant | Technologie |
|---|---|
| Langage | Java 17 |
| Interface graphique | JavaFX |
| Sérialisation | Jackson (JSON) |
| Build | Maven |
| Persistance | Fichiers JSON locaux |

---

## 2. Architecture en couches

L'application suit une architecture **MVC enrichie** en 5 couches distinctes :

```
┌─────────────────────────────────────────────┐
│           Couche Présentation (JavaFX)       │  Controllers UI
├─────────────────────────────────────────────┤
│              Couche Façade                   │  ManagementFacade
├─────────────────────────────────────────────┤
│              Couche Services                 │  Auth, Registration,
│                                             │  Payment, Notification…
├─────────────────────────────────────────────┤
│            Couche Repositories               │  Accès JSON (BaseRepository)
├─────────────────────────────────────────────┤
│           Couche Modèles (Domaine)           │  User, Child, TimeSlot…
└─────────────────────────────────────────────┘
                      ▲
              Patrons de Conception
     (Factory · Singleton · State · Strategy · Observer)
```

Chaque couche n'accède qu'à la couche immédiatement inférieure, garantissant une **faible couplage** et une **forte cohésion**.

---

## 3. Modèle de domaine

### 3.1 Hiérarchie utilisateurs (Héritage + Polymorphisme)

```
User (abstract)
├── Parent          – possède une liste d'enfants (agrégation)
└── Administrator   – accès aux fonctions d'administration
```

`User` est une classe abstraite avec la méthode `displayMenu()` polymorphique. Chaque sous-classe redéfinit son propre affichage de tableau de bord.

### 3.2 Classes principales

| Classe | Rôle | Relations clés |
|---|---|---|
| `User` | Base abstraite des utilisateurs | ← `Parent`, `Administrator` |
| `Parent` | Parent connecté, possède des enfants | `o-- List<Child>` (agrégation) |
| `Administrator` | Gestionnaire de la plateforme | — |
| `Child` | Enfant inscrit à des créneaux | porte `parentId` |
| `ClassRoom` | Salle de cours (nom, capacité) | composée dans `TimeSlot` |
| `TimeSlot` | Créneau de cours | `*-- ClassRoom`, délègue à `ITimeSlotState` |
| `Registration` | Inscription d'un enfant à un créneau | référence `childId`, `timeSlotId`, `parentId`, `*-- Payment` |
| `Payment` | Paiement associé à une inscription | porte `PaymentType` |
| `Notification` | Notification envoyée à un parent | porte `parentId`, `sent`, `sentDate` |

### 3.3 Note sur la sérialisation JSON

**Problème résolu — récursion infinie Jackson :**

`TimeSlot` contenait `List<Registration>` et `Registration` contenait `TimeSlot` → boucle infinie à la sérialisation.

**Solution appliquée :**
- `Registration` stocke uniquement les IDs (`childId`, `timeSlotId`) et non les objets complets.
- Les objets complets (`Child`, `TimeSlot`) sont annotés `@JsonIgnore` dans `Registration`.
- `TimeSlot` persiste `registrationCount` (entier) au lieu de `List<Registration>`.

---

## 4. Patrons de conception

### 4.1 Factory — `UserFactory` et `PaymentFactory`

**Problème résolu :** éviter la duplication de logique de création d'objets dans les controllers.

```java
// Création d'un utilisateur selon son rôle
User user = UserFactory.createUser(UserRole.PARENT, id, firstName, ...);

// Obtention de la stratégie de paiement
PaymentStrategy strategy = PaymentFactory.createPaymentStrategy(PaymentType.SPLIT_PAYMENT);
```

`UserFactory` retourne `Parent` ou `Administrator` selon le `UserRole`.
`PaymentFactory` retourne `SinglePayment` ou `SplitPayment` selon le `PaymentType`.

---

### 4.2 Singleton — `AppConfig`

**Problème résolu :** centraliser la configuration (chemins des fichiers JSON, paramètres métier) en une seule instance.

```java
int max = AppConfig.getInstance().getMaxInstallments(); // → 6
String path = AppConfig.getInstance().getParentsFilePath();
```

Constructeur privé, méthode statique `getInstance()` avec création paresseuse.

---

### 4.3 State — `ITimeSlotState` / `AvailableState` / `FullState`

**Problème résolu :** gérer dynamiquement la disponibilité d'un créneau sans multiplier les `if/else`.

```
TimeSlot.register()
    └──> state.register(this)
            ├── AvailableState : accepte si registrationCount < maxCapacity
            │                   → passe à FullState si plein
            └── FullState      : lance CapacityException
```

**Correction critique apportée :** le compteur utilisé est `registrationCount` (valeur persistée en JSON) et non `registrations.size()` (liste toujours vide en mémoire après désérialisation).

---

### 4.4 Strategy — `PaymentStrategy` / `SinglePayment` / `SplitPayment`

**Problème résolu :** interchanger l'algorithme de calcul du montant d'un versement.

```java
// Paiement unique → retourne totalAmount
// Paiement échelonné → retourne totalAmount / installmentCount
PaymentStrategy strategy = PaymentFactory.createPaymentStrategy(type);
double amount = strategy.calculatePayment(total, installments);
```

---

### 4.5 Observer — `Subject` / `Observer` / `NotificationService` / `ParentObserver`

**Problème résolu :** notifier automatiquement un parent après son inscription, sans que le service d'inscription connaisse les détails de notification.

```
NotificationService (Subject)
    └──> notifyParent(parentId, msg)
            1. clearObservers()
            2. addObserver(new ParentObserver(parentId, ...))
            3. notifyObservers(msg)
                └──> ParentObserver.update(msg)
                        → new Notification(id, parentId, msg)
                        → notification.markAsSent()
                        → NotificationRepository.save(notification)
```

---

### 4.6 Facade — `ManagementFacade`

**Problème résolu :** simplifier l'appel du controller en encapsulant l'orchestration des 3 services.

```java
// Côté controller : UN seul appel
facade.completeRegistration(parent, child, timeSlot, payment);

// En interne (ManagementFacade) :
// Étape 1 → RegistrationService.registerChild(...)
// Étape 2 → PaymentService.processPayment(...)
// Étape 3 → NotificationService.notifyParent(...)
```

---

## 5. Couche services

| Service | Responsabilité |
|---|---|
| `AuthService` | Authentification par email/mot de passe, gestion de session |
| `RegistrationService` | Inscription d'un enfant (State pattern + persistence) |
| `PaymentService` | Traitement d'un versement (Strategy pattern) |
| `NotificationService` | Notification d'un parent (Observer pattern) |
| `TimeSlotService` | Consultation et filtrage des créneaux disponibles |
| `ChildService` | Gestion du portefeuille d'enfants d'un parent |

---

## 6. Couche repositories

Tous les repositories héritent de `BaseRepository<T>` qui fournit :

- `findAll()`, `findById()`, `save()`, `update()`, `delete()`
- `generateNextId()` — calcul de l'ID suivant à partir du maximum existant

Les chemins des fichiers JSON sont lus depuis `AppConfig.getInstance()`.

| Repository | Fichier JSON |
|---|---|
| `ParentRepository` | `parents.json` |
| `AdministratorRepository` | `administrator.json` |
| `ChildRepository` | `children.json` |
| `TimeSlotRepository` | `timeSlots.json` |
| `RegistrationRepository` | `registration.json` |
| `PaymentRepository` | `payment.json` |
| `NotificationRepository` | `notifications.json` |

---

## 7. Interface utilisateur (JavaFX)

| Controller | Écran |
|---|---|
| `LoginController` | Écran de connexion (email/mot de passe) |
| `DashboardParentController` | Tableau de bord parent : liste des enfants, créneaux, inscriptions |
| `DashboardAdministratorController` | Tableau de bord admin : gestion des créneaux, statistiques |
| `RegistrationController` | Formulaire d'inscription : choix créneau, enfant, mode de paiement |
| `PaymentController` | Suivi et saisie des versements |

La session courante est gérée par `SessionManager` (pattern Singleton) qui conserve l'utilisateur connecté.

---

## 8. Persistance des données (JSON)

### Format de `registration.json`

```json
{
  "id": 1,
  "childId": 14426,
  "timeSlotId": 5,
  "parentId": 2,
  "registrationDate": "2026-05-18",
  "payment": {
    "id": 1,
    "totalAmount": 300.0,
    "paidAmount": 50.0,
    "remainingAmount": 250.0,
    "installmentCount": 6,
    "completedInstallments": 1,
    "remainingInstallments": 5,
    "paymentType": "SPLIT_PAYMENT",
    "completed": false
  }
}
```

### Format de `notifications.json`

```json
{
  "id": 1,
  "parentId": 2,
  "message": "Inscription de Jean Dupont au créneau \"Maths\" confirmée...",
  "sent": true,
  "sentDate": "2026-05-18T14:32:00"
}
```

---

## 9. Gestion des exceptions

| Exception | Déclenchée par | Contexte |
|---|---|---|
| `AuthException` | `AuthService` | Email ou mot de passe incorrect |
| `CapacityException` | `FullState.register()` | Créneau complet |
| `PaymentException` | `PaymentService` | Montant invalide ou paiement impossible |
| `InvalidDataException` | Services / validators | Données manquantes ou mal formées |

---

## 10. Guide d'installation

### Prérequis

- Java 17+
- Maven 3.8+
- JavaFX SDK (ou fourni via Maven)

### Étapes

```bash
# 1. Cloner le projet
git clone <url-du-projet>
cd apprenticeship

# 2. Compiler
mvn clean compile

# 3. Lancer l'application
mvn javafx:run
```

Les fichiers JSON de données se trouvent dans :
```
src/main/resources/data/
├── parents.json
├── children.json
├── timeSlots.json
├── registration.json
├── payment.json
├── notifications.json
└── administrator.json
```

---

## 11. Diagrammes UML

Les diagrammes PlantUML fournis sont :

| Fichier | Contenu |
|---|---|
| `01_modele_domaine.puml` | Classes métier, héritage, agrégation, enums |
| `02_patrons_conception.puml` | Factory, Singleton, Strategy, State, Observer |
| `03_architecture_applicative.puml` | Facade, Services, Repositories, SessionManager |
| `04_vue_globale_couches.puml` | Vue d'ensemble de toutes les couches |
| `05_observer_state_detail.puml` | Détail Observer + State avec notes de flux |

Pour générer les PNG depuis les `.puml` :

```bash
java -jar plantuml.jar *.puml
```

Ou utiliser l'extension **PlantUML** dans VS Code / IntelliJ IDEA.
