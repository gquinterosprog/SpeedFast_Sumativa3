package speedfast.vista;

import speedfast.dao.EntregaDAO;
import speedfast.dao.PedidoDAO;
import speedfast.dao.RepartidorDAO;
import speedfast.modelo.RegistroEntrega;
import speedfast.modelo.RegistroPedido;
import speedfast.modelo.RegistroRepartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

class PanelEntregas extends JPanel {
    private final EntregaDAO dao = new EntregaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final JComboBox<RegistroPedido> pedidos = new JComboBox<>();
    private final JComboBox<RegistroRepartidor> repartidores = new JComboBox<>();
    private final JTextField fecha = new JTextField(LocalDate.now().toString(), 10);
    private final JTextField hora = new JTextField(LocalTime.now().withSecond(0).withNano(0).toString(), 7);
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Pedido ID", "Repartidor ID", "Fecha", "Hora"}, 0) {
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private final Runnable despuesDeCambio;

    PanelEntregas(Runnable despuesDeCambio) {
        super(new BorderLayout(10, 10));
        this.despuesDeCambio = despuesDeCambio;
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        pedidos.setPreferredSize(new Dimension(230, 26));
        repartidores.setPreferredSize(new Dimension(190, 26));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Pedido:"));
        formulario.add(pedidos);
        formulario.add(new JLabel("Repartidor:"));
        formulario.add(repartidores);
        formulario.add(new JLabel("Fecha (AAAA-MM-DD):"));
        formulario.add(fecha);
        formulario.add(new JLabel("Hora (HH:mm):"));
        formulario.add(hora);

        JButton crear = new JButton("Registrar");
        JButton actualizar = new JButton("Editar seleccionado");
        JButton eliminar = new JButton("Eliminar seleccionado");
        JButton recargar = new JButton("Actualizar datos");
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
                seleccionarPedido((Integer) modelo.getValueAt(fila, 1));
                seleccionarRepartidor((Integer) modelo.getValueAt(fila, 2));
                fecha.setText(modelo.getValueAt(fila, 3).toString());
                hora.setText(modelo.getValueAt(fila, 4).toString().substring(0, 5));
            }
        });
        crear.addActionListener(e -> crear());
        actualizar.addActionListener(e -> actualizar());
        eliminar.addActionListener(e -> eliminar());
        recargar.addActionListener(e -> refrescar());
    }

    void refrescar() {
        try {
            Integer pedidoActual = pedidos.getSelectedItem() == null ? null
                    : ((RegistroPedido) pedidos.getSelectedItem()).getId();
            Integer repartidorActual = repartidores.getSelectedItem() == null ? null
                    : ((RegistroRepartidor) repartidores.getSelectedItem()).getId();
            pedidos.removeAllItems();
            for (RegistroPedido p : pedidoDAO.readAll()) pedidos.addItem(p);
            repartidores.removeAllItems();
            for (RegistroRepartidor r : repartidorDAO.readAll()) repartidores.addItem(r);
            if (pedidoActual != null) seleccionarPedido(pedidoActual);
            if (repartidorActual != null) seleccionarRepartidor(repartidorActual);
            modelo.setRowCount(0);
            for (RegistroEntrega e : dao.readAll()) {
                modelo.addRow(new Object[]{e.getId(), e.getPedidoId(), e.getRepartidorId(),
                        e.getFecha(), e.getHora()});
            }
        } catch (SQLException e) { Mensajes.errorSql(this, e); }
    }

    private void seleccionarPedido(int id) {
        for (int i = 0; i < pedidos.getItemCount(); i++) {
            if (pedidos.getItemAt(i).getId() == id) { pedidos.setSelectedIndex(i); return; }
        }
    }

    private void seleccionarRepartidor(int id) {
        for (int i = 0; i < repartidores.getItemCount(); i++) {
            if (repartidores.getItemAt(i).getId() == id) { repartidores.setSelectedIndex(i); return; }
        }
    }

    private RegistroEntrega leerFormulario(int id) {
        RegistroPedido p = (RegistroPedido) pedidos.getSelectedItem();
        RegistroRepartidor r = (RegistroRepartidor) repartidores.getSelectedItem();
        if (p == null || r == null) {
            Mensajes.error(this, "Registra al menos un pedido y un repartidor antes de crear una entrega.");
            return null;
        }
        try {
            LocalDate f = LocalDate.parse(fecha.getText().trim());
            LocalTime h = LocalTime.parse(hora.getText().trim());
            return new RegistroEntrega(id, p.getId(), r.getId(), f, h);
        } catch (DateTimeParseException e) {
            Mensajes.error(this, "Usa fecha AAAA-MM-DD y hora HH:mm (por ejemplo, 2026-10-05 y 19:30).");
            return null;
        }
    }

    private Integer idSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            Mensajes.error(this, "Selecciona una entrega en la tabla.");
            return null;
        }
        return (Integer) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
    }

    private void crear() {
        RegistroEntrega e = leerFormulario(0);
        if (e == null) return;
        try {
            dao.create(e);
            despuesDeCambio.run();
            Mensajes.info(this, "Entrega registrada.");
        } catch (SQLException error) { Mensajes.errorSql(this, error); }
    }

    private void actualizar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        RegistroEntrega e = leerFormulario(id);
        if (e == null) return;
        try {
            if (dao.update(e)) {
                despuesDeCambio.run();
                Mensajes.info(this, "Entrega actualizada.");
            } else Mensajes.error(this, "La entrega ya no existe.");
        } catch (SQLException error) { Mensajes.errorSql(this, error); }
    }

    private void eliminar() {
        Integer id = idSeleccionado();
        if (id == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar la entrega #" + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            if (dao.delete(id)) {
                despuesDeCambio.run();
                Mensajes.info(this, "Entrega eliminada.");
            } else Mensajes.error(this, "La entrega ya no existe.");
        } catch (SQLException error) { Mensajes.errorSql(this, error); }
    }
}
