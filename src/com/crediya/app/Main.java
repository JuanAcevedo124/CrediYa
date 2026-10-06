package com.crediya.app;

import com.crediya.service.CrediYaService;
import com.crediya.util.DatabaseConnection;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final CrediYaService svc = new CrediYaService();

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   CREDIYA S.A.S. - Gestion de Prestamos");
        System.out.println("==============================================");
        System.out.println("   Estado MySQL: " + (DatabaseConnection.getInstancia().hayConexion() ? "CONECTADO" : "modo ARCHIVOS"));
        int op;
        do {
            mostrarMenu();
            op = leerInt();
            try {
                switch (op) {
                    case 1 -> {
                        System.out.print("Nombre: "); String n = sc.nextLine();
                        System.out.print("Documento: "); String d = sc.nextLine();
                        System.out.print("Correo: "); String c = sc.nextLine();
                        System.out.print("Rol: "); String r = sc.nextLine();
                        System.out.print("Salario: "); double s = leerDouble();
                        svc.registrarEmpleado(n, d, c, r, s);
                        System.out.println("Empleado guardado (archivo + BD).");
                    }
                    case 2 -> svc.listarEmpleados().forEach(System.out::println);
                    case 3 -> {
                        System.out.print("Nombre: "); String n = sc.nextLine();
                        System.out.print("Documento: "); String d = sc.nextLine();
                        System.out.print("Correo: "); String co = sc.nextLine();
                        System.out.print("Teléfono: "); String t = sc.nextLine();
                        svc.registrarCliente(n, d, co, t);
                        System.out.println("Cliente guardado.");
                    }
                    case 4 -> svc.listarClientes().forEach(System.out::println);
                    case 5 -> {
                        System.out.print("ID cliente: "); int cli = leerInt();
                        System.out.print("ID empleado: "); int emp = leerInt();
                        System.out.print("Monto: "); double m = leerDouble();
                        System.out.print("Interés %: "); double i = leerDouble();
                        System.out.print("Cuotas: "); int cu = leerInt();
                        var p = svc.crearPrestamo(cli, emp, m, i, cu);
                        System.out.println("Creado: " + p);
                    }
                    case 6 -> {
                        svc.listarPrestamos().forEach(System.out::println);
                        System.out.print("¿Ver préstamos de un cliente? (id o 0=no): ");
                        int id = leerInt();
                        if (id != 0) svc.prestamosPorCliente(id).forEach(System.out::println);
                    }
                    case 7 -> {
                        System.out.print("ID préstamo: "); int id = leerInt();
                        System.out.print("Monto abono: "); double m = leerDouble();
                        svc.registrarPago(id, m);
                        System.out.println("Saldo pendiente: " + svc.saldoPendiente(id));
                    }
                    case 8 -> {
                        System.out.print("ID préstamo: "); int id = leerInt();
                        svc.historicoPagos(id).forEach(System.out::println);
                        System.out.println("Total pagado: " + svc.totalPagado(id));
                        System.out.println("Saldo: " + svc.saldoPendiente(id));
                    }
                    case 9 -> {
                        System.out.println("-- Activos --"); svc.prestamosActivos().forEach(System.out::println);
                        System.out.println("-- Vencidos --"); svc.prestamosVencidos().forEach(System.out::println);
                        System.out.println("-- Morosos --"); svc.clientesMorosos().forEach(System.out::println);
                        System.out.println("Total cartera activa: " + svc.totalCarteraActiva());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (op != 0);
        System.out.println("¡Hasta luego!");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("---------- MENU PRINCIPAL ----------");
        System.out.println("1. Registrar empleado");
        System.out.println("2. Listar empleados");
        System.out.println("3. Registrar cliente");
        System.out.println("4. Listar clientes");
        System.out.println("5. Crear prestamo");
        System.out.println("6. Listar prestamos");
        System.out.println("7. Registrar pago (abono)");
        System.out.println("8. Historico pagos / saldo");
        System.out.println("9. Ver reportes");
        System.out.println("0. Salir");
        System.out.println("------------------------------------");
        System.out.print("Elija una opcion: ");
    }

    private static int leerInt() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }
    private static double leerDouble() {
        try { return Double.parseDouble(sc.nextLine().trim()); }
        catch (Exception e) { return 0; }
    }
}
