package com.example.traveljoin.activities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.GroupFormInformationFragment;
import com.example.traveljoin.fragments.GroupFormInterestsFragment;
import com.example.traveljoin.fragments.GroupFormPoisFragment;
import com.example.traveljoin.fragments.GroupFormToursFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.Interest;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.Tour;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
	private ProgressDialog progressDialog;
	
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

	public void onCancelButtonClicked(View button) {
		Intent output = new Intent();
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}
	
	public void onAcceptButtonClicked(View button) {
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		Group group = new Group(appSectionsPagerAdapter.getGroupName(),
				appSectionsPagerAdapter.getGroupDescription(),
				appSectionsPagerAdapter.getGroupType(),
				appSectionsPagerAdapter.getPassword(),
				appSectionsPagerAdapter.getInterests(),
				appSectionsPagerAdapter.getPois(),
				appSectionsPagerAdapter.getTours(),
				globalContext.getUser().getId());
		
		progressDialog = ProgressDialog.show(this, getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/groups/create";
		CreateGroupTask createGroupTask = new CreateGroupTask(group);
		createGroupTask.execute(url);
	}
	
	private void closeActivity() {
		Intent output = new Intent();
		setResult(Activity.RESULT_CANCELED, output);
		finish();
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

		private GroupFormInformationFragment informationFragment;
		private GroupFormInterestsFragment interestsFragment;
		private GroupFormPoisFragment poisFragment;
		private GroupFormToursFragment toursFragment;

		private static final int GROUP_INFORMATION_TAB = 0;
		private static final int GROUP_INTERESTS_TAB = 1;
		private static final int GROUP_POIS_TAB = 2;
		private static final int GROUP_TOURS_TAB = 3;
		private static final int TABS_AMOUNT = 4;

		public AppSectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			switch (position) {
			case GROUP_INFORMATION_TAB:
				informationFragment = new GroupFormInformationFragment();
				fragment = informationFragment;
				break;
			case GROUP_INTERESTS_TAB:
				interestsFragment = new GroupFormInterestsFragment();
				fragment = interestsFragment;
				break;
			case GROUP_POIS_TAB:
				poisFragment = new GroupFormPoisFragment();
				fragment = poisFragment;
				break;
			case GROUP_TOURS_TAB:
				toursFragment = new GroupFormToursFragment();
				fragment = toursFragment;
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

		public String getGroupName() {
			return informationFragment.getGroupName();
		}

		public String getGroupDescription() {
			return informationFragment.getGroupDescription();
		}

		public Integer getGroupType() {
			return informationFragment.getGroupType();
		}

		public String getPassword() {
			return informationFragment.getPassword();
		}

		public ArrayList<Interest> getInterests() {
			return interestsFragment.getInterests();
		}

		public ArrayList<Poi> getPois() {
			return poisFragment.getPois();
		}

		public ArrayList<Tour> getTours() {
			return toursFragment.getTours();
		}
	}
	
	private class CreateGroupTask extends AsyncTask<String, Void, String> {
		private ApiInterface apiInterface = new ApiInterface();
		private Object object_to_send;
		private ApiResult api_result;

		public CreateGroupTask(Object object_to_send) {
			this.object_to_send = object_to_send;
		}

		@Override
		protected String doInBackground(String... urls) {
				api_result = apiInterface.POST(urls[0], object_to_send,
						"create");
			return api_result.getResult();
		}

		@Override
		protected void onPostExecute(String result) {
				progressDialog.dismiss();
				// para volver al mapa
				JSONObject groupJson;
				if (api_result.ok()) {
					try {
						//TODO: Terminar
						groupJson = new JSONObject(result);
//						Group group_created = Group.fromJSON(groupJson);
						closeActivity();
					} catch (JSONException e) {
						showExceptionError(e);
					}
				} else {
					showConnectionError();
				}
		}
	}
	
	public void showConnectionError() {
		CustomTravelJoinException exception = new CustomTravelJoinException();
		exception.alertConnectionProblem(this);
		// e.printStackTrace();
	}

	public void showExceptionError(Exception e) {
		CustomTravelJoinException exception = new CustomTravelJoinException(
				e.getMessage());
		exception.alertExceptionMessage(this);
		e.printStackTrace();
	}

}
