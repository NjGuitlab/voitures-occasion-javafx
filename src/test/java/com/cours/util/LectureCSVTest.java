package com.cours.util;

import com.cours.model.Voiture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


public class LectureCSVTest {

@Test
    void doitChargerLesVoitures(){

    // Création d'un nouveau object lectureCSV
    LectureCSV lectureCSV = new LectureCSV();

    // on charge notre liste de voitures
    List<Voiture> voitures = lectureCSV.chargerVoitures();

    // Verifie que notre liste n'est pas null
    Assertions.assertNotNull(voitures);

    // Compare le nombre de voitures récuperés au nombre réel
    Assertions.assertEquals(420,voitures.size());

    // Récupere la premiere voiture et son id et le compare à 1
    Assertions.assertEquals(1,voitures.get(0).getId());

    }

}
