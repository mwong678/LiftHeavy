package liftheavy.com.wongmatthew.liftheavy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 8/14/2016.
 */
public class ExerciseRepo  {

    private Exercise e;
    private final String TAG = ExerciseRepo.class.getSimpleName().toString();

    public ExerciseRepo(){



    }


    public static String createTable(){
        return "CREATE TABLE " + Exercise.TABLE  + "("
                + Exercise.KEY_Name  + "   PRIMARY KEY,"
                + Exercise.KEY_Category + " TEXT)";
    }


    public void insert(Exercise exercise) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Exercise.KEY_Name, exercise.getName());
        values.put(Exercise.KEY_Category, exercise.getCategory());
        // Inserting Row
        db.insert(Exercise.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }



    public void delete( ) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Exercise.TABLE, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public boolean delete(String name){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        //boolean answer = db.delete(Exercise.TABLE, Exercise.KEY_Name + "=" + name, null) > 0;
        boolean answer = db.delete(Exercise.TABLE, Exercise.KEY_Name + "=?", new String[]{String.valueOf(name)}) > 0;
        DatabaseManager.getInstance().closeDatabase();
        return answer;
    }

    public boolean update(String name, ContentValues values){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        boolean answer = db.update(Exercise.TABLE, values, Exercise.KEY_Name + "=?", new String[]{String.valueOf(name)}) > 0;
        DatabaseManager.getInstance().closeDatabase();
        return answer;
    }

    public List<Exercise> getExercises(){
        List<Exercise> exerciseList = new ArrayList<Exercise>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT * FROM " + Exercise.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                e = new Exercise();
                e.setName(cursor.getString(cursor.getColumnIndex(Exercise.KEY_Name)));
                e.setCategory(cursor.getString(cursor.getColumnIndex(Exercise.KEY_Category)));

                exerciseList.add(e);
            }while (cursor.moveToNext());

        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return exerciseList;
    }




}