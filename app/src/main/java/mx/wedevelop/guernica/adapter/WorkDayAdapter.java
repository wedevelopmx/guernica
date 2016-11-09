package mx.wedevelop.guernica.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.sqlite.model.WorkShift;
import mx.wedevelop.guernica.sqlite.model.helper.WorkDay;

/**
 * Created by root on 2/11/16.
 */
public class WorkDayAdapter extends ArrayAdapter<WorkDay> {
    FragmentManager fm;
    Activity activity;

    public WorkDayAdapter(Activity context, List<WorkDay> workDayList) {
        super(context, 0, workDayList);
        fm = context.getFragmentManager();
        activity = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.work_day_list_item, parent, false);

        //Take current work day
        WorkDay currentWorkDay = getItem(position);

        listItemView.setTag(currentWorkDay.getDayName());

        //Set Day Name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.work_day_name);
        nameTextView.setText(currentWorkDay.getDayName());

        //Take Layout
        LinearLayout ll = (LinearLayout) listItemView.findViewById(R.id.work_shift_conainer);
        //Remove all views(are we recycling?)
        ll.removeAllViews();

        //Set Day Shifts
        List<WorkShift> workShiftList = currentWorkDay.getWorkShiftList();
        for(WorkShift ws : workShiftList) {
            View shiftView = LayoutInflater.from(getContext()).inflate(R.layout.work_shift_list_item, ll, false);

            TextView startTime = (TextView) shiftView.findViewById(R.id.start_time);
            startTime.setText(ws.getStartTimeString());
            TextView endTime = (TextView) shiftView.findViewById(R.id.end_time);
            endTime.setText(ws.getEndTimeString());

            ll.addView(shiftView);
        }

        return listItemView;
    }

}
