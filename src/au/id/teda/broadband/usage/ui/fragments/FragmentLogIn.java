package au.id.teda.broadband.usage.ui.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import au.id.teda.broadband.usage.R;

public class FragmentLogIn extends DialogFragment implements OnEditorActionListener {
	
	// Interface for passing back username and password to activity
	public interface FragmentLogInListner {
        void onFinishLogInListner(String username, String password);
    }
	
	private EditText mUserName;
	private EditText mPassword;
	
	public FragmentLogIn() {
		// Empty constructor required for DialogFragment
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set dialog theme
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Dialog_NoActionBar);
        
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_log_in, container);
		mUserName = (EditText) view.findViewById(R.id.fragment_log_in_user_name_et);
		mPassword = (EditText) view.findViewById(R.id.fragment_log_in_password_et);
	    getDialog().setTitle("Enter User Credentials");
	    
	    // Show soft keyboard automatically
	    mUserName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        
        // Close dialog when done on password
        mPassword.setOnEditorActionListener(this);

	    return view;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
        	FragmentLogInListner activity = (FragmentLogInListner) getActivity();
            activity.onFinishLogInListner(mUserName.getText().toString(), mPassword.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
	}

}
