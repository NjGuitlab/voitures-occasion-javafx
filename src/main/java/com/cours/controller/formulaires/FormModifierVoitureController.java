package com.cours.controller.formulaires;

import com.cours.dao.VoiturePostgreSQLDAO;
import com.cours.model.Transmission;
import com.cours.model.TypeCarburant;
import com.cours.model.TypeVendeur;
import com.cours.model.Voiture;
import com.cours.service.VoitureService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.Year;

public class FormModifierVoitureController {
    @FXML
    private Label labelMarque, labelModele, labelVille, labelDatePub;
    @FXML
    private Spinner spinnerPrix, spinnerKilos, spinnerAnnee;
    @FXML
    private ComboBox comboTransmission, comboCarburant, comboVendeur, comboCouleur;
    @FXML
    private TextArea textDescription;

    private Voiture voiture;

    private ObservableList<String> couleursVoitures = FXCollections.observableArrayList(
            "Argent","Bordeaux","Beige","Blanc","Bleu","Brun","Gris","Noir","Rouge","Vert"
    );

    private VoitureService service = new VoitureService(new VoiturePostgreSQLDAO());

    public FormModifierVoitureController(Voiture v) {
        voiture = v;
    }

    public void initialize() {
        labelMarque.setText(voiture.getMarque());
        labelModele.setText(voiture.getModele());
        labelVille.setText(voiture.getVille());
        labelDatePub.setText(voiture.getDatePublication().toString());

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

    }
}
