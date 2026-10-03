/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author Antonio
 */
public class Venta {
    private String numeroFactura;
    private Date fecha;
    private float subtotal;
    private float iva;
    private float descuento;
    private float total;
    private List<DetalleVenta> detalles; // Relación 1 a Muchos con los detalles
    
    // Constructor vacío
    public Venta() {
        this.detalles = new ArrayList<>();
    }

    // Constructor completo
    public Venta(String numeroFactura, Date fecha, float subtotal, float iva, float descuento, float total) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.iva = iva;
        this.descuento = descuento;
        this.total = total;
        this.detalles = new ArrayList<>();
    }

    // Getters y Setters
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    // Método de apoyo para agregar ítems
    public void agregarDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
    }
}
