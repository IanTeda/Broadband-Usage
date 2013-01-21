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
	    public TextView day;
	    public TextView peak;
	    public TextView offpeak;
	    public TextView uploads;
	    public TextView freezone;
	    public TextView total;
	    public TextView accum;
	}
	
    public DailyVolumeUsageAdapter(Context context, int layoutResourceId, DailyVolumeUsage[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
    }

	@Override
	public View getView(int position, View row, ViewGroup parent) {

		ViewHolder holder = null;
        
        if(row == null){
        	
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ViewHolder();
            holder.day = (TextView) row.findViewById(R.id.fragment_data_table_row_date);
            holder.peak = (TextView) row.findViewById(R.id.fragment_data_table_row_peak_number);
            holder.offpeak = (TextView) row.findViewById(R.id.fragment_data_table_row_offpeak_number);
            holder.uploads = (TextView) row.findViewById(R.id.fragment_data_table_row_uploads_number);
            holder.freezone = (TextView) row.findViewById(R.id.fragment_data_table_row_freezone_number);
            holder.total = (TextView) row.findViewById(R.id.fragment_data_table_row_total_number);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }
        
        DailyVolumeUsage usage = data[position];
        
        long daylyTotal = usage.peak + usage.offpeak;
        runningTotal = runningTotal + daylyTotal;
        
		holder.day.setText(LongDateToString(usage.day, "dateOfMouth"));
		holder.peak.setText(IntUsageToString(usage.peak));
		holder.offpeak.setText(IntUsageToString(usage.offpeak));
		holder.uploads.setText(IntUsageToString(usage.uploads));
		holder.freezone.setText(IntUsageToString(usage.freezone));
		holder.total.setText(IntUsageToString(daylyTotal));
		holder.accum.setText(IntUsageToString(runningTotal));
       
		// Set font to roboto
		if (Build.VERSION.SDK_INT < 11) {
	        FontUtils.setRobotoFont(mContext, row);
	    }
		
        return row;
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
	private String IntUsageToString (long usage){
		NumberFormat numberFormat = new DecimalFormat("#,###");
		return numberFormat.format(usage/GB);
		
	}
    
}
