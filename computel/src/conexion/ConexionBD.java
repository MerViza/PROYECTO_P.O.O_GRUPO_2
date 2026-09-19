
package conexion;

/**
 *
 * @author jeremy
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/sistema_computel";
    private static final String USUARIO = "computel_app";
    private static final String PASSWORD = "Computel123!";

    public static Connection conectar() {
        try {
            Connection conexion = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    PASSWORD
            );

            System.out.println("Conexión exitosa a MySQL");
            return conexion;

        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
        
        
    }
    
    public static void main(String[] args) {
    conectar();
                }
}