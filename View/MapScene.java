package View;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MapScene extends Application {
    private Stage mapStage;
    private ArrayList<ArrayList<ImageView>> mapImageList;

    public MapScene(ArrayList<ArrayList<ImageView>> MapImageList) {
        mapImageList = MapImageList;
    }

    @Override
    public void start(Stage primaryStage) {
        this.mapStage = primaryStage;
        Group group = new Group();
        PannableCanvas canvas = new PannableCanvas(mapImageList);
        group.getChildren().add(canvas);
        Scene scene = new Scene(group, 1280, 720);
        SceneGestures sceneGestures = new SceneGestures(canvas);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        canvas.ResizeGrid(0);
        mapStage.setScene(scene);
        mapStage.show();
        mapStage.setFullScreen(true);
    }
}
