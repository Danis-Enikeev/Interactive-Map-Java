package View;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.io.File;
import java.util.Random;
import java.nio.IntBuffer;

public class PannableCanvas extends Pane {

    DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    double GridVal = 1;
    private int width = 2048 * 2;
    private int height = 1024 * 2;
    private final WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();

    public PannableCanvas() {
        setPrefSize(width, height);
        setStyle("-fx-background-color: white; -fx-border-color: blue;");
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
    }

    /**
     * Add a grid to the canvas
     */
    public void ResizeGrid(double resizeVal) {

        getChildren().clear();
        Image image = new Image(new File("View/map.png").toURI().toString());
        ImageView imageView = new ImageView(image);
        getChildren().add(imageView);
        int w = width;
        int h = height;
        GridVal *= resizeVal;
        double offset = 16 * GridVal;
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
        int[] buffer = new int[w * h];
        for (int i = 0; i < w; i += offset) {
            for (int j = 0; j < h; j += offset) {
                Random rand = new Random();
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
                int colorCode = toInt(Color.color(r, g, b, 0.6));
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
                ((int) (c.getOpacity() * 255) << 24) |
                        ((int) (c.getRed() * 255) << 16) |
                        ((int) (c.getGreen() * 255) << 8) |
                        ((int) (c.getBlue() * 255));
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
