package au.id.teda.broadband.usage.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.DummyContent;

public class AnalysisFragment extends Fragment {
	
	private static final String DEBUG_TAG = "bbusage";
	
	public static final String ARG_ITEM_ID = "item_id";
	
	DummyContent.DummyItem mItem;
	
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.

    	
    	/**
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        **/

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }
    
    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment from activity wrapper.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
        	mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        	updateAnalysisView(mItem.content);
        }
    }
    
    public void updateAnalysisView(String id) {
        TextView article = (TextView) getActivity().findViewById(R.id.analysis_detail);
        article.setText(id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

}
