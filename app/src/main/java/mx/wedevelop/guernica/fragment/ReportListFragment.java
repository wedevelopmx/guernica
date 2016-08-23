package mx.wedevelop.guernica.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mx.wedevelop.guernica.GuernicaController;
import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.adapter.ReportAdapter;
import mx.wedevelop.guernica.sqlite.model.ReportItem;

/**
 * Created by root on 25/07/16.
 */
public class ReportListFragment extends Fragment {

    public static final String REPORT = "REPORT";
    public static final String QUANTITY = "QUANTITY";
    public static final String COST = "COST";
    private static final String TAG = "ReportListFragment";

    public static ReportListFragment newInstance(List<ReportItem> reportItemList) {
        ReportListFragment fragment = new ReportListFragment();

        Bundle bundle = new Bundle();
        calculateAverage(reportItemList, bundle);
        bundle.putParcelableArrayList(ReportListFragment.REPORT, (ArrayList<ReportItem>)reportItemList);
        fragment.setArguments(bundle);

        return fragment;
    }

    public static void calculateAverage(List<ReportItem> reportItemList, Bundle bundle) {
        double avgCost = 0;
        double avgQuantity = 0;
        for(ReportItem reportItem: reportItemList){
            avgQuantity += reportItem.getQuantity();
            avgCost += reportItem.getCost();
        }

        avgCost /= reportItemList.size();
        avgQuantity /= reportItemList.size();

        bundle.putDouble(QUANTITY, avgQuantity);
        bundle.putDouble(COST, avgCost);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.report_list_fmt, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ArrayList<ReportItem> reportList = getArguments().getParcelableArrayList(REPORT);
        ListView shiftListView = (ListView) view.findViewById(R.id.report_list_view);
        ReportAdapter reportAdapter = new ReportAdapter(getActivity(), reportList);
        shiftListView.setAdapter(reportAdapter);
    }

}
