package modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReporteMasVendidos {

    private final List<ProductoVendido> lista;
    private final String criterio;

    public ReporteMasVendidos(List<ProductoVendido> lista, String criterio) {
        this.lista = lista;
        this.criterio = criterio;
    }

    public void guardar(File archivo) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(archivo.toPath(), StandardCharsets.UTF_8)) {
            bw.write(construirTexto());
        }
    }

    public String construirTexto() {
        String nl = System.lineSeparator();
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String[] cab = {"#", "CODIGO", "NOMBRE", "UNIDADES", "MONTO (S/)"};
        int[] w = new int[cab.length];
        for (int i = 0; i < cab.length; i++) {
            w[i] = cab[i].length();
        }

        int totalUnidades = 0;
        float totalMonto = 0;
        int puesto = 1;
        String[][] filas = new String[lista.size()][];
        for (ProductoVendido p : lista) {
            String[] f = {
                String.valueOf(puesto),
                p.getCodigo(),
                p.getNombre(),
                String.valueOf(p.getUnidades()),
                String.format("%.2f", p.getMonto())
            };
            filas[puesto - 1] = f;
            for (int i = 0; i < f.length; i++) {
                w[i] = Math.max(w[i], f[i].length());
            }
            totalUnidades += p.getUnidades();
            totalMonto += p.getMonto();
            puesto++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE PRODUCTOS MAS VENDIDOS - COMPUTEL").append(nl);
        sb.append("Fecha de impresion: ").append(fecha).append(nl);
        sb.append("Mostrando: ").append(criterio).append(nl).append(nl);

        String sep = lineaSeparadora(w);
        sb.append(sep).append(nl);
        sb.append(formatoFila(cab, w)).append(nl);
        sb.append(sep).append(nl);
        for (String[] f : filas) {
            sb.append(formatoFila(f, w)).append(nl);
        }
        sb.append(sep).append(nl).append(nl);

        sb.append("Total de productos: ").append(lista.size()).append(nl);
        sb.append("Total de unidades vendidas: ").append(totalUnidades).append(nl);
        sb.append("Monto total: S/ ").append(String.format("%.2f", totalMonto)).append(nl);
        return sb.toString();
    }

    // Columnas 0, 3 y 4 (#, unidades, monto) alineadas a la derecha; el resto a la izquierda
    private String formatoFila(String[] f, int[] w) {
        StringBuilder sb = new StringBuilder("| ");
        for (int i = 0; i < f.length; i++) {
            boolean derecha = (i == 0 || i == 3 || i == 4);
            sb.append(String.format(derecha ? "%" + w[i] + "s" : "%-" + w[i] + "s", f[i]));
            sb.append(i < f.length - 1 ? " | " : " |");
        }
        return sb.toString();
    }

    private String lineaSeparadora(int[] w) {
        StringBuilder sb = new StringBuilder("+");
        for (int a : w) {
            sb.append("-".repeat(a + 2)).append("+");
        }
        return sb.toString();
    }
}