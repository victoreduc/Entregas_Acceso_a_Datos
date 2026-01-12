package edu.thepower.accesoadatos.u1ficheros.entrega3;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Tercera%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OPCIONALES
Ejercicio Opcional 3: Analizador de Archivos Binarios con Reporte
Objetivo: Crear una herramienta que analice archivos binarios generados con DataOutputStream y genere un reporte detallado de su estructura y contenido.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// ÚNICA clase pública: debe llamarse como el archivo
public class EjercicioOpcional3 {

    /**
     * Analiza un archivo binario y genera reporte de su estructura
     */
    public static Reporte analizarArchivoBinario(String archivo) throws IOException {
        File f = new File(archivo);
        long size = f.length(); // tamaño en bytes [web:58]
        Reporte reporte = new Reporte(f.getName(), size);

        List<ElementoDato> elementos = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(f)))) {

            int posicion = 0;

            while (true) {
                try {
                    int posInicio = posicion;
                    int beforeAvailable = dis.available(); // bytes restantes antes de leer [web:73]

                    String descripcion = detectarTipoDato(dis); // avanza el stream

                    int afterAvailable = dis.available();
                    int bytesLeidos = beforeAvailable - afterAvailable;
                    int posFin = posInicio + bytesLeidos - 1;

                    String tipoSimple;
                    if (descripcion.startsWith("INT")) {
                        tipoSimple = "INT";
                        reporte.incrementarInts();
                    } else if (descripcion.startsWith("DOUBLE")) {
                        tipoSimple = "DOUBLE";
                        reporte.incrementarDoubles();
                    } else if (descripcion.startsWith("UTF")) {
                        tipoSimple = "UTF";
                        reporte.incrementarStrings();
                    } else if (descripcion.startsWith("BOOLEAN")) {
                        tipoSimple = "BOOLEAN";
                        reporte.incrementarBooleans();
                    } else {
                        tipoSimple = "DESCONOCIDO";
                    }

                    elementos.add(new ElementoDato(posInicio, posFin, tipoSimple, descripcion));
                    posicion = posFin + 1;

                } catch (EOFException eof) {
                    break; // fin de archivo
                }
            }
        }

        reporte.getElementos().addAll(elementos);
        return reporte;
    }

    /**
     * Intenta detectar el tipo de dato en la posición actual
     */
    private static String detectarTipoDato(DataInputStream dis) throws IOException {
        // Usamos mark/reset para probar tipos sin perder la posición [web:73][web:71]
        dis.mark(1024);

        try {
            int v = dis.readInt(); // 4 bytes
            return "INT: " + v;
        } catch (IOException e) {
            dis.reset();
        }

        dis.mark(1024);
        try {
            String s = dis.readUTF(); // formato UTF de DataOutputStream/DataInputStream [web:44][web:47]
            int numChars = s.length();
            return "UTF: \"" + s + "\" (" + numChars + " caracteres)";
        } catch (IOException e) {
            dis.reset();
        }

        dis.mark(1024);
        try {
            double d = dis.readDouble(); // 8 bytes [web:44][web:50]
            return "DOUBLE: " + d;
        } catch (IOException e) {
            dis.reset();
        }

        dis.mark(1024);
        try {
            boolean b = dis.readBoolean(); // 1 byte [web:45][web:54]
            return "BOOLEAN: " + b;
        } catch (IOException e) {
            dis.reset();
        }

        // Si nada encaja, leemos un byte «crudo»
        int raw = dis.read();
        if (raw == -1) {
            throw new EOFException();
        }
        return "BYTE: " + raw;
    }

    /**
     * Guarda el reporte en un archivo de texto
     */
    public static void guardarReporte(Reporte reporte, String archivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(archivo), "UTF-8"))) {

            pw.println("=== Reporte de Análisis de Archivo Binario ===");
            pw.println("Archivo: " + reporte.getNombreArchivo());
            pw.println("Tamaño: " + reporte.getTamañoBytes() + " bytes");
            pw.println("Estructura detectada:");
            for (ElementoDato e : reporte.getElementos()) {
                pw.printf("[Pos %d-%d] %s: %s%n",
                        e.getPosicionInicio(),
                        e.getPosicionFin(),
                        e.getTipo(),
                        e.getValor());
            }
            pw.println("Resumen:");
            pw.println("Enteros (int): " + reporte.getTotalInts());
            pw.println("Decimales (double): " + reporte.getTotalDoubles());
            pw.println("Cadenas (UTF): " + reporte.getTotalStrings());
            pw.println("Booleanos: " + reporte.getTotalBooleans());
            pw.println("Total elementos: " + reporte.getElementos().size());
        }
    }

    /**
     * Muestra el reporte por consola con formato
     */
    public static void mostrarReporte(Reporte reporte) {
        System.out.println("=== Reporte de Análisis de Archivo Binario ===");
        System.out.println("Archivo: " + reporte.getNombreArchivo());
        System.out.println("Tamaño: " + reporte.getTamañoBytes() + " bytes");
        System.out.println("Estructura detectada:");
        for (ElementoDato e : reporte.getElementos()) {
            System.out.printf("[Pos %d-%d] %s: %s%n",
                    e.getPosicionInicio(),
                    e.getPosicionFin(),
                    e.getTipo(),
                    e.getValor());
        }
        System.out.println("Resumen:");
        System.out.println("Enteros (int): " + reporte.getTotalInts());
        System.out.println("Decimales (double): " + reporte.getTotalDoubles());
        System.out.println("Cadenas (UTF): " + reporte.getTotalStrings());
        System.out.println("Booleanos: " + reporte.getTotalBooleans());
        System.out.println("Total elementos: " + reporte.getElementos().size());
    }

    // main de prueba opcional
    public static void main(String[] args) throws Exception {
        // Crear archivo binario de ejemplo
        try (DataOutputStream dos = new DataOutputStream(
                new FileOutputStream("datos.dat"))) {
            dos.writeInt(100);
            dos.writeUTF("Producto A");
            dos.writeDouble(99.99);
            dos.writeBoolean(true);
            dos.writeInt(200);
        }

        Reporte reporte = analizarArchivoBinario("datos.dat");
        mostrarReporte(reporte);
        guardarReporte(reporte, "reporte_datos.txt");
    }
}

