package au.id.teda.broadband.usage.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import au.id.teda.broadband.usage.R;

/**
 * Class for ViewFlipper pagination
 * @author Ian
 *
 */
public class PaginationView {

	// Static strings for debug tags
	//private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;

	// View objects for pagination
	private final View mListViewDot;
	private final View mBarChartDot;
	private final View mLineChartDot;

	// Drawable objects for drawable backgrounds
	private final Drawable active;
	private final Drawable inactive;

	// Change size of pagination square
	private final float scale;
	private final int DOT_ACTIVE_SIZE = 8;
	private final int DOT_INACTIVE_SIZE = 6;

	/**
	 * Class constructor
	 * @param view 
	 * @param ctx 
	 * 
	 * @param context
	 */
	public PaginationView(View view, Context context) {

		// Initialise pagination views
		mListViewDot = (View) view.findViewById(R.id.fragment_daily_usage_listview_dot);
		mBarChartDot = (View) view.findViewById(R.id.fragment_daily_usage_bar_chart_dot);
		mLineChartDot = (View) view.findViewById(R.id.fragment_daily_usage_line_chart_dot);

		// Initialise drawable backgrounds
		active = context.getResources().getDrawable(R.drawable.pagination_active);
		inactive = context.getResources().getDrawable(R.drawable.pagination_inactive);
		
		// Get device screen scale
		scale = context.getResources().getDisplayMetrics().density;

	}

	/**
	 * Method for setting position of pagination
	 * @param position
	 */
	public void setActive(int position){
		if (position == 1){
			setBarChartActive();
		}
		else if (position == 2){
			setLineChartActive();
		} else {
			setListViewActive();
		}

	}

	/**
	 * Set pagination to line chart
	 */
	private void setListViewActive(){
		mListViewDot.setBackgroundDrawable(active);
		mBarChartDot.setBackgroundDrawable(inactive);
		mLineChartDot.setBackgroundDrawable(inactive);

		setActiveSize(mListViewDot);
		setInactiveSize(mBarChartDot);
		setInactiveSize(mLineChartDot);
	}

	/**
	 * Set pagination to bar chart
	 */
	private void setBarChartActive(){
		mListViewDot.setBackgroundDrawable(inactive);
		mBarChartDot.setBackgroundDrawable(active);
		mLineChartDot.setBackgroundDrawable(inactive);

		setInactiveSize(mListViewDot);
		setActiveSize(mBarChartDot);
		setInactiveSize(mLineChartDot);
	}

	/**
	 * Set pagination to pie chart
	 */
	private void setLineChartActive(){
		mListViewDot.setBackgroundDrawable(inactive);
		mBarChartDot.setBackgroundDrawable(inactive);
		mLineChartDot.setBackgroundDrawable(active);

		setInactiveSize(mListViewDot);
		setInactiveSize(mBarChartDot);
		setActiveSize(mLineChartDot);
	}

	/**
	 * Resize view to active size
	 * @param view to be resized
	 */
	private void setActiveSize(View view){
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height =  getPixelDip(DOT_ACTIVE_SIZE);
		params.width = getPixelDip(DOT_ACTIVE_SIZE);
		view.setLayoutParams(params);
	}

	/**
	 * Resize view to inactive size
	 * @param view to be resized
	 */
	private void setInactiveSize(View view){
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height =  getPixelDip(DOT_INACTIVE_SIZE);
		params.width = getPixelDip(DOT_INACTIVE_SIZE);
		view.setLayoutParams(params);
	}

	/**
	 * Calculate pixel value for dip
	 * @param dip value to be converted
	 * @return pixel value of dip for current screen density
	 */
	private int getPixelDip(int dip){
		int pixels = (int) (dip * scale + 0.5f);
		return pixels;
	}

}