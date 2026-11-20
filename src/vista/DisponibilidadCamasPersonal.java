package vista;

import clases.Cama;
import clases.Hospital;
import clases.Paciente;
import clases.PersonalMedico;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DisponibilidadCamasPersonal extends JFrame {

    private Hospital hospital;
    private JTextField txtNumeroCama = new JTextField(8);
    private JTextField txtNombrePersonal = new JTextField(14);
    private JButton btnAgregarCama = new JButton("Agregar");
    private JButton btnAsignarCama = new JButton("Asignar");
    private JButton btnLiberarCama = new JButton("Liberar");
    private JButton btnAgregarPersonal = new JButton("Agregar");
    private JButton btnAsignarPersonal = new JButton("Asignar");
    private JButton btnLiberarPersonal = new JButton("Liberar");
    private DefaultTableModel modeloCamas = crearModelo(new String[]{"Número", "Estado", "Paciente"});
    private DefaultTableModel modeloPersonal = crearModelo(new String[]{"Nombre", "Estado", "Paciente"});
    private JTable tablaCamas = new JTable(modeloCamas);
    private JTable tablaPersonal = new JTable(modeloPersonal);

    public DisponibilidadCamasPersonal(Hospital hospital) {
        this.hospital = hospital;
        iniciarComponentes();
        cargarTablas();
    }

    private DefaultTableModel crearModelo(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private void iniciarComponentes() {
        setTitle("Disponibilidad de camas y personal");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(820, 480);

        JPanel panelCamas = crearPanel("Registro de cama", "Número de cama:", txtNumeroCama,
                btnAgregarCama, tablaCamas, btnAsignarCama, btnLiberarCama);
        JPanel panelPersonal = crearPanel("Registro de personal", "Nombre completo:", txtNombrePersonal,
                btnAgregarPersonal, tablaPersonal, btnAsignarPersonal, btnLiberarPersonal);

        JPanel centro = new JPanel(new GridLayout(1, 2, 15, 0));
        centro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centro.add(panelCamas);
        centro.add(panelPersonal);
        add(centro, BorderLayout.CENTER);

        // Eventos
        btnAgregarCama.addActionListener(e -> agregarCama());
        btnAsignarCama.addActionListener(e -> asignarCama());
        btnLiberarCama.addActionListener(e -> liberarCama());
        btnAgregarPersonal.addActionListener(e -> agregarPersonal());
        btnAsignarPersonal.addActionListener(e -> asignarPersonal());
        btnLiberarPersonal.addActionListener(e -> liberarPersonal());
    }

    private JPanel crearPanel(String titulo, String etiqueta, JTextField campo, JButton btnAgregar,
            JTable tabla, JButton btnAsignar, JButton btnLiberar) {
        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        arriba.add(new JLabel(etiqueta));
        arriba.add(campo);
        arriba.add(btnAgregar);

        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        abajo.add(btnAsignar);
        abajo.add(btnLiberar);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        panel.add(arriba, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(abajo, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- Camas ----------
    private void agregarCama() {
        try {
            int numero = Integer.parseInt(txtNumeroCama.getText().trim());
            hospital.registrarCama(numero);
            txtNumeroCama.setText("");
            cargarTablas();
            mostrarInfo("Cama " + numero + " registrada correctamente.");
        } catch (NumberFormatException ex) {
            mostrarError("El número de cama debe ser un número entero.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void asignarCama() {
        int fila = tablaCamas.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione una cama de la tabla.");
            return;
        }
        Cama cama = hospital.getCamas().get(fila);
        List<Paciente> sinCama = hospital.pacientesSinCama();
        if (sinCama.isEmpty()) {
            mostrarError("No hay pacientes sin cama.");
            return;
        }
        Paciente elegido = elegirPaciente(sinCama, "Asignar cama " + cama.getNumero());
        if (elegido == null) {
            return;
        }
        try {
            cama.asignar(elegido);
            cargarTablas();
            mostrarInfo("Cama " + cama.getNumero() + " asignada a " + elegido.getNombreCompleto() + ".");
        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void liberarCama() {
        int fila = tablaCamas.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione una cama de la tabla.");
            return;
        }
        Cama cama = hospital.getCamas().get(fila);
        if (cama.estaDisponible()) {
            mostrarError("La cama " + cama.getNumero() + " ya está libre.");
            return;
        }
        cama.liberar();
        cargarTablas();
        mostrarInfo("Cama " + cama.getNumero() + " liberada.");
    }

    // ---------- Personal ----------
    private void agregarPersonal() {
        try {
            hospital.registrarPersonal(txtNombrePersonal.getText());
            txtNombrePersonal.setText("");
            cargarTablas();
            mostrarInfo("Personal registrado correctamente.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void asignarPersonal() {
        int fila = tablaPersonal.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione a un integrante del personal.");
            return;
        }
        PersonalMedico medico = hospital.getPersonal().get(fila);
        List<Paciente> todos = hospital.pacientesPorPrioridad(null);
        if (todos.isEmpty()) {
            mostrarError("No hay pacientes registrados.");
            return;
        }
        Paciente elegido = elegirPaciente(todos, "Asignar a " + medico.getNombreCompleto());
        if (elegido == null) {
            return;
        }
        try {
            medico.asignar(elegido);
            cargarTablas();
            mostrarInfo(medico.getNombreCompleto() + " atenderá a " + elegido.getNombreCompleto() + ".");
        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void liberarPersonal() {
        int fila = tablaPersonal.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione a un integrante del personal.");
            return;
        }
        PersonalMedico medico = hospital.getPersonal().get(fila);
        if (medico.estaDisponible()) {
            mostrarError(medico.getNombreCompleto() + " ya está disponible.");
            return;
        }
        medico.liberar();
        cargarTablas();
        mostrarInfo(medico.getNombreCompleto() + " quedó disponible.");
    }

    // ---------- Apoyo ----------
    private Paciente elegirPaciente(List<Paciente> lista, String titulo) {
        return (Paciente) JOptionPane.showInputDialog(this, "Seleccione el paciente:", titulo,
                JOptionPane.QUESTION_MESSAGE, null, lista.toArray(), lista.get(0));
    }

    private void cargarTablas() {
        modeloCamas.setRowCount(0);
        for (Cama c : hospital.getCamas()) {
            modeloCamas.addRow(new Object[]{c.getNumero(),
                c.estaDisponible() ? "Libre" : "Ocupada",
                c.estaDisponible() ? "-" : c.getOcupante().getNombreCompleto()});
        }
        modeloPersonal.setRowCount(0);
        for (PersonalMedico m : hospital.getPersonal()) {
            modeloPersonal.addRow(new Object[]{m.getNombreCompleto(),
                m.estaDisponible() ? "Disponible" : "Ocupado",
                m.estaDisponible() ? "-" : m.getPacienteAsignado().getNombreCompleto()});
        }
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
