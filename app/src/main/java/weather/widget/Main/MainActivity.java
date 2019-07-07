package weather.widget.Main;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.Calendar;
import weather.widget.DataManager.DataContainer;
import weather.widget.Database.DatabaseManager;
import weather.widget.Fragments.StationNameFragment;
import weather.widget.Fragments.StationValuesFragment;
import weather.widget.R;
import weather.widget.Services.ValueUpdaterService;
import weather.widget.ViewPager.SectionsPageAdapter;



public class MainActivity extends AppCompatActivity{


    private ViewPager mViewPager;
    private SectionsPageAdapter pagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_locations:
                    startNameFragment();
                    return true;
                case R.id.navigation_weather:
                    startWeatherFragment();
                    return true;
                case R.id.navigation_refresh:
                    if(mViewPager.getCurrentItem() == 1){
                        DatabaseManager.getInstance().getValues();
                    }else if(mViewPager.getCurrentItem() == 0){
                        DatabaseManager.getInstance().getStations();
                    }
                    return true;
            }
            return false;
        }
    };


    private void requestPermissions(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_WIFI_STATE},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(MainActivity.this, "A használathoz tárhely hozzáférés szükséges!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mViewPager = findViewById(R.id.vcontainer);
        setupViewPager();
        startService();
        makeStationQuerryWithDelay();
        makeValueQuerryWithDelay();
        if(DataContainer.getInstance().getStationName().equals("none")){
            Toast.makeText(this,"Kérem válasszon ki egy állomást!",Toast.LENGTH_LONG).show();
            startNameFragment();
        }else{
            startWeatherFragment();
        }
    }

    private void startService(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        Intent intent = new Intent(getApplicationContext(), ValueUpdaterService.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5*60000, pintent);
    }

    private void setupViewPager() {
        pagerAdapter.addFragment(new StationNameFragment());
        pagerAdapter.addFragment(new StationValuesFragment());
        mViewPager.setAdapter(pagerAdapter);
    }

    private void makeStationQuerryWithDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseManager.getInstance().getStations();
            }}, 200);
    }

    private void makeValueQuerryWithDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseManager.getInstance().getValues();
            }}, 600);
    }

    private void startWeatherFragment(){
        mViewPager.setCurrentItem(1,true);
    }

    private void startNameFragment(){
        mViewPager.setCurrentItem(0,true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
