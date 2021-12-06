package wilfridlaurier.ianroberts.cp470_w8up;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

// TODO Update naming conventions

/*
    Naming. WorkoutsActivity                -> AllWorkoutsActivity     -> Shows list of all workouts
            RoutineActivity                 -> WorkoutViewActivity     -> Shows selected Workout
            CreateNewRoutineActivity        -> WorkoutCreateActivity   -> Allows for workout creation
            ExerciseOptionsActivity         -> ExerciseListActivity    -> Used when adding exercise to a workout
            ViewExerciseActivity            -> ExerciseViewActivity    -> Shows exercise weights/reps etc.
            CreateCustomExerciseActivity    -> ExerciseCreateActivity  -> Allows for custom exercise creation
            no previous file                -> ExerciseEditActivity    -> Allows user to edit an exercise
 */

/*

    This is the Workouts Activity. It will present the user with a list of their custom workouts.
    In addition it will:
        - Give the user the option to create a new custom workout that will send them to the
        WorkoutCreateActivity
        - Give the user the option to select a workout which will send them to the WorkoutViewActivity
        - Give them the option to delete a workout
        - The user will be able to search these workouts as well as filter by muscle group

 */

public class AllWorkoutsActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "WorkoutsActivity";

    SearchView workoutsSearchView;
    ImageButton newWorkoutButton;
    ImageButton filterWorkoutsButton;
    ListView workoutsListView;
    WorkoutsAdapter adapter;
    // TODO update this to be a list of workout objects?
    // ArrayList<String> list1;
    ArrayList<Workout> userWorkouts;

    FirebaseAuth fAuth;
    String userID;

    String filterOption = "ALL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_workouts);
        workoutsSearchView = findViewById(R.id.searchWorkoutsSearchView);
        newWorkoutButton = findViewById(R.id.newWorkoutButton);
        filterWorkoutsButton = findViewById(R.id.filterWorkoutsButton);
        workoutsListView = findViewById(R.id.workoutsListView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        DatabaseReference rootRef = database.getReference();
        DatabaseReference workoutsReference = rootRef.child("users").child(userID).child("userWorkouts");

        userWorkouts = new ArrayList<>();

//        ArrayList<MuscleGroup> muscleGroupOne = new ArrayList<>();
//        muscleGroupOne.add(MuscleGroup.CHEST);
//        muscleGroupOne.add(MuscleGroup.ARMS);
//        Workout testWorkoutOne = new Workout("WorkoutTestOne",muscleGroupOne);
//        SetRep testSetRepOne = new SetRep(3,10,350);
//        Exercise testExerciseOne = new Exercise("ExerciseTestOne",testSetRepOne,MuscleGroup.CHEST);
//        testWorkoutOne.addExercise(testExerciseOne);
//
//        ArrayList<MuscleGroup> muscleGroupTwo = new ArrayList<>();
//        muscleGroupTwo.add(MuscleGroup.BACK);
//        muscleGroupTwo.add(MuscleGroup.ARMS);
//        Workout testWorkoutTwo = new Workout("WorkoutTestTwo",muscleGroupTwo);
//        SetRep testSetRepTwo = new SetRep(3,10,350);
//        Exercise testExerciseTwo = new Exercise("ExerciseTestTwo",testSetRepTwo,MuscleGroup.BACK);
//        testWorkoutTwo.addExercise(testExerciseTwo);
//
//        userWorkouts.add(testWorkoutOne);
//        userWorkouts.add(testWorkoutTwo);
//
//        workoutsReference.setValue(userWorkouts);

        workoutsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userWorkouts = new ArrayList<>();
                adapter = new WorkoutsAdapter(getApplicationContext(), 0, userWorkouts);
                workoutsListView.setAdapter(adapter);
                for (DataSnapshot workout : snapshot.getChildren()) {
                    userWorkouts.add(workout.getValue(Workout.class));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(ACTIVITY_NAME, "Could not get user workouts", error.toException());
            }
        });


        workoutsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Workout> filterWorkouts = new ArrayList<>();
                for(Workout workout : userWorkouts){
                    if(workout.getWorkoutName().toLowerCase().contains(newText.toLowerCase())){
                        if(filterOption.equals("ALL")) {
                            filterWorkouts.add(workout);
                        }
                        else{
                            for(String muscleGroup : workout.getMuscleGroupCategories()){
                                if(muscleGroup.toLowerCase().equals(filterOption.toLowerCase())){
                                    filterWorkouts.add(workout);
                                    break;
                                }
                            }
                        }
                    }
                }
                adapter = new WorkoutsAdapter(getApplicationContext(),0,filterWorkouts);
                workoutsListView.setAdapter(adapter);
                return false;
            }
        });

        newWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked add new workout");
                Intent newWorkoutIntent = new Intent(getApplicationContext(), WorkoutCreateActivity.class);
                startActivity(newWorkoutIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    private class WorkoutsAdapter extends ArrayAdapter<Workout>{
        public WorkoutsAdapter(Context ctx,int resource, ArrayList<Workout> workoutsList) {
            super(ctx, resource,workoutsList);
        }

        @Override
        public View getView(int position, View result, ViewGroup parent) {
            Workout workout = getItem(position);

            if(result == null){
                result = LayoutInflater.from(getContext()).inflate(R.layout.activity_all_workouts_list_item,parent,false);
            }

            TextView workoutName = (TextView) result.findViewById(R.id.workoutName);
            workoutName.setText(workout.getWorkoutName());

            return result;
        }
    }
}