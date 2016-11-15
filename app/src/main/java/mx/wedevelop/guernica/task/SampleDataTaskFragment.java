package mx.wedevelop.guernica.task;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.wedevelop.guernica.sqlite.DBHelper;
import mx.wedevelop.guernica.sqlite.model.Order;
import mx.wedevelop.guernica.sqlite.model.Product;
import mx.wedevelop.guernica.sqlite.model.ProductType;
import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.sqlite.model.User;
import mx.wedevelop.guernica.sqlite.service.OrderService;
import mx.wedevelop.guernica.sqlite.service.ProductTypeService;
import mx.wedevelop.guernica.sqlite.service.ShiftService;
import mx.wedevelop.guernica.utils.simulation.ShiftElapseSimulation;

/**
 * Created by root on 14/11/16.
 */
public class SampleDataTaskFragment extends Fragment {

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    public interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();
    }

    private TaskCallbacks mCallbacks;
    private SampleDataTask mTask;

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // Create and execute the background task.
        mTask = new SampleDataTask();
        mTask.execute();
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * A dummy task that performs some (dumb) background work and
     * proxies progress updates and results back to the Activity.
     *
     * Note that we need to check if the callbacks are null in each
     * method in case they are invoked after the Activity's and
     * Fragment's onDestroy() method have been called.
     */
    private class SampleDataTask extends AsyncTask<Void, Integer, Void> {

        private DBHelper dbHelper;
        private SQLiteDatabase database;

        @Override
        protected void onPreExecute() {
            openDatabase();
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
        }

        /**
         * Note that we do NOT call the callback object's methods
         * directly from the background thread, as this could result
         * in a race condition.
         */
        @Override
        protected Void doInBackground(Void... ignore) {

            ShiftService shiftService = new ShiftService(database);
            OrderService orderService = new OrderService(database);
            ProductTypeService productTypeService = new ProductTypeService(database);


            List<ProductType> productTypeList = productTypeService.findAll();
            User user = new User("Origin", "contact@wedevelop.mx", "");
            ShiftElapseSimulation sim = new ShiftElapseSimulation(new Date(), 1);

            while (sim.tomorrow()) {

                Shift shift = shiftService.findOrCreateShift(user, sim.today());
                shift.setStartTime(sim.today());

                shiftService.update(shift);

                List<Order> orderList = generateOrder(productTypeList);
                for(Order order : orderList) {
                    order.setShift(shift);
                    orderService.save(order);
                }


                shift = shiftService.findOrCreateShift(user, sim.afternoonShift());
                shift.setStartTime(sim.afternoonShift());

                shiftService.update(shift);

                orderList = generateOrder(productTypeList);
                for(Order order : orderList) {
                    order.setShift(shift);
                    orderService.save(order);
                }

                publishProgress(sim.percentage());
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... percent) {
            if (mCallbacks != null) {
                mCallbacks.onProgressUpdate(percent[0]);
            }
        }

        @Override
        protected void onCancelled() {
            closeDatabase();
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(Void ignore) {
            closeDatabase();
            if (mCallbacks != null) {
                mCallbacks.onPostExecute();
            }
        }

        private void openDatabase() {
            dbHelper = new DBHelper(getContext());
            database = dbHelper.getWritableDatabase();
        }

        private void closeDatabase() {
            dbHelper.close();
            database = null;
        }

        private List<Order> generateOrder(List<ProductType> productTypeList) {
            List<Order> orderList = new ArrayList<Order>();
            int number = (int)Math.round(Math.random() * 12);
            while(number-- > 0) {
                Order order = new Order();
                order.setProductList(generateOrderProducts(productTypeList));
                orderList.add(order);
            }
            return orderList;
        }

        private List<Product> generateOrderProducts(List<ProductType> productTypeList) {
            List<Product> productList = new ArrayList<Product>();

            for(ProductType productType: productTypeList) {
                int number = (int)Math.round(Math.random() * 3);
                if(Math.random() > 0.5) {
                    Product product = new Product();
                    product.setQuantity(number);
                    product.setProductType(productType);
                    productList.add(product);
                }
            }

            return productList;
        }
    }
}
