package sole.controller;

import sole.model.Nodo;
import sole.model.User;
import sole.model.Product;

import java.io.*;

public class Cola {

    public Nodo inicio;

    public Cola() {
        inicio = null;
    }

    // ─────────────────── UTILIDADES BASE ───────────────────

    public boolean esVacia() {
        return inicio == null;
    }

    public int getLongitud() {
        if (esVacia()) return 0;
        Nodo p = inicio;
        int cont = 0;
        do {
            cont++;
            p = p.sig;
        } while (p != inicio);
        return cont;
    }

    // ─────────────────── ENCOLAR ───────────────────

    /** Agrega un nodo al final de la cola circular doblemente enlazada */
    public void encolar(Nodo info) {
        if (info == null) return;
        if (esVacia()) {
            inicio = info;
            inicio.sig = inicio;
            inicio.ant = inicio;
        } else {
            info.sig       = inicio;
            info.ant       = inicio.ant;
            inicio.ant.sig = info;
            inicio.ant     = info;
        }
    }

    /** Desencola (atiende) el nodo al frente */
    public Nodo desencolar() {
        if (esVacia()) return null;
        Nodo b = inicio;
        if (inicio.sig == inicio) {
            inicio = null;
        } else {
            inicio         = inicio.sig;
            inicio.ant     = b.ant;
            b.ant.sig      = inicio;
            b.sig = b.ant  = null;
        }
        return b;
    }

    // ─────────────────── BÚSQUEDA ───────────────────

    /** Busca un usuario por email (ignora mayúsculas) */
    public Nodo buscarPorEmail(String email) {
        if (esVacia()) return null;
        Nodo p = inicio;
        do {
            if ("user".equals(p.tipo) && p.email.equalsIgnoreCase(email))
                return p;
            p = p.sig;
        } while (p != inicio);
        return null;
    }

    /** Busca un producto por id */
    public Nodo buscarProductoPorId(int id) {
        if (esVacia()) return null;
        Nodo p = inicio;
        do {
            if ("product".equals(p.tipo) && p.idProd == id)
                return p;
            p = p.sig;
        } while (p != inicio);
        return null;
    }

    // ─────────────────── ARCHIVO: USUARIOS ───────────────────

    /**
     * Guarda todos los usuarios de la cola en users.txt
     * Formato: name;email;password;role
     */
    public boolean guardarUsuarios(String rutaArchivo) {
        if (esVacia()) return false;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            Nodo p = inicio;
            do {
                if ("user".equals(p.tipo)) {
                    bw.write(p.name + ";" + p.email + ";"
                           + p.password + ";" + p.role);
                    bw.newLine();
                }
                p = p.sig;
            } while (p != inicio);
            return true;
        } catch (IOException e) {
            System.err.println("Error guardando usuarios: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga usuarios desde users.txt y los encola.
     * Formato esperado: name;email;password;role
     */
    public boolean cargarUsuarios(String rutaArchivo) {
        File f = new File(rutaArchivo);
        if (!f.exists()) return false;
        inicio = null; // limpia cola
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] d = linea.split(";", -1);
                    if (d.length == 4) {
                        Nodo n = new Nodo(d[0].trim(), d[1].trim(),
                                          d[2].trim(), d[3].trim());
                        encolar(n);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────── ARCHIVO: PRODUCTOS ───────────────────

    /**
     * Guarda todos los productos de la cola en products.txt
     * Formato: id;name;brand;category;price;stock
     */
    public boolean guardarProductos(String rutaArchivo) {
        if (esVacia()) return false;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            Nodo p = inicio;
            do {
                if ("product".equals(p.tipo)) {
                    bw.write(p.idProd + ";" + p.nameProd + ";" + p.brand
                           + ";" + p.category + ";" + p.price + ";" + p.stock);
                    bw.newLine();
                }
                p = p.sig;
            } while (p != inicio);
            return true;
        } catch (IOException e) {
            System.err.println("Error guardando productos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga productos desde products.txt y los encola.
     * Formato esperado: id;name;brand;category;price;stock
     */
    public boolean cargarProductos(String rutaArchivo) {
        File f = new File(rutaArchivo);
        if (!f.exists()) return false;
        inicio = null;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] d = linea.split(";", -1);
                    if (d.length == 6) {
                        int    id       = Integer.parseInt(d[0].trim());
                        String nombre   = d[1].trim();
                        String marca    = d[2].trim();
                        String cat      = d[3].trim();
                        double precio   = Double.parseDouble(d[4].trim());
                        int    stock    = Integer.parseInt(d[5].trim());
                        Nodo n = new Nodo(id, nombre, marca, cat, precio, stock);
                        encolar(n);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error cargando productos: " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            System.err.println("Error de formato en productos: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────── CONVERTIR A OBJETOS ───────────────────

    /** Retorna el User que coincide con email+password, o null */
    public User autenticar(String email, String password) {
        if (esVacia()) return null;
        Nodo p = inicio;
        do {
            if ("user".equals(p.tipo)
                    && p.email.equalsIgnoreCase(email)
                    && p.password.equals(password)) {
                return new User(p.name, p.email, p.password, p.role);
            }
            p = p.sig;
        } while (p != inicio);
        return null;
    }
}
