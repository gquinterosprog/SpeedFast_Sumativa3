package speedfast.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final PanelRepartidores panelRepartidores;
    private final PanelPedidos panelPedidos;
    private final PanelEntregas panelEntregas;
    private final JTabbedPane pestañas = new JTabbedPane();

    public VentanaPrincipal() {
        super("SpeedFast - Gestión de pedidos y entregas");
        panelRepartidores = new PanelRepartidores(this::refrescarTodo);
        panelPedidos = new PanelPedidos(this::refrescarTodo);
        panelEntregas = new PanelEntregas(this::refrescarTodo);
        pestañas.addTab("Repartidores", panelRepartidores);
        pestañas.addTab("Pedidos", panelPedidos);
        pestañas.addTab("Entregas", panelEntregas);
        pestañas.addChangeListener(e -> refrescarSeleccionado());
        add(pestañas, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 460));
        setSize(1050, 540);
        setLocationRelativeTo(null);
        setVisible(true);
        refrescarSeleccionado();
    }

    private void refrescarSeleccionado() {
        switch (pestañas.getSelectedIndex()) {
            case 0 -> panelRepartidores.refrescar();
            case 1 -> panelPedidos.refrescar();
            case 2 -> panelEntregas.refrescar();
            default -> { }
        }
    }

    private void refrescarTodo() {
        panelRepartidores.refrescar();
        panelPedidos.refrescar();
        panelEntregas.refrescar();
    }
}
