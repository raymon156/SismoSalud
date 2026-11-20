package clases;

public class Cama {

    private int numero;
    private Paciente ocupante;

    public Cama(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de cama debe ser mayor que cero.");
        }
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public Paciente getOcupante() {
        return ocupante;
    }

    public boolean estaDisponible() {
        return ocupante == null;
    }

    public void asignar(Paciente p) {
        if (!estaDisponible()) {
            throw new IllegalStateException("La cama " + numero + " ya está ocupada.");
        }
        ocupante = p;
    }

    public Paciente liberar() {
        Paciente anterior = ocupante;
        ocupante = null;
        return anterior;
    }
}
