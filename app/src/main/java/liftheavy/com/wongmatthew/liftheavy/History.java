package liftheavy.com.wongmatthew.liftheavy;

/**
 * Created by Matthew on 8/17/2016.
 */
public class History {
    public static final String TAG = History.class.getSimpleName();
    public static final String TABLE = "History";
    //Columns
    public static final String KEY_Id = "Id";
    public static final String KEY_Routine = "Routine";
    public static final String KEY_Exercise = "Exercise";
    public static final String KEY_Reps = "Reps";
    public static final String KEY_Sets = "Sets";
    public static final String KEY_Weight = "Weight";
    public static final String KEY_Duration = "Duration";
    public static final String KEY_Distance = "Distance";
    public static final String KEY_Date = "Date";

    private String routine;
    private String id;
    private String exercise;
    private String reps;
    private String sets;
    private String weight;
    private String duration;
    private String distance;
    private String date;

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getRoutine(){
        return routine;
    }
    public void setRoutine(String routine){
        this.routine = routine;
    }

    public String getExercise(){
        return exercise;
    }
    public void setExercise(String exercise){
        this.exercise = exercise;
    }

    public String getSets(){
        return sets;
    }
    public void setSets(String sets){
        this.sets = sets;
    }

    public String getReps(){
        return reps;
    }
    public void setReps(String reps){
        this.reps = reps;
    }
    public String getWeight(){
        return weight;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }

    public String getDuration(){
        return duration;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    public String getDistance(){
        return distance;
    }
    public void setDistance(String distance){
        this.distance = distance;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }
}
