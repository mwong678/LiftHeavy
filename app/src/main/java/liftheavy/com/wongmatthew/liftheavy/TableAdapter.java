package liftheavy.com.wongmatthew.liftheavy;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import java.util.HashMap;


/**
 * Created by Matthew on 10/11/2017.
 * Takes in a hashmap instead of an array
 * uses the key specified to find how many of the current set there are
 */

public class TableAdapter extends BaseAdapter {
    static class ViewHolder {
        TextView setNumber;
        EditText weight;
        EditText reps;
        String key;
        int ref;
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private HashMap<String, Integer> mDataSource;
    private HashMap<String, String[]> saveState;
    private String key;
    private Handler mHandler;
    private TableAdapter tableAdapter;

    public TableAdapter(Context context, HashMap<String, Integer> hashMap, String s) {
        mContext = context;
        mDataSource = hashMap;
        key = s;
        mHandler = new Handler();
        saveState = new HashMap<String, String[]>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTableAdapter(TableAdapter ta) {
        tableAdapter = ta;
    }

    public void setDataSource(HashMap<String, Integer> items){
        mDataSource = items;
    }

    public void removeSet(String name, int position, int length){
        for (int i = position;i < length - 1;i++){
            String currKey = name + i;
            String nextKey = name+(i+1);
            String[] next = saveState.get(nextKey);
            saveState.put(currKey, next);
        }
        int prevSize = saveState.size();
        saveState.remove(name+(length-1));
        View v;
        EditText weight, reps;
        //iterate through list and clear null elements
        for (int i=0;i < prevSize;i++){
            v = tableAdapter.getView(i, null, null);
            weight = (EditText) v.findViewById(R.id.weight);
            reps = (EditText) v.findViewById(R.id.reps);
            String[] val = saveState.get(name+i);
            if (val == null){
                weight.setText("");
                reps.setText("");
            }
        }
        tableAdapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDataSource.get(key) != null) {
            return mDataSource.get(key);
        }
        return -1;
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(key);
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
        // Get view for row item
        if (rowView == null) {

            rowView = mInflater.inflate(R.layout.list_item_table, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.setNumber = (TextView) rowView.findViewById(R.id.setNumber);
            viewHolder.weight = (EditText) rowView.findViewById(R.id.weight);
            viewHolder.reps = (EditText) rowView.findViewById(R.id.reps);
            viewHolder.key = key;
            viewHolder.ref = position;

            viewHolder.setNumber.setText(Integer.toString(pos + 1));
            viewHolder.weight.getBackground().clearColorFilter();
            viewHolder.reps.getBackground().clearColorFilter();

            rowView.setTag(viewHolder);

            viewHolder.weight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    String weightValue = viewHolder.weight.getText().toString();
                    String repValue = viewHolder.reps.getText().toString();
                    saveState.put(viewHolder.key + viewHolder.ref, new String[]{weightValue, repValue});
                    tableAdapter.notifyDataSetChanged();
                }
            });

            viewHolder.reps.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String weightValue = viewHolder.weight.getText().toString();
                    String repValue = viewHolder.reps.getText().toString();
                    saveState.put(viewHolder.key + viewHolder.ref, new String[]{weightValue, repValue});
                    tableAdapter.notifyDataSetChanged();
                }
            });


        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        String[] values = saveState.get(viewHolder.key + viewHolder.ref);

        if (values != null) {
            if (!viewHolder.weight.getText().toString().equals(values[0])) {
                viewHolder.weight.setText(values[0]);

            }
            if (!viewHolder.reps.getText().toString().equals(values[1])) {
                viewHolder.reps.setText(values[1]);

            }
        }


        return rowView;
    }
}
