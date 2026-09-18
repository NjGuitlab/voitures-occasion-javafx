package com.cours.util;
import  com.cours.model.Voiture;
import java.util.List;

public interface SourceDonnees {

    List<Voiture> chargerVoitures();

    List<String> chargerMarques();
}
