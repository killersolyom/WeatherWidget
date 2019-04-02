package weather.widget.Classes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Clock {

    private Clock() {
    }

    public static Clock getInstance() {
        return ClockHolder.INSTANCE;
    }

    private static class ClockHolder {

        private static final Clock INSTANCE = new Clock();
    }

    public String getElapsedHoursInTimeStamp(){
        Date currentTime = Calendar.getInstance().getTime();
        Long time = ((System.currentTimeMillis()/1000) - ((currentTime.getHours() * 3600) +  (currentTime.getMinutes() * 60))) * 1000;
       // return time.toString();
        return "0";
    }
    public int getCurrentTime(){
        return  Calendar.getInstance().getTime().getHours();
    }

    public int getCurrentMinute(){
        return  Calendar.getInstance().getTime().getMinutes();
    }

    public String[] genetateTimeScale(){
        ArrayList<String> timeScale = new ArrayList<>();
        for (int i = 0; i <= getCurrentTime(); i+=2){
                timeScale.add(i+"");
        }
        timeScale.add(" ");
        String [] returnValues = new String[timeScale.size()];
        timeScale.toArray(returnValues);
        return returnValues;
    }

    public Date convertTimeStampToDate(long timestamp){
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        java.util.Date currenTimeZone=new java.util.Date(timestamp*1000);;
        return currenTimeZone;
    }

}
