package com.cours.controller;

import com.cours.controller.formulaires.FormModifierVoitureController;
import com.cours.model.Voiture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class CardVoitureController extends VBox {

    @FXML
    private Label labelCardMarque, labelCardModele, labelCardAnnee, labelCardKilometrage, labelCardPrix, labelCardVille, labelCardDatePublication;

    @FXML
    private Button btnVoirDetails, btnModifierVoiture;

    private Voiture vehicule;

    private static final java.text.DecimalFormat FORMAT_NOMBRE = new java.text.DecimalFormat("#,###");  // Pour avoir un beau formatage pour les chiffres

    public CardVoitureController(Voiture voiture, Consumer<Integer> onVoirDetails) {// Consumer sert à stocker l'ID pour le bouton

        vehicule = voiture;
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

        btnModifierVoiture.setOnAction(this::ouvrirFormModifierVoiture);
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

        @FXML
        private void ouvrirFormModifierVoiture(ActionEvent e) {
            try {

                FormModifierVoitureController formulaireModifier = new FormModifierVoitureController(vehicule);

                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/fxml/formulaires/formModifierVoiture.fxml"));
                loader.setController(formulaireModifier);
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Modifier une annonce");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

            } catch (Exception error) {
                error.printStackTrace();
            }

        }

    }

