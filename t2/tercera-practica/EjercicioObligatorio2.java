package edu.thepower.accesoadatos.u1ficheros.entrega3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Tercera%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OBLIGATORIOS
Ejercicio 2: Sistema de Usuarios con JDBC
Objetivo: Implementar un CRUD completo de usuarios usando JDBC con PreparedStatement.
 */

public class EjercicioObligatorio2 {

    // Clase Usuario
    public static class Usuario {
        private int id;
        private String nombre;
        private String email;
        private int edad;

        public Usuario(int id, String nombre, String email, int edad) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.edad = edad;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getEmail() {
            return email;
        }

        public int getEdad() {
            return edad;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setEdad(int edad) {
            this.edad = edad;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Nombre: " + nombre + ", Email: " + email + ", Edad: " + edad;
        }
    }

    /**
     * Crea la tabla usuarios si no existe
     *
     * @param conn conexión a la base de datos
     * @throws SQLException si hay error al crear
     */
    public static void crearTabla(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nombre VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                "edad INT NOT NULL" +
                ")";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
            System.out.println("Tabla 'usuarios' creada");
        }
    }

    /**
     * Inserta un nuevo usuario en la base de datos
     *
     * @param conn   conexión activa
     * @param nombre nombre del usuario
     * @param email  email del usuario
     * @param edad   edad del usuario
     * @return ID generado del usuario insertado
     * @throws SQLException si hay error
     */
    public static int insertarUsuario(Connection conn, String nombre, String email, int edad) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, edad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setInt(3, edad);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        System.out.println("Usuario insertado con ID: " + id);
                        return id;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Busca usuarios por nombre (búsqueda parcial)
     *
     * @param conn   conexión activa
     * @param nombre fragmento de nombre a buscar
     * @return lista de usuarios encontrados
     * @throws SQLException si hay error
     */
    public static List<Usuario> buscarPorNombre(Connection conn, String nombre) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, email, edad FROM usuarios WHERE nombre LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getInt("edad"));
                    lista.add(u);
                }
            }
        }
        System.out.println("Usuarios encontrados: " + lista.size());
        return lista;
    }

    /**
     * Actualiza el email de un usuario
     *
     * @param conn       conexión activa
     * @param id         ID del usuario
     * @param nuevoEmail nuevo email
     * @return true si se actualizó, false si no existe
     * @throws SQLException si hay error
     */
    public static boolean actualizarEmail(Connection conn, int id, String nuevoEmail) throws SQLException {
        String sql = "UPDATE usuarios SET email = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEmail);
            ps.setInt(2, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Email actualizado para usuario ID: " + id);
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un usuario por ID
     *
     * @param conn conexión activa
     * @param id   ID del usuario a eliminar
     * @return true si se eliminó, false si no existía
     * @throws SQLException si hay error
     */
    public static boolean eliminarUsuario(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Usuario eliminado: ID " + id);
                return true;
            }
        }
        return false;
    }

    // Ejemplo de uso en main
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String usuarioDb = "root";
        String passwordDb = "password";
        try (Connection conn = DriverManager.getConnection(url, usuarioDb, passwordDb)) {
            crearTabla(conn);
            int id1 = insertarUsuario(conn, "Juan Pérez", "juan@email.com", 25);
            int id2 = insertarUsuario(conn, "María García", "maria@email.com", 30);
            List<Usuario> usuarios = buscarPorNombre(conn, "Juan");
            for (Usuario u : usuarios) {
                System.out.println(u);
            }
            actualizarEmail(conn, id1, "juan.nuevo@email.com");
            eliminarUsuario(conn, id2);
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
}
