package com.crediya.dao;

import com.crediya.model.Pago;
import com.crediya.util.DatabaseConnection;
import com.crediya.util.FileManager;
import java.sql.*;
import java.util.*;

public class PagoDAO implements IGenericDAO<Pago> {
    private static final String FILE = "data/pagos.txt";

    @Override

    public void guardar(Pago p) {
        if (p.getId() == 0) p.setId(siguienteId());
        FileManager.agregarLinea(FILE, p.toFileString());

        try (Connection c = DatabaseConnection.getInstancia().getConnection()) {
            if (c == null) return;
            String sql = "INSERT INTO pagos(id, prestamo_id, fecha_pago, monto) VALUES(?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, p.getId());
                ps.setInt(2, p.getPrestamoId());
                ps.setDate(3, java.sql.Date.valueOf(p.getFechaPago()));
                ps.setDouble(4, p.getMonto());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("[MySQL] No se pudo guardar pago: " + ex.getMessage());
        }
    }

    private int siguienteId() {
        int maxFile = listarDeArchivo().stream().mapToInt(Pago::getId).max().orElse(0);
        int maxDb = 0;
        try (Connection c = DatabaseConnection.getInstancia().getConnection()) {
            if (c != null) try (Statement st = c.createStatement();
                 ResultSet rs = st.executeQuery("SELECT MAX(id) FROM pagos")) {
                if (rs.next()) maxDb = rs.getInt(1);
            }
        } catch (Exception ignored) {}
        return Math.max(maxFile, maxDb) + 1;
    }

    public List<Pago> listarDeArchivo() {
        List<Pago> lista = new ArrayList<>();
        for (String linea : FileManager.leerLineas(FILE)) {
            if (!linea.trim().isEmpty()) lista.add(Pago.fromFileString(linea));
        }
        return lista;
    }

    @Override
    public List<Pago> listar() {
        try (Connection c = DatabaseConnection.getInstancia().getConnection()) {
            if (c != null) {
                List<Pago> lista = new ArrayList<>();
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery("SELECT * FROM pagos")) {
                    while (rs.next()) {
                        lista.add(new Pago(rs.getInt("id"), rs.getInt("prestamo_id"),
                                rs.getDate("fecha_pago").toLocalDate(), rs.getDouble("monto")));
                    }
                }
                return lista;
            }
        } catch (SQLException ex) {
            System.out.println("[MySQL] Usando archivo: " + ex.getMessage());
        }
        return listarDeArchivo();
    }

    /** Histórico de un préstamo ordenado por fecha. Usa Stream + Lambda. */
    public List<Pago> listarPorPrestamo(int prestamoId) {
        return listar().stream()
                .filter(p -> p.getPrestamoId() == prestamoId)
                .sorted((a, b) -> a.getFechaPago().compareTo(b.getFechaPago()))
                .toList();
    }
}

