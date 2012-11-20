package au.id.teda.broadband.usage.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment 
	implements DialogInterface.OnClickListener {

	public static AlertDialogFragment newInstance(String message) {
	    AlertDialogFragment adf = new AlertDialogFragment();
	    Bundle bundle = new Bundle();
	    bundle.putString("alert-message", message);
	    adf.setArguments(bundle);
	 
	    return adf;
	  }
	 
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    this.setCancelable(true);
	    int style = DialogFragment.STYLE_NORMAL, theme = 0;
	    setStyle(style, theme);
	  }
	 
	  @Override
	  public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
	    b.setTitle("Alert!!");
	    b.setPositiveButton("Ok", this);
	    b.setNegativeButton("Cancel", this);
	    b.setMessage(this.getArguments().getString("alert-message"));
	    return b.create();
	  }
	 
	  public void onClick(DialogInterface dialog, int which) {
	    OnDialogDoneListener act = (OnDialogDoneListener) getActivity();
	    boolean cancelled = false;
	    if (which == AlertDialog.BUTTON_NEGATIVE) {
	      cancelled = true;
	    }
	    act.onDialogDone(getTag(), cancelled, "Alert dismissed");
	  }
	}
	 
	interface OnDialogDoneListener {
	  public void onDialogDone(String tag, boolean cancelled, CharSequence message);
	}
	
	/**
	public class Test extends Activity implements OnDialogDoneListener {
		  public static final String LOGTAG = "DialogFragmentDemo";
		 
		  @Override
		  public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.main);
		    FragmentManager.enableDebugLogging(true);
		 
		    FragmentTransaction ft = getFragmentManager().beginTransaction();
		 
		    AlertDialogFragment pdf = AlertDialogFragment
		        .newInstance("Enter Something");
		 
		    pdf.show(ft, "alert");
		  }
		 
		  public void onDialogDone(String tag, boolean cancelled, CharSequence message) {
		    String s = tag + " responds with: " + message;
		    if (cancelled)
		      s = tag + " was cancelled by the user";
		    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
		    Log.v(LOGTAG, s);
		  }
		}
		**/
