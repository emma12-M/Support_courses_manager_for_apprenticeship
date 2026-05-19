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
10. [Flux de l'application](#10-flux-de-lapplication)
11. [Guide d'installation](#11-guide-dinstallation)
12. [Diagrammes UML](#12-diagrammes-uml)

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
| Langage | Java 17 (Adoptium JDK) |
| Interface graphique | JavaFX 21 |
| Sérialisation | Jackson 2.17.1 (JSON) |
| Build | Maven 3.9 |
| Tests | JUnit 4.13.2 |
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
     (Factory · Singleton · State · Strategy · Observer · Facade)
```

Chaque couche n'accède qu'à la couche immédiatement inférieure, garantissant un **faible couplage** et une **forte cohésion**.

### Structure des fichiers sources

```
src/main/java/
├── Models/             – Entités métier (User, Child, TimeSlot…)
├── enums/              – PaymentType, UserRole
├── exceptions/         – AuthException, CapacityException…
├── interfaces/         – Observer, Subject, ITimeSlotState, PaymentStrategy
├── factories/          – UserFactory, PaymentFactory
├── strategies/         – SinglePayment, SplitPayment
├── states/             – AvailableState, FullState
├── observers/          – ParentObserver
├── singleton/          – AppConfig
├── managers/           – SessionManager
├── facades/            – ManagementFacade
├── services/           – AuthService, RegistrationService…
├── repositories/       – BaseRepository, ParentRepository…
├── ui/controllers/     – LoginController, DashboardParentController…
└── utils/              – AlertUtil, DataCleanupUtil…

src/main/resources/
├── data/               – Fichiers JSON de persistance
└── ui/views/           – Fichiers FXML (interfaces JavaFX)
```

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

### 4.2 Singleton — `AppConfig` et `SessionManager`

**Problème résolu :** centraliser la configuration et la session en une seule instance, garantissant une seule vérité dans toute l'application.

```java
// Configuration globale
int max = AppConfig.getInstance().getMaxInstallments(); // → 6
String path = AppConfig.getInstance().getParentsFilePath();

// Session utilisateur
SessionManager.getInstance().login(user);
User courant = SessionManager.getInstance().getCurrentUser();
```

Constructeur privé, méthode statique `getInstance()` avec création paresseuse. `AppConfig` centralise tous les chemins de fichiers JSON et les paramètres métier. `SessionManager` conserve l'utilisateur connecté entre les écrans JavaFX.

---

### 4.3 State — `ITimeSlotState` / `AvailableState` / `FullState`

**Problème résolu :** gérer dynamiquement la disponibilité d'un créneau sans multiplier les `if/else`.

```
TimeSlot.register()
    └──> state.register(this)
            ├── AvailableState : accepte si registrationCount < maxCapacity
            │                   → passe à FullState si créneau plein
            └── FullState      : lance CapacityException systématiquement
```

**Correction critique apportée :** le compteur utilisé est `registrationCount` (valeur persistée en JSON) et non `registrations.size()` (liste toujours vide en mémoire après désérialisation). L'état est recalculé au démarrage via `refreshState()`.

---

### 4.4 Strategy — `PaymentStrategy` / `SinglePayment` / `SplitPayment`

**Problème résolu :** interchanger l'algorithme de calcul du montant d'un versement sans modifier le service.

```java
PaymentStrategy strategy = PaymentFactory.createPaymentStrategy(type);
double montantVersement = strategy.calculatePayment(totalAmount, installmentCount);
```

- `SinglePayment` → retourne `totalAmount` en entier (paiement unique)
- `SplitPayment` → retourne `totalAmount / installmentCount` (paiement échelonné)

Exemple de calcul pour 120 € en 3 versements :

```
Versement 1 : 40 € → remainingAmount = 80, remainingInstallments = 2
Versement 2 : 40 € → remainingAmount = 40, remainingInstallments = 1
Versement 3 : 40 € → remainingAmount = 0,  completed = true
```

---

### 4.5 Observer — `Subject` / `Observer` / `NotificationService` / `ParentObserver`

**Problème résolu :** notifier automatiquement un parent après son inscription, sans que le service d'inscription connaisse les détails de notification.

```
NotificationService (Subject)
    └──> notifyParent(parentId, msg)
            1. clearObservers()        ← évite les doublons entre sessions
            2. addObserver(new ParentObserver(parentId, parentName))
            3. notifyObservers(msg)
                └──> ParentObserver.update(msg)
                        → new Notification(id, parentId, msg)
                        → notification.markAsSent()   ← sent=true, sentDate=now
                        → NotificationRepository.save(notification)
```

---

### 4.6 Facade — `ManagementFacade`

**Problème résolu :** simplifier l'appel du controller en encapsulant l'orchestration des 3 services en un seul point d'entrée.

```java
// Côté controller : UN seul appel
facade.completeRegistration(parent, child, timeSlot, payment);
```

En interne, la Facade orchestre dans l'ordre :

1. `RegistrationService.registerChild(...)` → inscription + Pattern State
2. `PaymentService.processPayment(...)` → premier versement + Pattern Strategy
3. `NotificationService.notifyParent(...)` → notification + Pattern Observer

---

## 5. Couche services

| Service | Responsabilité |
|---|---|
| `AuthService` | Authentification par email/mot de passe, gestion de session |
| `RegistrationService` | Inscription d'un enfant (State pattern + persistance) |
| `PaymentService` | Traitement d'un versement (Strategy pattern) |
| `NotificationService` | Notification d'un parent (Observer pattern / Subject) |
| `TimeSlotService` | Consultation et filtrage des créneaux disponibles |
| `ChildService` | Gestion du portefeuille d'enfants d'un parent |

---

## 6. Couche repositories

Tous les repositories héritent de `BaseRepository<T>` qui fournit :

- `findAll()`, `findById()`, `save()`, `update()`, `delete()`
- `generateNextId()` — calcul de l'ID suivant à partir du maximum existant dans le fichier JSON

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
| `LoginController` | Écran de connexion (email / mot de passe) |
| `DashboardParentController` | Tableau de bord parent : enfants, créneaux, inscriptions, notifications |
| `DashboardAdministratorController` | Tableau de bord admin : gestion des créneaux, statistiques |
| `RegistrationController` | Formulaire d'inscription : créneau, enfant, mode de paiement |
| `PaymentController` | Suivi et saisie des versements |

La session courante est gérée par `SessionManager` (Singleton) qui conserve l'utilisateur connecté et permet la navigation entre les écrans sans passer l'objet en paramètre.

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

## 10. Flux de l'application

### Authentification

```
Écran Login
    ↓ email + password
AuthService.login()
    ↓ vérification (ParentRepository ou AdministratorRepository)
SessionManager.getInstance().login(user)
    ↓
Dashboard Parent  ou  Dashboard Administrateur
```

### Inscription d'un enfant

```
RegistrationController
    ↓ formulaire : enfant, créneau, mode paiement, montant
ManagementFacade.completeRegistration(parent, child, timeSlot, payment)
    │
    ├── RegistrationService.registerChild()
    │     ├── timeSlot.register()             → AvailableState vérifie la capacité
    │     ├── timeSlot.incrementRegistrationCount()
    │     ├── TimeSlotRepository.update()     → mise à jour JSON
    │     └── RegistrationRepository.save()
    │
    ├── PaymentService.processPayment()
    │     ├── PaymentFactory.createPaymentStrategy(type)
    │     ├── strategy.calculatePayment(total, installments)
    │     └── PaymentRepository.save()
    │
    └── NotificationService.notifyParent()
          └── ParentObserver.update()
                └── NotificationRepository.save()
```

### Suivi des paiements

```
PaymentController
    ↓ saisie d'un versement
payment.makePayment(amount)
    ├── paidAmount += amount
    ├── remainingAmount -= amount
    ├── completedInstallments++
    ├── remainingInstallments--
    └── si remainingAmount == 0 → completed = true
PaymentRepository.update(payment)
```

---

## 11. Guide d'installation

### Prérequis

- Java 17+
- Maven 3.8+
- JavaFX SDK 21 (ou fourni via Maven)

### Étapes

```bash
# 1. Cloner le projet
git clone <url-du-projet>
cd apprenticeship

# 2. Compiler
mvn clean compile

# 3. Lancer l'application
mvn javafx:run

# 4. Générer le JAR (optionnel)
mvn clean package
java -jar target/apprenticeship-1.0.jar
```

### Comptes de test

| Rôle | Email | Mot de passe |
|---|---|---|
| Parent | jean.dupont@email.com | hashed_password_1 |
| Administrateur | robert.durand@admin.com | admin_secure_password_1 |

### Fichiers de données

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

## 12. Diagrammes UML

Les diagrammes PlantUML fournis couvrent l'intégralité de l'architecture :

| Fichier | Contenu |
|---|---|
| `01_modele_domaine.puml` | Classes métier, héritage, agrégation, enums |
| `02_patrons_conception.puml` | Factory, Singleton, Strategy, State, Observer |
| `03_architecture_applicative.puml` | Facade, Services, Repositories, SessionManager |
| `04_vue_globale_couches.puml` | Vue d'ensemble de toutes les couches |
| `05_observer_state_detail.puml` | Détail Observer + State avec notes de flux |

### Générer les diagrammes en PNG

```bash
# Avec PlantUML en ligne de commande
java -jar plantuml.jar *.puml

# Ou via l'extension PlantUML dans VS Code / IntelliJ IDEA
```