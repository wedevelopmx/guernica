package mx.wedevelop.guernica;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mx.wedevelop.guernica.fragment.CardHeader;
import mx.wedevelop.guernica.fragment.ReportListFragment;
import mx.wedevelop.guernica.fragment.SalesSummaryFragment;
import mx.wedevelop.guernica.layout.SlidingTabLayout;
import mx.wedevelop.guernica.sqlite.model.ReportItem;

public class ReportActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "ReportActivity";

    private AppSectionsPagerAdapter appSectionsPagerAdapter;
    private GuernicaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        CardHeader header = (CardHeader) getSupportFragmentManager().findFragmentById(R.id.card_header_fmt);
        header.updateUI(getString(R.string.report_header), getString(R.string.report_sub_header));

        controller = GuernicaController.getController(this);
        appSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        appSectionsPagerAdapter.addReportFragment(getString(R.string.daily), controller.findDailySummary());
        appSectionsPagerAdapter.addReportFragment(getString(R.string.weekly), controller.findWeeklySummary());
        appSectionsPagerAdapter.addReportFragment(getString(R.string.monthly), controller.findMonthlySummary());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(appSectionsPagerAdapter);
        onPageSelected(0);

        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(this);
    }

    @Override
    public void onPageSelected(int position) {
        double quantity, cost;
        ReportListFragment fragment = (ReportListFragment) appSectionsPagerAdapter.getItem(position);
        Bundle bundle = fragment.getArguments();
        quantity = bundle.getDouble(ReportListFragment.QUANTITY);
        cost = bundle.getDouble(ReportListFragment.COST);

        SalesSummaryFragment salesSummaryFragment = (SalesSummaryFragment) getSupportFragmentManager().findFragmentById(R.id.sales_summary_fmt);
        salesSummaryFragment.updateUI((int)quantity, (int)cost);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static class AppSectionsPagerAdapter extends FragmentStatePagerAdapter {
        private List<List<ReportItem>> fragmentList;
        private List<String> fragmentNames;

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<List<ReportItem>>();
            fragmentNames = new ArrayList<String>();
        }

        public void addReportFragment(String title, List<ReportItem> reportItemList){
            fragmentList.add(reportItemList);
            fragmentNames.add(title);
        }

        @Override
        public Fragment getItem(int i) {
           return ReportListFragment.newInstance(fragmentList.get(i));
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentNames.get(position);
        }
    }
}

