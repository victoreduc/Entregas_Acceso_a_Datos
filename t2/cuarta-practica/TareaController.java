package controllers;

import entrega4.gestortareas.Tarea;
import services.TareaService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class TareaController {

    private static final TareaService servicio = new TareaService();

    public static void obtenerTodas(Context ctx) {
        ctx.json(servicio.getAll());
    }

    public static void obtenerPorId(Context ctx) {
        // En Javalin 5 pathParam es igual
        int id = Integer.parseInt(ctx.pathParam("id"));
        Tarea tarea = servicio.getById(id);

        if (tarea != null) {
            ctx.json(tarea);
        } else {
            throw new NotFoundException("Tarea no encontrada");
        }
    }

    public static void crear(Context ctx) {
        Tarea body = ctx.bodyAsClass(Tarea.class);

        if (body.getTitulo() == null || body.getTitulo().trim().isEmpty()) {
            ctx.status(HttpStatus.BAD_REQUEST).result("El título es obligatorio");
            return;
        }

        Tarea creada = servicio.create(body.getTitulo(), body.getDescripcion());
        ctx.status(HttpStatus.CREATED).json(creada);
    }

    public static void actualizar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Tarea body = ctx.bodyAsClass(Tarea.class);

        if (body.getTitulo() == null || body.getTitulo().trim().isEmpty()) {
            ctx.status(HttpStatus.BAD_REQUEST).result("El título es obligatorio");
            return;
        }

        Tarea actualizada = servicio.update(id, body);

        if (actualizada != null) {
            ctx.json(actualizada);
        } else {
            throw new NotFoundException("Tarea no encontrada para actualizar");
        }
    }

    public static void eliminar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (servicio.delete(id)) {
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            throw new NotFoundException("Tarea no encontrada para eliminar");
        }
    }

    public static void marcarCompletada(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Tarea tarea = servicio.markAsCompleted(id);

        if (tarea != null) {
            ctx.json(tarea);
        } else {
            throw new NotFoundException("Tarea no encontrada");
        }
    }

    // Excepción simple
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }
}
