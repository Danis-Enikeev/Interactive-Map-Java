package Model;

import com.sun.imageio.plugins.png.PNGImageReader;
import com.sun.imageio.plugins.png.PNGImageReaderSpi;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Squares {
    private Square[][] squares0 = new Square[1024][2048]; // самый приближеннный маштаб. Остальные массивы по тому же принципу
    private Square[][] squares1 = new Square[512][1024];
    private Square[][] squares2 = new Square[256][512];
    private Square[][] squares3 = new Square[128][256];
    private Square[][] squares4 = new Square[64][128];
    private Date date;
    private int[] pixels;
    private int height;
    private int width;

    public Squares(File file) {
        try {

            readImageFromFile("View/map.png"); //считали файл карты и разобрали на пиксели

        } catch (IOException e) {
            System.err.println("Error reading map file");
        }

        SqInit(); // инициализируем массивы
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM yyyy"); // указываем формат даты
        String mainBuf; // строка в которую постоянно будем загружать посторчно данные из файла
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            mainBuf = r.readLine(); // считываем дату
            this.date = dateFormat.parse(mainBuf);
            Position bufPos = new Position(0, 0);
            Marker bufMar = new Marker("", 0);  // промежуточные переменные для сохранения выгруженных данных
            ArrayList<Marker> bufArMar = new ArrayList<Marker>(0);
            MeasModel buf;
            int MainIter = 0, SubIter = 0; // количество measmodels и количество в каждом из них маркеров
            mainBuf = r.readLine();
            MainIter = Integer.parseInt(mainBuf);
            for (int i = 0; i < MainIter; i++) {// используем for так как заранее знаем количество итераций
                mainBuf = r.readLine();
                bufPos.setY(Integer.parseInt(mainBuf));
                mainBuf = r.readLine();
                bufPos.setX(Integer.parseInt(mainBuf));
                mainBuf = r.readLine();
                SubIter = Integer.parseInt(mainBuf);
                for (int j = 0; j < SubIter; j++) {
                    mainBuf = r.readLine();
                    bufMar.setType(mainBuf);
                    mainBuf = r.readLine();
                    bufMar.setPercentage(Integer.parseInt(mainBuf));
                    bufArMar.add(new Marker(bufMar));
                }
                buf = new MeasModel(new Position(bufPos), bufArMar);
                //Валидация маркера по карте
                try {
                    if (pixels[bufPos.getY() * width + bufPos.getX()] != 0xffffff && pixels[bufPos.getY() * width + bufPos.getX()] != 0x000000) {
                        squares0[bufPos.getY() / 2][bufPos.getX() / 2].AddMeasModel(buf.getMarkerList());
                    }
                } catch (ArrayIndexOutOfBoundsException E) {
                }
                bufArMar = new ArrayList<Marker>(0);


            }
        } catch (IOException | ParseException e) {
            System.err.println("Error reading file with measurement data");
        }

        // считали все маркеры и поместили их в квадраты
        Square[][] bufSq = new Square[2][2]; // промежуточный массив квадратов 2*2
        for (int i = 0; i < 1024; i += 2)
            for (int j = 0; j < 2048; j += 2) {
                bufSq[0][0] = squares0[i][j];
                bufSq[0][1] = squares0[i][j + 1];
                bufSq[1][0] = squares0[i + 1][j];
                bufSq[1][1] = squares0[i + 1][j + 1];
                squares1[i / 2][j / 2] = new Square(bufSq);
            }//Уменьшили масштаб
        for (int i = 0; i < 512; i += 2)
            for (int j = 0; j < 1024; j += 2) {
                bufSq[0][0] = new Square(squares1[i][j]);
                bufSq[0][1] = new Square(squares1[i][j + 1]);
                bufSq[1][0] = new Square(squares1[i + 1][j]);
                bufSq[1][1] = new Square(squares1[i + 1][j + 1]);
                squares2[i / 2][j / 2] = new Square(bufSq);
            }
        for (int i = 0; i < 256; i += 2)
            for (int j = 0; j < 512; j += 2) {
                bufSq[0][0] = squares2[i][j];
                bufSq[0][1] = squares2[i][j + 1];
                bufSq[1][0] = squares2[i + 1][j];
                bufSq[1][1] = squares2[i + 1][j + 1];
                squares3[i / 2][j / 2] = new Square(bufSq);
            }
        for (int i = 0; i < 128; i += 2)
            for (int j = 0; j < 256; j += 2) {
                bufSq[0][0] = squares3[i][j];
                bufSq[0][1] = squares3[i][j + 1];
                bufSq[1][0] = squares3[i + 1][j];
                bufSq[1][1] = squares3[i + 1][j + 1];
                squares4[i / 2][j / 2] = new Square(bufSq);
            }
    }

    public Square[][] getSquares(int size) {
        switch (size) {
            case 2048:
                return squares0;
            case 1024:
                return squares1;
            case 512:
                return squares2;
            case 256:
                return squares3;
            case 128:
                return squares4;
            default:
                System.err.println("Error: wrong square size");
                return squares4;
        }
    }

    public Date getDate() {
        return date;
    }

    private void SqInit() { // метод инициализируещий все массивы squares(i)
        for (int i = 0; i < 1024; i++)
            for (int j = 0; j < 2048; j++)
                this.squares0[i][j] = new Square(new Position(j * 2, i * 2), new Position(j * 2 + 2, i * 2 + 2));// сразу присваиваем всем элементам координаты
        for (int i = 0; i < 512; i++)
            for (int j = 0; j < 1024; j++)
                this.squares1[i][j] = new Square(new Position(j * 4, i * 4), new Position(j * 4 + 4, i * 4 + 4));
        for (int i = 0; i < 256; i++)
            for (int j = 0; j < 512; j++)
                this.squares2[i][j] = new Square(new Position(j * 8, i * 8), new Position(j * 8 + 8, i * 8 + 8));
        for (int i = 0; i < 128; i++)
            for (int j = 0; j < 256; j++)
                this.squares3[i][j] = new Square(new Position(j * 16, i * 16), new Position(j * 16 + 16, i * 16 + 16));
        for (int i = 0; i < 64; i++)
            for (int j = 0; j < 128; j++)
                this.squares3[i][j] = new Square(new Position(j * 32, i * 32), new Position(j * 32 + 32, i * 32 + 32));
    }

    private void readImageFromFile(String fileName) throws IOException {

        ImageReader r = new PNGImageReader(new PNGImageReaderSpi());
        r.setInput(new FileImageInputStream(new File(fileName)));
        BufferedImage bi = r.read(0, new ImageReadParam());
        ((FileImageInputStream) r.getInput()).close();


        this.height = bi.getHeight();
        this.width = bi.getWidth();

        this.pixels = new int[height * width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                pixels[i * width + j] = bi.getRGB(j, i) & 0xFFFFFF; // 0xFFFFFF: записываем только 3 младших байта RGB


    }
}
