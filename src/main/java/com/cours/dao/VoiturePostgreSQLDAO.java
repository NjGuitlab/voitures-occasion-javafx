package com.cours.dao;
import com.cours.model.Voiture;
import com.cours.model.TypeCarburant;
import com.cours.model.Transmission;
import com.cours.model.TypeVendeur;

import java.util.List;
import java.util.Optional;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoiturePostgreSQLDAO implements VoitureDAO{



    // ============= Afficher toutes les voitures =============== //

    @Override
    public List<Voiture> trouverTous() {

        // Liste qui va contenir les voitures récupérés depuis PostgreSQL
        List<Voiture> voitures = new ArrayList<>();

        try(Connection connexion = DatabaseConnection.connexion()){

            // Requete pour récuperer toutes les voitures
            // On fait un join avec marques car notre objet Voiture
            // contient le nom de la marque(String), tandis que la base
            // contient seulement id_marque
            String sql = """
                    SELECT
                        v.id,
                        m.nom AS marque,
                        v.modele,
                        v.annee,
                        v.kilometrage,
                        v.prix,
                        v.carburant,
                        v.transmission,
                        v.couleur,
                        v.ville,
                        v.type_vendeur,
                        v.date_publication,
                        v.description
                    FROM voitures v
                    JOIN marques m ON v.id_marque = m.id
                    """;

            try (PreparedStatement statement = connexion.prepareStatement(sql);
                 ResultSet resultats = statement.executeQuery()){

                // On parcourt chaque ligne retournée par PostgreSQL
                while (resultats.next()){
                    Voiture voiture = new Voiture(
                            resultats.getInt("id"),
                            resultats.getString("marque"),
                            resultats.getString("modele"),
                            resultats.getInt("annee"),
                            resultats.getInt("kilometrage"),
                            resultats.getInt("prix"),
                            TypeCarburant.valueOf(resultats.getString("carburant")),
                            Transmission.valueOf(resultats.getString("transmission")),
                            resultats.getString("couleur"),
                            resultats.getString("ville"),
                            TypeVendeur.valueOf(resultats.getString("type_vendeur")),
                            resultats.getDate("date_publication").toLocalDate(),
                            resultats.getString("description")
                    );

                    // on ajoute la voiture crée à notre liste
                    voitures.add(voiture);
                }
            }

        }catch (SQLException e){
            throw new RuntimeException("Erreur lors de la récupération des voitures.", e);
        }

        return voitures;
    }

    // =============== Trouver une annonce par ID ================== //

    @Override
    public Optional<Voiture> trouverParId(int id) {
        try (Connection connexion = DatabaseConnection.connexion()) {

            // On cherche unne voiture par Id
            // Le ? sera remplacé par la valeur de id avec setInt().
            String sql = """
                SELECT
                    v.id,
                    m.nom AS marque,
                    v.modele,
                    v.annee,
                    v.kilometrage,
                    v.prix,
                    v.carburant,
                    v.transmission,
                    v.couleur,
                    v.ville,
                    v.type_vendeur,
                    v.date_publication,
                    v.description
                FROM voitures v
                JOIN marques m ON v.id_marque = m.id
                WHERE v.id = ?
                """;

            // On prépare la requête SQL.
            try (PreparedStatement statement = connexion.prepareStatement(sql)) {

                // On remplace le premier ? par la valeur de l'id
                statement.setInt(1, id);

                // On exécute la requête et on récupère le résultat
                try (ResultSet resultats = statement.executeQuery()) {

                    // Si une voiture a été trouvée...
                    if (resultats.next()) {

                        // On transforme la ligne PostgreSQL en objet voiture
                        Voiture voiture = new Voiture(
                                resultats.getInt("id"),
                                resultats.getString("marque"),
                                resultats.getString("modele"),
                                resultats.getInt("annee"),
                                resultats.getInt("kilometrage"),
                                resultats.getInt("prix"),
                                TypeCarburant.valueOf(resultats.getString("carburant")),
                                Transmission.valueOf(resultats.getString("transmission")),
                                resultats.getString("couleur"),
                                resultats.getString("ville"),
                                TypeVendeur.valueOf(resultats.getString("type_vendeur")),
                                resultats.getDate("date_publication").toLocalDate(),
                                resultats.getString("description")
                        );

                        // La voiture existe : on la retourne dans un Optional
                        return Optional.of(voiture);
                    }
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la voiture.", e);
        }

        // Aucune voiture ne correspond à l'id.
        return Optional.empty();
    }

    // ========== On récupère l'ID de la marque =========== //

    private int trouverIdMarque(String nomMarque) {

        String sql = "SELECT id FROM marques WHERE nom = ?";

        try (Connection connexion = DatabaseConnection.connexion();
             PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setString(1, nomMarque);

            try (ResultSet resultats = statement.executeQuery()) {
                if (resultats.next()) {
                    return resultats.getInt("id");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la marque.", e);
        }

        throw new IllegalArgumentException("La marque '" + nomMarque + "' n'existe pas.");
    }

    // =============== Ajouter une voiture à la base ================ //

    @Override
    public Voiture ajouter(Voiture voiture) {

        int idMarque = trouverIdMarque(voiture.getMarque());

        String sql = """
            INSERT INTO voitures (
                id_marque,
                modele,
                annee,
                kilometrage,
                prix,
                carburant,
                transmission,
                couleur,
                ville,
                type_vendeur,
                date_publication,
                description
            )
                VALUES (?, ?, ?, ?, ?, ?::type_carburant, ?::transmission, ?, ?, ?::type_vendeur, ?, ?)             """;

        try (Connection connexion = DatabaseConnection.connexion();
             PreparedStatement statement = connexion.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, idMarque);
            statement.setString(2, voiture.getModele());
            statement.setInt(3, voiture.getAnnee());
            statement.setInt(4, voiture.getKilometrage());
            statement.setInt(5, voiture.getPrix());
            statement.setString(6, voiture.getCarburant().name());
            statement.setString(7, voiture.getTransmission().name());
            statement.setString(8, voiture.getCouleur());
            statement.setString(9, voiture.getVille());
            statement.setString(10, voiture.getTypeVendeur().name());
            statement.setDate(11, java.sql.Date.valueOf(voiture.getDatePublication()));
            statement.setString(12, voiture.getDescription());

            statement.executeUpdate();

            try (ResultSet resultats = statement.getGeneratedKeys()) {

                if (resultats.next()) {

                    int nouvelId = resultats.getInt(1);

                    return new Voiture(
                            nouvelId,
                            voiture.getMarque(),
                            voiture.getModele(),
                            voiture.getAnnee(),
                            voiture.getKilometrage(),
                            voiture.getPrix(),
                            voiture.getCarburant(),
                            voiture.getTransmission(),
                            voiture.getCouleur(),
                            voiture.getVille(),
                            voiture.getTypeVendeur(),
                            voiture.getDatePublication(),
                            voiture.getDescription()
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la voiture.", e);
        }

        throw new IllegalStateException("La voiture a été ajoutée mais son ID n'a pas pu être récupéré.");
    }

    // ============= Modifier une annonce =============== //

    @Override
    public void modifier(Voiture voiture) {

        // 1. On récupère l'ID de la marque
        int idMarque = trouverIdMarque(voiture.getMarque());

        // 2. Requête SQL pour modifier la voiture
        String sql = """
            UPDATE voitures
            SET
                id_marque = ?,
                modele = ?,
                annee = ?,
                kilometrage = ?,
                prix = ?,
                carburant = ?::type_carburant,
                transmission = ?::transmission,
                couleur = ?,
                ville = ?,
                type_vendeur = ?::type_vendeur,
                date_publication = ?,
                description = ?
            WHERE id = ?
            """;

        try (Connection connexion = DatabaseConnection.connexion();
             PreparedStatement statement = connexion.prepareStatement(sql)) {

            // 3. On remplit les paramètres
            statement.setInt(1, idMarque);
            statement.setString(2, voiture.getModele());
            statement.setInt(3, voiture.getAnnee());
            statement.setInt(4, voiture.getKilometrage());
            statement.setInt(5, voiture.getPrix());
            statement.setString(6, voiture.getCarburant().name());
            statement.setString(7, voiture.getTransmission().name());
            statement.setString(8, voiture.getCouleur());
            statement.setString(9, voiture.getVille());
            statement.setString(10, voiture.getTypeVendeur().name());
            statement.setDate(
                    11,
                    java.sql.Date.valueOf(voiture.getDatePublication())
            );
            statement.setString(12, voiture.getDescription());

            // 4. ID de la voiture à modifier
            statement.setInt(13, voiture.getId());

            // 5. Exécution de la requête
            int lignesModifiees = statement.executeUpdate();

            // 6. Vérification
            if (lignesModifiees == 0) {
                throw new IllegalArgumentException(
                        "Aucune voiture trouvée avec l'ID " + voiture.getId()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur lors de la modification de la voiture.", e);
        }
    }

    // ============= Supprimer une annonce ============= //

    @Override
    public void supprimer(int id) {

        // Requête SQL pour supprimer la voiture
        String sql = "DELETE FROM voitures WHERE id = ?";

        try (Connection connexion = DatabaseConnection.connexion();
             PreparedStatement statement = connexion.prepareStatement(sql)) {

            // On indique l'ID de la voiture à supprimer
            statement.setInt(1, id);

            // On exécute la suppression
            int lignesSupprimees = statement.executeUpdate();

            // Vérification
            if (lignesSupprimees == 0) {
                throw new IllegalArgumentException(
                        "Aucune voiture trouvée avec l'ID " + id
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la voiture.", e);
        }
    }
}
