# CrediYa S.A.S.: sistema de préstamos y cobros

Aplicación de consola en Java para gestionar empleados, clientes, préstamos y pagos. Guarda los datos en archivos de texto y, si hay conexión, también en MySQL mediante JDBC.

## Funcionalidades

- Registro y listado de empleados y clientes.
- Creación de préstamos con cálculo automático de intereses y cuotas.
- Registro de abonos e historial de pagos con saldo.
- Reportes de préstamos activos, vencidos, clientes morosos y total de cartera.
- Funcionamiento con archivos cuando MySQL no está disponible.

## Tecnologías

Java 17, MySQL 8, JDBC, Streams y expresiones lambda.

## Diseño

| Concepto | Aplicación |
|---|---|
| Herencia | `Empleado` y `Cliente` extienden la clase abstracta `Persona` |
| Polimorfismo | Redefinición de `toFileString()` y `toString()` |
| Encapsulamiento | El total y la cuota se calculan dentro de `Prestamo` |
| Responsabilidad única | DAO por entidad; `FileManager` para archivos; `CrediYaService` para el negocio |
| Patrones | DAO y Singleton (`DatabaseConnection`) |

## Estructura

```text
CrediYa/
├── src/com/crediya/
│   ├── app/         Punto de entrada
│   ├── dao/         Acceso a datos
│   ├── exception/   Excepciones propias
│   ├── model/       Entidades
│   ├── service/     Reglas de negocio
│   └── util/        Configuración, conexión, archivos y validación
├── sql/schema.sql
├── data/            Persistencia en archivos
├── docs/            Diagrama UML
└── lib/             Conector de MySQL
```

## Instalación

Requisitos: Java 17 o superior y MySQL 8 (opcional). Comandos para PowerShell.

1. Copia la configuración y escribe tu contraseña en `db.properties`, que está excluido de Git:

   ```powershell
   Copy-Item db.properties.example db.properties
   ```

2. Crea la base de datos:

   ```powershell
   Get-Content sql/schema.sql | mysql -u root -p
   ```

3. Compila y ejecuta:

   ```powershell
   .\compilar.bat
   .\run.bat
   ```

## Ejemplo

Un préstamo de 1.000.000 con interés del 10 % a 12 cuotas genera un total de 1.100.000 y una cuota de 91.666,67. Después de un abono de 200.000, el saldo es de 900.000.

## Documentación

El diagrama de clases está en [docs/UML.md](docs/UML.md) y [docs/UML.puml](docs/UML.puml).

## Autor

Juan José Acevedo Morales, desarrollador de software junior.
[Portafolio](https://juanacevedo124.github.io/Portafolio/) | [GitHub](https://github.com/JuanAcevedo124) | [LinkedIn](https://www.linkedin.com/in/juan-acevedo-b24759450/)
