package weather.widget.Classes;

import android.graphics.drawable.Drawable;

public class StationValues {
    private String value = "0";
    private Drawable image;

    public StationValues() {
    }

    public StationValues(String value,Drawable image) {
        this.value = value;
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
