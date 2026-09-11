package com.cours.controller;

import com.cours.model.Voiture;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

// Un controlleur distinct pour la zone de détails (qui est dans un fichier FXML distinct)
public class DetailsVoitureController {

    @FXML
    private Label labelMarque, labelModele, labelAnnee, labelPrix,
            labelKilometres, labelCarburant, labelTransmission, labelCouleur, labelVendeur, labelVille, labelDescription, labelPrixParKilo;

    @FXML
    private Button buttonAddFavoris;

    public void afficherVoiture(Voiture voiture) {
        if(voiture != null) {
            labelMarque.setText(voiture.getMarque());
            labelModele.setText(voiture.getModele());
            labelAnnee.setText(String.valueOf(voiture.getAnnee()));
            labelPrix.setText(String.valueOf(voiture.getPrix()));
            labelKilometres.setText(String.valueOf(voiture.getKilometrage()));
            labelCarburant.setText(voiture.getCarburant() != null ? voiture.getCarburant().name() : ""); // On gère vu que Carburant est dans un Enum
            labelTransmission.setText(voiture.getTransmission() != null ? voiture.getTransmission().name() : "");  // Même chose pour transmission
            labelDescription.setText(voiture.getDescription());
            labelCouleur.setText(voiture.getCouleur());
            labelVendeur.setText(voiture.getTypeVendeur() != null ? voiture.getTypeVendeur().name() : "");  // enum ici aussi
            labelVille.setText(voiture.getVille());
            labelPrixParKilo.setText(String.format("%.2f $/km", voiture.getPrixParKilometre()));
        }
    }

}
