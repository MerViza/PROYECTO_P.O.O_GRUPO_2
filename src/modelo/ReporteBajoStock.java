package modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReporteBajoStock {

    private final List<Producto> productos;
    private final int umbral;
    private final String categoria;

    public ReporteBajoStock(List<Producto> productos, int umbral, String categoria) {
        this.productos = productos;
        this.umbral = umbral;
        this.categoria = categoria;
    }

    public void guardar(File archivo) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(archivo.toPath(), StandardCharsets.UTF_8)) {
            bw.write(construirTexto());
        }
    }

    private String estado(Producto p) {
        return (p.getCantidad() == 0) ? "Agotado" : "Bajo";
    }

    public String construirTexto() {
        String salto = System.lineSeparator();
        String[] titulos = {"CODIGO", "NOMBRE", "CATEGORIA", "STOCK", "ESTADO"};

        // Ancho de cada columna según el dato más largo
        int[] anchos = new int[titulos.length];
        for (int i = 0; i < titulos.length; i++) {
            anchos[i] = titulos[i].length();
        }
        for (Producto p : productos) {
            anchos[0] = Math.max(anchos[0], p.getCodigo().length());
            anchos[1] = Math.max(anchos[1], p.getNombre().length());
            anchos[2] = Math.max(anchos[2], p.getCategoria().length());
            anchos[3] = Math.max(anchos[3], String.valueOf(p.getCantidad()).length());
            anchos[4] = Math.max(anchos[4], estado(p).length());
        }

        String formato = "| %-" + anchos[0] + "s | %-" + anchos[1] + "s | %-" + anchos[2]
                       + "s | %" + anchos[3] + "s | %-" + anchos[4] + "s |" + salto;
        String separador = lineaSeparadora(anchos);

        String fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE PRODUCTOS CON BAJO STOCK - COMPUTEL").append(salto);
        sb.append("Fecha de impresion: ").append(fecha).append(salto);
        sb.append("Criterio: menos de ").append(umbral).append(" unidades").append(salto);
        sb.append("Categoria: ").append(categoria).append(salto);
        sb.append(salto);

        sb.append(separador).append(salto);
        sb.append(String.format(formato, (Object[]) titulos));
        sb.append(separador).append(salto);

        for (Producto p : productos) {
            sb.append(String.format(formato,
                    p.getCodigo(),
                    p.getNombre(),
                    p.getCategoria(),
                    String.valueOf(p.getCantidad()),
                    estado(p)));
        }

        sb.append(separador).append(salto);
        sb.append(salto);
        sb.append("Total de productos con bajo stock: ").append(productos.size()).append(salto);

        return sb.toString();
    }

    private String lineaSeparadora(int[] anchos) {
        StringBuilder sb = new StringBuilder("+");
        for (int ancho : anchos) {
            for (int i = 0; i < ancho + 2; i++) {
                sb.append('-');
            }
            sb.append('+');
        }
        return sb.toString();
    }
}