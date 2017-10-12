package liftheavy.com.wongmatthew.liftheavy;

/**
 * Created by Matthew on 8/14/2016.
 */
public class Exercise {

    public static final String TAG = Exercise.class.getSimpleName();
    public static final String TABLE = "Exercise";
    //Columns
    public static final String KEY_Name = "Name";
    public static final String KEY_Category = "Category";

    private String name;
    private String category;


    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }

}
