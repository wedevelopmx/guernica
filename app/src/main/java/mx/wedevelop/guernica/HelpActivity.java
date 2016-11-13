package mx.wedevelop.guernica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

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
                    Toast.makeText(this, getString(R.string.help_sample_success), Toast.LENGTH_SHORT).show();
                }

                return;
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void createSampleData() {
        GuernicaController.getController(this).generateSampleData();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.sp_sample_data), true);
        editor.commit();
    }
}
