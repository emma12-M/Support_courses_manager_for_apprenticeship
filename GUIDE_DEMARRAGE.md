# 📘 Support Courses Manager for Apprenticeship

Application JavaFX permettant la gestion des inscriptions aux cours, des enfants et des paiements dans un contexte scolaire.

---

## 1. 📦 Prérequis

Avant d’exécuter l’application, assurez-vous d’avoir installé :

- **Java JDK** : version 17 ou supérieure (OpenJDK recommandé)
- **Apache Maven** : version 3.9.15 ou supérieure
- **JavaFX SDK** : version 21 (configuré via Maven)

### 🔍 Vérification

```bash
java -version
mvn -version
```

---
🚀 Lancement de l’application
▶️ Méthode 1 : Maven (recommandée)
Shell : cd C:\projetPro\Support_courses_manager_for_apprenticeship\apprenticeship
mvn clean javafx:run

▶️ Méthode 2 : JAR exécutable
Shell : mvn clean package -DskipTests

Puis :

Shell : java -module-path "C:\chemin\vers\javafx-sdk-21\lib" ^    
--add-modules 
javafx.controls,javafx.fxml,javafx.graphics ^     -jar target/apprenticeship-1.0.jar

``

3. 🔐 Comptes de test
👨‍👩‍👧 Parent

Email : jean.dupont@email.com
Mot de passe : hashed_password_1

👨‍💼 Administrateur

Email :robert.durand@admin.com
Mot de passe : admin_secure_password_1


4. 📋 Fonctionnalités
🔑 Authentification

Interface de connexion sécurisée
Redirection vers un tableau de bord personnalisé


📊 Tableau de bord parent

Liste des enfants
Inscriptions aux cours
Notifications


➕ Inscription d’un enfant

Ajout d’un enfant
Choix du niveau scolaire
Sélection d’un créneau
Gestion du paiement :

Paiement unique
Paiement fractionné




💾 Persistance des données
Les données sont stockées au format JSON dans :
src/main/resources/data/

Fichiers :

children.json → enfants
parents.json → parents
registration.json → inscriptions
payment.json → paiements


5. 🎨 Interface utilisateur
🎨 Couleurs principales

Bleu : #1e3a8a
Vert : #10b981
Orange : #f59e0b
Rouge : #ef4444

🧩 Composants

Formulaires dynamiques
Validation utilisateur
Cartes (cards)
Navigation latérale
Interface responsive


6. ✅ Vérifications
✔️ Compilation
Shell : mvn clean compile

✔️ Tests
Shell : mvn test

✔️ Packaging
Shell : mvn clean package

7. 🐛 Dépannage
❌ JavaFX manquant
Shell : mvn clean javafx:run

❌ Erreur compilation
Shell : mvn clean compile

❌ Données non sauvegardées

Vérifier le dossier data
Vérifier les permissions
Consulter les logs


❌ Problème de connexion

Vérifier les identifiants
Vérifier les fichiers JSON
Consulter la console


8. 📁 Structure du projet
apprenticeship/
├── src/main/
│   ├── java/              → logique métier (MVC)
│   └── resources/
│       ├── config/
│       ├── data/
│       ├── images/
│       └── ui/
├── target/
├── pom.xml
└── README.md


9. ✅ Checklist

 Compilation sans erreurs
 Authentification fonctionnelle
 Ajout d’enfants opérationnel
 Persistance JSON valide
 Tableau de bord fonctionnel
 Gestion des paiements opérationnelle


10. 📌 Conclusion
Cette application constitue une solution pédagogique permettant de gérer les inscriptions aux cours et les paiements associés. Elle repose sur une architecture simple avec JavaFX et une persistance en JSON adaptée à un projet académique.

👨‍💻 Auteur
Projet réalisé dans le cadre d’un apprentissage en développement logiciel.

---
