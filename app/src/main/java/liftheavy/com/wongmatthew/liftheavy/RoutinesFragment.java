package liftheavy.com.wongmatthew.liftheavy;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

public class RoutinesFragment extends Fragment{

    private TextView routinesRecentWorkoutDetails;
    private TextView startWorkoutTextView;
    private ListView startWorkoutListView;
    private ListView routineListView;
    private ListView addRoutineListView;
    private ListView addRoutineExercisesListView;
    private Button addRoutine;
    private RoutineRepo routineRepo;
    private TextInputLayout addRoutineTIL;
    private List<String> listOfCategories;
    private List<String> listOfNewRoutineExercises;
    private List<String> listOfCurrentRoutineExercises;
    private ArrayAdapter<String> adapter;
    private EditText addRoutineEditText;
    private ExerciseRepo exerciseRepo;
    private List<Exercise> listOfExercises;
    private List<String> exercises= new ArrayList<String>();;
    private List<String> routines;
    private EditText filterRoutineExerciseEditText;
    private ArrayAdapter<String> adapter2 ;
    private ArrayAdapter<String> routineListAdapter;
    private RoutineAdapter routineAdapter;


    public RoutinesFragment(){
        listOfCategories = new ArrayList<String>();
        listOfCategories.add("Strength");
        listOfCategories.add("Cardio");

        listOfNewRoutineExercises = new ArrayList<String>();
        listOfCurrentRoutineExercises = new ArrayList<String>();
        routines = new ArrayList<String>();
        routineRepo = new RoutineRepo();
        exerciseRepo = new ExerciseRepo();
        listOfExercises = exerciseRepo.getExercises();


        //routineRepo.delete();
    }

