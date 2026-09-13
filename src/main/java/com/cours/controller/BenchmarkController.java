package com.cours.controller;

import com.cours.algorithmes.Algorithme;
import com.cours.algorithmes.reference.Chrono;
import com.cours.algorithmes.reference.util.GenerateurDonneesVoiture;
import com.cours.model.Transmission;
import com.cours.model.Voiture;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BenchmarkController {

    @FXML
    private CheckBox chkTriInsertion, chkFusion, chkRapide;

    @FXML
    private Spinner<Integer> spinnerRepetitions;

    @FXML
    private Button btnLancer, btnReset;

    @FXML
    private NumberAxis axeX, axeY;

    @FXML
    private LineChart<Number,Number> graphique;

    @FXML
    private RadioButton radioKm, radioPrixDown, radioPrixUp, radioDate;

    private ToggleGroup groupeCritere;

    private static final int[] TAILLES = {100,500,1_000,5_000,10_000,50_000};

    @FXML
    public void initialize(){
        spinnerRepetitions.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,10));

        graphique.setAnimated(false);

        btnLancer.setOnAction(e -> lancerBenchmark());

        btnReset.setOnAction(e -> reset());

        groupeCritere = new ToggleGroup();
        radioKm.setToggleGroup(groupeCritere);
        radioDate.setToggleGroup(groupeCritere);
        radioPrixDown.setToggleGroup(groupeCritere);
        radioPrixUp.setToggleGroup(groupeCritere);

        radioKm.setUserData(Voiture.PAR_KM_ASC);
        radioDate.setUserData(Voiture.PAR_DATE_DESC);
        radioTransmissionManuelle.setUserData(Transmission.MANUELLE);

        radioTransmissionToutes.setSelected(true);

        groupeTransmission.selectedToggleProperty().addListener((obs, ancToggle, nouvToggle) -> {
            if (nouvToggle != null) {
                appliquerFiltreMultipleEtTri();
            }
        });

    }

    private void lancerBenchmark(){
        List<Algorithme> algos = collecterAlgorithmes();
        if(algos.isEmpty()){
            afficherAlert("Selectionnez au moins un algorithme");
            return;
        }

        graphique.getData().clear();
        int repetitions = spinnerRepetitions.getValue();

        new Thread(() -> {
            for(Algorithme algo: algos){
                XYChart.Series<Number,Number> serie = new XYChart.Series<>();

                serie.setName(algo.nom());

                Comparator comparatorChoisi = ra

                for(int n : TAILLES){
                    ArrayList<Voiture> voitures = GenerateurDonneesVoiture.generateVoitures(n);
                    long tempsNs = Chrono.chronometrer(algo, voitures, repetitions);
                    double tempsMilli = tempsNs / 1_000_000.0;
                    Platform.runLater(() ->
                            serie.getData().add(new XYChart.Data<>(n,tempsNs)));
                }
                Platform.runLater(() -> graphique.getData().add(serie));
            }
        }).start();
    }

    private void reset(){
        graphique.getData().clear();
    }

    private List<Algorithme> collecterAlgorithmes(){
        List<Algorithme> algos = new ArrayList<>();
        // Algos Orginal
        if(chkDicho.isSelected()) algos.add(new RechercheDichotomique());
        if(chkLineaire.isSelected()) algos.add(new RechercheLineaire());
        if(chkDouble.isSelected()) algos.add(new DoubleBoucle());

        // Algos de Tri
        if(chkTriBulle.isSelected()) algos.add(new TriBulle());
        if(chkTriSelection.isSelected()) algos.add(new TriSelection());
        if(chkTriInsertion.isSelected()) algos.add(new TriInsertion());
        if(chkFusion.isSelected()) algos.add(new TriMerge());
        if(chkRapide.isSelected()) algos.add(new TriRapide());

        return algos;
    }

    private void afficherAlert(String msg){
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }

}

