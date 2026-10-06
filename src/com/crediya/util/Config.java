package com.crediya.util;

import java.io.*;
import java.util.Properties;

/**
 * Lee db.properties (patrón: configuración externa, no hardcodear claves).
 * Si no existe, usa valores por defecto.
 */
public final class Config {
    private static final Properties P = new Properties();
    static {
        try {
            File f1 = new File("db.properties");
            File f2 = new File("C:/Users/juana/OneDrive/Desktop/Proyecto Java/db.properties");
            InputStream in = null;
            if (f1.exists()) in = new FileInputStream(f1);
            else if (f2.exists()) in = new FileInputStream(f2);
            if (in != null) { P.load(in); in.close(); }
        } catch (Exception e) {
            System.out.println("[Config] Usando valores por defecto.");
        }
    }
    private Config() {}
    public static String url() { return P.getProperty("db.url", "jdbc:mysql://localhost:3306/crediya_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"); }
    public static String user() { return P.getProperty("db.user", "root"); }
    public static String password() { return P.getProperty("db.password", "TU_CLAVE_AQUI"); }
}
