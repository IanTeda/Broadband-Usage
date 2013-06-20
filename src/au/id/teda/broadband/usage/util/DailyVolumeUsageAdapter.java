package au.id.teda.broadband.usage.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;

public class DailyVolumeUsageAdapter extends ArrayAdapter<DailyVolumeUsage> {
	
	// Debug tag pulled from main activity
	//private final static String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	// Helper classes
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	
	private Context mContext; 
    private int layoutResourceId;    
    private DailyVolumeUsage data[] = null;
    private long runningTotal = 0;
    private int mAccumPosition = 0;
    private long[] accumArray;
    private int mOverColor;
    private int mEvenBackgroundColor;
    private int mOddBackgroundColor;
    
    // Integer used to determine row number and background color
    //private int rowNum;
    //private int oldRowNum;
    
    private int GB = 1000000;
    
    private final class ViewHolder {
    	public LinearLayout mRow;
    	public View mHightlight;
    	public TextView mNumber;
	    public TextView mDate;
	    public TextView mDay;
	    public TextView mMonth;
        public TextView mAnytime;
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
        
        // Load helper classes
     	mAccountInfo = new AccountInfoHelper(mContext);
     	mAccountStatus = new AccountStatusHelper(mContext);
        
        // Initialize array
        int days = ( mAccountStatus.getDaysToGo() + mAccountStatus.getDaysSoFar() );
        accumArray = new long[days];
        
        mOverColor = context.getResources().getColor(R.color.accent);
        mEvenBackgroundColor = context.getResources().getColor(R.color.background);
        mOddBackgroundColor = context.getResources().getColor(R.color.background_alt_light);

    }

