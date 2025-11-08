package edu.thepower.accesoadatos.u1ficheros.entrega2;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Segunda%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OBLIGATORIOS
Ejercicio 3: Sistema de Log con Rotación
Objetivo: Implementar un sistema de logging que escriba en un archivo y lo rote cuando alcance cierto tamaño.
*/

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

enum NivelLog {
    INFO,
    WARNING,
    ERROR
}

class SistemaLog {
    private String archivoLog;
    private long tamanoMaximo;
    private int numeroRotacion = 1;

    public SistemaLog(String archivoLog, long tamanoMaximo) {
        this.archivoLog = archivoLog;
        this.tamanoMaximo = tamanoMaximo;
    }

    public void escribirLog(String mensaje, NivelLog nivel) throws IOException {
        /**
         * Verifica si el archivo debe rotarse y ejecuta la rotación
         * @return true si se realizó la rotación
         * @throws IOException si hay error en la rotación
         */
        rotarSiNecesario();
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        BufferedWriter writer = new BufferedWriter(new FileWriter(archivoLog, true));
        writer.write(fecha + " " + nivel + " " + mensaje);
        writer.newLine();
        writer.close();
        System.out.println("Log escrito: " + mensaje);
    }

    private boolean rotarSiNecesario() throws IOException {
        /*
         * Obtiene el tamaño actual del archivo de log
         * @return tamaño en bytes
         */
        File archivo = new File(archivoLog);
        if (archivo.exists() && archivo.length() >= tamanoMaximo) {
            File archivoRotado = new File(archivoLog + "." + numeroRotacion);
            archivo.renameTo(archivoRotado);
            numeroRotacion++;
            System.out.println("Rotación realizada: " + archivoRotado.getName());
            return true;
        }
        return false;
    }

    private long obtenerTamanoLog() {
        File archivo = new File(archivoLog);
        if (archivo.exists()) {
            return archivo.length();
        } else {
            return 0;
        }
    }
}

public class EjercicioObligatorio3 {
    public static void main(String[] args) throws IOException {
        SistemaLog log = new SistemaLog("app.log", 1024); // 1 KB
        log.escribirLog("Aplicación iniciada", NivelLog.INFO);
        log.escribirLog("Usuario conectado", NivelLog.INFO);
        log.escribirLog("Error de conexión", NivelLog.ERROR);
        // Puedes repetir logs para probar la rotación.
    }
}
