package speedfast.modelo;

import speedfast.contrato.Cancelable;
import speedfast.contrato.Despachable;
import speedfast.contrato.Rastreable;

public class PedidoExpress extends Pedido implements Despachable, Cancelable, Rastreable {
    private String tienda;

    public PedidoExpress(int numero, String cliente, String direccionEntrega,
                         double distanciaKm, String tienda) {
        super(numero, cliente, direccionEntrega, distanciaKm);
        this.tienda = tienda;
    }

    @Override
    public void asignarRepartidor() {
        completarAsignacionAutomatica("Camila Rojas");
    }

    @Override
    public int calcularTiempoEntrega() {
        return 10 + (int) (getDistanciaKm() * 2);
    }

    @Override
    public String getTipoPedido() {
        return "PedidoExpress";
    }

    @Override
    public String getDetalle() {
        return "Tienda: " + tienda;
    }
}
