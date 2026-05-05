# Projet Gestion d'Inscriptions à des Cours de Soutien

## Aperçu
Créer une application en Java permettant l'inscription d'enfants à des cours de soutien selon la capacité des salles et la gestion des paiements.

## Fonctionnalités
- Gestion des utilisateurs (parents, gestionnaires)
- Authentification par email et mot de passe
- Inscription des enfants selon créneaux disponibles
- Gestion des créneaux et capacités
- Paiements en une fois ou en plusieurs versements (6 max)
- Suivi des montants et mouvements de paiement


## Concepts de Programmation Orientée Objet
- **Agrégation** : Un parent peut posséder plusieurs enfants inscrits.
- **Cohésion** : Chaque classe doit remplir une responsabilité unique et spécifique.
- **Héritage** : Les rôles Parent et Gestionnaire héritent d'une classe Utilisateur.
- **Encapsulation** : Les données sensibles (mot de passe, paiements) sont protégées par des accesseurs.
- **Polymorphisme** : Gestion différenciée des actions selon le type d'utilisateur.

## Utilisation de Patrons de Conception
- **Factory** : Pour créer des instances de modes de paiement ou de comptes utilisateurs.
- **Observer** : Pour notifier les parents des échéances de paiement.
- **State** : Pour gérer les états des créneaux (disponible, complet).
- **Strategy** : Différentes stratégies de calcul de paiements et d'affectation aux créneaux.
- **Singleton** : Gestion centralisée de la configuration de l'application.
- **Facade** : Fournir une interface simplifiée aux modules pour accéder aux services métiers.

## Interface
- Code pour inscription, suivi de paiements et gestion administrative.

## Livrables
- Code source en modules.
- Documentation de l'architecture, des fonctionnalités et guide d'installation.
