package liftheavy.com.wongmatthew.liftheavy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private BottomBar bottomBar;
    private FragNavController fragNavController;

    private final int INDEX_ROUTINES = FragNavController.TAB1;
    private final int INDEX_EXERCISES = FragNavController.TAB2;
    private final int INDEX_STATISTICS = FragNavController.TAB3;
    private final int INDEX_SETTINGS = FragNavController.TAB4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<android.support.v4.app.Fragment> fragmentList = new ArrayList<>(4);

        fragmentList.add(RoutinesFragment.newInstance());
        fragmentList.add(ExercisesFragment.newInstance());
        fragmentList.add(StatisticsFragment.newInstance());
        fragmentList.add(SettingsFragment.newInstance());

        fragNavController = new FragNavController(getSupportFragmentManager(), R.id.container, fragmentList);

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setMaxFixedTabs(4);
        bottomBar.setItems(R.menu.menu_main);
        //bottomBar.setActiveTabColor("#2196F3");
        //bottomBar.mapColorForTab(0, "#4CAF50");
        //bottomBar.mapColorForTab(1, "#2196F3");
        //bottomBar.mapColorForTab(2, "#F44336");
        //bottomBar.mapColorForTab(3, "#9E9E9E");


        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(int i) {

                switch (i) {
                    case R.id.routines:
                        fragNavController.switchTab(INDEX_ROUTINES);
                        bottomBar.setActiveTabColor("#4CAF50");
                        break;
                    case R.id.dumbbell:
                        fragNavController.switchTab(INDEX_EXERCISES);
                        bottomBar.setActiveTabColor("#2196F3");
                        break;
                    case R.id.statistics:
                        fragNavController.switchTab(INDEX_STATISTICS);
                        bottomBar.setActiveTabColor("#F44336");
                        break;
                    case R.id.settings:
                        fragNavController.switchTab(INDEX_SETTINGS);
                        bottomBar.setActiveTabColor("#9E9E9E");
                        break;

                }

            }

            @Override
            public void onMenuTabReSelected(int i) {
                fragNavController.clearStack();
            }
        });






        //insertData();

    }

    @Override
    public void onBackPressed() {
        if (fragNavController.getCurrentStack().size() > 1) {
            fragNavController.pop();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState(outState);
    }



}
