package com.crediya.dao;

import com.crediya.model.Cliente;
import com.crediya.util.DatabaseConnection;
import com.crediya.util.FileManager;
import java.sql.*;
import java.util.*;

public class ClienteDAO implements IGenericDAO<Cliente> {
    private static final String FILE = "data/clientes.txt";

    @Override

    public void guardar(Cliente c) {
        if (c.getId() == 0) c.setId(siguienteId());
        FileManager.agregarLinea(FILE, c.toFileString());

        try (Connection con = DatabaseConnection.getInstancia().getConnection()) {
            if (con == null) return;
            String sql = "INSERT INTO clientes(id, nombre, documento, correo, telefono) VALUES(?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, c.getId());
                ps.setString(2, c.getNombre());
                ps.setString(3, c.getDocumento());
                ps.setString(4, c.getCorreo());
                ps.setString(5, c.getTelefono());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("[MySQL] No se pudo guardar cliente: " + ex.getMessage());
        }
    }

    private int siguienteId() {
        int maxFile = listarDeArchivo().stream().mapToInt(Cliente::getId).max().orElse(0);
        int maxDb = 0;
        try (Connection con = DatabaseConnection.getInstancia().getConnection()) {
            if (con != null) try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT MAX(id) FROM clientes")) {
                if (rs.next()) maxDb = rs.getInt(1);
            }
        } catch (Exception ignored) {}
        return Math.max(maxFile, maxDb) + 1;
    }

    public List<Cliente> listarDeArchivo() {
        List<Cliente> lista = new ArrayList<>();
        for (String linea : FileManager.leerLineas(FILE)) {
            if (!linea.trim().isEmpty()) lista.add(Cliente.fromFileString(linea));
        }
        return lista;
    }

    @Override
    public List<Cliente> listar() {
        try (Connection con = DatabaseConnection.getInstancia().getConnection()) {
            if (con != null) {
                List<Cliente> lista = new ArrayList<>();
                try (Statement st = con.createStatement();
                     ResultSet rs = st.executeQuery("SELECT * FROM clientes")) {
                    while (rs.next()) {
                        lista.add(new Cliente(rs.getInt("id"), rs.getString("nombre"),
                                rs.getString("documento"), rs.getString("correo"),
                                rs.getString("telefono")));
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

