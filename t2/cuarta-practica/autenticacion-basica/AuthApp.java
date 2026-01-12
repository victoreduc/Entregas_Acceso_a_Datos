package entrega4.autenticacionbasica;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import io.javalin.http.UnauthorizedResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AuthApp {

    // Simulación de Base de Datos en memoria (Username -> Usuario Objeto)
    private static final Map<String, User> usuariosDB = new HashMap<>();

    public static void main(String[] args) {
        // Inicializar Javalin en el puerto 7070
        Javalin app = Javalin.create().start(7070);

        // Middleware: Se ejecuta antes de las rutas protegidas
        // Protegemos cualquier ruta que empiece por /perfil
        app.before("/perfil", AuthApp::verificarAutenticacion);

        // Definición de Endpoints
        app.post("/auth/registrar", AuthApp::registrar);
        app.post("/auth/login", AuthApp::login);
        app.get("/perfil", AuthApp::obtenerPerfil);

        System.out.println("Servidor corriendo en http://localhost:7070");
    }

    /*
     * Registra un nuevo usuario
     * @param ctx contexto con body {username, password, email}
     */
    public static void registrar(Context ctx) {
        // Parseamos el JSON del cuerpo a nuestra clase DTO (Data Transfer Object)
        User nuevoUsuario = ctx.bodyAsClass(User.class);

        // Validación básica
        if (nuevoUsuario.username == null || nuevoUsuario.password == null || nuevoUsuario.email == null) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("error", "Faltan datos requeridos"));
            return;
        }

        // Validar duplicados
        if (usuariosDB.containsKey(nuevoUsuario.username)) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("error", "El usuario ya existe"));
            return;
        }

        // Configurar fecha de registro (ISO 8601 simple)
        nuevoUsuario.fechaRegistro = LocalDateTime.now().toString();

        // NOTA EDUCATIVA: En producción, aquí deberíamos hashear la contraseña (ej. BCrypt)
        // Por limitaciones del ejercicio y dependencias, la guardamos tal cual.
        usuariosDB.put(nuevoUsuario.username, nuevoUsuario);

        // Respuesta 201 Created
        ctx.status(HttpStatus.CREATED).json(Map.of(
            "mensaje", "Usuario registrado exitosamente",
            "username", nuevoUsuario.username
        ));
    }

    /*
     * Realiza login y genera token simple
     * @param ctx contexto con body {username, password}
     */
    public static void login(Context ctx) {
        // Usamos una clase auxiliar para el login (solo username y password)
        LoginDTO credenciales = ctx.bodyAsClass(LoginDTO.class);

        User usuario = usuariosDB.get(credenciales.username);

        // Verificar si el usuario existe y si la contraseña coincide
        if (usuario == null || !usuario.password.equals(credenciales.password)) {
            ctx.status(HttpStatus.UNAUTHORIZED).json(Map.of("error", "Credenciales inválidas"));
            return;
        }

        // Generar token simple: username + "_" + timestamp
        String token = usuario.username + "_" + System.currentTimeMillis();

        // Respuesta 200 OK
        ctx.status(HttpStatus.OK).json(Map.of(
            "token", token,
            "username", usuario.username
        ));
    }

    /*
     * Handler que verifica autenticación antes de ejecutar endpoint
     * @param ctx contexto de Javalin
     */
    public static void verificarAutenticacion(Context ctx) {
        String token = ctx.header("Authorization");

        if (token == null) {
            throw new UnauthorizedResponse("No autorizado. Token requerido");
        }

        String usernameValidado = validarToken(token);

        // Validar si el token es correcto
        if (usernameValidado == null) {
            throw new UnauthorizedResponse("Token inválido o expirado");
        } else {
            // Token válido: guardamos el usuario y continuamos
            ctx.attribute("username", usernameValidado);
        }
    }

    /*
     * Valida token de autenticación
     * @param token token a validar
     * @return username del usuario o null si inválido
     */
    public static String validarToken(String token) {
        try {
            // El formato es "usuario_timestamp"
            String[] partes = token.split("_");
            if (partes.length < 2) return null;

            String username = partes[0];
            // En un caso real, aquí verificaríamos también la expiración del timestamp

            if (usuariosDB.containsKey(username)) {
                return username;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /*
     * Obtiene perfil del usuario autenticado
     * @param ctx contexto con header Authorization
     */
    public static void obtenerPerfil(Context ctx) {
        // Como ya pasó por "verificarAutenticacion", sabemos que el token es válido.
        // Podríamos recuperar el usuario del atributo seteado:
        // String username = ctx.attribute("username");
        
        // Pero siguiendo la firma estricta del ejercicio, lo extraemos del header nuevamente:
        String token = ctx.header("Authorization");
        String username = validarToken(token); 
        
        User usuario = usuariosDB.get(username);

        // Retornamos los datos del perfil (sin la contraseña)
        ctx.status(HttpStatus.OK).json(Map.of(
            "username", usuario.username,
            "email", usuario.email,
            "fechaRegistro", usuario.fechaRegistro
        ));
    }

    // --- Clases de Modelo (DTOs) ---

    // Clase para representar al Usuario en "Base de Datos" y Registro
    public static class User {
        public String username;
        public String password;
        public String email;
        public String fechaRegistro;
        
        // Constructor vacío necesario para Jackson
        public User() {}
    }

    // Clase auxiliar solo para recibir el JSON de Login
    public static class LoginDTO {
        public String username;
        public String password;
        
        public LoginDTO() {}
    }
}
