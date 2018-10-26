package View;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.Pane;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class PannableCanvas extends Pane {

    private final WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
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


    public void ResizeGrid(int resizeVal) {


        getChildren().clear();
        getChildren().add(imageView);


//        int w = width;
//        int h = height;
        GridVal += resizeVal;

//        //Group group = new Group();
//       /* for (int i = 0; i < w; i += offset) {
//            for (int j = 0; j < h; j += offset) {
//                Rectangle rect1 = new Rectangle(offset, offset);
//                rect1.setTranslateX(i);
//                rect1.setTranslateY(j);
//                group.getChildren().add(rect1);
//            }
//        }*/
//        //getChildren().add(group);
//        WritableImage imageLayer = new WritableImage(w, h);
//        PixelWriter p = imageLayer.getPixelWriter();
//        int colorCode;
//        int[] buffer = new int[w * h];
//        for (int i = 0; i < w; i += offset) {
//            for (int j = 0; j < h; j += offset) {
//                if (Config.getSettings().get("Autotests").equals("1")){
//                    Random rand = new Random();
//                    float r = rand.nextFloat();
//                    float g = rand.nextFloat();
//                    float b = rand.nextFloat();
//                    float o = rand.nextFloat();
//                    colorCode = toInt(Color.color(r, g, b, 0.4));
//                }
//                else {
//
//                    Square square = squaresList.get(0).getSquares(w/offset)[j/offset][i/offset];
//                    colorCode = toInt(square);
//                }
//                for (int dx = 0; dx < offset; dx++) {
//                    for (int dy = 0; dy < offset; dy++) {
//                        buffer[i + dx + w * (j + dy)] = colorCode;
//                    }
//                }
//
//            }
//        }
//
//        p.setPixels(0, 0, w, h, pixelFormat, buffer, 0, w);
//        ImageView dataLayer = new ImageView(imageLayer);
        getChildren().add(mapImageList.get(0).get(3 + GridVal));

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
