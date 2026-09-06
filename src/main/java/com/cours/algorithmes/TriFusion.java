package com.cours.algorithmes;

import java.util.ArrayList;
import java.util.Comparator;

public class TriFusion<T> implements Algorithme<T> {

    @Override
    public String nom() { return "Tri par Fusion";}

    @Override
    public String complexiteTheorique() { return "O(nlogn)";}

    @Override
    public void ordonner(ArrayList<T> data, Comparator<T> comparateur) {

        diviser(data, 0, data.size() - 1, comparateur);
    }

    private void diviser(ArrayList<T> data, int deb, int fin, Comparator<T> comparateur) {
        if (deb >= fin) return;

        int mil = (deb + fin)/2;

        diviser(data, deb, mil, comparateur);
        diviser(data, mil + 1, fin, comparateur);
        fusionner(data, deb, mil, fin, comparateur);
    }

    private void fusionner(ArrayList<T> data, int deb, int mil, int fin, Comparator<T> comparateur) {

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
