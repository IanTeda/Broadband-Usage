package au.id.teda.broadband.usage.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.cursoradapter.DailyDataTableCursorAdapter;
import au.id.teda.broadband.usage.database.DailyDataDatabaseAdapter;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;

import com.actionbarsherlock.app.SherlockListFragment;

public class DataTableFragment extends SherlockListFragment {

	private Context mContext;
	
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	
	private DailyDataDatabaseAdapter mDatabase;
	
	// Set TextView Objects
	private TextView mPeriodTv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mContext = getSherlockActivity();
		
    	mAccountInfo = new AccountInfoHelper(mContext);
    	mAccountStatus = new AccountStatusHelper(mContext);
    	
    	mDatabase = new DailyDataDatabaseAdapter(mContext);
    	mDatabase.open();
    	
    	String period = mAccountStatus.getCurrentMonthString();
    	Cursor cursor = mDatabase.getPriodUsageCursor(period);
    	
    	DailyDataTableCursorAdapter adapter = new DailyDataTableCursorAdapter(mContext, cursor, false);
    	setListAdapter(adapter);
    	
    	mDatabase.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mFragementView = inflater.inflate(R.layout.listfragment_data_table, container, false);
		
		//mPeriodTv = (TextView) mFragementView.findViewById(R.id.listfragment_data_table_title);
		//mPeriodTv.setText(mAccountStatus.getCurrentMonthString());
		
		return mFragementView;
	}

}
