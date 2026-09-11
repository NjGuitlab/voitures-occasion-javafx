package com.cours.service;

import com.cours.model.Transmission;
import com.cours.model.TypeCarburant;
import com.cours.model.Voiture;
import com.cours.util.LectureCSV;
import org.junit.jupiter.api.Test;


import java.util.List;

import static com.cours.model.TypeCarburant.*;
import static org.junit.jupiter.api.Assertions.*;


public class VoitureServiceTest {

    @Test
    void doitChargerServiceTest(){

        // On crée la source de données
        LectureCSV lectureCSV = new LectureCSV();

        // On fournit cette source au service
        VoitureService service = new VoitureService(lectureCSV);

        // Verifie que la liste existe
        assertNotNull(service.getVoitures());

        // Verifie que le service contient bien les 420 voitures du csv
        assertEquals(420, service.getVoitures().size());


    }

    @Test
    void doitFiltrerParMarque(){

        // On crée la source de données
        LectureCSV lectureCSV = new LectureCSV();

        // On fournit cette source au service
        VoitureService service = new VoitureService(lectureCSV);

        List<Voiture> resultats = service.filtrerParMarque("Toyota");

        assertNotNull(resultats);
        assertFalse(resultats.isEmpty());

        for (Voiture voiture: resultats) {
            assertEquals("Toyota", voiture.getMarque());
        }

    }

    @Test
    void doitFiltrerParCarburant(){

        LectureCSV lectureCSV = new LectureCSV();

        VoitureService service = new VoitureService(lectureCSV);

        List<Voiture> resultats = service.filtrerParCarburant(TypeCarburant.ESSENCE);

        assertNotNull(resultats);
        assertFalse(resultats.isEmpty());

        for(Voiture voiture : resultats){
            assertEquals(TypeCarburant.ESSENCE, voiture.getCarburant());
        }

    }

    @Test
    void doitFiltreParTransmission(){

        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> resultats = service.filtrerParTransmission(Transmission.AUTOMATIQUE);

        assertNotNull(resultats);
        assertFalse(resultats.isEmpty());

        for(Voiture voiture: resultats){
            assertEquals(Transmission.AUTOMATIQUE, voiture.getTransmission());
        }

    }

    @Test
    void doitFiltrerParPrixMax(){
        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> resultats = service.filtrerParPrixMax(9000);

        assertNotNull(resultats);
        assertFalse(resultats.isEmpty());

        for(Voiture voiture: resultats){
            assertTrue(voiture.getPrix()<=9000);
        }
    }

    @Test
    void doitFiltrerParAnneeMax(){
        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> resultats = service.filtrerParAnneeMax(2016);

        assertNotNull(resultats);
        assertFalse(resultats.isEmpty());

        for(Voiture voiture: resultats){
            assertTrue(voiture.getAnnee()<=2016);
        }
    }

    @Test
    void doitFiltrerParAnneeMin(){
        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> resultats = service.filtrerParAnneeMin(2013);

        assertNotNull(resultats);
        assertFalse(resultats.isEmpty());

        for(Voiture voiture: resultats){
            assertTrue(voiture.getAnnee()>=2013);
        }
    }

    @Test
    void doitFiltrerParKilometrageMax(){
        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> resultats = service.filtrerParKilometrageMax(10000);

        assertNotNull(resultats);
        assertFalse(resultats.isEmpty());

        for(Voiture voiture: resultats){
            assertTrue(voiture.getKilometrage()<=10000);
        }
    }

    @Test
    void doitChercherParMotClef(){
        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> resultats = service.rechercher("Toyo");

        assertNotNull(resultats);
        assertFalse(resultats.isEmpty());

        for(Voiture voiture: resultats){
            assertTrue(voiture.getMarque().equalsIgnoreCase("Toyota"));
        }

    }

    // ID existant
    @Test
    void doitTrouverParIdExistant(){
        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        Voiture voiture = service.trouverParId(1);

        assertNotNull(voiture);
        assertEquals(1, voiture.getId());
    }

    // ID inexistant
    @Test
    void doitTrouverParIdInexistant(){
        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        Voiture voiture = service.trouverParId(500);

        assertNull(voiture);
    }
}
