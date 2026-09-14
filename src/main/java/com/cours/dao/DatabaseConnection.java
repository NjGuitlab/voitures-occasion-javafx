package com.cours.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {

    public static Connection connexion(){

        // Crée un objet Properties pour lire les infos de database.properties
        Properties properties = new Properties();

        try {
            // On cherche le fichier database.properties dans les ressources
            InputStream input = DatabaseConnection.class
                    .getClassLoader()
                    .getResourceAsStream("database.properties");

            if (input == null) {
                throw new Exception("Fichier database.properties introuvable");
            }

            // Charger les informations du fichier dans properties
            properties.load(input);

            // On récupere les informations de connexion
            String URL = properties.getProperty("db.url");
            String USERNAME = properties.getProperty("db.username");
            String PASSWORD = properties.getProperty("db.password");

            // On crée la connexion avec PostgreSQL
            Connection connexion = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            // Retourne la connexion crée
            return connexion;

        } catch (Exception e) {
            // Affiche l'erreur si la connexion ne fonctionne pas
            e.printStackTrace();
        }

        // Si une erreur se produit, aucune connexion n'est retournée
        return null;
    }

}
