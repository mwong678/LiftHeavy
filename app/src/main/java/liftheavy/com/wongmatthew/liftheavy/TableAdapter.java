package liftheavy.com.wongmatthew.liftheavy;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static liftheavy.com.wongmatthew.liftheavy.App.getContext;

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
        //HashMap<Integer, String[]> saveState;
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

    private void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tableAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
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
