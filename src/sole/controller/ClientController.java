package sole.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sole.model.Product;
import sole.model.User;
import sole.util.DataStore;
import sole.util.Styles;
import javafx.scene.image.ImageView;

import java.util.List;

public class ClientController {

    private final Stage stage;
    private final User  user;

    // sidebar buttons
    private Button btnProfile, btnPedidos, btnWishlist, btnCerrar;
    private BorderPane mainLayout;
    private BorderPane contentArea;

    public ClientController(Stage stage, User user) {
        this.stage = stage;
        this.user  = user;
        buildUI();
    }

    /* ═══════════════════════════════════════════
       MAIN LAYOUT
    ═══════════════════════════════════════════ */
    private void buildUI() {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #0d0d0d;");

        mainLayout.setTop(buildTopBar());
        mainLayout.setLeft(buildSidebar());

        contentArea = new BorderPane();
        contentArea.setStyle("-fx-background-color: #0d0d0d;");
        mainLayout.setCenter(contentArea);

        showCatalog(); // default view

        Scene scene = new Scene(mainLayout, 1100, 680);
        scene.getStylesheets().add("data:text/css," + Styles.BASE_CSS.replace(" ", "%20"));
        stage.setScene(scene);
        stage.setTitle("SOLE — Cliente");
    }

    /* ── TOP BAR ── */
    private HBox buildTopBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setMinHeight(54);
        bar.setPadding(new Insets(0, 20, 0, 20));
        bar.setStyle("-fx-background-color: #111111; -fx-border-color: #222222; -fx-border-width: 0 0 1 0;");

        Label logo = new Label("SOL");
        logo.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px; -fx-font-weight: bold;");
        Label logoE = new Label("E");
        logoE.setStyle("-fx-text-fill: #c8ff00; -fx-font-size: 20px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label navInicio    = navLink("Inicio");
        Label navCatalogo  = navLink("Catálogo");
        Label navOfertas   = navLink("Ofertas");

        navInicio.setOnMouseClicked(e   -> showCatalog());
        navCatalogo.setOnMouseClicked(e -> showCatalog());

        Label iconSearch = icon("🔍");
        Label iconHeart  = icon("❤️");
        Label iconCart   = icon("🛒");
        iconHeart.setOnMouseClicked(e -> showWishlist());
        iconCart.setOnMouseClicked(e  -> showCart());

        bar.getChildren().addAll(logo, logoE, spacer,
            navInicio, gap(24), navCatalogo, gap(24), navOfertas, gap(32),
            iconSearch, gap(12), iconHeart, gap(12), iconCart);
        return bar;
    }

    /* ── SIDEBAR ── */
    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.setMinWidth(200);
        sidebar.setStyle("-fx-background-color: #111111; -fx-border-color: #222222; -fx-border-width: 0 1 0 0;");
        sidebar.setPadding(new Insets(20, 0, 20, 0));

        // Avatar / user info
        VBox userBox = new VBox(4);
        userBox.setAlignment(Pos.CENTER);
        userBox.setPadding(new Insets(0, 0, 20, 0));

        String initials = user.getName().substring(0,1).toUpperCase();
        Label avatar = new Label(initials + user.getName().split(" ")[1].substring(0,1).toUpperCase());
        avatar.setStyle("-fx-background-color: #c8ff00; -fx-text-fill: #000000; -fx-font-weight: bold; " +
            "-fx-font-size: 18px; -fx-min-width: 52; -fx-min-height: 52; -fx-background-radius: 26; -fx-alignment: center;");

        Label nameL  = new Label(user.getName().toUpperCase());
        nameL.setStyle("-fx-text-fill: #c8ff00; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label emailL = new Label(user.getEmail());
        emailL.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px;");

        userBox.getChildren().addAll(avatar, nameL, emailL);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #222222;");

        btnProfile = sideNav("👤  Mi Perfil");
        btnPedidos = sideNav("📦  Mis Pedidos");
        btnWishlist = sideNav("❤️  Wishlist");
        btnCerrar  = sideNav("🚪  Cerrar Sesión");

        btnProfile.setOnAction(e  -> showProfile());
        btnPedidos.setOnAction(e  -> showPedidos());
        btnWishlist.setOnAction(e -> showWishlist());
        btnCerrar.setOnAction(e   -> logout());

        sidebar.getChildren().addAll(userBox, sep, btnProfile, btnPedidos, btnWishlist,
            new Region(), btnCerrar);
        VBox.setVgrow(sidebar.getChildren().get(sidebar.getChildren().size()-2), Priority.ALWAYS);
        return sidebar;
    }

