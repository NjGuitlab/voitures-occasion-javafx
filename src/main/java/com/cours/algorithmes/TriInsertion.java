package com.cours.algorithmes;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Implémentation de l'algorithme de tri par insertion.
 * <p>
 * Cette classe implémente l'interface Algorithme pour définir le tri par insertion dans la méthode ordonner.
 * </p>
 *
 * @version 1.0
 */
public class TriInsertion<T> implements Algorithme<T> {

    @Override
    public String nom() { return "Tri par Insertion";}

    @Override
    public String complexiteTheorique() { return "O(n\u00B2)";}

    /**
     * Trie une liste d'objets spécifiés en ordre.
     * <p>
     * Tous les éléments du tableau doivent implémenter l'interface Comparator.
     * </p>
     *
     * @param ArrayList<T> le type des éléments de la liste, devant être comparables entre eux
     * @param comparateur permet de déterminer le critère de tri
     * @version 1.0
     */
    @Override
    public void ordonner(ArrayList<T> data, Comparator<T> comparateur) {
        
        for (int i = 1; i < data.size(); i++) {
            T key = data.get(i);
            int j = i - 1;

            while (j >= 0 && comparateur.compare(data.get(j), key) > 0) {
                data.set(j + 1, data.get(j));
                j--;
            }
            data.set(j + 1, key);
        }
    }
}
