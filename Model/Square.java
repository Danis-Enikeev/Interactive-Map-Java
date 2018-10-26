package Model;

import java.util.ArrayList;

public class Square {

    private Position LeftUp;
    private Position RightDown;
    private ArrayList<Marker> MarkerList;

    public Square(Position leftUp, Position rightDown) {
        this.LeftUp = leftUp;
        this.RightDown = rightDown;
        this.MarkerList = new ArrayList<Marker>(0);
    }

    public Square(ArrayList<Square> squares) {
        this.RightDown.setY(0);
        this.RightDown.setX(0);
        this.MarkerList = new ArrayList<Marker>(0);
        for (Square square : squares) {
            if ((square.getLeftUp().getX() <= this.LeftUp.getX()) && (square.getLeftUp().getY() <= this.LeftUp.getY())) {
                this.LeftUp.setX(square.getLeftUp().getX());
                this.LeftUp.setY(square.getLeftUp().getY());
            }
            if ((square.getRightDown().getX() >= this.RightDown.getX()) && (square.getRightDown().getY() >= this.RightDown.getY())) {
                this.RightDown.setX(square.getRightDown().getX());
                this.RightDown.setY(square.getRightDown().getY());
            }
            this.AddMeasModel(new MeasModel(square.getRightDown(), square.getMarkerList()));

        }

    }

    public Square(Square[][] squares) {
        this.RightDown = squares[1][1].getRightDown();
        this.LeftUp = squares[0][0].getLeftUp();
        this.MarkerList = new ArrayList<Marker>(0);
        Square.AddMeasModel(squares, this);
    }

    public Square(Square copy) {
        this.LeftUp = copy.LeftUp;
        this.RightDown = copy.RightDown;
        this.MarkerList = copy.MarkerList;
    }

    public static void AddMeasModel(Square[][] squares, Square sq) {
        boolean fl;
        for (int k = 0; k < 2; k++)
            for (int h = 0; h < 2; h++) {
                for (Marker marker : squares[k][h].getMarkerList()) {
                    fl = false;
                    for (int i = 0; i < sq.MarkerList.size(); i++) {
                        if (marker.getType().equals(sq.MarkerList.get(i).getType())) {
                            sq.MarkerList.get(i).setPercentage(sq.MarkerList.get(i).getPercentage() + marker.getPercentage());
                            fl = true;
                            break;
                        }
                    }
                    if (fl) continue;
                    else {
                        marker.setPercentage(marker.getPercentage());
                        sq.MarkerList.add(new Marker(marker));
                    }
                }
            }
        for (Marker marker : sq.getMarkerList()) {
            marker.setPercentage(marker.getPercentage() / 4);
        }
    }

    public void AddMeasModel(MeasModel measModel) {
        for (Marker marker : this.MarkerList)
            marker.setPercentage(marker.getPercentage() / 2); // удалил деление на два так как есть ошибка копирования
        boolean fl;
        for (Marker marker : measModel.getMarkerList()) {
            fl = false;
            for (int i = 0; i < this.MarkerList.size(); i++) {
                if (marker.getType().equals(this.MarkerList.get(i).getType())) {
                    this.MarkerList.get(i).setPercentage(this.MarkerList.get(i).getPercentage() + marker.getPercentage() / 2);
                    fl = true;
                    break;
                }
            }
            if (fl) continue;
            else {
                marker.setPercentage(marker.getPercentage() / 2);
                this.MarkerList.add(marker);
            }
        }

    }

    public Position getLeftUp() {
        return LeftUp;
    }

    public void setLeftUp(Position leftUp) {
        LeftUp = leftUp;
    }

    public Position getRightDown() {
        return RightDown;
    }

    public void setRightDown(Position rightDown) {
        RightDown = rightDown;
    }

    public ArrayList<Marker> getMarkerList() {
        return MarkerList;
    }

    public void setMarkerList(ArrayList<Marker> markerList) {
        MarkerList = markerList;
    }


}
