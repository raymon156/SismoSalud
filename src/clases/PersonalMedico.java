package clases;

public class PersonalMedico extends Persona {

    private Paciente pacienteAsignado;

    public PersonalMedico(String nombreCompleto) {
        super(nombreCompleto);
    }

    public Paciente getPacienteAsignado() {
        return pacienteAsignado;
    }

    public boolean estaDisponible() {
        return pacienteAsignado == null;
    }

    public void asignar(Paciente p) {
        if (!estaDisponible()) {
            throw new IllegalStateException("El personal ya tiene un paciente asignado.");
        }
        pacienteAsignado = p;
    }

    public Paciente liberar() {
        Paciente anterior = pacienteAsignado;
        pacienteAsignado = null;
        return anterior;
    }

    @Override
    public String resumen() {
        return getNombreCompleto() + (estaDisponible() ? " (disponible)" : " (atiende a " + pacienteAsignado.getNombreCompleto() + ")");
    }
}
