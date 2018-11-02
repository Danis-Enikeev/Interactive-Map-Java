package View;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
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


        Slider slider = new Slider(0, mapImageList.size() - 1, 1);
        slider.setValue(0);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        group.getChildren().add(slider);
        Scene scene = new Scene(group, 1280, 720);
        SceneGestures sceneGestures = new SceneGestures(canvas, slider);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                canvas.ResizeGrid(0, (int) slider.getValue());
            }
        });

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        canvas.ResizeGrid(0, 0);
        mapStage.setScene(scene);
        mapStage.setTitle("Map");
        mapStage.show();
        mapStage.setFullScreen(true);
    }
}
