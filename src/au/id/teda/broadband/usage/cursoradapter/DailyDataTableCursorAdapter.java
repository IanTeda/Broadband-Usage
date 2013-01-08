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

	public DailyDataTableCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		
		Log.d(DEBUG_TAG, "DailyDataCursorAdapter");
		
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// Set variables and pull data from database cursor
		// TODO: Change DailyDataDBAdapter to dailyDataDBHelper???
		long dateLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.DAY)); // Pull date time stamp from cursor
		long peakUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.PEAK)); // Pull peak usage from cursor
		long offpeakUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.OFFPEAK)); // Pull offpeak usage from cursor
		long uploadUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.UPLOADS)); // Pull upload usage from cursor
		long freezoneUsageLong = cursor.getLong(cursor.getColumnIndex(DailyDataDatabaseAdapter.FREEZONE)); // Pull upload usage from cursor
		long totalUsageLong = (peakUsageLong + offpeakUsageLong); // Add up the total for the day
		
		
		// Set usage textviews
		TextView dateTV = (TextView) view.findViewById(R.id.listview_date_tv);
		dateTV.setText(LongDateToString(dateLong, "dateOfMouth")); // Day of the mouth
		TextView peakTV = (TextView) view.findViewById(R.id.listview_peak_tv);
		peakTV.setText(IntUsageToString(peakUsageLong)); // Peak usage
		TextView offpeakTV = (TextView) view.findViewById(R.id.listview_offpeak_tv);
		offpeakTV.setText(IntUsageToString(offpeakUsageLong)); // Offpeak usage
		TextView uploadTV = (TextView) view.findViewById(R.id.listview_upload_tv);
		uploadTV.setText(IntUsageToString(uploadUsageLong)); // Upload usage
		TextView freezoneTV = (TextView) view.findViewById(R.id.listview_freezone_tv);
		freezoneTV.setText(IntUsageToString(freezoneUsageLong)); // Freezone usage
		TextView totalTV = (TextView) view.findViewById(R.id.listview_total_tv);
		totalTV.setText(IntUsageToString(totalUsageLong)); // Freezone usage

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
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
