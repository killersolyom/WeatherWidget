package weather.widget.DataManager;

import com.jjoe64.graphview.series.DataPoint;
import java.util.ArrayList;

import weather.widget.Interfaces.IStationsChangeListener;
import weather.widget.Interfaces.IUpdateListener;
import weather.widget.Interfaces.IValuesChangeListener;
import weather.widget.Interfaces.IValuesChangeListenerForService;
import weather.widget.Interfaces.IViewPagerListener;

public class DataContainer {
    ///Adat tagok
    private String temperature = "0";
    private String humidity = "0";
    private String pressure = "0";
    private String uvLevel = "0";
    private String rainlevel = "0";
    private String windSpeed = "0";
    private String windDirection = "0";
    ///Tárolók
    private ArrayList<String> stations = new ArrayList<>();
    private ArrayList<DataPoint> points = new ArrayList<>();
    ///Külső tárhely
    private InternalStorageManager manager = new InternalStorageManager();
    ///Interface-k
    private IUpdateListener updateListener;
    private IValuesChangeListener valueChangeListener;
    private IStationsChangeListener stationChangeListener;
    private IValuesChangeListenerForService serviceChangeListener;
    private IViewPagerListener viewPagerListener;

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
    ///View pager-t értesítő interface
    public void setListener(IViewPagerListener ch){this.viewPagerListener=ch;}

    private static final DataContainer ourInstance = new DataContainer();
    public static DataContainer getInstance() {
        return ourInstance;
    }

    private DataContainer() {
    }


    public String getStationName() {
        if(manager.isExist()){
            return manager.readData();
        }
        return "none";
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getRainlevel() {
        return rainlevel;
    }

    public String getUvLevel() {
        return uvLevel;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public ArrayList<String> getStations() { return stations; }

    public void setStations(ArrayList<String> st) {
        this.stations.clear();
        this.stations.addAll(st);
        stationChangeListener.change(true);
        viewPagerListener.change(true);
    }
    public void clearStations(){
        this.stations.clear();
    }

    public void setStationName(String stationName) {
        manager.writeData(stationName);
    }

    public void setAllValue(String value){
        temperature = value;
        humidity = value;
        pressure = value;
        uvLevel = value;
        rainlevel = value;
        windSpeed = value;
        windDirection = "Nincs";
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

    public void clearPoints(){
        points.clear();
        updateListener.newValue(true);
    }

    public void setAllValues(String temp, String hum, String pres, String uv, String rain, String windsp, String winddir){
        temperature = temp;
        humidity = hum;
        pressure = pres;
        uvLevel = uv;
        rainlevel = rain;
        windSpeed = windsp;
        windDirection = convertPositionToDirection(winddir);
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

    private void notifyDataChange(){
        valueChangeListener.change(true);
        serviceChangeListener.change(true);
        viewPagerListener.change(true);
    }

    public ArrayList<String> getAllValues(){
        ArrayList values = new ArrayList<>();
        values.add(temperature);
        values.add(humidity);
        values.add(pressure);
        values.add(rainlevel);
        values.add(uvLevel);
        values.add(windDirection);
        values.add(windSpeed);
        return values;
    }
}
