package edu.thepower.accesoadatos.u1ficheros.entrega2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Segunda%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OPCIONALES
Ejercicio Opcional 2: Carga de Variables de Entorno desde .env
Objetivo: Leer un archivo .env y cargar las variables en un Map que simule variables de entorno.
*/

public class EjercicioOpcional2 {
    private static Map<String, String> envVars = new HashMap<>();

    /**
     * Lee un archivo .env y carga las variables
     * @param archivoEnv ruta del archivo .env
     * @return Map con las variables cargadas
     * @throws IOException si hay error de lectura
     */
    public static Map<String, String> cargarEnv(String archivoEnv) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(archivoEnv));
        String linea;
        int contador = 0;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) {
                continue; // Ignora comentarios y líneas vacías
            }
            int idx = linea.indexOf('=');
            if (idx > 0) {
                String clave = linea.substring(0, idx).trim();
                String valor = linea.substring(idx + 1).trim();
                envVars.put(clave, valor);
                contador++;
            }
        }
        br.close();
        System.out.println("Cargadas " + contador + " variables desde " + archivoEnv);
        return envVars;
    }

    /**
     * Obtiene el valor de una variable de entorno
     * @param clave nombre de la variable
     * @param valorPorDefecto valor si la variable no existe
     * @return valor de la variable o valorPorDefecto
     */
    public static String getEnv(String clave, String valorPorDefecto) {
        if (envVars.containsKey(clave)) {
            return envVars.get(clave);
        }
        return valorPorDefecto;
    }

    // Ejemplo de uso en main
    public static void main(String[] args) {
        try {
            Map<String, String> env = cargarEnv(".env");
            System.out.println("Base de datos: " + env.get("DB_HOST") + ":" + env.get("DB_PORT"));
            String debug = getEnv("DEBUG", "false");
            System.out.println("Debug mode: " + debug);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
