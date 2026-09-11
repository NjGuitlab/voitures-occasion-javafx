package com.cours.algorithmes.benchmark;

import com.cours.algorithmes.Algorithme;
import com.cours.model.Voiture;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * La classe {@code Chrono} fournit des outils pour mesurer les performances temporelles
 * d'algorithmes de tri ou d'ordonnancement.
 * <p>
 * Elle inclut un mécanisme de préchauffage de la machine virtuelle Java (JVM)
 * pour garantir des mesures de temps plus précises et stables.
 * </p>
 *
 * @version 1.0
 */
public class Chrono {

    /**
     * Mesure le temps d'exécution moyen d'un algorithme d'ordonnancement.
     * <p>
     * Cette méthode effectue d'abord 3 itérations de préchauffage pour permettre
     * à la JVM d'optimiser le code (compilation JIT). Ensuite, elle exécute l'algorithme
     * un nombre spécifié de fois sur des copies distinctes du tableau initial afin d'éviter
     * les biais liés aux effets de bord ou au tri d'un tableau déjà ordonné.
     * </p>
     *
     * @param algo        l'algorithme d'ordonnancement à chronométrer.
     * @param tableau     la liste initiale d'éléments (de type générique) à ordonner.
     * @param comparateur le comparateur définissant les critères de tri des éléments.
     * @param repetitions le nombre de fois que l'algorithme doit être exécuté pour la mesure.
     * @return le temps d'exécution moyen par répétition, exprimé en <b>nanosecondes</b>.
     */
    public static <T> long chronometrer(
            Algorithme algo, ArrayList<T> tableau, Comparator comparateur, int repetitions) {

        for (int i = 0; i < 3; i++){
            ArrayList<T> copiePrepJVM = new ArrayList<>(tableau);
            algo.ordonner(copiePrepJVM, comparateur);
        }

        long deb = System.nanoTime();
        for (int i = 0; i < repetitions; i++){
            ArrayList<T> listeReference = new ArrayList<>(tableau);
            algo.ordonner(listeReference, comparateur);
        }
        long fin = System.nanoTime();

        return ((fin - deb) / repetitions);
    }
}
