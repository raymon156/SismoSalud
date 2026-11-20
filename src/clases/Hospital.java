package clases;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Hospital {

    private List<Paciente> pacientes = new ArrayList<>();
    private List<PersonalMedico> personal = new ArrayList<>();
    private List<Cama> camas = new ArrayList<>();
    private List<Medicamento> medicamentos = new ArrayList<>();
    private int contadorPacientes = 0;

    // ---------- Pacientes (RF01, RF02, RF03) ----------
    public Paciente registrarPaciente(String nombre, String dni, int edad, Prioridad prioridad) {
        for (Paciente p : pacientes) {
            if (p.coincideDni(dni)) {
                throw new IllegalArgumentException("Ya existe un paciente con ese DNI.");
            }
        }
        String id = String.format("P%03d", contadorPacientes + 1);
        Paciente nuevo = new Paciente(id, nombre, dni, edad, prioridad);
        contadorPacientes++;
        pacientes.add(nuevo);
        return nuevo;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    // Función de orden superior: el criterio de filtrado llega como parámetro
    public List<Paciente> filtrarPacientes(Predicate<Paciente> criterio) {
        return pacientes.stream().filter(criterio).collect(Collectors.toList());
    }

    // Ordena del más urgente (ROJO) al menos urgente (VERDE)
    public List<Paciente> pacientesPorPrioridad(Prioridad filtro) {
        return pacientes.stream()
                .filter(p -> filtro == null || p.getPrioridad() == filtro)
                .sorted(Comparator.comparingInt(p -> p.getPrioridad().getNivel()))
                .collect(Collectors.toList());
    }

    // ---------- Camas (RF04) ----------
    public Cama registrarCama(int numero) {
        for (Cama c : camas) {
            if (c.getNumero() == numero) {
                throw new IllegalArgumentException("La cama " + numero + " ya está registrada.");
            }
        }
        Cama nueva = new Cama(numero);
        camas.add(nueva);
        return nueva;
    }

    public List<Cama> getCamas() {
        return camas;
    }

    public List<Cama> camasDisponibles() {
        return camas.stream().filter(Cama::estaDisponible).collect(Collectors.toList());
    }

    public boolean tieneCama(Paciente p) {
        return camas.stream().anyMatch(c -> c.getOcupante() == p);
    }

    public List<Paciente> pacientesSinCama() {
        return pacientesPorPrioridad(null).stream().filter(p -> !tieneCama(p)).collect(Collectors.toList());
    }

    // ---------- Personal médico ----------
    public PersonalMedico registrarPersonal(String nombre) {
        PersonalMedico nuevo = new PersonalMedico(nombre);
        personal.add(nuevo);
        return nuevo;
    }

    public List<PersonalMedico> getPersonal() {
        return personal;
    }

    // ---------- Medicamentos (RF05, RF06) ----------
    public Medicamento registrarMedicamento(String codigo, String nombre, int stock, int minimo) {
        for (Medicamento m : medicamentos) {
            if (m.getCodigo().equalsIgnoreCase(codigo.trim())) {
                throw new IllegalArgumentException("Ya existe un medicamento con el código " + codigo.trim() + ".");
            }
        }
        Medicamento nuevo = new Medicamento(codigo, nombre, stock, minimo);
        medicamentos.add(nuevo);
        return nuevo;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public List<Medicamento> medicamentosStockBajo() {
        return medicamentos.stream().filter(Medicamento::tieneStockBajo).collect(Collectors.toList());
    }
}
