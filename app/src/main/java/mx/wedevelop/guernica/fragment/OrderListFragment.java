package mx.wedevelop.guernica.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import mx.wedevelop.guernica.GuernicaController;
import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.adapter.OrderAdapter;
import mx.wedevelop.guernica.sqlite.model.Order;
import mx.wedevelop.guernica.sqlite.model.ProductType;

/**
 * Created by root on 24/07/16.
 */
public class OrderListFragment extends Fragment implements ListView.OnItemLongClickListener, SimpleDialogFragment.SimpleDialogListener {

    GuernicaController controller;
    ListView salesListView;
    OrderAdapter orderAdapter;
    Order selectedOrder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.order_list_fmt, container, false);

        controller = GuernicaController.getController(getActivity());
        salesListView = (ListView) rootView.findViewById(R.id.order_list_view);
        orderAdapter = new OrderAdapter(getActivity(), controller.getOrderList());
        salesListView.setAdapter(orderAdapter);
        salesListView.setOnItemLongClickListener(this);

        return rootView;
    }

    public void updateUI() {
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        selectedOrder = (Order) adapterView.getItemAtPosition(position);
        SimpleDialogFragment dialog = SimpleDialogFragment.newInstance(
                getString(R.string.order_delete_dialog_question, selectedOrder.getQuantity()),
                getString(R.string.agree),
                getString(R.string.cancel));
        dialog.setTargetFragment(this, 1);
        dialog.show(getActivity().getSupportFragmentManager(),getString(R.string.order_delete_dialog));

        return true;
    }

    @Override
    public void onPositiveAnswer(DialogInterface dialog, int id) {
        controller.remove(selectedOrder);
        orderAdapter.remove(selectedOrder);
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNegativeAnswer(DialogInterface dialog, int id) {

    }
}
