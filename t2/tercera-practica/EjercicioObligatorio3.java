package edu.thepower.accesoadatos.u1ficheros.entrega3;

import java.io.*;
import java.util.Properties;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Tercera%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OBLIGATORIOS
Ejercicio 3: Configurador de Aplicación con Properties
Objetivo: Crear un sistema de configuración que lea, modifique y guarde parámetros usando Properties, con validación y valores por defecto.
 */

public class EjercicioObligatorio3 {

    /**
     * Carga la configuración desde archivo o crea una por defecto
     *
     * @param archivo ruta del archivo de configuración
     * @return objeto Properties cargado
     * @throws IOException si hay error de lectura
     */
    public static Properties cargarConfiguracion(String archivo) throws IOException {
        Properties props = new Properties();
        File f = new File(archivo);
        if (f.exists()) {
            try (FileInputStream fis = new FileInputStream(f)) {
                props.load(fis);
                System.out.println("Configuración cargada: " + archivo);
            }
        } else {
            // Propiedades por defecto (DB, app, UI)
            props.setProperty("db.host", "localhost");
            props.setProperty("db.port", "3306");
            props.setProperty("db.name", "mi_base_datos");
            props.setProperty("db.user", "admin");
            props.setProperty("db.password", "");
            props.setProperty("app.titulo", "Mi Aplicación");
            props.setProperty("app.version", "1.0.0");
            props.setProperty("app.debug", "false");
            props.setProperty("app.idioma", "en");
            props.setProperty("ui.tema", "claro");
            props.setProperty("ui.tamano_fuente", "12");
            guardarConfiguracion(props, archivo, "Configuración por defecto");
            System.out.println("Archivo de configuración creado con valores por defecto: " + archivo);
        }
        return props;
    }

    /**
     * Obtiene una propiedad como String con valor por defecto
     *
     * @param props        objeto Properties
     * @param clave        clave de la propiedad
     * @param valorDefecto valor si no existe
     * @return valor de la propiedad o valorDefecto
     */
    public static String getString(Properties props, String clave, String valorDefecto) {
        String valor = props.getProperty(clave);
        if (valor == null) return valorDefecto;
        return valor;
    }

    /**
     * Obtiene una propiedad como int con validación
     *
     * @param props        objeto Properties
     * @param clave        clave de la propiedad
     * @param valorDefecto valor si no existe o es inválido
     * @return valor int de la propiedad
     */
    public static int getInt(Properties props, String clave, int valorDefecto) {
        String valor = props.getProperty(clave);
        if (valor == null) return valorDefecto;
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return valorDefecto;
        }
    }

    /**
     * Obtiene una propiedad como boolean
     *
     * @param props        objeto Properties
     * @param clave        clave de la propiedad
     * @param valorDefecto valor si no existe
     * @return valor boolean de la propiedad
     */
    public static boolean getBoolean(Properties props, String clave, boolean valorDefecto) {
        String valor = props.getProperty(clave);
        if (valor == null) return valorDefecto;
        return valor.equalsIgnoreCase("true");
    }

    /**
     * Guarda la configuración en archivo
     *
     * @param props      objeto Properties a guardar
     * @param archivo    ruta del archivo destino
     * @param comentario comentario para el archivo
     * @throws IOException si hay error de escritura
     */
    public static void guardarConfiguracion(Properties props, String archivo, String comentario) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            props.store(fos, comentario);
            System.out.println("Configuración guardada: " + archivo);
        }
    }

    /**
     * Muestra todas las propiedades por consola
     *
     * @param props objeto Properties
     */
    public static void mostrarConfiguracion(Properties props) {
        for (String k : props.stringPropertyNames()) {
            System.out.println(k + " = " + props.getProperty(k));
        }
    }

    // Ejemplo de uso en main
    public static void main(String[] args) {
        try {
            Properties config = cargarConfiguracion("app.properties");
            // Leer configuración
            String dbHost = getString(config, "db.host", "localhost");
            int dbPort = getInt(config, "db.port", 3306);
            boolean debug = getBoolean(config, "app.debug", false);

            System.out.println("=== Configuración Actual ===");
            mostrarConfiguracion(config);

            // Modificar configuración
            config.setProperty("app.idioma", "es");
            config.setProperty("ui.tema", "oscuro");
            config.setProperty("db.port", "3307");
            guardarConfiguracion(config, "app.properties", "Configuración de Mi Aplicación");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
