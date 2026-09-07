package com.cours.model;
import java.util.Comparator;

/**
 * Classe utilitaire fournissant des comparateurs prédéfinis pour trier des objets {@link Voiture}.
 *
 * <p>Cette classe est finale et ne peut pas être instanciée.</p>
 *
 * @version 1.0
 */
public final class VoitureComparateurs {

    private VoitureComparateurs() {
        throw new AssertionError("Instanciation interdit.");
    }

    /**
     * Comparateur pour trier les voitures par prix, dans l'ordre croissant.
     */
    public static final Comparator<Voiture> PAR_PRIX_ASC =
            Comparator.comparing(Voiture::getPrix);

    /**
     * Comparateur pour trier les voitures par prix, dans l'ordre décroissant.
     */
    public static final Comparator<Voiture> PAR_PRIX_DESC =
            Comparator.comparing(Voiture::getPrix).reversed();

    /**
     * Comparateur pour trier les voitures par kilométrage, dans l'ordre croissant.
     */
    public static final Comparator<Voiture> PAR_KM_ASC =
            Comparator.comparing(Voiture::getKilometrage);

    /**
     * Comparateur pour trier les voitures par date de publication, de la plus récente à la plus ancienne.
     */
    public static final Comparator<Voiture> PAR_DATE_DESC =
            Comparator.comparing(Voiture::getDatePublication).reversed();
}
