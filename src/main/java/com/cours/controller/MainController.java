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
import java.util.*;

public class MainController {

    // On implémente une instance de Voiture service dans le Controller
    private final VoitureService service = new VoitureService(new LectureCSV());

    private List<Voiture> listeVoitures;

    private TriFusion<Voiture> fusion = new TriFusion<>();
    private TriRapide<Voiture> rapide = new TriRapide<>();
    private TriInsertion<Voiture> insertion = new TriInsertion<>();

    private Pagination pagination;  // On importe l'util pour gérer la pagnination
    private static final int VOITURES_PAR_PAGE = 12;  // Le nombre de cards qu'on veut voir dans chaque page

    @FXML
    private TextField champRecherche;

    @FXML
    private Slider sliderKilometrage, sliderAnneeMin, sliderAnneeMax, sliderPrix;

    @FXML
    private ComboBox comboCarburant, comboMarques;

    @FXML
    private ComboBox<String> comboTypeTri;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    private Button btnPagePrecedenteCards, btnPageSuivanteCards;

    @FXML
    private RadioButton radioTransmissionToutes, radioTransmissionAuto, radioTransmissionManuelle;

    private ToggleGroup groupeTransmission;  //Pour fait un toggle entre les radio boutons

    @FXML
    private Label labelSliderKilometrage, labelSliderAnneeMin, labelSliderAnneeMax, labelSliderPrix, labelNumeroPagesCards;

    @FXML
    private DetailsVoitureController detailsVoitureController;

    @FXML
    private FavorisController zoneFavorisController;

    private final PauseTransition debounce = new PauseTransition(Duration.millis(300));  // Pour éviter de lancer trop rapidement la requête à chaque touche tapée

