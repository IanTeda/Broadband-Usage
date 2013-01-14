package au.id.teda.broadband.usage.cursoradapter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.database.DailyDataDatabaseAdapter;
import au.id.teda.broadband.usage.ui.MainActivity;

public class DailyDataTableCursorAdapter extends CursorAdapter {
	
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	LayoutInflater mInflater; 

	private Context mContext;
	
	private int count;
	
	private final class ViewHolder {
	    public TextView day;
	    public TextView peak;
	    public TextView offpeak;
	    public TextView uploads;
	    public TextView freezone;
	    public TextView total;
	    public TextView accum;
	}
	
	public DailyDataTableCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
		super(context, cursor, autoRequery);
		Log.d(DEBUG_TAG, "DailyDataCursorAdapter");
		
		// Set layout inflater
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		
	}
	
	@Override
    public int getCount() {
        return count;
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
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		Log.d(DEBUG_TAG, "DailyDataCursorAdapter.newView()");
		// Inflate the list view with the changes above
		final View view = mInflater.inflate(R.layout.fragment_data_table_row, parent, false);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(DEBUG_TAG, "DailyDataCursorAdapter.bindView()");
		// Set usage text views
		TextView dateTV = (TextView) view.findViewById(R.id.fragment_data_table_row_date);
		TextView peakTV = (TextView) view.findViewById(R.id.fragment_data_table_row_peak);
		TextView offpeakTV = (TextView) view.findViewById(R.id.fragment_data_table_row_offpeak);
		TextView uploadTV = (TextView) view.findViewById(R.id.fragment_data_table_row_uploads);
		TextView freezoneTV = (TextView) view.findViewById(R.id.fragment_data_table_row_freezone);
		TextView totalTV = (TextView) view.findViewById(R.id.fragment_data_table_row_total);
		TextView accumTV = (TextView) view.findViewById(R.id.fragment_data_table_row_accum);

		// Set variables and pull data from database cursor
		// TODO: Change DailyDataDBAdapter to dailyDataDBHelper???
		long dateLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.DAY));
		long peakUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.PEAK));
		long offpeakUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.OFFPEAK));
		long uploadUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.UPLOADS));
		long freezoneUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.FREEZONE));
		long totalUsageLong = (peakUsageLong + offpeakUsageLong);
		
		Log.d(DEBUG_TAG, "dateLong:" + dateLong);
		Log.d(DEBUG_TAG, "peakUsageLong:" + peakUsageLong);
		Log.d(DEBUG_TAG, "offpeakUsageLong:" + offpeakUsageLong);
		Log.d(DEBUG_TAG, "uploadUsageLong:" + uploadUsageLong);
		Log.d(DEBUG_TAG, "freezoneUsageLong:" + freezoneUsageLong);
		Log.d(DEBUG_TAG, "totalUsageLong:" + totalUsageLong);

		dateTV.setText(LongDateToString(dateLong, "dateOfMouth"));
		peakTV.setText(IntUsageToString(peakUsageLong));
		offpeakTV.setText(IntUsageToString(offpeakUsageLong));
		uploadTV.setText(IntUsageToString(uploadUsageLong));
		freezoneTV.setText(IntUsageToString(freezoneUsageLong));
		totalTV.setText(IntUsageToString(totalUsageLong));

	}

	// Return string values for date long millisec stored in db
	private String LongDateToString(long millisecs, String convertTo) {
		DateFormat date_format = null;
		if (convertTo == "dayOfWeek") {
			date_format = new SimpleDateFormat("EEE");
		} else if (convertTo == "dateOfMouth"){
			date_format = new SimpleDateFormat("dd");
		} else if (convertTo == "mouthOfYear"){
			date_format = new SimpleDateFormat("MMM");
		}
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}
	
	// Return formated string value for int stored in db
	private String IntUsageToString (long usageLong){
		NumberFormat numberFormat = new DecimalFormat("#,###");
		long usage = usageLong/1000000;
		return numberFormat.format(usage);
		
	}

}
