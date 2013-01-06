package au.id.teda.broadband.usage.cursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import au.id.teda.broadband.usage.ui.MainActivity;

public class DailyDataCursorAdapter extends CursorAdapter {
	
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;

	public DailyDataCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		
		Log.d(DEBUG_TAG, "DailyDataCursorAdapter");
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
