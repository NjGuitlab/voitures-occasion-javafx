package com.cours.algorithmes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implémentation de l'algorithme de tri par fusion.
 * <p>
 * Cette classe implémente l'interface Algorithme pour définir le tri par fusion dans la méthode ordonner.
 * </p>
 *
 * @version 1.0
 */
public class TriFusion<T> implements Algorithme<T> {

    @Override
    public String nom() { return "Tri par Fusion";}

    @Override
    public String complexiteTheorique() { return "O(nlogn)";}

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
    public <T> void ordonner(List<T> data, Comparator<T> comparateur) {

        diviser(data, 0, data.size() - 1, comparateur);
    }

    private <T> void diviser(List<T> data, int deb, int fin, Comparator<T> comparateur) {
        if (deb >= fin) return;

        int mil = (deb + fin)/2;

        diviser(data, deb, mil, comparateur);
        diviser(data, mil + 1, fin, comparateur);
        fusionner(data, deb, mil, fin, comparateur);
    }

    private <T> void fusionner(List<T> data, int deb, int mil, int fin, Comparator<T> comparateur) {

        ArrayList<T> temp = new ArrayList<>(fin - deb + 1);

        for (int x = 0; x <= (fin - deb); x++) {
            temp.add(null);
        }

        int i = deb; int j = mil + 1; int k = 0;

        while (i <= mil && j <= fin) {

            if (comparateur.compare(data.get(i), data.get(j)) <= 0) {
                temp.set(k++, data.get(i++));
            } else {
                temp.set(k++, data.get(j++));
            }
        }

        while (i <= mil) temp.set(k++, data.get(i++));

        while (j <= fin) temp.set(k++, data.get(j++));

        for (int x = 0; x < temp.size(); x++) data.set(deb + x, temp.get(x));

    }

}
