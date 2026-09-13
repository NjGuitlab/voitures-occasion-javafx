package com.cours.controller;

import com.cours.model.Voiture;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.function.Consumer;

public class CardFavoriController extends HBox {

    @FXML
    private Label labelCardMarque, labelCardModele, labelCardAnnee, labelCardDatePublication, labelCardPrix;

    @FXML
    private Button btnSupprimerFavori;

    public CardFavoriController(Voiture voiture, Consumer<Integer> onClicDetails, Runnable onSupprimer) {  // Consumer sert à stocker l'ID pour le bouton
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CardFavoris.fxml"));  // Pour charger le fichier FXML

        loader.setRoot(this);
        loader.setController(this);
        this.getStyleClass().add("card-favoris");

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Erreur de chargement du FXML de la Card", e);
        }

        remplirDonnees(voiture);

        this.setOnMouseClicked(event -> { // Pour afficher les détails quand on clique sur la Card d'un Favori
            if (onClicDetails != null) {
                onClicDetails.accept(voiture.getId());
            }
        });

        this.setStyle(this.getStyle() + "; -fx-cursor: hand;");

        btnSupprimerFavori.setOnAction(event -> {
            event.consume();
            if (onSupprimer != null) {
                onSupprimer.run();
            }
        });
    }

    // La fonction pour remplir les donnees dans la Card
    private void remplirDonnees(Voiture voiture) {
        labelCardMarque.setText(voiture.getMarque());
        labelCardModele.setText(voiture.getModele());
        labelCardAnnee.setText(String.valueOf(voiture.getAnnee()));
        labelCardDatePublication.setText(String.valueOf(voiture.getDatePublication()));
        labelCardPrix.setText(String.valueOf(voiture.getPrix()));
    }
}

