package com.cours.util;

import com.cours.model.Voiture;
import com.cours.model.TypeCarburant;
import com.cours.model.Transmission;
import com.cours.model.TypeVendeur;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class LectureCSV implements SourceDonnees {

    // -------- Méthode pour charger toutes les voitures du CSV -------- //
    @Override
    public List<Voiture> chargerVoitures() {

        // Rechercher le fichier dans les ressources
        InputStream fichier = getClass().getResourceAsStream("/data/voitures.csv");

        if (fichier == null) {
            throw new IllegalStateException(
                    "Le fichier voitures.csv est introuvable."
            );
        }

        // Transformation des octets en caractères avec UTF-8
        InputStreamReader lecteur = new InputStreamReader(fichier, StandardCharsets.UTF_8);

        // Permet de lire le fichier ligne par ligne
        BufferedReader lecteurCSV = new BufferedReader(lecteur);

        // Contient toutes les voitures créées à partir du CSV
        List<Voiture> voitures = new ArrayList<>();

        try {

            // Lecture de l'en-tête
            String ligne = lecteurCSV.readLine();

            // Lecture des lignes suivantes
            while ((ligne = lecteurCSV.readLine()) != null) {

                // Découpage de la ligne en 13 champs
                String[] champs = parserLigneCSV(ligne);

                // Conversion des champs vers les types Java
                int id = Integer.parseInt(champs[0]);
                String marque = champs[1];
                String modele = champs[2];
                int annee = Integer.parseInt(champs[3]);
                int kilometrage = Integer.parseInt(champs[4]);
                int prix = Integer.parseInt(champs[5]);

                TypeCarburant carburant = TypeCarburant.valueOf(champs[6]);

                Transmission transmission = Transmission.valueOf(champs[7]);

                String couleur = champs[8];
                String ville = champs[9];

                TypeVendeur typeVendeur = TypeVendeur.valueOf(champs[10]);

                LocalDate datePublication = LocalDate.parse(champs[11]);

                String description = champs[12];

                // Création de la voiture
                Voiture voiture = new Voiture(
                        id,
                        marque,
                        modele,
                        annee,
                        kilometrage,
                        prix,
                        carburant,
                        transmission,
                        couleur,
                        ville,
                        typeVendeur,
                        datePublication,
                        description
                );

                // Ajout de la voiture dans la liste
                voitures.add(voiture);
            }

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Erreur lors de la lecture du fichier CSV.",
                    e
            );
        }

        return voitures;
    }


    private String[] parserLigneCSV(String ligne) {

        // Liste des champs trouvés
        List<String> champs = new ArrayList<>();

        // Indique si on est entre guillemets
        boolean entreGuillemets = false;

        // Construit le champ actuel
        StringBuilder champ = new StringBuilder();

        // Parcourt la ligne caractère par caractère
        for (int i = 0; i < ligne.length(); i++) {

            char caractere = ligne.charAt(i);

            // Change l'état quand on rencontre "
            if (caractere == '"') {
                entreGuillemets = !entreGuillemets;
            }

            // Une virgule hors guillemets sépare les champs
            else if (caractere == ',' && !entreGuillemets) {
                champs.add(champ.toString());
                champ.setLength(0);
            }

            // Sinon, ajoute le caractère au champ
            else {
                champ.append(caractere);
            }
        }

        // Ajoute le dernier champ
        champs.add(champ.toString());

        // Transforme la liste en tableau
        return champs.toArray(new String[0]);
    }
}