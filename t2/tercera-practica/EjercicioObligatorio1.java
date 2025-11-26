package edu.thepower.accesoadatos.u1ficheros.entrega3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
Enunciados: https://github.com/PimPamTT/Acceso-a-Datos/blob/main/Tercera%20pr%C3%A1ctica%20de%20Acceso%20a%20Datos.pdf
EJERCICIOS OBLIGATORIOS
Ejercicio 1: Gestor de Inventario Binario
Objetivo: Crear un sistema que guarde y lea productos desde un archivo binario usando DataInputStream/DataOutputStream.
 */

public class EjercicioObligatorio1 {
    // Clase Producto
    public static class Producto {
        private int id;
        private String nombre;
        private double precio;
        private int stock;

        public Producto(int id, String nombre, double precio, int stock) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public double getPrecio() {
            return precio;
        }

        public int getStock() {
            return stock;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setPrecio(double precio) {
            this.precio = precio;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Nombre: " + nombre + ", Precio: " + precio + ", Stock: " + stock;
        }
    }

    /**
     * Escribe un producto en el archivo binario
     *
     * @param archivo  ruta del archivo donde guardar
     * @param producto objeto Producto a guardar
     * @throws IOException si hay error al escribir
     */
    public static void escribirProducto(String archivo, Producto producto) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivo))) {
            dos.writeInt(producto.getId());
            dos.writeUTF(producto.getNombre());
            dos.writeDouble(producto.getPrecio());
            dos.writeInt(producto.getStock());
        }
        System.out.println("Producto guardado: " + producto.getNombre());
    }

    /**
     * Añade un producto al final del archivo (modo append)
     *
     * @param archivo  ruta del archivo
     * @param producto producto a añadir
     * @throws IOException si hay error
     */
    public static void agregarProducto(String archivo, Producto producto) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivo, true))) {
            dos.writeInt(producto.getId());
            dos.writeUTF(producto.getNombre());
            dos.writeDouble(producto.getPrecio());
            dos.writeInt(producto.getStock());
        }
        System.out.println("Producto añadido: " + producto.getNombre());
    }

    /**
     * Lee todos los productos del archivo binario
     *
     * @param archivo ruta del archivo a leer
     * @return lista de productos leídos
     * @throws IOException si hay error al leer
     */
    public static List<Producto> leerProductos(String archivo) throws IOException {
        List<Producto> productos = new ArrayList<>();
        File f = new File(archivo);
        if (!f.exists()) {
            return productos;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(archivo))) {
            while (true) {
                try {
                    int id = dis.readInt();
                    String nombre = dis.readUTF();
                    double precio = dis.readDouble();
                    int stock = dis.readInt();
                    Producto p = new Producto(id, nombre, precio, stock);
                    productos.add(p);
                } catch (EOFException e) {
                    break;
                }
            }
        }
        return productos;
    }

    // Ejemplo de uso en main
    public static void main(String[] args) {
        try {
            Producto p1 = new Producto(1, "Ordenador", 999.99, 10);
            Producto p2 = new Producto(2, "Ratón", 19.99, 50);
            escribirProducto("inventario.dat", p1);
            agregarProducto("inventario.dat", p2);

            List<Producto> productos = leerProductos("inventario.dat");
            for (Producto p : productos) {
                System.out.println(p);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
