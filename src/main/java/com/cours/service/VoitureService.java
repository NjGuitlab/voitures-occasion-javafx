package com.cours.service;

import com.cours.algorithmes.TriFusion;
import com.cours.algorithmes.TriInsertion;
import com.cours.algorithmes.TriRapide;
import com.cours.dao.VoitureDAO;
import com.cours.model.Transmission;
import com.cours.model.Voiture;
import com.cours.util.SourceDonnees;
import com.cours.model.TypeCarburant;

import java.util.ArrayList;
import java.util.List;

public class VoitureService {

    private List<Voiture> voitures;
    private List<Voiture> favoris;
    private SourceDonnees sourceDonnees;
    private VoitureDAO dao;

    private TriFusion<Voiture> fusion = new TriFusion<>();
    private TriRapide<Voiture> rapide = new TriRapide<>();
    private TriInsertion<Voiture> insertion = new TriInsertion<>();  // On l'utilise pour trier les noms de marques

    // Constructeur utilisé lorsque les opérations CRUD doivent être effectuées
    // via le DAO PostgreSQL.
    public VoitureService(SourceDonnees sourceDonnees, VoitureDAO dao) {

        this.sourceDonnees = sourceDonnees;
        this.dao = dao;
        this.voitures = sourceDonnees.chargerVoitures();
        this.favoris = new ArrayList<>();

    }

    // Constructeur utilisé pour conserver la compatibilité avec les fonctionnalités
    // existantes qui utilisent uniquement une SourceDonnees (ex. CSV et tests).
    public VoitureService(SourceDonnees sourceDonnees) {

        this.sourceDonnees = sourceDonnees;
        this.voitures = sourceDonnees.chargerVoitures();
        this.favoris = new ArrayList<>();

    }

    // ----- Methode pour retourner toutes les voitures de notre sourcedonnées --- //
    public List<Voiture> getVoitures() {
        voitures.clear();
        voitures.addAll(sourceDonnees.chargerVoitures());
        return voitures;
    }

    // ----- Methode pour retourner toutes les marques de notre sourcedonnées --- //
    public List<String> getMarques() {
        return sourceDonnees.chargerMarques();
    }

    public Voiture ajouter(Voiture voiture) {
        return dao.ajouter(voiture);
    }

    public void modifier(Voiture voiture) {
        dao.modifier(voiture);
    }

    public void supprimer(int id) {
        dao.supprimer(id);
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
            Integer kilometrageMax,
            Integer anneeMin,
            Integer anneeMax,
            Transmission transmission) {

        List<Voiture> resultats = new ArrayList<>();

        for (Voiture voiture : voitures) {

            if (marque != null
                    && !marque.equalsIgnoreCase("Toutes")
                    && !voiture.getMarque().equalsIgnoreCase(marque)) {
                continue;
            }

            if (carburant != null
                    && !voiture.getCarburant().equals(carburant)) {
                continue;
            }

            if (prixMax != null
                    && voiture.getPrix() > prixMax) {
                continue;
            }

            if (kilometrageMax != null
                    && voiture.getKilometrage() > kilometrageMax) {
                continue;
            }

            if (anneeMin != null
                    && voiture.getAnnee() < anneeMin) {
                continue;
            }

            if (anneeMax != null
                    && voiture.getAnnee() > anneeMax) {
                continue;
            }

            if (transmission != null
                    && voiture.getTransmission() != transmission) {
                continue;
            }

            resultats.add(voiture);
        }

        return resultats;
    }
    // ------------- Gestion des favoris ----------------- //

    //Ajouter aux favoris
    public void ajouterFavori(int id){
        Voiture voiture = trouverParId(id);

        if(voiture !=null && !favoris.contains(voiture)){
            favoris.add(voiture);
        }
    }

    //Afficher les favoris
    public List<Voiture> getFavoris(){
        return favoris;
    }

    //Supprimer un favoris
    public void supprimerFavori(int id){
        favoris.removeIf(voiture ->voiture.getId() == id);
    }


    /**
     * Applique un tri selon le nombre d'éléments à trier afin d'optimiser la performance.
     *
     * @param listeOrigine la liste à trier.
     * @param typeTri le critère de tri.
     * @param stable si l'ordre relatif est à préserver.
     */

    public void triApplique(ArrayList<Voiture> listeOrigine, String typeTri, boolean stable) {

        if (typeTri == null || "Tous".equals(typeTri)) {
            return;}

        if (listeOrigine.size() <= 1100) {
            switch (typeTri) {
                case "Kilométrage croissant" -> insertion.ordonner(listeOrigine, Voiture.PAR_KM_ASC);
                case "Prix croissant" -> insertion.ordonner(listeOrigine, Voiture.PAR_PRIX_ASC);
                case "Prix décroissant" -> insertion.ordonner(listeOrigine, Voiture.PAR_PRIX_DESC);
                case "Date décroissante" -> insertion.ordonner(listeOrigine, Voiture.PAR_DATE_DESC);
            }
        } else {
            if (!stable) {
                switch (typeTri) {
                    case "Kilométrage croissant" -> rapide.ordonner(listeOrigine, Voiture.PAR_KM_ASC);
                    case "Prix croissant" -> rapide.ordonner(listeOrigine, Voiture.PAR_PRIX_ASC);
                    case "Prix décroissant" -> rapide.ordonner(listeOrigine, Voiture.PAR_PRIX_DESC);
                    case "Date décroissante" -> rapide.ordonner(listeOrigine, Voiture.PAR_DATE_DESC);
                }
            } else {
                switch (typeTri) {
                    case "Kilométrage croissant" -> fusion.ordonner(listeOrigine, Voiture.PAR_KM_ASC);
                    case "Prix croissant" -> fusion.ordonner(listeOrigine, Voiture.PAR_PRIX_ASC);
                    case "Prix décroissant" -> fusion.ordonner(listeOrigine, Voiture.PAR_PRIX_DESC);
                    case "Date décroissante" -> fusion.ordonner(listeOrigine, Voiture.PAR_DATE_DESC);
                }
            }
        }
    }


}
