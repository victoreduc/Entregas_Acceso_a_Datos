package edu.thepower.accesoadatos.u1ficheros.entrega3;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Tercera%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OPCIONALES
Ejercicio Opcional 1: Exportador de Base de Datos a Archivo Binario
Objetivo: Leer datos de una tabla JDBC y exportarlos a un archivo binario usando DataOutputStream, luego poder importarlos de vuelta.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EjercicioOpcional1 {

    /**
     * Exporta todos los productos de la base de datos a archivo binario
     *
     * @param conn    conexión JDBC activa
     * @param archivo ruta del archivo destino
     * @return número de productos exportados
     * @throws SQLException si hay error de BD
     * @throws IOException  si hay error de archivo
     */
    public static int exportarProductos(Connection conn, String archivo)
            throws SQLException, IOException {

        String sql = "SELECT id, nombre, precio, stock FROM productos";
        int contador = 0;

        // try-with-resources para cerrar todo automáticamente
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();
             DataOutputStream dos = new DataOutputStream(
                     new FileOutputStream(archivo))) {

            System.out.println("Exportando productos...");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");

                // Escribir en el archivo binario siempre en el mismo orden
                dos.writeInt(id);
                dos.writeUTF(nombre);
                dos.writeDouble(precio);
                dos.writeInt(stock);

                System.out.println("Producto exportado: ID=" + id +
                        ", Nombre=" + nombre);
                contador++;
            }
        }

        return contador;
    }

    /**
     * Importa productos desde archivo binario a la base de datos
     *
     * @param conn    conexión JDBC activa
     * @param archivo ruta del archivo fuente
     * @return número de productos importados
     * @throws SQLException si hay error de BD
     * @throws IOException  si hay error de archivo
     */
    public static int importarProductos(Connection conn, String archivo)
            throws SQLException, IOException {

        String sqlInsert = "INSERT INTO productos(id, nombre, precio, stock) " +
                "VALUES (?, ?, ?, ?)";
        int contador = 0;

        // Desactivar auto-commit para hacer todo en una transacción
        boolean autoCommitOriginal = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try (DataInputStream dis = new DataInputStream(
                new FileInputStream(archivo));
             PreparedStatement ps = conn.prepareStatement(sqlInsert)) {

            System.out.println("Importando productos...");

            while (true) {
                try {
                    // Leer en el mismo orden en que se escribió
                    int id = dis.readInt();
                    String nombre = dis.readUTF();
                    double precio = dis.readDouble();
                    int stock = dis.readInt();

                    ps.setInt(1, id);
                    ps.setString(2, nombre);
                    ps.setDouble(3, precio);
                    ps.setInt(4, stock);
                    ps.executeUpdate();

                    System.out.println("Producto importado: ID=" + id +
                            ", Nombre=" + nombre);
                    contador++;

                } catch (IOException eof) {
                    // Cuando se llega al final del archivo salta una IOExceptio (End Of File). Salimos del bucle.
                    break;
                }
            }

            conn.commit();
        } catch (SQLException | IOException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(autoCommitOriginal);
        }

        return contador;
    }

    // Método main de prueba
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/inventario";
        String usuario = "root";
        String password = "admin";

        try (Connection conn = java.sql.DriverManager
                .getConnection(url, usuario, password)) {

            int exportados = exportarProductos(conn, "backup_productos.dat");
            System.out.println("Productos exportados: " + exportados);

            int importados = importarProductos(conn, "backup_productos.dat");
            System.out.println("Productos importados: " + importados);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
