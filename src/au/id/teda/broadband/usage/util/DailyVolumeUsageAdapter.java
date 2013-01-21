package au.id.teda.broadband.usage.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.MainActivity;

public class DailyVolumeUsageAdapter extends ArrayAdapter<DailyVolumeUsage> {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	private Context mContext; 
    private int layoutResourceId;    
    private DailyVolumeUsage data[] = null;
    private long runningTotal = 0;
    
    private int GB = 1000000;
    
    private final class ViewHolder {
    	public LinearLayout mRow;
    	public TextView mNumber;
	    public TextView mDate;
	    public TextView mDay;
	    public TextView mMonth;
	    public TextView mPeak;
	    public TextView mOffpeak;
	    public TextView mUploads;
	    public TextView mFreezone;
	    public TextView mTotal;
	    public TextView mAccum;
	}
	
    public DailyVolumeUsageAdapter(Context context, int layoutResourceId, DailyVolumeUsage[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
    }

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder = null;
        
        if(view == null){
        	
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ViewHolder();
            holder.mRow=  (LinearLayout) view.findViewById(R.id.fragment_data_table_row_container);
            holder.mNumber = (TextView) view.findViewById(R.id.fragment_data_table_row_number);
            holder.mDate = (TextView) view.findViewById(R.id.fragment_data_table_row_date);
            holder.mDay = (TextView) view.findViewById(R.id.fragment_data_table_row_date_day);
            holder.mMonth = (TextView) view.findViewById(R.id.fragment_data_table_row_date_month); 
            holder.mPeak = (TextView) view.findViewById(R.id.fragment_data_table_row_peak_number);
            holder.mOffpeak = (TextView) view.findViewById(R.id.fragment_data_table_row_offpeak_number);
            holder.mUploads = (TextView) view.findViewById(R.id.fragment_data_table_row_uploads_number);
            holder.mFreezone = (TextView) view.findViewById(R.id.fragment_data_table_row_freezone_number);
            holder.mTotal = (TextView) view.findViewById(R.id.fragment_data_table_row_total_number);
            
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        
        DailyVolumeUsage usage = data[position];
        
        long daylyTotal = usage.peak + usage.offpeak;
        runningTotal = runningTotal + daylyTotal;
        
        int rowNumber = position + 1;
        
        // Set alternate row backgrounds
		if (isRowEven(rowNumber)){
			holder.mRow.setBackgroundResource(R.color.background);
		} else {
			holder.mRow.setBackgroundResource(R.color.background_alt_light);
		}
        
        // Set text
        holder.mNumber.setText(String.valueOf(rowNumber));
		holder.mDate.setText(LongDateToString(usage.day, "dateOfMouth"));
		holder.mDay.setText(LongDateToString(usage.day, "dayOfWeek"));
		holder.mMonth.setText(LongDateToString(usage.day, "mouthOfYear"));
		holder.mPeak.setText(IntUsageToString(usage.peak));
		holder.mOffpeak.setText(IntUsageToString(usage.offpeak));
		holder.mUploads.setText(IntUsageToString(usage.uploads));
		holder.mFreezone.setText(IntUsageToString(usage.freezone));
		holder.mTotal.setText(IntUsageToString(daylyTotal));
		//holder.accum.setText(IntUsageToString(runningTotal));
       
		// Set font to roboto
		if (Build.VERSION.SDK_INT < 11) {
	        FontUtils.setRobotoFont(mContext, view);
	    }
		
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
		String date = date_format.format(resultdate);
		return date.toUpperCase();
	}

	// Return formated string value for int stored in db
	private String IntUsageToString (long usage){
		NumberFormat numberFormat = new DecimalFormat("#,###");
		return numberFormat.format(usage/GB);
		
	}
	
	/**
	 * Is number divisible by 2 then it is even else it is odd
	 * @param row
	 * @return
	 */
	private boolean isRowEven(int row){
		if (row % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}
    
}
