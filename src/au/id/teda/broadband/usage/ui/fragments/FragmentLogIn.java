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
	
	public interface FragmentLogInListner {
        void onFinishLogInListner(String inputText);
    }
	
	private EditText mEditText;
	
	public FragmentLogIn() {
		// Empty constructor required for DialogFragment
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_log_in, container);
		mEditText = (EditText) view.findViewById(R.id.txt_your_name);
	    getDialog().setTitle("Hello");
	    
	    // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

	    return view;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
        	FragmentLogInListner activity = (FragmentLogInListner) getActivity();
            activity.onFinishLogInListner(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
	}

}
