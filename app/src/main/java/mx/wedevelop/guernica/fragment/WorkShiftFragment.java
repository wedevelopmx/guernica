package mx.wedevelop.guernica.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.sqlite.model.WorkShift;

/**
 * Created by root on 4/11/16.
 */
public class WorkShiftFragment extends Fragment {
    public static final String WORK_SHIFT = "WORK_SHIFT";

    public static WorkShiftFragment newInstance(WorkShift workShift) {
        WorkShiftFragment fragment = new WorkShiftFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(WorkShiftFragment.WORK_SHIFT, workShift);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        return inflater.inflate(R.layout.work_shift_list_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        WorkShift workShift = (WorkShift) getArguments().getParcelable(WorkShiftFragment.WORK_SHIFT);



        // TODO: create an item for each list element and insert into root layout
    }
}
