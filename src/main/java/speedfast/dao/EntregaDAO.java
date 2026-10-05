package speedfast.dao;

import speedfast.modelo.RegistroEntrega;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {
    public int create(RegistroEntrega entrega) throws SQLException {
        String sql = "INSERT INTO entregas(id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            asignarValores(ps, entrega);
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                return claves.next() ? claves.getInt(1) : 0;
            }
        }
    }

    public List<RegistroEntrega> readAll() throws SQLException {
        List<RegistroEntrega> lista = new ArrayList<>();
        String sql = "SELECT id, id_pedido, id_repartidor, fecha, hora FROM entregas ORDER BY id";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new RegistroEntrega(rs.getInt("id"), rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"), rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()));
            }
        }
        return lista;
    }

    public boolean update(RegistroEntrega entrega) throws SQLException {
        String sql = "UPDATE entregas SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            asignarValores(ps, entrega);
            ps.setInt(5, entrega.getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM entregas WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private void asignarValores(PreparedStatement ps, RegistroEntrega entrega) throws SQLException {
        ps.setInt(1, entrega.getPedidoId());
        ps.setInt(2, entrega.getRepartidorId());
        ps.setDate(3, Date.valueOf(entrega.getFecha()));
        ps.setTime(4, Time.valueOf(entrega.getHora()));
    }
}
