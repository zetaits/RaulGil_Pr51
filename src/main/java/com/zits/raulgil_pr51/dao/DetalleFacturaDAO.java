package com.zits.raulgil_pr51.dao;

import com.zits.raulgil_pr51.model.DetalleFactura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetalleFacturaDAO {
    private final DatabaseManager dbManager;

    public DetalleFacturaDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void insertar(DetalleFactura detalle) throws SQLException {
        String sql = "INSERT INTO DETALLE_FACTURA(id_factura, tramo, consumo_kwh, precio_kwh) VALUES(?, ?, ?, ?)";
        try (Connection conn = dbManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, detalle.getIdFactura());
            pstmt.setString(2, detalle.getTramo());
            pstmt.setDouble(3, detalle.getConsumoKwh());
            pstmt.setDouble(4, detalle.getPrecioKwh());

            pstmt.executeUpdate();
        }
    }

    public void eliminarPorFactura(int idFactura) throws SQLException {
        String sql = "DELETE FROM DETALLE_FACTURA WHERE id_factura=?";
        try (Connection conn = dbManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFactura);
            pstmt.executeUpdate();
        }
    }
}
