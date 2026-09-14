package com.cours.controller;

import com.cours.model.Voiture;
import com.cours.service.VoitureService;
import com.cours.util.LectureCSV;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

// Un controlleur distinct pour la zone de détails (qui est dans un fichier FXML distinct)
public class DetailsVoitureController {

    private VoitureService service;

    private Voiture voitureActuelle;  // Pour stocker la voiture courante

    private Runnable onFavoriAjoute;

    @FXML
    private VBox racineDetails;  // La zone d'affichage des détails

    @FXML
    private Label labelMarque, labelModele, labelAnnee, labelPrix,
            labelKilometres, labelCarburant, labelTransmission, labelCouleur, labelVendeur, labelVille, labelDescription, labelPrixParKilo;

    @FXML
    private Button buttonAddFavoris;

    @FXML
    public void initialize() {
        racineDetails.managedProperty().bind(racineDetails.visibleProperty());

        racineDetails.setVisible(false);

        buttonAddFavoris.setOnAction(e -> {
            if (voitureActuelle != null) {
                ajouterFavori(voitureActuelle.getId());
            }
        });
    }

    //Pour utiliser le même VoitureService que dans MainController
    public void setVoitureService(VoitureService service) {
        this.service = service;
    }

    public void afficherVoiture(Voiture voiture) {
        if(voiture == null) {
            racineDetails.setVisible(false); // Si aucune voiture n'est sélectionnée, on n'affiche rien
            this.voitureActuelle = null;
            return;
        }
        this.voitureActuelle = voiture;
        racineDetails.setVisible(true);

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

            public void setOnFavoriAjoute(Runnable onFavoriAjoute) {
                this.onFavoriAjoute = onFavoriAjoute;
            }

            // La méthode pour ajouter la Voiture affichée dans les favoris
            public void ajouterFavori(int idVoiture){
                if (service != null) {
                    service.ajouterFavori(idVoiture);
                    if (onFavoriAjoute != null) {
                        onFavoriAjoute.run();
                    }
                } else {
                    System.err.println("Erreur : Service est null");
                }
            }

}
