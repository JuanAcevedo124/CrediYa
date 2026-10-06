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
        List<Cliente> todos = listarDeArchivo();
        int nuevoId = todos.stream().mapToInt(Cliente::getId).max().orElse(0) + 1;
        if (c.getId() == 0) c.setId(nuevoId);
        FileManager.agregarLinea(FILE, c.toFileString());

        try (Connection con = DatabaseConnection.getInstancia().getConnection()) {
            if (con == null) return;
            String sql = "INSERT INTO clientes(nombre, documento, correo, telefono) VALUES(?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, c.getNombre());
                ps.setString(2, c.getDocumento());
                ps.setString(3, c.getCorreo());
                ps.setString(4, c.getTelefono());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("[MySQL] No se pudo guardar cliente: " + ex.getMessage());
        }
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

