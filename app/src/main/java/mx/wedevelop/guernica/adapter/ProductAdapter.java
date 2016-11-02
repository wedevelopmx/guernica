package mx.wedevelop.guernica.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.sqlite.model.ProductType;

/**
 * Created by root on 26/07/16.
 */
public class ProductAdapter extends ArrayAdapter<ProductType> {
    public ProductAdapter(Activity context, List<ProductType> productList) {
        super(context, 0, productList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.product_list_item, parent, false);

        // Get the {@link AndroidFlavor} object located at this position in the list
        ProductType currentProduct = (ProductType)getItem(position);

        TextView quantityTextView = (TextView)listItemView.findViewById(R.id.product_name);
        quantityTextView.setText(currentProduct.getName());

        TextView hourTextView = (TextView)listItemView.findViewById(R.id.product_description);
        hourTextView.setText(currentProduct.getDescription());

        TextView priceTextView = (TextView)listItemView.findViewById(R.id.product_price);
        priceTextView.setText(getContext().getString(R.string.income_fmt, currentProduct.getUnitCost()));

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
