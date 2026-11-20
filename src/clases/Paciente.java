package clases;

public class Paciente extends Persona {

    private String id;
    private String dni;
    private int edad;
    private Prioridad prioridad;

    public Paciente(String id, String nombreCompleto, String dni, int edad, Prioridad prioridad) {
        super(nombreCompleto);
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos numéricos.");
        }
        if (edad < 0 || edad > 120) {
            throw new IllegalArgumentException("La edad debe estar entre 0 y 120 años.");
        }
        if (prioridad == null) {
            throw new IllegalArgumentException("Debe seleccionar una prioridad.");
        }
        this.id = id;
        this.dni = dni;
        this.edad = edad;
        this.prioridad = prioridad;
    }

    public String getId() {
        return id;
    }

    public int getEdad() {
        return edad;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    // RNF04: el DNI completo nunca sale de la clase, solo se muestran los 2 últimos dígitos
    public String getDniEnmascarado() {
        return "******" + dni.substring(6);
    }

    public boolean coincideDni(String otroDni) {
        return dni.equals(otroDni);
    }

    @Override
    public String resumen() {
        return id + " - " + getNombreCompleto() + " (" + prioridad + ")";
    }

    @Override
    public String toString() {
        return resumen();
    }
}
