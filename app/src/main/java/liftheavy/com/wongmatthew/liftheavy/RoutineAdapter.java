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
    private int defaultSetCount = 3;
    private HashMap<String, Integer> overallSetCounts;
    private HashMap<String, TableAdapter> tableAdapterHashMap;
    private RoutineAdapter routineAdapter;
    private ListView listView;
    private RoutineRepo routineRepo;
    private String routineName;


    public RoutineAdapter(Context context, List<Routine> items, ListView lv, String rn) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        overallSetCounts = new HashMap<String, Integer>();
        tableAdapterHashMap = new HashMap<String, TableAdapter>();
        listView = lv;
        routineName = rn;
        routineRepo = new RoutineRepo();
    }

    public void setRoutineAdapter(RoutineAdapter ra) {
        routineAdapter = ra;
    }

    public void setDataSource(List<Routine> items){
        mDataSource = items;
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

        /*
        If the rowview is null, then this first case occurs.
        The elements are set, as well as the listeners for them.
         */
        if (rowView == null) {
            //set rowview equal to the specified xml
            rowView = mInflater.inflate(R.layout.list_item_workout, parent, false);
            //create viewholder object
            viewHolder = new ViewHolder();
            //set the fields for the viewholder object
            viewHolder.routine = (Routine) getItem(pos);
            final String name = viewHolder.routine.getExercise();
            viewHolder.exerciseName = (TextView) rowView.findViewById(R.id.exerciseName);
            viewHolder.addSetButton = (Button) rowView.findViewById(R.id.addSetButton);
            viewHolder.workoutListView = (ListView) rowView.findViewById(R.id.workoutListView);

            //insert into hashmap the default number of sets as well as the exercise
            if (!overallSetCounts.containsKey(name))
                overallSetCounts.put(name, defaultSetCount);

            //create a tableadapter for each exercise that uses overallSetCounts as a dataSource.
            if (!tableAdapterHashMap.containsKey(name)) {
                //put the element in the map, and then set the adapter equal to it.
                tableAdapterHashMap.put(name, new TableAdapter(mContext, overallSetCounts, name));
                //if there are already values in the list,
                for (String key:tableAdapterHashMap.keySet()){
                    tableAdapterHashMap.get(key).setDataSource(overallSetCounts);
                    updateSetCount(key, viewHolder.workoutListView);
                }
                tableAdapterHashMap.get(name).setTableAdapter(tableAdapterHashMap.get(name));
            }

            viewHolder.exerciseName.setText(name);
            viewHolder.workoutListView.setAdapter(tableAdapterHashMap.get(name));

            //set height of listview to show all rows
            setListViewHeightBasedOnChildren(viewHolder.workoutListView);

            //set tag for row
            rowView.setTag(viewHolder);

            //this is for when you swipe a row away in the exercise list.
            viewHolder.touchListener = new SwipeDismissListViewTouchListener(viewHolder.workoutListView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                @Override
                public boolean canDismiss(int position) {
                    return true;
                }

                @Override
                public void onDismiss(ListView listView, int[] reverseSortedPositions) {

                    //for each position, minus one set from the overall counts for the
                    //specified position
                    for (int position : reverseSortedPositions) {
                        overallSetCounts.put(name, overallSetCounts.get(name) - 1);
                        //remove selected row from the table
                        tableAdapterHashMap.get(name).removeSet(name, position, tableAdapterHashMap.size());
                    }
                    //update the setcount
                    updateSetCount(name, viewHolder.workoutListView);


                    //remove exercise if all the sets are gone
                    String isZero = anyZero();
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
                    //TODO
                }
            });

            //what happens when you add a new set to the table
            viewHolder.addSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    overallSetCounts.put(name, overallSetCounts.get(name) + 1);
                    updateSetCount(name, viewHolder.workoutListView);
                }
            });
        } else {
            //this is the case where the viewholder isn't null

            //get tge tagged viewholder
            viewHolder = (ViewHolder) rowView.getTag();
            final String name = ((Routine) getItem(pos)).getExercise();
            viewHolder.exerciseName.setText(name);
            //place in overallsetcount if doesn't exist
            if (!overallSetCounts.containsKey(name))
                overallSetCounts.put(name, defaultSetCount);
            //also add the table adapter for the current exercise
            if (!tableAdapterHashMap.containsKey(name)) {
                //put the element in the map, and then set the adapter equal to it.
                tableAdapterHashMap.put(name, new TableAdapter(mContext, overallSetCounts, name));
                //if there are already values in the list,
                for (String key:tableAdapterHashMap.keySet()){
                    tableAdapterHashMap.get(key).setDataSource(overallSetCounts);
                    updateSetCount(key, viewHolder.workoutListView);
                }
                tableAdapterHashMap.get(name).setTableAdapter(tableAdapterHashMap.get(name));
            }
            viewHolder.workoutListView.setAdapter(tableAdapterHashMap.get(name));
            tableAdapterHashMap.get(name).setTableAdapter(tableAdapterHashMap.get(name));
            updateSetCount(name, viewHolder.workoutListView);

            viewHolder.touchListener = new SwipeDismissListViewTouchListener(viewHolder.workoutListView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                @Override
                public boolean canDismiss(int position) {
                    return true;
                }

                @Override
                public void onDismiss(ListView listView, int[] reverseSortedPositions) {

                    //for each position, minus one set from the overall counts for the
                    //specified position
                    for (int position : reverseSortedPositions) {
                        overallSetCounts.put(name, overallSetCounts.get(name) - 1);
                        //remove selected row from the table
                        tableAdapterHashMap.get(name).removeSet(name, position, tableAdapterHashMap.size());
                    }
                    //update the setcount
                    updateSetCount(name, viewHolder.workoutListView);

                    //remove exercise if all the sets are gone
                    String isZero = anyZero();
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
                            routineRepo.deleteExercise(routineName, isZero);
                        }

                        routineAdapter.notifyDataSetChanged();
                    }
                }
            });

            viewHolder.workoutListView.setOnTouchListener(viewHolder.touchListener);

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


    //this function checks if any of the overallsetcounts have a 0 in them
    private String anyZero() {
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
