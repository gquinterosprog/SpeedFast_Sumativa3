package speedfast.app;

import speedfast.dao.ConexionDB;
import speedfast.dao.InicializadorDB;
import speedfast.vista.VentanaPrincipal;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.Arrays;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (System.getenv("SPEEDFAST_DB_PASSWORD") == null) {
                JPasswordField campoClave = new JPasswordField(20);
                int respuesta = JOptionPane.showConfirmDialog(null, campoClave,
                        "Contraseña local de MySQL", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                if (respuesta != JOptionPane.OK_OPTION) return;
                char[] clave = campoClave.getPassword();
                ConexionDB.configurarClave(new String(clave));
                Arrays.fill(clave, '\0');
            }
            try {
                InicializadorDB.preparar();
                new VentanaPrincipal();
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo preparar speedfast_db. Revisa que MySQL esté iniciado, "
                                + "que la clave sea correcta y que el puerto sea 3306.\n"
                                + error.getMessage(),
                        "Error de conexión", JOptionPane.ERROR_MESSAGE);
                error.printStackTrace();
            }
        });
    }
}
