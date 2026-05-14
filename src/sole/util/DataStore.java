package sole.util;

import sole.controller.Cola;
import sole.controller.Pila;
import sole.model.Nodo;
import sole.model.Product;
import sole.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    // ── Rutas de archivos ──
    private static final String RUTA_USERS    = "src/data/users.txt";
    private static final String RUTA_PRODUCTS = "src/data/products.txt";

    // ── Estructuras de datos ──
    public static final Cola colaUsuarios  = new Cola();
    public static final Cola colaProductos = new Cola();
    public static final Pila pilaHistorial = new Pila(); // historial de navegación

    static {
        // Crear carpeta data si no existe
        new File("data").mkdirs();

        // ── Cargar usuarios ──
        boolean usuariosCargados = colaUsuarios.cargarUsuarios(RUTA_USERS);
        if (!usuariosCargados) {
            // Datos iniciales si no existe el archivo
            colaUsuarios.encolar(new Nodo("Juan García", "juan@email.com",  "123456", "cliente", "+57 300 000 0000", "Calle 123 #45-67, Bogotá, Colombia"));
            colaUsuarios.encolar(new Nodo("María López", "maria@email.com", "123456", "cliente", "+57 311 111 1111", "Carrera 45 #12-34, Medellín, Colombia"));
            colaUsuarios.encolar(new Nodo("Admin SOLE",  "admin@sole.com",  "admin",  "admin",   "+57 320 000 0000", "Sede Principal SOLE, Bogotá"));
            // Guardar en archivo por primera vez
            colaUsuarios.guardarUsuarios(RUTA_USERS);
        }

        // ── Cargar productos ──
        boolean productosCargados = colaProductos.cargarProductos(RUTA_PRODUCTS);
        if (!productosCargados) {
            // Datos iniciales si no existe el archivo
            colaProductos.encolar(new Nodo(1, "Nike Air Max 270",      "Nike",        "Running",    320000, 24));
            colaProductos.encolar(new Nodo(2, "Adidas Ultraboost 22",  "Adidas",      "Running",    480000,  3));
            colaProductos.encolar(new Nodo(3, "Chuck Taylor All Star", "Converse",    "Casual",     185000, 40));
            colaProductos.encolar(new Nodo(4, "Air Jordan 1 Retro",    "Nike",        "Basketball", 650000,  0));
            colaProductos.encolar(new Nodo(5, "Puma RS-X",             "Puma",        "Casual",     320000, 18));
            colaProductos.encolar(new Nodo(6, "Nike Air Force 1 '07",  "Nike",        "Casual",     299000, 15));
            colaProductos.encolar(new Nodo(7, "New Balance 990 V6",    "New Balance", "Running",    580000,  0));
            colaProductos.encolar(new Nodo(8, "Chuck 70 Hi",           "Converse",    "Casual",     185000, 10));
            colaProductos.encolar(new Nodo(9, "Nike Blazer Mid 77",    "Nike",        "Casual",     400000,  7));
            // Guardar en archivo por primera vez
            colaProductos.guardarProductos(RUTA_PRODUCTS);
        }

        // Registrar inicio de app en la pila de historial
        pilaHistorial.push("APP_INICIO");
    }

    // ─────────────────── AUTENTICACIÓN ───────────────────

    /**
     * Autentica un usuario usando la cola circular de usuarios.
     * Lee desde el archivo users.txt a través de la cola.
     */
    public static User authenticate(String email, String password) {
        User user = colaUsuarios.autenticar(email, password);
        if (user != null) {
            pilaHistorial.push("LOGIN:" + user.getRole().toUpperCase());
        }
        return user;
    }

    // ─────────────────── PRODUCTOS (para compatibilidad con UI) ───────────────────

    /**
     * Retorna todos los productos de la cola como List<Product>
     * para compatibilidad con el resto de la UI existente.
     */
    public static List<Product> getProducts() {
        List<Product> lista = new ArrayList<>();
        if (colaProductos.esVacia()) return lista;
        Nodo p = colaProductos.inicio;
        do {
            lista.add(new Product(
                p.idProd, p.nameProd, p.brand,
                p.category, (long) p.price, p.stock
            ));
            p = p.sig;
        } while (p != colaProductos.inicio);
        return lista;
    }

    /**
     * Retorna todos los usuarios de la cola como List<User>
     */
    public static List<User> getUsers() {
        List<User> lista = new ArrayList<>();
        if (colaUsuarios.esVacia()) return lista;
        Nodo p = colaUsuarios.inicio;
        do {
            lista.add(new User(p.name, p.email, p.password, p.role));
            p = p.sig;
        } while (p != colaUsuarios.inicio);
        return lista;
    }

    // ─────────────────── PERSISTENCIA ───────────────────

    public static void guardarTodo() {
        colaUsuarios.guardarUsuarios(RUTA_USERS);
        colaProductos.guardarProductos(RUTA_PRODUCTS);
    }
}