	@Override
	public View getView(int position, View view, ViewGroup parent) {

        // Setup view holder
		ViewHolder holder = null;
        if(view == null) {
        	
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ViewHolder();
            holder.mHightlight = view.findViewById(R.id.listview_data_table_row_highlight);
            holder.mRow = (LinearLayout) view.findViewById(R.id.listview_data_table_row_container);
            holder.mNumber = (TextView) view.findViewById(R.id.listview_data_table_row_number);
            holder.mDate = (TextView) view.findViewById(R.id.listview_data_table_row_date);
            holder.mDay = (TextView) view.findViewById(R.id.listview_data_table_row_date_day);
            holder.mMonth = (TextView) view.findViewById(R.id.listview_data_table_row_date_month);
            holder.mAnytime = (TextView) view.findViewById(R.id.listview_data_table_row_anytime_number);
            holder.mPeak = (TextView) view.findViewById(R.id.listview_data_table_row_peak_number);
            holder.mOffpeak = (TextView) view.findViewById(R.id.listview_data_table_row_offpeak_number);
            holder.mUploads = (TextView) view.findViewById(R.id.listview_data_table_row_uploads_number);
            holder.mFreezone = (TextView) view.findViewById(R.id.listview_data_table_row_freezone_number);
            holder.mTotal = (TextView) view.findViewById(R.id.listview_data_table_row_total_number);
            holder.mAccum = (TextView) view.findViewById(R.id.listview_data_table_row_accum_number);
            
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        
        DailyVolumeUsage usage = data[position];

        // Calculate daily total
        long daylyTotal = -1;
        // Check if i'm an anytime account
        if (mAccountInfo.isAccountAnyTime()){
            daylyTotal = usage.anytime;

        // Else I must be a peak / off peak
        } else {
            daylyTotal = usage.peak + usage.offpeak;
        }
        
		// Need to use an array to store accumulative values because views are destroyed
		// Check to see if rows are increasing
		if (mAccumPosition < position || position == 0){
			// Check to see if row number in array does not have a value
			if (accumArray[position] == 0){

                if (isDatePastToday(usage.day)) {
                    runningTotal = 0;
                } else {
                    // Add new total to accumulative total
                    runningTotal = runningTotal + daylyTotal;
                }
				// Add accumulative total to array based on position
				accumArray[position] = runningTotal;
			}
		}
        
        int rowNumber = position + 1;
        
        setRowBackground(holder, rowNumber);
        setCellText(position, holder, usage, daylyTotal, rowNumber);
        setOverUsageColor(holder, usage, rowNumber);
        setTableRows(view);

        // Set font to roboto
		if (Build.VERSION.SDK_INT < 11) {
	        FontUtils.setRobotoFont(mContext, view);
	    }

        return view;
    }

    /**
     * Set cell text for row
     * @param position
     * @param holder
     * @param usage
     * @param daylyTotal
     * @param rowNumber
     */
    private void setCellText(int position, ViewHolder holder, DailyVolumeUsage usage, long daylyTotal, int rowNumber) {
        // Set text
        holder.mNumber.setText(String.valueOf(rowNumber));
        holder.mDate.setText(LongDateToString(usage.day, "dateOfMouth"));
        holder.mDay.setText(LongDateToString(usage.day, "dayOfWeek"));
        holder.mMonth.setText(LongDateToString(usage.day, "mouthOfYear"));
        holder.mAnytime.setText(IntUsageToString(usage.anytime));
        holder.mPeak.setText(IntUsageToString(usage.peak));
        holder.mOffpeak.setText(IntUsageToString(usage.offpeak));
        holder.mUploads.setText(IntUsageToString(usage.uploads));
        holder.mFreezone.setText(IntUsageToString(usage.freezone));
        holder.mTotal.setText(IntUsageToString(daylyTotal));
        holder.mAccum.setText(IntUsageToString(accumArray[position] / 1000));
    }

    /**
     * Set table rows for different account types
     * @param view
     */
    private void setTableRows(View view) {
        // Hide rows based on account type
        if (mAccountInfo.isAccountAnyTime()){
            // Hide peak container
            RelativeLayout peakContainer = (RelativeLayout) view.findViewById(R.id.listview_data_table_row_peak_container);
            peakContainer.setVisibility(View.GONE);

            // Hide offpeak container
            RelativeLayout offpeakContainer = (RelativeLayout) view.findViewById(R.id.listview_data_table_row_offpeak_container);
            offpeakContainer.setVisibility(View.GONE);

        } else {

            // Hide anytime container
            RelativeLayout anytimeContainer = (RelativeLayout) view.findViewById(R.id.listview_data_table_row_anytime_container);
            anytimeContainer.setVisibility(View.GONE);

        }
    }

    /**
     * Check if date passed to method is past current date
     * @param milliseconds
     * @return
     */
    private boolean isDatePastToday (long milliseconds){
        Calendar now = Calendar.getInstance();
        setCalToMidnight(now);

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(milliseconds);
        setCalToMidnight(date);

        if ( date.getTimeInMillis() <= now.getTimeInMillis() ){
            return false;
        } else {
            return true;
        }

    }

    /**
     * Set time in calendar object to midnight
     * @param cal
     */
    private void setCalToMidnight(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    /**
	 * Set the text and high lighter view to accent if dev over daily quota
	 * @param holder
	 * @param usage
	 * @param rowNumber
	 */
	private void setOverUsageColor(ViewHolder holder, DailyVolumeUsage usage,
			int rowNumber) {
        long GB = 1000000;
        long dailyAnytimeQuota = mAccountInfo.getAnyTimeQuotaDailyMb();
        long anytime = usage.anytime/GB;
		long dailyPeakQuota = mAccountInfo.getPeakQuotaDailyMb();
		long peak = usage.peak/GB;
		long dailyOffpeakQuota = mAccountInfo.getOffpeakQuotaDailyMb();
		long offpeak = usage.offpeak/GB;

        if (anytime > dailyAnytimeQuota) {
            holder.mAnytime.setTextColor(mOverColor);
        } else {
            holder.mAnytime.setTextColor(Color.BLACK);
        }

		if (peak > dailyPeakQuota) {
			holder.mPeak.setTextColor(mOverColor);
		} else {
			holder.mPeak.setTextColor(Color.BLACK);
		}
			
		if (offpeak > dailyOffpeakQuota) {
			holder.mOffpeak.setTextColor(mOverColor);
		} else {
			holder.mOffpeak.setTextColor(Color.BLACK);
		}
		
		if (peak > dailyPeakQuota || offpeak > dailyOffpeakQuota){
			holder.mHightlight.setBackgroundColor(mOverColor);
		} else {
			if (isRowEven(rowNumber)){
				holder.mHightlight.setBackgroundColor(mEvenBackgroundColor);
			} else {
				holder.mHightlight.setBackgroundColor(mOddBackgroundColor);
			}
		}
	}

	/**
	 * Set row background color based on odd and even row number
	 * @param holder
	 * @param rowNumber
	 */
	private void setRowBackground(ViewHolder holder, int rowNumber) {
		// Set alternate row backgrounds
		if (isRowEven(rowNumber)){
			holder.mRow.setBackgroundColor(mEvenBackgroundColor);
		} else {
			holder.mRow.setBackgroundColor(mOddBackgroundColor);
		}
	}

	/**
	 * Reutnr string value for date long milliseced
	 * @param millisecs
	 * @param convertTo
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private String LongDateToString(long millisecs, String convertTo) {
		DateFormat date_format = null;
		if (convertTo == "dayOfWeek") {
			date_format = new SimpleDateFormat("EEE", Locale.getDefault());
		} else if (convertTo == "dateOfMouth"){
			date_format = new SimpleDateFormat("dd", Locale.getDefault());
		} else if (convertTo == "mouthOfYear"){
			date_format = new SimpleDateFormat("MMM", Locale.getDefault());
		}
		Date resultdate = new Date(millisecs);
		String date = date_format.format(resultdate);
		return date.toUpperCase(Locale.getDefault());
	}

	/**
	 * Return formated string value of long to MB
	 * @param usage
	 * @return
	 */
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
