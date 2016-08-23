package mx.wedevelop.guernica.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.sqlite.model.Order;
import mx.wedevelop.guernica.sqlite.model.ProductType;

/**
 * Created by root on 25/07/16.
 */
public class ShiftSummaryAdapter extends ArrayAdapter<ProductType> {
    public ShiftSummaryAdapter(Activity context, List<ProductType> productTypeList) {
        super(context, 0, productTypeList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.shift_summary_item, parent, false);

        // Get the {@link AndroidFlavor} object located at this position in the list
        ProductType currentProductType = (ProductType) getItem(position);

        TextView nameTextView = (TextView)listItemView.findViewById(R.id.shift_summary_name);
        nameTextView.setText(currentProductType.getName());

        TextView descTextView = (TextView)listItemView.findViewById(R.id.shift_summary_desc);
        descTextView.setText(currentProductType.getDescription());

        TextView quantityTextView = (TextView)listItemView.findViewById(R.id.shift_summary_quantity);
        quantityTextView.setText(getContext().getString(R.string.quantity_fmt, currentProductType.getQuantity()));


        TextView costTextView = (TextView)listItemView.findViewById(R.id.shift_summary_earnings);
        costTextView.setText(getContext().getString(R.string.income_fmt, currentProductType.getUnitCost()));

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
