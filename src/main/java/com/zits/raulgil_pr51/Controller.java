package com.zits.raulgil_pr51;

import com.zits.raulgil_pr51.dao.*;
import com.zits.raulgil_pr51.model.*;
import com.zits.raulgil_pr51.service.ReportService;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;

public class Controller {

    // --- Componentes FXML ---
    @FXML
    private TextField txtNombre, txtApellidos, txtNif, txtDireccion;
    @FXML
    private TextField txtKwhPunta, txtKwhLlano, txtKwhValle;
    @FXML
    private DatePicker dpInicio, dpFin, dpFechaEmision;
    @FXML
    private Label lblMensaje;

    @FXML
    private ComboBox<Cliente> comboClientesExistentes;

    // Tabla
    @FXML
    private TableView<Factura> tablaFacturas;
    @FXML
    private TableColumn<Factura, Integer> colId;
    @FXML
    private TableColumn<Factura, String> colCliente;
    @FXML
    private TableColumn<Factura, String> colFecha;
    @FXML
    private TableColumn<Factura, Double> colTotal;

    // Informes (DUAL DROPDOWN)
    @FXML
    private ComboBox<Cliente> comboClienteImpresion;
    @FXML
    private ComboBox<Factura> comboFacturaImpresion;
    @FXML
    private VBox panelDetalles;
    @FXML
    private Label lblResumenCliente, lblResumenTotal;
    @FXML
    private Button btnGenerarPDF;

    // Análisis
    @FXML
    private ComboBox<String> comboTramo;
    @FXML
    private DatePicker dpAnalisisInicio, dpAnalisisFin;
    @FXML
    private BarChart<String, Number> graficoJavaFX;
    @FXML
    private PieChart graficoSectores;

    // DEPENDENCIAS
    private DatabaseManager dbManager;
    private ClienteDAO clienteDAO;
    private FacturaDAO facturaDAO;
    private DetalleFacturaDAO detalleFacturaDAO;
    private ReportService reportService;

    @FXML
    public void initialize() {
        dbManager = new DatabaseManager();
        clienteDAO = new ClienteDAO(dbManager);
        facturaDAO = new FacturaDAO(dbManager);
        detalleFacturaDAO = new DetalleFacturaDAO(dbManager);
        reportService = new ReportService(dbManager, facturaDAO);

        comboTramo.getItems().addAll("punta", "llano", "valle", "TODOS");
        comboTramo.getSelectionModel().selectFirst();

        dpInicio.setValue(LocalDate.now().minusDays(30));
        dpFin.setValue(LocalDate.now());
        dpFechaEmision.setValue(LocalDate.now());

        dpAnalisisInicio.setValue(LocalDate.now().minusMonths(3));
        dpAnalisisFin.setValue(LocalDate.now());

        // --- LOGICA DE IMPRESION (DUAL) ---
        comboClienteImpresion.setDisable(true);
        comboFacturaImpresion.setDisable(true);

        comboClienteImpresion.setOnAction(e -> onClienteImpresionSeleccionado());
        comboFacturaImpresion.setOnAction(e -> mostrarResumenFactura());

        comboClientesExistentes.setOnAction(e -> onClienteSeleccionado());

        // Tooltips
        comboClientesExistentes.setTooltip(new Tooltip("Seleccione un cliente existente para autocompletar los datos"));
        dpFechaEmision.setTooltip(new Tooltip("Fecha de emisión de la factura"));
        txtKwhPunta.setTooltip(new Tooltip("Consumo en periodo Punta (0.30€/kWh)"));
        txtKwhLlano.setTooltip(new Tooltip("Consumo en periodo Llano (0.20€/kWh)"));
        txtKwhValle.setTooltip(new Tooltip("Consumo en periodo Valle (0.10€/kWh)"));
        btnGenerarPDF.setTooltip(new Tooltip("Generar y visualizar el PDF de la factura seleccionada"));

        dbManager.crearTablasSiNoExisten();
        cargarTabla();
        actualizarCombosClientes();
    }

