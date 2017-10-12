package liftheavy.com.wongmatthew.liftheavy;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
    private RoutineAdapter routineAdapter;
    private ListView listView;
    private RoutineRepo routineRepo;
    private String routineName;


    public RoutineAdapter(Context context, List<Routine> items, ListView lv, String rn){
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setCounts  = new HashMap<String, Integer>();
        listView = lv;
        routineName = rn;
        routineRepo = new RoutineRepo();
        for (Routine r:mDataSource){
            setCounts.put(r.getExercise(), defaultSetCount);
        }
    }
    public void setRoutineAdapter(RoutineAdapter ra){
        routineAdapter = ra;
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

        //swipe to delete a set
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(workoutListView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {

                for (int position : reverseSortedPositions){
                    setCounts.put(name, setCounts.get(name)-1);

                    tableAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(workoutListView);
                    /*
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler(Looper.myLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
                                @Override
                                public void run() {

                                }
                            });
                        }
                    });
                    thread.start();*/


                    //setListViewHeightBasedOnChildren(workoutListView);
                }

                //remove exercise if all the sets are gone
                String isZero = anyZero();
                if (isZero != null && routineAdapter != null){
                    setCounts.remove(isZero);
                    int mark = -1;
                    for (int i=0;i<mDataSource.size();i++){
                        if (mDataSource.get(i).getExercise().equals(isZero)){
                            mark = i;
                        }
                    }
                    if (mark >= 0){
                        mDataSource.remove(mark);
                        Log.d("Removed",isZero+"  "+mark);
                        routineRepo.deleteExercise(routineName, isZero);
                    }

                    routineAdapter.notifyDataSetChanged();
                }
            }
        });

        workoutListView.setOnTouchListener(touchListener);

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

    private String anyZero(){
        for (String key:setCounts.keySet()){
            if (setCounts.get(key) <= 0){
                return key;
            }
        }
        return null;
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
