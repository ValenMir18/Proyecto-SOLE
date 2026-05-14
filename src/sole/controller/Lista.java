package sole.controller;

public class Lista {

    private NodoLista cabeza;
    private int size;

    // ── Nodo interno de la lista ──
    public static class NodoLista {
        public int    idProducto;
        public String nombre;
        public double precio;
        public int    cantidad;
        public NodoLista siguiente;

        public NodoLista(int idProducto, String nombre, double precio, int cantidad) {
            this.idProducto = idProducto;
            this.nombre     = nombre;
            this.precio     = precio;
            this.cantidad   = cantidad;
            this.siguiente  = null;
        }
    }

    public Lista() {
        cabeza = null;
        size   = 0;
    }

    public boolean esVacia() {
        return cabeza == null;
    }

    /** Agrega producto al final de la lista */
    public void agregar(int idProducto, String nombre, double precio, int cantidad) {
        NodoLista nuevo = new NodoLista(idProducto, nombre, precio, cantidad);
        if (esVacia()) {
            cabeza = nuevo;
        } else {
            NodoLista p = cabeza;
            while (p.siguiente != null) p = p.siguiente;
            p.siguiente = nuevo;
        }
        size++;
    }

    /** Elimina un producto por id */
    public boolean eliminar(int idProducto) {
        if (esVacia()) return false;
        if (cabeza.idProducto == idProducto) {
            cabeza = cabeza.siguiente;
            size--;
            return true;
        }
        NodoLista p = cabeza;
        while (p.siguiente != null) {
            if (p.siguiente.idProducto == idProducto) {
                p.siguiente = p.siguiente.siguiente;
                size--;
                return true;
            }
            p = p.siguiente;
        }
        return false;
    }

    /** Calcula el total del carrito */
    public double getTotal() {
        double total = 0;
        NodoLista p = cabeza;
        while (p != null) {
            total += p.precio * p.cantidad;
            p = p.siguiente;
        }
        return total;
    }

    public int getSize() { return size; }

    public NodoLista getCabeza() { return cabeza; }

    public void vaciar() {
        cabeza = null;
        size   = 0;
    }
}
