package weather.widget.Database;


import android.util.Log;
import com.jjoe64.graphview.series.DataPoint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import weather.widget.Classes.Clock;
import weather.widget.DataManager.DataContainer;

public class Database  {


    private static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    //private static final String DEFAULT_URL = "jdbc:oracle:thin:@192.168.43.109:1521:XE";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@192.168.0.64:1521:XE";
    private static String DEFAULT_USERNAME = "Weatherstation";
    private static String DEFAULT_PASSWORD = "sajt";
    private Connection connection = null;

    public Database() {
    }

    public boolean connect(){
        try {
            Class.forName(DEFAULT_DRIVER);
            connection = DriverManager.getConnection(DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
        } catch (Exception e) {
            Log.e("Database","Database connection fail");
            return false;
        }
        return test();
    }

    private boolean test(){
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT min(Main_ID) FROM Main_Table");
            rs.next();
            return true;
        } catch (SQLException e) {
           return false;
        }
    }

    public void disconnect(){
        try {
            connection.close();
            connection = null;
        } catch (SQLException ignored) {
        }
    }


    public boolean getData() {
        try {
            int id = getLastId();
            if(id == -1){
                DataContainer.getInstance().setAllValue("0");
                Log.e("query","no id found");
                return false;
            }
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select TEMPERATURE,PRESSURE,UV_INDEX,HUMIDITY,RAIN_LEVEL,WIND_SPEED,WIND_DIRECTION from MAIN_TABLE where Main_ID = " + id +" and STATION_NAME like " + "'"+ DataContainer.getInstance().getStationName() +"'");
            rs.next();
            DataContainer.getInstance().setAllValues(rs.getString("TEMPERATURE"), rs.getString("HUMIDITY"), rs.getString("PRESSURE"), rs.getString("UV_INDEX"), rs.getString("RAIN_LEVEL"), rs.getString("WIND_SPEED"), rs.getString("WIND_DIRECTION"));
            return true;
        } catch (SQLException e) {
            DataContainer.getInstance().setAllValue("0");
            return false;
        }
    }

    public boolean getValues(String data) {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select " + data + " from Main_Table where UPLOAD_DATE > " + Clock.getInstance().getElapsedHoursInTimeStamp() +" and STATION_NAME like " + "'"+ DataContainer.getInstance().getStationName().toString() +"'");
                String result = "";
                ArrayList<DataPoint> points = new ArrayList<>();
                DataContainer.getInstance().clearPoints();
                int index = 0;
                while (rs.next()) {
                    try {
                        //Date d  = Clock.getInstance().convertTimeStampToDate(Long.valueOf(rs.getString("UPLOAD_DATE")));
                        DataPoint point = new DataPoint(index,Double.parseDouble(rs.getString(data)));
                        points.add(point);
                        index++;
                    }catch (Exception e){
                        Log.e("Appw"," data query exception... " + data);
                    }
                }
                DataContainer.getInstance().setPoints(points);
                if (points.size()==0){
                    Log.e("Appw","");
                }
                return true;
            } catch (SQLException e) {
                return false;
            }
    }

    public boolean getStations() {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select Distinct STATION_NAME from MAIN_TABLE");
                ArrayList<String> stations = new ArrayList<>();
                while (rs.next()) {
                    stations.add(rs.getString("STATION_NAME"));
                    //DataContainer.getInstance().addStations(rs.getString("STATION_NAME"));
                }
                DataContainer.getInstance().setStations(stations);
                return true;
            } catch (SQLException e) {
                return false;
            }
    }


    private int getLastId() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(Main_ID) FROM Main_Table where STATION_NAME like " +  "'"+ DataContainer.getInstance().getStationName().toString() +"'");
            String result = "null";
            while (rs.next()) {
                result = rs.getString("MAX(MAIN_ID)");
            }
            if (result == null) {
                return -1;
            }
            return (Integer.parseInt(result));
        } catch (SQLException e) {
            return -1;
        }

    }

}
