package au.id.teda.broadband.usage.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import au.id.teda.broadband.usage.R;

public class ProgressDialogCircleFragment extends DialogFragment {

	public ProgressDialogCircleFragment() {
		// Empty constructor required for DialogFragment
	}

	private EditText mEditText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_progress_dialog_circle, container);
		mEditText = (EditText) view.findViewById(R.id.txt_your_name);
		getDialog().setTitle("Hello");

		return view;
	}
}
