package clases;

public enum Prioridad {
    ROJO(1, "Inmediata"),
    AMARILLO(2, "Urgente"),
    VERDE(3, "No urgente");

    private final int nivel;
    private final String descripcion;

    Prioridad(int nivel, String descripcion) {
        this.nivel = nivel;
        this.descripcion = descripcion;
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public String toString() {
        return name() + " - " + descripcion;
    }
}
