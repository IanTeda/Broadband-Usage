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
	
	private final LayoutInflater mInflater;

	public DailyDataTableCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
		super(context, cursor, autoRequery);
		
		Log.d(DEBUG_TAG, "DailyDataCursorAdapter");
		
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(DEBUG_TAG, "DailyDataCursorAdapter.bindView()");
		// Set usage textviews
		TextView dateTV = (TextView) view.findViewById(R.id.data_table_column_date);
		TextView peakTV = (TextView) view.findViewById(R.id.data_table_column_peak);
		TextView offpeakTV = (TextView) view.findViewById(R.id.data_table_column_offpeak);
		TextView uploadTV = (TextView) view.findViewById(R.id.data_table_column_upload);
		TextView freezoneTV = (TextView) view.findViewById(R.id.data_table_column_freezone);
		TextView totalTV = (TextView) view.findViewById(R.id.data_table_column_total);
		TextView accumTV = (TextView) view.findViewById(R.id.data_table_column_accum);

		// Set variables and pull data from database cursor
		// TODO: Change DailyDataDBAdapter to dailyDataDBHelper???
		long dateLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.DAY)); // Pull date time stamp from cursor
		long peakUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.PEAK)); // Pull peak usage from cursor
		long offpeakUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.OFFPEAK)); // Pull offpeak usage from cursor
		long uploadUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.UPLOADS)); // Pull upload usage from cursor
		long freezoneUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.FREEZONE)); // Pull upload usage from cursor
		long totalUsageLong = (peakUsageLong + offpeakUsageLong); // Add up the total for the day
		
		Log.d(DEBUG_TAG, "dateLong:" + dateLong);
		Log.d(DEBUG_TAG, "peakUsageLong:" + peakUsageLong);
		Log.d(DEBUG_TAG, "offpeakUsageLong:" + offpeakUsageLong);
		Log.d(DEBUG_TAG, "uploadUsageLong:" + uploadUsageLong);
		Log.d(DEBUG_TAG, "freezoneUsageLong:" + freezoneUsageLong);
		Log.d(DEBUG_TAG, "totalUsageLong:" + totalUsageLong);

		dateTV.setText(LongDateToString(dateLong, "dateOfMouth")); // Day of the mouth
		peakTV.setText(IntUsageToString(peakUsageLong)); // Peak usage
		offpeakTV.setText(IntUsageToString(offpeakUsageLong)); // Offpeak usage
		uploadTV.setText(IntUsageToString(uploadUsageLong)); // Upload usage
		freezoneTV.setText(IntUsageToString(freezoneUsageLong)); // Freezone usage
		totalTV.setText(IntUsageToString(totalUsageLong)); // Freezone usage

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// Inflate the listview with the changes above
		View view = mInflater.inflate(R.layout.table_row, parent, false);
		return view;
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
