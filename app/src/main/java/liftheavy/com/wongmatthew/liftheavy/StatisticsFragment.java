package liftheavy.com.wongmatthew.liftheavy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Matthew on 8/13/2016.
 */

public class StatisticsFragment extends Fragment{

    public StatisticsFragment(){


    }

    public static StatisticsFragment newInstance(){
        StatisticsFragment statisticsFragment = new StatisticsFragment();
        return statisticsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.statistics, container, false);
    }

}
