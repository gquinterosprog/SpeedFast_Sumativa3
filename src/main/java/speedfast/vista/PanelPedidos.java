package speedfast.vista;

import speedfast.dao.PedidoDAO;
import speedfast.modelo.EstadoPedido;
import speedfast.modelo.RegistroPedido;
import speedfast.modelo.TipoPedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

class PanelPedidos extends JPanel {
    private final PedidoDAO dao = new PedidoDAO();
    private final JTextField direccion = new JTextField(24);
    private final JComboBox<TipoPedido> tipo = new JComboBox<>(TipoPedido.values());
    private final JComboBox<EstadoPedido> estado = new JComboBox<>(new EstadoPedido[]{
            EstadoPedido.PENDIENTE, EstadoPedido.EN_REPARTO, EstadoPedido.ENTREGADO});
    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Dirección", "Tipo", "Estado"}, 0) {
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private final Runnable despuesDeCambio;

    PanelPedidos(Runnable despuesDeCambio) {
        super(new BorderLayout(10, 10));
        this.despuesDeCambio = despuesDeCambio;
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Dirección:"));
        formulario.add(direccion);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(tipo);
        formulario.add(new JLabel("Estado:"));
        formulario.add(estado);

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
                direccion.setText((String) modelo.getValueAt(fila, 1));
                tipo.setSelectedItem(modelo.getValueAt(fila, 2));
                estado.setSelectedItem(modelo.getValueAt(fila, 3));
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
            for (RegistroPedido p : dao.readAll())
                modelo.addRow(new Object[]{p.getId(), p.getDireccion(), p.getTipo(), p.getEstado()});
        } catch (SQLException e) { Mensajes.errorSql(this, e); }
    }

    private RegistroPedido leerFormulario(int id) {
        String dato = direccion.getText().trim();
        if (dato.isEmpty() || dato.length() > 100) {
            Mensajes.error(this, "Ingresa una dirección de 1 a 100 caracteres.");
            return null;
        }
        return new RegistroPedido(id, dato, (TipoPedido) tipo.getSelectedItem(),
                (EstadoPedido) estado.getSelectedItem());
    }

    private Integer idSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            Mensajes.error(this, "Selecciona un pedido en la tabla.");
            return null;
        }
        return (Integer) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
    }

    private void crear() {
        RegistroPedido p = leerFormulario(0);
        if (p == null) return;
        try {
            dao.create(p);
            direccion.setText("");
            despuesDeCambio.run();
            Mensajes.info(this, "Pedido registrado.");
        } catch (SQLException e) { Mensajes.errorSql(this, e); }
    }

    private void actualizar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        RegistroPedido p = leerFormulario(id);
        if (p == null) return;
        try {
            if (dao.update(p)) {
                despuesDeCambio.run();
                Mensajes.info(this, "Pedido actualizado.");
            } else Mensajes.error(this, "El pedido ya no existe.");
        } catch (SQLException e) { Mensajes.errorSql(this, e); }
    }

    private void eliminar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el pedido #" + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            if (dao.delete(id)) {
                direccion.setText("");
                despuesDeCambio.run();
                Mensajes.info(this, "Pedido eliminado.");
            } else Mensajes.error(this, "El pedido ya no existe.");
        } catch (SQLException e) { Mensajes.errorSql(this, e); }
    }
}
