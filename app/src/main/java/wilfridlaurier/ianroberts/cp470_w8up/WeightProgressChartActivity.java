package wilfridlaurier.ianroberts.cp470_w8up;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class WeightProgressChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_progress_chart);

        // Dropdown spinner set up
        Spinner progressSpinner = findViewById(R.id.progressSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);


//        // Firebase object to pull categories and exercises
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Storing muscle categories from Firebase into ArrayList
//        List<String> muscleGroupArrayList = new ArrayList<>();
//
//        db.collection("workouts")
//                .document("3VTC59DxTQIyYwfZVccT")
//                .get()
//                 .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onSuccess(DocumentSnapshot docSnapshot) {
//                                                if (docSnapshot.exists()) {
//                                                    ArrayList<String> group = (ArrayList<String>) docSnapshot.get("muscleGroupCategories");
//                                                    for (String str : group) {
//                                                        muscleGroupArrayList.add(str.toString());
//                                                    }
//                                                }
//                                            }
//
//
//                                        }
//                 );



//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, muscleGroupArrayList);
//        progressSpinner.setAdapter(spinnerAdapter);

        // Line Chart Set up
        LineChart legChart = findViewById(R.id.weightLineChart);

        ArrayList<Entry> lineYvals = new ArrayList<>();

        lineYvals.add(new Entry(0,105f));
        lineYvals.add(new Entry(1,135f));
        lineYvals.add(new Entry(2,115f));
        lineYvals.add(new Entry(3,165f));
        lineYvals.add(new Entry(4,190f));
        lineYvals.add(new Entry(5,225f));

        LineDataSet lineLegSet1 = new LineDataSet(lineYvals, "Back Squats");
        lineLegSet1.setFillAlpha(110);

        LineData lineLegData1 = new LineData(lineLegSet1);
        legChart.setData(lineLegData1);


        // Radar Chart Set Up
        RadarChart weightRadarChart = findViewById(R.id.weightRadarChart);

        ArrayList<RadarEntry> legWeights = new ArrayList<>();
        legWeights.add(new RadarEntry(205)); //Back squat
        legWeights.add(new RadarEntry(155)); //Front squat
        legWeights.add(new RadarEntry(165)); //Calf raise
        legWeights.add(new RadarEntry(265)); //Leg press
        legWeights.add(new RadarEntry(205)); //Leg extension
        legWeights.add(new RadarEntry(205)); //Leg curl

        RadarDataSet legRadarDataSet = new RadarDataSet(legWeights, "Leg Workout Progress");
        legRadarDataSet.setColor(Color.DKGRAY);
        legRadarDataSet.setLineWidth(3f);
        legRadarDataSet.setValueTextColor(Color.MAGENTA);
        legRadarDataSet.setValueTextSize(15f);

        RadarData legRadarData = new RadarData();
        legRadarData.addDataSet(legRadarDataSet);

        String[] labels = {"Back Squat","Front Squat","Calf Raise","Leg Press","Leg Extension","Leg Curl",};
        XAxis xAxis = weightRadarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        weightRadarChart.getDescription().setText("Leg Lifts");
        weightRadarChart.setData(legRadarData);

    }


}