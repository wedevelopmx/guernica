package mx.wedevelop.guernica.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import mx.wedevelop.guernica.GuernicaController;
import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.adapter.ShiftSummaryAdapter;
import mx.wedevelop.guernica.sqlite.model.Shift;

/**
 * Created by root on 25/07/16.
 */
public class ShiftSummaryFragment extends Fragment {
    GuernicaController controller;
    ShiftSummaryAdapter shiftSummaryAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.shift_summary_fmt, container, false);

        controller = GuernicaController.getController(getActivity());
        setupShift(rootView);

        return rootView;
    }

    public void setupShift(View view) {
        Shift currentShift = controller.getCurrentShift();

        ListView shiftListView = (ListView) view.findViewById(R.id.shift_summary_list_view);
        shiftSummaryAdapter = new ShiftSummaryAdapter(getActivity(), controller.getShiftListWithSummary(currentShift.getId()));
        shiftListView.setAdapter(shiftSummaryAdapter);
    }

    public void updateUI() {
        setupShift(getView());
        shiftSummaryAdapter.notifyDataSetChanged();
    }
}
