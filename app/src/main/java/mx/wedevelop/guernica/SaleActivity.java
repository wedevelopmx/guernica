package mx.wedevelop.guernica;

import android.content.DialogInterface;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.wedevelop.guernica.fragment.CardHeader;
import mx.wedevelop.guernica.fragment.SalesControlFragment;
import mx.wedevelop.guernica.fragment.SalesSummaryFragment;
import mx.wedevelop.guernica.fragment.SimpleDialogFragment;
import mx.wedevelop.guernica.sqlite.model.Order;
import mx.wedevelop.guernica.sqlite.model.Product;
import mx.wedevelop.guernica.sqlite.model.ProductType;
import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.utils.Utils;

public class SaleActivity extends AppCompatActivity implements View.OnClickListener, SalesControlFragment.OnSalesControlUpdate {

    private static final String TAG = "SaleActivity";
    private static final String PRODUCT_LIST = "PRODUCT_LIST";

    private int totalQuantity;
    private double totalCost;

    private List<ProductType> productTypeList;
    private HashMap<String, Product> productHash = new LinkedHashMap<String, Product>();
    private GuernicaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        controller = GuernicaController.getController(this);

        //We need to restore activity
        if (savedInstanceState != null) {
            productTypeList = savedInstanceState.getParcelableArrayList(PRODUCT_LIST);
            //Restore fragment state
            for(ProductType productType : productTypeList) {
                getSupportFragmentManager().getFragment(savedInstanceState, productType.getName());
                createProduct(productType);
            }
        } else {
            productTypeList = controller.getProductTypes();
            if (findViewById(R.id.fragment_container) != null) {
                for(ProductType productType : productTypeList) {
                    // Create a new Fragment to be placed in the activity layout
                    SalesControlFragment productCtrl = new SalesControlFragment();

                    // In case this activity was started with special instructions from an
                    // Intent, pass the Intent's extras to the fragment as arguments
                    Bundle bundle = new Bundle();
                    bundle.putString(SalesControlFragment.BRAND, productType.getName());
                    bundle.putString(SalesControlFragment.DESCRIPTION, productType.getDescription());
                    productCtrl.setArguments(bundle);

                    // Add the fragment to the 'fragment_container' FrameLayout
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, productCtrl, productType.getName()).commit();

                    createProduct(productType);
                }
            }
        }

        FloatingActionButton submit = (FloatingActionButton) findViewById(R.id.sales_submit);
        submit.setOnClickListener(this);

        CardHeader header = (CardHeader) getSupportFragmentManager().findFragmentById(R.id.card_header_fmt);
        header.updateUI(getString(R.string.sales_header), Utils.formatDateHour(new Date()));

        calculateSummary();
        updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PRODUCT_LIST, (ArrayList<ProductType>) productTypeList);

        for(ProductType productType : productTypeList) {
            Fragment fragement = getSupportFragmentManager().findFragmentByTag(productType.getName());
            getSupportFragmentManager().putFragment(outState, productType.getName(), fragement);
        }
        Log.i(TAG, "onSaveInstanceState");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sales_submit:
                if(totalQuantity == 0) {
                    Toast toast = Toast.makeText(this, getString(R.string.sales_error),  Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } else {
                    Log.i(TAG, "Submitting new order");
                    Shift shift = controller.getCurrentShift();
                    if (shift != null) {
                        Order order = new Order(totalQuantity, totalCost, shift);
                        List<Product> productList = new ArrayList<Product>();

                        Iterator it = productHash.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, Product> pair = (Map.Entry<String, Product>) it.next();
                            Product product = pair.getValue();
                            if (product.getQuantity() > 0) {
                                productList.add(product);
                            }
                        }
                        order.setProductList(productList);

                        controller.save(order);
                    }

                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    @Override
    public void OnSalesControlUpdate(String brand, int quantity) {
        Product product = productHash.get(brand);
        product.setQuantity(quantity);
        calculateSummary();
        updateUI();
    }

    private void createProduct(ProductType productType) {
        Product product = new Product();
        product.setProductType(productType);
        productHash.put(productType.getName(), product);
    }

    private void resetSummary() {
        totalQuantity = 0;
        totalCost = 0;
    }

    private void calculateSummary() {
        resetSummary();
        Iterator it = productHash.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Product> pair = (Map.Entry<String, Product>) it.next();
            Product product = pair.getValue();
            Log.i(TAG, "Calculating: " + pair.getKey());
            totalQuantity += product.getQuantity();
            totalCost += (product.getQuantity() * product.getProductType().getUnitCost());
        }
    }

    private void updateUI() {
        SalesSummaryFragment salesSummary = (SalesSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.sales_summary_fmt);
        salesSummary.updateUI(totalQuantity, totalCost);
    }
}