    @FXML
    public void initialize() {   // On initialise les données et toutes les options de filtrage
        chargerDonneesVoitures();
        pagination = new Pagination(listeVoitures, VOITURES_PAR_PAGE);  // Pour la pagination des cards
        btnPagePrecedenteCards.setOnAction(e -> allerPagePrecedente());
        btnPageSuivanteCards.setOnAction(e -> allerPageSuivante());

        if (detailsVoitureController != null) {
            detailsVoitureController.setVoitureService(service);  // Pour que la zone de détails ait le même service
        }
        if (zoneFavorisController != null) {
            zoneFavorisController.setVoitureService(this.service);  // Pour que la zone de favoris ait le même service
            zoneFavorisController.setOnVoitureSelectionnee(this::afficherDetailsVoitures);
        }
        if (zoneFavorisController != null && detailsVoitureController != null) {
            detailsVoitureController.setOnFavoriAjoute(() -> zoneFavorisController.rafraichirVueFavoris());
        }
        afficherCartes();
        remplirComboBox(listeVoitures.stream().map(Voiture::getMarque).distinct().toList(), comboMarques, "Toutes");
        remplirComboBox(listeVoitures.stream().map(v -> Objects.toString(v.getCarburant(), "")).filter(s -> !s.isBlank()).distinct().toList(), comboCarburant, "Tous");
        comboMarques.setOnAction(e -> appliquerFiltreMultipleEtTri());  // Écouteur d'événement
        comboCarburant.setOnAction(e -> appliquerFiltreMultipleEtTri());
        remplirComboTri();

        int maxKm = listeVoitures.stream().mapToInt(Voiture::getKilometrage).max().orElse(0);
        int maxPrix = listeVoitures.stream().mapToInt(Voiture::getPrix).max().orElse(0);
        int minAnnee = listeVoitures.stream().mapToInt(Voiture::getAnnee).min().orElse(1980);
        int maxAnnee = listeVoitures.stream().mapToInt(Voiture::getAnnee).max().orElseGet(() -> Year.now().getValue());

        // On initialise les sliders avec les valeurs
        initSlider(sliderKilometrage, labelSliderKilometrage, " km", 0, maxKm, maxKm);
        initSlider(sliderPrix, labelSliderPrix, " $", 0, maxPrix, maxPrix);
        initSlider(sliderAnneeMin, labelSliderAnneeMin, "", minAnnee, maxAnnee, minAnnee);
        initSlider(sliderAnneeMax, labelSliderAnneeMax, "", minAnnee, maxAnnee, maxAnnee);

        debounce.setOnFinished(event -> afficherVoituresCherchees(champRecherche.getText()));
        champRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            debounce.playFromStart();  // Pour écouter les événements du champ de recherche
        });

        groupeTransmission = new ToggleGroup();
        radioTransmissionToutes.setToggleGroup(groupeTransmission);
        radioTransmissionAuto.setToggleGroup(groupeTransmission);
        radioTransmissionManuelle.setToggleGroup(groupeTransmission);

        radioTransmissionToutes.setUserData(null);
        radioTransmissionAuto.setUserData(Transmission.AUTOMATIQUE);
        radioTransmissionManuelle.setUserData(Transmission.MANUELLE);

        radioTransmissionToutes.setSelected(true);

        groupeTransmission.selectedToggleProperty().addListener((obs, ancToggle, nouvToggle) -> {
            if (nouvToggle != null) {
                appliquerFiltreMultipleEtTri();
            }
        });
    }
    // Une fonction pour charger les données des voitures quand on ouvre la page
    private void chargerDonneesVoitures (){
        if (service != null) {
            this.listeVoitures = service.getVoitures();
        }
    }
    // Un fonction générique pour remplir les ComboBox
    private void remplirComboBox(List<String> listeElements, ComboBox<String> combo, String generale) {
        if (combo == null) {
            return;
        }
        List<String> items = new ArrayList<>();
        if (generale != null && !generale.isBlank()) {
            items.add(generale);
        }
        if (listeElements != null && !listeElements.isEmpty()) {
            items.addAll(listeElements);
        }
        combo.setItems(FXCollections.observableArrayList(items));
        combo.getSelectionModel().selectFirst();
    }
    // Une fonction pour initialiser le ComboBox des types de tri
    private void remplirComboTri() {
        comboTypeTri.getItems().addAll("Tous", "Kilométrage croissant", "Prix croissant", "Prix décroissant", "Date décroissante");
        comboTypeTri.getSelectionModel().selectFirst();

        comboTypeTri.setOnAction(e -> appliquerFiltreMultipleEtTri());
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
        Transmission transmission = (groupeTransmission.getSelectedToggle() != null)
                ? (Transmission) groupeTransmission.getSelectedToggle().getUserData()
                : null;

        int prixMax = (int) sliderPrix.getValue();
        int kmMax = (int) sliderKilometrage.getValue();
        int anneeMin = (int) sliderAnneeMin.getValue();
        int anneeMax = (int) sliderAnneeMax.getValue();

        ArrayList<Voiture> filtrees = new ArrayList<>(service.filtrerParCriteres(marque, carburant, prixMax, kmMax, anneeMin, anneeMax, transmission));

        triApplique(filtrees);  // On applique s'il y a un tri choisi

        pagination = new Pagination(filtrees, VOITURES_PAR_PAGE);
        afficherCartes();
    }
    // Une fonction pour initialiser tous les sliders
    private void initSlider(Slider slider, Label label, String suffixe, double min, double max, double valDefaut) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(valDefaut);
        configurerSlider(slider, label, suffixe, this::appliquerFiltreMultipleEtTri);
    }
    // Une fonction pour configurer tous les sliders
    private void configurerSlider(Slider slider, Label label, String suffixe, Runnable onAction) {
        //Pour afficher la valeur en-dessous dans le Label prévu à cet effet
        label.setText(String.format("%d" + suffixe, (int) slider.getValue())); // Parce que le getValue() retourne normalement un double, on le transforme en int

        // Pour écouter le changement de valeur du slider
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            label.setText(newVal.intValue() + " " + suffixe);
        });
        slider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                onAction.run();
            }
        });
        slider.setOnMouseClicked(event -> {
            onAction.run();
        });
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
            case "Kilométrage croissant" -> fusion.ordonner(listeOrigine, VoitureComparateurs.PAR_KM_ASC);
            case "Prix croissant" -> rapide.ordonner(listeOrigine, VoitureComparateurs.PAR_PRIX_ASC);
            case "Prix décroissant" -> rapide.ordonner(listeOrigine, VoitureComparateurs.PAR_PRIX_DESC);
            case "Date décroissante" -> insertion.ordonner(listeOrigine, VoitureComparateurs.PAR_DATE_DESC);
            }
        }
    }

