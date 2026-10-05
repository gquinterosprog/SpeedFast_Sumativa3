package speedfast.modelo;

import speedfast.contrato.Cancelable;
import speedfast.contrato.Despachable;
import speedfast.contrato.Rastreable;

public class PedidoComida extends Pedido implements Despachable, Cancelable, Rastreable {
    private String restaurante;

    public PedidoComida(int numero, String cliente, String direccionEntrega,
                        double distanciaKm, String restaurante) {
        super(numero, cliente, direccionEntrega, distanciaKm);
        this.restaurante = restaurante;
    }

    @Override
    public void asignarRepartidor() {
        completarAsignacionAutomatica("Luis Díaz");
    }

    @Override
    public int calcularTiempoEntrega() {
        return 15 + (int) (getDistanciaKm() * 3);
    }

    @Override
    public String getTipoPedido() {
        return "PedidoComida";
    }

    @Override
    public String getDetalle() {
        return "Restaurante: " + restaurante;
    }
}
