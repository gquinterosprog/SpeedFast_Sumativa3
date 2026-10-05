package speedfast.vista;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.sql.SQLException;

final class Mensajes {
    private Mensajes() { }

    static void info(Component padre, String texto) {
        JOptionPane.showMessageDialog(padre, texto, "SpeedFast", JOptionPane.INFORMATION_MESSAGE);
    }

    static void error(Component padre, String texto) {
        JOptionPane.showMessageDialog(padre, texto, "SpeedFast", JOptionPane.WARNING_MESSAGE);
    }

    static void errorSql(Component padre, SQLException error) {
        String mensaje;
        if (error.getSQLState() != null && error.getSQLState().startsWith("23")) {
            mensaje = "La operación no se pudo completar porque el registro está relacionado con otros datos o infringe una restricción.";
        } else if (error.getMessage() != null && error.getMessage().contains("Communications link failure")) {
            mensaje = "No se pudo conectar con MySQL. Comprueba que el servidor esté iniciado.";
        } else {
            mensaje = "No se pudo completar la operación en la base de datos: " + error.getMessage();
        }
        JOptionPane.showMessageDialog(padre, mensaje, "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        error.printStackTrace();
    }
}
