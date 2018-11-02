package View;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.ArrayList;

public class PannableCanvas extends Pane {

    DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    int GridVal = 0;
    ImageView imageView;
    private int width = 1024 * 4;
    private int height = 1024 * 2;
    private ArrayList<ArrayList<ImageView>> mapImageList;

    public PannableCanvas(ArrayList<ArrayList<ImageView>> mapImageList) {
        this.mapImageList = mapImageList;
        setPrefSize(width, height);
        setStyle("-fx-background-color: white; -fx-border-color: blue;");
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
        Image image = new Image(new File("View/map.png").toURI().toString());
        imageView = new ImageView(image);

    }


    public void ResizeGrid(int resizeVal, int timeVal) {


        getChildren().clear();
        getChildren().add(imageView);

        GridVal += resizeVal;

        getChildren().add(mapImageList.get(timeVal).get(3 + GridVal));

    }


    public double getScale() {
        return myScale.get();
    }

    public void setScale(double scale) {
        myScale.set(scale);
    }

    public void setPivot(double x, double y) {
        setTranslateX(getTranslateX() - x);
        setTranslateY(getTranslateY() - y);
    }
}
