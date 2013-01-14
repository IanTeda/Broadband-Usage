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
	
	private int mLayout;
	private LayoutInflater mLayoutInflater;
	private Cursor mCursor;
	private Context mContext;
	
	private int COLUMN_INDEX_DAY;
	private int COLUMN_INDEX_PEAK;
	private int COLUMN_INDEX_OFFPEAK;
	private int COLUMN_INDEX_UPLOADS;
	private int COLUMN_INDEX_FREEZONE;
	
	private int count;
	
	private int GB = 1000000;
	
	private final class ViewHolder {
	    public TextView day;
	    public TextView peak;
	    public TextView offpeak;
	    public TextView uploads;
	    public TextView freezone;
	    public TextView total;
	    public TextView accum;
	}
	
	public DailyDataTableCursorAdapter(Context context, int layout, Cursor cursor, boolean autoRequery) {
		super(context, cursor, autoRequery);
		Log.d(DEBUG_TAG, "DailyDataCursorAdapter");
		
		this.mContext = context;
		this.mLayout = layout;
		this.mCursor = cursor;
		this.mLayoutInflater = LayoutInflater.from(context);
		
		this.COLUMN_INDEX_DAY = mCursor.getColumnIndex(DailyDataDatabaseAdapter.DAY);
		this.COLUMN_INDEX_PEAK = mCursor.getColumnIndex(DailyDataDatabaseAdapter.PEAK);
		this.COLUMN_INDEX_OFFPEAK = mCursor.getColumnIndex(DailyDataDatabaseAdapter.OFFPEAK);
		this.COLUMN_INDEX_UPLOADS = mCursor.getColumnIndex(DailyDataDatabaseAdapter.UPLOADS);
		this.COLUMN_INDEX_FREEZONE = mCursor.getColumnIndex(DailyDataDatabaseAdapter.FREEZONE);
		
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
    
    /**
    @Override
    public View getView(int position, View view, ViewGroup parent) {
    	
    	Log.d(DEBUG_TAG, "DailyDataCursorAdapter.getView()");
    	
        if (mCursor.moveToPosition(position)) {
            ViewHolder holder;

            if (view == null) {
                view = mLayoutInflater.inflate(mLayout, null);

                holder = new ViewHolder();
                holder.day = (TextView) view.findViewById(R.id.fragment_data_table_row_date);
                holder.peak = (TextView) view.findViewById(R.id.fragment_data_table_row_peak);
                holder.offpeak = (TextView) view.findViewById(R.id.fragment_data_table_row_offpeak);
                holder.uploads = (TextView) view.findViewById(R.id.fragment_data_table_row_uploads);
                holder.freezone = (TextView) view.findViewById(R.id.fragment_data_table_row_freezone);
                holder.total = (TextView) view.findViewById(R.id.fragment_data_table_row_total);
                holder.accum  = (TextView) view.findViewById(R.id.fragment_data_table_row_accum);


                view.setTag(holder);
            }
            else {
                holder = (ViewHolder) view.getTag();
            }

            String day = LongDateToString(mCursor.getLong(COLUMN_INDEX_DAY), "dateOfMouth");
    		String peak = IntUsageToString(mCursor.getLong(COLUMN_INDEX_PEAK));
    		String offpeak = IntUsageToString(mCursor.getLong(COLUMN_INDEX_OFFPEAK));
    		String uploads = IntUsageToString(mCursor.getLong(COLUMN_INDEX_UPLOADS));
    		String freezone = IntUsageToString(mCursor.getLong(COLUMN_INDEX_FREEZONE));
    		String total = IntUsageToString( mCursor.getLong(COLUMN_INDEX_PEAK)
    				+ mCursor.getLong(COLUMN_INDEX_OFFPEAK) );
    		
    		Log.d(DEBUG_TAG, "day:" + day);
    		Log.d(DEBUG_TAG, "peak:" + peak);
    		Log.d(DEBUG_TAG, "offpeak:" + offpeak);
    		Log.d(DEBUG_TAG, "uploads:" + uploads);
    		Log.d(DEBUG_TAG, "freezone:" + freezone);
    		Log.d(DEBUG_TAG, "totalUsageLong:" + total);

    		holder.day.setText(day);
    		holder.peak.setText(peak);
    		holder.offpeak.setText(offpeak);
    		holder.uploads.setText(uploads);
    		holder.freezone.setText(freezone);
    		holder.total.setText(total);
        }

        return view;
    }
    **/

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		Log.d(DEBUG_TAG, "DailyDataCursorAdapter.newView()");
		// Inflate the list view with the changes above
		final View view = mLayoutInflater.inflate(mLayout, parent, false);
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
		long dateLong = cursor.getLong(COLUMN_INDEX_DAY);
		long peakUsageLong = cursor.getLong(COLUMN_INDEX_PEAK);
		long offpeakUsageLong = cursor.getLong(COLUMN_INDEX_OFFPEAK);
		long uploadUsageLong = cursor.getLong(COLUMN_INDEX_UPLOADS);
		long freezoneUsageLong = cursor.getLong(COLUMN_INDEX_FREEZONE);
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
	
	public void testCursor(Cursor cursor){
		
		cursor.moveToFirst();
		
		//List<VolumeUsage> usage = new ArrayList<VolumeUsage>;
		
		if (cursor != null){
			while (cursor.moveToNext()){
				
				long dateLong = cursor.getLong(COLUMN_INDEX_DAY);
				long peakUsageLong = cursor.getLong(COLUMN_INDEX_PEAK);
				long offpeakUsageLong = cursor.getLong(COLUMN_INDEX_OFFPEAK);
				long uploadUsageLong = cursor.getLong(COLUMN_INDEX_UPLOADS);
				long freezoneUsageLong = cursor.getLong(COLUMN_INDEX_FREEZONE);
				long totalUsageLong = (peakUsageLong + offpeakUsageLong);
				
				Log.d(DEBUG_TAG, "dateLong:" + dateLong);
				Log.d(DEBUG_TAG, "peakUsageLong:" + peakUsageLong);
				Log.d(DEBUG_TAG, "offpeakUsageLong:" + offpeakUsageLong);
				Log.d(DEBUG_TAG, "uploadUsageLong:" + uploadUsageLong);
				Log.d(DEBUG_TAG, "freezoneUsageLong:" + freezoneUsageLong);
				Log.d(DEBUG_TAG, "totalUsageLong:" + totalUsageLong);
				
			}
			cursor.close();
		}
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
