package services;

/*
EJERCICIOS OBLIGATORIOS
Ejercicio 1: API de Gestión de Tareas
Objetivo: Crear una API REST completa para gestionar tareas (To-Do List) con
Javalin, implementando todas las operaciones CRUD y validaciones básicas.
*/

import entrega4.gestortareas.Tarea;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TareaService {
    private static final Map<Integer, Tarea> tareasDB = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    public List<Tarea> getAll() {
        return new ArrayList<>(tareasDB.values());
    }

    public Tarea getById(int id) {
        return tareasDB.get(id);
    }

    public Tarea create(String titulo, String descripcion) {
        int id = idGenerator.getAndIncrement();
        Tarea nuevaTarea = new Tarea(id, titulo, descripcion);
        tareasDB.put(id, nuevaTarea);
        return nuevaTarea;
    }

    public Tarea update(int id, Tarea datosNuevos) {
        Tarea tarea = tareasDB.get(id);
        if (tarea != null) {
            tarea.setTitulo(datosNuevos.getTitulo());
            tarea.setDescripcion(datosNuevos.getDescripcion());
        }
        return tarea;
    }

    public boolean delete(int id) {
        return tareasDB.remove(id) != null;
    }

    public Tarea markAsCompleted(int id) {
        Tarea tarea = tareasDB.get(id);
        if (tarea != null) {
            tarea.setCompletada(true);
        }
        return tarea;
    }
}
