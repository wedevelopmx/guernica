package mx.wedevelop.guernica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import mx.wedevelop.guernica.adapter.ReportAdapter;
import mx.wedevelop.guernica.fragment.CardHeader;
import mx.wedevelop.guernica.sqlite.model.ReportItem;

public class CheckoutActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String REPORT_ITEM = "REPORT_ITEM";
    private static final int SHIFT_REQUEST = 6000;

    GuernicaController controller;
    ListView listView;
    ReportAdapter reportAdapter;
    List<ReportItem> reportItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        CardHeader header =
                (CardHeader) getSupportFragmentManager().findFragmentById(R.id.card_header_fmt);
        header.updateUI(getString(R.string.checkout_header), getString(R.string.checkout_sub_header));

        controller = GuernicaController.getController(this);

        listView = (ListView) findViewById(R.id.checkout_list_view);

        reportItemList = controller.findShiftSummary();
        reportAdapter = new ReportAdapter(this, reportItemList, R.string.report_summary_shift);
        listView.setAdapter(reportAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long time) {
        ReportItem item = (ReportItem) adapterView.getItemAtPosition(position);
        Intent intent = new Intent(CheckoutActivity.this, ShiftActivity.class);
        intent.putExtra(REPORT_ITEM, item);
        startActivityForResult(intent, SHIFT_REQUEST);
    }

}
