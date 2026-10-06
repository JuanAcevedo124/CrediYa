package com.crediya.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Utilidad genérica para persistencia en archivos .txt.
 * Cada línea = un objeto en formato "campo1;campo2;...".
 */
public class FileManager {

    public static List<String> leerLineas(String ruta) {
        try {
            Path p = Paths.get(ruta);
            if (!Files.exists(p)) return new ArrayList<>();
            return Files.readAllLines(p);
        } catch (IOException e) {
            System.out.println("Error leyendo " + ruta + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void escribirLineas(String ruta, List<String> lineas) {
        try {
            Path p = Paths.get(ruta);
            Files.createDirectories(p.getParent() == null ? Paths.get(".") : p.getParent());
            Files.write(p, lineas);
        } catch (IOException e) {
            System.out.println("Error escribiendo " + ruta + ": " + e.getMessage());
        }
    }

    public static void agregarLinea(String ruta, String linea) {
        List<String> actuales = leerLineas(ruta);
        actuales.add(linea);
        escribirLineas(ruta, actuales);
    }
}
