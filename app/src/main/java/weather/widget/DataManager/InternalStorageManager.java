package weather.widget.DataManager;

import android.app.Application;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class InternalStorageManager {

    public InternalStorageManager() {
    }


    public void writeData(String data){
        File file = null;
        try {
            file = new File(getApplicationUsingReflection().getApplicationContext().getFilesDir(),"widgetData");
            if(!file.exists()){
                file.mkdir();
            }try{
                File gpxfile = new File(file, "widgetData");
                FileWriter writer = new FileWriter(gpxfile);
                writer.write(data);
                writer.flush();
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readData() {
        try {
            File file = new File(getApplicationUsingReflection().getApplicationContext().getFilesDir(),"widgetData");
            File gpxfile = new File(file, "widgetData");
            FileReader reader = new FileReader(gpxfile);
            char [] array = new char[35];
            for(int i = 0; i < array.length; i++){
                array[i] = '*';
            }
            reader.read(array);
            reader.close();
            String returnvalue = "";
            for(int i = 0; i < array.length; i++){
                if(array[i] == '*'){
                    break;
                }else{
                    returnvalue += array[i];
                }
            }
            return returnvalue;
        } catch (Exception e) {
            return "NO FILE";
        }
    }

    public boolean isExist(){
        try{
            File file = new File(getApplicationUsingReflection().getApplicationContext().getFilesDir(),"widgetData");
            if(!file.exists()){
                return false;
            }else {
                return true;
            }
        }catch (Exception e){
            return false;
        }

    }


    private static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
    }



}
