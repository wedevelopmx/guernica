package mx.wedevelop.guernica.task;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.sqlite.DBHelper;

/**
 * Created by root on 14/11/16.
 */
public class ResetDatabaseTaskFragment extends TaskFragment {

    private Task mTask;

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create and execute the background task.
        mTask = new Task();
        mTask.execute();
    }

    /**
     * A dummy task that performs some (dumb) background work and
     * proxies progress updates and results back to the Activity.
     *
     * Note that we need to check if the callbacks are null in each
     * method in case they are invoked after the Activity's and
     * Fragment's onDestroy() method have been called.
     */
    private class Task extends AsyncTask<Void, Integer, Void> {

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
            publishProgress(10);
            database.execSQL(getContext().getString(R.string.delete_all_products));
            publishProgress(40);
            database.execSQL(getContext().getString(R.string.delete_all_orders));
            publishProgress(70);
            database.execSQL(getContext().getString(R.string.delete_all_shifts));
            publishProgress(100);

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
    }
}
