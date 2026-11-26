package clases;

import java.time.LocalDate;

public class Medicamento {

    private String codigo;
    private String nombre;
    private int stock;
    private int stockMinimo;
    private LocalDate fechaVencimiento;

    public Medicamento(String codigo, String nombre, int stock, int stockMinimo, LocalDate fechaVencimiento) {
        if (codigo == null || codigo.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El código y el nombre son obligatorios.");
        }
        if (stock < 0 || stockMinimo < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (fechaVencimiento == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria.");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.fechaVencimiento = fechaVencimiento;
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

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
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