    private void actualizarCombosClientes() {
        try {
            ObservableList<Cliente> clientes = clienteDAO.obtenerTodos();
            comboClientesExistentes.setItems(clientes);
            comboClienteImpresion.setItems(clientes);
            comboClienteImpresion.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClienteImpresionSeleccionado() {
        Cliente c = comboClienteImpresion.getValue();
        comboFacturaImpresion.setDisable(true);
        comboFacturaImpresion.getItems().clear();
        panelDetalles.setVisible(false);
        btnGenerarPDF.setDisable(true);

        if (c != null) {
            ObservableList<Factura> facturas = facturaDAO.obtenerPorCliente(c.getId());
            comboFacturaImpresion.setItems(facturas);
            comboFacturaImpresion.setDisable(false);
        }
    }

    @FXML
    public void onClienteSeleccionado() {
        Cliente seleccionado = comboClientesExistentes.getValue();
        if (seleccionado != null) {
            txtNombre.setText(((Cliente) seleccionado).getNombre());
            txtApellidos.setText(((Cliente) seleccionado).getApellidos());
            txtNif.setText(((Cliente) seleccionado).getNif());
            txtDireccion.setText(((Cliente) seleccionado).getDireccion());
            setCamposClienteEditables(false);
        }
    }

    @FXML
    public void limpiarSeleccionCliente() {
        comboClientesExistentes.getSelectionModel().clearSelection();
        limpiarFormulario();
        setCamposClienteEditables(true);
    }

    private void setCamposClienteEditables(boolean editable) {
        txtNombre.setEditable(editable);
        txtApellidos.setEditable(editable);
        txtNif.setEditable(editable);
        txtDireccion.setEditable(editable);
    }

    @FXML
    public void resetearBDCompleta() {
        try {
            dbManager.resetearBDCompleta();
            mostrarAlerta("Éxito", "BD reseteada.", Alert.AlertType.INFORMATION);
            cargarTabla();
            actualizarCombosClientes();
            onClienteImpresionSeleccionado();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error reset: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void guardarDatos() {
        if (txtNombre.getText().isEmpty() || txtApellidos.getText().isEmpty()) {
            mostrarAlerta("Error", "Faltan datos.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int idCliente;

            Cliente clienteSeleccionado = comboClientesExistentes.getValue();
            if (clienteSeleccionado != null) {
                idCliente = clienteSeleccionado.getId();
            } else {
                Cliente nuevoCliente = new Cliente(txtNombre.getText(), txtApellidos.getText(), txtNif.getText(),
                        txtDireccion.getText());
                idCliente = clienteDAO.insertar(nuevoCliente);
                actualizarCombosClientes();
            }

            Factura factura = new Factura(idCliente, dpFechaEmision.getValue().toString(),
                    dpInicio.getValue().toString(),
                    dpFin.getValue().toString());
            int idFactura = facturaDAO.insertar(factura);

            detalleFacturaDAO
                    .insertar(new DetalleFactura(idFactura, "punta", Double.parseDouble(txtKwhPunta.getText()), 0.30));
            detalleFacturaDAO
                    .insertar(new DetalleFactura(idFactura, "llano", Double.parseDouble(txtKwhLlano.getText()), 0.20));
            detalleFacturaDAO
                    .insertar(new DetalleFactura(idFactura, "valle", Double.parseDouble(txtKwhValle.getText()), 0.10));

            lblMensaje.setText("Factura " + idFactura + " guardada.");
            cargarTabla();
            if (comboClienteImpresion.getValue() != null && comboClienteImpresion.getValue().getId() == idCliente) {
                onClienteImpresionSeleccionado();
            }

            limpiarSeleccionCliente();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error guardar: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtApellidos.clear();
        txtNif.clear();
        txtDireccion.clear();
    }

    @FXML
    protected void cargarTabla() {
        tablaFacturas.setItems(facturaDAO.obtenerTodasConDetalles());
    }

    @FXML
    protected void eliminarFactura() {
        Factura seleccion = tablaFacturas.getSelectionModel().getSelectedItem();
        if (seleccion == null)
            return;

        try {
            detalleFacturaDAO.eliminarPorFactura(seleccion.getId());
            facturaDAO.eliminar(seleccion.getId());
            cargarTabla();
            onClienteImpresionSeleccionado();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error eliminar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void cargarGraficoAnalisis() {
        graficoJavaFX.getData().clear();
        String tramo = comboTramo.getValue();
        ObservableList<XYChart.Data<String, Number>> datos = facturaDAO
                .obtenerDatosAnalisis(dpAnalisisInicio.getValue(), dpAnalisisFin.getValue(), tramo);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Media (kWh)");
        series.setData(datos);
        graficoJavaFX.getData().add(series);

        graficoSectores.getData().clear();
        for (XYChart.Data<String, Number> d : datos) {
            graficoSectores.getData().add(new PieChart.Data(d.getXValue(), d.getYValue().doubleValue()));
        }
    }

    private void mostrarResumenFactura() {
        Factura seleccion = comboFacturaImpresion.getValue();
        if (seleccion != null) {
            panelDetalles.setVisible(true);
            btnGenerarPDF.setDisable(false);
            lblResumenCliente.setText("Cliente: " + seleccion.getCliente());
            lblResumenTotal.setText("Total (IVA inc.): " + seleccion.getTotal() + " €");
        } else {
            panelDetalles.setVisible(false);
            btnGenerarPDF.setDisable(true);
        }
    }

    @FXML
    protected void generarFactura() {
        Factura seleccion = comboFacturaImpresion.getValue();
        if (seleccion == null)
            return;
        try {
            reportService.generarFactura(seleccion.getId());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error Reporte", "Error reporte: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String t, String c, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(t);
        a.setContentText(c);
        a.showAndWait();
    }

    @FXML
    public void alCambiarPestana(javafx.event.Event e) {
        if (e.getSource() instanceof Tab) {
            Tab tab = (Tab) e.getSource();
            if (tab.isSelected()) {
                actualizarCombosClientes();
            }
        }
    }

    @FXML
    public void mostrarAyuda() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/help.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Ayuda y Documentación");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ayuda: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}