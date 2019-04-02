package weather.widget.Database;

import android.util.Log;
import weather.widget.DataManager.DataContainer;


public class DatabaseManager  {
    private static final DatabaseManager ourInstance = new DatabaseManager();
    private Database database = new Database();
    public static DatabaseManager getInstance() {
        return ourInstance;
    }

    private DatabaseManager() {

    }




    public void getValues(){
            Thread valueQueryThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try  {
                        for(int i = 0; i < 5; i++){
                            if(database.connect()){
                                Thread.sleep(5);
                                    if(!database.getData()){
                                        Log.e("Appw","Nem sikerült az adatlekérdezés! " + i);
                                        Thread.sleep(1000);
                                    }
                                    database.disconnect();
                                    break;
                            }else{
                                Log.e("Appw","Nem sikerült a kapcsolódás az adatbázishoz!");
                                database.disconnect();}
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            valueQueryThread.start();
    }



    public void getValues(final String name){
        Thread valuesQueryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    if(database.connect()){
                        if(database.getValues(name)){
                            //Log.e("Appw"," query finished " + DataContainer.getInstance().getPointsSize());
                            DataContainer.getInstance().alertGraphicon();
                        }
                        database.disconnect();
                    }else{
                        Log.e("Database","Database connection fail");
                        database.disconnect();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        valuesQueryThread.start();
    }


    public void getStations(){
        Thread stationQueryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    if(database.connect()){
                        if(!database.getStations()){
                            Log.e("Appw","Nem sikerült az állomások lekérdezése!");
                        }
                        database.disconnect();
                    }else {
                        Log.e("Appw","Nem sikerült az állomások lekérdezése!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        stationQueryThread.start();
    }

}
