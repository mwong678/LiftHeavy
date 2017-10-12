package liftheavy.com.wongmatthew.liftheavy;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import liftheavy.com.wongmatthew.liftheavy.Exercise;
import liftheavy.com.wongmatthew.liftheavy.Routine;


/**
 * Created by Matthew on 8/14/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    //change whenever modifying table
    private static final int DATABASE_VERSION = 1;
    //database name
    private static final String DATABASE_NAME = "LiftHeavy.db";
    private static final String TAG = DBHelper.class.getSimpleName().toString();

    public DBHelper(){
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(ExerciseRepo.createTable());
        db.execSQL(RoutineRepo.createTable());
        db.execSQL(HistoryRepo.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        // Drop table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Exercise.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Routine.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + History.TABLE);
        onCreate(db);
    }
}
