package speedfast.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Crea el esquema de la actividad cuando se ejecuta por primera vez. */
public final class InicializadorDB {
    private InicializadorDB() { }

    public static void preparar() throws SQLException {
        try (Connection con = ConexionDB.obtenerConexionServidor();
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS speedfast_db");
        }
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS repartidores ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(100) NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS pedidos ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, direccion VARCHAR(100) NOT NULL, "
                    + "tipo ENUM('COMIDA','ENCOMIENDA','EXPRESS'), "
                    + "estado ENUM('PENDIENTE','EN_REPARTO','ENTREGADO'))");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS entregas ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, id_pedido INT, id_repartidor INT, "
                    + "fecha DATE, hora TIME, "
                    + "FOREIGN KEY (id_pedido) REFERENCES pedidos(id), "
                    + "FOREIGN KEY (id_repartidor) REFERENCES repartidores(id))");
        }
    }
}
