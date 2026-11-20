package clases;

public abstract class Persona {

    private String nombreCompleto;

    public Persona(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio.");
        }
        this.nombreCompleto = nombreCompleto.trim();
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public abstract String resumen();
}
