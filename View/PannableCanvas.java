package View;

import Controller.Config;
import Controller.ZoomAndScrollApplication;
import Model.Square;
import Model.Squares;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.nio.IntBuffer;

import static Controller.ZoomAndScrollApplication.getStage;

public class PannableCanvas extends Pane {

    DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    double GridVal = 1;
    private int width = 1024*4;
    private int height = 512*4;
    private final WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
    private ArrayList<Squares> squaresList;
    public PannableCanvas() {
        setPrefSize(width, height);
        if (Config.getSettings().get("Autotests").equals("0")) {
            this.squaresList = ZoomAndScrollApplication.setSquares();
        }
        setStyle("-fx-background-color: white; -fx-border-color: blue;");
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
        Image image = new Image(new File("View/map.png").toURI().toString());
        ImageView imageView = new ImageView(image);
        getChildren().add(imageView);

    }


    public void ResizeGrid(double resizeVal) {
        try {
            getChildren().remove(1);
        }catch (Exception e){}
        int w = width;
        int h = height;
        GridVal *= resizeVal;
        int offset = (int)(16 * GridVal);
        //Group group = new Group();
       /* for (int i = 0; i < w; i += offset) {
            for (int j = 0; j < h; j += offset) {
                Rectangle rect1 = new Rectangle(offset, offset);
                rect1.setTranslateX(i);
                rect1.setTranslateY(j);
                group.getChildren().add(rect1);
            }
        }*/
        //getChildren().add(group);
        WritableImage imageLayer = new WritableImage(w, h);
        PixelWriter p = imageLayer.getPixelWriter();
        int colorCode;
        int[] buffer = new int[w * h];
        for (int i = 0; i < w; i += offset) {
            for (int j = 0; j < h; j += offset) {
                if (Config.getSettings().get("Autotests").equals("1")){
                    Random rand = new Random();
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    float o = rand.nextFloat();
                    colorCode = toInt(Color.color(r, g, b, 0.4));
                }
                else {

                    Square square = squaresList.get(0).getSquares(w/offset)[j/offset][i/offset];
                    colorCode = toInt(square);
                }
                for (int dx = 0; dx < offset; dx++) {
                    for (int dy = 0; dy < offset; dy++) {
                        buffer[i + dx + w * (j + dy)] = colorCode;
                    }
                }

            }
        }

        p.setPixels(0, 0, w, h, pixelFormat, buffer, 0, w);
        ImageView dataLayer = new ImageView(imageLayer);
        getChildren().add(dataLayer);
    }

    private int toInt(Color c) {
        return
                ( (int)(c.getOpacity()*255 ) << 24) |
                        ((int) (c.getRed() * 255) << 16) |
                        ((int) (c.getGreen() * 255) << 8) |
                        ((int) (c.getBlue() * 255));
    }
    private int toInt(Square s) {
        int r, g, b, o;
        try {
            r = s.getMarkerList().get(0).getPercentage() * 255 / 100;
        }catch(Exception e){
            r = 0;
        }
        try {
             g = s.getMarkerList().get(1).getPercentage() * 255 / 100;
        }catch(Exception e){
             g = 0;
        }
        try{
         b = s.getMarkerList().get(3).getPercentage()*255/100;
        }catch(Exception e){
             b = 0;
        }
         o = r+g+b;
        return
                (o << 24) | (r << 16) | (g << 8) | b;
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
