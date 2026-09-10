package com.cours.util;

import com.cours.model.Voiture;
import com.cours.util.Pagination;
import com.cours.service.VoitureService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;




public class PaginationTest {

    @Test
    void doitOrganiserLesPages(){

    LectureCSV lecture = new LectureCSV();
    VoitureService service = new VoitureService(lecture);

    List<Voiture> resultats = service.getVoitures();

    Pagination pagination = new Pagination(resultats,10);

    List<Voiture> page = pagination.getVoiturePageActuelle();

    assertEquals(10, page.size());

    }

    // Vérifie qye 420 voitures avec 10 voitures par page donne bien 42 pages
    @Test
    void doitCalculerLeNombreDePages(){

        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> voitures = service.getVoitures();

        Pagination pagination = new Pagination(voitures,10);

        assertEquals(42,pagination.getNombrePages());

    }

    // Vérifie que pageSuivante() permet de passer
    // de la page 1 à la page 2
    @Test
    void doitPasserALaPageSuivante() {

        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> voitures = service.getVoitures();

        Pagination pagination = new Pagination(voitures, 10);

        List<Voiture> premierePage = pagination.getVoiturePageActuelle();

        pagination.pageSuivante();

        List<Voiture> deuxiemePage = pagination.getVoiturePageActuelle();

        assertEquals(10, premierePage.size());
        assertEquals(10, deuxiemePage.size());

        // La première voiture de la deuxième page
        // doit être différente de celle de la première page
        assertNotEquals(
                premierePage.get(0).getId(),
                deuxiemePage.get(0).getId()
        );
    }


    // Vérifie que pagePrecedente() permet de revenir
    // de la page 2 à la page 1
    @Test
    void doitRevenirALaPagePrecedente() {

        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> voitures = service.getVoitures();

        Pagination pagination = new Pagination(voitures, 10);

        List<Voiture> premierePage = pagination.getVoiturePageActuelle();

        pagination.pageSuivante();

        pagination.pagePrecedente();

        List<Voiture> pageApresRetour =
                pagination.getVoiturePageActuelle();

        assertEquals(
                premierePage.get(0).getId(),
                pageApresRetour.get(0).getId()
        );
    }


    // Vérifie qu'on ne peut pas dépasser la dernière page
    @Test
    void neDoitPasDepasserLaDernierePage() {

        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> voitures = service.getVoitures();

        Pagination pagination = new Pagination(voitures, 10);

        // On essaie d'aller beaucoup plus loin
        // que la dernière page
        for (int i = 0; i < 50; i++) {
            pagination.pageSuivante();
        }

        List<Voiture> dernierePage =
                pagination.getVoiturePageActuelle();

        // La dernière page contient 10 voitures
        assertEquals(10, dernierePage.size());
    }


    // Vérifie qu'on ne peut pas revenir avant la page 1
    @Test
    void neDoitPasDescendreAvantLaPremierePage() {

        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> voitures = service.getVoitures();

        Pagination pagination = new Pagination(voitures, 10);

        // On essaie de revenir plusieurs fois en arrière
        for (int i = 0; i < 10; i++) {
            pagination.pagePrecedente();
        }

        List<Voiture> page =
                pagination.getVoiturePageActuelle();

        // On doit toujours être sur la première page
        assertEquals(1, page.get(0).getId());
    }

    @Test
    void doitRefuserUneTailleDePageInvalide() {

        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> voitures = service.getVoitures();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Pagination(voitures, 0)
        );
    }

    @Test
    void doitRefuserUneTailleDePageNegative() {

        LectureCSV lecture = new LectureCSV();
        VoitureService service = new VoitureService(lecture);

        List<Voiture> voitures = service.getVoitures();

        assertThrows(
                IllegalArgumentException.class,
                () -> new Pagination(voitures, -5)
        );
    }

}
