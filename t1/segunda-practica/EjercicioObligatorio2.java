package edu.thepower.accesoadatos.u1ficheros.entrega2;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Segunda%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OBLIGATORIOS
Ejercicio 2: Merge de Archivos con Filtrado
Objetivo: Leer múltiples archivos de texto, filtrar líneas según un criterio y combinar el resultado en un único archivo de salida.
*/

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EjercicioObligatorio2 {

    /**
     * Combina múltiples archivos en uno solo, filtrando líneas
     * @param archivosEntrada array con las rutas de los archivos a combinar
     * @param archivoSalida ruta del archivo resultado
     * @param filtro palabra que debe contener la línea para incluirse (null = todas)
     * @return número total de líneas escritas
     * @throws IOException si hay error de lectura/escritura
     */
    public static int combinarArchivos(String[] archivosEntrada, String archivoSalida, String filtro) throws IOException {
        int totalLineasEscritas = 0;

        // Validar existencia de archivos de entrada
        for (String ruta : archivosEntrada) {
            File file = new File(ruta);
            if (!file.exists()) {
                System.err.println("El archivo de entrada '" + ruta + "' no existe.");
                throw new FileNotFoundException("Archivo no encontrado: " + ruta);
            }
        }

        // Escritura con try-with-resources
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(archivoSalida), StandardCharsets.UTF_8))) {

            for (String archivo : archivosEntrada) {
                int lineasCoinciden = 0;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(archivo), StandardCharsets.UTF_8))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        if (cumpleFiltro(linea, filtro)) {
                            bw.write(linea);
                            bw.newLine();
                            lineasCoinciden++;
                        }
                    }
                }
                System.out.println("Procesando " + archivo + ": " + lineasCoinciden + " líneas coinciden");
                totalLineasEscritas += lineasCoinciden;
            }
        }
        System.out.println("Total: " + totalLineasEscritas + " líneas escritas en " + archivoSalida);
        return totalLineasEscritas;
    }

    /**
     * Verifica si una línea cumple el criterio de filtrado
     * @param linea línea a evaluar
     * @param filtro criterio de búsqueda (null = siempre true)
     * @return true si la línea debe incluirse
     */
    private static boolean cumpleFiltro(String linea, String filtro) {
        return filtro == null || linea.contains(filtro);
    }

    public static void main(String[] args) {
        String[] archivos = {
                "C:\\Users\\AlumnoAfternoon\\Documents\\GitHub\\2_DAM_Acceso_a_Datos\\proyecto-intellij\\acceso-a-datos\\resources\\archivo1.txt",
                "C:\\Users\\AlumnoAfternoon\\Documents\\GitHub\\2_DAM_Acceso_a_Datos\\proyecto-intellij\\acceso-a-datos\\resources\\archivo2.txt"
        };
        String archivoSalida = "C:\\Users\\AlumnoAfternoon\\Documents\\GitHub\\2_DAM_Acceso_a_Datos\\proyecto-intellij\\acceso-a-datos\\resources\\combinado.txt";
        String filtro = "Java"; // null para incluir todas
        try {
            combinarArchivos(archivos, archivoSalida, filtro);
        } catch (IOException e) {
            System.err.println("Error durante la combinación: " + e.getMessage());
        }
    }
}
