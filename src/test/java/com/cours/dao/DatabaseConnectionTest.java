package com.cours.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseConnectionTest {

    @Test
    void testerConnexion() throws SQLException {

        Connection connection = DatabaseConnection.connexion();

        assertNotNull(connection);

        connection.close();

    }
}
