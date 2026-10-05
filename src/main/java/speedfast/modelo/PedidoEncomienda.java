package speedfast.modelo;

import speedfast.contrato.Cancelable;
import speedfast.contrato.Despachable;
import speedfast.contrato.Rastreable;

public class PedidoEncomienda extends Pedido implements Despachable, Cancelable, Rastreable {
    private double pesoKg;

    public PedidoEncomienda(int numero, String cliente, String direccionEntrega,
                            double distanciaKm, double pesoKg) {
        super(numero, cliente, direccionEntrega, distanciaKm);
        this.pesoKg = pesoKg;
    }

    @Override
    public void asignarRepartidor() {
        completarAsignacionAutomatica("Daniela Tapia");
    }

    @Override
    public int calcularTiempoEntrega() {
        int tiempo = 20 + (int) (getDistanciaKm() * 2);
        if (pesoKg > 10) {
            tiempo = tiempo + 5;
        }
        return tiempo;
    }

    @Override
    public String getTipoPedido() {
        return "PedidoEncomienda";
    }

    @Override
    public String getDetalle() {
        return "Peso: " + pesoKg + " kg";
    }
}
