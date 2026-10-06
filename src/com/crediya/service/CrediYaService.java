package com.crediya.service;

import com.crediya.dao.*;
import com.crediya.exception.ValidacionException;
import com.crediya.model.*;
import com.crediya.util.Validador;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

/**
 * Lógica de negocio + Reportes con Lambda y Stream API.
 */
public class CrediYaService {
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final PagoDAO pagoDAO = new PagoDAO();

    // --- Empleados / Clientes ---
    public void registrarEmpleado(String nombre, String doc, String correo, String rol, double salario) {
        empleadoDAO.guardar(new Empleado(0, nombre, doc, correo, rol, salario));
    }
    public List<Empleado> listarEmpleados() { return empleadoDAO.listar(); }

    public void registrarCliente(String nombre, String doc, String correo, String tel) {
        clienteDAO.guardar(new Cliente(0, nombre, doc, correo, tel));
    }
    public List<Cliente> listarClientes() { return clienteDAO.listar(); }

    // --- Préstamos ---
    public Prestamo crearPrestamo(int clienteId, int empleadoId, double monto, double interes, int cuotas) throws ValidacionException {
        Validador.positivo(monto, "Monto");
        Validador.noNegativo(interes, "Interes");
        Validador.positivo(cuotas, "Cuotas");
        boolean existeCli = listarClientes().stream().anyMatch(c -> c.getId() == clienteId);
        if (!existeCli) throw new ValidacionException("No existe cliente con id " + clienteId + ". Use 2.Listar empleados / 4.Listar clientes para ver IDs.");
        boolean existeEmp = listarEmpleados().stream().anyMatch(e -> e.getId() == empleadoId);
        if (!existeEmp) throw new ValidacionException("No existe empleado con id " + empleadoId + ".");
        Prestamo p = new Prestamo(0, clienteId, empleadoId, monto, interes, cuotas, LocalDate.now(), "pendiente");
        prestamoDAO.guardar(p);
        return p;
    }
    public List<Prestamo> listarPrestamos() { return prestamoDAO.listar(); }

    public List<Prestamo> prestamosPorCliente(int clienteId) {
        return listarPrestamos().stream()
                .filter(p -> p.getClienteId() == clienteId)
                .toList();
    }

    // --- Pagos ---
    public void registrarPago(int prestamoId, double monto) throws ValidacionException {
        Validador.positivo(monto, "Monto del pago");
        boolean existe = listarPrestamos().stream().anyMatch(p -> p.getId() == prestamoId);
        if (!existe) throw new ValidacionException("No existe prestamo con id " + prestamoId + ".");
        pagoDAO.guardar(new Pago(0, prestamoId, LocalDate.now(), monto));
        // Si ya cubrió el total -> marcar pagado automáticamente
        if (saldoPendiente(prestamoId) <= 0) {
            prestamoDAO.cambiarEstado(prestamoId, "pagado");
        }
    }

    public double totalPagado(int prestamoId) {
        return pagoDAO.listarPorPrestamo(prestamoId).stream()
                .mapToDouble(Pago::getMonto).sum();
    }

    public double saldoPendiente(int prestamoId) {
        return listarPrestamos().stream()
                .filter(p -> p.getId() == prestamoId)
                .findFirst()
                .map(p -> p.getMontoTotal() - totalPagado(prestamoId))
                .orElse(0.0);
    }

    public List<Pago> historicoPagos(int prestamoId) {
        return pagoDAO.listarPorPrestamo(prestamoId);
    }

    // --- REPORTES con Streams/Lambdas ---
    public List<Prestamo> prestamosActivos() {
        return listarPrestamos().stream()
                .filter(p -> "pendiente".equalsIgnoreCase(p.getEstado()))
                .toList();
    }

    public List<Prestamo> prestamosVencidos() {
        return listarPrestamos().stream()
                .filter(Prestamo::isVencido) // method reference = lambda corta
                .toList();
    }

    public List<Cliente> clientesMorosos() {
        Set<Integer> idsMorosos = prestamosVencidos().stream()
                .map(Prestamo::getClienteId)
                .collect(Collectors.toSet());
        return listarClientes().stream()
                .filter(c -> idsMorosos.contains(c.getId()))
                .toList();
    }

    public double totalCarteraActiva() {
        return prestamosActivos().stream()
                .mapToDouble(p -> saldoPendiente(p.getId()))
                .sum();
    }
}
