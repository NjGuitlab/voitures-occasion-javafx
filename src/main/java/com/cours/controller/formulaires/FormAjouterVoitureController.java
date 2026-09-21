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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Contrôleur pour le formulaire d'ajout d'une nouvelle voiture.
 */
public class FormAjouterVoitureController {
    @FXML
    private ComboBox comboMarque, comboModele, comboTransmission, comboCarburant, comboVendeur, comboCouleur;
    @FXML
    private TextField textVille;
    @FXML
    private Spinner<Integer> spinnerPrix, spinnerKilos, spinnerAnnee;
    @FXML
    private TextArea textDescription;
    @FXML
    private Button btnAjouter, btnAnnuler;

    private ObservableList<String> couleursVoitures = FXCollections.observableArrayList(
            "Argent","Bordeaux","Beige","Blanc","Bleu","Brun","Gris","Noir","Rouge","Vert"
    );

    final int PRIX_MAX = 1000000;
    final int PRIX_MIN = 1;
    final int ANNEE_MAX = Year.now().getValue();
    final int ANNEE_MIN = 1900;
    final int KILOS_MAX = 1000000;
    final int KILOS_MIN = 0;

    private final Map<String, List<String>> modelesVoitures = new HashMap<>();

    private VoitureService service;

    private Runnable rafraichirUI;

    /**
     * Initialise le contrôleur avec le service de gestion des voitures.
     *
     * @param serv Le service d'accès aux données des voitures.
     */
    public FormAjouterVoitureController(VoitureService serv) {
        this.service = serv;

        modelesVoitures.put("Tesla", new ArrayList<>(List.of("Model 3", "Model Y", "Model S", "Model X")));
        modelesVoitures.put("Porsche", new ArrayList<>(List.of("Cayenne", "Macan", "Taycan", "Panamera")));
        modelesVoitures.put("Ford", new ArrayList<>(List.of("Mustang", "F-150", "Explorer", "Escape")));
        modelesVoitures.put("Volvo", new ArrayList<>(List.of("XC90", "XC60", "S60", "V60")));
        modelesVoitures.put("Mazda", new ArrayList<>(List.of("Mazda3", "CX-5", "CX-90", "MX-5 Miata")));
        modelesVoitures.put("Honda", new ArrayList<>(List.of("Civic", "Accord", "CR-V", "Pilot")));
        modelesVoitures.put("BMW", new ArrayList<>(List.of("3 Series", "5 Series", "X5", "M4")));
        modelesVoitures.put("Hyundai", new ArrayList<>(List.of("Elantra", "Sonata", "Tucson", "Ioniq 5")));
        modelesVoitures.put("Nissan", new ArrayList<>(List.of("Altima", "Sentra", "Rogue", "GT-R")));
        modelesVoitures.put("Jeep", new ArrayList<>(List.of("Wrangler", "Grand Cherokee", "Compass", "Gladiator")));
        modelesVoitures.put("Kia", new ArrayList<>(List.of("Forte", "Sportage", "Telluride", "EV6")));
        modelesVoitures.put("Chrysler", new ArrayList<>(List.of("Pacifica", "300", "Voyager")));
        modelesVoitures.put("Audi", new ArrayList<>(List.of("A4", "A6", "Q5", "e-tron")));
        modelesVoitures.put("Chevrolet", new ArrayList<>(List.of("Silverado", "Malibu", "Equinox", "Corvette")));
        modelesVoitures.put("Mercedes-Benz", new ArrayList<>(List.of("C-Class", "E-Class", "S-Class", "GLE")));
        modelesVoitures.put("Volkswagen", new ArrayList<>(List.of("Golf", "Jetta", "Tiguan", "ID.4")));
        modelesVoitures.put("Infiniti", new ArrayList<>(List.of("Q50", "QX50", "QX60", "QX80")));
        modelesVoitures.put("GMC", new ArrayList<>(List.of("Sierra", "Yukon", "Acadia", "Terrain")));
        modelesVoitures.put("Toyota", new ArrayList<>(List.of("Camry", "Corolla", "RAV4", "Prius")));
        modelesVoitures.put("Maserati", new ArrayList<>(List.of("Ghibli", "Levante", "Quattroporte", "MC20")));
        modelesVoitures.put("Subaru", new ArrayList<>(List.of("Impreza", "Outback", "Forester", "WRX")));
        modelesVoitures.put("Buick", new ArrayList<>(List.of("Encore", "Envision", "Enclave")));
        modelesVoitures.put("Dodge", new ArrayList<>(List.of("Charger", "Challenger", "Durango", "Hornet")));
        modelesVoitures.put("MINI", new ArrayList<>(List.of("Cooper", "Countryman", "Clubman")));
        modelesVoitures.put("Mitsubishi", new ArrayList<>(List.of("Outlander", "Eclipse Cross", "Mirage")));
        modelesVoitures.put("Cadillac", new ArrayList<>(List.of("Escalade", "CT5", "XT5", "Lyriq")));
        modelesVoitures.put("Jaguar", new ArrayList<>(List.of("F-TYPE", "F-PACE", "I-PACE", "XF")));
        modelesVoitures.put("Alfa Romeo", new ArrayList<>(List.of("Giulia", "Stelvio", "Tonale")));
        modelesVoitures.put("Acura", new ArrayList<>(List.of("TLX", "Integra", "MDX", "RDX")));
        modelesVoitures.put("Isuzu", new ArrayList<>(List.of("D-Max", "MU-X")));
    }

