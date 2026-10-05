package speedfast.app;

import speedfast.modelo.EstadoPedido;
import speedfast.modelo.PedidoComida;
import speedfast.modelo.PedidoEncomienda;
import speedfast.modelo.PedidoExpress;
import speedfast.servicio.ControladorDeEnvios;
import speedfast.servicio.Repartidor;
import speedfast.servicio.ZonaDeCarga;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainSemana5 {
    public static void main(String[] args) {
        ControladorDeEnvios controlador = new ControladorDeEnvios();
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

        controlador.agregarPedido(new PedidoComida(
                101, "Carolina Muñoz", "Av. Los Alerces 245", 4.5, "Sabores del Lago"));
        controlador.agregarPedido(new PedidoEncomienda(
                102, "Tomás Herrera", "Av. Santa Rosa 567", 7.0, 12.5));
        controlador.agregarPedido(new PedidoExpress(
                103, "Fernanda Silva", "Pasaje Las Rosas 89", 3.2, "Mercado Central"));
        controlador.agregarPedido(new PedidoComida(
                104, "Ignacio Soto", "Los Carrera 1140", 5.0, "Cocina Urbana"));
        controlador.agregarPedido(new PedidoEncomienda(
                105, "Valentina Ríos", "Portugal 615", 6.5, 8.0));

        controlador.cargarZonaDeCarga(zonaDeCarga);

        System.out.println("SpeedFast - Coordinación de entregas");
        System.out.println("Pedidos cargados: " + controlador.cantidadPedidos());
        System.out.println("Repartidores disponibles: 3");
        System.out.println();

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(new Repartidor("Camila", zonaDeCarga));
        executor.execute(new Repartidor("Luis", zonaDeCarga));
        executor.execute(new Repartidor("Pedro", zonaDeCarga));
        executor.shutdown();

        try {
            boolean procesoFinalizado = executor.awaitTermination(30, TimeUnit.SECONDS);

            if (!procesoFinalizado) {
                executor.shutdownNow();
                System.out.println("El proceso no finalizó dentro del tiempo esperado.");
                return;
            }

            mostrarResumen(controlador, zonaDeCarga);
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            System.out.println("El proceso principal fue interrumpido.");
        }
    }

    private static void mostrarResumen(ControladorDeEnvios controlador,
                                       ZonaDeCarga zonaDeCarga) {
        int entregados = controlador.contarPedidosEnEstado(EstadoPedido.ENTREGADO);

        System.out.println();
        System.out.println("Resumen del proceso");
        System.out.println("Pedidos creados: " + controlador.cantidadPedidos());
        System.out.println("Pedidos entregados: " + entregados);
        System.out.println("Pedidos pendientes en zona de carga: "
                + zonaDeCarga.cantidadPedidosPendientes());

        if (entregados == controlador.cantidadPedidos()
                && zonaDeCarga.cantidadPedidosPendientes() == 0) {
            System.out.println("Todos los pedidos han sido entregados correctamente");
        } else {
            System.out.println("El proceso terminó con pedidos pendientes o inconsistencias.");
        }
    }
}
