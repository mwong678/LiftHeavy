package liftheavy.com.wongmatthew.liftheavy;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * This custom adapter is used in order to customize what a list item on the exercise list looks
 * like.
 */

public class RoutineAdapter extends BaseAdapter {

    static class ViewHolder {
        Routine routine;
        TextView exerciseName;
        Button addSetButton;
        ListView workoutListView;
        SwipeDismissListViewTouchListener touchListener;
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Routine> mDataSource;
    private TableAdapter tableAdapter;
    private int defaultSetCount = 3;
    private HashMap<String, Integer> overallSetCounts;
    private HashMap<String, TableAdapter> tableAdapterHashMap;
    private RoutineAdapter routineAdapter;
    private ListView listView;
    private RoutineRepo routineRepo;
    private String routineName;
    private Handler mHandler;


    public RoutineAdapter(Context context, List<Routine> items, ListView lv, String rn) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        overallSetCounts = new HashMap<String, Integer>();
        tableAdapterHashMap = new HashMap<String, TableAdapter>();
        listView = lv;
        routineName = rn;
        mHandler = new Handler();
        routineRepo = new RoutineRepo();
        for (Routine r : mDataSource) {
            Log.d("DATASOURCE "+mDataSource, r.getExercise());
        }

    }

    public void setRoutineAdapter(RoutineAdapter ra) {
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
        final ViewHolder viewHolder;
        final int pos = position;
        View rowView = convertView;

        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.list_item_workout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.routine = (Routine) getItem(pos);
            final String name = viewHolder.routine.getExercise();
            viewHolder.exerciseName = (TextView) rowView.findViewById(R.id.exerciseName);
            viewHolder.addSetButton = (Button) rowView.findViewById(R.id.addSetButton);
            viewHolder.workoutListView = (ListView) rowView.findViewById(R.id.workoutListView);
            //viewHolder.tableAdapter = new TableAdapter(mContext, overallSetCounts, name);
            //Log.d("POSITION "+position, name);
            overallSetCounts.put(name, defaultSetCount);
            tableAdapterHashMap.put(name, new TableAdapter(mContext, overallSetCounts, name));
            tableAdapterHashMap.get(name).setTableAdapter(tableAdapterHashMap.get(name));
            viewHolder.exerciseName.setText(name);

            viewHolder.workoutListView.setAdapter(tableAdapterHashMap.get(name));
            setListViewHeightBasedOnChildren(viewHolder.workoutListView);


            //set tag for row
            rowView.setTag(viewHolder);


            viewHolder.touchListener = new SwipeDismissListViewTouchListener(viewHolder.workoutListView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                @Override
                public boolean canDismiss(int position) {
                    return true;
                }

                @Override
                public void onDismiss(ListView listView, int[] reverseSortedPositions) {

                    for (int position : reverseSortedPositions) {
                        overallSetCounts.put(name, overallSetCounts.get(name) - 1);
                    }
                    updateSetCount(name, viewHolder.workoutListView);


                    //remove exercise if all the sets are gone
                    String isZero = anyZero(viewHolder);
                    if (isZero != null && routineAdapter != null) {
                        overallSetCounts.remove(isZero);
                        int mark = -1;
                        for (int i = 0; i < mDataSource.size(); i++) {
                            if (mDataSource.get(i).getExercise().equals(isZero)) {
                                mark = i;
                            }
                        }
                        if (mark >= 0) {
                            mDataSource.remove(mark);
                            Log.d("Removed", isZero + "  " + mark);
                            routineRepo.deleteExercise(routineName, isZero);
                        }

                        routineAdapter.notifyDataSetChanged();
                    }
                }
            });

            viewHolder.workoutListView.setOnTouchListener(viewHolder.touchListener);


            viewHolder.workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String itemValue = (String) adapterView.getItemAtPosition(i);
                    //Log.d("", itemValue);
                }
            });

            viewHolder.addSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    overallSetCounts.put(name, overallSetCounts.get(name) + 1);
                    updateSetCount(name, viewHolder.workoutListView);
                }
            });
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
            final String name = ((Routine) getItem(pos)).getExercise();
            viewHolder.exerciseName.setText(name);
            if (!overallSetCounts.containsKey(name))
                overallSetCounts.put(name, defaultSetCount);
            if (!tableAdapterHashMap.containsKey(name)) {
                tableAdapterHashMap.put(name, new TableAdapter(mContext, overallSetCounts, name));
                tableAdapterHashMap.get(name).setTableAdapter(tableAdapterHashMap.get(name));
            }
            viewHolder.workoutListView.setAdapter(tableAdapterHashMap.get(name));
            tableAdapterHashMap.get(name).setTableAdapter(tableAdapterHashMap.get(name));
            updateSetCount(name, viewHolder.workoutListView);

            viewHolder.addSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    overallSetCounts.put(name, overallSetCounts.get(name) + 1);
                    updateSetCount(name, viewHolder.workoutListView);
                }
            });

        }


        return rowView;
    }

    private String anyZero(ViewHolder viewHolder) {
        for (String key : overallSetCounts.keySet()) {
            if (overallSetCounts.get(key) <= 0) {
                return key;
            }
        }
        return null;
    }

    private void updateSetCount(String name, ListView listView) {
        tableAdapterHashMap.get(name).notifyDataSetChanged();
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
