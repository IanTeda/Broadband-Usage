package au.id.teda.broadband.dev.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import au.id.teda.broadband.dev.R;

public class AboutDialogFragment extends DialogFragment {
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// Reference the dialog builder
		AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
	    
		// Reference the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Get the view to be inflated
	    View mDialogView = inflater.inflate(R.layout.dialog_about, null);
	    
	    // Setup dialog view & add buttons
	    mDialog.setView(mDialogView)
	    	.setPositiveButton("Got Ya", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Nothing to see here
                   }
               });
        
	    // Reference text views in custom dialog
	    TextView mLegal = (TextView) mDialogView.findViewById(R.id.dialog_about_legal);
	    TextView mInfo = (TextView) mDialogView.findViewById(R.id.dialog_about_info);
	    
	    // Set text for application legal text view
	    mLegal.setText(readRawTextFile(R.raw.dialog_about_legal));
	    
	    // Set text for application info
	    mInfo.setText(Html.fromHtml(readRawTextFile(R.raw.dialog_about_info)));
	    // Make text clickable to website
	    mInfo.setLinkTextColor(getActivity().getResources().getColor(R.color.accent));
	    Linkify.addLinks(mInfo, Linkify.ALL);
	    
	    // Create the AlertDialog object and return it
        return mDialog.create();
    }
	
	/**
	 * Read raw text files and return a string out put
	 * @param id: Reference to raw file
	 * @return string: Return string vaule of raw file
	 */
	private String readRawTextFile(int id) {
		InputStream inputStream = getActivity().getResources().openRawResource(id);
		InputStreamReader in = new InputStreamReader(inputStream);
		BufferedReader buf = new BufferedReader(in);
		String line;
		StringBuilder text = new StringBuilder();
		try {
			while (( line = buf.readLine()) != null) text.append(line);
		} catch (IOException e) {
				return null;
		}
		return text.toString();
	}

}
