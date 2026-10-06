package com.crediya.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Patrón Singleton: una sola instancia de conexión.
 * Si MySQL no está disponible, devuelve null y el sistema sigue con archivos.
 */
public class DatabaseConnection {
    private static DatabaseConnection instancia;
    private static final String URL = "jdbc:mysql://localhost:3306/crediya_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "1097103214Ace"; // <-- CAMBIA por tu clave de MySQL (la de 12 **** que te funcionó)

    private DatabaseConnection() {}

    public static DatabaseConnection getInstancia() {
        if (instancia == null) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("[AVISO] Sin conexión MySQL (" + e.getMessage() + "). Se usará archivos.");
            return null;
        }
    }

    /** Prueba rápida de conexión. */
    public boolean hayConexion() {
        try (Connection c = getConnection()) {
            return c != null && !c.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
