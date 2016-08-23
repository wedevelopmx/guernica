package mx.wedevelop.guernica;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import mx.wedevelop.guernica.fragment.CardHeader;
import mx.wedevelop.guernica.fragment.SalesSummaryFragment;
import mx.wedevelop.guernica.fragment.ShiftSummaryFragment;
import mx.wedevelop.guernica.fragment.SimpleDialogFragment;
import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.utils.Utils;

public class ShiftActivity extends AppCompatActivity implements View.OnClickListener, SimpleDialogFragment.SimpleDialogListener {

    private String TAG = "ShiftActivity";
    private GuernicaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.shift_off);
        fab.setOnClickListener(this);

        controller = GuernicaController.getController(this);
        Shift currentShift = controller.getCurrentShift();

        CardHeader header = (CardHeader) getSupportFragmentManager().findFragmentById(R.id.card_header_fmt);
        header.updateUI(getString(R.string.shift_header), Utils.formatDate(currentShift.getStartTime()));

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
        controller.updateStatics();

        SalesSummaryFragment salesSummary = (SalesSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.sales_summary_fmt);
        salesSummary.updateUI(controller.getSells(), controller.getEarnings());

        ShiftSummaryFragment shiftSummary = (ShiftSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.shift_summary_fmt);
        shiftSummary.updateUI();
    }

}
