package com.example.dao;

import com.example.models.Producto;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class ProductoDAO {
    
    public List<Producto> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Producto", Producto.class).list();
        }
    }

    public List<Producto> buscarPorNombre(String filtro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Producto p WHERE p.nombre LIKE :n ORDER BY p.precio DESC", Producto.class)
                    .setParameter("n", "%" + filtro + "%")
                    .list();
        }
    }

    public Long contarProductos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(p) FROM Producto p", Long.class).uniqueResult();
        }
    }
}