# CrediYa S.A.S. - Sistema de Préstamos y Cobros

## Descripción
Sistema de consola en Java para gestionar empleados, clientes, préstamos y pagos.
Persistencia dual: archivos `.txt` (siempre) + MySQL con JDBC (si hay conexión).

## POO aplicada
- **Herencia:** `Empleado` y `Cliente` heredan de `Persona` (abstracta).
- **Polimorfismo:** `toFileString()` y `toString()` redefinidos; `Prestamo::isVencido` como referencia de método.
- **Encapsulamiento:** atributos privados + getters/setters; cálculo de `total` y `cuota` dentro de `Prestamo`.
- **SOLID:** DAO separados por entidad, `FileManager` solo archivos, `DatabaseConnection` Singleton solo conexión, `CrediYaService` solo negocio.
- **Patrones:** DAO + Singleton.
- **Streams/Lambdas:** en reportes (`filter`, `mapToDouble`, `collect`).

## Requisitos
- Java 17+ (`java -version`)
- MySQL 8.0 corriendo + BD creada con `sql/schema.sql`
- Conector ya incluido en `lib/mysql-connector-j.jar`

## Configurar clave MySQL
Editar `src/com/crediya/util/DatabaseConnection.java`:
```java
private static final String PASSWORD = "TU_CLAVE";
```

## Crear BD (PowerShell, NO usa < )
```powershell
Get-Content "sql/schema.sql" | mysql -u root -p
mysql -u root -p -e "USE crediya_db; SHOW TABLES;"
```

## Compilar y ejecutar
Doble clic o en terminal:
```
.\compilar.bat
.\run.bat
```
O manual:
```powershell
javac -cp "lib/mysql-connector-j.jar" -d out (Get-ChildItem -Recurse -Filter "*.java" -Path "src" | ForEach-Object FullName)
java -cp "out;lib/mysql-connector-j.jar" com.crediya.app.Main
```

## Ejemplo de uso
1. Registrar empleado (rol asesor) -> id 1
2. Registrar cliente -> id 1
3. Crear préstamo: cliente 1, empleado 1, monto 1000000, interés 10, cuotas 12
   -> total = 1100000, cuota = 91666.66
4. Registrar pago de 200000 -> saldo = 900000
5. Reportes: activos, vencidos, morosos, total cartera.

## Estructura
```
src/com/crediya/model/ Persona, Empleado, Cliente, Prestamo, Pago
src/com/crediya/util/ DatabaseConnection, FileManager
src/com/crediya/dao/ EmpleadoDAO, ClienteDAO, PrestamoDAO, PagoDAO
src/com/crediya/service/ CrediYaService
src/com/crediya/app/ Main
sql/schema.sql  lib/mysql-connector-j.jar  data/*.txt  docs/UML.puml
```

## UML
Ver `docs/UML.puml` (abrir en https://plantuml.com o extensión PlantUML en VSCode).
