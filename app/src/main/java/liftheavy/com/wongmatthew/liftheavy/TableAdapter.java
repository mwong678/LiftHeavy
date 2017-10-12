package liftheavy.com.wongmatthew.liftheavy;

import android.content.Context;
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

    public TableAdapter(Context context, HashMap<String, Integer> hashMap, String s){
        mContext = context;
        mDataSource = hashMap;
        key = s;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.get(key);
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

        TextView setNumber = (TextView) rowView.findViewById(R.id.setNumber);
        EditText weight = (EditText) rowView.findViewById(R.id.weight);
        EditText reps = (EditText) rowView.findViewById(R.id.reps);
        setNumber.setText(Integer.toString(position+1));

        //remove underline
        weight.getBackground().clearColorFilter();
        reps.getBackground().clearColorFilter();


        return rowView;
    }
}
