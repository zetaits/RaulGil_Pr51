package com.zits.raulgil_pr51.model;

public class Factura {
    private int id;
    private int idCliente;
    private String fechaEmision;
    private String periodoInicio;
    private String periodoFin;

    private String clienteNombre;
    private double totalImporte;

    public Factura(int id, int idCliente, String fechaEmision, String periodoInicio, String periodoFin) {
        this.id = id;
        this.idCliente = idCliente;
        this.fechaEmision = fechaEmision;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
    }

    public Factura(int id, String clienteNombre, String fechaEmision, double totalImporte) {
        this.id = id;
        this.clienteNombre = clienteNombre;
        this.fechaEmision = fechaEmision;
        this.totalImporte = totalImporte;
    }

    public Factura(int idCliente, String fechaEmision, String periodoInicio, String periodoFin) {
        this.idCliente = idCliente;
        this.fechaEmision = fechaEmision;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public String getPeriodoInicio() {
        return periodoInicio;
    }

    public String getPeriodoFin() {
        return periodoFin;
    }

    public String getCliente() {
        return clienteNombre;
    }

    public String getFecha() {
        return fechaEmision;
    }

    public double getTotal() {
        return totalImporte;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public void setTotalImporte(double totalImporte) {
        this.totalImporte = totalImporte;
    }

    @Override
    public String toString() {
        return "Factura " + id + " - " + (clienteNombre != null ? clienteNombre : "Cliente " + idCliente) + " ("
                + fechaEmision + ")";
    }
}
