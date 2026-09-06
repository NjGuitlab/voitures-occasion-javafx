package com.cours.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public class Voiture {
    String id = UUID.randomUUID().toString();
    String marque;
    String modele;
    int anneeModele;
    int kilometrage;
    int prix;
    TypeCarburant carburant;
    Transmission transmission;
    String couleur;
    String villeVendeur;
    TypeVendeur vendeur;
    LocalDate datePublication;
    String description;

    public static final Comparator<Voiture> PAR_PRIX_ASC =
            Comparator.comparing(Voiture::getPrix);

    public static final Comparator<Voiture> PAR_PRIX_DESC =
            Comparator.comparing(Voiture::getPrix).reversed();

    public static final Comparator<Voiture> PAR_KM_ASC =
            Comparator.comparing(Voiture::getKilometrage);

    public static final Comparator<Voiture> PAR_DATE_DESC =
            Comparator.comparing(Voiture::getDatePublication).reversed();

    public Voiture(int kilometrage, int prix, LocalDate datePublication) {
        this.kilometrage = kilometrage;
        this.prix = prix;
        this.datePublication = datePublication;
    }

    public int getPrix() {
        return prix;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    @Override
    public String toString() {
        return "Voiture{" +
                "kilometrage=" + kilometrage +
                ", prix=" + prix +
                ", datePublication=" + datePublication +
                '}';
    }
}
