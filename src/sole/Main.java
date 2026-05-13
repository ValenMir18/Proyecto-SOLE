package sole;

import javafx.application.Application;
import javafx.stage.Stage;
import sole.controller.LoginController;

public class Main extends Application {


    public void start(Stage primaryStage) {
        primaryStage.setTitle("SOLE — Tienda de Tenis");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        new LoginController(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
