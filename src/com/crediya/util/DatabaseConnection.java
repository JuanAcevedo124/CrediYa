package com.crediya.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Patrón Singleton: una sola instancia de conexión.
 * Si MySQL no está disponible, devuelve null y el sistema sigue con archivos.
 */
public class DatabaseConnection {
    private static DatabaseConnection instancia;

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
            return DriverManager.getConnection(Config.url(), Config.user(), Config.password());
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
