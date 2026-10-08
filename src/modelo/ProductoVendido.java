package modelo;

public class ProductoVendido {

    private final String codigo;
    private final String nombre;
    private final int unidades;
    private final float monto;

    public ProductoVendido(String codigo, String nombre, int unidades, float monto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.unidades = unidades;
        this.monto = monto;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getUnidades() { return unidades; }
    public float getMonto() { return monto; }
}