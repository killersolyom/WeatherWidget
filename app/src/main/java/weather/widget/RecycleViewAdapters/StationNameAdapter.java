package weather.widget.RecycleViewAdapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import weather.widget.DataManager.DataContainer;
import weather.widget.Database.DatabaseManager;
import weather.widget.R;


public class StationNameAdapter extends RecyclerView.Adapter<StationNameAdapter.RecyclerViewHolder> {



    private ArrayList<String> stations = new ArrayList<>();
    private Context context;

    public StationNameAdapter(Context context, ArrayList<String> station) {
        this.context = context;
        stations = station;
        }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_station_name_component, parent, false));
    }

    @Override
    public void onBindViewHolder( final RecyclerViewHolder holder, int position) {
        try {
            final String station  = stations.get(position);
            holder.stationName.setText(station);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,"Az adatok betöltése nem siekrült!", Toast.LENGTH_LONG).show();
        }
        holder.stationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DataContainer.getInstance().getStationName().equals(holder.stationName.getText().toString())){
                    DataContainer.getInstance().setStationName(holder.stationName.getText().toString());
                    DatabaseManager.getInstance().getValues();
                }
                Toast.makeText(context,"Kiválasztva: "+ DataContainer.getInstance().getStationName(), Toast.LENGTH_SHORT).show();

            }
        });

    }



    @Override
    public int getItemCount() {
        return stations.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView stationName;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            stationName = itemView.findViewById(R.id.stationName);
        }
    }




}