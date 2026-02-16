package com.zits.raulgil_pr51;

import com.zits.raulgil_pr51.service.HelpService;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

public class HelpController {

    @FXML
    private WebView webView;

    private HelpService helpService;

    @FXML
    public void initialize() {
        helpService = new HelpService();
        cargarAyudaBasica();
    }

    @FXML
    public void cargarAyudaBasica() {
        String htmlContent = helpService.getBasicHelpHtml();
        webView.getEngine().loadContent(htmlContent);
    }

    @FXML
    public void cargarAyuda() {
        String htmlContent = helpService.loadHelpContent();
        webView.getEngine().loadContent(htmlContent);
    }

    @FXML
    public void cargarJavadoc() {
        String javadocUrl = helpService.loadJavadocOrError();
        if (javadocUrl != null) {
            // Convert to URI if it's a local path string, though service returns URI string
            // or null
            // Fix: Service logic returns URI string if exists.
            // But let's make sure it is a valid URI.
            try {
                File file = new File("docs/javadoc/index.html");
                if (file.exists()) {
                    webView.getEngine().load(file.toURI().toString());
                } else {
                    webView.getEngine().loadContent(helpService.getJavadocErrorHtml());
                }
            } catch (Exception e) {
                webView.getEngine().loadContent(helpService.getJavadocErrorHtml());
            }
        } else {
            webView.getEngine().loadContent(helpService.getJavadocErrorHtml());
        }
    }

    @FXML
    public void cerrar() {
        Stage stage = (Stage) webView.getScene().getWindow();
        stage.close();
    }
}
