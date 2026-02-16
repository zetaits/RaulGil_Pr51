package com.zits.raulgil_pr51.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:electricity.db";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void crearTablasSiNoExisten() {
        String sqlC = "CREATE TABLE IF NOT EXISTS CLIENTE (id_cliente INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, apellidos TEXT, nif TEXT, direccion TEXT);";
        String sqlF = "CREATE TABLE IF NOT EXISTS FACTURA (id_factura INTEGER PRIMARY KEY AUTOINCREMENT, id_cliente INTEGER, fecha_emision TEXT, periodo_inicio TEXT, periodo_fin TEXT);";
        String sqlD = "CREATE TABLE IF NOT EXISTS DETALLE_FACTURA (id_detalle INTEGER PRIMARY KEY AUTOINCREMENT, id_factura INTEGER, tramo TEXT, consumo_kwh REAL, precio_kwh REAL, FOREIGN KEY(id_factura) REFERENCES FACTURA(id_factura));";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlC);
            stmt.execute(sqlF);
            stmt.execute(sqlD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetearBDCompleta() throws SQLException {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS DETALLE_FACTURA");
            stmt.execute("DROP TABLE IF EXISTS FACTURA");
            stmt.execute("DROP TABLE IF EXISTS CLIENTE");
            crearTablasSiNoExisten();
        }
    }
}
