package com.cours.model;

import java.time.LocalDate;

public class Voiture {

    private int id;
    private String marque;
    private String modele;
    private int annee;
    private int kilometrage;
    private int prix;
    private TypeCarburant carburant;
    private Transmission transmission;
    private String couleur;
    private String ville;
    private TypeVendeur typeVendeur;
    private LocalDate datePublication;
    private String description;

    // -------- Constructeur --------- //

    // Utilisation des setters pour appliquer les règles de validation dès la création de l'objet.

    public Voiture(int id, String marque, String modele, int annee, int kilometrage, int prix, TypeCarburant carburant, Transmission transmission, String couleur, String ville, TypeVendeur typeVendeur, LocalDate datePublication, String description) {
        setId(id);
        setMarque(marque);
        setModele(modele);
        setAnnee(annee);
        setKilometrage(kilometrage);
        setPrix(prix);
        setCarburant(carburant);
        setTransmission(transmission);
        setCouleur(couleur);
        setVille(ville);
        setTypeVendeur(typeVendeur);
        setDatePublication(datePublication);
        setDescription(description);
    }


    // ----- Les getters ----- //

    public int getId() {
        return id;
    }

    public String getMarque() {
        return marque;
    }

    public String getModele() {
        return modele;
    }

    public int getAnnee() {
        return annee;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    public int getPrix() {
        return prix;
    }

    public TypeCarburant getCarburant() {
        return carburant;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public String getCouleur() {
        return couleur;
    }

    public String getVille() {
        return ville;
    }

    public TypeVendeur getTypeVendeur() {
        return typeVendeur;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public String getDescription() {
        return description;
    }


    // ---- Les setters ---- //

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant doit être supérieur à 0."
            );
        }
        this.id = id;
    }

    public void setMarque(String marque) {

        if (marque == null || marque.isBlank()) {
            throw new IllegalArgumentException(
                    "La marque est obligatoire."
            );
        }
        this.marque = marque;
    }

    public void setModele(String modele) {
        if (modele == null || modele.isBlank()) {
            throw new IllegalArgumentException(
                    "Le modèle est obligatoire."
            );
        }
        this.modele = modele;
    }

    public void setAnnee(int annee) {
        if (annee < 1900 || annee > 2100) {
            throw new IllegalArgumentException(
                    "L'année doit être comprise entre 1900 et 2100."
            );
        }
        this.annee = annee;
    }

    public void setKilometrage(int kilometrage) {
        if (kilometrage < 0) {
            throw new IllegalArgumentException(
                    "Le kilométrage ne peut pas être négatif."
            );
        }
        this.kilometrage = kilometrage;
    }

    public void setPrix(int prix) {
        if (prix <= 0) {
            throw new IllegalArgumentException(
                    "Le prix doit être supérieur à 0."
            );
        }
        this.prix = prix;
    }

    public void setTransmission(Transmission transmission) {
        if (transmission == null) {
            throw new IllegalArgumentException(
                    "La transmission est obligatoire."
            );
        }
        this.transmission = transmission;
    }

    public void setCarburant(TypeCarburant carburant) {
        if (carburant == null) {
            throw new IllegalArgumentException(
                    "Le type de carburant est obligatoire."
            );
        }
        this.carburant = carburant;
    }

    public void setCouleur(String couleur) {
        if (couleur == null || couleur.isBlank()) {
            throw new IllegalArgumentException(
                    "La couleur est obligatoire."
            );
        }
        this.couleur = couleur;
    }

    public void setVille(String ville) {
        if (ville == null || ville.isBlank()) {
            throw new IllegalArgumentException(
                    "La ville est obligatoire."
            );
        }
        this.ville = ville;
    }

    public void setTypeVendeur(TypeVendeur typeVendeur) {
        if (typeVendeur == null) {
            throw new IllegalArgumentException(
                    "Le type de vendeur est obligatoire."
            );
        }
        this.typeVendeur = typeVendeur;
    }

    public void setDatePublication(LocalDate datePublication) {
        if (datePublication == null) {
            throw new IllegalArgumentException(
                    "La date de publication est obligatoire."
            );
        }
        this.datePublication = datePublication;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "La description est obligatoire."
            );
        }
        this.description = description;
    }

    @Override
    public String toString() {
        return "Voiture{" +
                "id=" + id +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", annee=" + annee +
                ", kilometrage=" + kilometrage +
                ", prix=" + prix +
                ", ville='" + ville + '\'' +
                '}';
    }
}
