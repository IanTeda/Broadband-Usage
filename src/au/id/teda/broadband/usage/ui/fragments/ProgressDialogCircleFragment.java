package au.id.teda.broadband.usage.ui.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import au.id.teda.broadband.usage.R;

public class ProgressDialogCircleFragment extends DialogFragment {
	
	Dialog dialog;
	//Context mContext;

	public ProgressDialogCircleFragment() {
		// Empty constructor required for DialogFragment
		//mContext = context;
	}
	
	public static ProgressDialogCircleFragment newInstance() {
        return new ProgressDialogCircleFragment();
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Dialog dialog = new Dialog(getActivity());
		
		// Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    dialog.setContentView(R.layout.progress_bar_spinner_custom);
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    //builder.setView(inflater.inflate(R.layout.progress_bar_spinner_custom, null));
		
	    /**
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage("Doing nothing useful...");
		dialog.setCancelable(false);	//Disables back button
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setProgress(0);
		return dialog;
		**/
	    
	    return dialog;
	}
	
	public void dismissDialog(){
		getDialog().dismiss();
	}

}
