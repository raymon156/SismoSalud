package vista;

import clases.Hospital;
import clases.Prioridad;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Principal extends JFrame {

    // Único objeto Hospital: todas las ventanas trabajan con los mismos datos
    private Hospital hospital = new Hospital();

    public Principal() {
        cargarDatosDemo();
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        setTitle("Sismo-Salud PE");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 380);
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel("Sismo-Salud PE", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        titulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));

        JButton btnPacientes = new JButton("Ingresar");
        JButton btnCamas = new JButton("Ingresar");
        JButton btnMedicamentos = new JButton("Ingresar");

        // Eventos: cada botón abre su ventana
        btnPacientes.addActionListener(e -> abrirVentana(new RegistroPacientes(hospital)));
        btnCamas.addActionListener(e -> abrirVentana(new DisponibilidadCamasPersonal(hospital)));
        btnMedicamentos.addActionListener(e -> abrirVentana(new InventarioMedicamentos(hospital)));

        JPanel opciones = new JPanel(new GridLayout(3, 2, 20, 25));
        opciones.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        opciones.add(new JLabel("Registro de pacientes"));
        opciones.add(btnPacientes);
        opciones.add(new JLabel("Disponibilidad de camas y personal"));
        opciones.add(btnCamas);
        opciones.add(new JLabel("Inventario de medicamentos"));
        opciones.add(btnMedicamentos);

        add(titulo, BorderLayout.NORTH);
        add(opciones, BorderLayout.CENTER);
    }

    private void abrirVentana(JFrame ventana) {
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    // Datos de ejemplo (todos ficticios)
    private void cargarDatosDemo() {
        hospital.registrarPaciente("Ana Torres Díaz", "70000001", 34, Prioridad.ROJO);
        hospital.registrarPaciente("Luis Rojas Peña", "70000002", 58, Prioridad.AMARILLO);
        hospital.registrarPaciente("Marta Soto Vega", "70000003", 12, Prioridad.VERDE);
        hospital.registrarCama(1);
        hospital.registrarCama(2);
        hospital.registrarCama(3);
        hospital.registrarPersonal("Dr. Carlos Ramos");
        hospital.registrarPersonal("Dra. Elena Paredes");
        hospital.registrarMedicamento("M001", "Paracetamol 500 mg", 200, 50);
        hospital.registrarMedicamento("M002", "Suero fisiológico", 30, 40);
        hospital.registrarMedicamento("M003", "Gasas estériles", 120, 60);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Principal().setVisible(true));
    }
}
