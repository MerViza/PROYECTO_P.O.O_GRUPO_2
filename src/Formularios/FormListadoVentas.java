/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Formularios;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import modelo.DetalleVenta;
import modelo.Venta;
import modelo.VentaDAO;

/**
 *
 * @author jeremy
 */
public class FormListadoVentas extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormListadoVentas.class.getName());
    private VentaDAO ventaDAO = new VentaDAO();
    /**
     * Creates new form FormListadoVentas
     */
    public FormListadoVentas() {
        initComponents();
        cargarVentas(""); // Carga todas las ventas al abrir la ventana
    }

    // Método para cargar la tabla de Ventas (Izquierda)
private void cargarVentas(String codigoFiltro) {
    DefaultTableModel model = (DefaultTableModel) tablehistorial1.getModel(); // Ajusta 'tblVentas' al nombre de tu JTable izquierda
    model.setRowCount(0);

    List<Venta> lista = ventaDAO.listarVentas(codigoFiltro);

    for (Venta v : lista) {
        model.addRow(new Object[]{
            v.getNumeroFactura(),
            String.format("%.2f", v.getSubtotal()),
            String.format("%.2f", v.getIva()),
            String.format("%.2f", v.getDescuento()),
            String.format("%.2f", v.getTotal())
        });
    }

    // Limpia la tabla de detalle al volver a filtrar
    limpiarTablaDetalle();
}

private void generarReporteTXT() {
    // Verificar si hay registros en la tabla
    if (tablehistorial1.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "No hay datos en el historial para exportar.", "Atención", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Abrir un selector de archivos para elegir la ubicación y nombre del reporte
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Guardar Reporte de Ventas");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo de texto (*.txt)", "txt"));
    fileChooser.setSelectedFile(new File("Reporte_Historial_Ventas.txt"));

    int seleccion = fileChooser.showSaveDialog(this);

    if (seleccion == JFileChooser.APPROVE_OPTION) {
        File archivoGuardar = fileChooser.getSelectedFile();
        
        // Asegurar la extensión .txt
        if (!archivoGuardar.getName().toLowerCase().endsWith(".txt")) {
            archivoGuardar = new File(archivoGuardar.getAbsolutePath() + ".txt");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoGuardar))) {
            // Cabecera del reporte
            pw.println("=======================================================================");
            pw.println("                      REPORTE DE HISTORIAL DE VENTAS                   ");
            pw.println("=======================================================================");
            pw.println(String.format("%-20s %-12s %-10s %-12s %-12s", "N° FACTURA", "SUBTOTAL", "IVA", "DESCUENTO", "TOTAL"));
            pw.println("-----------------------------------------------------------------------");

            // Recorrer la tabla de ventas
            int totalFilas = tablehistorial1.getRowCount();
            double sumaTotales = 0.0;

            for (int i = 0; i < totalFilas; i++) {
                String numFactura = tablehistorial1.getValueAt(i, 0).toString();
                String subtotal = tablehistorial1.getValueAt(i, 1).toString();
                String iva = tablehistorial1.getValueAt(i, 2).toString();
                String descuento = tablehistorial1.getValueAt(i, 3).toString();
                String totalStr = tablehistorial1.getValueAt(i, 4).toString();

                try {
                    sumaTotales += Double.parseDouble(totalStr.replace(",", "."));
                } catch (NumberFormatException ignored) {}

                pw.println(String.format("%-20s S/ %-9s S/ %-7s S/ %-9s S/ %-9s", 
                        numFactura, subtotal, iva, descuento, totalStr));
            }

            pw.println("-----------------------------------------------------------------------");
            pw.println(String.format("TOTAL ACUMULADO VENTAS: S/ %.2f", sumaTotales));
            pw.println("=======================================================================");

            JOptionPane.showMessageDialog(this, 
                "Reporte guardado con éxito en:\n" + archivoGuardar.getAbsolutePath(), 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}





// Método para cargar la tabla de Detalle de Venta (Derecha)
private void cargarDetalleVenta(String numeroFactura) {
    DefaultTableModel model = (DefaultTableModel) tableDetalleVenta.getModel(); 
    model.setRowCount(0);

    List<DetalleVenta> listaDetalle = ventaDAO.obtenerDetallesPorFactura(numeroFactura);

    for (DetalleVenta det : listaDetalle) {
        model.addRow(new Object[]{
            det.getNumeroFactura(),
            det.getCodigoProducto(),
            det.getCantidad(),
            String.format("%.2f", det.getPrecioUnitario()),
            String.format("%.2f", det.getSubtotal())
        });
    }
}
    private void limpiarTablaDetalle() {
    DefaultTableModel model = (DefaultTableModel) tableDetalleVenta.getModel();
    model.setRowCount(0);
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableDetalleVenta = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnFiltrar = new javax.swing.JButton();
        btnReporte = new javax.swing.JButton();
        txtCodigoComprobante = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablehistorial1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText("Codigo de Comprobante");

        tableDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Numero de Factura", "Codigo de Producto", "Cantidad", "Precio Unitario", "SubTotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableDetalleVenta);
        if (tableDetalleVenta.getColumnModel().getColumnCount() > 0) {
            tableDetalleVenta.getColumnModel().getColumn(0).setResizable(false);
            tableDetalleVenta.getColumnModel().getColumn(1).setResizable(false);
            tableDetalleVenta.getColumnModel().getColumn(2).setResizable(false);
            tableDetalleVenta.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        jLabel1.setText("Historial de Ventas");

        btnFiltrar.setText("Filtrar");
        btnFiltrar.addActionListener(this::btnFiltrarActionPerformed);

        btnReporte.setText("Imprimir Reporte");
        btnReporte.addActionListener(this::btnReporteActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        tablehistorial1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Numero de Factura", "SubTotal", "IVA", "Descuento", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablehistorial1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablehistorial1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablehistorial1);
        if (tablehistorial1.getColumnModel().getColumnCount() > 0) {
            tablehistorial1.getColumnModel().getColumn(0).setResizable(false);
            tablehistorial1.getColumnModel().getColumn(1).setResizable(false);
            tablehistorial1.getColumnModel().getColumn(2).setResizable(false);
            tablehistorial1.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtCodigoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(244, 244, 244)
                        .addComponent(jLabel1)))
                .addContainerGap(430, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(btnReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCodigoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar)
                    .addComponent(btnLimpiar))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnReporte))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        // TODO add your handling code here:
        String codigoIngresado = txtCodigoComprobante.getText().trim();
        cargarVentas(codigoIngresado);
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        txtCodigoComprobante.setText("");
        cargarVentas("");
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void tablehistorial1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablehistorial1MouseClicked
        // TODO add your handling code here:   
        int filaSel = tablehistorial1.getSelectedRow();
    if (filaSel != -1) {
        // Lee el número de factura de la primera columna (columna 0)
        String numeroFactura = tablehistorial1.getValueAt(filaSel, 0).toString();
        
        // Carga el detalle correspondiente en la tabla derecha
        cargarDetalleVenta(numeroFactura);
    }
    }//GEN-LAST:event_tablehistorial1MouseClicked

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        // TODO add your handling code here:
        generarReporteTXT();
    }//GEN-LAST:event_btnReporteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormListadoVentas().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tableDetalleVenta;
    private javax.swing.JTable tablehistorial1;
    private javax.swing.JTextField txtCodigoComprobante;
    // End of variables declaration//GEN-END:variables
}
