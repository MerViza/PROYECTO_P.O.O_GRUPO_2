/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jeremy
 */
public class Producto {
    
    private String codigo;
    private String nombre;
    private String categoria; // "Pieza" o "Equipo"
    private int cantidad;
    private float precio;
    private String socketPuerto;  // Usado solo si categoria = "Pieza"
    private Integer aniosGarantia; // Usado solo si categoria = "Equipo"

    public Producto() {
    }

    // Constructor completo
    public Producto(String codigo, String nombre, String categoria, int cantidad, float precio, String socketPuerto, Integer aniosGarantia) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precio = precio;
        this.socketPuerto = socketPuerto;
        this.aniosGarantia = aniosGarantia;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public float getPrecio() { return precio; }
    public void setPrecio(float precio) { this.precio = precio; }

    public String getSocketPuerto() { return socketPuerto; }
    public void setSocketPuerto(String socketPuerto) { this.socketPuerto = socketPuerto; }

    public Integer getAniosGarantia() { return aniosGarantia; }
    public void setAniosGarantia(Integer aniosGarantia) { this.aniosGarantia = aniosGarantia; }
}
