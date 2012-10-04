package au.id.teda.broadband.usage.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ActionbarSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

	String[] items = new String[] {"One", "Two", "Three"};
	
	
	public ActionbarSpinnerAdapter(Context context) {

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public Object getItem(int position){
		return mSpinnerList.get(itemPosition);
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
        mTextView.setText((CharSequence) mSpinnerList.get(position));
        
        return mView;
	}

}
