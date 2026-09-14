# LABORATOIRE 2 - 420-930-MA - Ete 2026 - gr. 25604

Instructions :

1. Copiez ce fichier dans votre depot GitHub sous le nom README.md
2. Remplissez toutes les sections marquees [A COMPLETER]
3. Supprimez tous les commentaires HTML (<!-- ... -->) avant la remise
4. Deposez ce fichier (rempli) sur Teams, canal du groupe
   avec le titre : "Lab2 - Sujet X - Nom1 Nom2 [Nom3]"
   ====================================================================

# Voitures d'occasion - Lab 2

**Cours** : 420-930-MA — Algorithmes et modèles de programmation
**Session** : Été 2026, groupe 25604
**Laboratoire** : 2 (Application JavaFX v1)
**Date de remise** : 13 septembre 2026, 23h59

---

## Équipe

| Nom complet           | Adresse courriel        | Contribution principale                |
| --------------------- | ----------------------- | -------------------------------------- |
| Ammmour, Nadjib       | nadjib.ammour@gmail.com | Modèle, Service, Tris, Pagination, CSV |
| Pierre, Jean-François | jfp112@hotmail.com      | UI FXML, Controller, CSS               |
| Chandel, Amit         | a.chandel@pm.me         | Algorithmes, Benchmark                 |

---

## Sujet choisi

**Numéro du sujet** : 2
**Nom du sujet** : Voitures d'occasion

---

## 🔗 Lien du dépôt GitHub PUBLIC

**URL** : https://github.com/NjGuitlab/voitures-occasion-javafx

> ⚠️ Vérifier que le dépôt est **PUBLIC** et accessible sans authentification.
> Tester le lien dans un navigateur privé avant la remise.

---

## Fonctionnalités implémentées

### ✅ Obligatoires (cocher ce qui est fait)

- [x] Architecture MVC avec packages séparés (model / service / algorithmes / controller / util)
- [x] Chargement des données depuis fichier CSV (nombre de lignes : 420)
- [x] Interface JavaFX principale avec liste/tableau
- [x] Panneau détail affichant l'élément sélectionné
- [x] Pagination fonctionnelle (taille de page : 12)
- [x] Filtres multi-critères combinables (nombre implémentés : 6 / 7)
- [x] Recherche par texte en temps réel
- [x] Interface Algorithme définie
- [x] Tri #1 implémenté : Insertion
- [x] Tri #2 implémenté : Rapide
- [x] Tri #3 implémenté : Fusion
- [x] Comparateur/benchmark des tris avec mesure du temps
- [x] Wishlist / Favoris (ajout, retrait, pas de doublons)
- [x] CSS appliqué (thème visuel du projet)

### 🎁 Bonus (cocher ce qui est fait)

- [x] Cards au lieu d'un tableau
- [ ] [Bonus 2 : ex. Statistiques]
- [ ] [Bonus 3 : ...]

### ❌ Non implémenté (assumer honnêtement)

- ***

## Structure du projet

```

voitures-occasion-javafx/
├── pom.xml
├── src/main/
│   ├── java/
│   │   └── com.cours/
│   │       ├── MainFx.java
│   │       ├── model/
│   │       ├── service/
│   │       ├── algorithmes/
│   │       ├── controller/
│   │       └── util/
│   └── resources/
│       ├── fxml/
│       ├── styles/
│       └── data/voitures.csv
```

---

## Instructions pour lancer le projet

### Prérequis

- JDK 21
- Maven version 3.14.0
- (optionnel) IntelliJ IDEA / Eclipse

### Étapes

```bash
# 1. Cloner le dépôt
git clone https://github.com/NjGuitlab/voitures-occasion-javafx.git
cd voitures-occasion-javafx

# 2. Compiler
mvn clean compile

# 3. Lancer l'application
mvn javafx:run
```

### Alternative dans IntelliJ

1. Ouvrir le projet dans IntelliJ (File > Open > dossier du projet)
2. Attendre que Maven télécharge les dépendances
3. Ouvrir `Launcher.java`
4. Cliquer sur le bouton Run

---

## Choix techniques

### Version Java utilisée

Java 21

### Format des données

CSV, séparateur: virgule, encodage : UTF-8, nombre de lignes : 420

### Algorithmes de tri implémentés

- Insertion : O(n²)
- Rapide : O(n log n)
- Fusion : O(n log n)

### Bibliothèques externes utilisées

- junit-jupiter

---

## Difficultés rencontrées

Rendre le contrôleur assez mince en implantant tous les tris, filtres, fonctions, etc.

---

## Répartition du travail (auto-évaluation)

| Membre        | % contribution estimée | Ce sur quoi j'ai travaillé                                |
| ------------- | ---------------------- | --------------------------------------------------------- |
| Jean-François | 33%                    | Les fichiers FXML, les controlleurs, le CSS et le README  |
| Nadjib        | 33 %                   | Le modèle, le service, la pagination et la lecture du CSV |
| Amit          | 33%%                   | Les algorithmes, les comparateurs et le benchmark         |

---

## Notes pour le correcteur

Le benchmark est accessible en cliquant sur le bouton "Performance des tris"

---

## Captures d'écran

### Écran principal

![Écran principal](screenshots/principal.png)

### Écran de benchmark

![Benchmark](screenshots/benchmark.PNG)


---

## Historique Git

**Nombre total de commits** : 85
**Date du premier commit** : 2026-08-26
**Date du dernier commit** : 2026-09-13

Voir l'onglet **Insights > Contributors** de GitHub pour voir la contribution de chacun.

---


