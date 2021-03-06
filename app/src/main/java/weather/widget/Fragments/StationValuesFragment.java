package weather.widget.Fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Objects;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import weather.widget.Activities.GraphiconActivity;
import weather.widget.Classes.StationValues;
import weather.widget.DataManager.DataContainer;
import weather.widget.Database.DatabaseManager;
import weather.widget.Interfaces.IValueClickedListener;
import weather.widget.Interfaces.IValuesChangeListener;
import weather.widget.R;
import weather.widget.RecycleViewAdapters.StationValueAdapter;
import weather.widget.Widget.WeatherWidget;


public class StationValuesFragment extends Fragment implements IValuesChangeListener, IValueClickedListener{

    private ArrayList<StationValues> values = new ArrayList<>();
    private RecyclerView recycle;
    private StationValueAdapter adapter;
    private WaveSwipeRefreshLayout waveSwipeRefreshLayout;

    public StationValuesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataContainer.getInstance().setListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_station_values, container, false);
        waveSwipeRefreshLayout = view.findViewById(R.id.swipe_container_values);
        waveSwipeRefreshLayout.setWaveColor(Color.parseColor("#C60AF5F5"));
        waveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                DatabaseManager.getInstance().getValues();
                Toast.makeText(getContext(),"Frisítés...",Toast.LENGTH_SHORT).show();
                stopRefreshingAfterDelay();
            }
        });
        getSelectedStationValues();
        recycle = view.findViewById(R.id.recycler_view_values_frame);
        adapter = new StationValueAdapter(this.getContext(),values);
        adapter.setListener(this);
        recycle.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycle.setAdapter(adapter);
        getSelectedStationValues();
        return view;
    }


    public void getSelectedStationValues(){
        try {
            values.clear();
            adapter.notifyDataSetChanged();
            ArrayList<String> value = DataContainer.getInstance().getAllValues();
            values.add( new StationValues("Hőmérséklet: " +value.get(0)+"°C", ContextCompat.getDrawable(getContext(),R.drawable.temperature)));
            values.add( new StationValues("Páratartalom: " +value.get(1)+"%", ContextCompat.getDrawable(getContext(),R.drawable.humidity)));
            values.add( new StationValues("Légnyomás " +value.get(2)+" hPa", ContextCompat.getDrawable(getContext(),R.drawable.pressure)));
            values.add( new StationValues("Csapadék: " +value.get(3)+" mm/m^2", ContextCompat.getDrawable(getContext(),R.drawable.cloud)));
            values.add( new StationValues("Uv index: " +value.get(4), ContextCompat.getDrawable(getContext(),R.drawable.uv)));
            values.add( new StationValues("Szélirány: " +value.get(5), ContextCompat.getDrawable(getContext(),R.drawable.direction)));
            values.add( new StationValues("Szélsebesség: " +value.get(6)+" Km/h", ContextCompat.getDrawable(getContext(),R.drawable.speed)));
            adapter.notifyDataSetChanged();
        }catch (Exception ignored){
        }

    }

    @Override
    public void change(boolean status) {
        if(status){
            recycle.smoothScrollBy(0,-1);
            try {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getSelectedStationValues();
                    }
                });
                waveSwipeRefreshLayout.setRefreshing(false);
            }catch (Exception ignored){
            }
            try {
                Intent intent = new Intent(getContext(), WeatherWidget.class);
                intent.setAction("UPDATE_WIDGET");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent,0);
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getContext(),"Az állomás értékek lekérdezése nem sikerült!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void clicked(int index) {
        if(index == 0){
            startGraghicon("TEMPERATURE","Hőmérséklet","°C");
        }else if(index == 1){
            startGraghicon("HUMIDITY","Páratartalom","%");
        }else if(index == 2){
            startGraghicon("PRESSURE","Légnyomás","hPa");
        }else if(index == 3){
            startGraghicon("RAIN_LEVEL","Csapadék szint","mm/m²");
        }else if(index == 4){
            startGraghicon("UV_INDEX","Uv Index","Index");
        }else if(index == 5){
            Toast.makeText(getContext(),"Ezt az adat csoportot nem lehet grafikusan ábrázolni!",Toast.LENGTH_SHORT).show();
        }else if(index == 6){
            startGraghicon("WIND_SPEED","Szélsebesség","Km/h");
        }

    }

    private void startGraghicon(String data,String title, String unit){
        try {
            GraphiconActivity graphiconActivity = new GraphiconActivity(data,title,unit);
            Intent intent = new Intent(getActivity(), graphiconActivity.getClass());
            intent.putExtra("0",data);
            intent.putExtra("1",title);
            intent.putExtra("2",unit);
            startActivity(intent);
        }catch (Exception ignored){
            Toast.makeText(getContext(),"Nem sikerült betölteni a grafikont!",Toast.LENGTH_SHORT).show();
        }
    }


    private void stopRefreshingAfterDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                waveSwipeRefreshLayout.setRefreshing(false);
            }}, 2000);
    }





}
