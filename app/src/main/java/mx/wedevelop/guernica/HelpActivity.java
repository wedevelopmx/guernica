package mx.wedevelop.guernica;


import android.content.Context;
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

import mx.wedevelop.guernica.task.SampleDataTaskFragment;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener, SampleDataTaskFragment.TaskCallbacks {

    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private SampleDataTaskFragment mTaskFragment;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (SampleDataTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        progressBar = (ProgressBar) findViewById(R.id.sample_data_progress);

        LinearLayout faq = (LinearLayout) findViewById(R.id.help_faq);
        faq.setOnClickListener(this);
        LinearLayout donation = (LinearLayout) findViewById(R.id.help_donations);
        donation.setOnClickListener(this);
        LinearLayout wd = (LinearLayout) findViewById(R.id.help_wedevelop);
        wd.setOnClickListener(this);
        LinearLayout sample = (LinearLayout) findViewById(R.id.help_sample);
        sample.setOnClickListener(this);
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
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                if(sharedPref.getBoolean(getString(R.string.sp_sample_data), false)) {
                    Toast.makeText(this, getString(R.string.help_sample_error), Toast.LENGTH_SHORT).show();
                } else {
                    createSampleData();
                }

                return;
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void createSampleData() {

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        FragmentManager fm = getSupportFragmentManager();
        if (mTaskFragment == null) {
            mTaskFragment = new SampleDataTaskFragment();
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
        editor.putBoolean(getString(R.string.sp_sample_data), true);
        editor.commit();

        //Hide progress bar
        progressBar.setVisibility(View.GONE);

        //Show toast
        Toast.makeText(this, getString(R.string.help_sample_success), Toast.LENGTH_SHORT).show();
    }
}
