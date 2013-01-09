package au.id.teda.broadband.usage.ui.fragments;

import java.text.ParseException;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.cursoradapter.DailyDataTableCursorAdapter;
import au.id.teda.broadband.usage.database.DailyDataDatabaseAdapter;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;

import com.actionbarsherlock.app.SherlockListFragment;

public class DataTableFragment extends SherlockListFragment {

	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	
	private DailyDataDatabaseAdapter mDatabase;
	
	// Set TextView Objects
	private TextView mPeriod;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
    	mAccountInfo = new AccountInfoHelper(getSherlockActivity());
    	mAccountStatus = new AccountStatusHelper(getSherlockActivity());
    	
    	mDatabase = new DailyDataDatabaseAdapter(getSherlockActivity());
    	mDatabase.open();
    	
    	String period = mAccountStatus.getCurrentMonthString();
    	Cursor cursor = mDatabase.getPriodUsageCursor(period);
    	
    	DailyDataTableCursorAdapter adapter = new DailyDataTableCursorAdapter(getSherlockActivity(), cursor, false);
    	setListAdapter(adapter);
    	
    	mDatabase.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 return inflater.inflate(R.layout.listfragment_data_table, container, false);
	}
	
	private void loadData() throws ParseException{
		/**
		// Get string value of current data period
		String preiod = myAccount.getCurrentDataPeriod();
		
		// Open database (i.e. set tables if needed)
		myDailyDataDB.open();
		
		// Set cursor object
		myDailyDBCursor = myDailyDataDB.fetchPeriodUsage(preiod);
		
		// Start managing cursor TODO: What does this mean?
		startManagingCursor(myDailyDBCursor);
		
		// Load data into cursor array
		setListAdapter(new DailyDataCursorAdapter(this, myDailyDBCursor));
		
		// Close database
		myDailyDataDB.close();
		**/
	}

}
