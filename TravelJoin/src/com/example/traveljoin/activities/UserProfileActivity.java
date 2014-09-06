package com.example.traveljoin.activities;

import com.example.traveljoin.R;
import com.example.traveljoin.fragments.PoiListFragment;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
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
		private static final int REAUTH_ACTIVITY_CODE = 100;

		private UiLifecycleHelper uiHelper;
		private Session.StatusCallback callback = new Session.StatusCallback() {
			@Override
			public void call(final Session session, final SessionState state,
					final Exception exception) {
				onSessionStateChange(session, state, exception);
			}
		};

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View view = inflater.inflate(R.layout.fragment_user_information,
					container, false);

			uiHelper = new UiLifecycleHelper(getActivity(), callback);
			uiHelper.onCreate(savedInstanceState);

			profilePictureView = (ProfilePictureView) view
					.findViewById(R.id.selection_profile_pic);
			profilePictureView.setCropped(true);
			userNameView = (TextView) view
					.findViewById(R.id.selection_user_name);

			Session session = Session.getActiveSession();
			if (session != null && session.isOpened()) {
				makeMeRequest(session);
			}

			return view;

		}

		private void onSessionStateChange(Session session, SessionState state,
				Exception exception) {

			if (session != null && session.isOpened()) {
				makeMeRequest(session);
			} else {
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
			}

		}

		private void makeMeRequest(final Session session) {

			Request request = Request.newMeRequest(session,
					new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							// If the response is successful
							if (session == Session.getActiveSession()) {
								if (user != null) {
									profilePictureView.setProfileId(user
											.getId());
									userNameView.setText(user.getName());
								}
							}
							if (response.getError() != null) {
								// TODO: Handle errors, will do so later.
							}

						}
					});
			request.executeAsync();
		}

		@Override
		public void onResume() {
			super.onResume();
			uiHelper.onResume();
		}

		@Override
		public void onPause() {
			super.onPause();
			uiHelper.onPause();
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == REAUTH_ACTIVITY_CODE) {
				uiHelper.onActivityResult(requestCode, resultCode, data);
			}
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			uiHelper.onDestroy();
		}

		@Override
		public void onSaveInstanceState(Bundle bundle) {
			super.onSaveInstanceState(bundle);
			uiHelper.onSaveInstanceState(bundle);
		}

	}
}
