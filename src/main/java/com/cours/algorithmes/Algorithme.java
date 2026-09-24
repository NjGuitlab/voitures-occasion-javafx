package com.cours.algorithmes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Définie la structure non-négociable pour implémenter des algorithmes de tris
 * <p>L'inclusion d'une référence à cette interface dans un context permet de choisir à l'exécution parmi
 * plusieurs tris, si implémentés, via l'interface.</p>
 *
 * @param <T> le type d'objet à trier, où le modèle défini au moins un Comparator.
 * @version 1.0
 */
public interface Algorithme<T> {

    String nom();

    String complexiteTheorique();

    void ordonner(ArrayList<T> data, Comparator<T> comparator);
}
