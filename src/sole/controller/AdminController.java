package sole.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sole.model.Product;
import sole.model.User;
import sole.util.DataStore;
import sole.util.Styles;

import java.util.List;

public class AdminController {

    private final Stage stage;
    private final User  user;

    private Button btnDash, btnPedidos, btnProductos, btnUsuarios, btnReportes;
    private BorderPane contentArea;

    public AdminController(Stage stage, User user) {
        this.stage = stage;
        this.user  = user;
        buildUI();
    }

    /* ═══════════════════════════════════════════
       MAIN LAYOUT
    ═══════════════════════════════════════════ */
    private void buildUI() {
        BorderPane root = new BorderPane();
        root.setLeft(buildSidebar());

        contentArea = new BorderPane();
        contentArea.setStyle("-fx-background-color: #0d0d0d;");
        root.setCenter(contentArea);

        showDashboard();

        Scene scene = new Scene(root, 1100, 680);
        scene.getStylesheets().add("data:text/css," + Styles.BASE_CSS.replace(" ", "%20"));
        stage.setScene(scene);
        stage.setTitle("SOLE — Panel Admin");
    }

    /* ── SIDEBAR ── */
    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.setMinWidth(210);
        sidebar.setStyle("-fx-background-color: #111111; -fx-border-color: #222222; -fx-border-width: 0 1 0 0;");
        sidebar.setPadding(new Insets(0, 0, 16, 0));

