package au.id.teda.broadband.usage.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import au.id.teda.broadband.usage.R;

public class ProgressDialogCircleFragment extends DialogFragment {
	
	ProgressDialog dialog;

	public ProgressDialogCircleFragment() {
		// Empty constructor required for DialogFragment
	}

	@Override
	public ProgressDialog onCreateDialog(Bundle savedInstanceState) {
		
		ProgressDialog.Builder builder = new ProgressDialog.Builder(getActivity());
		
		// Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    //builder.setView(inflater.inflate(R.layout.progress_bar_spinner_custom, null));
		
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage("Doing nothing useful...");
		dialog.setCancelable(false);	//Disables back button
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setProgress(0);
		return dialog;
	}

	public void setProgress(int p) {
		if(dialog != null)
			dialog.setProgress(p);
	}
}
