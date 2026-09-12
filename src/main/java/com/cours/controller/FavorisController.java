package com.cours.controller;

import com.cours.algorithmes.TriInsertion;
import com.cours.model.Voiture;
import com.cours.service.VoitureService;
import com.cours.util.LectureCSV;
import com.cours.util.Pagination;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class FavorisController {

    private VoitureService service;

    private TriInsertion<Voiture> insertion = new TriInsertion<>();

    private Pagination pagination;  // On importe l'util pour gérer la pagnination
    private static final int FAVORIS_PAR_PAGE = 10;

    private List<Voiture> listeFavoris;

    public FavorisController(){}

    @FXML
    private VBox racineFavoris;  // La zone d'affichage des favoris

    @FXML
    private Button buttonVoirBenchmark, btnPagePrecedenteFavoris, btnPageSuivanteFavoris;

    @FXML
    private FlowPane favorisCardsContainer;

    @FXML
    private Label labelNombrePagesFavoris;

    @FXML
    public void initialize() {
        btnPagePrecedenteFavoris.setOnAction(e -> allerPagePrecedente());
        btnPageSuivanteFavoris.setOnAction(e -> allerPageSuivante());
    }

    //Pour utiliser le même VoitureService que dans MainController
    public void setVoitureService(VoitureService service) {
        this.service = service;
        rafraichirVueFavoris();
    }

    public void rafraichirVueFavoris() {

        if(service == null) {
            return;
        }
        this.listeFavoris = service.getFavoris();
        if (this.listeFavoris == null) {
            this.listeFavoris = new ArrayList<>();
        }

        pagination = new Pagination(listeFavoris, FAVORIS_PAR_PAGE); // pour la pagination des cards

        afficherCartesFavoris();
    }

    // La fonction pour afficher les cards de voitures
    private void afficherCartesFavoris() {
        favorisCardsContainer.getChildren().clear();

        if (pagination == null || listeFavoris.isEmpty()) {
            labelNombrePagesFavoris.setText("0/0");
            btnPagePrecedenteFavoris.setDisable(true);
            btnPageSuivanteFavoris.setDisable(true);
            return;
        }

        List<Voiture> favorisPage = pagination.getVoiturePageActuelle();

        for (Voiture voiture : favorisPage) {  // On affiche le nombre de cartes qu'on veut dans une page
            CardFavoriController card = new CardFavoriController(voiture, id -> {
                service.supprimerFavori(id);
                rafraichirVueFavoris();
            });
            favorisCardsContainer.getChildren().add(card);
        }

        labelNombrePagesFavoris.setText(pagination.getPageActuelle() + "/" + pagination.getNombrePages()); // Met à jour la page dans le label

        // On met à jour les contrôles de pagination
        btnPagePrecedenteFavoris.setDisable(pagination.getPageActuelle() <= 1); // On désactive le bouton Précédent si on est sur la page 1
        btnPageSuivanteFavoris.setDisable(pagination.getPageActuelle() >= pagination.getNombrePages());  // On désactive le bouton Suivant si on est à la dernière page
    }

    // Les fonctions pour les boutons de pagination
    @FXML
    private void allerPagePrecedente() {
        if (pagination != null) {
            pagination.pagePrecedente();
            afficherCartesFavoris();
        }
    }

    @FXML
    private void allerPageSuivante() {
        if (pagination != null) {
            pagination.pageSuivante();
            afficherCartesFavoris();
        }
    }
}
