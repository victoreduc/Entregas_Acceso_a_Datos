package com.example.seeders;

import com.example.models.*;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSeeder {
    public static void ejecutar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Proveedor p1 = new Proveedor("Tech Supply");
            Proveedor p2 = new Proveedor("Global Goods");
            session.persist(p1); session.persist(p2);

            Categoria cat = new Categoria("Componentes");
            session.persist(cat);

            for (int i = 1; i <= 10; i++) {
                Producto prod = new Producto("Producto " + i, 15.0 * i, (i % 2 == 0) ? p1 : p2);
                prod.getCategorias().add(cat);
                session.persist(prod);
            }

            tx.commit();
            System.out.println("[SEEDER] Datos insertados correctamente.");
        }
    }
}