package edu.thepower.accesoadatos.u1ficheros.entrega1;

import java.io.File;
import java.net.URI;
import java.util.Scanner;

/*Ejercicio 8: Explorador de carpetas inteligente
• Función explorarCarpeta(String ruta): lista contenido.
• Función analizarElemento(String ruta): muestra si es archivo (con tamaño) o carpeta (con número de elementos).
• Función convertirAURI(String ruta): convierte ruta a URI.
• Practica: list(), isFile(), isDirectory(), toURI().*/

public class Ejercicio8 {

    public static void explorarCarpeta(String ruta) {
        File carpeta = new File(ruta);

        if (carpeta.exists() && carpeta.isDirectory()) {
            String[] contenido = carpeta.list();
            System.out.println("\nContenido de la carpeta " + ruta + ":");
            if (contenido != null && contenido.length > 0) {
                for (int i = 0; i < contenido.length; i++) {
                    System.out.println("- " + contenido[i]);
                }
            } else {
                System.out.println("la carpeta esta vacia");
            }
        } else {
            System.out.println("la ruta no es una carpeta");
        }
    }

    public static void analizarElemento(String ruta) {
        File elemento = new File(ruta);

        if (!elemento.exists()) {
            System.out.println("la ruta no existe");
            return;
        }

        if (elemento.isFile()) {
            System.out.println("\nes un archivo");
            System.out.println("tamaño: " + elemento.length() + " bytes");
        } else if (elemento.isDirectory()) {
            String[] contenido = elemento.list();
            int cantidad = 0;
            if (contenido != null) {
                cantidad = contenido.length;
            }
            System.out.println("numero de elementos: " + cantidad);
        }
    }

    public static void convertirAURI(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            System.out.println("no se puede convertir porque la ruta no existe");
            return;
        }

        URI uri = archivo.toURI();
        System.out.println("\nconversion de ruta a URI:");
        System.out.println("ruta original: " + archivo.getAbsolutePath());
        System.out.println("ruta en URI: " + uri);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("introduce la ruta que quieras explorar: ");
        String ruta = sc.nextLine();

        File comprobacion = new File(ruta);
        if (!comprobacion.exists()) {
            System.out.println("la ruta no existe");
        } else {
            explorarCarpeta(ruta);
            analizarElemento(ruta);
            convertirAURI(ruta);
        }
        sc.close();
    }
}