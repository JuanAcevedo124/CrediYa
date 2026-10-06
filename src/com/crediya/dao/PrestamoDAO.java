package com.crediya.dao;

import com.crediya.model.Prestamo;
import com.crediya.util.DatabaseConnection;
import com.crediya.util.FileManager;
import java.sql.*;
import java.util.*;

public class PrestamoDAO implements IGenericDAO<Prestamo> {
    private static final String FILE = "data/prestamos.txt";

    @Override

    public void guardar(Prestamo p) {
        List<Prestamo> todos = listarDeArchivo();
        int nuevoId = todos.stream().mapToInt(Prestamo::getId).max().orElse(0) + 1;
        if (p.getId() == 0) p.setId(nuevoId);
        FileManager.agregarLinea(FILE, p.toFileString());

        try (Connection c = DatabaseConnection.getInstancia().getConnection()) {
            if (c == null) return;
            String sql = "INSERT INTO prestamos(cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado) VALUES(?,?,?,?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, p.getClienteId());
                ps.setInt(2, p.getEmpleadoId());
                ps.setDouble(3, p.getMonto());
                ps.setDouble(4, p.getInteres());
                ps.setInt(5, p.getCuotas());
                ps.setDate(6, java.sql.Date.valueOf(p.getFechaInicio()));
                ps.setString(7, p.getEstado());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("[MySQL] No se pudo guardar préstamo: " + ex.getMessage());
        }
    }

    public List<Prestamo> listarDeArchivo() {
        List<Prestamo> lista = new ArrayList<>();
        for (String linea : FileManager.leerLineas(FILE)) {
            if (!linea.trim().isEmpty()) lista.add(Prestamo.fromFileString(linea));
        }
        return lista;
    }

    @Override
    public List<Prestamo> listar() {
        try (Connection c = DatabaseConnection.getInstancia().getConnection()) {
            if (c != null) {
                List<Prestamo> lista = new ArrayList<>();
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery("SELECT * FROM prestamos")) {
                    while (rs.next()) {
                        lista.add(new Prestamo(rs.getInt("id"), rs.getInt("cliente_id"),
                                rs.getInt("empleado_id"), rs.getDouble("monto"),
                                rs.getDouble("interes"), rs.getInt("cuotas"),
                                rs.getDate("fecha_inicio").toLocalDate(),
                                rs.getString("estado")));
                    }
                }
                return lista;
            }
        } catch (SQLException ex) {
            System.out.println("[MySQL] Usando archivo: " + ex.getMessage());
        }
        return listarDeArchivo();
    }

    public void cambiarEstado(int prestamoId, String nuevoEstado) {
        // Archivo
        List<Prestamo> todos = listarDeArchivo();
        List<String> lineas = new ArrayList<>();
        for (Prestamo p : todos) {
            if (p.getId() == prestamoId) p.setEstado(nuevoEstado);
            lineas.add(p.toFileString());
        }
        FileManager.escribirLineas(FILE, lineas);
        // MySQL
        try (Connection c = DatabaseConnection.getInstancia().getConnection()) {
            if (c == null) return;
            try (PreparedStatement ps = c.prepareStatement("UPDATE prestamos SET estado=? WHERE id=?")) {
                ps.setString(1, nuevoEstado);
                ps.setInt(2, prestamoId);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("[MySQL] No se pudo actualizar estado: " + ex.getMessage());
        }
    }
}

