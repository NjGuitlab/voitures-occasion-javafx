package com.cours.controller;

import com.cours.model.Voiture;
import com.cours.service.VoitureService;
import com.cours.util.LectureCSV;
import com.cours.util.SourceDonnees;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainController {

    // On implémente une instance de Voiture service dans le Controller
    private final VoitureService service = new VoitureService(new LectureCSV());

    private List<Voiture> listeVoitures;

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
    public void initialize(){

        if (service != null) {
            this.listeVoitures = service.getVoitures();
        }
        // Pour mettre toutes les marques distinctes dans le ComboBox des marques
        comboMarques.setItems(FXCollections.observableArrayList(listeVoitures.stream().map(Voiture::getMarque).distinct().toList()));
    }



}
