package edu.thepower.accesoadatos.u1ficheros.entrega1;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

// Ejercicio 9: Asistente personal de archivos.
public class Ejercicio9 {

    // Muestra las opciones disponibles en el menú principal
    public static void mostrarMenu() {
        System.out.println("===== MENÚ PRINCIPAL =====");
        System.out.println("1. Verificar archivo");
        System.out.println("2. Explorar carpeta");
        System.out.println("3. Crear carpeta");
        System.out.println("4. Crear archivo");
        System.out.println("5. Trabajar con URIs");
        System.out.println("6. Salir");
        System.out.print("Selecciona una opción: ");
    }

    // Verificar si un archivo existe
    public static void verificarArchivo(Scanner sc) {
        System.out.print("Introduce la ruta del archivo: ");
        String ruta = sc.nextLine();
        File archivo = new File(ruta);
        if (archivo.exists() && archivo.isFile()) {
            System.out.println("El archivo existe: " + archivo.getName());
        } else {
            System.out.println("El archivo NO existe o la ruta es incorrecta.");
        }
    }

    // Mostrar los archivos contenidos en un directorio
    public static void explorarDirectorio(Scanner sc) {
        System.out.print("Introduce la ruta del directorio: ");
        String ruta = sc.nextLine();
        File carpeta = new File(ruta);
        if (carpeta.exists() && carpeta.isDirectory()) {
            System.out.println("Contenido de la carpeta: " + carpeta.getAbsolutePath());
            String[] archivos = carpeta.list();
            if (archivos != null && archivos.length > 0) {
                for (String nombre : archivos) {
                    System.out.println(" - " + nombre);
                }
            } else {
                System.out.println("La carpeta está vacía.");
            }
        } else {
            System.out.println("El directorio no existe o no es válido.");
        }
    }

    // Crear una nueva carpeta
    public static void crearCarpeta(Scanner sc) {
        System.out.print("Introduce la ruta para la nueva carpeta: ");
        String ruta = sc.nextLine();
        File nuevaCarpeta = new File(ruta);
        if (nuevaCarpeta.exists()) {
            System.out.println("La carpeta ya existe.");
        } else {
            boolean creada = nuevaCarpeta.mkdirs();
            System.out.println(creada ? "Carpeta creada exitosamente." : "No se pudo crear la carpeta.");
        }
    }

    // Crear un archivo vacío
    public static void crearArchivo(Scanner sc) {
        System.out.print("Introduce la ruta para el nuevo archivo: ");
        String ruta = sc.nextLine();
        File nuevoArchivo = new File(ruta);
        if (nuevoArchivo.exists()) {
            System.out.println("El archivo ya existe.");
        } else {
            try {
                boolean creado = nuevoArchivo.createNewFile();
                System.out.println(creado ? "Archivo creado correctamente." : "No se pudo crear el archivo.");
            } catch (IOException e) {
                System.out.println("Error al crear el archivo: " + e.getMessage());
            }
        }
    }

    // Opción 5: Demostración con objetos URI
    public static void trabajarConURI(Scanner sc) {
        System.out.print("Introduce una URI válida (ejemplo: file:///C:/ruta/archivo.txt): ");
        String uriTexto = sc.nextLine();
        try {
            URI uri = new URI(uriTexto);
            File recurso = new File(uri);
            if (recurso.exists()) {
                System.out.println("El recurso de la URI ya existe: " + recurso.getAbsolutePath());
            } else {
                if (uriTexto.endsWith("/")) {
                    recurso.mkdirs();
                    System.out.println("Carpeta creada desde URI.");
                } else {
                    recurso.createNewFile();
                    System.out.println("Archivo creado desde URI.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: la URI es inválida (" + e.getMessage() + ").");
        }
    }

    // Metodo principal con el menú interactivo
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = 0;

        do {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1 -> verificarArchivo(sc);
                    case 2 -> explorarDirectorio(sc);
                    case 3 -> crearCarpeta(sc);
                    case 4 -> crearArchivo(sc);
                    case 5 -> trabajarConURI(sc);
                    case 6 -> System.out.println("Saliendo del asistente. ¡Hasta pronto!");
                    default -> System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Debe ingresar un número.");
            }
            System.out.println();
        } while (opcion != 6);

        sc.close();
    }
}
