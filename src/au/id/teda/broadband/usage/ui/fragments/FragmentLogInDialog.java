package au.id.teda.broadband.usage.ui.fragments;

import com.actionbarsherlock.app.SherlockDialogFragment;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import au.id.teda.broadband.usage.R;

public class FragmentLogInDialog extends SherlockDialogFragment implements OnEditorActionListener {
	
	private static final String DEBUG_TAG = "bbusage";
	
	// Interface for passing back username and password to activity
	public interface FragmentLogInListner {
        void onFinishLogInListner(String username, String password);
    }
	
	private EditText mUserName;
	private EditText mPassword;
	private CheckBox mCheckBox;
	
	public FragmentLogInDialog() {
		// Empty constructor required for DialogFragment
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set dialog theme
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_NoActionBar);
        
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate fragment view
		View view = inflater.inflate(R.layout.fragment_log_in, container, false);
		
		// Set edit text reference
		mUserName = (EditText) view.findViewById(R.id.fragment_log_in_user_name_et);
		mPassword = (EditText) view.findViewById(R.id.fragment_log_in_password_et);
		mCheckBox = (CheckBox) view.findViewById(R.id.fragment_log_in_show_pass_cbox);
	    
	    // Show soft keyboard automatically
	    mUserName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        
        // Close dialog when done on password
        mPassword.setOnEditorActionListener(this);
        
        // Watch for checkbox toggle
        mCheckBox.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	showHidePassword(view);
            }
        });

	    return view;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
        	FragmentLogInListner activity = (FragmentLogInListner) getSherlockActivity();
            activity.onFinishLogInListner(mUserName.getText().toString(), mPassword.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
	}
	
	/**
	 * Method for handling onClick events for the show/hide password check box
	 * 
	 * @param view
	 */
	public void showHidePassword(View view) {

		// Show password If check box is checked
		if (((CheckBox) view).isChecked()) {
			mPassword.setTransformationMethod(null);
		// Else hide password
		} else {
			mPassword.setTransformationMethod(new PasswordTransformationMethod());
		}

	}

}
