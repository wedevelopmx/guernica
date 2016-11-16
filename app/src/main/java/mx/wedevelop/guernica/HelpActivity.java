package mx.wedevelop.guernica;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import mx.wedevelop.guernica.fragment.SimpleDialogFragment;
import mx.wedevelop.guernica.task.ResetDatabaseTaskFragment;
import mx.wedevelop.guernica.task.SampleDataTaskFragment;
import mx.wedevelop.guernica.task.TaskFragment;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener, SampleDataTaskFragment.TaskCallbacks, SimpleDialogFragment.SimpleDialogListener {

    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private TaskFragment mTaskFragment;
    private int mActiveTask;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        LinearLayout faq = (LinearLayout) findViewById(R.id.help_faq);
        faq.setOnClickListener(this);
        LinearLayout donation = (LinearLayout) findViewById(R.id.help_donations);
        donation.setOnClickListener(this);
        LinearLayout wd = (LinearLayout) findViewById(R.id.help_wedevelop);
        wd.setOnClickListener(this);
        LinearLayout sample = (LinearLayout) findViewById(R.id.help_sample);
        sample.setOnClickListener(this);
        LinearLayout reset = (LinearLayout) findViewById(R.id.help_reset);
        reset.setOnClickListener(this);

        updateUI();
    }

    @Override
    public void onClick(View view) {
        String url = getString(R.string.wedevelop_url);
        switch (view.getId()) {
            case R.id.help_faq:
                url = getString(R.string.faq_url);
                break;
            case R.id.help_donations:
                url = getString(R.string.donate_url);
                break;
            case R.id.help_wedevelop:
                break;
            case R.id.help_sample:
                if(!getPreferences(Context.MODE_PRIVATE).getBoolean(getString(R.string.sp_sample_data), false)) {
                    mActiveTask = R.id.help_sample;
                    SimpleDialogFragment dialog = SimpleDialogFragment.newInstance(
                            getString(R.string.sample_dialog_question),
                            getString(R.string.agree),
                            getString(R.string.cancel));
                    dialog.show(getSupportFragmentManager(),getString(R.string.sample_dialog));
                }
                return;
            case R.id.help_reset:
                if(getPreferences(Context.MODE_PRIVATE).getBoolean(getString(R.string.sp_sample_data), false)) {
                    mActiveTask = R.id.help_reset;
                    SimpleDialogFragment dialog = SimpleDialogFragment.newInstance(
                            getString(R.string.reset_dialog_question),
                            getString(R.string.agree),
                            getString(R.string.cancel));
                    dialog.show(getSupportFragmentManager(),getString(R.string.reset_dialog));
                }
                return;
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //Dialog Inteface
    @Override
    public void onPositiveAnswer(DialogInterface dialog, int id) {
        switch (mActiveTask) {
            case R.id.help_sample:
                createSampleData();
                break;
            case R.id.help_reset:
                resetData();
                break;
        }

    }

    @Override
    public void onNegativeAnswer(DialogInterface dialog, int id) {

    }

    private void createSampleData() {
        mActiveTask = R.id.help_sample;
        progressBar = (ProgressBar) findViewById(R.id.sample_data_progress);
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        FragmentManager fm = getSupportFragmentManager();
        if (mTaskFragment == null) {
            mTaskFragment = new SampleDataTaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

    }

    private void resetData() {
        mActiveTask = R.id.help_reset;
        progressBar = (ProgressBar) findViewById(R.id.reset_data_progress);
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        FragmentManager fm = getSupportFragmentManager();
        if (mTaskFragment == null) {
            mTaskFragment = new ResetDatabaseTaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

    }

    @Override
    public void onPreExecute() {
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressUpdate(int percent) {
        progressBar.setProgress(percent);
    }

    @Override
    public void onCancelled() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPostExecute() {
        //Update preferences
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        switch (mActiveTask) {
            case R.id.help_sample:
                editor.putBoolean(getString(R.string.sp_sample_data), true);
                //Show toast
                Toast.makeText(this, getString(R.string.help_sample_success), Toast.LENGTH_SHORT).show();
                break;
            case R.id.help_reset:
                editor.putBoolean(getString(R.string.sp_sample_data), false);
                //Show toast
                Toast.makeText(this, getString(R.string.help_reset_success), Toast.LENGTH_SHORT).show();
                break;
        }

        //save preference
        editor.commit();
        //Hide progress bar
        progressBar.setVisibility(View.GONE);

        updateUI();
    }

    private void updateUI() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        LinearLayout sample = (LinearLayout) findViewById(R.id.help_sample);
        LinearLayout reset = (LinearLayout) findViewById(R.id.help_reset);

        if(sharedPref.getBoolean(getString(R.string.sp_sample_data), false)) {
            sample.setVisibility(View.GONE);
            reset.setVisibility(View.VISIBLE);
        } else {
            sample.setVisibility(View.VISIBLE);
            reset.setVisibility(View.GONE);
        }
    }
}
