package vista;

import clases.Hospital;
import clases.Medicamento;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
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
import javax.swing.table.TableCellRenderer;

public class InventarioMedicamentos extends JFrame {

    private Hospital hospital;
    private JTextField txtCodigo = new JTextField(6);
    private JTextField txtNombre = new JTextField(12);
    private JTextField txtStock = new JTextField(5);
    private JTextField txtMinimo = new JTextField(5);
    private JButton btnAgregar = new JButton("Agregar");
    private JButton btnActualizar = new JButton("Actualizar stock");
    private JLabel lblAlertas = new JLabel(" ");
    private DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Código", "Nombre", "Stock", "Mínimo", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private JTable tabla;

    public InventarioMedicamentos(Hospital hospital) {
        this.hospital = hospital;
        iniciarComponentes();
        cargarTabla();
    }

    private void iniciarComponentes() {
        setTitle("Inventario de medicamentos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(720, 460);

        // Las filas con stock bajo se resaltan en rojo (RF06)
        tabla = new JTable(modelo) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int fila, int columna) {
                Component c = super.prepareRenderer(r, fila, columna);
                if (!isRowSelected(fila)) {
                    boolean bajo = "STOCK BAJO".equals(getValueAt(fila, 4));
                    c.setBackground(bajo ? new Color(255, 200, 200) : Color.WHITE);
                }
                return c;
            }
        };

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        formulario.add(new JLabel("Código:"));
        formulario.add(txtCodigo);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Stock:"));
        formulario.add(txtStock);
        formulario.add(new JLabel("Mínimo:"));
        formulario.add(txtMinimo);
        formulario.add(btnAgregar);
        formulario.add(btnActualizar);

        lblAlertas.setForeground(Color.RED);
        lblAlertas.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(lblAlertas, BorderLayout.SOUTH);

        // Eventos
        btnAgregar.addActionListener(e -> agregar());
        btnActualizar.addActionListener(e -> actualizarStock());
    }

    private void agregar() {
        try {
            int stock = Integer.parseInt(txtStock.getText().trim());
            int minimo = Integer.parseInt(txtMinimo.getText().trim());
            Medicamento m = hospital.registrarMedicamento(txtCodigo.getText(), txtNombre.getText(), stock, minimo);
            txtCodigo.setText("");
            txtNombre.setText("");
            txtStock.setText("");
            txtMinimo.setText("");
            cargarTabla();
            mostrarResultado(m, "registrado");
        } catch (NumberFormatException ex) {
            mostrarError("El stock y el mínimo deben ser números enteros.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void actualizarStock() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un medicamento de la tabla.");
            return;
        }
        Medicamento m = hospital.getMedicamentos().get(fila);
        String texto = JOptionPane.showInputDialog(this, "Nuevo stock de " + m.getNombre() + ":", m.getStock());
        if (texto == null) {
            return;
        }
        try {
            m.actualizarStock(Integer.parseInt(texto.trim()));
            cargarTabla();
            mostrarResultado(m, "actualizado");
        } catch (NumberFormatException ex) {
            mostrarError("El stock debe ser un número entero.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // RF06: muestra la alerta cuando el medicamento queda con pocas existencias
    private void mostrarResultado(Medicamento m, String accion) {
        if (m.tieneStockBajo()) {
            JOptionPane.showMessageDialog(this,
                    "Medicamento " + accion + ", pero tiene pocas existencias: " + m.getStock()
                    + " unidades (mínimo " + m.getStockMinimo() + ").",
                    "Alerta de stock bajo", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Medicamento " + accion + " correctamente.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Medicamento m : hospital.getMedicamentos()) {
            modelo.addRow(new Object[]{m.getCodigo(), m.getNombre(), m.getStock(), m.getStockMinimo(),
                m.tieneStockBajo() ? "STOCK BAJO" : "Normal"});
        }
        int bajos = hospital.medicamentosStockBajo().size();
        lblAlertas.setText(bajos == 0 ? " " : "⚠ Medicamentos con stock bajo: " + bajos);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
