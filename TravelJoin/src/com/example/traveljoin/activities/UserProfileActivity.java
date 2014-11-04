package com.example.traveljoin.activities;

import java.util.ArrayList;
import java.util.List;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.UserProfilePoisFragment;
import com.example.traveljoin.fragments.UserProfileGroupsFragment;
import com.example.traveljoin.fragments.UserProfileInformationFragment;
import com.example.traveljoin.fragments.UserProfileToursFragment;
import com.example.traveljoin.models.User;
import com.facebook.Session;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class UserProfileActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	public static final int USER_INFORMATION_TAB = 0;
	public static final int USER_GROUPS_TAB = 1;
	public static final int USER_POIS_TAB = 2;
	public static final int USER_TOURS_TAB = 3;

	private AppSectionsPagerAdapter appSectionsPagerAdapter;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private List<Fragment> listFragments;
	public User user;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		initializeUser();
		
		listFragments = new ArrayList<Fragment>();
		listFragments.add(new UserProfileInformationFragment());
		listFragments.add(new UserProfileGroupsFragment());
		listFragments.add(new UserProfilePoisFragment());
		listFragments.add(new UserProfileToursFragment());

		appSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager(), listFragments);
		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.profile);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
				.setText(getString(R.string.general_user_profile_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.groups_user_profile_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.pois_user_profile_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tours_user_profile_tab))
				.setTabListener(this));
	}
	
	public void initializeUser() {
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		user = globalContext.getUser(); 
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		fetchUserAndRefreshUserProfile();
	}

	private void fetchUserAndRefreshUserProfile() {
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		globalContext.fetchUserAndRefreshUserProfile(this);
	}

	public void refreshUserProfile(User user) {
		UserProfileInformationFragment informationFragment = (UserProfileInformationFragment) listFragments
				.get(USER_INFORMATION_TAB);
		UserProfileGroupsFragment groupsFragment = (UserProfileGroupsFragment) listFragments
				.get(USER_GROUPS_TAB);
		UserProfilePoisFragment poisFragment = (UserProfilePoisFragment) listFragments
				.get(USER_POIS_TAB);
		UserProfileToursFragment toursFragment = (UserProfileToursFragment) listFragments
				.get(USER_TOURS_TAB);
		
		informationFragment.fillGeneralUserInformation(user);
		groupsFragment.initializeGroupedList(user);
		poisFragment.initializeGroupedList(user);
		toursFragment.initializeGroupedList(user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.user_profile_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_logout:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void logout() {
		Session session = Session.getActiveSession();
		session.closeAndClearTokenInformation();
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public AppSectionsPagerAdapter(FragmentManager fragmentManager,
				List<Fragment> fragments) {
			super(fragmentManager);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}

}
