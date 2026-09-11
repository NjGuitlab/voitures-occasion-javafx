package com.cours.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoitureTest {

    @Test
    void doitCalculerLePrixParKilometre() {
        Voiture voiture = new Voiture(
                1,
                "Toyota",
                "Corolla",
                2020,
                100000,
                20000,
                TypeCarburant.ESSENCE,
                Transmission.MANUELLE,
                "Noir",
                "Montreal",
                TypeVendeur.PARTICULIER,
                LocalDate.of(2026, 1, 1),
                "Voiture de test"
        );

        assertEquals(0.20, voiture.getPrixParKilometre(), 0.001);
    }

}
