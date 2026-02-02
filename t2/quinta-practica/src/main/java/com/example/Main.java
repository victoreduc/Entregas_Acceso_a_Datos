package com.example;

import com.example.seeders.DatabaseSeeder;
import com.example.services.ProductoService;
import com.example.dao.ProductoDAO;
import com.example.util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        // 1. Poblamos
        DatabaseSeeder.ejecutar();

        // 2. Usamos DAOs y Servicios
        ProductoDAO dao = new ProductoDAO();
        ProductoService service = new ProductoService();

        System.out.println("\n--- REPORTE DE PRODUCTOS ---");
        System.out.println("Total en inventario: " + dao.contarProductos());

        dao.buscarPorNombre("Producto").forEach(p ->
                System.out.println("Item: " + p.getNombre() + " | Precio: " + p.getPrecio() + "€"));

        // 3. Cerramos
        HibernateUtil.shutdown();
    }
}