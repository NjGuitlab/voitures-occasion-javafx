package com.cours.algorithmes;
import java.util.ArrayList;
import java.util.Comparator;

public class TriInsertion<T> implements Algorithme<T> {

    @Override
    public String nom() { return "Tri par Insertion";}

    @Override
    public String complexiteTheorique() { return "O(n\u00B2)";}

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
