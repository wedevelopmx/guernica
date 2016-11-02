package mx.wedevelop.guernica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import mx.wedevelop.guernica.adapter.WorkShiftAdapter;
import mx.wedevelop.guernica.fragment.CardHeader;
import mx.wedevelop.guernica.sqlite.model.WorkShift;

public class WorkshiftActivity extends AppCompatActivity {

    GuernicaController controller;
    ListView workShiftListView;
    WorkShiftAdapter workShiftAdapter;
    List<WorkShift> workShiftList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshift);

        controller = GuernicaController.getController(this);

        workShiftListView = (ListView) findViewById(R.id.work_shift_list_view);
        workShiftAdapter = new WorkShiftAdapter(this, controller.findWorkShift());
        workShiftListView.setAdapter(workShiftAdapter);

        CardHeader header = (CardHeader) getSupportFragmentManager().findFragmentById(R.id.card_header_fmt);
        header.updateUI(getString(R.string.workshift_header), getString(R.string.workshift_sub_header));
    }


}
