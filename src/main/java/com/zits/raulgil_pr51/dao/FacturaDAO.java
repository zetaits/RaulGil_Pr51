package com.zits.raulgil_pr51.dao;

import com.zits.raulgil_pr51.model.Factura;
import com.zits.raulgil_pr51.model.GastoMensual;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FacturaDAO {
    private final DatabaseManager dbManager;

    public FacturaDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public int insertar(Factura factura) throws SQLException {
        String sql = "INSERT INTO FACTURA(id_cliente, fecha_emision, periodo_inicio, periodo_fin) VALUES(?, ?, ?, ?)";
        try (Connection conn = dbManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, factura.getIdCliente());
            pstmt.setString(2, factura.getFechaEmision());
            pstmt.setString(3, factura.getPeriodoInicio());
            pstmt.setString(4, factura.getPeriodoFin());

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

    public ObservableList<Factura> obtenerTodasConDetalles() {
        ObservableList<Factura> lista = FXCollections.observableArrayList();
        String sql = "SELECT f.id_factura, f.fecha_emision, c.nombre || ' ' || c.apellidos as cliente, SUM(d.consumo_kwh * d.precio_kwh) * 1.21 as total "
                +
                "FROM FACTURA f " +
                "JOIN CLIENTE c ON f.id_cliente = c.id_cliente " +
                "JOIN DETALLE_FACTURA d ON f.id_factura = d.id_factura " +
                "GROUP BY f.id_factura ORDER BY f.id_factura DESC";

        try (Connection conn = dbManager.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Factura(
                        rs.getInt("id_factura"),
                        rs.getString("cliente"),
                        rs.getString("fecha_emision"),
                        Math.round(rs.getDouble("total") * 100.0) / 100.0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ObservableList<Factura> obtenerPorCliente(int idCliente) {
        ObservableList<Factura> lista = FXCollections.observableArrayList();
        String sql = "SELECT f.id_factura, f.fecha_emision, c.nombre || ' ' || c.apellidos as cliente, SUM(d.consumo_kwh * d.precio_kwh) * 1.21 as total "
                + "FROM FACTURA f "
                + "JOIN CLIENTE c ON f.id_cliente = c.id_cliente "
                + "JOIN DETALLE_FACTURA d ON f.id_factura = d.id_factura "
                + "WHERE f.id_cliente = ? " // Filter by client
                + "GROUP BY f.id_factura ORDER BY f.id_factura DESC";

        try (Connection conn = dbManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Factura(
                        rs.getInt("id_factura"),
                        rs.getString("cliente"),
                        rs.getString("fecha_emision"),
                        Math.round(rs.getDouble("total") * 100.0) / 100.0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void eliminar(int idFactura) throws SQLException {
        String sql = "DELETE FROM FACTURA WHERE id_factura=?";
        try (Connection conn = dbManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFactura);
            pstmt.executeUpdate();
        }
    }

    public ObservableList<XYChart.Data<String, Number>> obtenerDatosAnalisis(LocalDate inicio, LocalDate fin,
            String tramoFiltro) {
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

        String sql = "SELECT d.tramo, AVG(d.consumo_kwh) as media " +
                "FROM DETALLE_FACTURA d " +
                "JOIN FACTURA f ON d.id_factura = f.id_factura " +
                "WHERE f.fecha_emision BETWEEN ? AND ? ";

        if (!"TODOS".equals(tramoFiltro))
            sql += " AND d.tramo = ? ";
        sql += " GROUP BY d.tramo";

        try (Connection conn = dbManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inicio.toString());
            pstmt.setString(2, fin.toString());

            if (!"TODOS".equals(tramoFiltro))
                pstmt.setString(3, tramoFiltro);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                data.add(new XYChart.Data<>(rs.getString("tramo"), rs.getDouble("media")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private LocalDate obtenerFechaEmision(int idFactura) {
        String sql = "SELECT fecha_emision FROM FACTURA WHERE id_factura = ?";
        try (Connection conn = dbManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFactura);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return LocalDate.parse(rs.getString("fecha_emision"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return LocalDate.now();
    }

    public List<GastoMensual> obtenerGastosHistoricos(int idFactura) {
        LocalDate fechaFactura = obtenerFechaEmision(idFactura);
        YearMonth invoiceMonth = YearMonth.from(fechaFactura);

        Map<YearMonth, Double> gastosMap = new TreeMap<>();
        for (int i = 0; i < 12; i++) {
            gastosMap.put(invoiceMonth.minusMonths(i), 0.0);
        }

        String sql = "SELECT strftime('%Y-%m', f.fecha_emision) as mes, SUM(d.consumo_kwh * d.precio_kwh) * 1.21 as total "
                +
                "FROM FACTURA f " +
                "JOIN DETALLE_FACTURA d ON f.id_factura = d.id_factura " +
                "WHERE f.fecha_emision <= ? AND f.fecha_emision > date(?, '-12 months') " +
                "GROUP BY mes";

        try (Connection conn = dbManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fechaFactura.toString());
            pstmt.setString(2, fechaFactura.toString());

            ResultSet rs = pstmt.executeQuery();
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM");

            while (rs.next()) {
                try {
                    YearMonth ym = YearMonth.parse(rs.getString("mes"), parser);
                    if (gastosMap.containsKey(ym)) {
                        gastosMap.put(ym, rs.getDouble("total"));
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<GastoMensual> lista = new ArrayList<>();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM", new Locale("es", "ES"));

        for (Map.Entry<YearMonth, Double> entry : gastosMap.entrySet()) {
            String formattedDate = entry.getKey().format(outputFormatter);
            formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
            formattedDate = formattedDate.replace(".", "");
            lista.add(new GastoMensual(formattedDate, entry.getValue()));
        }

        return lista;
    }

    public List<GastoMensual> obtenerGastosHistoricos() {
        return obtenerGastosHistoricosDesdeFecha(LocalDate.now());
    }

    private List<GastoMensual> obtenerGastosHistoricosDesdeFecha(LocalDate fechaRef) {
        YearMonth currentMonth = YearMonth.from(fechaRef);
        Map<YearMonth, Double> gastosMap = new TreeMap<>();
        for (int i = 0; i < 12; i++) {
            gastosMap.put(currentMonth.minusMonths(i), 0.0);
        }

        String sql = "SELECT strftime('%Y-%m', f.fecha_emision) as mes, SUM(d.consumo_kwh * d.precio_kwh) * 1.21 as total "
                +
                "FROM FACTURA f " +
                "JOIN DETALLE_FACTURA d ON f.id_factura = d.id_factura " +
                "WHERE f.fecha_emision <= ? AND f.fecha_emision > date(?, '-12 months') " +
                "GROUP BY mes";

        try (Connection conn = dbManager.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fechaRef.toString());
            pstmt.setString(2, fechaRef.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM");
                while (rs.next()) {
                    try {
                        YearMonth ym = YearMonth.parse(rs.getString("mes"), parser);
                        if (gastosMap.containsKey(ym)) {
                            gastosMap.put(ym, rs.getDouble("total"));
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<GastoMensual> lista = new ArrayList<>();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM", new Locale("es", "ES"));

        for (Map.Entry<YearMonth, Double> entry : gastosMap.entrySet()) {
            String formattedDate = entry.getKey().format(outputFormatter);
            formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
            formattedDate = formattedDate.replace(".", "");
            lista.add(new GastoMensual(formattedDate, entry.getValue()));
        }
        return lista;
    }
}