// Clases adicionales (no públicas) en el mismo archivo

class Reporte {
    private String nombreArchivo;
    private long tamañoBytes;
    private List<ElementoDato> elementos = new ArrayList<>();
    private int totalInts;
    private int totalDoubles;
    private int totalStrings;
    private int totalBooleans;

    public Reporte(String nombreArchivo, long tamañoBytes) {
        this.nombreArchivo = nombreArchivo;
        this.tamañoBytes = tamañoBytes;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public long getTamañoBytes() {
        return tamañoBytes;
    }

    public List<ElementoDato> getElementos() {
        return elementos;
    }

    public int getTotalInts() {
        return totalInts;
    }

    public int getTotalDoubles() {
        return totalDoubles;
    }

    public int getTotalStrings() {
        return totalStrings;
    }

    public int getTotalBooleans() {
        return totalBooleans;
    }

    public void incrementarInts() {
        totalInts++;
    }

    public void incrementarDoubles() {
        totalDoubles++;
    }

    public void incrementarStrings() {
        totalStrings++;
    }

    public void incrementarBooleans() {
        totalBooleans++;
    }
}

class ElementoDato {
    private int posicionInicio;
    private int posicionFin;
    private String tipo;
    private String valor;

    public ElementoDato(int posicionInicio, int posicionFin,
                        String tipo, String valor) {
        this.posicionInicio = posicionInicio;
        this.posicionFin = posicionFin;
        this.tipo = tipo;
        this.valor = valor;
    }

    public int getPosicionInicio() {
        return posicionInicio;
    }

    public int getPosicionFin() {
        return posicionFin;
    }

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }
}
