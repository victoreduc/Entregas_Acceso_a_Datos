package edu.thepower.accesoadatos.u1ficheros.entrega2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Segunda%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OPCIONALES
Ejercicio Opcional 1: Parser de JSON Simple
Objetivo: Leer y escribir archivos JSON básicos sin usar librerías externas, o usando Gson si está disponible.
*/

public class EjercicioOpcional1 {

    /**
     * Lee un archivo JSON y extrae pares clave-valor simples
     * @param archivoJson ruta del archivo JSON
     * @return Map con las claves y valores parseados
     * @throws IOException si hay error de lectura
     */
    public static Map<String, String> leerJsonSimple(String archivoJson) throws IOException {
        Map<String, String> resultado = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(archivoJson));
        String linea;
        StringBuilder contenido = new StringBuilder();
        while ((linea = br.readLine()) != null) {
            contenido.append(linea.trim());
        }
        br.close();

        String json = contenido.toString();
        // Elimina llaves iniciales y finales
        json = json.replaceAll("[{}]", "");
        // Divide por comas los pares clave-valor
        String[] pares = json.split(",");
        for (String par : pares) {
            String[] kv = par.split(":");
            if (kv.length == 2) {
                // Quita comillas y espacios
                String clave = kv[0].replaceAll("\"", "").trim();
                String valor = kv[1].replaceAll("\"", "").trim();
                resultado.put(clave, valor);
            }
        }
        System.out.println("JSON leído: " + resultado.size() + " propiedades");
        return resultado;
    }

    /**
     * Escribe un Map como archivo JSON formateado
     * @param datos Map con los datos a escribir
     * @param archivoJson ruta del archivo de salida
     * @throws IOException si hay error de escritura
     */
    public static void escribirJsonSimple(Map<String, String> datos, String archivoJson) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(archivoJson));
        bw.write("{\n");
        int i = 0;
        for (Map.Entry<String, String> entry : datos.entrySet()) {
            bw.write("  \"" + entry.getKey() + "\": \"" + entry.getValue() + "\"");
            if (i < datos.size() - 1) {
                bw.write(",");
            }
            bw.write("\n");
            i++;
        }
        bw.write("}\n");
        bw.close();
        System.out.println("JSON escrito: " + datos.size() + " propiedades en " + archivoJson);
    }

    // Ejemplo de uso en main
    public static void main(String[] args) {
        try {
            Map<String, String> config = leerJsonSimple("config.json");
            System.out.println("Host: " + config.get("host"));
            config.put("version", "1.0");
            escribirJsonSimple(config, "config_nuevo.json");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}