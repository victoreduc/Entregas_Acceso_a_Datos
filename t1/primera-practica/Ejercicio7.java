package edu.thepower.accesoadatos.u1ficheros.entrega1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/*Organizador de biblioteca
• Función organizarBiblioteca(): crea carpeta de categoría y archivo catalogo.txt.
• Función verificarLibro(): verifica si existe un libro; si no, pregunta si se crea.
• Practica: exists(), mkdir(), createNewFile(), funciones separadas.*/

public class Ejercicio7 {
    private static Scanner sc = new Scanner(System.in);
    private static String categoria = "";
    private static String nombre = "catalogo.txt";

    public static void main (String[] args){
        organizarBiblioteca();
        verificarLibro();
    }

    public static void organizarBiblioteca(){
        System.out.println("> Bienvenido al organizador de bibliotecas");
        System.out.print("Ingrese el nombre de la categoria: ");
        categoria = sc.nextLine();

        String directorio = "C:\\Users\\AlumnoAfternoon\\Documents\\2_DAM_Acceso_a_Datos\\PruebasJavaAccesoDatos\\Biblioteca\\" + categoria;
        File directorioPadre = new File(directorio);

        String archivo = directorio + "\\" + nombre;
        File archivoHijo = new File(archivo);

        boolean fin = false;

        do {
            if (!directorioPadre.exists()) {
                directorioPadre.mkdir();
                System.out.println("Categoria " + categoria + " creada exitosamente");
                System.out.println("Creado en: " + directorioPadre.getAbsolutePath());

                if (!archivoHijo.exists()) {
                    System.out.println("El archivo no existe.");
                    try {
                        archivoHijo.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("El archivo creado por defecto.");
                }
            } else {
                System.out.println("El directorio y el archivo existen.");
                fin = true;
            }
        } while (!fin);
    }

    public static void verificarLibro(){
        System.out.println("Introduzca la categoria del libro: ");
        categoria = sc.nextLine();
        System.out.println("Introduzca el nombre del libro: ");
        nombre = sc.nextLine();

        String directorio = "C:\\Users\\AlumnoAfternoon\\Documents\\2_DAM_Acceso_a_Datos\\PruebasJavaAccesoDatos\\Biblioteca\\" + categoria;
        File directorioPadre = new File(directorio);

        String archivo = directorio + "\\" + nombre;
        File archivoHijo = new File(archivo);

        if (directorioPadre.exists() && archivoHijo.exists()) {
            System.out.println("El libro existe en: " + archivoHijo.getAbsolutePath());
        } else {
            System.out.println("El libro no existe en: " + directorioPadre.getAbsolutePath());
        }
        sc.close();
    }
}
