package com.cours.algorithmes.reference.util;

import com.cours.model.Transmission;
import com.cours.model.TypeCarburant;
import com.cours.model.TypeVendeur;
import com.cours.model.Voiture;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * La classe {@code GenerateurDonneesVoiture} permet de générer une liste de voitures avec attributs aléatoires.
 *
 * @version 1.0
 */
public class GenerateurDonneesVoiture {

    private static final String[] MARQUES = {"Honda", "Toyota", "Ford", "Tesla", "Hyundai"};

    private static final String[][] MODELES_PAR_MARQUE = {
            {"Civic", "CR-V", "Accord"},
            {"Corolla", "RAV4", "Prius"},
            {"F-150", "Escape", "Mustang"},
            {"Model 3", "Model Y", "Model S"},
            {"Elantra", "Tucson", "Ioniq 5"}
    };

    private static final String[] COULEURS = {"Noir", "Blanc", "Gris", "Bleu", "Rouge", "Argent"};
    private static final String[] VILLES = {"Montreal", "Toronto", "Vancouver", "Calgary", "Quebec"};

    private static final String[] COMMENTAIRES_DROLES = {
            "Jamais accidentée, sauf une fois au chalet.",
            "Le moteur ronronne comme un chaton bien nourri.",
            "Senteur de voiture neuve disparue depuis longtemps, mais elle roule comme un charme.",
            "Idéale pour survivre aux nids-de-poule d'ici.",
            "Raison de la vente : j'ai acheté une trottinette électrique.",
            "Pas de lowballers, je sais ce que je vaux (et la voiture aussi)."
    };

    private static final Random RANDOM = new Random();

    /**
     * Génère une liste d'objets de type Voiture avec attributs déterminés pour simuler des voitures usagées à vendre
     * dans le marché canadien.
     *
     * @param nbVoitures (int) pour le nombre d'objets à générer
     * @return  List<Voiture> qui contient les objets générés
     * @version 1.0
     */
    public static List<Voiture> generateVoitures(int nbVoitures) {
        List<Voiture> voitures = new ArrayList<>();

        for (int i = 1; i <= nbVoitures; i++) {

            int id = i;

            int marqueIdx = RANDOM.nextInt(MARQUES.length);
            String marque = MARQUES[marqueIdx];
            String[] modeles = MODELES_PAR_MARQUE[marqueIdx];
            String modele = modeles[RANDOM.nextInt(modeles.length)];

            int annee = 2010 + RANDOM.nextInt(2026 - 2010 + 1);

            // Détermination d'un kilométrage selon l'usage annuel + montant aléatoire
            int age = 2026 - annee;
            int baseKm = age * 15000;
            int kilometrage = Math.max(5000, baseKm + (RANDOM.nextInt(20000) - 10000));

            // Attribution du prix, du type de carburant et du type de transmission selon la marque ou le modèle de voiture
            TypeCarburant carburant = TypeCarburant.ESSENCE;
            Transmission transmission = RANDOM.nextBoolean() ? Transmission.AUTOMATIQUE : Transmission.MANUELLE;
            int prixBaseNeuf = 30000;

            if (marque.equals("Tesla") || modele.equals("Ioniq 5")) {
                carburant = TypeCarburant.ELECTRIQUE;
                transmission = Transmission.AUTOMATIQUE;
                prixBaseNeuf = 55000;
            } else if (modele.equals("Prius")) {
                carburant = TypeCarburant.HYBRIDE;
                transmission = Transmission.AUTOMATIQUE;
                prixBaseNeuf = 35000;
            } else if (modele.equals("F-150")) {
                prixBaseNeuf = 50000;
                if (RANDOM.nextBoolean()) carburant = TypeCarburant.DIESEL;
            } else if (modele.equals("CR-V") || modele.equals("Tucson")) {
                if (RANDOM.nextBoolean()) carburant = TypeCarburant.HYBRIDE_RECHARGEABLE;
            }

            // Calculer le prix en considérant la dépréciation
            double facteurDepreciation = Math.pow(0.88, age);
            int prix = (int) (prixBaseNeuf * facteurDepreciation);
            if (kilometrage > 150000) prix *= 0.8;
            prix = Math.max(3000, prix);

            String couleur = COULEURS[RANDOM.nextInt(COULEURS.length)];
            String ville = VILLES[RANDOM.nextInt(VILLES.length)];

            TypeVendeur typeVendeur = (RANDOM.nextDouble() < 0.60)
                    ? TypeVendeur.CONCESSIONNAIRE
                    : TypeVendeur.PARTICULIER;

            LocalDate current = LocalDate.now();
            LocalDate datePublication = current.minusDays(RANDOM.nextInt(90));


            String description = String.format("Magnifique %s %s %d de couleur %s. %d km au compteur. " +
                            "Moteur %s, transmission %s. %s",
                    marque, modele, annee, couleur, kilometrage,
                    carburant.toString().toLowerCase(), transmission.toString().toLowerCase(),
                    COMMENTAIRES_DROLES[RANDOM.nextInt(COMMENTAIRES_DROLES.length)]);

            voitures.add(new Voiture(
                    id, marque, modele, annee, kilometrage, prix,
                    carburant, transmission, couleur, ville, typeVendeur,
                    datePublication, description
            ));
        }

        return voitures;
    }
}