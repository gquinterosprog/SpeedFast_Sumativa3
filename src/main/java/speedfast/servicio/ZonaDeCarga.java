package speedfast.servicio;

import speedfast.modelo.Pedido;

import java.util.ArrayList;
import java.util.List;

public class ZonaDeCarga {
    private final List<Pedido> pedidos;

    public ZonaDeCarga() {
        pedidos = new ArrayList<Pedido>();
    }

    public synchronized void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    // La comprobación y el retiro pertenecen a la misma sección crítica.
    public synchronized Pedido retirarPedido() {
        if (pedidos.isEmpty()) {
            return null;
        }

        return pedidos.remove(0);
    }

    public synchronized int cantidadPedidosPendientes() {
        return pedidos.size();
    }
}
