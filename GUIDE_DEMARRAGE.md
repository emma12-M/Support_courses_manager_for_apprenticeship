# 🚀 GUIDE DE DÉMARRAGE RAPIDE

## 📦 Prérequis

- **Java**: OpenJDK 17+ (Adoptium JDK recommandé)
- **Maven**: 3.9.15 ou plus
- **JavaFX SDK**: 21 (configuré dans pom.xml)

Vérifier l'installation:
```bash
java -version
mvn -version
```

---

## 🎯 DÉMARRAGE DE L'APPLICATION

### Option 1: Via Maven (Recommandé)

```bash
cd c:\projetPro\Support_courses_manager_for_apprenticeship\apprenticeship

# Compiler et lancer
mvn clean javafx:run
```

### Option 2: Via JAR Exécutable

```bash
cd c:\projetPro\Support_courses_manager_for_apprenticeship\apprenticeship

# Compiler et packager
mvn clean package -DskipTests

# Lancer le JAR
java -module-path "C:\chemin\vers\javafx-sdk-21\lib" \
     --add-modules javafx.controls,javafx.fxml,javafx.graphics \
     -jar target/apprenticeship-1.0.jar
```

---

## 🔐 Comptes de Test

### Pour Parent
```
Email:    emma@gmail.com
Password: 1234
```

### Pour Administrateur
```
Email:    admin@ecole.fr
Password: admin123
```

---

## 📋 FLUX D'UTILISATION

### 1️⃣ Connexion
1. Lancer l'application
2. Voir écran de login avec design moderne
3. Entrer email + mot de passe
4. Cliquer "Se connecter"

### 2️⃣ Dashboard Parent
1. Après connexion, voir liste:
   - 👶 Mes enfants
   - 📋 Mes inscriptions  
   - 🔔 Notifications
2. Menu latéral avec actions:
   - ✏️ Nouveau (inscrire enfant)
   - 💳 Paiements

### 3️⃣ Inscrire un Enfant
1. Cliquer "Nouveau"
2. Remplir formulaire:
   - Prénom de l'enfant
   - Nom de l'enfant
   - Niveau scolaire (optionnel)
   - Sélectionner créneau
3. Section Paiement:
   - Choisir mode (Paiement unique / Fractionné)
   - Montant total
   - *(Si fractionné)* Nombre de versements
4. Cliquer "Inscrire"
5. Confirmation + données sauvegardées

### 4️⃣ Vérifier Persistance
Les données sont automatiquement sauvegardées dans:
```
src/main/resources/data/
├── children.json          (enfants)
├── parents.json           (parents + enfants)
├── registration.json      (inscriptions)
└── payment.json           (paiements)
```

---

## 🎨 DESIGN & INTERFACE

### Couleurs
- **Primary** (Bleu): #1e3a8a
- **Success** (Vert): #10b981
- **Warning** (Orange): #f59e0b
- **Danger** (Rouge): #ef4444

### Composants
- Formulaires avec validation
- Listes interactives
- Boutons réactifs au survol
- Design card moderne
- Navigation intuitive

---

## 🔍 VÉRIFICATIONS

### Compilation
```bash
cd apprenticeship
mvn clean compile
# Résultat attendu: BUILD SUCCESS
```

### Tests
```bash
mvn test
# Tous les tests doivent passer
```

### Package
```bash
mvn clean package
# Résultat: target/apprenticeship-1.0.jar créé
```

---

## 🐛 DÉPANNAGE

### Problème: "JavaFX runtime components are missing"
**Solution**: 
```bash
# Ajouter --add-modules au lancement:
mvn clean javafx:run

# Ou si JAR:
java -module-path "./lib" --add-modules javafx.controls,javafx.fxml -jar apprenticeship-1.0.jar
```

### Problème: "Cannot find symbol" lors de compilation
**Solution**: 
```bash
# Nettoyer et reconstruire:
mvn clean compile

# Ou vérifier les imports dans les fichiers .java
```

### Problème: Données non persistées
**Solution**:
- Vérifier que `src/main/resources/data/` existe
- Vérifier les permissions d'écriture du dossier
- Consulter les logs lors du lancement

### Problème: Login échoue
**Solution**:
- Vérifier credentials (voir section comptes de test)
- Vérifier que `administrator.json` et `parents.json` existent
- Consulter la console pour les messages d'erreur

---

## 📊 STRUCTURE DU PROJET

```
apprenticeship/
├── src/main/
│   ├── java/
│   │   └── (Controllers, Services, Models, Repositories)
│   └── resources/
│       ├── config/
│       │   └── app.properties
│       ├── data/
│       │   └── (JSON files)
│       ├── images/
│       └── ui/
│           └── (FXML files)
├── target/
│   └── apprenticeship-1.0.jar
├── pom.xml
└── README.md
```

---

## 📚 DOCUMENTATION SUPPLÉMENTAIRE

- **Rapport Complet**: `RAPPORT_FINAL.md`
- **Tests de Validation**: `TEST_VALIDATION.md`
- **Architecture**: `EcoleProjet.md`

---

## ✅ CHECKLIST AVANT DÉPLOIEMENT

- [ ] Application compile sans erreurs
- [ ] Comptes de test fonctionnent
- [ ] Inscription d'enfant fonctionne
- [ ] Données persistées dans JSON
- [ ] Dashboard affiche les données
- [ ] Paiements enregistrés correctement
- [ ] Notifications reçues

---

## 📞 SUPPORT

Pour toute question ou problème:
1. Consulter les fichiers de documentation
2. Vérifier les logs de l'application
3. Reconstruire avec `mvn clean build`
4. Vérifier que tous les prérequis sont installés

---

**Bonne utilisation ! 🎉**

