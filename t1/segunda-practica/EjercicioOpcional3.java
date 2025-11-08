package edu.thepower.accesoadatos.u1ficheros.entrega2;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Segunda%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OPCIONALES
Ejercicio Opcional 3: Backup Incremental
Objetivo: Crear un sistema que haga backup solo de archivos modificados desde el último backup.
*/

public class EjercicioOpcional3 {
    /**
     * Realiza backup incremental de una carpeta
     * @param carpetaOrigen ruta de la carpeta a respaldar
     * @param carpetaDestino ruta donde guardar el backup
     * @param archivoControl archivo que registra el último backup
     * @return número de archivos copiados
     * @throws IOException si hay error en el proceso
     */
    public static int backupIncremental(String carpetaOrigen, String carpetaDestino, String archivoControl) throws IOException {
        System.out.println("Iniciando backup incremental...");
        long ultimoBackup = leerUltimoBackup(archivoControl);
        if (ultimoBackup == 0) {
            System.out.println("Último backup: nunca");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("Último backup: " + sdf.format(new Date(ultimoBackup)));
        }
        File fOrigen = new File(carpetaOrigen);
        File[] archivos = fOrigen.listFiles();
        int copiaCount = 0;
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    long modificado = archivo.lastModified();
                    if (ultimoBackup == 0 || modificado > ultimoBackup) {
                        String tipo = (ultimoBackup == 0 ? "Copiando" : "Copiando: " + archivo.getName() + " (modificado)");
                        if (ultimoBackup == 0) {
                            System.out.println("Copiando: " + archivo.getName());
                        } else {
                            System.out.println("Copiando: " + archivo.getName() + " (modificado)");
                        }
                        File destino = new File(carpetaDestino + File.separator + archivo.getName());
                        copiarArchivo(archivo, destino);
                        copiaCount++;
                    }
                }
            }
        }
        System.out.println("Backup completado: " + copiaCount + " archivos");
        // Actualiza registro con timestamp actual
        long ahora = System.currentTimeMillis();
        BufferedWriter bw = new BufferedWriter(new FileWriter(archivoControl));
        bw.write(Long.toString(ahora));
        bw.close();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Registro actualizado: " + sdf.format(new Date(ahora)));
        return copiaCount;
    }

    /**
     * Lee la fecha del último backup desde el archivo de control
     * @param archivoControl ruta del archivo de control
     * @return timestamp del último backup, o 0 si no existe
     * @throws IOException si hay error de lectura
     */
    private static long leerUltimoBackup(String archivoControl) throws IOException {
        File f = new File(archivoControl);
        if (!f.exists()) {
            return 0;
        }
        BufferedReader br = new BufferedReader(new FileReader(f));
        String linea = br.readLine();
        br.close();
        if (linea != null && !linea.isEmpty()) {
            try {
                return Long.parseLong(linea.trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * Copia un archivo de origen a destino
     * @param origen archivo fuente
     * @param destino archivo destino
     * @throws IOException si hay error en la copia
     */
    private static void copiarArchivo(File origen, File destino) throws IOException {
        InputStream in = new FileInputStream(origen);
        OutputStream out = new FileOutputStream(destino);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.close();
    }

    // Ejemplo de uso en main
    public static void main(String[] args) {
        try {
            int archivosCopiados = backupIncremental(
                    "./documentos", "./backup", "./backup/.lastbackup");
            System.out.println("Backup completado: " + archivosCopiados + " archivos");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
