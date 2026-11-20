package vista;

import clases.Hospital;
import clases.Paciente;
import clases.Prioridad;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class RegistroPacientes extends JFrame {

    private Hospital hospital;
    private JTextField txtNombre = new JTextField(14);
    private JTextField txtDni = new JTextField(8);
    private JTextField txtEdad = new JTextField(4);
    private JComboBox<Prioridad> cmbPrioridad = new JComboBox<>(Prioridad.values());
    private JComboBox<Object> cmbFiltro = new JComboBox<>();
    private JButton btnRegistrar = new JButton("Registrar");
    private JButton btnLimpiar = new JButton("Limpiar");
    private DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nombre completo", "DNI", "Edad", "Prioridad"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private JTable tabla;

    public RegistroPacientes(Hospital hospital) {
        this.hospital = hospital;
        iniciarComponentes();
        cargarTabla();
    }

    private void iniciarComponentes() {
        setTitle("Registro de pacientes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);   // al cerrar se vuelve a la ventana principal
        setSize(760, 480);

        cmbFiltro.addItem("Todas");
        for (Prioridad p : Prioridad.values()) {
            cmbFiltro.addItem(p);
        }

        // La prioridad se muestra con el color del triaje
        tabla = new JTable(modelo) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int fila, int columna) {
                Component c = super.prepareRenderer(r, fila, columna);
                if (!isRowSelected(fila)) {
                    String prioridad = String.valueOf(getValueAt(fila, 4));
                    if (prioridad.startsWith("ROJO")) {
                        c.setBackground(new Color(255, 200, 200));
                    } else if (prioridad.startsWith("AMARILLO")) {
                        c.setBackground(new Color(255, 245, 190));
                    } else {
                        c.setBackground(new Color(205, 240, 205));
                    }
                }
                return c;
            }
        };

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        formulario.add(new JLabel("Nombre completo:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("DNI:"));
        formulario.add(txtDni);
        formulario.add(new JLabel("Edad:"));
        formulario.add(txtEdad);
        formulario.add(new JLabel("Prioridad:"));
        formulario.add(cmbPrioridad);
        formulario.add(btnRegistrar);
        formulario.add(btnLimpiar);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filtro.add(new JLabel("Mostrar prioridad:"));
        filtro.add(cmbFiltro);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(formulario, BorderLayout.NORTH);
        norte.add(filtro, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Eventos
        btnRegistrar.addActionListener(e -> registrar());
        btnLimpiar.addActionListener(e -> limpiar());
        cmbFiltro.addActionListener(e -> cargarTabla());
    }

    private void registrar() {
        try {
            int edad = Integer.parseInt(txtEdad.getText().trim());
            Paciente p = hospital.registrarPaciente(txtNombre.getText(), txtDni.getText().trim(),
                    edad, (Prioridad) cmbPrioridad.getSelectedItem());
            JOptionPane.showMessageDialog(this,
                    "Paciente " + p.getId() + " registrado con prioridad " + p.getPrioridad() + ".",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
            cargarTabla();
        } catch (NumberFormatException ex) {
            mostrarError("La edad debe ser un número entero.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtDni.setText("");
        txtEdad.setText("");
        cmbPrioridad.setSelectedIndex(0);
        txtNombre.requestFocus();
    }

    private void cargarTabla() {
        Object seleccion = cmbFiltro.getSelectedItem();
        Prioridad filtro = (seleccion instanceof Prioridad) ? (Prioridad) seleccion : null;
        modelo.setRowCount(0);
        for (Paciente p : hospital.pacientesPorPrioridad(filtro)) {
            modelo.addRow(new Object[]{p.getId(), p.getNombreCompleto(), p.getDniEnmascarado(),
                p.getEdad(), p.getPrioridad()});
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
