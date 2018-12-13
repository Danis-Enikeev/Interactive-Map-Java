package Controller;

import View.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ZoomAndScrollApplication extends Application {
    private static Stage GUIStage;

    public static Stage getStage() {
        return GUIStage;
    }


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {
        //Platform.setImplicitExit(false);
        GUIStage = stage;
        Config config = new Config("config");
        stage.setTitle("Login page");
        LoginScreen loginScreen = new LoginScreen(config);
        Scene scene = new Scene(loginScreen, 300, 275);
        stage.setScene(scene);
        stage.show();

    }
}