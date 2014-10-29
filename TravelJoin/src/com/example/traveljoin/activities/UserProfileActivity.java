package com.example.traveljoin.activities;

import com.example.traveljoin.R;
import com.example.traveljoin.fragments.UserFavouritesFragment;
import com.example.traveljoin.fragments.UserGroupListFragment;
import com.example.traveljoin.fragments.UserInformationFragment;
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

	private AppSectionsPagerAdapter appSectionsPagerAdapter;
	private ViewPager viewPager;
	private ActionBar actionBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);

		appSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());
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
		
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.general_user_profile_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.groups_user_profile_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.favourites_user_profile_tab))
				.setTabListener(this));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//TODO: refrescar los contenidos
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

		public static final int USER_INFORMATION_TAB = 0;
		public static final int USER_GROUPS_TAB = 1;
		public static final int USER_FAVOURITES_TAB = 2;
		public static final int TABS_AMOUNT = 3;

		public AppSectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			switch (position) {
			case USER_INFORMATION_TAB:
				fragment = new UserInformationFragment();
				break;
			case USER_GROUPS_TAB:
				fragment = new UserGroupListFragment();
				break;
			case USER_FAVOURITES_TAB:
				fragment = new UserFavouritesFragment();
				break;
			default:
				fragment = new UserInformationFragment();
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
