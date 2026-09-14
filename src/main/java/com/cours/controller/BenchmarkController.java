package com.cours.controller;

import com.cours.algorithmes.Algorithme;
import com.cours.algorithmes.TriFusion;
import com.cours.algorithmes.TriInsertion;
import com.cours.algorithmes.TriRapide;
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

    private static final int[] TAILLES = {100,500,1_000,5_000,10_000,50_000, 100_000};
    private static final int[] TAILLES_INSERTION = {100, 500, 1_000, 2_000, 5_000};

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

        radioKm.setSelected(true);

        axeX.setAutoRanging(true);
        axeY.setAutoRanging(true);
    }
    // La fonction pour récupérer le radio-bouton choisi.
    private Comparator<Voiture> recupererComparateurChoisi() {
        if (radioKm.isSelected()) {
            return Voiture.PAR_KM_ASC;
        } else if (radioPrixUp.isSelected()) {
            return Voiture.PAR_PRIX_ASC;
        } else if (radioPrixDown.isSelected()) {
            return Voiture.PAR_PRIX_DESC;
        } else if (radioDate.isSelected()) {
            return Voiture.PAR_DATE_DESC;
        }
        return Voiture.PAR_KM_ASC;  // La valeur par défaut
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

                Comparator<Voiture> comparatorChoisi = recupererComparateurChoisi();

                int[] tailles = (algo instanceof TriFusion
                        || algo instanceof TriRapide) ? TAILLES : TAILLES_INSERTION;

                for(int n : tailles){
                    ArrayList<Voiture> voitures = new ArrayList<>(GenerateurDonneesVoiture.generateVoitures(n));
                    long tempsNs = Chrono.chronometrer(algo, voitures, comparatorChoisi, repetitions);
                    double tempsMilli = tempsNs / 1_000_000.0;
                    Platform.runLater(() ->
                            serie.getData().add(new XYChart.Data<>(n,tempsMilli)));
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
        // Algos de Tri
        if(chkTriInsertion.isSelected()) algos.add(new TriInsertion());
        if(chkFusion.isSelected()) algos.add(new TriFusion());
        if(chkRapide.isSelected()) algos.add(new TriRapide());

        return algos;
    }

    private void afficherAlert(String msg){
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }
}

