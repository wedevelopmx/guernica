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

/**
 * Created by root on 22/07/16.
 */
public class OrderAdapter extends ArrayAdapter<Order> {

    public OrderAdapter(Activity context, List<Order> orderList) {
        super(context, 0, orderList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.order_list_item, parent, false);

        SimpleDateFormat dfHour = new SimpleDateFormat("HH:mm a");
        SimpleDateFormat dfDay = new SimpleDateFormat("EEE, MMM d, yyyy");
        // Get the {@link AndroidFlavor} object located at this position in the list
        Order currentOrder = (Order)getItem(position);

        TextView quantityTextView = (TextView)listItemView.findViewById(R.id.sales_quantity);
        quantityTextView.setText(getContext().getString(R.string.quantity_fmt, currentOrder.getQuantity()));

        TextView hourTextView = (TextView)listItemView.findViewById(R.id.sales_hour);
        hourTextView.setText(dfHour.format(currentOrder.getCreatedDate()));

        TextView dateTextView = (TextView)listItemView.findViewById(R.id.sales_date);
        dateTextView.setText(dfDay.format(currentOrder.getCreatedDate()));

        TextView priceTextView = (TextView)listItemView.findViewById(R.id.sales_price);
        priceTextView.setText(getContext().getString(R.string.income_fmt, currentOrder.getCost()));

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
