package entrega4;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class BibliotecaApp {

    // Base de datos en memoria (HashMap es eficiente para búsquedas por clave)
    private static Map<String, Libro> biblioteca = new HashMap<>();
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        // Inicializamos datos de prueba para no empezar vacíos
        biblioteca.put("978-0134685991", new Libro("978-0134685991", "Effective Java", "Joshua Bloch", 2018));
        biblioteca.put("978-0132350884", new Libro("978-0132350884", "Clean Code", "Robert C. Martin", 2008));

        // Configuramos el puerto (opcional, por defecto es 4567)
        port(4567);

        // Definimos que todas las respuestas sean de tipo JSON
        after((req, res) -> res.type("application/json"));

        // Llamada a la función requerida
        configurarRutas();
        
        System.out.println("Servidor corriendo en http://localhost:4567/libros");
    }

    /*
     * Registra todas las rutas de la API
     */
    public static void configurarRutas() {
        // Importante: Definir rutas específicas antes de las rutas con parámetros (:isbn)
        
        // Buscar libros (GET /libros/buscar?q=Java)
        get("/libros/buscar", BibliotecaApp::buscarLibros, new JsonTransformer());

        // Obtener todos o filtrar (GET /libros?autor=X)
        get("/libros", BibliotecaApp::obtenerLibros, new JsonTransformer());

        // Obtener un libro específico (GET /libros/:isbn)
        get("/libros/:isbn", BibliotecaApp::obtenerLibroPorIsbn, new JsonTransformer());

        // Crear libro (POST /libros)
        post("/libros", BibliotecaApp::crearLibro, new JsonTransformer());
        
        // Extras mencionados en "Endpoints disponibles" (Implementación básica)
        delete("/libros/:isbn", (req, res) -> {
            String isbn = req.params(":isbn");
            if (biblioteca.remove(isbn) != null) {
                res.status(204); // No content
                return "";
            }
            res.status(404);
            return gson.toJson("Libro no encontrado");
        });
    }

    /*
     * Obtiene todos los libros o filtra por autor
     */
    public static Object obtenerLibros(Request req, Response res) {
        String autorParam = req.queryParams("autor");

        if (autorParam != null && !autorParam.isEmpty()) {
            // Filtrado usando Stream API (ignorando mayúsculas/minúsculas)
            List<Libro> librosFiltrados = biblioteca.values().stream()
                .filter(l -> l.getAutor().equalsIgnoreCase(autorParam))
                .collect(Collectors.toList());
            
            return librosFiltrados;
        }

        // Si no hay filtro, devolvemos todo
        return biblioteca.values();
    }

    /*
     * Busca libros por título (búsqueda parcial)
     * Ruta: /libros/buscar?q=titulo
     */
    public static Object buscarLibros(Request req, Response res) {
        String query = req.queryParams("q");

        if (query == null || query.isEmpty()) {
            res.status(400); // Bad Request
            return "Falta el parámetro de búsqueda 'q'";
        }

        // Búsqueda parcial (contains) e insuensible a mayúsculas
        List<Libro> resultado = biblioteca.values().stream()
            .filter(l -> l.getTitulo().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());

        return resultado;
    }

    /*
     * Obtiene un libro específico por ISBN
     */
    public static Object obtenerLibroPorIsbn(Request req, Response res) {
        String isbn = req.params(":isbn");
        Libro libro = biblioteca.get(isbn);

        if (libro != null) {
            return libro;
        } else {
            res.status(404);
            // Retornamos un objeto simple de error, Gson lo convertirá
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Libro no encontrado con ISBN: " + isbn);
            return error;
        }
    }

    /*
     * Crea un nuevo libro
     */
    public static Object crearLibro(Request req, Response res) {
        try {
            // Deserializamos el JSON del body a un objeto Libro
            Libro nuevoLibro = gson.fromJson(req.body(), Libro.class);

            // Validaciones básicas de Junior
            if (nuevoLibro.getIsbn() == null || nuevoLibro.getIsbn().isEmpty()) {
                res.status(400);
                return "El ISBN es obligatorio";
            }

            if (biblioteca.containsKey(nuevoLibro.getIsbn())) {
                res.status(409); // Conflict
                return "Ya existe un libro con ese ISBN";
            }

            // Aseguramos disponibilidad por defecto al crear
            nuevoLibro.setDisponible(true);

            // Guardamos en el HashMap
            biblioteca.put(nuevoLibro.getIsbn(), nuevoLibro);

            res.status(201); // Created
            return nuevoLibro;

        } catch (Exception e) {
            res.status(400);
            return "Error en el formato del JSON enviando";
        }
    }
}
