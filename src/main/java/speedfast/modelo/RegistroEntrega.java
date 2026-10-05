package speedfast.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class RegistroEntrega {
    private final int id;
    private final int pedidoId;
    private final int repartidorId;
    private final LocalDate fecha;
    private final LocalTime hora;

    public RegistroEntrega(int id, int pedidoId, int repartidorId, LocalDate fecha, LocalTime hora) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.repartidorId = repartidorId;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() { return id; }
    public int getPedidoId() { return pedidoId; }
    public int getRepartidorId() { return repartidorId; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
}
