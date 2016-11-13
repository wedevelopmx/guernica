package mx.wedevelop.guernica;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mx.wedevelop.guernica.fragment.CardHeader;
import mx.wedevelop.guernica.fragment.OrderListFragment;
import mx.wedevelop.guernica.fragment.SalesSummaryFragment;
import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.sqlite.model.User;
import mx.wedevelop.guernica.utils.Utils;
import mx.wedevelop.guernica.utils.signin.SignInActivity;

public class MainActivity extends SignInActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private static final int SETTINGS_REQUEST = 9001;
    private static final int SELL_REQUEST = 9002;
    private static final int CHECKOUT_REQUEST = 9003;
    private static final int SYNC_REQUEST = 9004;
    private static final int PRODUCT_REQUEST = 9005;
    private static final int REPROT_REQUEST = 9006;
    private static final int WORKSHIFT_REQUEST = 9007;
    private static final int HELP_REQUEST = 9008;

    GuernicaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaleActivity.class);
                startActivityForResult(intent, SELL_REQUEST);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        controller = GuernicaController.getController(this);
        User user = controller.getCurrentUser();
        Shift currentShift = controller.getCurrentShift();

        CardHeader header = (CardHeader) getSupportFragmentManager().findFragmentById(R.id.card_header_fmt);
        header.updateUI(getString(R.string.main_header), Utils.formatDate(currentShift.getStartTime()) + " " +
                Utils.formatHour(currentShift.getWorkShift().getStartTime()));

        updateHeaderMain(navigationView, user);
        updateUI();

        this.setAutoLoginEnabled(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_shifts) {
            Intent intent = new Intent(MainActivity.this, CheckoutActivity.class);
            startActivityForResult(intent, CHECKOUT_REQUEST);
        } else if (id == R.id.nav_report) {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivityForResult(intent, REPROT_REQUEST);
        } else if (id == R.id.nav_product) {
            Intent intent = new Intent(MainActivity.this, ProductActivity.class);
            startActivityForResult(intent, PRODUCT_REQUEST);
        } else if (id == R.id.nav_workshift) {
            Intent intent = new Intent(MainActivity.this, WorkShiftActivity.class);
            startActivityForResult(intent, WORKSHIFT_REQUEST);
//        } else if (id == R.id.nav_settings) {
//            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
//            startActivityForResult(intent, SETTINGS_REQUEST);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivityForResult(intent, HELP_REQUEST);
        } else if (id == R.id.nav_logout) {
            onGoogleSignOutClicked();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSignOut() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "MainActivity.onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        switch (requestCode) {
            case CHECKOUT_REQUEST:
                if(resultCode == RESULT_OK) {

                }
                break;
            case SETTINGS_REQUEST:
                if(resultCode == RESULT_OK) {
                    if(data.getExtras().getBoolean(AccountActivity.BACK_TO_LOGIN)) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            case SELL_REQUEST:
                if(resultCode == RESULT_OK) {
                    updateUI();
                }
                break;
            case SYNC_REQUEST:
                if(resultCode == RESULT_OK) {

                }
                break;
        }
    }

    private void updateUI() {
        controller.updateStatics();

        SalesSummaryFragment salesSummary = (SalesSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.sales_summary_fmt);
        salesSummary.updateUI(controller.getSells(), controller.getEarnings());

        OrderListFragment salesList = (OrderListFragment) getSupportFragmentManager().findFragmentById(R.id.sales_list_fmt);
        salesList.updateUI();
    }

    private  void updateHeaderMain(NavigationView navigationView, User user) {
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        ImageView userAvatar = (ImageView) headerLayout.findViewById(R.id.user_avatar);
        if(user.getAvatar() != null && user.getAvatar().length() > 0)
            Picasso.with(getApplicationContext())
                    .load(user.getAvatar())
                    .resize(120, 120)
                    .error(R.drawable.guernica)
                    .into(userAvatar);

        TextView userName = (TextView) headerLayout.findViewById(R.id.user_name);
        userName.setText(user.getDisplayName());

        TextView userEmail = (TextView) headerLayout.findViewById(R.id.user_email);
        userEmail.setText(user.getEmail());
    }
}
