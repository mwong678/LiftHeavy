package liftheavy.com.wongmatthew.liftheavy;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Matthew on 10/11/2017.
 * Takes in a hashmap instead of an array
 * uses the key specified to find how many of the current set there are
 */

public class TableAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private HashMap<String, Integer> mDataSource;
    private String key;
    private Handler mHandler;

    public TableAdapter(Context context, HashMap<String, Integer> hashMap, String s){
        mContext = context;
        mDataSource = hashMap;
        key = s;
        mHandler = new Handler();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mDataSource.get(key) != null){
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
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_table, parent, false);

        final TextView setNumber = (TextView) rowView.findViewById(R.id.setNumber);
        final EditText weight = (EditText) rowView.findViewById(R.id.weight);
        final EditText reps = (EditText) rowView.findViewById(R.id.reps);
        final int pos = position;

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setNumber.setText(Integer.toString(pos+1));
                        //remove underline
                        weight.getBackground().clearColorFilter();
                        reps.getBackground().clearColorFilter();
                    }
                });
            }
        }).start();*/

        setNumber.setText(Integer.toString(pos+1));
        //remove underline
        weight.getBackground().clearColorFilter();
        reps.getBackground().clearColorFilter();


        return rowView;
    }
}
