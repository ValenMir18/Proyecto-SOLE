package sole.model;

public class Product {
    private int id;
    private String name;
    private String brand;
    private String category;
    private double price;
    private int stock;
    private String status; // "Activo", "Stock bajo", "Agotado"

    public Product(int id, String name, String brand, String category, double price, int stock) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.stock = stock;
        if (stock == 0)      this.status = "Agotado";
        else if (stock <= 5) this.status = "Stock bajo";
        else                 this.status = "Activo";
    }

    public int    getId()       { return id; }
    public String getName()     { return name; }
    public String getBrand()    { return brand; }
    public String getCategory() { return category; }
    public double getPrice()    { return price; }
    public int    getStock()    { return stock; }
    public String getStatus()   { return status; }

    public String getPriceFormatted() {
        return String.format("$%,.0f", price).replace(",", ".");
    }
}
