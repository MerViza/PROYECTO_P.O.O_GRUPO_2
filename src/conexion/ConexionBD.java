
package conexion;

/**
 *
 * @author jeremy
 */

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    private static final String URL = 
            "jdbc:mysql://localhost:3306/sistema_computel";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
        return conexion;
    }
}