        // Logo header
        HBox logoBox = new HBox(4);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(18, 20, 18, 20));
        logoBox.setStyle("-fx-border-color: #222222; -fx-border-width: 0 0 1 0;");
        Label sol = new Label("SOL");
        sol.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px; -fx-font-weight: bold;");
        Label e = new Label("E");
        e.setStyle("-fx-text-fill: #c8ff00; -fx-font-size: 20px; -fx-font-weight: bold;");
        logoBox.getChildren().addAll(sol, e);

        // Admin info
        VBox adminBox = new VBox(2);
        adminBox.setPadding(new Insets(16, 20, 16, 20));
        adminBox.setStyle("-fx-border-color: #222222; -fx-border-width: 0 0 1 0;");
        Label panelLbl = new Label("PANEL ADMIN");
        panelLbl.setStyle("-fx-text-fill: #555555; -fx-font-size: 9px;");
        Label emailLbl = new Label(user.getEmail());
        emailLbl.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px;");
        adminBox.getChildren().addAll(panelLbl, emailLbl);

        btnDash      = sideNav("📊  Dashboard");
        btnPedidos   = sideNav("📦  Pedidos");
        btnProductos = sideNav("👟  Productos");
        btnUsuarios  = sideNav("👥  Usuarios");
        btnReportes  = sideNav("📈  Reportes");
        Button btnLogout = sideNav("🚪  Cerrar Sesión");

        btnDash.setOnAction(e2      -> showDashboard());
        btnPedidos.setOnAction(e2   -> showPedidos());
        btnProductos.setOnAction(e2 -> showProductos());
        btnUsuarios.setOnAction(e2  -> showUsuarios());
        btnReportes.setOnAction(e2  -> showReportes());
        btnLogout.setOnAction(e2    -> logout());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(logoBox, adminBox,
            btnDash, btnPedidos, btnProductos, btnUsuarios, btnReportes,
            spacer, btnLogout);
        return sidebar;
    }

    /* ═══════════════════════════════════════════
       VIEWS
    ═══════════════════════════════════════════ */

    /* ── DASHBOARD ── */
    private void showDashboard() {
        setActive(btnDash);
        VBox view = new VBox(24);
        view.setPadding(new Insets(28));

        // Breadcrumb
        Label crumb = new Label("MÓDULO 1 — DASHBOARD");
        crumb.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px;");

        Label title = sectionTitle("DASHBOARD");
        Label date  = new Label("Panel de administración · Hoy, 21 Abr 2026");
        date.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        // KPI cards
        HBox kpis = new HBox(16);
        kpis.getChildren().addAll(
            kpiCard("VENTAS HOY",    "$4.2M",  "+12%",  "#c8ff00"),
            kpiCard("PEDIDOS",       "47",      "+8%",   "#4dabf7"),
            kpiCard("PRODUCTOS",     "320",     "activos","#ffffff"),
            kpiCard("USUARIOS",      "1,240",   "registrados","#fcc419")
        );

        // Two columns
        HBox cols = new HBox(20);
        HBox.setHgrow(cols, Priority.ALWAYS);

        // Recent orders table
        VBox recentBox = new VBox(12);
        HBox.setHgrow(recentBox, Priority.ALWAYS);
        Label recentTitle = new Label("ÚLTIMOS PEDIDOS");
        recentTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold;");

        TableView<String[]> tbl = buildOrderTable();
        recentBox.getChildren().addAll(recentTitle, tbl);

        // Activity feed
        VBox actBox = new VBox(10);
        actBox.setMinWidth(260);
        actBox.getStyleClass().add("card");
        Label actTitle = new Label("ACTIVIDAD RECIENTE");
        actTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold;");
        actBox.getChildren().addAll(actTitle,
            actItem("🟡", "Nuevo pedido #0045"),
            actItem("🟠", "Stock bajo: Air Max"),
            actItem("⚪", "Usuario nuevo registrado"),
            actItem("⚪", "Pedido #0041 entregado")
        );

        cols.getChildren().addAll(recentBox, actBox);
        view.getChildren().addAll(crumb, title, date, kpis, cols);
        contentArea.setCenter(new ScrollPane(view) {{ setFitToWidth(true); getStyleClass().add("scroll-pane"); }});
    }

    private TableView<String[]> buildOrderTable() {
        TableView<String[]> tbl = new TableView<>();
        tbl.getStyleClass().add("table-view");
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbl.setMaxHeight(160);

        String[][] data = {
            {"#0045", "Juan G.", "$720.000", "Enviado"},
            {"#0044", "María L.", "$480.000", "Pendiente"},
            {"#0043", "Carlos M.", "$185.000", "Entregado"}
        };
        String[] cols = {"PEDIDO","CLIENTE","TOTAL","ESTADO"};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue()[idx]));
            if (i == 0) col.setStyle("-fx-text-fill: #c8ff00;");
            tbl.getColumns().add(col);
        }
        for (String[] row : data) tbl.getItems().add(row);
        return tbl;
    }

    /* ── PEDIDOS ADMIN ── */
    private void showPedidos() {
        setActive(btnPedidos);
        VBox view = new VBox(20);
        view.setPadding(new Insets(28));

        Label crumb = new Label("MÓDULO 2 — GESTIÓN DE PEDIDOS");
        crumb.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px;");
        Label title = sectionTitle("GESTIÓN DE PEDIDOS");
        Label sub   = new Label("Administrar, filtrar y actualizar pedidos");
        sub.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        HBox searchRow = new HBox(12);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        TextField search = new TextField();
        search.setPromptText("Buscar pedido...");
        search.getStyleClass().add("field");
        search.setMinWidth(280);
        HBox.setHgrow(search, Priority.ALWAYS);
        String[] fTab = {"Todos","Enviado","Pendiente","Entregado","Cancelado"};
        for (String t : fTab) {
            Button b = new Button(t);
            b.getStyleClass().add(t.equals("Todos") ? "btn-primary" : "btn-ghost");
            searchRow.getChildren().add(b);
        }
        searchRow.getChildren().add(0, search);

        TableView<String[]> tbl = new TableView<>();
        tbl.getStyleClass().add("table-view");
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbl.setMaxHeight(280);

        String[][] data = {
            {"#0045","Juan G.","Air Max 270","$720.000","21/04/26","Enviado"},
            {"#0044","María L.","Ultraboost 22","$480.000","21/04/26","Pendiente"},
            {"#0043","Carlos M.","Chuck Taylor","$185.000","20/04/26","Entregado"},
            {"#0042","Ana P.","Jordan 1 Retro","$650.000","20/04/26","Enviado"},
            {"#0041","Luis R.","Puma RS-X","$320.000","19/04/26","Entregado"}
        };
        String[] cols = {"#PEDIDO","CLIENTE","PRODUCTO","TOTAL","FECHA","ESTADO"};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue()[idx]));
            tbl.getColumns().add(col);
        }
        for (String[] row : data) tbl.getItems().add(row);

        Label funcs = new Label("Funciones disponibles: Ver detalle · Cambiar estado · Imprimir comprobante · Cancelar pedido");
        funcs.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px; -fx-border-color: #c8ff00; -fx-border-width: 0 0 0 3; -fx-padding: 0 0 0 12;");

        view.getChildren().addAll(crumb, title, sub, searchRow, tbl, funcs);
        contentArea.setCenter(new ScrollPane(view) {{ setFitToWidth(true); getStyleClass().add("scroll-pane"); }});
    }

    /* ── PRODUCTOS ADMIN ── */
    private void showProductos() {
        setActive(btnProductos);
        VBox view = new VBox(20);
        view.setPadding(new Insets(28));

        Label crumb = new Label("MÓDULO 3 — GESTIÓN DE PRODUCTOS");
        crumb.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px;");
        Label title = sectionTitle("CATÁLOGO DE PRODUCTOS");
        Label sub   = new Label("Agregar, editar y administrar inventario");
        sub.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        HBox searchRow = new HBox(12);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        TextField search = new TextField();
        search.setPromptText("Buscar producto...");
        search.getStyleClass().add("field");
        HBox.setHgrow(search, Priority.ALWAYS);
        Button newBtn = new Button("+ NUEVO");
        newBtn.getStyleClass().add("btn-primary");
        newBtn.setOnAction(e -> showAlert("Nuevo Producto", "Formulario: Nombre · Categoría · Precio · Stock · Descripción · Estado"));
        searchRow.getChildren().addAll(search, newBtn);

        TableView<Product> tbl = new TableView<>();
        tbl.getStyleClass().add("table-view");
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(50);

        TableColumn<Product, String> colName = new TableColumn<>("PRODUCTO");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> colCat = new TableColumn<>("CATEGORÍA");
        colCat.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Product, String> colPrice = new TableColumn<>("PRECIO");
        colPrice.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPriceFormatted()));

        TableColumn<Product, Integer> colStock = new TableColumn<>("STOCK");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Product, String> colStatus = new TableColumn<>("ESTADO");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Product, Void> colActions = new TableColumn<>("ACCIONES");
        colActions.setCellFactory(col -> new TableCell<>() {
            final Button edit = new Button("✏ Editar");
            final Button del  = new Button("🗑 Eliminar");
            { edit.getStyleClass().add("btn-ghost"); del.getStyleClass().add("btn-ghost");
              edit.setOnAction(e -> showAlert("Editar", "Editando: " + getTableView().getItems().get(getIndex()).getName()));
              del.setOnAction(e  -> showAlert("Eliminar", "¿Eliminar " + getTableView().getItems().get(getIndex()).getName() + "?")); }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) { setGraphic(null); } else {
                    HBox box = new HBox(6, edit, del);
                    setGraphic(box);
                }
            }
        });

        tbl.getColumns().addAll(colId, colName, colCat, colPrice, colStock, colStatus, colActions);
        tbl.getItems().addAll(DataStore.getProducts());

        Label funcs = new Label("Formulario de producto: Nombre · Categoría · Precio · Stock · Descripción · Imagen · Estado (Activo/Inactivo)");
        funcs.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px; -fx-border-color: #c8ff00; -fx-border-width: 0 0 0 3; -fx-padding: 0 0 0 12;");

        view.getChildren().addAll(crumb, title, sub, searchRow, tbl, funcs);
        contentArea.setCenter(new ScrollPane(view) {{ setFitToWidth(true); getStyleClass().add("scroll-pane"); }});
    }

    /* ── USUARIOS ADMIN ── */
    private void showUsuarios() {
        setActive(btnUsuarios);
        VBox view = new VBox(20);
        view.setPadding(new Insets(28));

        Label crumb = new Label("MÓDULO 4 — GESTIÓN DE USUARIOS");
        crumb.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px;");
        Label title = sectionTitle("GESTIÓN DE USUARIOS");
        Label sub   = new Label("Administrar clientes y roles del sistema");
        sub.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        HBox kpis = new HBox(16);
        kpis.getChildren().addAll(
            kpiCard("TOTAL USUARIOS", "1,240", "registrados", "#ffffff"),
            kpiCard("ADMINS",          "3",     "activos",     "#c8ff00"),
            kpiCard("NUEVOS ESTE MES", "87",    "+7% vs anterior","#51cf66")
        );

        TableView<String[]> tbl = new TableView<>();
        tbl.getStyleClass().add("table-view");
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        String[][] data = {
            {"001","Juan García","juan@email.com","Cliente","10/01/26","Activo"},
            {"002","María López","maria@email.com","Cliente","15/02/26","Activo"},
            {"003","Carlos M.","carlos@email.com","Admin","01/01/26","Admin"},
            {"004","Ana Pérez","ana@email.com","Cliente","20/03/26","Inactivo"},
            {"005","Luis Ramos","luis@email.com","Cliente","05/04/26","Activo"}
        };
        String[] cols = {"ID","NOMBRE","EMAIL","ROL","FECHA REG.","ESTADO"};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue()[idx]));
            tbl.getColumns().add(col);
        }
        for (String[] row : data) tbl.getItems().add(row);

        Label funcs = new Label("Acciones: Ver perfil · Editar datos · Cambiar rol · Activar/Desactivar · Eliminar cuenta");
        funcs.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px; -fx-border-color: #c8ff00; -fx-border-width: 0 0 0 3; -fx-padding: 0 0 0 12;");

        view.getChildren().addAll(crumb, title, sub, kpis, tbl, funcs);
        contentArea.setCenter(new ScrollPane(view) {{ setFitToWidth(true); getStyleClass().add("scroll-pane"); }});
    }

    /* ── REPORTES ADMIN ── */
    private void showReportes() {
        setActive(btnReportes);
        VBox view = new VBox(20);
        view.setPadding(new Insets(28));

        Label crumb = new Label("MÓDULO 5 — REPORTES");
        crumb.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px;");
        Label title = sectionTitle("REPORTES");
        Label sub   = new Label("Ventas, inventario y actividad del sistema");
        sub.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        HBox kpis = new HBox(16);
        kpis.getChildren().addAll(
            kpiCard("VENTAS MES",    "$18.4M", "+15% vs anterior","#c8ff00"),
            kpiCard("PEDIDOS MES",   "213",    "completados",      "#4dabf7"),
            kpiCard("PROD. VENDIDOS","847",    "unidades",         "#ffffff"),
            kpiCard("TICKET PROM.",  "$86.400","por pedido",       "#fcc419")
        );

        HBox cols = new HBox(20);

        // Bar chart (simple visual)
        VBox chartBox = new VBox(12);
        HBox.setHgrow(chartBox, Priority.ALWAYS);
        chartBox.getStyleClass().add("card");
        Label chartTitle = new Label("VENTAS POR MES");
        chartTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold;");

        HBox bars = new HBox(16);
        bars.setAlignment(Pos.BOTTOM_LEFT);
        bars.setMinHeight(120);
        String[][] barData = {{"Ene","12"},{"Feb","14"},{"Mar","17"},{"Abr","18"}};
        for (String[] b : barData) {
            VBox barCol = new VBox(4);
            barCol.setAlignment(Pos.BOTTOM_CENTER);
            double h = Double.parseDouble(b[1]) * 5.5;
            Label bar = new Label();
            bar.setMinSize(40, h);
            bar.setMaxSize(40, h);
            bar.setStyle("-fx-background-color: #c8ff00; -fx-background-radius: 3 3 0 0;");
            Label val = new Label(b[1] + "M");
            val.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 11px;");
            Label month = new Label(b[0]);
            month.setStyle("-fx-text-fill: #666666; -fx-font-size: 10px;");
            barCol.getChildren().addAll(val, bar, month);
            bars.getChildren().add(barCol);
        }
        chartBox.getChildren().addAll(chartTitle, bars);

        // Top products
        VBox topBox = new VBox(10);
        topBox.setMinWidth(260);
        topBox.getStyleClass().add("card");
        Label topTitle = new Label("TOP PRODUCTOS");
        topTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold;");
        topBox.getChildren().addAll(topTitle,
            topProduct("Air Max 270",   "$3.2M", 1.0),
            topProduct("Jordan 1 Retro","$2.6M", 0.8),
            topProduct("Ultraboost 22", "$2.0M", 0.6),
            topProduct("Chuck Taylor",  "$1.5M", 0.45)
        );

        cols.getChildren().addAll(chartBox, topBox);

        Label export = new Label("Exportar: PDF · Excel · CSV | Filtrar por: Rango de fechas · Categoría · Estado");
        export.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px;");

        view.getChildren().addAll(crumb, title, sub, kpis, cols, export);
        contentArea.setCenter(new ScrollPane(view) {{ setFitToWidth(true); getStyleClass().add("scroll-pane"); }});
    }

    /* ═══════════════════════════════════════════
       HELPERS
    ═══════════════════════════════════════════ */
    private VBox kpiCard(String label, String value, String sub, String color) {
        VBox box = new VBox(4);
        box.getStyleClass().add("stat-box");
        box.setMinWidth(180);
        Label l = new Label(label);
        l.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 26px; -fx-font-weight: bold;");
        Label s = new Label(sub);
        s.setStyle("-fx-text-fill: #51cf66; -fx-font-size: 10px;");
        box.getChildren().addAll(l, v, s);
        return box;
    }

    private HBox actItem(String dot, String text) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        Label d = new Label(dot); d.setStyle("-fx-font-size: 12px;");
        Label t = new Label(text); t.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");
        row.getChildren().addAll(d, t);
        return row;
    }

    private VBox topProduct(String name, String val, double ratio) {
        VBox box = new VBox(4);
        HBox row = new HBox();
        Label n = new Label(name); n.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label v = new Label(val); v.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 12px; -fx-font-weight: bold;");
        row.getChildren().addAll(n, sp, v);
        ProgressBar pb = new ProgressBar(ratio);
        pb.setMaxWidth(Double.MAX_VALUE);
        pb.setStyle("-fx-accent: #c8ff00; -fx-background-color: #222222; -fx-background-radius: 2; -fx-pref-height: 6;");
        box.getChildren().addAll(row, pb);
        return box;
    }

    private Label sectionTitle(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 22px; -fx-font-weight: bold;");
        return l;
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
        for (Button b : new Button[]{btnDash, btnPedidos, btnProductos, btnUsuarios, btnReportes}) {
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
