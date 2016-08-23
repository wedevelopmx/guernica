package mx.wedevelop.guernica.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.wedevelop.guernica.R;
import mx.wedevelop.guernica.layout.SlidingTabLayout;

/**
 * Created by root on 27/07/16.
 */
public class ReportSummaryFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = "ReportSummaryFragment";

    AppSectionsPagerAdapter appSectionsPagerAdapter;
    SlidingTabLayout mSlidingTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sync_summary_fmt, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        appSectionsPagerAdapter = new AppSectionsPagerAdapter(getFragmentManager());

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(appSectionsPagerAdapter);

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(this);

    }

    @Override
    public void onPageSelected(int position) {
        ReportListFragment fragment = (ReportListFragment)appSectionsPagerAdapter.getItem(position);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList;
        List<String> fragmentNames;

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
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
