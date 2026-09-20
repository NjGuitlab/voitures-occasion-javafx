
package com.cours.controller;

import com.cours.controller.formulaires.FormModifierVoitureController;
import com.cours.model.Voiture;
import com.cours.service.VoitureService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class CardVoitureController extends VBox {

    @FXML
    private Label labelCardMarque, labelCardModele, labelCardAnnee, labelCardKilometrage, labelCardPrix, labelCardVille, labelCardDatePublication;

    @FXML
    private Button btnVoirDetails, btnModifierVoiture, btnSupprimerVoiture;

    private Voiture vehicule;

    private VoitureService service;

    private static final java.text.DecimalFormat FORMAT_NOMBRE = new java.text.DecimalFormat("#,###");  // Pour avoir un beau formatage pour les chiffres

    private Runnable rafraichirUI;

    public CardVoitureController(Voiture voiture, VoitureService serv, Consumer<Integer> onVoirDetails) {// Consumer sert à stocker l'ID pour le bouton

        vehicule = voiture;
        service = serv;
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
        btnSupprimerVoiture.setOnAction(this::supprimerVoiture);
    }

    /**
     * Reçoit la fonction de rappel permettant de rafraîchir l'interface principale.
     *
     * @param fonction Action à exécuter pour mettre à jour l'UI.
     */
    public void recevoirFnRafraichirUI(Runnable fonction) {
        this.rafraichirUI = fonction;
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

    /**
     * Charge et affiche la fenêtre modale pour modifier les informations de la voiture.
     *
     * @param e L'événement déclencheur.
     */
    @FXML
    private void ouvrirFormModifierVoiture(ActionEvent e) {

        FormModifierVoitureController formulaireModifier = new FormModifierVoitureController(vehicule, service);
        formulaireModifier.recevoirFnRafraichirUI(rafraichirUI);

        Thread chargerFichier = new Thread(()->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/fxml/formulaires/formModifierVoiture.fxml"));

                loader.setController(formulaireModifier);
                Parent root = loader.load();

                Platform.runLater(()->{
                    Stage stage = new Stage();
                    stage.setTitle("Modifier une annonce");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                });

            } catch (Exception erreur) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors du chargement d'un formulaire: " + erreur.getMessage());
                alert.showAndWait();
            }
        });

        chargerFichier.setDaemon(true);
        chargerFichier.start();
    }

    private void supprimerVoiture(ActionEvent e) {

        Alert suppression = new Alert(Alert.AlertType.WARNING);
        suppression.setTitle("Confirmer suppression");
        suppression.setHeaderText("Suppression de voiture");
        suppression.setContentText("Voulez-vous vraiment compléter cette opération?");

        Optional<ButtonType> result = suppression.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            Thread suppressionVoiture = new Thread(()-> {
                try {
                    service.supprimer(vehicule.getId());
                    Platform.runLater(() -> {
                        rafraichirUI.run();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Voiture supprimée!");
                        alert.showAndWait();
                    });

                } catch (Exception error) {
                    Platform.runLater(()-> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Erreur lors de la suppression: " + error.getMessage());
                        alert.showAndWait();
                    });
                }
            });
            suppressionVoiture.setDaemon(true);
            suppressionVoiture.start();

        }
    }

}
