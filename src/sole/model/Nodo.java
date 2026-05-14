package sole.model;

public class Nodo {
    // Campos para Usuario
    public String name;
    public String email;
    public String password;
    public String role;

    // Campos para Producto
    public int idProd;
    public String nameProd;
    public String brand;
    public String category;
    public double price;
    public int stock;

    // Tipo de nodo: "user" o "product"
    public String tipo;

    // Punteros dobles para cola circular doblemente enlazada
    public Nodo sig;
    public Nodo ant;

    /** Constructor para nodo de Usuario */
    public Nodo(String name, String email, String password, String role) {
        this.tipo     = "user";
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.role     = role;
        this.sig      = null;
        this.ant      = null;
    }

    /** Constructor para nodo de Producto */
    public Nodo(int idProd, String nameProd, String brand,
                String category, double price, int stock) {
        this.tipo     = "product";
        this.idProd   = idProd;
        this.nameProd = nameProd;
        this.brand    = brand;
        this.category = category;
        this.price    = price;
        this.stock    = stock;
        this.sig      = null;
        this.ant      = null;
    }
}
