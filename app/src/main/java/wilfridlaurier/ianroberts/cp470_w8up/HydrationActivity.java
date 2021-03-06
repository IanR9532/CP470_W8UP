package wilfridlaurier.ianroberts.cp470_w8up;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HydrationActivity extends AppCompatActivity {

    Button addWater;
    EditText waterText;
    Button waterGoal;
    EditText goalText;
    ProgressBar waterBar;
    String waterTextVal;
    String goalTextVal;
    TextView waterAmount;
    double total;
    double goal;
    double progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydration);

        addWater = findViewById(R.id.addWater);
        waterText = findViewById(R.id.waterText);
        waterGoal = findViewById(R.id.waterGoal);
        goalText = findViewById(R.id.goalText);
        waterBar =  findViewById(R.id.waterBar);
        waterTextVal = waterText.getText().toString();
        total = Integer.parseInt(waterTextVal);
        goalTextVal = goalText.getText().toString();
        goal = Integer.parseInt(goalTextVal);
        waterAmount = findViewById(R.id.waterAmount);

        waterBar.setProgress((int)(total/goal)*100);
        waterAmount.setText((int)total + "/" + (int)goal);

        addWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total += Integer.parseInt(waterText.getText().toString());
                Log.i("HydrationActivity", "" + (int)total);
                progress = total/goal;
                Log.i("HydrationActivity", "" + (int)((progress)*100));
                waterBar.setProgress((int)(progress*100));
                waterAmount.setText((int)total + "/" + (int)goal);
            }
        });

        waterGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal = Integer.parseInt(goalText.getText().toString());
                progress = total/goal;
                waterBar.setProgress((int)(progress*100));
                waterAmount.setText((int)total + "/" + (int)goal);
            }
        });
    }
}