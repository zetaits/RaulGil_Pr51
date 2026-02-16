package com.zits.raulgil_pr51.model;

public class GastoMensual {
    private String mes;
    private double total;

    public GastoMensual(String mes, double total) {
        this.mes = mes;
        this.total = total;
    }

    public String getMes() {
        return mes;
    }

    public double getTotal() {
        return total;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
