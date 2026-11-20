package clases;

public class Medicamento {

    private String codigo;
    private String nombre;
    private int stock;
    private int stockMinimo;

    public Medicamento(String codigo, String nombre, int stock, int stockMinimo) {
        if (codigo == null || codigo.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El código y el nombre son obligatorios.");
        }
        if (stock < 0 || stockMinimo < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStock() {
        return stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void actualizarStock(int nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.stock = nuevoStock;
    }

    // RF06: la alerta se calcula dentro de la propia clase
    public boolean tieneStockBajo() {
        return stock <= stockMinimo;
    }
}
