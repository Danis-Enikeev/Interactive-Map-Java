package Controller;

import Model.Square;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class ZoomAndScrollApplication extends Application {
    private static ArrayList<Squares> squaresList = new ArrayList<>(0);
    private static Stage GUIStage;
    public static Stage getStage() {
        return GUIStage;
    }

    public static ArrayList<Squares> setSquares(){
        File folder = new File("Data");
        File[] listOfFiles = folder.listFiles();
        try {
            for (File file : listOfFiles) {
                squaresList.add(new Squares(file));
            }
        }catch(java.lang.NullPointerException e){
            System.err.println("Error no files found in Data directory");
        }
        return squaresList;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void mapScene(){
        PannableCanvas canvas = new PannableCanvas();
        Scene scene = new Scene(canvas, 2048, 1024);
        SceneGestures sceneGestures = new SceneGestures(canvas);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        getStage().setScene(scene);
        getStage().show();

        canvas.ResizeGrid(1);
    }

    @Override
    public void start(Stage stage) {
        GUIStage = stage;
        Config config = new Config("config");
        /*for (HashMap.Entry<String, String> pair : Config.getSettings().entrySet()) {
            System.out.println(pair.getKey() + " : " + pair.getValue());
        }*/
        stage.setTitle("Login page");

        //mapScene();

        LoginScreen loginScreen = new LoginScreen(config);
        Scene scene = new Scene(loginScreen, 300, 275);
        stage.setScene(scene);
        stage.show();

    }
}