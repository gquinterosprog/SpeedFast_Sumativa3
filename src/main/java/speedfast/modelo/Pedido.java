package speedfast.modelo;

import java.util.ArrayList;

// Clase abstracta que reúne los atributos y métodos comunes de los pedidos.
public abstract class Pedido {
    private int id;
    private String cliente;
    private String direccionEntrega;
    private double distanciaKm;
    private String repartidorAsignado;
    private EstadoPedido estado;
    private final ArrayList<String> historial;

    public Pedido(int id, String cliente, String direccionEntrega, double distanciaKm) {
        this.id = id;
        this.cliente = cliente;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
        this.repartidorAsignado = "Sin asignar";
        this.estado = EstadoPedido.PENDIENTE;
        this.historial = new ArrayList<String>();
        agregarEvento("Pedido creado.");
    }

    // Este método será sobrescrito por cada tipo de pedido.
    public abstract void asignarRepartidor();

    // Sobrecarga del método para realizar una asignación manual.
    public void asignarRepartidor(String nombre) {
        repartidorAsignado = nombre;
        estado = EstadoPedido.ASIGNADO;
        agregarEvento("Repartidor asignado manualmente: " + nombre + ".");
    }

    public abstract int calcularTiempoEntrega();

    public abstract String getTipoPedido();

    public abstract String getDetalle();

    public void reservar() {
        estado = EstadoPedido.RESERVADO;
        agregarEvento("Pedido reservado correctamente.");
    }

    // Método implementado que reutilizan todas las clases derivadas.
    public void mostrarResumen() {
        System.out.println();
        System.out.println("[" + getTipoPedido() + "]");
        System.out.println("Pedido: #" + id);
        System.out.println("Cliente: " + cliente);
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + distanciaKm + " km");
        System.out.println("Detalle: " + getDetalle());
        System.out.println("Repartidor: " + repartidorAsignado);
        System.out.println("Tiempo estimado: " + calcularTiempoEntrega() + " minutos");
        System.out.println("Estado: " + estado);
    }

    public void despachar() {
        if (estado == EstadoPedido.ASIGNADO) {
            estado = EstadoPedido.DESPACHADO;
            agregarEvento("Pedido despachado con " + repartidorAsignado + ".");
            System.out.println(getTipoPedido() + " #" + id + " despachado correctamente.");
        } else {
            System.out.println("El pedido #" + id + " no puede ser despachado.");
        }
    }

    public void cancelar() {
        if (estado != EstadoPedido.ENTREGADO) {
            estado = EstadoPedido.CANCELADO;
            agregarEvento("Pedido cancelado exitosamente.");
            System.out.println(getTipoPedido() + " #" + id + " cancelado exitosamente.");
        } else {
            System.out.println("El pedido #" + id + " ya fue entregado y no puede cancelarse.");
        }
    }

    public void marcarEntregado() {
        if (estado == EstadoPedido.DESPACHADO || estado == EstadoPedido.EN_REPARTO) {
            estado = EstadoPedido.ENTREGADO;
            agregarEvento("Pedido entregado por " + repartidorAsignado + ".");
        }
    }

    public void verHistorial() {
        System.out.println();
        System.out.println("Historial de " + getTipoPedido() + " #" + id + ":");
        for (int i = 0; i < historial.size(); i++) {
            System.out.println((i + 1) + ". " + historial.get(i));
        }
    }

    protected void completarAsignacionAutomatica(String nombre) {
        repartidorAsignado = nombre;
        estado = EstadoPedido.ASIGNADO;
        agregarEvento("Repartidor asignado automáticamente: " + nombre + ".");
    }

    private void agregarEvento(String evento) {
        historial.add(evento);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public String getRepartidorAsignado() {
        return repartidorAsignado;
    }

    public void setRepartidorAsignado(String repartidorAsignado) {
        this.repartidorAsignado = repartidorAsignado;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void setEstado(String nuevoEstado) {
        this.estado = EstadoPedido.valueOf(nuevoEstado.trim().toUpperCase());
    }

    @Override
    public String toString() {
        return getTipoPedido() + "{" +
                "id=" + id +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", estado=" + estado +
                '}';
    }
}
