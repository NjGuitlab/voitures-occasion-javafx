package com.cours.dao;

import com.cours.model.TypeCarburant;
import com.cours.model.Transmission;
import com.cours.model.TypeVendeur;
import com.cours.model.Voiture;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class VoiturePostgreSQLDAOTest {

    // On crée un objet de notre classe VoiturePostgreSQLDAO
    private final VoiturePostgreSQLDAO dao = new VoiturePostgreSQLDAO();

    @Test
    void trouverTous_retourneLesVoitures(){

        List<Voiture> voitures = dao.trouverTous();

        assertNotNull(voitures);
        assertFalse(voitures.isEmpty());

        System.out.println("Nombre de voitures:" + voitures.size());

    }

    @Test
    void trouverParId_retourneUneVoiture() {

        var resultat = dao.trouverParId(1);

        assertTrue(resultat.isPresent());

        Voiture voiture = resultat.get();

        assertEquals(1, voiture.getId());

        System.out.println(voiture);
    }

    @Test
    void trouverParId_retourneOptionalVideSiIntrouvable() {

        var resultat = dao.trouverParId(999999);

        assertTrue(resultat.isEmpty());
    }

    @Test
    void ajouter_creeUneVoiture() {

        Voiture voiture = new Voiture(
                1,
                "Toyota",
                "Corolla Test",
                2024,
                10000,
                25000,
                TypeCarburant.ESSENCE,
                Transmission.AUTOMATIQUE,
                "Noir",
                "Montreal",
                TypeVendeur.PARTICULIER,
                java.time.LocalDate.now(),
                "Voiture créée pour le test"
        );

        Voiture voitureAjoutee = dao.ajouter(voiture);

        assertNotNull(voitureAjoutee);
        assertTrue(voitureAjoutee.getId() > 0);

        System.out.println(
                "Voiture ajoutée avec ID : " + voitureAjoutee.getId()
        );
    }

    @Test
    void modifier_modifieUneVoiture() {

        Voiture voiture = dao.trouverParId(1).orElseThrow();

        voiture.setPrix(26000);

        dao.modifier(voiture);

        Voiture voitureModifiee = dao.trouverParId(1).orElseThrow();

        assertEquals(26000, voitureModifiee.getPrix());
    }

    @Test
    void supprimer_supprimeUneVoiture() {

        Voiture voiture = new Voiture(
                1,
                "Toyota",
                "Voiture Test Suppression",
                2024,
                10000,
                20000,
                TypeCarburant.ESSENCE,
                Transmission.MANUELLE,
                "Blanc",
                "Montreal",
                TypeVendeur.PARTICULIER,
                java.time.LocalDate.now(),
                "Test suppression"
        );

        Voiture voitureAjoutee = dao.ajouter(voiture);

        int id = voitureAjoutee.getId();

        dao.supprimer(id);

        assertTrue(dao.trouverParId(id).isEmpty());
    }

}