    /* ═══════════════════════════════════════════
       VIEWS
    ═══════════════════════════════════════════ */

    /* ── CATALOG ── */
    private void showCatalog() {
        clearActive();
        VBox view = new VBox(20);
        view.setPadding(new Insets(28));

        Label title = sectionTitle("CATÁLOGO");

        // Search bar
        TextField search = new TextField();
        search.setPromptText("Buscar tenis...");
        search.getStyleClass().add("field");
        search.setMaxWidth(320);

        // Product grid
        FlowPane grid = new FlowPane();
        grid.setHgap(16);
        grid.setVgap(16);

        List<Product> products = DataStore.getProducts();
        for (Product p : products) {
            grid.getChildren().add(buildProductCard(p));
        }

        search.textProperty().addListener((obs, old, val) -> {
            grid.getChildren().clear();
            for (Product p : products) {
                if (p.getName().toLowerCase().contains(val.toLowerCase()) ||
                    p.getBrand().toLowerCase().contains(val.toLowerCase())) {
                    grid.getChildren().add(buildProductCard(p));
                }
            }
        });

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        view.getChildren().addAll(title, search, scroll);
        contentArea.setCenter(view);
    }

    private VBox buildProductCard(Product p) {
        VBox card = new VBox(10);
        card.setPrefWidth(200);
        card.getStyleClass().add("product-card");

        // Status badge
        Label badge = new Label(p.getStatus());
        String badgeColor = switch (p.getStatus()) {
            case "Activo"     -> "#1a3a1a; -fx-text-fill: #51cf66;";
            case "Stock bajo" -> "#3a2a00; -fx-text-fill: #fcc419;";
            default           -> "#3a1a1a; -fx-text-fill: #ff6b6b;";
        };
        badge.setStyle("-fx-background-color: " + badgeColor +
            "-fx-background-radius: 3; -fx-padding: 2 7; -fx-font-size: 9px;");

        // ── IMAGEN REAL DEL TENIS ──
ImageView shoe = new ImageView();
shoe.setFitWidth(160);
shoe.setFitHeight(110);
shoe.setPreserveRatio(true);
shoe.setSmooth(true);

String imageName = switch (p.getBrand().toLowerCase()) {
    case "nike"     -> switch (p.getName().toLowerCase()) {
        case String n when n.contains("air max")  -> "nikeAirMax.jpg";
        case String n when n.contains("blazer")   -> "nikeBlazer.jpg";
        case String n when n.contains("force")    -> "nikeForce.jpg";
        default                                    -> "nikeAirMax.jpg";
    };
    case "adidas"   -> "adidasUltra.jpg";
    case "jordan"   -> "airJordan.jpg";
    case "converse" -> switch (p.getName().toLowerCase()) {
        case String n when n.contains("taylor")   -> "chuckTaylor.jpg";
        default                                    -> "chuck.jpg";
    };
    case "new balance" -> "newBlance.jpg";
    case "puma"     -> "pumaRs.jpg";
    default         -> "nikeAirMax.jpg";
};

try {
    javafx.scene.image.Image img = new javafx.scene.image.Image(
        getClass().getResourceAsStream("/images/" + imageName)
    );
    shoe.setImage(img);
} catch (Exception ex) {
    System.out.println("⚠ Imagen no encontrada: " + imageName);
}

        Label brand  = new Label(p.getBrand().toUpperCase());
        brand.getStyleClass().add("product-brand");
        Label name   = new Label(p.getName());
        name.getStyleClass().add("product-name");
        name.setWrapText(true);
        Label price  = new Label(p.getPriceFormatted());
        price.getStyleClass().add("product-price");
        Label cat    = new Label(p.getCategory());
        cat.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px;");

        Button addBtn = new Button(p.getStock() == 0 ? "Agotado" : "Agregar al carrito");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        if (p.getStock() == 0) {
            addBtn.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: #555555; -fx-cursor: default; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 8;");
        } else {
            addBtn.getStyleClass().add("btn-primary");
            addBtn.setOnAction(e -> showAlert("Carrito", p.getName() + " agregado al carrito ✓"));
        }

        // Contenedor centrado para la imagen
        javafx.scene.layout.StackPane imgContainer = new javafx.scene.layout.StackPane(shoe);
        imgContainer.setStyle("-fx-background-color: #1e1e1e; -fx-background-radius: 6; -fx-padding: 8;");
        imgContainer.setMinHeight(126);

        card.getChildren().addAll(badge, imgContainer, brand, name, price, cat, addBtn);
        return card;
    }

