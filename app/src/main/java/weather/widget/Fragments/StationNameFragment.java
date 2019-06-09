package weather.widget.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import weather.widget.DataManager.DataContainer;
import weather.widget.Database.DatabaseManager;
import weather.widget.Interfaces.IStationsChangeListener;
import weather.widget.R;
import weather.widget.RecycleViewAdapters.StationNameAdapter;


public class StationNameFragment extends Fragment implements IStationsChangeListener{

    private RecyclerView recycle;
    private StationNameAdapter adapter;
    private WaveSwipeRefreshLayout waveSwipeRefreshLayout;

    public StationNameFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataContainer.getInstance().setListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_station_names, container, false);
        DatabaseManager.getInstance().getStations();
        //Toast.makeText(this.getContext(),"Állomások keresése folyamatban...",Toast.LENGTH_LONG).show();
        waveSwipeRefreshLayout = view.findViewById(R.id.swipe_container_names);
        waveSwipeRefreshLayout.setWaveColor(Color.parseColor("#C60AF5F5"));
        waveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                DatabaseManager.getInstance().getStations();
                Toast.makeText(getContext(),"Frisítés...",Toast.LENGTH_SHORT).show();
                stopRefreshingAfterDelay();
            }
        });
        recycle = view.findViewById(R.id.recycler_view_station_frame);
        adapter = new StationNameAdapter(this.getContext(),DataContainer.getInstance().getStations());
        recycle.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycle.setAdapter(adapter);
        return view;
    }

    @Override
    public void change(boolean status) {
        if(status){
            adapter.notifyDataSetChanged();
            recycle.smoothScrollBy(0,-1);
            waveSwipeRefreshLayout.setRefreshing(false);
        }else{
            Toast.makeText(getContext(),"Az állomás nevek lekérdezése nem sikerült!",Toast.LENGTH_SHORT).show();
            DataContainer.getInstance().clearStations();
            adapter.notifyDataSetChanged();
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
