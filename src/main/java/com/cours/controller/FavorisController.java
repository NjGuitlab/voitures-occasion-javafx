package com.cours.controller;

import com.cours.algorithmes.TriInsertion;
import com.cours.model.Voiture;
import com.cours.service.VoitureService;
import com.cours.util.LectureCSV;
import com.cours.util.Pagination;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ConstrainedColumnResizeBase;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
    private Runnable onDemandeOuvertureBenchmark;  // Pour ouvrir la fenêtre des benchmark

    @FXML
    private FlowPane favorisCardsContainer;

    @FXML
    private Label labelNombrePagesFavoris;

    private Consumer<Integer> onVoitureSelectionnee;

    public void setOnVoitureSelectionnee(Consumer<Integer> onVoitureSelectionnee) {
        this.onVoitureSelectionnee = onVoitureSelectionnee;
    }

    @FXML
    public void initialize() {
        btnPagePrecedenteFavoris.setOnAction(e -> allerPagePrecedente());
        btnPageSuivanteFavoris.setOnAction(e -> allerPageSuivante());

        buttonVoirBenchmark.setOnAction(e -> ouvrirFenetreBenchmark(e));
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
                if (onVoitureSelectionnee != null) {
                    onVoitureSelectionnee.accept(id);
                }
            },
                    () -> {
                service.supprimerFavori(voiture.getId());
                rafraichirVueFavoris();  // Ça rafraichit la zone de favoris quand on en supprimer un
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

    // Fonction pour lancer la fenêtre de benchmark
    @FXML
    private void ouvrirFenetreBenchmark(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/benchmark.fxml"));
            Parent root = loader.load();

            Stage stageBenchmark = new Stage();
            stageBenchmark.setTitle("Comparaison des Algorithmes de Tri");
            stageBenchmark.setScene(new Scene(root));

            Stage mainFenetre = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageBenchmark.initOwner(mainFenetre);
            stageBenchmark.initModality(Modality.APPLICATION_MODAL);

            stageBenchmark.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
