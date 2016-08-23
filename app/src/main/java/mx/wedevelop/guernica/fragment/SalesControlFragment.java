package mx.wedevelop.guernica.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import mx.wedevelop.guernica.R;

/**
 * Created by root on 23/07/16.
 */
public class SalesControlFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "SalesControlFragment";
    public static final String BRAND = "Brand";
    public static final String DESCRIPTION = "Description";
    public static final String QUANTITY = "QUANTITY";

    public interface OnSalesControlUpdate {
        void OnSalesControlUpdate(String brand, int quantity);
    }

    private String brand;
    private String description;
    private int quantity = 0;
    private OnSalesControlUpdate parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sales_control, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        brand = bundle.getString(BRAND);
        description = bundle.getString(DESCRIPTION);

        if (savedInstanceState != null) {
            //Pull out the saved value
            quantity = savedInstanceState.getInt(QUANTITY);
            //Setup view
            TextView quantityTextView = (TextView) view.findViewById(R.id.sales_quantity);
            quantityTextView.setText(getString(R.string.quantity_fmt, quantity));
            //Ask parent to update summary card
            parentActivity.OnSalesControlUpdate(brand, quantity);
        }

        ImageButton addButton = (ImageButton) view.findViewById(R.id.add_quantity);
        addButton.setOnClickListener(this);

        ImageButton subButton = (ImageButton) view.findViewById(R.id.sub_quantity);
        subButton.setOnClickListener(this);

        TextView brandTextView = (TextView) view.findViewById(R.id.product_name);
        brandTextView.setText(brand);

        TextView descriptionTextView = (TextView) view.findViewById(R.id.product_description);
        descriptionTextView.setText(description);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            parentActivity = (OnSalesControlUpdate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(QUANTITY, quantity);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sub_quantity:
                quantity--;
                if(quantity < 0)
                    quantity = 0;
                break;
            case R.id.add_quantity:
                quantity++;
                break;
        }

        TextView quantityTextView = (TextView) getView().findViewById(R.id.sales_quantity);
        quantityTextView.setText(getString(R.string.quantity_fmt, quantity));

        parentActivity.OnSalesControlUpdate(brand, quantity);
    }
}