    /* ── PROFILE ── */
    private void showProfile() {
        setActive(btnProfile);
        VBox view = new VBox(24);
        view.setPadding(new Insets(28));

        Label title = sectionTitle("MI PERFIL");

        // Stats row
        HBox stats = new HBox(16);
        stats.getChildren().addAll(
            statBox("12", "PEDIDOS"),
            statBox("3",  "WISHLIST"),
            statBox("$1.2M", "GASTADO")
        );

        // Form
        GridPane form = new GridPane();
        form.setHgap(16); form.setVgap(12);

        String[] parts = user.getName().split(" ", 2);
        form.add(formField("NOMBRE", parts[0]),    0, 0);
        form.add(formField("APELLIDO", parts.length > 1 ? parts[1] : ""),  1, 0);
        form.add(formField("EMAIL", user.getEmail()),   0, 1);
        form.add(formField("TELÉFONO", "+57 300 000 0000"), 1, 1);

        VBox dirBox = new VBox(6);
        Label dirLbl = new Label("DIRECCIÓN");
        dirLbl.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px; -fx-font-weight: bold;");
        TextField dirField = new TextField("Calle 123 #45-67, Bogotá, Colombia");
        dirField.getStyleClass().add("field");
        dirField.setMaxWidth(Double.MAX_VALUE);
        dirBox.getChildren().addAll(dirLbl, dirField);
        GridPane.setColumnSpan(dirBox, 2);
        form.add(dirBox, 0, 2);

        HBox actions = new HBox(12);
        Button save   = new Button("GUARDAR CAMBIOS");
        save.getStyleClass().add("btn-primary");
        Button cancel = new Button("Cancelar");
        cancel.getStyleClass().add("btn-ghost");
        save.setOnAction(e -> showAlert("Perfil", "Cambios guardados correctamente ✓"));
        actions.getChildren().addAll(save, cancel);

        view.getChildren().addAll(title, stats, form, actions);
        contentArea.setCenter(new ScrollPane(view) {{ setFitToWidth(true); getStyleClass().add("scroll-pane"); }});
    }

    /* ── PEDIDOS ── */
    private void showPedidos() {
        setActive(btnPedidos);
        VBox view = new VBox(20);
        view.setPadding(new Insets(28));

        Label title = sectionTitle("MIS PEDIDOS");

        // Tabs
        HBox tabs = new HBox(8);
        String[] tabNames = {"Todos", "Enviados", "Entregados", "Cancelados"};
        for (String t : tabNames) {
            Button tb = new Button(t);
            if (t.equals("Todos")) tb.getStyleClass().add("btn-primary");
            else tb.getStyleClass().add("btn-ghost");
            tabs.getChildren().add(tb);
        }

        VBox orders = new VBox(12);
        orders.getChildren().addAll(
            orderRow("#0045", "18 abr 2026", "Air Max 270 · T:42 + Nike Blazer · T:41", "Enviado",    "$720.000", "Entrega estimada: 22 abr 2026"),
            orderRow("#0038", "2 mar 2026",  "Air Jordan 1 Retro High · T:41",           "Entregado", "$480.000", "Entregado el 6 mar 2026"),
            orderRow("#0031", "14 ene 2026", "Puma RS-X Efekt · T:44",                   "Cancelado", "$185.000", "Cancelado por el usuario")
        );

        view.getChildren().addAll(title, tabs, orders);
        contentArea.setCenter(new ScrollPane(view) {{ setFitToWidth(true); getStyleClass().add("scroll-pane"); }});
    }

