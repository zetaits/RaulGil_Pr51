# ElectroFactura ⚡

**ElectroFactura** es una aplicación de escritorio moderna y eficiente diseñada para la gestión integral de facturación eléctrica. Desarrollada en **JavaFX**, ofrece una interfaz intuitiva para administrar clientes, generar facturas con desglose por tramos horarios y visualizar informes detallados.

## 🚀 Características Principales

*   **Gestión de Clientes**: Alta, modificación y consulta de abonados.
*   **Facturación por Tramos**: Cálculo automático de importes basado en consumo (Punta, Llano, Valle).
*   **Generación de Informes**: Creación de facturas en PDF listas para imprimir con **JasperReports**.
*   **Análisis de Consumo**: Gráficos interactivos para visualizar tendencias de gasto.
*   **Ayuda Integrada**: Sistema de ayuda dual con guía rápida y manual completo.

## 📚 Documentación

El proyecto incluye documentación detallada para diferentes perfiles:

*   **[Manual de Usuario](src/main/resources/docs/manual_usuario.md)**: Guía completa sobre el uso de la aplicación. (Accesible también desde la aplicación en la pestaña "Ayuda").
*   **[Manual del Desarrollador](manual_desarrollador.md)**: Información técnica sobre la arquitectura del proyecto, estructura de paquetes y tecnologías utilizadas.
*   **[Documentación Técnica de la Base de Datos](documentacion_tecnica_bd.md)**: Esquema de la base de datos, diagrama ER y definición de tablas.

## 🛠️ Requisitos y Ejecución

### Requisitos Previos
*   Java JDK 17 o superior.
*   Conexión a internet (para descargar dependencias de Gradle).

### Cómo Ejecutar
Clona el repositorio y ejecuta el siguiente comando en la raíz del proyecto:

```bash
# En Windows
./gradlew run

# En Linux/macOS
./gradlew run
```

## 🏗️ Construcción (Build)

Para generar el ejecutable y compilar el proyecto:

```bash
./gradlew build
```

---
Desarrollado por [Tu Nombre/Usuario] - 2026
