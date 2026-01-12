package edu.thepower.accesoadatos.u1ficheros.entrega3;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Tercera%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OPCIONALES
Ejercicio Opcional 2: Migrador de Configuración Properties a Base de Datos
Objetivo: Leer un archivo Properties y migrar todas las configuraciones a una tabla de base de datos, con posibilidad de sincronización bidireccional.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Map;
import java.util.Properties;

public class EjercicioOpcional2 {

    // Crea la tabla configuracion si no existe
    private static void crearTablaConfiguracion(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS configuracion (" +
                "clave VARCHAR(255) PRIMARY KEY," +
                "valor VARCHAR(255))";
        try (Statement stmt = conn.createStatement()) {
            System.out.println("Creando tabla 'configuracion'...");
            stmt.executeUpdate(sql);
        }
    }

    /**
     * Migra todas las propiedades de archivo a base de datos
     */
    public static int migrarPropertiesABD(String archivo, Connection conn)
            throws IOException, SQLException {

        crearTablaConfiguracion(conn);

        Properties props = new Properties();
        // Leer el properties (UTF‑8 para evitar problemas de acentos)
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(archivo), StandardCharsets.UTF_8)) {
            props.load(reader); // clave=valor por línea [web:21][web:25]
        }

        System.out.println("Migrando propiedades a BD...");

        String sqlInsert = "INSERT INTO configuracion (clave, valor) " +
                "VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE valor = VALUES(valor)";
        int contador = 0;

        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                String clave = entry.getKey().toString();
                String valor = entry.getValue().toString();

                ps.setString(1, clave);
                ps.setString(2, valor);
                ps.executeUpdate();
                System.out.println(clave + " = " + valor);
                contador++;
            }
        }

        return contador;
    }

    /**
     * Exporta configuración de base de datos a archivo Properties
     */
    public static int exportarBDaProperties(Connection conn, String archivo)
            throws SQLException, IOException {

        crearTablaConfiguracion(conn);

        Properties props = new Properties();

        String sqlSelect = "SELECT clave, valor FROM configuracion";
        int contador = 0;

        System.out.println("Exportando configuración de BD a archivo...");

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlSelect)) {

            while (rs.next()) {
                String clave = rs.getString("clave");
                String valor = rs.getString("valor");
                props.setProperty(clave, valor); // guarda en objeto Properties [web:23][web:37]
                contador++;
            }
        }

        // Guardar en archivo .properties
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(archivo), StandardCharsets.UTF_8)) {
            props.store(writer, "Exportado desde BD"); // escribe en formato .properties
        }

        return contador;
    }

    /**
     * Sincroniza: actualiza BD con valores de Properties que hayan cambiado
     * (solo UPDATE, no inserta nuevos ni borra)
     */
    public static int sincronizarPropiedades(String archivo, Connection conn)
            throws IOException, SQLException {

        crearTablaConfiguracion(conn);

        Properties props = new Properties();
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(archivo), StandardCharsets.UTF_8)) {
            props.load(reader);
        }

        String sqlUpdate = "UPDATE configuracion SET valor = ? WHERE clave = ? AND valor <> ?";
        String sqlSelectValor = "SELECT valor FROM configuracion WHERE clave = ?";

        int actualizadas = 0;

        try (PreparedStatement psSelect = conn.prepareStatement(sqlSelectValor);
             PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {

            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                String clave = entry.getKey().toString();
                String valorArchivo = entry.getValue().toString();

                // Obtener valor actual en BD
                psSelect.setString(1, clave);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        String valorBD = rs.getString(1);

                        // Solo actualizar si es distinto
                        if (!valorArchivo.equals(valorBD)) {
                            psUpdate.setString(1, valorArchivo);
                            psUpdate.setString(2, clave);
                            psUpdate.setString(3, valorArchivo); // condición valor <> ?
                            int filas = psUpdate.executeUpdate();
                            if (filas > 0) {
                                actualizadas += filas;
                                System.out.println("Actualizada: " + clave +
                                        " de '" + valorBD + "' a '" + valorArchivo + "'");
                            }
                        }
                    }
                }
            }
        }
        return actualizadas;
    }

    // main de prueba opcional
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/config_db";
        String usuario = "root";
        String password = "admin";

        try (Connection conn = DriverManager.getConnection(url, usuario, password)) {
            int migradas = migrarPropertiesABD("config.properties", conn);
            System.out.println("Propiedades migradas a BD: " + migradas);

            int exportadas = exportarBDaProperties(conn, "config_exportado.properties");
            System.out.println("Propiedades exportadas a archivo: " + exportadas);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
