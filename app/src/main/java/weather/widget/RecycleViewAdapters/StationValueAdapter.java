package weather.widget.RecycleViewAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import weather.widget.Classes.StationValues;
import weather.widget.Interfaces.IValueClickedListener;
import weather.widget.R;


public class StationValueAdapter extends RecyclerView.Adapter<StationValueAdapter.RecyclerViewHolder>{
    private IValueClickedListener mclicked;
    private ArrayList<StationValues> values = new ArrayList<>();
    private Context context;
    public void setListener(IValueClickedListener ch){this.mclicked=ch;}

    public StationValueAdapter(Context context, ArrayList<StationValues> value) {
        this.context = context;
        values = value;
        }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_station_value_component, parent, false));
    }

    @Override
    public void onBindViewHolder( final RecyclerViewHolder holder, int position) {
        final StationValues value  = values.get(position);
        try {
            holder.stationValue.setText(value.getValue());
            holder.valuesImage.setImageDrawable(value.getImage());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,"Az adapter nem tudta betölteni az adatokat!", Toast.LENGTH_LONG).show();
        }



    }


    @Override
    public int getItemCount() {
        return values.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView stationValue;
        private ImageView valuesImage;

        RecyclerViewHolder(final View itemView) {
            super(itemView);
            stationValue = itemView.findViewById(R.id.value);
            valuesImage= itemView.findViewById(R.id.valueImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mclicked.clicked(getAdapterPosition());
        }

    }




}