package liftheavy.com.wongmatthew.liftheavy;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Matthew on 8/13/2016.
 */

public class ExercisesFragment extends Fragment {

    private Button addExercise;
    private ListView exerciseListView;
    private EditText filterExerciseEditText;
    private String[] strengthExercises;
    private String[] cardioExercises;
    private ArrayList<String> exercises;
    private ExerciseRepo exerciseRepo;
    private List<Exercise> listOfExercises;
    private List<String> listOfCategories;
    private ArrayAdapter<String> adapter;

    public ExercisesFragment(){

        listOfCategories = new ArrayList<String>();
        listOfCategories.add("Strength");
        listOfCategories.add("Cardio");
        strengthExercises = new String[]{"Back Extension", "Bench Dips", "Bench Press", "Bent Over Row",
        "Bicep Curl", "Cable Crossover", "Cable Curl", "Cable Fly", "Chin-up", "Concentration Curl", "Crunches",
        "Deadlift", "Decline Bench Press", "Decline Fly", "Dips", "Dumbbell Raise", "Front Squat",
        "Glute-Ham Raise", "Good-Morning", "Hack Squat", "Hammer Curl","Hanging Knee Raise", "Hip Abductor", "Hip Adductor",
        "Hyperextension", "Incline Bench Press", "Incline Curl", "Incline Fly", "Inverted Row", "Lat Pull-Down", "Lateral Raise",
        "Lying Dumbbell Tricep Extension", "Leg Curl", "Leg Extension", "Leg Press", "Leg Raise", "Lunge", "Lying Leg Curl",
        "Lying Tricep Press", "Oblique Crunches", "One-Arm Row", "Overhead Press", "Overhead Tricep Extension", "Preacher Curl",
        "Pull-up", "Push-up", "Rear Delt Row", "Reverse Crunches", "Reverse Fly", "Romanian Deadlift", "Russian Twist", "Seated Cable Row",
        "Seated Calf Raise", "Seated Curl", "Seated Overhead Press", "Seated Tricep Press", "Shoulder Press", "Shrug", "Skullcrusher",
        "Split Squat", "Squat", "Squat Jump", "Standing Calf Raise", "Standing Curl", "Standing Heel Raise", "Standing Overhead Press",
        "Straight Arm Pulldown", "Straight Leg Deadlift", "Sumo Deadlift", "T-Bar Row", "Tricep Extension", "Tricep Kickback", "Tricep Pushdown",
        "Upright Row", "Wide-Grip Lat Pulldown"};

        cardioExercises = new String[]{"Burpee", "Box Jump", "Elliptical", "Flutter Kick", "Jump Rope", "Jumping Jack", "Lateral Stepover",
        "Mountain Climber", "Push-up Burpee", "Single-Leg Box Jump", "Single-Leg Burpee", "Single-Leg Hop", "Stair Climb", "Stationary Bike",
        "Step-up", "Treadmill", "Vertical Jump"};

    }

    public static ExercisesFragment newInstance(){
        ExercisesFragment exercisesFragment = new ExercisesFragment();
        return exercisesFragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.exercises, container, false);
        addExercise = (Button) view.findViewById(R.id.addExercise);
        exerciseListView = (ListView) view.findViewById(R.id.exerciseListView);
        filterExerciseEditText = (EditText) view.findViewById(R.id.filterExerciseEditText);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        exerciseRepo = new ExerciseRepo();
        if (!DatabaseManager.isInitialized()){
            Log.d("IS INITIALIZED", "NOT INITIALIZED");
            return;
        }
        if (exerciseRepo.getExercises().size() == 0){
            insertData();
        }else{
            populateList();
        }
        updateList();


        filterExerciseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
        });



        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemPosition = i;

                final String itemValue = (String) exerciseListView.getItemAtPosition(itemPosition);

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.click_exercise);
                dialog.setTitle("View Exercise");

                final TextInputLayout viewExerciseTIL = (TextInputLayout) dialog.findViewById(R.id.viewExerciseTIL);
                viewExerciseTIL.setErrorEnabled(true);

                final EditText viewExerciseEditText = (EditText) dialog.findViewById(R.id.viewExerciseEditText);
                viewExerciseEditText.setText(itemValue);


                TextView categoryLabel = (TextView) dialog.findViewById(R.id.categoryLabel);
                //change label color to blue
                categoryLabel.setTextColor(getResources().getColor(R.color.blue));

                final Exercise currentExercise = search(itemValue);



                final Spinner categorySpinner = (Spinner) dialog.findViewById(R.id.categorySpinner);
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listOfCategories);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoryAdapter);

                //set matching category
                categorySpinner.setSelection(listOfCategories.indexOf(currentExercise.getCategory()));


                Button viewExerciseOKButton = (Button) dialog.findViewById(R.id.viewExerciseOKButton);
                viewExerciseOKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo modify if changed
                        String exerciseName = viewExerciseEditText.getText().toString();
                        String categoryName = categorySpinner.getSelectedItem().toString();
                        if (exerciseName.length() == 0) {
                            viewExerciseTIL.setError("You must enter a value.");
                        } else {
                            if (!currentExercise.getName().equals(exerciseName)  || !currentExercise.getCategory().equals(categoryName)) {
                                //modification
                                //UPDATE
                                ContentValues values = new ContentValues();
                                values.put(Exercise.KEY_Name, exerciseName);
                                values.put(Exercise.KEY_Category, categoryName);
                                if (!exerciseRepo.update(currentExercise.getName(), values)) {
                                    Log.d("DATABASE ERROR:", " COULD NOT UPDATE RECORD");
                                } else {
                                    Toast.makeText(getContext(), currentExercise.getName() + " Updated!", Toast.LENGTH_SHORT).show();
                                }
                                updateList();

                            }
                            dialog.dismiss();
                        }
                    }
                });

                Button viewExerciseDeleteButton = (Button) dialog.findViewById(R.id.viewExerciseDeleteButton);
                viewExerciseDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo delete currentExercise
                        if (!exerciseRepo.delete(itemValue)) {
                            Log.d("DATABASE ERROR:", " COULD NOT DELETE RECORD");
                        } else {
                            Toast.makeText(getContext(), currentExercise.getName() + " Deleted!", Toast.LENGTH_SHORT).show();
                        }
                        updateList();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());

                dialog.setContentView(R.layout.add_new_exercise);
                dialog.setTitle("Add New Exercise");

                final TextInputLayout addExerciseTIL = (TextInputLayout) dialog.findViewById(R.id.addExerciseTIL);
                addExerciseTIL.setErrorEnabled(true);

                final EditText addExerciseEditText = (EditText) dialog.findViewById(R.id.addExerciseEditText);

                //change color of edit text
                //addExerciseEditText.getBackground().setColorFilter(R.color.black, PorterDuff.Mode.SRC_IN);


                TextView categoryLabel = (TextView) dialog.findViewById(R.id.categoryLabel);
                //change label color to blue
                categoryLabel.setTextColor(getResources().getColor(R.color.blue));


                final Spinner categorySpinner = (Spinner) dialog.findViewById(R.id.categorySpinner);
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listOfCategories);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoryAdapter);


                Button addExerciseOKButton = (Button) dialog.findViewById(R.id.addExerciseOKButton);
                addExerciseOKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String exerciseName = addExerciseEditText.getText().toString();
                        String categoryName = categorySpinner.getSelectedItem().toString();

                        if (addExerciseEditText.getText().length() == 0) {
                            addExerciseTIL.setError("You must enter a value.");
                        } else {

                            addExercise(exerciseName, categoryName);
                            Toast.makeText(getContext(), exerciseName + " Added!", Toast.LENGTH_SHORT).show();
                            updateList();
                            dialog.dismiss();
                        }
                    }
                });

                Button addExerciseCancelButton = (Button) dialog.findViewById(R.id.addExerciseCancelButton);
                addExerciseCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });




    }



    private void populateList(){
        listOfExercises = exerciseRepo.getExercises();
        if (!listOfExercises.isEmpty()){
            exercises = new ArrayList<String>();
            for (Exercise e: listOfExercises){
                exercises.add(e.getName());
            }
            Collections.sort(exercises, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });
        }

    }

    private void addExercise(String name, String category){
        Exercise e = new Exercise();
        e.setName(name);
        e.setCategory(category);
        exerciseRepo.insert(e);
    }

    private Exercise search(String name){
        Exercise exercise = null;
        if (listOfExercises != null && name != null){
            for (Exercise e: listOfExercises){
                if (e.getName().equals(name)){
                    exercise = e;
                    break;
                }
            }
        }
        return exercise;
    }

    private void updateList(){
        populateList();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, exercises.toArray(new String[exercises.size()]));
        exerciseListView.setAdapter(adapter);
    }


    private void insertData(){
        exerciseRepo.delete();

        Exercise exercise = new Exercise();

        for (int i = 0; i < strengthExercises.length; i++){
            exercise.setName(strengthExercises[i]);
            exercise.setCategory("Strength");
            exerciseRepo.insert(exercise);
        }
        for (int i = 0; i < cardioExercises.length; i++){
            exercise.setName(cardioExercises[i]);
            exercise.setCategory("Cardio");
            exerciseRepo.insert(exercise);
        }

        populateList();

    }
}
