package mx.wedevelop.guernica.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mx.wedevelop.guernica.R;

/**
 * Created by root on 26/07/16.
 */
public class CardHeader extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.card_header_fmt, container, false);
        return rootView;
    }

    public void updateUI(String title, String subtitle) {
        TextView headerTextView = (TextView) getView().findViewById(R.id.card_header);
        headerTextView.setText(title);
        TextView subHeaderTextView = (TextView) getView().findViewById(R.id.card_sub_header);
        subHeaderTextView.setText(subtitle);
    }
}
