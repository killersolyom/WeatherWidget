package weather.widget.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.Objects;
import weather.widget.DataManager.DataContainer;
import weather.widget.Database.DatabaseManager;
import weather.widget.Interfaces.IStationsChangeListener;
import weather.widget.R;
import weather.widget.RecycleViewAdapters.StationNameAdapter;


public class StationNameFragment extends Fragment implements IStationsChangeListener,SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recycle;
    private StationNameAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        Toast.makeText(this.getContext(),"Állomások keresése folyamatban...",Toast.LENGTH_LONG).show();
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container_names);
        mSwipeRefreshLayout.setOnRefreshListener(this);
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
            Log.e("Appw","Nevek megjöttek");
        }else{
            Toast.makeText(getContext(),"Az állomás nevek lekérdezése nem sikerült!",Toast.LENGTH_SHORT).show();
            DataContainer.getInstance().clearStations();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        DataContainer.getInstance().clearStations();
        adapter.notifyDataSetChanged();
        DatabaseManager.getInstance().getStations();
        Toast.makeText(getContext(),"Frisítés...",Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.setRefreshing(false);
    }


}
