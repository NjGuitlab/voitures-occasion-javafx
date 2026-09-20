package com.cours.controller.formulaires;
import com.cours.model.Transmission;
import com.cours.model.TypeCarburant;
import com.cours.model.TypeVendeur;
import com.cours.model.Voiture;
import com.cours.service.VoitureService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.Year;

public class FormModifierVoitureController {
    @FXML
    private Label labelMarque, labelModele, labelVille, labelDatePub;
    @FXML
    private Spinner<Integer> spinnerPrix, spinnerKilos, spinnerAnnee;
    @FXML
    private ComboBox comboTransmission, comboCarburant, comboVendeur, comboCouleur;
    @FXML
    private TextArea textDescription;
    @FXML
    private Button btnModifier, btnAnnuler;

    private Voiture voiture;

    private ObservableList<String> couleursVoitures = FXCollections.observableArrayList(
            "Argent","Bordeaux","Beige","Blanc","Bleu","Brun","Gris","Noir","Rouge","Vert"
    );

    private VoitureService service;

    private Runnable rafraichirUI;

    public FormModifierVoitureController(Voiture v, VoitureService serv) {
        voiture = v;
        service = serv;
    }

    public void initialize() {
        labelMarque.setText(voiture.getMarque());
        labelModele.setText(voiture.getModele());
        labelVille.setText(voiture.getVille());
        labelDatePub.setText(voiture.getDatePublication().toString());

        // Validation de saisie dans les spinners
        spinnerPrix.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (newText.matches("\\d*") && Integer.parseInt(newText) <= 1000000) {
                return change;
            }

            return null;
        }));

        spinnerAnnee.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (newText.matches("\\d*") && Integer.parseInt(newText) <= Year.now().getValue()) {
                return change;
            }

            return null;
        }));

        spinnerKilos.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (newText.matches("\\d*") && Integer.parseInt(newText) <= 1000000) {
                return change;
            }

            return null;
        }));



        SpinnerValueFactory<Integer> spinnerPrixFactory = new SpinnerValueFactory.
                IntegerSpinnerValueFactory(1, 1000000, voiture.getPrix());
        SpinnerValueFactory<Integer> spinnerKilosFactory = new SpinnerValueFactory.
                IntegerSpinnerValueFactory(0, 1000000, voiture.getKilometrage());
        SpinnerValueFactory<Integer> spinnerAnneeFactory = new SpinnerValueFactory.
                IntegerSpinnerValueFactory(1900, Year.now().getValue(), voiture.getAnnee());

        spinnerPrix.setValueFactory(spinnerPrixFactory);
        spinnerKilos.setValueFactory(spinnerKilosFactory);
        spinnerAnnee.setValueFactory(spinnerAnneeFactory);

        comboTransmission.setItems(FXCollections.observableArrayList(Transmission.values()));
        comboVendeur.setItems(FXCollections.observableArrayList(TypeVendeur.values()));
        comboCouleur.setItems(couleursVoitures);
        comboCarburant.setItems(FXCollections.observableArrayList(TypeCarburant.values()));

        comboTransmission.setValue(voiture.getTransmission());
        comboCarburant.setValue(voiture.getCarburant());
        comboCouleur.setValue(voiture.getCouleur());
        comboVendeur.setValue(voiture.getTypeVendeur());

        textDescription.setText(voiture.getDescription());

        // Actions
        btnAnnuler.setOnAction(this::fermerForm);
        btnModifier.setOnAction(this::sauvegarderModification);
    }

    public void recevoirFnRafraichirUI(Runnable fonction) {
        this.rafraichirUI = fonction;
    }

    private void fermerForm(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void sauvegarderModification(ActionEvent event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        int id = voiture.getId();
        String marque = voiture.getMarque();
        String modele = voiture.getModele();
        String ville = voiture.getVille();
        LocalDate datePub = voiture.getDatePublication();
        int prix = spinnerPrix.getValue();
        int annee = spinnerAnnee.getValue();
        int kilos = spinnerKilos.getValue();
        Transmission transmission = Transmission.valueOf(comboTransmission.getValue().toString());
        TypeCarburant carburant = TypeCarburant.valueOf(comboCarburant.getValue().toString());
        TypeVendeur vendeur = TypeVendeur.valueOf(comboVendeur.getValue().toString());
        String couleur = comboCouleur.getValue().toString();
        String desc = textDescription.getText();

        Thread modifierAnnonce = new Thread(() -> {

            try {
                service.modifier(new Voiture(
                        id, marque, modele, annee, kilos, prix,
                        carburant, transmission, couleur, ville, vendeur, datePub, desc
                ));

                Platform.runLater(() ->{
                    rafraichirUI.run();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Voiture modifiée!");
                    alert.showAndWait();
                    stage.close();
                });

            } catch(Exception erreur) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur d'application");
                alert.setContentText("Une erreur est survenue: " + erreur.getMessage());
                alert.showAndWait();

            }
        });
        modifierAnnonce.setDaemon(true);
        modifierAnnonce.start();

    }
}