    public static RoutinesFragment newInstance(){
        RoutinesFragment routinesFragment = new RoutinesFragment();
        return routinesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.routines, container, false);
        routinesRecentWorkoutDetails = (TextView) view.findViewById(R.id.routinesRecentWorkoutDetails);
        routineListView = (ListView) view.findViewById(R.id.routineListView);
        addRoutine = (Button) view.findViewById(R.id.addRoutine);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        routineRepo = new RoutineRepo();
        if (!DatabaseManager.isInitialized()){
            Log.d("IS INITIALIZED", "NOT INITIALIZED");
            return;
        }
        if (routineRepo.getAllRoutines().size() > 0){
            print(""+routineRepo.getAllRoutines().size());
            updateRoutineList();
        }
        routinesRecentWorkoutDetails.setMovementMethod(new ScrollingMovementMethod());
        //example text
        //TODO
        routinesRecentWorkoutDetails.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit,\n" +
                " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,\n" +
                " quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute\n" +
                " irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\n" +
                " Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit \n" +
                "anim id est laborum.");



        addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //FIRST SCREEN
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.add_new_routine);
                dialog.setTitle("Add New Routine");


                addRoutineTIL = (TextInputLayout) dialog.findViewById(R.id.addRoutineTIL);
                addRoutineTIL.setErrorEnabled(true);

                addRoutineEditText = (EditText)dialog.findViewById(R.id.addRoutineEditText);

                addRoutineListView = (ListView) dialog.findViewById(R.id.addRoutineListView);
                adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listOfNewRoutineExercises);
                addRoutineListView.setAdapter(adapter);

                Button addRoutinesExercisesButton = (Button)dialog.findViewById(R.id.addRoutinesExercisesButton);
                addRoutinesExercisesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //ADD ROUTINE SCREEN
                        final Dialog dialog2 = new Dialog(getContext());
                        dialog2.setContentView(R.layout.add_routine_exercises);
                        dialog2.setTitle("Add Exercises to Routine");
                        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                        populateList();

                        addRoutineExercisesListView = (ListView) dialog2.findViewById(R.id.addRoutineExercisesListView);
                        adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, exercises);
                        addRoutineExercisesListView.setAdapter(adapter2);
                        addRoutineExercisesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                        addRoutineExercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                //print(""+i);
                                addRoutineExercisesListView.setItemChecked(i, addRoutineExercisesListView.isItemChecked(i));
                            }
                        });

                        filterRoutineExerciseEditText = (EditText) dialog2.findViewById(R.id.filterRoutineExerciseEditText);
                        filterRoutineExerciseEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                print(charSequence.toString());
                                adapter2.getFilter().filter(charSequence);
                            }

                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                            }
                        });


                        Button addExerciseToRoutineButton = (Button) dialog2.findViewById(R.id.addExerciseToRoutineButton);
                        addExerciseToRoutineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //ADD NEW EXERCISES TO ADD EXERCISES SCREEN
                                final Dialog dialog3 = new Dialog(getContext());
                                dialog3.setContentView(R.layout.add_new_exercise);
                                dialog3.setTitle("Add New Exercise");

                                final TextInputLayout addExerciseTIL = (TextInputLayout) dialog3.findViewById(R.id.addExerciseTIL);
                                addExerciseTIL.setErrorEnabled(true);

                                final EditText addExerciseEditText = (EditText) dialog3.findViewById(R.id.addExerciseEditText);

                                TextView categoryLabel = (TextView) dialog3.findViewById(R.id.categoryLabel);
                                categoryLabel.setTextColor(getResources().getColor(R.color.blue));


                                final Spinner categorySpinner = (Spinner) dialog3.findViewById(R.id.categorySpinner);
                                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listOfCategories);
                                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                categorySpinner.setAdapter(categoryAdapter);


                                Button addExerciseOKButton = (Button) dialog3.findViewById(R.id.addExerciseOKButton);
                                addExerciseOKButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String exerciseName = addExerciseEditText.getText().toString();
                                        String categoryName = categorySpinner.getSelectedItem().toString();

                                        if (addExerciseEditText.getText().length() == 0) {
                                            addExerciseTIL.setError("You must enter a value.");
                                        } else {
                                            //TODO ADD TO EXERCISES AND UPDATE APDATER
                                            addExercise(exerciseName, categoryName);
                                            Toast.makeText(getContext(), exerciseName + " Added!", Toast.LENGTH_SHORT).show();
                                            updateList();
                                            dialog3.dismiss();


                                        }
                                    }
                                });

                                Button addExerciseCancelButton = (Button) dialog3.findViewById(R.id.addExerciseCancelButton);
                                addExerciseCancelButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog3.dismiss();
                                    }
                                });


                                dialog3.show();
                            }
                        });

                        Button addRoutineExercisesOKButton = (Button) dialog2.findViewById(R.id.addRoutineExercisesOKButton);
                        addRoutineExercisesOKButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SparseBooleanArray checked = addRoutineExercisesListView.getCheckedItemPositions();
                                int length = checked.size();
                                if (length > 0) {
                                    for (int i = 0; i < length; i++) {
                                        int key = checked.keyAt(i);
                                        boolean value = checked.get(key);
                                        if (value) {
                                            String toADD = (String) addRoutineExercisesListView.getItemAtPosition(key);
                                            if (!listOfNewRoutineExercises.contains(toADD)){
                                                listOfNewRoutineExercises.add(toADD);
                                            }
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                                dialog2.dismiss();
                            }
                        });

                        Button addRoutineExercisesCancelButton = (Button) dialog2.findViewById(R.id.addRoutineExercisesCancelButton);
                        addRoutineExercisesCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog2.dismiss();
                            }
                        });

                        dialog2.show();
                    }
                });


                //list of exercises on the add routine xml
                addRoutineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });


                Button addRoutineOKButton = (Button) dialog.findViewById(R.id.addRoutineOKButton);
                addRoutineOKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String routineName = addRoutineEditText.getText().toString();
                        if (routineName.length() == 0) {
                            addRoutineTIL.setError("You must enter a value.");
                        } else {
                            addRoutine(routineName, listOfNewRoutineExercises);
                            Toast.makeText(getContext(), routineName + " Added!", Toast.LENGTH_SHORT).show();
                            updateRoutineList();
                            listOfNewRoutineExercises.clear();
                            dialog.dismiss();
                        }

                    }
                });

                Button addRoutineCancelButton = (Button) dialog.findViewById(R.id.addRoutineCancelButton);
                addRoutineCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });





        //swipe to delete a repo
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(routineListView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {

                for (int position : reverseSortedPositions){
                    String routine = routines.remove(position);
                    routineRepo.deleteRoutine(routine);
                    print("Deleting "+routine);
                    routineListAdapter.notifyDataSetChanged();
                }
            }
        });

        routineListView.setOnTouchListener(touchListener);


        //this is for adding new exercises when you are on the start workout page
        routineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int itemPosition = i;
                final String itemValue = (String) routineListView.getItemAtPosition(itemPosition);
                final Dialog dialog = new Dialog(getContext());
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                dialog.setContentView(R.layout.start_workout);
                startWorkoutTextView = (TextView) dialog.findViewById(R.id.startWorkoutTextView);
                startWorkoutListView = (ListView) dialog.findViewById(R.id.startWorkoutListView);
                startWorkoutTextView.setText(itemValue);
                dialog.setTitle("Start Workout");
                Button cancelWorkoutButton = (Button) dialog.findViewById(R.id.cancelWorkoutButton);
                cancelWorkoutButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                populateCurrentRoutineList(itemValue);
                routineAdapter = new RoutineAdapter(getContext(), routineRepo.getRoutine(itemValue), startWorkoutListView);
                routineAdapter.setRoutineAdapter(routineAdapter);
                startWorkoutListView.setAdapter(routineAdapter);


                Button addExercisesButton = (Button) dialog.findViewById(R.id.addExercisesButton);
                addExercisesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //ADD ROUTINE SCREEN
                        final Dialog dialog2 = new Dialog(getContext());
                        dialog2.setContentView(R.layout.add_routine_exercises);
                        dialog2.setTitle("Add Exercises to Routine");
                        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                        populateList();

                        addRoutineExercisesListView = (ListView) dialog2.findViewById(R.id.addRoutineExercisesListView);
                        adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, exercises);
                        addRoutineExercisesListView.setAdapter(adapter2);
                        addRoutineExercisesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                        addRoutineExercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                //print(""+i);
                                addRoutineExercisesListView.setItemChecked(i, addRoutineExercisesListView.isItemChecked(i));
                            }
                        });

                        filterRoutineExerciseEditText = (EditText) dialog2.findViewById(R.id.filterRoutineExerciseEditText);
                        filterRoutineExerciseEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                print(charSequence.toString());
                                adapter2.getFilter().filter(charSequence);
                            }

                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                            }
                        });


                        Button addExerciseToRoutineButton = (Button) dialog2.findViewById(R.id.addExerciseToRoutineButton);
                        addExerciseToRoutineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //ADD NEW EXERCISES TO ADD EXERCISES SCREEN
                                final Dialog dialog3 = new Dialog(getContext());
                                dialog3.setContentView(R.layout.add_new_exercise);
                                dialog3.setTitle("Add New Exercise");

                                final TextInputLayout addExerciseTIL = (TextInputLayout) dialog3.findViewById(R.id.addExerciseTIL);
                                addExerciseTIL.setErrorEnabled(true);

                                final EditText addExerciseEditText = (EditText) dialog3.findViewById(R.id.addExerciseEditText);

                                TextView categoryLabel = (TextView) dialog3.findViewById(R.id.categoryLabel);
                                categoryLabel.setTextColor(getResources().getColor(R.color.blue));


                                final Spinner categorySpinner = (Spinner) dialog3.findViewById(R.id.categorySpinner);
                                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listOfCategories);
                                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                categorySpinner.setAdapter(categoryAdapter);


                                Button addExerciseOKButton = (Button) dialog3.findViewById(R.id.addExerciseOKButton);
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
                                            dialog3.dismiss();


                                        }
                                    }
                                });

                                Button addExerciseCancelButton = (Button) dialog3.findViewById(R.id.addExerciseCancelButton);
                                addExerciseCancelButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog3.dismiss();
                                    }
                                });


                                dialog3.show();
                            }
                        });

                        Button addRoutineExercisesOKButton = (Button) dialog2.findViewById(R.id.addRoutineExercisesOKButton);
                        addRoutineExercisesOKButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SparseBooleanArray checked = addRoutineExercisesListView.getCheckedItemPositions();
                                int length = checked.size();
                                if (length > 0) {
                                    for (int i = 0; i < length; i++) {
                                        int key = checked.keyAt(i);
                                        boolean value = checked.get(key);
                                        if (value) {
                                            String toADD = (String) addRoutineExercisesListView.getItemAtPosition(key);
                                            List<String> listOfCurrentExercises = new ArrayList<String>();
                                            for (Routine r: routineRepo.getRoutine(itemValue)){
                                                listOfCurrentExercises.add(r.getExercise());
                                            }
                                            if (!listOfCurrentExercises.contains(toADD)){
                                                listOfNewRoutineExercises.add(toADD);
                                            }
                                        }
                                    }

                                    //add the exercises to the current routine
                                    //update the adapter to reflect changes
                                    addRoutine(itemValue, listOfNewRoutineExercises);
                                    Toast.makeText(getContext(), "Added " + listOfNewRoutineExercises.size() + " exercises!", Toast.LENGTH_SHORT).show();
                                    listOfNewRoutineExercises.clear();
                                    routineAdapter = new RoutineAdapter(getContext(), routineRepo.getRoutine(itemValue), startWorkoutListView);
                                    routineAdapter.setRoutineAdapter(routineAdapter);
                                    startWorkoutListView.setAdapter(routineAdapter);
                                }

                                dialog2.dismiss();
                            }
                        });

                        Button addRoutineExercisesCancelButton = (Button) dialog2.findViewById(R.id.addRoutineExercisesCancelButton);
                        addRoutineExercisesCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog2.dismiss();
                            }
                        });

                        dialog2.show();
                    }
                });

                dialog.show();

            }
        });
    }

    private void populateList(){
        listOfExercises = exerciseRepo.getExercises();
        if (!listOfExercises.isEmpty()){
            exercises.clear();
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

    private void updateList(){
        populateList();
        adapter2.notifyDataSetChanged();
    }

    /*
     * This function populates a list variable that represents the current routine's exercises.
     */
    private void populateCurrentRoutineList(String name){
        List<Routine> currentExerciseList = routineRepo.getRoutine(name);
        listOfCurrentRoutineExercises.clear();
        for (Routine r: currentExerciseList){
            listOfCurrentRoutineExercises.add(r.getExercise());
        }
        Collections.sort(listOfCurrentRoutineExercises, new Comparator<String>() {
            @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
        });
    }
    /*
     *  This function updates the list variable that represents the current routine's exercises.
     */
    private void updateCurrentRoutineList(String name){

    }
    private void addRoutine(String name, List<String> exercises){
        Routine r = new Routine();
        for (String e: exercises){
            r.setName(name);
            r.setExercise(e);
            routineRepo.insert(r);
        }
    }

    private void print(String x){
        Log.d(x,x);
    }

    private void populateRoutineList(){
        List<Routine> listOfRoutines = routineRepo.getAllRoutines();
        routines.clear();
        for (Routine r: listOfRoutines){
            routines.add(r.getName());
        }
        Collections.sort(routines, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

    }
    private void updateRoutineList(){
        populateRoutineList();
        routineListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, routines);
        routineListView.setAdapter(routineListAdapter);
    }

    private void addExercise(String name, String category){
        Exercise e = new Exercise();
        e.setName(name);
        e.setCategory(category);
        exerciseRepo.insert(e);
    }
}
