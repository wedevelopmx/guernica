package mx.wedevelop.guernica.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.sqlite.model.WorkShift;

/**
 * Created by root on 2/11/16.
 */
public class WorkShiftAdapter extends ArrayAdapter<WorkShift> {
    public WorkShiftAdapter(Activity context, List<WorkShift> shiftList) {
        super(context, 0, shiftList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.work_shift_list_item, parent, false);

        WorkShift currentWorkShift = (WorkShift) getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.work_shift_name);
        nameTextView.setText(currentWorkShift.getName());

        TextView startTextView = (TextView) listItemView.findViewById(R.id.text_start_hour);
        startTextView.setText(currentWorkShift.getStartTimeString());

        TextView endTextView = (TextView) listItemView.findViewById(R.id.text_end_hour);
        endTextView.setText(currentWorkShift.getEndTimeString());

        return listItemView;
    }

}
