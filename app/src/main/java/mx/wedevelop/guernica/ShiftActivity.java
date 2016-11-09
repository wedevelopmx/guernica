package mx.wedevelop.guernica;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import mx.wedevelop.guernica.adapter.ShiftSummaryAdapter;
import mx.wedevelop.guernica.fragment.CardHeader;
import mx.wedevelop.guernica.fragment.SalesSummaryFragment;
import mx.wedevelop.guernica.fragment.SimpleDialogFragment;
import mx.wedevelop.guernica.sqlite.model.ProductType;
import mx.wedevelop.guernica.sqlite.model.ReportItem;
import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.utils.Utils;

public class ShiftActivity extends AppCompatActivity implements View.OnClickListener, SimpleDialogFragment.SimpleDialogListener {

    private String TAG = "ShiftActivity";
    private GuernicaController controller;

    private ListView listView;
    private ShiftSummaryAdapter shiftSummaryAdapter;
    private List<ProductType> productTypeList;

    private int sells = 0;
    private int earnings = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.shift_off);
        fab.setOnClickListener(this);

        controller = GuernicaController.getController(this);
        ReportItem item = getIntent().getParcelableExtra(CheckoutActivity.REPORT_ITEM);
        Shift shift = controller.findShift(item.getId());

        //Setup header
        CardHeader header = (CardHeader) getSupportFragmentManager().findFragmentById(R.id.card_header_fmt);
        header.updateUI(getString(R.string.shift_header), Utils.formatDate(shift.getStartTime()));

        //Setup list view
        listView = (ListView) findViewById(R.id.shift_summary_list_view);
        productTypeList = controller.getShiftListWithSummary(shift.getId());
        shiftSummaryAdapter = new ShiftSummaryAdapter(this, productTypeList);
        listView.setAdapter(shiftSummaryAdapter);

        updateUI();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shift_off:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        SimpleDialogFragment dialog = SimpleDialogFragment.newInstance(
                getString(R.string.shift_close_msg),
                getString(R.string.agree),
                getString(R.string.cancel));
        dialog.show(getSupportFragmentManager(), getString(R.string.shift_dialog));
    }

    @Override
    public void onPositiveAnswer(DialogInterface dialog, int id) {
        Log.i(TAG, "Closing shift");
        controller.closeShift();
        controller.openShift();
        updateUI();
    }

    @Override
    public void onNegativeAnswer(DialogInterface dialog, int id) {

    }

    private void updateUI() {
        updateStatics();

        SalesSummaryFragment salesSummary = (SalesSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.sales_summary_fmt);
        salesSummary.updateUI(sells, earnings);
    }

    private void updateStatics() {
        sells = earnings = 0;
        for(ProductType productType : productTypeList) {
            sells += productType.getQuantity();
            earnings += productType.getUnitCost();
        }
    }

}