    /* ── WISHLIST ── */
    private void showWishlist() {
        setActive(btnWishlist);
        VBox view = new VBox(20);
        view.setPadding(new Insets(28));

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = sectionTitle("LISTA DE DESEOS");
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        VBox totalBox = new VBox(2);
        Label totLbl = new Label("Valor total");
        totLbl.setStyle("-fx-text-fill: #666666; -fx-font-size: 10px;");
        Label totVal = new Label("$1.195.000");
        totVal.setStyle("-fx-text-fill: #c8ff00; -fx-font-size: 18px; -fx-font-weight: bold;");
        totalBox.getChildren().addAll(totLbl, totVal);
        header.getChildren().addAll(title, sp, totalBox);

        Label sub = new Label("4 productos guardados");
        sub.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        FlowPane grid = new FlowPane(16, 16);
        List<Product> wl = List.of(
            DataStore.getProducts().get(5),
            DataStore.getProducts().get(1),
            DataStore.getProducts().get(6),
            DataStore.getProducts().get(7)
        );
        for (Product p : wl) grid.getChildren().add(buildProductCard(p));

        view.getChildren().addAll(header, sub, grid);
        contentArea.setCenter(new ScrollPane(view) {{ setFitToWidth(true); getStyleClass().add("scroll-pane"); }});
    }

