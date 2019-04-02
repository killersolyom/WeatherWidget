package weather.widget.Main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.util.Calendar;
import weather.widget.DataManager.DataContainer;
import weather.widget.Database.DatabaseManager;
import weather.widget.Fragments.StationNameFragment;
import weather.widget.Fragments.StationValuesFragment;
import weather.widget.Interfaces.IViewPagerListener;
import weather.widget.R;
import weather.widget.Services.ValueUpdaterService;
import weather.widget.ViewPager.SectionsPageAdapter;



public class MainActivity extends AppCompatActivity implements IViewPagerListener{

    private ViewPager mViewPager;
    private SectionsPageAdapter pagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager.getInstance().getStations();
        if(DataContainer.getInstance().getStationName().equals("none")){
            Toast.makeText(this,"Kérem válasszon ki egy állomást!",Toast.LENGTH_LONG).show();
        }
        mViewPager = findViewById(R.id.vcontainer);
        setupViewPager();
        startWeatherFragment();
        startService();
        DataContainer.getInstance().setListener(this);
    }

    private void setupViewPager() {
        pagerAdapter.addFragment(new StationNameFragment());
        pagerAdapter.addFragment(new StationValuesFragment());
        mViewPager.setAdapter(pagerAdapter);
    }

    public void startService(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, ValueUpdaterService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5*60000, pintent);
    }

    private void startWeatherFragment(){
        mViewPager.setCurrentItem(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void change(boolean status) {
        Log.e("Appw","Main "+status);
        if (status){
            pagerAdapter.notifyDataSetChanged();
        }
    }

}
