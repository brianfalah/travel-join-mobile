package com.example.traveljoin.activities;

import com.example.traveljoin.R;
import com.example.traveljoin.fragments.GroupFormInformationFragment;
import com.example.traveljoin.fragments.GroupFormInterestsFragment;
import com.example.traveljoin.fragments.GroupFormPoisFragment;
import com.example.traveljoin.fragments.GroupFormToursFragment;

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

public class GroupFormActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	private AppSectionsPagerAdapter appSectionsPagerAdapter;
	private ViewPager viewPager;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_form);
		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.groups_creation);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		appSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(appSectionsPagerAdapter);
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.group_general_information_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.group_interests_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.group_pois_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.group_tours_tab))
				.setTabListener(this));
	}

	public void cancel(View button) {
		Intent output = new Intent();
		setResult(Activity.RESULT_CANCELED, output);
		finish();
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

		public static final int GROUP_INFORMATION_TAB = 0;
		public static final int GROUP_INTERESTS_TAB = 1;
		public static final int GROUP_POIS_TAB = 2;
		public static final int GROUP_TOURS_TAB = 3;
		public static final int TABS_AMOUNT = 4;
		
		public AppSectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			switch (position) {
			case GROUP_INFORMATION_TAB:
				fragment = new GroupFormInformationFragment();
				break;
			case GROUP_INTERESTS_TAB:
				fragment = new GroupFormInterestsFragment();
				break;
			case GROUP_POIS_TAB:
				fragment = new GroupFormPoisFragment();
				break;
			case GROUP_TOURS_TAB:
				fragment = new GroupFormToursFragment();
				break;
			default:
				fragment = new GroupFormInformationFragment();
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
