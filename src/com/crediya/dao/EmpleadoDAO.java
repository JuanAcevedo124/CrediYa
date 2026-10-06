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
public class EmpleadoDAO {
    private static final String FILE = "data/empleados.txt";

    public void guardar(Empleado e) {
        // 1. Archivo (siempre)
        List<Empleado> todos = listarDeArchivo();
        int nuevoId = todos.stream().mapToInt(Empleado::getId).max().orElse(0) + 1;
        if (e.getId() == 0) e.setId(nuevoId);
        FileManager.agregarLinea(FILE, e.toFileString());

        // 2. MySQL (si hay conexión)
        try (Connection c = DatabaseConnection.getInstancia().getConnection()) {
            if (c == null) return;
            String sql = "INSERT INTO empleados(nombre, documento, rol, correo, salario) VALUES(?,?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, e.getNombre());
                ps.setString(2, e.getDocumento());
                ps.setString(3, e.getRol());
                ps.setString(4, e.getCorreo());
                ps.setDouble(5, e.getSalario());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("[MySQL] No se pudo guardar empleado: " + ex.getMessage());
        }
    }

    public List<Empleado> listarDeArchivo() {
        List<Empleado> lista = new ArrayList<>();
        for (String linea : FileManager.leerLineas(FILE)) {
            if (!linea.trim().isEmpty()) lista.add(Empleado.fromFileString(linea));
        }
        return lista;
    }

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
