package liftheavy.com.wongmatthew.liftheavy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This custom adapter is used in order to customize what a list item on the exercise list looks
 * like.
 */

public class RoutineAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Routine> mDataSource;
    private TableAdapter tableAdapter;
    private int defaultSetCount = 3;
    private Button addSetButton;
    private HashMap<String, Integer> setCounts;


    public RoutineAdapter(Context context, List<Routine> items){
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setCounts  = new HashMap<String, Integer>();
        for (Routine r:mDataSource){
            setCounts.put(r.getExercise(), defaultSetCount);
        }
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_workout, parent, false);
        Routine r = (Routine) getItem(position);
        final String name = r.getExercise();
        final TextView exerciseName = (TextView) rowView.findViewById(R.id.exerciseName);
        exerciseName.setText(name);

        addSetButton = (Button) rowView.findViewById(R.id.addSetButton);
        final ListView workoutListView = (ListView) rowView.findViewById(R.id.workoutListView);

        tableAdapter = new TableAdapter(mContext, setCounts, name);
        workoutListView.setAdapter(tableAdapter);
        setListViewHeightBasedOnChildren(workoutListView);
        workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemValue = (String) adapterView.getItemAtPosition(i);
            }
        });

        addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCounts.put(name, setCounts.get(name) + 1);
                updateSetCount(workoutListView);
            }
        });



        return rowView;
    }

    private void updateSetCount(ListView listView){
        tableAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listView);
    }

    /*
    This function determines the calculated length of a listview and makes it so
    This is specifically for the context of nested ListViews
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
