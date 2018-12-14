package View;

import Controller.Config;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static View.MapPreloader.*;

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

        Canvas leftCanvas = new Canvas(150, 500);
        GraphicsContext gc = leftCanvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, 150, 500);
        group.getChildren().add(leftCanvas);


        String[] colors = {"red", "green", "blue", "violet", "yellow", "orange", "cyan", "none"};
        int n = TypeColor.size();
        Label[] markerName = new Label[n];
        ComboBox[] comboBox = new ComboBox[n];
        for (int i = 0; i < n; i++) {
            comboBox[i] = new ComboBox();
            comboBox[i].getItems().add(TypeColor.values().toArray()[i]);
            comboBox[i].getSelectionModel().select(0);

            for (int j = 0; j < 8; j++) {
                if (!comboBox[i].getValue().equals(colors[j])) {
                    comboBox[i].getItems().add(colors[j]);
                }
            }
            comboBox[i].setTranslateX(60);
            comboBox[i].setTranslateY(160 + i * 30);
            markerName[i] = new Label((String) TypeColor.keySet().toArray()[i]);
            markerName[i].setTranslateX(5);
            markerName[i].setTranslateY(160 + i * 30);

            group.getChildren().addAll(comboBox[i], markerName[i]);

        }


        Menu menuFile = new Menu("File");
        MenuBar menuBar = new MenuBar();
        Menu menuAbout = new Menu("About");

        MenuItem fileFolder = new MenuItem("Choose the folder with files");
        if (Config.getSettings().get("Developer mode").equals("0")) {
            fileFolder.setDisable(true);
        }
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
                BufferedWriter bw;
                for (int i = 0; i < n; i++) {
                    TypeColor.put((String) TypeColor.keySet().toArray()[i], (String) comboBox[i].getValue());
                }
                try {
                    bw = new BufferedWriter(new FileWriter(fileDir + infoFile));
                    try {
                        for (HashMap.Entry<String, String> pair : TypeColor.entrySet()) {
                            bw.write(pair.getKey() + ":" + pair.getValue() + "\r\n");
                        }
                        bw.close();
                    } catch (Exception e) {
                        System.err.println("Error writing to the color info file");
                    }
                } catch (IOException e) {
                    System.err.println("Error opening the color info file for writing");
                }
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
