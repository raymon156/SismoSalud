package vista;

import clases.Hospital;
import clases.Medicamento;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
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

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private Hospital hospital;
    private JTextField txtCodigo = new JTextField(6);
    private JTextField txtNombre = new JTextField(12);
    private JTextField txtStock = new JTextField(5);
    private JTextField txtMinimo = new JTextField(5);
    private JTextField txtVencimiento = new JTextField(8);
    private JButton btnAgregar = new JButton("Agregar");
    private JButton btnActualizar = new JButton("Actualizar stock");
    private JLabel lblAlertas = new JLabel(" ");
    private DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Código", "Nombre", "Stock", "Mínimo", "Vencimiento", "Estado"}, 0) {
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
        setSize(780, 460);

        // Las filas con stock bajo se resaltan en rojo (RF06)
        tabla = new JTable(modelo) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int fila, int columna) {
                Component c = super.prepareRenderer(r, fila, columna);
                if (!isRowSelected(fila)) {
                    boolean bajo = "STOCK BAJO".equals(getValueAt(fila, 5));
                    c.setBackground(bajo ? new Color(255, 200, 200) : Color.WHITE);
                }
                return c;
            }
        };

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        fila1.add(new JLabel("Código:"));
        fila1.add(txtCodigo);
        fila1.add(new JLabel("Nombre:"));
        fila1.add(txtNombre);
        fila1.add(new JLabel("Stock:"));
        fila1.add(txtStock);
        fila1.add(new JLabel("Mínimo:"));
        fila1.add(txtMinimo);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        fila2.add(new JLabel("Vencimiento (dd/mm/aaaa):"));
        fila2.add(txtVencimiento);
        fila2.add(btnAgregar);
        fila2.add(btnActualizar);

        JPanel formulario = new JPanel(new GridLayout(2, 1));
        formulario.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        formulario.add(fila1);
        formulario.add(fila2);

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
            LocalDate vencimiento = LocalDate.parse(txtVencimiento.getText().trim(), FORMATO_FECHA);
            Medicamento m = hospital.registrarMedicamento(txtCodigo.getText(), txtNombre.getText(),
                    stock, minimo, vencimiento);
            txtCodigo.setText("");
            txtNombre.setText("");
            txtStock.setText("");
            txtMinimo.setText("");
            txtVencimiento.setText("");
            cargarTabla();
            mostrarResultado(m, "registrado");
        } catch (NumberFormatException ex) {
            mostrarError("El stock y el mínimo deben ser números enteros.");
        } catch (DateTimeParseException ex) {
            mostrarError("La fecha de vencimiento debe tener el formato dd/mm/aaaa (por ejemplo, 31/12/2027).");
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
                m.getFechaVencimiento().format(FORMATO_FECHA), m.tieneStockBajo() ? "STOCK BAJO" : "Normal"});
        }
        int bajos = hospital.medicamentosStockBajo().size();
        lblAlertas.setText(bajos == 0 ? " " : "⚠ Medicamentos con stock bajo: " + bajos);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
