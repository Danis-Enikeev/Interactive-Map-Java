package View;

import Controller.Config;
import Controller.FileQueue;
import Model.Square;
import Model.Squares;
import javafx.application.Preloader;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;


public class MapPreloader extends Preloader {
    private static ArrayList<Squares> squaresList = new ArrayList<>(0);
    private static final WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
    private static int w = 1024 * 4;
    private static int h = 1024 * 2;
    private ArrayList<ArrayList<ImageView>> mapImageList;
    private Stage preloaderStage;
    private File[] listOfFiles;
    private ArrayList<ArrayList<ImageView>> mapImageList1;


    public void setSquares() {
        File folder = new File("Data");
        listOfFiles = folder.listFiles();

        if (listOfFiles.length >= 2) {
            mapImageList = new ArrayList<ArrayList<ImageView>>(listOfFiles.length / 2);
            mapImageList1 = new ArrayList<ArrayList<ImageView>>(listOfFiles.length - listOfFiles.length / 2);

            FileQueue fileTask1 = new FileQueue(0, listOfFiles.length / 2 - 1, mapImageList, listOfFiles);
            FileQueue fileTask2 = new FileQueue(listOfFiles.length / 2, listOfFiles.length - 1, mapImageList1, listOfFiles);
            fileTask1.start();
            fileTask2.start();
            while (fileTask1.getState() != Thread.State.TERMINATED || fileTask2.getState() != Thread.State.TERMINATED) {
            }
            mapImageList.addAll(mapImageList1);

        } else {
            mapImageList = new ArrayList<ArrayList<ImageView>>(listOfFiles.length);

            FileQueue fileTask1 = new FileQueue(0, listOfFiles.length - 1, mapImageList, listOfFiles);
            fileTask1.start();
            while (fileTask1.getState() != Thread.State.TERMINATED) {
            }
        }

    }

    @Override
    public void start(Stage primaryStage) {


        this.preloaderStage = primaryStage;
        ProgressBar bar = new ProgressBar();
        BorderPane p = new BorderPane();
        p.setCenter(bar);

        preloaderStage.setScene(new Scene(p, 300, 150));
        preloaderStage.setTitle("Please wait...");

        preloaderStage.show();

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            public Boolean call() {

                Boolean result = mapCompute();
                return result;
            }
        };

        task.setOnSucceeded(e -> {
            hidestage();
        });

        new Thread(task).start();


    }

    public void hidestage() {
        preloaderStage.hide();
        MapScene mapScene = new MapScene(mapImageList);
        mapScene.start(preloaderStage);
    }

    private int toInt(Color c) {
        return
                ((int) (c.getOpacity() * 255) << 24) |
                        ((int) (c.getRed() * 255) << 16) |
                        ((int) (c.getGreen() * 255) << 8) |
                        ((int) (c.getBlue() * 255));
    }

    public Boolean mapCompute() {
        mapImageList = new ArrayList<>(0);
        if (Config.getSettings().get("Autotests").equals("0")) {
            setSquares();
        } else {
            int offset = 2;
            for (int i = 0; i < 10; i++) {
                ArrayList<ImageView> mapImageListBuff = new ArrayList<>(0);

                for (int j = offset; j <= 32; j *= 2) {
                    mapImageListBuff.add(getMap(j));
                }
                mapImageList.add(mapImageListBuff);
            }

        }
        return Boolean.TRUE;
    }

    private static int toInt(Square s) {
        int r, g, b, o;
        try {
            r = s.getMarkerList().get(0).getPercentage() * 255 / 100;
        } catch (Exception e) {
            r = 0;
        }
        try {
            g = s.getMarkerList().get(1).getPercentage() * 255 / 100;
        } catch (Exception e) {
            g = 0;
        }
        try {
            b = s.getMarkerList().get(3).getPercentage() * 255 / 100;
        } catch (Exception e) {
            b = 0;
        }
        o = r + g + b;
        return
                (o << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Возвращает ImageView с картой всех квадратов
     *
     * @param squares - экземпляр класса Squares, для которого нужно построить карту
     * @param offset  - количество пикселей в одном квадрате
     * @return ImageView с картой всех квадратов
     */
    public static ImageView getMap(Squares squares, int offset) {
        WritableImage imageLayer = new WritableImage(w, h);
        PixelWriter p = imageLayer.getPixelWriter();
        int colorCode;
        int[] buffer = new int[w * h];
        for (int i = 0; i < w; i += offset) {
            for (int j = 0; j < h; j += offset) {

                Square square = squares.getSquares(w / offset)[j / offset][i / offset];
                colorCode = toInt(square);
                for (int dx = 0; dx < offset; dx++) {
                    for (int dy = 0; dy < offset; dy++) {
                        buffer[i + dx + w * (j + dy)] = colorCode;
                    }
                }

            }
        }
        p.setPixels(0, 0, w, h, pixelFormat, buffer, 0, w);
        return new ImageView(imageLayer);
    }

    /**
     * Возвращает ImageView с картой всех квадратов, раскрашенных случайным образом
     *
     * @param offset - количество пикселей в одном квадрате
     * @return ImageView с картой всех квадратов
     */
    public ImageView getMap(int offset) {
        WritableImage imageLayer = new WritableImage(w, h);
        PixelWriter p = imageLayer.getPixelWriter();
        int colorCode;
        int[] buffer = new int[w * h];
        for (int i = 0; i < w; i += offset) {
            for (int j = 0; j < h; j += offset) {
                Random rand = new Random();
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
                float o = rand.nextFloat();
                colorCode = toInt(Color.color(r, g, b, o));

                for (int dx = 0; dx < offset; dx++) {
                    for (int dy = 0; dy < offset; dy++) {
                        buffer[i + dx + w * (j + dy)] = colorCode;
                    }
                }
            }
        }
        p.setPixels(0, 0, w, h, pixelFormat, buffer, 0, w);
        ImageView dataLayer = new ImageView(imageLayer);
        return dataLayer;
    }

}