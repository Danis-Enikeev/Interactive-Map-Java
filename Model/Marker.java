package Model;

public class Marker {
    private String Type;
    private int Percentage;

    public Marker(String type, int percentage) {
        this.Type = type;
        this.Percentage = percentage;
    }

    public Marker(String type) {
        this.Type = type;
    }

    public Marker(Marker copy) {
        this(copy.Type, copy.Percentage);
    }

    public int getPercentage() {
        return this.Percentage;
    }

    public void setPercentage(int percentage) {
        this.Percentage = percentage;
    }

    public String getType() {
        return this.Type;
    }

    public void setType(String type) {
        this.Type = type;
    }
}
