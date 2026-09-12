package com.cours.service;

import com.cours.model.Transmission;
import com.cours.model.Voiture;
import com.cours.util.SourceDonnees;
import com.cours.model.TypeCarburant;

import java.util.ArrayList;
import java.util.List;

public class VoitureService {

    private List<Voiture> voitures;

    public VoitureService(SourceDonnees sourceDonnees) {

        this.voitures = sourceDonnees.chargerVoitures();

    }

    public List<Voiture> getVoitures() {
        return voitures;
    }

    //---------------- Filter les voitures par marque ----------------------- //

    public List<Voiture> filtrerParMarque(String marque) {

        List<Voiture> resultat = new ArrayList<>();

        for (Voiture voiture : voitures) {

            if (voiture.getMarque().equalsIgnoreCase(marque)) {

                resultat.add(voiture);

            }

        }

        return resultat;
    }

    //--------------- Filtrer les voitures par Carburant ------------------- //

    public List<Voiture> filtrerParCarburant(TypeCarburant carburant){

        List<Voiture> resultats = new ArrayList<>();

        for(Voiture voiture: voitures){

            if(voiture.getCarburant().equals(carburant)){

                resultats.add(voiture);
            }

        }

        return resultats;

    }

    // ---------------- Filter les voitures par transmission ------------------- //

    public List<Voiture> filtrerParTransmission(Transmission transmission){

        List<Voiture> resultats = new ArrayList<>();

        for(Voiture voiture : voitures){
            if(voiture.getTransmission().equals(transmission)){
                resultats.add(voiture);
            }
        }

        return resultats;

    }

    // ------------- Filtrer les voitures par Prix Max ----------------- //

    public List<Voiture> filtrerParPrixMax(int prixMax){

        List<Voiture> resultats = new ArrayList<>();

        for (Voiture voiture : voitures){
            if(voiture.getPrix() <= prixMax){
                resultats.add(voiture);
            }
        }
        return resultats;

    }

    // ------------- Filtrer les voitures par Annee Min------------ //

    public List<Voiture> filtrerParAnneeMin(int annee){
        List<Voiture> resultats = new ArrayList<>();

        for (Voiture voiture: voitures){
            if(voiture.getAnnee() >= annee){
                resultats.add(voiture);
            }
        }
        return resultats;

    }

    // ------------- Filtrer les voitures par Annee Max------------ //

    public List<Voiture> filtrerParAnneeMax(int annee){
        List<Voiture> resultats = new ArrayList<>();

        for (Voiture voiture: voitures){
            if(voiture.getAnnee() <= annee){
                resultats.add(voiture);
            }
        }
        return resultats;

    }

    //-------------- Filter par Kilometrage Max ------------------ //

    public List<Voiture> filtrerParKilometrageMax(int kilometrageMax){

        List<Voiture> resultats = new ArrayList<>();

        for (Voiture voiture: voitures){
            if(voiture.getKilometrage()<= kilometrageMax){
                resultats.add(voiture);
            }
        }

        return resultats;

    }

    // ------------ Recherche de voiture par mot clé ------------ //
    public List<Voiture> rechercher(String motCle){
        List<Voiture> resultats = new ArrayList<>();
        String recherche = motCle.toLowerCase();

        for(Voiture voiture: voitures){
            if(voiture.getMarque().toLowerCase().contains(recherche)
                    || voiture.getModele().toLowerCase().contains(recherche)
                    || voiture.getVille().toLowerCase().contains(recherche)){
                resultats.add(voiture);
            }
        }
        return resultats;

    }

    // --------------- Rechercher une voiture par ID ----------------- //
    public Voiture trouverParId(int id){

        for(Voiture voiture : voitures) {
            if (voiture.getId() == id) {
                return voiture;
            }
        }
        return null;

    }

    // ---------------------- Filtrer par plusieurs criteres ---------------------- //

    public List<Voiture> filtrerParCriteres(
            String marque,
            TypeCarburant carburant,
            Integer prixMax,
            Integer kilometrageMax){

        List<Voiture> resultats = new ArrayList<>();

        for (Voiture voiture: voitures){

            if (marque != null
                    && !marque.equalsIgnoreCase("Toutes")
                    && !voiture.getMarque().equalsIgnoreCase(marque)){
                continue;
            }
            if (carburant !=null
                    && !voiture.getCarburant().equals(carburant)){
                continue;
            }
            if (prixMax != null
                    && voiture.getPrix() > prixMax){
                continue;
            }
            if (kilometrageMax != null
                    && voiture.getKilometrage() > kilometrageMax){
                continue;
            }
            resultats.add(voiture);
        }

        return resultats;

    }


}
