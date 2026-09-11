package com.cours.controller;

import com.cours.model.TypeCarburant;
import com.cours.model.Voiture;
import com.cours.service.VoitureService;
import com.cours.util.LectureCSV;
import com.cours.util.Pagination;
import com.cours.util.SourceDonnees;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainController {

    // On implémente une instance de Voiture service dans le Controller
    private final VoitureService service = new VoitureService(new LectureCSV());

    private List<Voiture> listeVoitures;

    private Pagination pagination;  // On importe l'util pour gérer la pagnination
    private static final int VOITURES_PAR_PAGE = 12;  // Le nombre de cards qu'on veut voir dans chaque page

    public MainController(){}

    @FXML
    private TextField champRecherche;

    @FXML
    private Slider sliderKilometrage, sliderAnnee, sliderPrix;

    @FXML
    private ComboBox comboCarburant, comboVille, comboTypeTri, comboMarques;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    private Button btnPagePrecedenteCards, btnPageSuivanteCards, buttonVoirBenchmark, btnPagePrecedenteFavoris, btnPageSuivanteFavoris;

    @FXML
    private RadioButton radioTransmissionToutes, radioTransmissionAuto, radioTransmissionManuelle;

    @FXML
    private Label labelSliderKilometrage, labelSliderAnnee, labelSliderPrix;

    @FXML
    private DetailsVoitureController detailsVoitureController;

    @FXML
    public void initialize(){   // On initialise les données et toutes les options de filtrage
        chargerDonneesVoitures();
        pagination = new Pagination(listeVoitures, VOITURES_PAR_PAGE);  // Pour la pagination des cards
        btnPagePrecedenteCards.setOnAction(e -> allerPagePrecedente());
        btnPageSuivanteCards.setOnAction(e -> allerPageSuivante());

        afficherCartes();
        remplirComboMarques();
        remplirComboCarburant();
        remplirComboVilles();
        remplirComboTri();
        initialiserSliderKilometraqe();
        initialiserSliderAnnee();
        initialiserSliderPrix();
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

        //Pour afficher la valeur en-dessous dans le Label prévu à cet effet
        int kilometrageEntier = (int) sliderKilometrage.getValue();  // Parce que le getValue() retourne normalement un double, on le transforme en int
        labelSliderKilometrage.setText(String.format("%d km", kilometrageEntier));

        // Pour écouter le changement de valeur du slider
        sliderKilometrage.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelSliderKilometrage.setText(String.format("%d km", newVal.intValue()));
        });
    }

    // Une fonction pour déterminer les bornes du slider d'année
    private void initialiserSliderAnnee() {
        if (listeVoitures == null || listeVoitures.isEmpty()) {
            return;
        }

        sliderAnnee.setMin(1940);
        sliderAnnee.setMax(Year.now().getValue());

        //Pour afficher la valeur en-dessous dans le Label prévu à cet effet
        labelSliderAnnee.setText(String.format("%d", (int) sliderAnnee.getValue()));

        // Pour écouter le changement de valeur du slider
        sliderAnnee.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelSliderAnnee.setText(String.format("%d", newVal.intValue()));
        });
    }

    // Une fonction pour déterminer les bornes du slider d'année
    private void initialiserSliderPrix() {
        if (listeVoitures == null || listeVoitures.isEmpty()) {
            return;
        }

        sliderPrix.setMin(0);

        // Pour obtenir la valeur maximale des prix des véhicules disponibles
        int prixMax = Collections.max(listeVoitures.stream().map(Voiture::getPrix).toList());
        sliderPrix.setMax(prixMax);

        //Pour afficher la valeur en-dessous dans le Label prévu à cet effet
        labelSliderPrix.setText(String.format("%d $", (int) sliderPrix.getValue()));

        // Pour écouter le changement de valeur du slider
        sliderPrix.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelSliderPrix.setText(String.format("%d $", newVal.intValue()));
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

}
