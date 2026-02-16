package com.zits.raulgil_pr51.service;

import com.zits.raulgil_pr51.dao.DatabaseManager;
import com.zits.raulgil_pr51.dao.FacturaDAO;
import com.zits.raulgil_pr51.model.GastoMensual;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private final DatabaseManager dbManager;
    private final FacturaDAO facturaDAO;

    public ReportService(DatabaseManager dbManager, FacturaDAO facturaDAO) {
        this.dbManager = dbManager;
        this.facturaDAO = facturaDAO;
    }

    public ReportService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        this.facturaDAO = new FacturaDAO(dbManager);
    }

    public void generarFactura(int idFactura) throws Exception {
        try (Connection conn = dbManager.connect()) {
            JasperDesign design = JRXmlLoader.load(getClass().getResourceAsStream("/Factura.jrxml"));
            JasperReport report = JasperCompileManager.compileReport(design);

            List<GastoMensual> gastos = facturaDAO.obtenerGastosHistoricos(idFactura);
            JRBeanCollectionDataSource chartsDs = new JRBeanCollectionDataSource(gastos);

            Map<String, Object> params = new HashMap<>();
            params.put("idFactura", idFactura);
            params.put("dsGastos", chartsDs);

            JasperPrint print = JasperFillManager.fillReport(report, params, conn);
            JasperViewer.viewReport(print, false);
        }
    }
}
