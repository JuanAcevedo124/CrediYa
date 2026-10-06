package com.crediya.dao;

import com.crediya.model.Empleado;
import com.crediya.util.DatabaseConnection;
import com.crediya.util.FileManager;
import java.sql.*;
import java.util.*;

/**
 * Patrón DAO: separa acceso a datos de la lógica.
 * Guarda en ARCHIVO y en MySQL (si hay conexión).
 */
public class EmpleadoDAO implements IGenericDAO<Empleado> {
    private static final String FILE = "data/empleados.txt";

    @Override

    public void guardar(Empleado e) {
        // 1. Archivo (siempre) con ID sincronizado archivo/BD
        if (e.getId() == 0) e.setId(siguienteId());
        FileManager.agregarLinea(FILE, e.toFileString());

        // 2. MySQL (si hay conexión)
        try (Connection c = DatabaseConnection.getInstancia().getConnection()) {
            if (c == null) return;
            String sql = "INSERT INTO empleados(id, nombre, documento, rol, correo, salario) VALUES(?,?,?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, e.getId());
                ps.setString(2, e.getNombre());
                ps.setString(3, e.getDocumento());
                ps.setString(4, e.getRol());
                ps.setString(5, e.getCorreo());
                ps.setDouble(6, e.getSalario());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("[MySQL] No se pudo guardar empleado: " + ex.getMessage());
        }
    }

    private int siguienteId() {
        int maxFile = listarDeArchivo().stream().mapToInt(Empleado::getId).max().orElse(0);
        int maxDb = 0;
        try (Connection c = DatabaseConnection.getInstancia().getConnection()) {
            if (c != null) try (Statement st = c.createStatement();
                 ResultSet rs = st.executeQuery("SELECT MAX(id) FROM empleados")) {
                if (rs.next()) maxDb = rs.getInt(1);
            }
        } catch (Exception ignored) {}
        return Math.max(maxFile, maxDb) + 1;
    }

    public List<Empleado> listarDeArchivo() {
        List<Empleado> lista = new ArrayList<>();
        for (String linea : FileManager.leerLineas(FILE)) {
            if (!linea.trim().isEmpty()) lista.add(Empleado.fromFileString(linea));
        }
        return lista;
    }

    @Override
    public List<Empleado> listar() {
        // Intenta MySQL primero, si falla usa archivo
        try (Connection c = DatabaseConnection.getInstancia().getConnection()) {
            if (c != null) {
                List<Empleado> lista = new ArrayList<>();
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery("SELECT * FROM empleados")) {
                    while (rs.next()) {
                        lista.add(new Empleado(rs.getInt("id"), rs.getString("nombre"),
                                rs.getString("documento"), rs.getString("correo"),
                                rs.getString("rol"), rs.getDouble("salario")));
                    }
                }
                return lista;
            }
        } catch (SQLException ex) {
            System.out.println("[MySQL] Usando archivo: " + ex.getMessage());
        }
        return listarDeArchivo();
    }
}

