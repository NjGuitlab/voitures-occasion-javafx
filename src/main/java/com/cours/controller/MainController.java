package com.cours.controller;

import com.cours.algorithmes.TriFusion;
import com.cours.algorithmes.TriInsertion;
import com.cours.algorithmes.TriRapide;
import com.cours.model.Transmission;
import com.cours.model.TypeCarburant;
import com.cours.model.Voiture;
import com.cours.model.VoitureComparateurs;
import com.cours.service.VoitureService;
import com.cours.util.LectureCSV;
import com.cours.util.Pagination;
import com.cours.util.SourceDonnees;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainController {

    // On implémente une instance de Voiture service dans le Controller
    private final VoitureService service = new VoitureService(new LectureCSV());

    private List<Voiture> listeVoitures;

    private TriFusion<Voiture> fusion = new TriFusion<>();
    private TriRapide<Voiture> rapide = new TriRapide<>();
    private TriInsertion<Voiture> insertion = new TriInsertion<>();

    private Pagination pagination;  // On importe l'util pour gérer la pagnination
    private static final int VOITURES_PAR_PAGE = 12;  // Le nombre de cards qu'on veut voir dans chaque page

    public MainController(){}

    @FXML
    private TextField champRecherche;

    @FXML
    private Slider sliderKilometrage, sliderAnneeMin, sliderAnneeMax, sliderPrix;

    @FXML
    private ComboBox comboCarburant, comboVille, comboMarques;

    @FXML
    private ComboBox<String> comboTypeTri;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    private Button btnPagePrecedenteCards, btnPageSuivanteCards, buttonVoirBenchmark;

    @FXML
    private RadioButton radioTransmissionToutes, radioTransmissionAuto, radioTransmissionManuelle;

    private ToggleGroup groupeTransmission;  //Pour fait un toggle entre les radio boutons

    @FXML
    private Label labelSliderKilometrage, labelSliderAnneeMin, labelSliderAnneeMax, labelSliderPrix, labelNumeroPagesCards;

    @FXML
    private DetailsVoitureController detailsVoitureController;

    private final PauseTransition debounce = new PauseTransition(Duration.millis(300));  // Pour éviter de lancer trop rapidement la requête à chaque touche tapée

    @FXML
    public void initialize(){   // On initialise les données et toutes les options de filtrage
        chargerDonneesVoitures();
        pagination = new Pagination(listeVoitures, VOITURES_PAR_PAGE);  // Pour la pagination des cards
        btnPagePrecedenteCards.setOnAction(e -> allerPagePrecedente());
        btnPageSuivanteCards.setOnAction(e -> allerPageSuivante());

        afficherCartes();
        remplirComboMarques();
        comboMarques.setOnAction(e -> appliquerFiltreMultipleEtTri());  // Écouteur d'événement
        remplirComboCarburant();
        comboCarburant.setOnAction(e -> appliquerFiltreMultipleEtTri());
        remplirComboVilles();
        remplirComboTri();
        initialiserSliderKilometraqe();
        initialiserSliderAnneeMin();
        initialiserSliderAnneeMax();
        initialiserSliderPrix();

        debounce.setOnFinished(event -> afficherVoituresCherchees(champRecherche.getText()));
        champRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            debounce.playFromStart();  // Pour écouter les événements du champ de recherche
        });

        groupeTransmission = new ToggleGroup();
        radioTransmissionToutes.setToggleGroup(groupeTransmission);
        radioTransmissionAuto.setToggleGroup(groupeTransmission);
        radioTransmissionManuelle.setToggleGroup(groupeTransmission);

        radioTransmissionToutes.setSelected(true); // Sélection par défaut

        groupeTransmission.selectedToggleProperty().addListener((obs, ancToggle, nouvToggle) -> {
            if (nouvToggle != null) {
                RadioButton boutonSelectionne = (RadioButton) nouvToggle;
                String text = boutonSelectionne.getText();

                Transmission transmission = null;
                if(!"Toutes".equalsIgnoreCase(text)) {
                    try {
                        transmission = Transmission.valueOf(text.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        if (text.toLowerCase().startsWith("auto")) {
                            transmission = Transmission.AUTOMATIQUE;
                        } else if (text.toLowerCase().startsWith("man")) {
                            transmission = Transmission.MANUELLE;
                        }
                    }
                }
                filtrerTransmission(transmission);
            }
        });

    }

    // Une fonction pour charger les données des voitures quand on ouvre la page
    private void chargerDonneesVoitures (){
        if (service != null) {
            this.listeVoitures = service.getVoitures();
        }
    }

    // Une fonction pour remplir le comboBox des différentes marques distinctes
    private void remplirComboMarques() {

        if (listeVoitures == null || listeVoitures.isEmpty()) {
            return;
        }
        List<String> marques = new ArrayList<>(listeVoitures.stream().map(Voiture::getMarque).distinct().toList());

    // On ajoute l'option Toutes pour que le filtre ne s'applique pas
    marques.add(0, "Toutes");

    // On met la liste dans le comboBox
        comboMarques.setItems(FXCollections.observableArrayList(marques));
        comboMarques.getSelectionModel().selectFirst();  // C'est la première option qui s'affiche par défaut
    }

    // Une fonction pour remplir le comboBox des types de carburants
    private void remplirComboCarburant() {
        if (listeVoitures == null || listeVoitures.isEmpty()) {
            return;
        }

        // On transforme les types de carburant de type TypeCarburant en String pour l'affichage dans le ComboBox
        List<String> carburantsString = new ArrayList<>(listeVoitures.stream().map(v -> Objects.toString(v.getCarburant(), "")).filter(s -> !s.isBlank()).distinct().toList());

        // On ajoute l'option Tous pour que le filtre ne s'applique pas
        carburantsString.add(0, "Tous");

        comboCarburant.setItems(FXCollections.observableArrayList(carburantsString));
        comboCarburant.getSelectionModel().selectFirst();  // C'est la première option qui s'affiche par défaut
    }

    // Une fonction pour afficher les villes dans le ComboBox des villes
    private void remplirComboVilles() {
        if (listeVoitures == null || listeVoitures.isEmpty()) {
            return;
        }

        List<String> villes = new ArrayList<>(listeVoitures.stream().map(Voiture::getVille).distinct().toList());

        // On ajoute l'option Toutes pour que le filtre ne s'applique pas
        villes.add(0, "Toutes");

        comboVille.setItems(FXCollections.observableArrayList(villes));
        comboVille.getSelectionModel().selectFirst();  // C'est la première option qui s'affiche par défaut
    }

    // Une fonction pour initialiser le ComboBox des types de tri
    private void remplirComboTri() {
        comboTypeTri.getItems().addAll("Tous", "Kilométrage croissant", "Prix croissant", "Prix décroissant", "Date décroissante");
        comboTypeTri.getSelectionModel().selectFirst();

        comboTypeTri.setOnAction(e -> appliquerFiltreMultipleEtTri());
    }

    // Une fonction pour déterminer les bornes du slider de kilométrage
    private void initialiserSliderKilometraqe() {
        if (listeVoitures == null || listeVoitures.isEmpty()) {
            return;
        }

        sliderKilometrage.setMin(0);

        // Pour obtenir la valeur maximale des kilométrage des véhicules disponibles
        int kiloMax = Collections.max(listeVoitures.stream().map(Voiture::getKilometrage).toList());

        sliderKilometrage.setMax(kiloMax);
        sliderKilometrage.setValue(kiloMax);  // Pour que le curseur soit à droite

        //Pour afficher la valeur en-dessous dans le Label prévu à cet effet
        int kilometrageEntier = (int) sliderKilometrage.getValue();  // Parce que le getValue() retourne normalement un double, on le transforme en int
        labelSliderKilometrage.setText(String.format("%d km", kilometrageEntier));

        // Pour écouter le changement de valeur du slider
        sliderKilometrage.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelSliderKilometrage.setText(String.format("%d km", newVal.intValue()));
        });

        sliderKilometrage.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                appliquerFiltreMultipleEtTri();
            }
        });

        sliderKilometrage.setOnMouseClicked(event -> appliquerFiltreMultipleEtTri());
    }

    // La fonction pour filter selon plusieurs critères et appliquer des tris
    private void appliquerFiltreMultipleEtTri() {
        String marque = (comboMarques.getValue() != null) ? comboMarques.getValue().toString() : null;
        String carburantStr = (comboCarburant.getValue() != null) ? comboCarburant.getValue().toString() : null;
        TypeCarburant carburant = null;
        if (carburantStr != null && !"Tous".equalsIgnoreCase(carburantStr)) {
            try {
                carburant = TypeCarburant.valueOf(carburantStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                carburant = null;
            }
        }
        Integer prixMax = (int) sliderPrix.getValue();
        Integer kmMax = (int) sliderKilometrage.getValue();

        ArrayList<Voiture> filtrees = new ArrayList<>(service.filtrerParCriteres(marque, carburant, prixMax, kmMax));

        triApplique(filtrees);  // On applique s'il y a un tri choisi

        pagination = new Pagination(filtrees, VOITURES_PAR_PAGE);
        afficherCartes();
    }

    // Une fonction pour déterminer les bornes du slider d'année minumum
    private void initialiserSliderAnneeMin() {
        if (listeVoitures == null || listeVoitures.isEmpty()) {
            return;
        }

        int anneeMax = Collections.max(listeVoitures.stream().map(Voiture::getAnnee).toList());
        sliderAnneeMin.setMin(1980);
        sliderAnneeMin.setMax(anneeMax);

        //Pour afficher la valeur en-dessous dans le Label prévu à cet effet
        labelSliderAnneeMin.setText(String.format("%d", (int) sliderAnneeMin.getValue()));

        // Pour écouter le changement de valeur du slider
        sliderAnneeMin.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelSliderAnneeMin.setText(String.format("%d", newVal.intValue()));
        });

        sliderAnneeMin.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                filtrerAnneeMin((int) sliderAnneeMin.getValue());
            }
        });

        sliderAnneeMin.setOnMouseClicked(event -> {
            filtrerAnneeMin((int) sliderAnneeMin.getValue());
        });
    }

    // La fonction pour filtrer selon l'année minimum
    private void filtrerAnneeMin(int anneeMin) {
        ArrayList<Voiture> filtrees = new ArrayList<>(service.filtrerParAnneeMin(anneeMin));
        triApplique(filtrees);  // On applique un tri si un est choisi
        pagination = new Pagination(filtrees, VOITURES_PAR_PAGE);  // On affiche les cartes des voitures filtrées
        afficherCartes();
    }

    // Une fonction pour déterminer les bornes du slider d'année maximum
    private void initialiserSliderAnneeMax() {
        if (listeVoitures == null || listeVoitures.isEmpty()) {
            return;
        }

        int anneeMin = Collections.min(listeVoitures.stream().map(Voiture::getAnnee).toList());
        sliderAnneeMax.setMin(anneeMin);
        int anneeMax = Year.now().getValue();
        sliderAnneeMax.setMax(anneeMax);
        sliderAnneeMax.setValue(anneeMax); // Pour que le curseur soit à droite à l'ouverture

        //Pour afficher la valeur en-dessous dans le Label prévu à cet effet
        labelSliderAnneeMax.setText(String.format("%d", (int) sliderAnneeMax.getValue()));

        // Pour écouter le changement de valeur du slider
        sliderAnneeMax.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelSliderAnneeMax.setText(String.format("%d", newVal.intValue()));
        });

        sliderAnneeMax.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                filtrerAnneeMax((int) sliderAnneeMax.getValue());
            }
        });

        sliderAnneeMax.setOnMouseClicked(event -> {
            filtrerAnneeMax((int) sliderAnneeMax.getValue());
        });
    }

    // La fonction pour filtrer selon l'année maximum
    private void filtrerAnneeMax(int anneeMax) {
        ArrayList<Voiture> filtrees = new ArrayList<>(service.filtrerParAnneeMax(anneeMax));
        triApplique(filtrees);
        pagination = new Pagination(filtrees, VOITURES_PAR_PAGE);  // On affiche les cartes des voitures filtrées
        afficherCartes();
    }

    // Une fonction pour déterminer les bornes du slider de prix
    private void initialiserSliderPrix() {
        if (listeVoitures == null || listeVoitures.isEmpty()) {
            return;
        }

        sliderPrix.setMin(0);

        // Pour obtenir la valeur maximale des prix des véhicules disponibles
        int prixMax = Collections.max(listeVoitures.stream().map(Voiture::getPrix).toList());
        sliderPrix.setMax(prixMax);

        sliderPrix.setValue(prixMax); // Pour que le curseur soit à droite à l'ouverture
        //Pour afficher la valeur en-dessous dans le Label prévu à cet effet
        labelSliderPrix.setText(String.format("%d $", (int) sliderPrix.getValue()));

        // Pour écouter le changement de valeur du slider
        sliderPrix.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelSliderPrix.setText(String.format("%d $", newVal.intValue()));
        });

        // Pour exécuter la requête seulement au relâchement de la souris. (Pour quand on va utiliser une BDD)
        sliderPrix.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                appliquerFiltreMultipleEtTri();
            }
        });

        // Pour quand l'utilisateur clique directement
        sliderPrix.setOnMouseClicked(event -> {
            appliquerFiltreMultipleEtTri();
        });
    }

    // La fonction pour appliquer le filtrage par Type de Transmission
    private void filtrerTransmission(Transmission transmission) {
        List<Voiture> source;
        if (transmission == null) {
            source = listeVoitures;
        } else {
            source = service.filtrerParTransmission(transmission);
        }
        ArrayList<Voiture> filtrees = new ArrayList<>(source);
        triApplique(filtrees);
        pagination = new Pagination(filtrees, VOITURES_PAR_PAGE);
        afficherCartes();
    }

    // La fonction pour afficher les cards de voitures
    private void afficherCartes() {
        cardsContainer.getChildren().clear();

        List<Voiture> voituresPage = pagination.getVoiturePageActuelle();

        for (Voiture voiture : voituresPage) {  // On affiche le nombre de cartes qu'on veut dans une page
            CardVoitureController card = new CardVoitureController(voiture, id -> afficherDetailsVoitures(id));
            cardsContainer.getChildren().add(card);
        }

        labelNumeroPagesCards.setText(pagination.getPageActuelle() + "/" + pagination.getNombrePages()); // Met à jour la page dans le label

        // On met à jour les contrôles de pagination
        btnPagePrecedenteCards.setDisable(pagination.getPageActuelle() <= 1); // On désactive le bouton Précédent si on est sur la page 1
        btnPageSuivanteCards.setDisable(pagination.getPageActuelle() >= pagination.getNombrePages());  // On désactive le bouton Suivant si on est à la dernière page
    }

    // Les fonctions pour les boutons de pagination
    @FXML
    private void allerPagePrecedente() {
        pagination.pagePrecedente();
        afficherCartes();
    }

    @FXML
    private void allerPageSuivante() {
        pagination.pageSuivante();
        afficherCartes();
    }

    // La méthode pour afficher les détails
    private void afficherDetailsVoitures(int idVoiture) {
        Voiture selectionnee = service.trouverParId(idVoiture);
        if (detailsVoitureController != null) {
            detailsVoitureController.afficherVoiture(selectionnee);
        } else {
            System.err.println("Erreur : detailsVoitureController est null");
        }
    }

    // La méthode pour appeler la fontion de recherche avec le TextField
    private void afficherVoituresCherchees(String recherche) {
        ArrayList<Voiture> trouvees = new ArrayList<>(service.rechercher(recherche));  // les tris ont un ArrayList comme paramètre
        triApplique(trouvees);
        pagination = new Pagination(trouvees, VOITURES_PAR_PAGE);
        afficherCartes();
    }

    // Une fonction pour appliquer des tris si c'est demandé  (on utilise trois algorithmes différents)
    private void triApplique(ArrayList<Voiture> listeOrigine) {
        if (listeOrigine == null || listeOrigine.size() <= 1) {
            return;  // Pour empêcher que le tri plante si la liste est vide
        }

        String typeTri = comboTypeTri.getValue();

        if (typeTri == null || "Tous".equals(typeTri)) {
            return;}

        switch (typeTri) {
            case "Kilométrage croissant" -> fusion.ordonner(listeOrigine, VoitureComparateurs.PAR_KM_ASC);  // Pour que le tri soit stable
            case "Prix croissant" -> rapide.ordonner(listeOrigine, VoitureComparateurs.PAR_PRIX_ASC);  // Algorithme avec la meilleure performance
            case "Prix décroissant" -> rapide.ordonner(listeOrigine, VoitureComparateurs.PAR_PRIX_DESC);
            case "Date décroissante" -> insertion.ordonner(listeOrigine, VoitureComparateurs.PAR_DATE_DESC); // Alrgorithme stable (utile puisque plusieurs annonces ont la même date)
            }
        }
    }

