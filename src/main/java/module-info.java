module com.zits.raulgil_pr51 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires org.xerial.sqlitejdbc;
    requires flexmark;
    requires flexmark.util.data;
    requires javafx.web;

    opens com.zits.raulgil_pr51 to javafx.fxml;

    opens com.zits.raulgil_pr51.model;

    exports com.zits.raulgil_pr51;
    exports com.zits.raulgil_pr51.dao;
    exports com.zits.raulgil_pr51.service;
    exports com.zits.raulgil_pr51.model;
}
