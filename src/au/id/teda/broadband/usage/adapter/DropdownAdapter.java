package au.id.teda.broadband.usage.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DropdownAdapter extends BaseAdapter {

	private List<Map<String, String>> mSpinnerItems;
    private LayoutInflater mLayoutInflater;
    private int itemPosition;
	
	public DropdownAdapter(Context context) {
		mSpinnerItems = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("title", "Title 1");
		map.put("activity", "Activity 1");
		//map.put( "fragment", Fragment.instantiate( context, Fragment1.class.getName() ));
		mSpinnerItems.add(map);
		
		map = new HashMap<String, String>();
		map.put("title", "Title 2");
		map.put("activity", "Activity 2");
		//map.put( "fragment", Fragment.instantiate( this, Fragment2.class.getName() ));
		mSpinnerItems.add(map);
		
		// Retrieve the layout inflater from the provided context
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position){
		return mSpinnerItems.get(itemPosition);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View mView = convertView;
		 
        // Try to reuse views as much as possible.
        // It is alot faster than inflating new views all the time
        // and it saves quite a bit on memory usage aswell.
        if (convertView == null) {
            // inflate a new layout for the view.
        	mView = mLayoutInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
 
        TextView mTextView = (TextView) mView.findViewById(android.R.id.text1);
        // FindViewById<TextView>(Resource.Id.DisplayTextLabel);
        mTextView.setText((CharSequence) mSpinnerItems.get(position));
        
        return mView;
	}

}
