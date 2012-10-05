package au.id.teda.broadband.usage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

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
		return position;
		
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return parent;
	}

}
