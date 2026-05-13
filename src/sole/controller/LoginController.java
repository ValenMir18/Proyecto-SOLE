package sole.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import sole.model.User;
import sole.util.DataStore;
import sole.util.Styles;

public class LoginController {

    private final Stage stage;

    public LoginController(Stage stage) {
        this.stage = stage;
        buildUI();
    }

    private void buildUI() {
        /* ── LEFT PANEL ── */
        StackPane leftPanel = new StackPane();
        leftPanel.setStyle("-fx-background-color: #0d0d0d;");
        leftPanel.setMinWidth(380);

        // Big ghost text background
        Label ghost = new Label("SOLE");
        ghost.setStyle("-fx-text-fill: #1a1a1a; -fx-font-size: 140px; -fx-font-weight: bold;");

        VBox brandBox = new VBox(8);
        brandBox.setAlignment(Pos.CENTER);

        Label logo = buildLogo("56px");
        Label tagline = new Label("STEP INTO YOUR STYLE");
        tagline.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px; -fx-letter-spacing: 3;");

        Label shoe = new Label("👟");
        shoe.setStyle("-fx-font-size: 60px;");

        brandBox.getChildren().addAll(shoe, logo, tagline);
        leftPanel.getChildren().addAll(ghost, brandBox);

        /* ── RIGHT PANEL ── */
        VBox rightPanel = new VBox(24);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(60, 60, 60, 60));
        rightPanel.setStyle("-fx-background-color: #111111;");
        rightPanel.setMinWidth(420);

        Label welcome = new Label("Bienvenido");
        welcome.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label sub = new Label("Inicia sesión en tu cuenta");
        sub.setStyle("-fx-text-fill: #666666; -fx-font-size: 13px;");

        // Email
        VBox emailBox = new VBox(6);
        Label emailLbl = new Label("EMAIL");
        emailLbl.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px; -fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("correo@ejemplo.com");
        emailField.getStyleClass().add("field");
        emailField.setMaxWidth(Double.MAX_VALUE);
        emailBox.getChildren().addAll(emailLbl, emailField);

        // Password
        VBox passBox = new VBox(6);
        Label passLbl = new Label("CONTRASEÑA");
        passLbl.setStyle("-fx-text-fill: #555555; -fx-font-size: 10px; -fx-font-weight: bold;");
        PasswordField passField = new PasswordField();
        passField.setPromptText("••••••••");
        passField.getStyleClass().add("field");
        passField.setMaxWidth(Double.MAX_VALUE);
        passBox.getChildren().addAll(passLbl, passField);

        // Error label
        Label errorLbl = new Label("");
        errorLbl.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 12px;");

        // Login button
        Button loginBtn = new Button("INICIAR SESIÓN");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setMinHeight(46);

        // Info note
        Label note = new Label("⚠  Proyecto NetBeans — solo Iniciar Sesión.\nSin registro social ni creación de cuenta.");
        note.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px; -fx-background-color: #1a1a1a; -fx-padding: 10 14; -fx-background-radius: 4;");
        note.setWrapText(true);

        rightPanel.getChildren().addAll(welcome, sub, emailBox, passBox, errorLbl, loginBtn, note);
        VBox.setMargin(welcome, new Insets(0, 0, -8, 0));

        // Action
        loginBtn.setOnAction(e -> handleLogin(emailField.getText(), passField.getText(), errorLbl));
        passField.setOnAction(e -> handleLogin(emailField.getText(), passField.getText(), errorLbl));

        /* ── ROOT ── */
        HBox root = new HBox();
        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        root.getChildren().addAll(leftPanel, rightPanel);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add("data:text/css," + Styles.BASE_CSS.replace(" ", "%20"));
        stage.setScene(scene);
        stage.show();
    }

    private void handleLogin(String email, String password, Label errorLbl) {
        if (email.isBlank() || password.isBlank()) {
            errorLbl.setText("Por favor ingresa tu correo y contraseña.");
            return;
        }
        User user = DataStore.authenticate(email.trim(), password);
        if (user == null) {
            errorLbl.setText("Correo o contraseña incorrectos.");
            return;
        }
        // Route by role
        if ("admin".equals(user.getRole())) {
            new AdminController(stage, user);
        } else {
            new ClientController(stage, user);
        }
    }

    /** Renders "SOL<E_yellow>" */
    private Label buildLogo(String size) {
        Label lbl = new Label("SOLE");
        lbl.setStyle("-fx-text-fill: #ffffff; -fx-font-size: " + size + "; -fx-font-weight: bold;");
        return lbl;
    }
}
