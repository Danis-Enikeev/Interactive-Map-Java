package Controller;

import Model.Squares;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;

import static View.MapPreloader.getMap;

public class FileQueue extends Thread {
    private int start;
    private int end;
    private ArrayList<ArrayList<ImageView>> mapImageList;
    private File[] listOfFiles;

    public FileQueue(int start, int end, ArrayList<ArrayList<ImageView>> mapImageList, File[] listOfFiles) {
        this.start = start;
        this.end = end;
        this.mapImageList = mapImageList;
        this.listOfFiles = listOfFiles;
    }

    @Override
    public void run() {
        Squares squares;
        int offset = 2;

        try {
            for (int j = start; j <= end; j++) {

                squares = new Squares(listOfFiles[j]);
                ArrayList<ImageView> mapImageListBuff = new ArrayList<>(0);
                for (int i = offset; i <= 32; i *= 2) {
                    mapImageListBuff.add(getMap(squares, i));
                }
                mapImageList.add(mapImageListBuff);


            }
        } catch (java.lang.NullPointerException e) {
            System.err.println("Error no files found in Data directory");
        }
    }


}
