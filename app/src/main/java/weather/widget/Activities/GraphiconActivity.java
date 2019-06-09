package weather.widget.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import weather.widget.Classes.Clock;
import weather.widget.DataManager.DataContainer;
import weather.widget.Database.DatabaseManager;
import weather.widget.Interfaces.IUpdateListener;
import weather.widget.R;

public class GraphiconActivity extends AppCompatActivity implements IUpdateListener {

    private GraphView graphicon;
    private LineGraphSeries<DataPoint> series;
    private String data="";
    private String title="";
    private String unit="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphicon);
        graphicon = findViewById(R.id.graph);
        ImageButton refreshButton = findViewById(R.id.refreshButton);;
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Frissítés...",Toast.LENGTH_SHORT).show();
                getData();
            }
        });
        DataContainer.getInstance().setListener(this);
        Intent intent = getIntent();
        data = intent.getStringExtra("0");
        title = intent.getStringExtra("1");
        unit = intent.getStringExtra("2");
        getData();
    }


    private void getData(){
        DatabaseManager.getInstance().getValues(data);
    }
    public GraphiconActivity(String data, String title, String unit) {
        this.data = data;
        this.title = title;
        this.unit = unit;
    }

    public GraphiconActivity() {

    }

    @Override
    public void newValue(boolean value) {
        if(value){
            if(DataContainer.getInstance().getPointsSize() > 0){
                try {
                    series = new LineGraphSeries<>(DataContainer.getInstance().getPoints().toArray(new DataPoint[DataContainer.getInstance().getPointsSize()]));
                    draw();
                }catch (Exception e){
                    Toast.makeText(this,"Nem sikerült az adat ábrázolás!",Toast.LENGTH_SHORT).show();
                }
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Nincs megjeleníthető adat!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void draw(){
            GridLabelRenderer gridLabel = graphicon.getGridLabelRenderer();
            gridLabel.setHorizontalAxisTitle("Idő (t)");
            gridLabel.setVerticalAxisTitle(title +" (" +unit+")" );
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphicon);
            staticLabelsFormatter.setHorizontalLabels(Clock.getInstance().genetateTimeScale());
            graphicon.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graphicon.setTitleTextSize(70);
            graphicon.setTitleColor(Color.WHITE);
            series.setColor(Color.RED);
            series.setAnimated(true);
            series.setThickness(9);
            graphicon.addSeries(series);
            graphicon.getViewport().setScalable(true);
            graphicon.getViewport().setScrollable(true);

    }
}
