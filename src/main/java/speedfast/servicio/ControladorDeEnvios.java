package speedfast.servicio;

import speedfast.modelo.Pedido;
import speedfast.modelo.EstadoPedido;

import java.util.ArrayList;

// Gestiona la colección de pedidos sin conocer las reglas de cada subclase.
public class ControladorDeEnvios {
    private ArrayList<Pedido> pedidos;

    public ControladorDeEnvios() {
        pedidos = new ArrayList<Pedido>();
    }

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public void reservarPedidos() {
        for (Pedido pedido : pedidos) {
            pedido.reservar();
        }
    }

    // La referencia Pedido ejecuta el método de la subclase correspondiente.
    public void asignarRepartidores() {
        for (Pedido pedido : pedidos) {
            pedido.asignarRepartidor();
        }
    }

    public void mostrarPedidos() {
        for (Pedido pedido : pedidos) {
            pedido.mostrarResumen();
        }
    }

    public void mostrarHistorialGeneral() {
        System.out.println();
        System.out.println("========== HISTORIAL GENERAL ==========");

        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
                System.out.println("- " + pedido.getTipoPedido() + " #" + pedido.getId()
                        + " entregado por " + pedido.getRepartidorAsignado());
            } else if (pedido.getEstado() == EstadoPedido.CANCELADO) {
                System.out.println("- " + pedido.getTipoPedido() + " #" + pedido.getId()
                        + " cancelado");
            } else {
                System.out.println("- " + pedido.getTipoPedido() + " #" + pedido.getId()
                        + " en estado " + pedido.getEstado());
            }
        }
    }

    public void cargarZonaDeCarga(ZonaDeCarga zonaDeCarga) {
        for (Pedido pedido : pedidos) {
            zonaDeCarga.agregarPedido(pedido);
        }
    }

    public int contarPedidosEnEstado(EstadoPedido estado) {
        int cantidad = 0;

        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == estado) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public int cantidadPedidos() {
        return pedidos.size();
    }
}
