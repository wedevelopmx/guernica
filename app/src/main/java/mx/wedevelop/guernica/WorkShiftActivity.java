package mx.wedevelop.guernica;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.wedevelop.guernica.adapter.WorkDayAdapter;
import mx.wedevelop.guernica.fragment.CardHeader;
import mx.wedevelop.guernica.sqlite.model.helper.WorkDay;

public class WorkShiftActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int EDIT_WORK_SHIFT_REQUEST = 4000;

    GuernicaController controller;
    ListView listView;
    List<WorkDay> workDayList;
    WorkDayAdapter workDayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshift);

        CardHeader header =
                (CardHeader) getSupportFragmentManager().findFragmentById(R.id.card_header_fmt);
        header.updateUI(getString(R.string.workshift_header), getString(R.string.workshift_sub_header));

        controller = GuernicaController.getController(this);

        workDayList = controller.findAllWorkDays();
        workDayAdapter = new WorkDayAdapter(this, workDayList);
        listView = (ListView) findViewById(R.id.work_day_list_view);
        listView.setAdapter(workDayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        FloatingActionButton edit = (FloatingActionButton)findViewById(R.id.edit_workday_button);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_workday_button:
                List<WorkDay> selectedWorkDays = new ArrayList<WorkDay>();
                String selected = "Selected: ";
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                for(int i = 0; i < checked.size(); i ++) {
                    int position = checked.keyAt(i);
                    if(checked.valueAt(i)) {
                        selected += workDayAdapter.getItem(position).getDayName() + ", ";
                        selectedWorkDays.add(workDayAdapter.getItem(position));
                    }
                }

                if(selectedWorkDays.size() == 0) {
                    Toast.makeText(this, R.string.work_shift_no_item_selected, Toast.LENGTH_SHORT ).show();
                    return;
                }


                Intent intent = new Intent(WorkShiftActivity.this, EditWorkShiftActivity.class);
                intent.putParcelableArrayListExtra(EditWorkShiftActivity.WORK_DAYS, (ArrayList<WorkDay>) selectedWorkDays);
                startActivityForResult(intent, EDIT_WORK_SHIFT_REQUEST);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_WORK_SHIFT_REQUEST:
                if (resultCode == RESULT_OK) {
                    listView.clearChoices();
                    workDayList.clear();
                    workDayList.addAll(controller.findAllWorkDays());
                    workDayAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
