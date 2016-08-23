package mx.wedevelop.guernica;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mx.wedevelop.guernica.adapter.ProductAdapter;
import mx.wedevelop.guernica.fragment.CardHeader;
import mx.wedevelop.guernica.fragment.SimpleDialogFragment;
import mx.wedevelop.guernica.sqlite.model.ProductType;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemLongClickListener, SimpleDialogFragment.SimpleDialogListener {
    GuernicaController controller;
    ProductType selectedProductType;
    ListView productListView;
    ProductAdapter productAdapter;
    List<ProductType> productTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        controller = GuernicaController.getController(this);

        productTypeList = controller.getProductTypes();
        productAdapter = new ProductAdapter(this, productTypeList);
        productListView = (ListView) findViewById(R.id.product_list);
        productListView.setAdapter(productAdapter);
        productListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        productListView.setOnItemLongClickListener(this);

        FloatingActionButton newProduct = (FloatingActionButton) findViewById(R.id.product_new);
        newProduct.setOnClickListener(this);

        FloatingActionButton submitProduct = (FloatingActionButton) findViewById(R.id.product_submit);
        submitProduct.setOnClickListener(this);

        CardHeader header = (CardHeader) getSupportFragmentManager().findFragmentById(R.id.card_header_fmt);
        header.updateUI(getString(R.string.product_header), getString(R.string.product_sub_header));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.product_new:
                showProductForm();
                break;
            case R.id.product_submit:
                if(createProduct()) {
                    clearForm();
                    updateAdapter();
                    hideProductForm();
                }

                break;
        }
    }

    private void updateAdapter() {
        productListView.deferNotifyDataSetChanged();
    }

    private boolean createProduct() {
        ProductType productType = new ProductType();

        TextView productNameTextView = (TextView) findViewById(R.id.product_name);
        if(productNameTextView.getText().toString().length() > 0)
            productType.setName(productNameTextView.getText().toString());
        else
            return showErrorMessage(getString(R.string.product_field_error_msg, getString(R.string.product_name)));

        TextView productDescTextView = (TextView) findViewById(R.id.product_description);
        if(productDescTextView.getText().toString().length() > 0) {
            productType.setDescription(productDescTextView.getText().toString());
        } else  {
            return showErrorMessage(getString(R.string.product_field_error_msg, getString(R.string.product_description)));
        }

        TextView produtPriceTextView = (TextView) findViewById(R.id.product_price);
        if(produtPriceTextView.getText().toString().length() > 0) {
            try {
                productType.setUnitCost(Double.parseDouble(produtPriceTextView.getText().toString()));
            } catch(NumberFormatException e) {
                return showErrorMessage(getString(R.string.number_field_error_msg, getString(R.string.product_price)));
            }
        } else {
            return showErrorMessage(getString(R.string.product_field_error_msg, getString(R.string.product_price)));
        }

        controller.save(productType);
        productTypeList.add(0, productType);

        return true;
    }

    private boolean showErrorMessage(String error) {
        Toast.makeText(ProductActivity.this, error, Toast.LENGTH_SHORT).show();
        return false;
    }

    private void clearForm() {
        TextView productNameTextView = (TextView) findViewById(R.id.product_name);
        productNameTextView.setText("");

        TextView productDescTextView = (TextView) findViewById(R.id.product_description);
        productDescTextView.setText("");

        TextView produtPriceTextView = (TextView) findViewById(R.id.product_price);
        produtPriceTextView.setText("");
    }

    private void showProductForm() {
        FloatingActionButton newProduct = (FloatingActionButton) findViewById(R.id.product_new);
        newProduct.setVisibility(View.GONE);

        FloatingActionButton submitProduct = (FloatingActionButton) findViewById(R.id.product_submit);
        submitProduct.setVisibility(View.VISIBLE);

        LinearLayout productForm = (LinearLayout) findViewById(R.id.product_form);
        productForm.setVisibility(View.VISIBLE);
    }

    private void hideProductForm() {
        FloatingActionButton newProduct = (FloatingActionButton) findViewById(R.id.product_new);
        newProduct.setVisibility(View.VISIBLE);

        FloatingActionButton submitProduct = (FloatingActionButton) findViewById(R.id.product_submit);
        submitProduct.setVisibility(View.GONE);

        LinearLayout productForm = (LinearLayout) findViewById(R.id.product_form);
        productForm.setVisibility(View.GONE);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        selectedProductType = (ProductType) adapterView.getItemAtPosition(position);
        SimpleDialogFragment dialog = SimpleDialogFragment.newInstance(
                getString(R.string.product_delete_dialog_question, selectedProductType.getName()),
                getString(R.string.agree),
                getString(R.string.cancel));
        dialog.show(getSupportFragmentManager(),getString(R.string.product_delete_dialog));
        return true;
    }

    @Override
    public void onPositiveAnswer(DialogInterface dialog, int id) {
        controller.remove(selectedProductType);
        productAdapter.remove(selectedProductType);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNegativeAnswer(DialogInterface dialog, int id) {

    }
}
