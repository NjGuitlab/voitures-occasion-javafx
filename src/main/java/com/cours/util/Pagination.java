package com.cours.util;

import com.cours.model.Voiture;

import java.util.List;

public class Pagination {

    // Liste complete des voitures
    private final List<Voiture> voitures;

    // Nombre de voiture affichées sur une page
    private final int taillePage;

    // Numéro de la page actuellement affichée(debut:Page 1)
    private int pageActuelle;

    // Constructeur
    public Pagination(List<Voiture> voitures, int taillePage) {

        if (taillePage <= 0) {
            throw new IllegalArgumentException(
                    "La taille de page doit être supérieure à 0."
            );
        }

        this.voitures = voitures;
        this.taillePage = taillePage;
        this.pageActuelle = 1;
    }

    // Retourne les voitures de la page actuellement sélectionnée
    public List<Voiture> getVoiturePageActuelle(){

        // Calcule l'index de début dans la liste
        int debut = (pageActuelle - 1) * taillePage;

        // Math.min permet de ne jamais dépasser le nombre réel de voitures
        int fin = Math.min(debut + taillePage, voitures.size());

        // Retourne une partie de la liste (début inclus, fin exclu)
        return voitures.subList(debut, fin);

    }
    // Retourne le numéro de la page actuelle
    public int getPageActuelle() {
        return pageActuelle;
    }

    // Calcule et retourne le nombre total de pages
    public int getNombrePages() {

        // On convertit en double pour éviter une division entière
        //
        // Exemple : 420 / 10 = 42
        // 425 / 10 = 42.5
        // Math.ceil(42.5) = 43

        return (int) Math.ceil((double) voitures.size() / taillePage
        );
    }

    // Passe à la page suivante
    public void pageSuivante() {

        // On vérifie qu'il existe une page suivante
        if (pageActuelle < getNombrePages()) {
            pageActuelle++;
        }
    }


    // Retourne à la page précédente
    public void pagePrecedente() {
        if (pageActuelle > 1) {
            pageActuelle--;
        }
    }
}
