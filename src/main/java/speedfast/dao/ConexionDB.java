package speedfast.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Centraliza los datos de conexión sin guardar contraseñas en el repositorio. */
public final class ConexionDB {
    private ConexionDB() { }

    public static Connection obtenerConexion() throws SQLException {
        String url = valor("SPEEDFAST_DB_URL", "jdbc:mysql://localhost:3306/speedfast_db");
        String usuario = valor("SPEEDFAST_DB_USER", "root");
        String clave = System.getenv("SPEEDFAST_DB_PASSWORD");
        return DriverManager.getConnection(url, usuario, clave == null ? "" : clave);
    }

    private static String valor(String nombre, String predeterminado) {
        String dato = System.getenv(nombre);
        return dato == null || dato.isBlank() ? predeterminado : dato;
    }
}
