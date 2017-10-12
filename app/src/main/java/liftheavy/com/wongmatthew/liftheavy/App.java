package liftheavy.com.wongmatthew.liftheavy;

import android.app.Application;
import android.content.Context;

/**
 * Created by Matthew on 8/14/2016.
 */
public class  App extends Application {
    private static Context context;
    private static DBHelper dbHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this.getApplicationContext();
        dbHelper = new DBHelper();
        DatabaseManager.initializeInstance(dbHelper);

    }


    public static Context getContext(){
        return context;
    }

}
