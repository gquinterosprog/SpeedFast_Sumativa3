package speedfast.vista;

import speedfast.dao.RepartidorDAO;
import speedfast.modelo.RegistroRepartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

class PanelRepartidores extends JPanel {
    private final RepartidorDAO dao = new RepartidorDAO();
    private final JTextField nombre = new JTextField(22);
    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private final Runnable despuesDeCambio;

    PanelRepartidores(Runnable despuesDeCambio) {
        super(new BorderLayout(10, 10));
        this.despuesDeCambio = despuesDeCambio;
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(nombre);

        JButton crear = new JButton("Registrar");
        JButton actualizar = new JButton("Editar seleccionado");
        JButton eliminar = new JButton("Eliminar seleccionado");
        JButton recargar = new JButton("Actualizar tabla");
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acciones.add(crear);
        acciones.add(actualizar);
        acciones.add(eliminar);
        acciones.add(recargar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(formulario, BorderLayout.NORTH);
        superior.add(acciones, BorderLayout.SOUTH);
        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.convertRowIndexToModel(tabla.getSelectedRow());
                nombre.setText((String) modelo.getValueAt(fila, 1));
            }
        });
        crear.addActionListener(e -> crear());
        actualizar.addActionListener(e -> actualizar());
        eliminar.addActionListener(e -> eliminar());
        recargar.addActionListener(e -> refrescar());
    }

    void refrescar() {
        try {
            modelo.setRowCount(0);
            for (RegistroRepartidor r : dao.readAll()) modelo.addRow(new Object[]{r.getId(), r.getNombre()});
        } catch (SQLException e) { Mensajes.errorSql(this, e); }
    }

    private String validarNombre() {
        String dato = nombre.getText().trim();
        if (dato.isEmpty() || dato.length() > 100) {
            Mensajes.error(this, "Ingresa un nombre de 1 a 100 caracteres.");
            return null;
        }
        return dato;
    }

    private Integer idSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            Mensajes.error(this, "Selecciona un repartidor en la tabla.");
            return null;
        }
        return (Integer) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
    }

    private void crear() {
        String dato = validarNombre();
        if (dato == null) return;
        try {
            dao.create(new RegistroRepartidor(0, dato));
            nombre.setText("");
            despuesDeCambio.run();
            Mensajes.info(this, "Repartidor registrado.");
        } catch (SQLException e) { Mensajes.errorSql(this, e); }
    }

    private void actualizar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        String dato = validarNombre();
        if (dato == null) return;
        try {
            if (dao.update(new RegistroRepartidor(id, dato))) {
                despuesDeCambio.run();
                Mensajes.info(this, "Repartidor actualizado.");
            } else Mensajes.error(this, "El repartidor ya no existe.");
        } catch (SQLException e) { Mensajes.errorSql(this, e); }
    }

    private void eliminar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el repartidor #" + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            if (dao.delete(id)) {
                nombre.setText("");
                despuesDeCambio.run();
                Mensajes.info(this, "Repartidor eliminado.");
            } else Mensajes.error(this, "El repartidor ya no existe.");
        } catch (SQLException e) { Mensajes.errorSql(this, e); }
    }
}
