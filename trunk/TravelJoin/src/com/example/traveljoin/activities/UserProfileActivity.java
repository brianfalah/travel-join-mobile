package com.example.traveljoin.activities;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.PoiListFragment;
import com.example.traveljoin.models.User;
import com.facebook.widget.ProfilePictureView;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserProfileActivity extends FragmentActivity implements
		ActionBar.TabListener {

	AppSectionsPagerAdapter appSectionsPagerAdapter;
	ViewPager viewPager;
	ActionBar actionBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);

		appSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());
		actionBar = getActionBar();
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

		actionBar.addTab(actionBar.newTab().setText("General")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Grupos")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("POIs")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Circuitos")
				.setTabListener(this));
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
		public static final int USER_POIS_TAB = 2;
		public static final int USER_CIRCUITS_TAB = 3;

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
				fragment = new PoiListFragment();
				break;
			case USER_POIS_TAB:
				fragment = new PoiListFragment();
				break;
			case USER_CIRCUITS_TAB:
				fragment = new PoiListFragment();
				break;
			default:
				fragment = new PoiListFragment();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 4;
		}
	}

	public static class UserInformationFragment extends Fragment {

		private ProfilePictureView profilePictureView;
		private TextView userNameView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View view = inflater.inflate(R.layout.fragment_user_information,
					container, false);
			
			profilePictureView = (ProfilePictureView) view
					.findViewById(R.id.selection_profile_pic);
			profilePictureView.setCropped(true);
			userNameView = (TextView) view
					.findViewById(R.id.selection_user_name);
			
			GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
			User user = globalContext.getUser();
			profilePictureView.setProfileId(user.getFacebookId());
			userNameView.setText(user.getFullName());

			return view;

		}

	}
}
