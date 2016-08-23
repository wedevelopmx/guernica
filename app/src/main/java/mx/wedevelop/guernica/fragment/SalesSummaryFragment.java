package mx.wedevelop.guernica.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mx.wedevelop.guernica.R;

/**
 * Created by root on 24/07/16.
 */
public class SalesSummaryFragment extends Fragment {

    TextView mSoldUnits;
    TextView mEarnings;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.sales_summary_fmt, container, false);

        mSoldUnits = (TextView) rootView.findViewById(R.id.sold_units);
        mEarnings = (TextView) rootView.findViewById(R.id.earnings);

        return rootView;
    }

    public void updateUI(int soldUnits, double earnings) {
        mSoldUnits.setText(getString(R.string.quantity_fmt, soldUnits));
        mEarnings.setText(getString(R.string.income_fmt, earnings));

    }
}
