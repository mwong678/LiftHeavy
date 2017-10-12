package liftheavy.com.wongmatthew.liftheavy;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Matthew on 8/17/2016.
 */
public class HistoryRepo {
    private History history;

    public HistoryRepo(){
        history = new History();
    }

    public static String createTable(){
        return "CREATE TABLE " + History.TABLE  + "("
                + History.KEY_Routine  + "   TEXT,"
                + History.KEY_Exercise  + "   TEXT,"
                + History.KEY_Reps  + "   TEXT,"
                + History.KEY_Sets  + "   TEXT,"
                + History.KEY_Weight  + "   TEXT,"
                + History.KEY_Duration  + "   TEXT,"
                + History.KEY_Distance  + "   TEXT,"
                + History.KEY_Date + " TEXT)";
    }


    public void insert(Routine routine) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(History.KEY_Routine, history.getRoutine());
        values.put(History.KEY_Exercise, history.getExercise());
        values.put(History.KEY_Reps, history.getReps());
        values.put(History.KEY_Sets, history.getSets());
        values.put(History.KEY_Weight, history.getWeight());
        values.put(History.KEY_Duration, history.getDuration());
        values.put(History.KEY_Distance, history.getDistance());
        values.put(History.KEY_Date, history.getDate());
        // Inserting Row
        db.insert(History.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }



    public void delete( ) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(History.TABLE,null,null);
        DatabaseManager.getInstance().closeDatabase();
    }
}
