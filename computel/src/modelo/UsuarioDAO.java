
package modelo;
import conexion.ConexionBD;
import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario autenticar(String usuario, String password) {

        String sql = "SELECT id, usuario, password FROM usuarios "
                   + "WHERE usuario = ? AND password = ?";

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("usuario"),
                    rs.getString("password")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al autenticar usuario: " + e.getMessage());
        }

        return null;
    }
}