    /* ── CART ── */
    private void showCart() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(28));
        Label title = sectionTitle("CARRITO");
        Label sub = new Label("3 productos en tu carrito");
        sub.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        HBox content = new HBox(24);
        HBox.setHgrow(content, Priority.ALWAYS);

        // Items
        VBox items = new VBox(12);
        HBox.setHgrow(items, Priority.ALWAYS);
        items.getChildren().addAll(
            cartItem("Air Max 270",    "Nike · Talla 42 · Negro/Blanco", "$320.000", 1),
            cartItem("Ultraboost 22",  "Adidas · Talla 41 · Gris",       "$420.000", 1),
            cartItem("Chuck 70 Hi",    "Converse · Talla 40 · Blanco",   "$185.000", 2)
        );

        // Summary
        VBox summary = new VBox(14);
        summary.setMinWidth(260);
        summary.getStyleClass().add("card");
        Label sumTitle = new Label("RESUMEN");
        sumTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");
        summary.getChildren().addAll(
            sumTitle,
            summaryRow("Subtotal",    "$925.000", false),
            summaryRow("Descuentos",  "-$135.000", false),
            summaryRow("Envío",       "Gratis", false),
            new Separator() {{ setStyle("-fx-background-color: #222222;"); }},
            summaryRow("TOTAL",       "$790.000", true)
        );

        Button checkout = new Button("FINALIZAR COMPRA →");
        checkout.getStyleClass().add("btn-primary");
        checkout.setMaxWidth(Double.MAX_VALUE);
        checkout.setMinHeight(46);
        checkout.setOnAction(e -> showAlert("Compra", "¡Compra realizada con éxito! Pedido #0046 creado."));
        summary.getChildren().add(checkout);

        content.getChildren().addAll(items, summary);
        view.getChildren().addAll(title, sub, content);
        contentArea.setCenter(new ScrollPane(view) {{ setFitToWidth(true); getStyleClass().add("scroll-pane"); }});
    }

    /* ═══════════════════════════════════════════
       HELPERS
    ═══════════════════════════════════════════ */

    private HBox orderRow(String num, String date, String product, String status, String total, String info) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #151515; -fx-border-color: #222222; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 14;");

        Label shoe = new Label("👟");
        shoe.setStyle("-fx-font-size: 28px;");

        VBox left = new VBox(3);
        Label numL = new Label(num);
        numL.setStyle("-fx-text-fill: #c8ff00; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label dateL = new Label(date);
        dateL.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");
        Label prodL = new Label(product);
        prodL.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");
        left.getChildren().addAll(numL, dateL, prodL);

        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);

        Button detail = new Button("Ver detalle");
        detail.getStyleClass().add("btn-ghost");

        String badgeStyle = switch (status) {
            case "Enviado"   -> "#1a3a5c; #4dabf7";
            case "Entregado" -> "#1a3a1a; #51cf66";
            default          -> "#3a1a1a; #ff6b6b";
        };
        String[] bs = badgeStyle.split("; ");
        Label badge = new Label(status);
        badge.setStyle("-fx-background-color: " + bs[0] + "; -fx-text-fill: " + bs[1] +
            "; -fx-background-radius: 4; -fx-padding: 4 10; -fx-font-size: 11px;");

        VBox right = new VBox(2);
        right.setAlignment(Pos.CENTER_RIGHT);
        Label totalL = new Label(total);
        totalL.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label infoL  = new Label(info);
        infoL.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px;");
        right.getChildren().addAll(totalL, infoL);

        row.getChildren().addAll(shoe, left, sp, detail, badge, right);
        return row;
    }

    private HBox cartItem(String name, String desc, String price, int qty) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #151515; -fx-border-color: #222222; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 14;");

        Label shoe = new Label("👟"); shoe.setStyle("-fx-font-size: 32px;");
        VBox info = new VBox(3);
        Label n = new Label(name); n.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 14px;");
        Label d = new Label(desc); d.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");
        HBox qtyBox = new HBox(8);
        qtyBox.setAlignment(Pos.CENTER_LEFT);
        Button minus = new Button("−"); minus.getStyleClass().add("btn-ghost"); minus.setStyle(minus.getStyle() + "-fx-padding: 2 10;");
        Label qtyL = new Label(String.valueOf(qty)); qtyL.setStyle("-fx-text-fill: #ffffff;");
        Button plus = new Button("+"); plus.getStyleClass().add("btn-ghost"); plus.setStyle(plus.getStyle() + "-fx-padding: 2 10;");
        qtyBox.getChildren().addAll(minus, qtyL, plus);
        info.getChildren().addAll(n, d, qtyBox);

        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label p = new Label(price); p.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15px; -fx-font-weight: bold;");
        row.getChildren().addAll(shoe, info, sp, p);
        return row;
    }

    private HBox summaryRow(String label, String val, boolean bold) {
        HBox row = new HBox();
        Label l = new Label(label);
        l.setStyle("-fx-text-fill: " + (bold ? "#ffffff" : "#888888") + "; -fx-font-size: " + (bold ? "14" : "12") + "px;" + (bold ? "-fx-font-weight:bold;" : ""));
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label v = new Label(val);
        v.setStyle("-fx-text-fill: " + (bold ? "#c8ff00" : "#cccccc") + "; -fx-font-size: " + (bold ? "16" : "12") + "px;" + (bold ? "-fx-font-weight:bold;" : ""));
        row.getChildren().addAll(l, sp, v);
        return row;
    }

    private VBox formField(String label, String value) {
        VBox box = new VBox(6);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px; -fx-font-weight: bold;");
        TextField tf = new TextField(value);
        tf.getStyleClass().add("field");
        tf.setMinWidth(220);
        box.getChildren().addAll(lbl, tf);
        return box;
    }

    private HBox statBox(String value, String label) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().add("stat-box");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill: #c8ff00; -fx-font-size: 22px; -fx-font-weight: bold;");
        Label l = new Label(label);
        l.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px;");
        box.getChildren().addAll(v, l);
        return box;
    }

    private Label sectionTitle(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 22px; -fx-font-weight: bold;");
        return l;
    }

    private Label navLink(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #888888; -fx-font-size: 13px; -fx-cursor: hand;");
        l.setOnMouseEntered(e -> l.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-cursor: hand;"));
        l.setOnMouseExited(e  -> l.setStyle("-fx-text-fill: #888888; -fx-font-size: 13px; -fx-cursor: hand;"));
        return l;
    }

    private Label icon(String emoji) {
        Label l = new Label(emoji);
        l.setStyle("-fx-font-size: 16px; -fx-cursor: hand;");
        return l;
    }

    private Region gap(double w) {
        Region r = new Region();
        r.setMinWidth(w); r.setMaxWidth(w);
        return r;
    }

    private Button sideNav(String text) {
        Button b = new Button(text);
        b.getStyleClass().add("nav-item");
        return b;
    }

    private void setActive(Button btn) {
        clearActive();
        btn.getStyleClass().add("nav-item-active");
    }

    private void clearActive() {
        for (Button b : new Button[]{btnProfile, btnPedidos, btnWishlist}) {
            b.getStyleClass().remove("nav-item-active");
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void logout() {
        new LoginController(stage);
    }
}
