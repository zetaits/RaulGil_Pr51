# Manual del Desarrollador - ElectroFactura

## 1. Visión General
**ElectroFactura** es una aplicación de escritorio desarrollada en Java para la gestión de clientes y facturación eléctrica. Sigue el patrón de arquitectura **MVC (Modelo-Vista-Controlador)** para separar la lógica de negocio de la interfaz de usuario.

## 2. Tecnologías Utilizadas
*   **Lenguaje**: Java 17+
*   **Interfaz Gráfica**: JavaFX
*   **Base de Datos**: SQLite (JDBC)
*   **Informes**: JasperReports
*   **Gestión de Dependencias**: Gradle

## 3. Arquitectura del Proyecto

### Estructura de Paquetes
El código fuente se encuentra en `src/main/java/com/zits/raulgil_pr51/` y se organiza de la siguiente manera:

*   **`model`**: Contiene las clases POJO (Plain Old Java Objects) que representan las entidades del dominio.
    *   `Cliente`: Representa a un cliente (nombre, nif, dirección).
    *   `Factura`: Representa el encabezado de una factura.
    *   `DetalleFactura`: Representa las líneas de consumo de una factura.
    *   `GastoMensual`: Clase auxiliar para la generación de gráficos en informes.

*   **`dao` (Data Access Object)**: Capa de persistencia. Se encarga de la comunicación con la base de datos.
    *   `DatabaseManager`: Gestiona la conexión JDBC y la creación inicial de tablas.
    *   `ClienteDAO`: Operaciones CRUD para la tabla `CLIENTE`.
    *   `FacturaDAO`: Operaciones CRUD para la tabla `FACTURA`.
    *   `DetalleFacturaDAO`: Operaciones para `DETALLE_FACTURA`.

*   **`service`**: Capa de servicios que agrupa lógica de negocio más compleja.
    *   `HelpService`: Lógica para cargar y transformar la documentación de ayuda (Markdown -> HTML).
    *   `ReportService`: Lógica para la compilación y generación de informes PDF con JasperReports.

*   **`view`** (Resources): Los archivos FXML (`vista.fxml`, `help.fxml`) y hojas de estilo (`estilos.css`) se encuentran en `src/main/resources`.

*   **Controladores (Root)**:
    *   `Controller.java`: Controlador principal que gestiona la interacción de la ventana principal (`vista.fxml`).
    *   `HelpController.java`: Controlador específico para la ventana de ayuda (`help.fxml`).

## 4. Flujo de Datos
1.  El usuario interactúa con la interfaz (JavaFX).
2.  El `Controller` captura el evento.
3.  El `Controller` invoca al `DAO` correspondiente o al `Service` necesario.
4.  El `DAO` ejecuta consultas SQL contra SQLite.
5.  Los resultados se devuelven como objetos del `model` y se actualiza la Vista.

## 5. Configuración del Entorno
Para compilar y ejecutar el proyecto:
```bash
./gradlew run
```
Para generar Javadoc:
```bash
./gradlew javadoc
```
Los archivos generados se ubicarán en `build/` y `docs/`.
