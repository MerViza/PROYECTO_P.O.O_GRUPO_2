/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import conexion.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Antonio
 */
public class VentaDAO {
    // Generar correlativo automático para la factura (Ej: F001-0000001)
    public String generarNumeroFactura() {
        String sql = "SELECT numero_factura FROM ventas ORDER BY numero_factura DESC LIMIT 1";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String ultimo = rs.getString("numero_factura"); // Ejemplo: F001-0000005
                int numero = Integer.parseInt(ultimo.split("-")[1]) + 1;
                return String.format("F001-%07d", numero);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "F001-0000001"; // Primera factura por defecto
    }

   // Ahora el método recibe la entidad completa 'Venta' (que ya incluye sus detalles)
public boolean registrarVenta(Venta venta) {
    String sqlVenta = "INSERT INTO ventas (numero_factura, subtotal, iva, descuento, total) VALUES (?, ?, ?, ?, ?)";
    String sqlDetalle = "INSERT INTO detalle_ventas (numero_factura, codigo_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
    String sqlStock = "UPDATE productos SET cantidad = cantidad - ? WHERE codigo = ?";

    Connection con = null;
    try {
        con = ConexionBD.conectar();
        if (con == null) return false;

        con.setAutoCommit(false); // Iniciar transacción

        // 1. Guardar la entidad Venta
        try (PreparedStatement psVenta = con.prepareStatement(sqlVenta)) {
            psVenta.setString(1, venta.getNumeroFactura());
            psVenta.setFloat(2, venta.getSubtotal());
            psVenta.setFloat(3, venta.getIva());
            psVenta.setFloat(4, venta.getDescuento());
            psVenta.setFloat(5, venta.getTotal());
            psVenta.executeUpdate();
        }

        // 2. Guardar la lista de entidad DetalleVenta
        try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
             PreparedStatement psStock = con.prepareStatement(sqlStock)) {

            for (DetalleVenta det : venta.getDetalles()) {
                psDetalle.setString(1, venta.getNumeroFactura());
                psDetalle.setString(2, det.getCodigoProducto());
                psDetalle.setInt(3, det.getCantidad());
                psDetalle.setFloat(4, det.getPrecioUnitario());
                psDetalle.setFloat(5, det.getSubtotal());
                psDetalle.addBatch();

                psStock.setInt(1, det.getCantidad());
                psStock.setString(2, det.getCodigoProducto());
                psStock.addBatch();
            }

            psDetalle.executeBatch();
            psStock.executeBatch();
        }

        con.commit();
        return true;

    } catch (SQLException e) {
        if (con != null) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return false;
    } finally {
        if (con != null) {
            try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}

    // RF-28: Consultar Venta por Número de Factura
    public ResultSet consultarVentaPorFactura(String numFactura) {
        String sql = "SELECT v.numero_factura, v.fecha, v.subtotal, v.iva, v.descuento, v.total, " +
                     "d.codigo_producto, p.nombre, d.cantidad, d.precio_unitario, d.subtotal AS subtotal_item " +
                     "FROM ventas v " +
                     "INNER JOIN detalle_ventas d ON v.numero_factura = d.numero_factura " +
                     "INNER JOIN productos p ON d.codigo_producto = p.codigo " +
                     "WHERE v.numero_factura = ?";
        try {
            Connection con = ConexionBD.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, numFactura);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Obtener todas las ventas o filtrar por número de comprobante
public List<Venta> listarVentas(String filtroComprobante) {
    List<Venta> lista = new ArrayList<>();
    String sql = "SELECT numero_factura, subtotal, iva, descuento, total FROM ventas";
    
    if (filtroComprobante != null && !filtroComprobante.trim().isEmpty()) {
        sql += " WHERE numero_factura LIKE ?";
    }

    try (Connection con = ConexionBD.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        if (filtroComprobante != null && !filtroComprobante.trim().isEmpty()) {
            ps.setString(1, "%" + filtroComprobante.trim() + "%");
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Venta v = new Venta();
            v.setNumeroFactura(rs.getString("numero_factura"));
            v.setSubtotal(rs.getFloat("subtotal"));
            v.setIva(rs.getFloat("iva"));
            v.setDescuento(rs.getFloat("descuento"));
            v.setTotal(rs.getFloat("total"));
            lista.add(v);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}

// Obtener el detalle específico de una venta seleccionada
public List<DetalleVenta> obtenerDetallesPorFactura(String numeroFactura) {
    List<DetalleVenta> listaDetalle = new ArrayList<>();
    
    // Asegúrate de que los nombres de la tabla y campos sean IDÉNTICOS a tu BD
    String sql = "SELECT numero_factura, codigo_producto, cantidad, precio_unitario, subtotal " +
                 "FROM detalle_ventas WHERE TRIM(numero_factura) = TRIM(?)";

    try (Connection con = ConexionBD.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, numeroFactura);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            DetalleVenta det = new DetalleVenta();
            det.setNumeroFactura(rs.getString("numero_factura"));
            det.setCodigoProducto(rs.getString("codigo_producto"));
            det.setCantidad(rs.getInt("cantidad"));
            det.setPrecioUnitario(rs.getFloat("precio_unitario"));
            det.setSubtotal(rs.getFloat("subtotal"));
            listaDetalle.add(det);
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener detalle: " + e.getMessage());
        e.printStackTrace();
    }
    return listaDetalle;
}
public List<ProductoVendido> obtenerMasVendidos(int limite) {
    List<ProductoVendido> lista = new ArrayList<>();
    String sql = "SELECT d.codigo_producto, p.nombre, "
               + "SUM(d.cantidad) AS unidades, SUM(d.subtotal) AS monto "
               + "FROM detalle_ventas d "
               + "INNER JOIN productos p ON p.codigo = d.codigo_producto "
               + "GROUP BY d.codigo_producto, p.nombre "
               + "ORDER BY unidades DESC, monto DESC, p.nombre ASC";

    if (limite > 0) {
        sql += " LIMIT ?";
    }

    try (Connection con = ConexionBD.conectar()) {
        if (con == null) return lista;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (limite > 0) {
                ps.setInt(1, limite);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ProductoVendido(
                            rs.getString("codigo_producto"),
                            rs.getString("nombre"),
                            rs.getInt("unidades"),
                            rs.getFloat("monto")
                    ));
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al consultar más vendidos: " + e.getMessage());
    }
    return lista;
}




}
