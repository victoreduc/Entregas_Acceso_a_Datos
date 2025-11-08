package edu.thepower.accesoadatos.u1ficheros.entrega2;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Segunda%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OBLIGATORIOS
Ejercicio 1: Contador de Palabras y Estadísticas
Objetivo: Leer un archivo de texto y generar estadísticas completas sobre su contenido.
 */

import java.io.*;
import java.nio.charset.StandardCharsets;

class EstadisticasTexto {
    private int numeroLineas;
    private int numeroPalabras;
    private int numeroCaracteres;
    private String palabraMasLarga;

    public EstadisticasTexto(int lineas, int palabras, int caracteres, String masLarga) {
        this.numeroLineas = lineas;
        this.numeroPalabras = palabras;
        this.numeroCaracteres = caracteres;
        this.palabraMasLarga = masLarga;
    }
    public int getNumeroLineas() { return numeroLineas; }
    public int getNumeroPalabras() { return numeroPalabras; }
    public int getNumeroCaracteres() { return numeroCaracteres; }
    public String getPalabraMasLarga() { return palabraMasLarga; }
}

public class EjercicioObligatorio1 {

    /*
     * Lee un archivo y cuenta palabras, líneas y caracteres
     * @param nombreArchivo ruta del archivo a analizar
     * @return objeto EstadisticasTexto con los resultados
     * @throws IOException si hay error al leer el archivo
     */
    public static EstadisticasTexto analizarArchivo(String nombreArchivo) throws IOException {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            throw new FileNotFoundException("El archivo '" + nombreArchivo + "' no existe.");
        }

        int lineas = 0;
        int palabras = 0;
        int caracteres = 0;
        String palabraMasLarga = "";

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(nombreArchivo), StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas++;
                caracteres += linea.length() + 1; // +1 por el salto de línea (opcional)
                String[] partes = linea.trim().split("\\s+");
                for (String palabra : partes) {
                    if (!palabra.isEmpty()) {
                        palabras++;
                        if (palabra.length() > palabraMasLarga.length()) {
                            palabraMasLarga = palabra;
                        }
                    }
                }
            }
        }
        return new EstadisticasTexto(lineas, palabras, caracteres, palabraMasLarga);
    }

    /**
     * Escribe las estadísticas en un archivo de salida
     * @param estadisticas objeto con las estadísticas
     * @param archivoSalida ruta donde guardar el resultado
     * @throws IOException si hay error al escribir
     */
    public static void guardarEstadisticas(EstadisticasTexto estadisticas, String archivoSalida) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivoSalida), StandardCharsets.UTF_8))) {

            writer.write("Líneas: " + estadisticas.getNumeroLineas());
            writer.newLine();
            writer.write("Palabras: " + estadisticas.getNumeroPalabras());
            writer.newLine();
            writer.write("Caracteres: " + estadisticas.getNumeroCaracteres());
            writer.newLine();
            writer.write("Palabra más larga: " + estadisticas.getPalabraMasLarga());
            writer.newLine();
        }
    }

    public static void main(String[] args) {
        String archivoEntrada = "C:\\Users\\AlumnoAfternoon\\Documents\\GitHub\\2_DAM_Acceso_a_Datos\\proyecto-intellij\\acceso-a-datos\\resources\\archivo.txt";
        String archivoSalida = "C:\\Users\\AlumnoAfternoon\\Documents\\GitHub\\2_DAM_Acceso_a_Datos\\proyecto-intellij\\acceso-a-datos\\resources\\estadisticas.txt";
        try {
            EstadisticasTexto stats = analizarArchivo(archivoEntrada);
            // Imprimir en consola
            System.out.println("=== Estadísticas del archivo ===");
            System.out.println("Líneas: " + stats.getNumeroLineas());
            System.out.println("Palabras: " + stats.getNumeroPalabras());
            System.out.println("Caracteres: " + stats.getNumeroCaracteres());
            System.out.println("Palabra más larga: " + stats.getPalabraMasLarga() +
                    " (" + stats.getPalabraMasLarga().length() + " caracteres)");
            // Guardar en archivo
            guardarEstadisticas(stats, archivoSalida);
        } catch (IOException e) {
            System.err.println("Error al analizar el archivo: " + e.getMessage());
        }
    }
}