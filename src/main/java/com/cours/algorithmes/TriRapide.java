package com.cours.algorithmes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implémentation de l'algorithme de tri rapide.
 * <p>
 * Cette classe implémente l'interface Algorithme pour définir le tri rapide dans la méthode ordonner.
 * </p>
 *
 * @version 1.0
 */
public class TriRapide<T> implements Algorithme<T> {
    @Override
    public String nom() { return "Tri Rapide";}

    @Override
    public String complexiteTheorique() { return "O(nlogn)";}

    /**
     * Trie une liste d'objets spécifiés en ordre.
     * <p>
     * Tous les éléments du tableau doivent implémenter l'interface Comparator.
     * </p>
     *
     * @param data le type des éléments de la liste, devant être comparables entre eux
     * @param comparateur permet de déterminer le critère de tri
     * @version 1.0
     */
    @Override
    public void ordonner(ArrayList<T> data, Comparator<T> comparateur) {
        if (data == null || data.size() <= 1) return;
        trier(data, 0, data.size() - 1, comparateur);
    }

    private void trier(List<T> data, int deb, int fin, Comparator<T> comparateur) {
        if (deb < fin) {
            int p = sectionner(data, deb, fin, comparateur);
            trier(data, deb, p, comparateur);
            trier(data, p + 1, fin, comparateur);
        }
    }

    private int sectionner(List<T> data, int deb, int fin, Comparator<T> comparateur) {
        // Choix de pivot aléatoire afin de réduire le nombre de comparaisons pour des données presque triées
        int pivotIndex = deb + ThreadLocalRandom.current().nextInt(fin - deb + 1);
        T pivot = data.get(pivotIndex);

        int i = deb - 1;
        int j = fin + 1;

        // Utilisation de deux pointeurs à chaque extrémité de la section qui s'arrêtent lorsqu'un élément est hors
        // position relativement au pivot
        while (true) {
            do {
                j--;
            } while (comparateur.compare(data.get(j), pivot) > 0);

            do {
                i++;
            } while (comparateur.compare(data.get(i), pivot) < 0);

            if (i >= j) {
                return j;
            }

            // Si i et j ne se sont pas croisées encore, faire un échange de position des éléments pointés.
            T temp = data.get(i);
            data.set(i, data.get(j));
            data.set(j, temp);
        }
    }

}
