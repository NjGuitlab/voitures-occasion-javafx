package com.cours.dao;

import com.cours.model.Voiture;
import java.util.List;
import java.util.Optional;

public interface VoitureDAO {

    // Retourne toutes les voitures qui sont dans la base
    List<Voiture> trouverTous();

    // Chercher une voiture avec son ID, optionnal gere le cas ou la voiture n'éxiste pas
    Optional<Voiture> trouverParId(int id);

    // Ajouter une nouvelle voiture dans postgres
    // Postgress va génerer l'ID
    Voiture ajouter(Voiture voiture);

    // Modifier les informations d'une voiture existente
    void modifier(Voiture voiture);

    // Supprime une voiture
    void supprimer(int id);

}
