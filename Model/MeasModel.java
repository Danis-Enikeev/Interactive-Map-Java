package Model;
import java.util.ArrayList;

public class MeasModel {
   // private Date MeasDate;  //Date of measurement
    private Position Coordinates;
    private ArrayList<Marker> MarkerList;
    public MeasModel(Position coordinates, ArrayList<Marker> markerList) {
        //this.MeasDate = measDate;
        this.Coordinates = coordinates;
        this.MarkerList = markerList;
    }
    public MeasModel(MeasModel copy){
        this(copy.Coordinates,copy.MarkerList);
    }
    /*public Date getMeasDate() {
        return MeasDate;
    }*/

    public Position getCoordinates() {
        return Coordinates;
    }

    public ArrayList<Marker> getMarkerList() {
        return MarkerList;
    }
    /*public void setMeasDate(Date measDate) {
        MeasDate = measDate;
    }*/

    public void setCoordinates(Position coordinates) {
        Coordinates = coordinates;
    }

    public void setMarkerList(ArrayList<Marker> markerList) {
        MarkerList = markerList;
    }
}
