package weather.widget.DataManager;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import java.util.ArrayList;

import weather.widget.Interfaces.IStationsChangeListener;
import weather.widget.Interfaces.IUpdateListener;
import weather.widget.Interfaces.IValuesChangeListener;
import weather.widget.Interfaces.IValuesChangeListenerForService;

public class DataContainer {

    ///Tárolók
    private ArrayList<String> values = new ArrayList<>();
    private ArrayList<String> stations = new ArrayList<>();
    private ArrayList<DataPoint> points = new ArrayList<>();

    ///Külső tárhely
    private StorageManager manager = new StorageManager();

    ///Interface-k
    private IUpdateListener updateListener;
    private IValuesChangeListener valueChangeListener;
    private IStationsChangeListener stationChangeListener;
    private IValuesChangeListenerForService serviceChangeListener;

    /// Grafikon frissitő interface
    public void setListener(IUpdateListener ch){
        this.updateListener = ch;
    }
    /// Values frissitő interface
    public void setListener(IValuesChangeListener ch){
        this.valueChangeListener=ch;
    }
    /// Station frissitő interface
    public void setListener(IStationsChangeListener ch){
        this.stationChangeListener=ch;
    }
    ///Service-t értesítő interface
    public void setListener(IValuesChangeListenerForService ch){this.serviceChangeListener=ch;}

    private static final DataContainer ourInstance = new DataContainer();
    public static DataContainer getInstance() {
        return ourInstance;
    }

    private DataContainer() {
    }


    public String getStationName() {
        if(manager.isExistData("sName")){
            return manager.readData("sName");
        }
        return "none";
    }

    public String getTemperature() {
        if (manager.isExistData("sTemperature")){
            return manager.readData("sTemperature");
        }
        return "0";
    }

    public String getHumidity() {
        if (manager.isExistData("sHumidity")){
            return manager.readData("sHumidity");
        }
        return "0";
    }

    public String getRainlevel() {
        if (manager.isExistData("sRainLevel")){
            return manager.readData("sRainLevel");
        }
        return "0";
    }

    public String getUvLevel() {
        if (manager.isExistData("sUvLevel")){
            return manager.readData("sUvLevel");
        }
        return "0";
    }

    public String getWindSpeed() {
        if (manager.isExistData("sWindSpeed")){
            return manager.readData("sWindSpeed");
        }
        return "0";
    }

    public String getWindDirection() {
        if (manager.isExistData("sWindDirection")){
            return manager.readData("sWindDirection");
        }
        return "0";
    }

    public String getPressure() {
        if (manager.isExistData("sPressure")){
            return manager.readData("sPressure");
        }
        return "0";
    }

    public ArrayList<String> getAllValues(){
        values.clear();
        values.add(getTemperature());
        values.add(getHumidity());
        values.add(getPressure());
        values.add(getRainlevel());
        values.add(getUvLevel());
        values.add(getWindDirection());
        values.add(getWindSpeed());
        return values;
    }

    public ArrayList<String> getStations() { return stations; }

    public void setStations(ArrayList<String> st) {
        this.stations.clear();
        this.stations.addAll(st);
        stationChangeListener.change(true);
    }

    public void setStationName(String value) {
        manager.writeData(value,"sName");
    }

    private void setWindDirection(String value) {
        manager.writeData(value,"sWindDirection");
    }

    private void setTemperature(String value) {
        manager.writeData(value,"sTemperature");
    }

    private void setHumidity(String value) {
        manager.writeData(value,"sHumidity");

    }

    private void setRainLevel(String value) {
        manager.writeData(value,"sRainLevel");
    }

    private void setUvLevel(String value) {
        manager.writeData(value,"sUvLevel");
    }

    private void setWindSpeed(String value) {
        manager.writeData(value,"sWindSpeed");
    }

    private void setPressure(String value) {
        manager.writeData(value,"sPressure");
    }

    public ArrayList<DataPoint> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<DataPoint> points) {
        this.points = points;
    }

    public void alertGraphicon(){
        updateListener.newValue(true);
    }

    public int getPointsSize(){
      return points.size();
    }


    public void setAllValues(String temp, String hum, String press, String uv, String rain, String wSpeed, String wDirection){
        setTemperature(temp);
        setHumidity(hum);
        setPressure(press);
        setUvLevel(uv);
        setRainLevel(rain);
        setWindSpeed(wSpeed);
        setWindDirection(convertPositionToDirection(wDirection));
        notifyDataChange();
    }

    private String convertPositionToDirection(String number){
        try {
          int position = Integer.valueOf(number);
          if(position == 0){
              return "Észak-Kelet";
          }else if(position == 1){
              return "Kelet";
          }else if(position == 2){
              return "Dél-Kelet";
          }else if(position == 3){
              return "Dél";
          }else if(position == 4){
              return "Dél-Nyugat";
          }else if(position == 5){
              return "Nyugat";
          }else if(position == 6){
              return "Észak-Nyugat";
          }else if(position == 7){
              return "Észak";
          }else{
              return "Nincs";
          }
        }catch (Exception e){
            return "Ismeretlen";
        }
    }

    public void clearStations(){
        this.stations.clear();
    }

    public void clearPoints(){
        points.clear();
        updateListener.newValue(true);
    }

    private void notifyDataChange(){
        valueChangeListener.change(true);
        serviceChangeListener.change(true);
    }

}
