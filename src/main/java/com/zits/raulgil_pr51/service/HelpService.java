package com.zits.raulgil_pr51.service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelpService {

    public String loadHelpContent() {
        try (InputStream is = getClass().getResourceAsStream("/docs/manual_usuario.md")) {
            if (is == null) {
                return "<h1>Error</h1><p>No se encontró el archivo de ayuda.</p>";
            }
            String markdown = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return convertMarkdownToHtml(markdown);
        } catch (IOException e) {
            e.printStackTrace();
            return "<h1>Error</h1><p>Error al cargar la ayuda: " + e.getMessage() + "</p>";
        }
    }

    public String getBasicHelpHtml() {
        String htmlBody = "" +
                "<h1>Ayuda R&aacute;pida de ElectroFactura</h1>" +
                "<p>Bienvenido al sistema de gesti&oacute;n. Aqu&iacute; tiene los pasos b&aacute;sicos para empezar:</p>"
                +
                "<h2>1. Crear una Factura</h2>" +
                "<ul>" +
                "<li>Seleccione un cliente existente o rellene los datos para uno nuevo.</li>" +
                "<li>Ingrese el consumo en kWh para cada tramo (Punta, Llano, Valle).</li>" +
                "<li>Pulse <b>Guardar Factura</b>.</li>" +
                "</ul>" +
                "<h2>2. Imprimir Factura</h2>" +
                "<ul>" +
                "<li>Vaya a la pesta&ntilde;a <b>Imprimir PDF</b>.</li>" +
                "<li>Seleccione el cliente y la factura deseada.</li>" +
                "<li>Haga clic en <b>Generar y Visualizar PDF</b>.</li>" +
                "</ul>" +
                "<h2>&iquest;Necesita m&aacute;s informaci&oacute;n?</h2>" +
                "<p>Si necesita una gu&iacute;a detallada, haga clic en el bot&oacute;n <b>Manual de Usuario</b> en la barra superior.</p>";

        String css = "<style>" +
                "body { font-family: 'Segoe UI', sans-serif; padding: 20px; line-height: 1.6; color: #333; }" +
                "h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }" +
                "h2 { color: #2980b9; margin-top: 20px; }" +
                "ul { margin-left: 20px; }" +
                "li { margin-bottom: 5px; }" +
                "b { color: #2c3e50; }" +
                "</style>";

        return "<html><head>" + css + "</head><body>" + htmlBody + "</body></html>";
    }

    private String convertMarkdownToHtml(String markdown) {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        String htmlBody = renderer.render(parser.parse(markdown));

        // Estilos CSS simples para mejorar la visualización
        String css = "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; padding: 20px; line-height: 1.6; color: #333; }"
                +
                "h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }" +
                "h2 { color: #2980b9; margin-top: 25px; }" +
                "code { background-color: #f1f1f1; padding: 2px 5px; border-radius: 4px; font-family: Consolas, monospace; }"
                +
                "blockquote { border-left: 4px solid #3498db; margin: 0; padding-left: 15px; color: #555; background-color: #f9f9f9; padding: 10px; }"
                +
                "table { border-collapse: collapse; width: 100%; margin: 20px 0; }" +
                "th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }" +
                "th { background-color: #f2f2f2; }" +
                "tr:nth-child(even) { background-color: #f9f9f9; }" +
                "ul, ol { margin-left: 20px; }" +
                "li { margin-bottom: 5px; }" +
                "</style>";

        return "<html><head>" + css + "</head><body>" + htmlBody + "</body></html>";
    }

    /*
     * private String convertMarkdownToHtml(String markdown) {
     * MutableDataSet options = new MutableDataSet();
     * Parser parser = Parser.builder(options).build();
     * HtmlRenderer renderer = HtmlRenderer.builder(options).build();
     * 
     * return renderer.render(parser.parse(markdown));
     * }
     */

    public String loadJavadocOrError() {
        Path javadocPath = Paths.get("docs/javadoc/index.html");
        if (Files.exists(javadocPath)) {
            return javadocPath.toUri().toString();
        } else {
            return null; // Indieca que no existe
        }
    }

    public String getJavadocErrorHtml() {
        return "<html><body><h1>Javadoc no disponible</h1>" +
                "<p>La documentación Javadoc no se ha generado aún.</p>" +
                "<p>Ejecute <code>gradlew javadoc</code> para generarla.</p></body></html>";
    }
}
