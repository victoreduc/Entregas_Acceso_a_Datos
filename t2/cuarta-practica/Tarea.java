package entrega4.gestortareas;

/*
EJERCICIOS OBLIGATORIOS
Ejercicio 1: API de Gestión de Tareas
Objetivo: Crear una API REST completa para gestionar tareas (To-Do List) con
Javalin, implementando todas las operaciones CRUD y validaciones básicas.
*/

import java.time.LocalDateTime;

public class Tarea {
    private int id;
    private String titulo;
    private String descripcion;
    private boolean completada;
    private String fechaCreacion; // Cambiado a String para compatibilidad

    // Constructor vacío necesario para Jackson
    public Tarea() {
    }

    public Tarea(int id, String titulo, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completada = false;
        // Convertimos la fecha a String al momento de crearla
        this.fechaCreacion = LocalDateTime.now().withNano(0).toString();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
