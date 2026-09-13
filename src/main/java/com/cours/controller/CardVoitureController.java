package com.cours.controller;

import com.cours.model.Voiture;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.Console;
import java.io.IOException;
import java.util.function.Consumer;

public class CardVoitureController extends VBox {

    @FXML
    private Label labelCardMarque, labelCardModele, labelCardAnnee, labelCardKilometrage, labelCardPrix, labelCardVille, labelCardDatePublication;

    @FXML
    private Button btnVoirDetails;

    private static final java.text.DecimalFormat FORMAT_NOMBRE = new java.text.DecimalFormat("#,###");  // Pour avoir un beau formatage pour les chiffres

    public CardVoitureController(Voiture voiture, Consumer<Integer> onVoirDetails) {  // Consumer sert à stocker l'ID pour le bouton
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CardVoiture.fxml"));  // Pour charger le fichier FXML

        loader.setRoot(this);
        loader.setController(this);
        this.getStyleClass().add("card-voiture");

        try {
            loader.load();
            this.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        } catch (IOException e) {
            throw new RuntimeException("Erreur de chargement du FXML de la Card", e);
        }

        remplirDonnees(voiture);

        btnVoirDetails.setOnAction(event -> {
            if (onVoirDetails != null) {
                onVoirDetails.accept(voiture.getId());
            }
        });
    }

        // La fonction pour remplir les donnees dans la Card
        private void remplirDonnees(Voiture voiture) {
            labelCardMarque.setText(voiture.getMarque());
            labelCardModele.setText(voiture.getModele());
            labelCardAnnee.setText(String.valueOf(voiture.getAnnee()));

            labelCardKilometrage.setText(FORMAT_NOMBRE.format(voiture.getKilometrage()) + " km");
            labelCardDatePublication.setText(voiture.getDatePublication().toString());
            labelCardPrix.setText(FORMAT_NOMBRE.format(voiture.getPrix()) + " $");
            labelCardVille.setText(voiture.getVille());
        }

    }

