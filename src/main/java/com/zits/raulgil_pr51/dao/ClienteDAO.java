package com.zits.raulgil_pr51.dao;

import com.zits.raulgil_pr51.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class ClienteDAO {
    private final DatabaseManager dbManager;

    public ClienteDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public int insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO CLIENTE(nombre, apellidos, nif, direccion) VALUES(?, ?, ?, ?)";
        try (Connection conn = dbManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellidos());
            pstmt.setString(3, cliente.getNif());
            pstmt.setString(4, cliente.getDireccion());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating form failed, no ID obtained.");
                }
            }
        }
    }

    public ObservableList<Cliente> obtenerTodos() {
        ObservableList<Cliente> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM CLIENTE ORDER BY nombre, apellidos";
        try (Connection conn = dbManager.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("nif"),
                        rs.getString("direccion")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
