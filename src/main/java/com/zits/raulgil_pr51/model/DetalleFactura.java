package com.zits.raulgil_pr51.model;

public class DetalleFactura {
    private int id;
    private int idFactura;
    private String tramo;
    private double consumoKwh;
    private double precioKwh;

    public DetalleFactura(int id, int idFactura, String tramo, double consumoKwh, double precioKwh) {
        this.id = id;
        this.idFactura = idFactura;
        this.tramo = tramo;
        this.consumoKwh = consumoKwh;
        this.precioKwh = precioKwh;
    }

    public DetalleFactura(int idFactura, String tramo, double consumoKwh, double precioKwh) {
        this.idFactura = idFactura;
        this.tramo = tramo;
        this.consumoKwh = consumoKwh;
        this.precioKwh = precioKwh;
    }

    public int getId() {
        return id;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public String getTramo() {
        return tramo;
    }

    public double getConsumoKwh() {
        return consumoKwh;
    }

    public double getPrecioKwh() {
        return precioKwh;
    }
}
