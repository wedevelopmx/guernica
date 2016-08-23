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
import mx.wedevelop.guernica.sqlite.model.ReportItem;


/**
 * Created by root on 25/07/16.
 */
public class ReportAdapter extends ArrayAdapter<ReportItem> {

    public ReportAdapter(Activity context, List<ReportItem> orderList) {
        super(context, 0, orderList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.report_list_item, parent, false);


        // Get the {@link AndroidFlavor} object located at this position in the list
        ReportItem currentItem = (ReportItem) getItem(position);

        TextView headerTextView = (TextView)listItemView.findViewById(R.id.item_header);
        headerTextView.setText(currentItem.getHeader());

        TextView summaryTextView = (TextView)listItemView.findViewById(R.id.item_summary);
        summaryTextView.setText(getContext().getString(R.string.report_summary, currentItem.getSummary()));

        TextView quantityTextView = (TextView)listItemView.findViewById(R.id.item_quantity);
        quantityTextView.setText(getContext().getString(R.string.quantity_fmt, currentItem.getQuantity()));

        TextView costTextView = (TextView)listItemView.findViewById(R.id.item_cost);
        costTextView.setText(getContext().getString(R.string.income_fmt, currentItem.getCost()));

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}

