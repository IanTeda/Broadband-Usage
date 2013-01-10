package au.id.teda.broadband.usage.ui.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.cursoradapter.DailyDataTableCursorAdapter;
import au.id.teda.broadband.usage.database.DailyDataDatabaseAdapter;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.ui.MainActivity;

import com.actionbarsherlock.app.SherlockListFragment;

public class DataTableListFragment extends SherlockListFragment {

	private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	private ArrayList<String> dataname = new ArrayList<String>();
	private ArrayList<String> datacode = new ArrayList<String>();
	
	private Context mContext;
	
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	
	private DailyDataDatabaseAdapter mDatabase;
	
	// Set TextView Objects
	private TextView mPeriodTv;
	
	 public static class viewHolder {
		  TextView tname;
		  TextView tcode;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.d(DEBUG_TAG, "DataTableListFragment.onCreate()");
		
		mContext = getSherlockActivity();

		  for (int i = 0; i < 10; i++) {
		   dataname.add("Name" + i);
		   datacode.add("Code" + i);
		  }

		  setListAdapter(new EfficientAdapter(mContext));
		/**
    	mAccountInfo = new AccountInfoHelper(mContext);
    	mAccountStatus = new AccountStatusHelper(mContext);
    	
    	mDatabase = new DailyDataDatabaseAdapter(mContext);
    	mDatabase.open();
    	
    	String period = mAccountStatus.getDataBaseMonthString();
    	Log.d(DEBUG_TAG, "Month:" + period);
    	Cursor cursor = mDatabase.getPriodUsageCursor(period);
    	
    	DailyDataTableCursorAdapter adapter = new DailyDataTableCursorAdapter(mContext, cursor, false);
    	setListAdapter(adapter);
    	
    	mDatabase.close();
    	**/   	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		Log.d(DEBUG_TAG, "DataTableListFragment.onCreateView()");
		
		View mFragementView = inflater.inflate(R.layout.listfragment_data_table, container, false);
		
		//mPeriodTv = (TextView) mFragementView.findViewById(R.id.listfragment_data_table_title);
		//mPeriodTv.setText(mAccountStatus.getCurrentMonthString());
		
		return mFragementView;
	}
	
	private class EfficientAdapter extends BaseAdapter {

		  private Context mContext;
		  LayoutInflater inflater;

		  public EfficientAdapter(Context context) {
		   // TODO Auto-generated constructor stub
		   this.mContext = context;
		   inflater = LayoutInflater.from(context);

		  }

		  @Override
		  public int getCount() {
		   return datacode.size();
		  }

		  @Override
		  public Object getItem(int position) {
		   return position;
		  }

		  @Override
		  public long getItemId(int position) {
		   return position;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		   viewHolder holder;
		   if (convertView == null) {
		    convertView = inflater.inflate(R.layout.listinflate, null);
		    holder = new viewHolder();

		    convertView.setTag(holder);

		   } else {
		    holder = (viewHolder) convertView.getTag();
		   }
		   holder.tname = (TextView) convertView
		     .findViewById(R.id.textViewName);
		   holder.tcode = (TextView) convertView
		     .findViewById(R.id.textViewCode);
		   holder.tname.setText(dataname.get(position));
		   holder.tcode.setText(datacode.get(position));

		   return convertView;
		  }

		 }

}
