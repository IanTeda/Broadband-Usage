package au.id.teda.broadband.usage.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;

public class DailyVolumeUsageAdapter extends ArrayAdapter<DailyVolumeUsage> {
	
	Context mContext; 
    int layoutResourceId;    
    DailyVolumeUsage data[] = null;
    
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
		//return super.getView(position, row, parent);
		
        ViewHolder holder = null;
        
        if(row == null){
        	
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ViewHolder();
            holder.day = (TextView) row.findViewById(R.id.fragment_data_table_row_date);
            holder.peak = (TextView) row.findViewById(R.id.fragment_data_table_row_peak);
            holder.offpeak = (TextView) row.findViewById(R.id.fragment_data_table_row_offpeak);
            holder.uploads = (TextView) row.findViewById(R.id.fragment_data_table_row_uploads);
            holder.freezone = (TextView) row.findViewById(R.id.fragment_data_table_row_freezone);
            holder.total = (TextView) row.findViewById(R.id.fragment_data_table_row_total);
            holder.accum  = (TextView) row.findViewById(R.id.fragment_data_table_row_accum);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }
        
        DailyVolumeUsage usage = data[position];
        
		holder.day.setText(LongDateToString(usage.day, "dateOfMouth"));
		holder.peak.setText(IntUsageToString(usage.peak));
		holder.offpeak.setText(IntUsageToString(usage.offpeak));
		holder.uploads.setText(IntUsageToString(usage.uploads));
		holder.freezone.setText(IntUsageToString(usage.freezone));
       
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
