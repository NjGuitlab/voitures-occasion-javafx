package com.cours.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection connexion() throws SQLException {

        // Crée un objet Properties pour lire les infos de database.properties
        Properties properties = new Properties();

        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "Fichier database.properties introuvable."
                );
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

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Impossible de lire la configuration de la base de données.",
                    e
            );
        }
    }

}
