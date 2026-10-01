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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeremy
 */
public class ProductoDAO {
    
    
    
    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (codigo, nombre, cantidad, precio) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getCodigo());
                ps.setString(2, p.getNombre());
                ps.setInt(3, p.getCantidad());
                ps.setDouble(4, p.getPrecio());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT codigo, nombre, cantidad, precio FROM productos ORDER BY nombre";

        try (Connection con = ConexionBD.conectar()) {
            if (con == null) {
                return lista;
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(new Producto(
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("cantidad"),
                            rs.getFloat("precio")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }
    
    public List<Producto> buscar(String texto) {
    List<Producto> lista = new ArrayList<>();
    String sql = "SELECT codigo, nombre, cantidad, precio FROM productos "
               + "WHERE codigo LIKE ? OR nombre LIKE ? ORDER BY nombre";

    try (Connection con = ConexionBD.conectar()) {
        if (con == null) {
            return lista;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            String patron = "%" + texto + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Producto(
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("cantidad"),
                            rs.getFloat("precio")
                    ));
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al buscar productos: " + e.getMessage());
    }
    return lista;
}
    
    
}
