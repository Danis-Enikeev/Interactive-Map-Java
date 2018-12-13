package View;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
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
        Menu menuFile = new Menu("File");
        MenuBar menuBar = new MenuBar();
        Menu menuAbout = new Menu("About");
        MenuItem fileFolder = new MenuItem("Choose the folder with files");
        fileFolder.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File dir = directoryChooser.showDialog(primaryStage);
                if (dir != null) {
                    MapPreloader.fileDir = dir.getAbsolutePath();
                }
            }
        });
        MenuItem ourTeam = new MenuItem("Our team");

        ourTeam.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Label secondLabel = new Label("Our team: \n\tАбаськин К.\n\tЕникеев Д.\n\tМарандюк А.");

                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);

                Scene secondScene = new Scene(secondaryLayout, 230, 100);
                Stage newWindow = new Stage();
                newWindow.setTitle("Info");
                newWindow.setScene(secondScene);
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                newWindow.show();
            }
        });
        menuAbout.getItems().addAll(ourTeam);
        menuBar.getMenus().addAll(menuFile, menuAbout);
        menuFile.getItems().addAll(fileFolder);
        group.getChildren().add(menuBar);

        Button reloadButton = new Button();
        reloadButton.setTranslateY(100);
        Image image = new Image(new File("View/reload_icon.png").toURI().toString());
        reloadButton.setGraphic(new ImageView(image));
        reloadButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mapStage.hide();
                MapPreloader preloader = new MapPreloader();
                preloader.start(mapStage);
            }
        });

        group.getChildren().add(reloadButton);
        Slider slider = new Slider(0, mapImageList.size() - 1, 1);
        slider.setTranslateY(50);
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
    }
}
