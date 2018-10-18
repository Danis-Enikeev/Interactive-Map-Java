package Controller;

import Model.Squares;
import View.LoginScreen;
import View.PannableCanvas;
import View.SceneGestures;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

public class ZoomAndScrollApplication extends Application {
    public static void setSquares(){
        File file = new File("base.txt");
        Squares sq = new Squares(file);
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Config config = new Config("config");
        for (HashMap.Entry<String, String> pair : config.getSettings().entrySet()) {
            System.out.println(pair.getKey() + " : " + pair.getValue());
        }
        stage.setTitle("Login page");
        LoginScreen loginScreen = new LoginScreen(config);
        Scene scene = new Scene(loginScreen, 300, 275);
        stage.setScene(scene);
        stage.show();

        File file = new File("base.txt");
        Squares sq = new Squares(file);
        Group group = new Group();
        PannableCanvas canvas = new PannableCanvas();
        group.getChildren().add(canvas);
        scene = new Scene(group, 1280, 720);
        SceneGestures sceneGestures = new SceneGestures(canvas);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        stage.setScene(scene);
        stage.show();
        canvas.ResizeGrid(1);


    }
}