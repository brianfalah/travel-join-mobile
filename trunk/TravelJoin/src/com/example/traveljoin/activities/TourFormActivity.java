package com.example.traveljoin.activities;

import com.example.traveljoin.R;
import com.example.traveljoin.fragments.TourFormInformationFragment;
import com.example.traveljoin.fragments.TourFormPoisFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class TourFormActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	private AppSectionsPagerAdapter appSectionsPagerAdapter;
	private ViewPager viewPager;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour_form);
		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.groups_creation);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		appSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.TourPager);
		viewPager.setAdapter(appSectionsPagerAdapter);
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tour_general_information_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tour_pois_tab))
				.setTabListener(this));
	}

	public void onCancelButtonClicked(View button) {
		Intent output = new Intent();
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}

	public void onAcceptButtonClicked(View button) {
		// TODO
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public static final int TOUR_INFORMATION_TAB = 0;
		public static final int TOUR_POIS_TAB = 1;
		public static final int TABS_AMOUNT = 2;
		
		public AppSectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			switch (position) {
			case TOUR_INFORMATION_TAB:
				fragment = new TourFormInformationFragment();
				break;
			case TOUR_POIS_TAB:
				fragment = new TourFormPoisFragment();
				break;
			default:
				fragment = new TourFormInformationFragment();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return TABS_AMOUNT;
		}
	}

}