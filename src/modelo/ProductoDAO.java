/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeremy
 */
public class ProductoDAO {

    // 1. Insertar producto
    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (codigo, nombre, categoria, cantidad, precio, socket_puerto, anios_garantia) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setInt(4, p.getCantidad());
            ps.setFloat(5, p.getPrecio());
            
            // Asignación de campos específicos según la categoría seleccionada
            if ("Pieza".equalsIgnoreCase(p.getCategoria())) {
                ps.setString(6, p.getSocketPuerto());
                ps.setNull(7, java.sql.Types.INTEGER);
            } else if ("Equipo".equalsIgnoreCase(p.getCategoria())) {
                ps.setNull(6, java.sql.Types.VARCHAR);
                if (p.getAniosGarantia() != null) {
                    ps.setInt(7, p.getAniosGarantia());
                } else {
                    ps.setNull(7, java.sql.Types.INTEGER);
                }
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar producto en BD: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 2. Actualizar producto
    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, tipo = ?, cantidad = ?, precio = ?, "
                   + "socket_puerto = ?, anios_garantia = ? WHERE codigo = ?";

        try (Connection con = ConexionBD.conectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getCategoria());
                ps.setInt(3, p.getCantidad());
                ps.setDouble(4, p.getPrecio());

                if (p.getSocketPuerto() != null && !p.getSocketPuerto().isEmpty()) {
                    ps.setString(5, p.getSocketPuerto());
                } else {
                    ps.setNull(5, Types.VARCHAR);
                }

                if (p.getAniosGarantia() != null) {
                    ps.setInt(6, p.getAniosGarantia());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }

                ps.setString(7, p.getCodigo());

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // 3. Eliminar producto
    public boolean eliminar(String codigo) {
        String sql = "DELETE FROM productos WHERE codigo = ?";
        try (Connection con = ConexionBD.conectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, codigo);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // 4. Listar todos los productos
    public List<Producto> listar() {
        return buscarOFiltrar("", "Todos");
    }

    // 5. Filtrar por categoría (RF-09) y/o búsqueda por texto
    public List<Producto> buscarOFiltrar(String texto, String categoria) {
        List<Producto> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT codigo, nombre, tipo, cantidad, precio, socket_puerto, anios_garantia FROM productos WHERE 1=1");

        if (texto != null && !texto.trim().isEmpty()) {
            sql.append(" AND (codigo LIKE ? OR nombre LIKE ?)");
        }
        if (categoria != null && !categoria.equalsIgnoreCase("Todos")) {
            sql.append(" AND tipo = ?");
        }
        sql.append(" ORDER BY nombre");

        try (Connection con = ConexionBD.conectar()) {
            if (con == null) return lista;
            try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
                int paramIndex = 1;

                if (texto != null && !texto.trim().isEmpty()) {
                    String patron = "%" + texto + "%";
                    ps.setString(paramIndex++, patron);
                    ps.setString(paramIndex++, patron);
                }
                if (categoria != null && !categoria.equalsIgnoreCase("Todos")) {
                    ps.setString(paramIndex++, categoria);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Producto p = new Producto(
                                rs.getString("codigo"),
                                rs.getString("nombre"),
                                rs.getString("tipo"),
                                rs.getInt("cantidad"),
                                rs.getFloat("precio"),
                                rs.getString("socket_puerto"),
                                (Integer) rs.getObject("anios_garantia")
                        );
                        lista.add(p);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar productos: " + e.getMessage());
        }
        return lista;
    }

    // 6. Verificar si un código ya existe
    public boolean existeCodigo(String codigo) {
        String sql = "SELECT COUNT(*) FROM productos WHERE codigo = ?";
        try (Connection con = ConexionBD.conectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, codigo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar código: " + e.getMessage());
        }
        return false;
    }
    
    public int obtenerUltimoNumeroCodigo() {
    int maxNumero = 0;
    // Extrae la parte numérica del código (ej: de 'P-002' toma '002' y lo convierte a entero)
    String sql = "SELECT MAX(CAST(SUBSTRING(codigo, 3) AS UNSIGNED)) FROM productos WHERE codigo LIKE 'P-%'";

    try (Connection con = ConexionBD.conectar();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            maxNumero = rs.getInt(1); // Retorna 0 si no hay registros o si es NULL
        }

    } catch (SQLException e) {
        System.out.println("Error al obtener el último código: " + e.getMessage());
        e.printStackTrace();
    }
    return maxNumero;
    }
}