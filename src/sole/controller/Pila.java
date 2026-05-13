package sole.controller;

/**
 * PILA (Stack) enlazada simple.
 * Usada para registrar el historial de navegación del usuario en sesión,
 * cumpliendo el requisito del profesor de mostrar uso de pilas.
 *
 * Ejemplo de uso:
 *   pila.push("LOGIN");
 *   pila.push("DASHBOARD");
 *   pila.push("PRODUCTO_3");
 *   pila.pop() → "PRODUCTO_3"
 */
public class Pila {

    private NodoPila tope;
    private int size;

    // ── Nodo interno de la pila ──
    private static class NodoPila {
        String valor;
        NodoPila siguiente;
        NodoPila(String valor) {
            this.valor = valor;
            this.siguiente = null;
        }
    }

    public Pila() {
        tope = null;
        size = 0;
    }

    public boolean esVacia() {
        return tope == null;
    }

    /** Apila un valor */
    public void push(String valor) {
        NodoPila nuevo = new NodoPila(valor);
        nuevo.siguiente = tope;
        tope = nuevo;
        size++;
    }

    /** Desapila y retorna el tope */
    public String pop() {
        if (esVacia()) return null;
        String val = tope.valor;
        tope = tope.siguiente;
        size--;
        return val;
    }

    /** Consulta el tope sin desapilar */
    public String peek() {
        return esVacia() ? null : tope.valor;
    }

    public int getSize() {
        return size;
    }

    /** Muestra contenido de la pila para debug */
    @Override
    public String toString() {
        if (esVacia()) return "[Pila vacía]";
        StringBuilder sb = new StringBuilder("Pila (tope→base): ");
        NodoPila p = tope;
        while (p != null) {
            sb.append("[").append(p.valor).append("] ");
            p = p.siguiente;
        }
        return sb.toString();
    }
}
