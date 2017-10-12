package liftheavy.com.wongmatthew.liftheavy;

/**
 * Created by Matthew on 8/14/2016.
 */
public class Routine {
    public static final String TAG = Routine.class.getSimpleName();
    public static final String TABLE = "Routine";
    //Columns
    public static final String KEY_Name = "Name";
    public static final String KEY_Exercise = "Exercise";

    private String name;
    private String exercise;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getExercise(){
        return exercise;
    }
    public void setExercise(String exercise){
        this.exercise = exercise;
    }

}
