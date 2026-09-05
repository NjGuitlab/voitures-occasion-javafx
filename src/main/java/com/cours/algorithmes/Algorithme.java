package com.cours.algorithmes;

import com.cours.model.Voiture;

import java.util.ArrayList;
import java.util.Comparator;

public interface Algorithme<T> {

    String nom();

    String complexiteTheorique();

    void ordonner(ArrayList<T> data, Comparator<T> comparator);
}
