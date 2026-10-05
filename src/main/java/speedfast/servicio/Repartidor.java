package speedfast.servicio;

import speedfast.modelo.EstadoPedido;
import speedfast.modelo.Pedido;

public class Repartidor implements Runnable {
    private final String nombre;
    private final ZonaDeCarga zonaDeCarga;
    private int cantidadEntregados;

    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
        this.cantidadEntregados = 0;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Pedido pedido = zonaDeCarga.retirarPedido();

            if (pedido == null) {
                break;
            }

            pedido.setRepartidorAsignado(nombre);
            pedido.setEstado(EstadoPedido.EN_REPARTO);
            mostrarEstado(pedido, "retira");

            try {
                // La entrega se simula fuera de la sección crítica de ZonaDeCarga.
                Thread.sleep(pedido.calcularTiempoEntrega() * 20L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[" + Thread.currentThread().getName() + " | "
                        + nombre + "] entrega interrumpida");
                return;
            }

            pedido.marcarEntregado();
            cantidadEntregados++;
            mostrarEstado(pedido, "entrega");
        }

        System.out.println("[" + Thread.currentThread().getName() + " | " + nombre
                + "] finaliza con " + cantidadEntregados + " pedido(s) entregado(s)");
    }

    private void mostrarEstado(Pedido pedido, String accion) {
        System.out.println("[" + Thread.currentThread().getName() + " | " + nombre + "] "
                + accion + " " + pedido.getTipoPedido()
                + " #" + pedido.getId()
                + " - " + pedido.getDireccionEntrega()
                + " - Estado: " + pedido.getEstado());
    }
}
