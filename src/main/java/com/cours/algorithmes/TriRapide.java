package com.cours.algorithmes;
import java.util.ArrayList;
import java.util.Comparator;

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
     * @param ArrayList<T> le type des éléments de la liste, devant être comparables entre eux
     * @param comparateur permet de déterminer le critère de tri
     * @version 1.0
     */
    @Override
    public void ordonner(ArrayList<T> data, Comparator<T> comparateur) {
        if(data == null || data.size() <= 1 ) return;
        trier(data, 0, data.size() - 1, comparateur);
    }

    private void trier(ArrayList<T> data, int deb, int fin, Comparator<T> comparateur) {
        if(deb < fin) {
            int p = sectionner(data, deb, fin, comparateur);
            trier(data, deb, p - 1, comparateur);
            trier(data, p + 1 , fin, comparateur);
        }
    }

    private int sectionner (ArrayList<T> data, int deb, int fin, Comparator<T> comparateur) {
        T pivot = data.get(fin);
        int i = deb - 1 ;

        for(int j = deb ; j < fin ; j++){
            if(comparateur.compare(data.get(j), pivot) <= 0){
                i++;
                T temp = data.get(i);
                data.set(i, data.get(j));
                data.set(j, temp);
            }
        }

        T temp = data.get(i+1);
        data.set(i+1, data.get(fin));
        data.set(fin, temp);
        return i + 1;
    }

}
