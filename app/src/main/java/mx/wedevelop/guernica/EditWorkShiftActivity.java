package mx.wedevelop.guernica;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.wedevelop.guernica.fragment.CardHeader;
import mx.wedevelop.guernica.fragment.SimpleDialogFragment;
import mx.wedevelop.guernica.sqlite.model.WorkShift;
import mx.wedevelop.guernica.sqlite.model.helper.WorkDay;

public class EditWorkShiftActivity extends AppCompatActivity
        implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, SimpleDialogFragment.SimpleDialogListener {
    public static final String WORK_DAYS = "WORK_DAYS";

    private List<WorkDay> workDaysList = new ArrayList<WorkDay>();
    private int selectedResurceId;
    WorkShift morningShift;
    WorkShift afternoonShift;

    GuernicaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_work_shift);

        controller = GuernicaController.getController(this);

        CardHeader header =
                (CardHeader) getSupportFragmentManager().findFragmentById(R.id.card_header_fmt);
        header.updateUI(getString(R.string.edit_workshift_header), getString(R.string.edit_workshift_sub_header));

        Intent intent = getIntent();
        workDaysList = intent.getParcelableArrayListExtra(WORK_DAYS);

        TextView textView = (TextView) findViewById(R.id.days_to_edit);
        textView.setText(findSelection());

        FloatingActionButton edit = (FloatingActionButton)findViewById(R.id.save_work_day);
        edit.setOnClickListener(this);

        setupView();
        updateView();
    }

    private String findSelection() {
        String days = "";
        for(WorkDay wd : workDaysList) {
            days += wd.getDayName() + ", ";
        }
        return workDaysList.size() > 0 ? days.substring(0, days.length() - 2) : "";
    }

    private void setupView() {
        if(workDaysList.size() > 0) {
            WorkDay wd = workDaysList.get(workDaysList.size() - 1);

            if(wd.getWorkShiftList().size() > 0)
                morningShift = wd.getWorkShiftList().get(0);

            if(wd.getWorkShiftList().size() > 0)
                afternoonShift = wd.getWorkShiftList().get(wd.getWorkShiftList().size() - 1);
        }
    }

    private void updateView() {
        Button mStartHour = (Button) findViewById(R.id.m_start_time);
        mStartHour.setText(morningShift.getStartTimeString());
        mStartHour.setOnClickListener(this);

        Button mEndHour = (Button) findViewById(R.id.m_end_time);
        mEndHour.setText(morningShift.getEndTimeString());
        mEndHour.setOnClickListener(this);

        Button aStartHour = (Button) findViewById(R.id.a_start_time);
        aStartHour.setText(afternoonShift.getStartTimeString());
        aStartHour.setOnClickListener(this);

        Button aEndHour = (Button) findViewById(R.id.a_end_time);
        aEndHour.setText(afternoonShift.getEndTimeString());
        aEndHour.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_work_day:
                save();
                break;
            case R.id.m_start_time:
                selectedResurceId = view.getId();
                displayTimePicketDialog(morningShift.getStartTime());
                break;
            case R.id.m_end_time:
                selectedResurceId = view.getId();
                displayTimePicketDialog(morningShift.getEndTime());
                break;
            case R.id.a_start_time:
                selectedResurceId = view.getId();
                displayTimePicketDialog(afternoonShift.getStartTime());
                break;
            case R.id.a_end_time:
                selectedResurceId = view.getId();
                displayTimePicketDialog(afternoonShift.getEndTime());
                break;
        }
    }

    private void displayTimePicketDialog(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);

        TimePickerDialog dialog = new TimePickerDialog(this, this,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this));

        dialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);

        switch (selectedResurceId) {
            case R.id.m_start_time:
                morningShift.setStartTime(c.getTime());
                break;
            case R.id.m_end_time:
                morningShift.setEndTime(c.getTime());
                afternoonShift.setStartTime(c.getTime());
                break;
            case R.id.a_start_time:
                morningShift.setEndTime(c.getTime());
                afternoonShift.setStartTime(c.getTime());
                break;
            case R.id.a_end_time:
                afternoonShift.setEndTime(c.getTime());
                break;
        }

        updateView();
    }

    private void save() {
        SimpleDialogFragment dialog = SimpleDialogFragment.newInstance(
                getString(R.string.work_shift_dialog_question, workDaysList.size()),
                getString(R.string.agree),
                getString(R.string.cancel));
        dialog.show(getSupportFragmentManager(),getString(R.string.work_shift_dialog));
    }

    @Override
    public void onPositiveAnswer(DialogInterface dialog, int id) {
        for(WorkDay wd : workDaysList) {
            if(wd.getWorkShiftList().size() > 0) {
                wd.getWorkShiftList().get(0).setStartTime(morningShift.getStartTime());
                wd.getWorkShiftList().get(0).setEndTime(morningShift.getEndTime());
            }


            if(wd.getWorkShiftList().size() - 1 > 0) {
                wd.getWorkShiftList().get(wd.getWorkShiftList().size() - 1).setStartTime(afternoonShift.getStartTime());
                wd.getWorkShiftList().get(wd.getWorkShiftList().size() - 1).setEndTime(afternoonShift.getEndTime());
            }

        }

        controller.updateWorkDays(workDaysList);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onNegativeAnswer(DialogInterface dialog, int id) {

    }
}