    /**
     * Initialise les composants de l'interface, les filtres de saisie et les événements.
     */
    public void initialize() {

        comboMarque.setItems(FXCollections.observableArrayList(service.getMarques()).sorted());
        comboMarque.getSelectionModel().selectedItemProperty().addListener(
                ((observable, o, n) -> {
                    if (n != null && modelesVoitures.containsKey(n)) {
                        comboModele.getItems().clear();
                        comboModele.getItems().addAll(modelesVoitures.get(n));
                        comboModele.getSelectionModel().clearSelection();
                    } else {
                        comboModele.getItems().clear();
                    }
                }
                ));

        spinnerPrix.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (newText.matches("\\d*") && Integer.parseInt(newText) <= PRIX_MAX) {
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

            if (newText.matches("\\d*") && Integer.parseInt(newText) <= KILOS_MAX) {
                return change;
            }

            return null;
        }));

        SpinnerValueFactory<Integer> spinnerPrixFactory = new SpinnerValueFactory.
                IntegerSpinnerValueFactory(PRIX_MIN, PRIX_MAX, 0);
        SpinnerValueFactory<Integer> spinnerKilosFactory = new SpinnerValueFactory.
                IntegerSpinnerValueFactory(KILOS_MIN, KILOS_MAX, 0);
        SpinnerValueFactory<Integer> spinnerAnneeFactory = new SpinnerValueFactory.
                IntegerSpinnerValueFactory(ANNEE_MIN, ANNEE_MAX, ANNEE_MIN);

        spinnerPrix.setValueFactory(spinnerPrixFactory);
        spinnerKilos.setValueFactory(spinnerKilosFactory);
        spinnerAnnee.setValueFactory(spinnerAnneeFactory);

        textVille.setTextFormatter(new TextFormatter<>(change -> {

            if (change.getControlNewText().matches("[a-zA-Z-]*")) {
                return change;
            }
            return null;
        }));

        comboTransmission.setItems(FXCollections.observableArrayList(Transmission.values()));
        comboVendeur.setItems(FXCollections.observableArrayList(TypeVendeur.values()));
        comboCouleur.setItems(couleursVoitures);
        comboCarburant.setItems(FXCollections.observableArrayList(TypeCarburant.values()));

        //Actions
        btnAnnuler.setOnAction(this::fermerForm);
        btnAjouter.setOnAction(this::sauvegarderAjout);

    }

    /**
     * Reçoit la fonction de rappel permettant de rafraîchir l'interface principale.
     *
     * @param fonction Action à exécuter pour mettre à jour l'UI.
     */
    public void recevoirFnRafraichirUI(Runnable fonction) {
        this.rafraichirUI = fonction;
    }

    /**
     * Ferme la fenêtre active du formulaire.
     *
     * @param e L'événement déclencheur.
     */
    private void fermerForm(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Valide le formulaire et enregistre la nouvelle voiture de manière asynchrone.
     *
     * @param e L'événement déclencheur.
     */
    private void sauvegarderAjout(ActionEvent e) {

        // Validations de saisies
        if (comboMarque.getSelectionModel().getSelectedItem() == null
                || comboModele.getSelectionModel().getSelectedItem() == null
                || comboTransmission.getSelectionModel().getSelectedItem() == null
                || comboCarburant.getSelectionModel().getSelectedItem() == null
                || comboCouleur.getSelectionModel().getSelectedItem() == null
                || comboVendeur.getSelectionModel().getSelectedItem() == null
                || textDescription.getText() == null
                || textDescription.getText().trim().isEmpty()
                || spinnerAnnee.getEditor().getText() == null
                || spinnerAnnee.getEditor().getText().trim().isEmpty()
                || spinnerPrix.getEditor().getText() == null
                || spinnerPrix.getEditor().getText().trim().isEmpty()
                || spinnerKilos.getEditor().getText() == null
                || spinnerKilos.getEditor().getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText("Saisie formulaire invalide");
            alert.setContentText("Veuillez confirmer que le formulaire est complet.");
            alert.showAndWait();
            return;
        }

        Voiture v = new Voiture(
                999, comboMarque.getValue().toString(), comboModele.getValue().toString(), spinnerAnnee.getValue(),
                spinnerKilos.getValue(), spinnerPrix.getValue(),
                TypeCarburant.valueOf(comboCarburant.getValue().toString()),
                Transmission.valueOf(comboTransmission.getValue().toString()), comboCouleur.getValue().toString(),
                textVille.getText(), TypeVendeur.valueOf(comboVendeur.getValue().toString()), LocalDate.now(),
                textDescription.getText()
        );

        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        Thread ajouterVoiture = new Thread(()->{
            try {
                service.ajouter(v);
                Platform.runLater(() -> {
                    rafraichirUI.run();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Opération");
                    alert.setHeaderText("Ajout de voiture");
                    alert.setContentText("Voiture ajoutée!");
                    alert.showAndWait();
                    stage.close();
                });

            } catch (Exception erreur) {
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur d'application");
                    alert.setContentText("Une erreur est survenue: " + erreur.getMessage());
                    alert.showAndWait();
                    stage.close();
                });
            }
        });

        ajouterVoiture.setDaemon(true);
        ajouterVoiture.start();
    }
}
