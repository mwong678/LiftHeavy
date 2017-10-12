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
public class RoutineRepo {

    private Routine routine;

    public RoutineRepo(){

        routine = new Routine();

    }


    public static String createTable(){
        return "CREATE TABLE " + Routine.TABLE  + "("
                + Routine.KEY_Name  + " TEXT,"
                + Routine.KEY_Exercise + " TEXT)";
    }


    public void insert(Routine routine) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Routine.KEY_Name, routine.getName());
        values.put(Routine.KEY_Exercise, routine.getExercise());
        // Inserting Row
        Log.d(routine.getName(),routine.getExercise());
        db.insertWithOnConflict(Routine.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        //db.insert(Routine.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteRoutine(String name){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.execSQL("DELETE FROM " + Routine.TABLE + " WHERE " + Routine.KEY_Name + "= '" + name + "'");
        DatabaseManager.getInstance().closeDatabase();
    }

    public void delete( ) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Routine.TABLE, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<Routine> getRoutine(String name){
        List<Routine> routineList = new ArrayList<Routine>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        //String selectQuery = "SELECT * FROM " + Routine.TABLE + " WHERE " + Routine.KEY_Name + "=?" + "\""+name+"\"";
        String selectQuery = "SELECT * FROM " + Routine.TABLE + " WHERE " + Routine.KEY_Name + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});
        if (cursor.moveToFirst()){
            do{
                routine = new Routine();
                routine.setName(cursor.getString(cursor.getColumnIndex(Routine.KEY_Name)));
                routine.setExercise(cursor.getString(cursor.getColumnIndex(Routine.KEY_Exercise)));

                routineList.add(routine);
            }while (cursor.moveToNext());

        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return routineList;
    }

    public List<Routine> getAllRoutines(){
        List<Routine> routineList = new ArrayList<Routine>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT DISTINCT("+Routine.KEY_Name+") FROM " + Routine.TABLE;

        //Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor = db.query(true, Routine.TABLE, new String[]{Routine.KEY_Name, Routine.KEY_Exercise},null,null,Routine.KEY_Name,null,null,null);
        if (cursor.moveToFirst()){
            do{
                routine = new Routine();
                routine.setName(cursor.getString(cursor.getColumnIndex(Routine.KEY_Name)));
                routine.setExercise(cursor.getString(cursor.getColumnIndex(Routine.KEY_Exercise)));

                routineList.add(routine);
            }while (cursor.moveToNext());

        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return routineList;
    }
}
