package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.SmartFragmentStatePagerAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.GroupFormInformationFragment;
import com.example.traveljoin.fragments.GroupFormInterestsFragment;
import com.example.traveljoin.fragments.GroupFormPoisFragment;
import com.example.traveljoin.fragments.GroupFormToursFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.User;

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
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class GroupFormActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	public User user;
	public ProgressDialog progress;
	public Group group;
	public Button createButton;
	public Button updateButton;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private MyPagerAdapter adapterViewPager;

	private static int NUM_ITEMS = 4;
	private static final int GROUP_INFORMATION_TAB = 0;
	private static final int GROUP_INTERESTS_TAB = 1;
	private static final int GROUP_POIS_TAB = 2;
	private static final int GROUP_TOURS_TAB = 3;

	private static final int ADD_GROUP_METHOD = 1;
	private static final int UPDATE_GROUP_METHOD = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_form);
		initializeUser();
		initializeViewReferences();

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		if (getIntent().getExtras() != null) {
			initializeViewForEditingMode();
		} else {
			initializeViewForCreatingMode();
		}

		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(adapterViewPager);
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

	private void initializeViewForCreatingMode() {
		actionBar.setSubtitle(R.string.groups_creation);
		group = null;
		createButton.setVisibility(View.VISIBLE);
	}

	private void initializeViewForEditingMode() {
		actionBar.setSubtitle(R.string.groups_edition);
		group = (Group) getIntent().getExtras().get("group");
		updateButton.setVisibility(View.VISIBLE);
	}

	private void initializeViewReferences() {
		createButton = (Button) findViewById(R.id.GroupCreateButton);
		updateButton = (Button) findViewById(R.id.GroupUpdateButton);
	}

	private void initializeUser() {
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		user = globalContext.getUser();
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

	// Extend from SmartFragmentStatePagerAdapter now instead for more dynamic
	// ViewPager items
	public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case GROUP_INFORMATION_TAB:
				return new GroupFormInformationFragment();
			case GROUP_INTERESTS_TAB:
				return new GroupFormInterestsFragment();
			case GROUP_POIS_TAB:
				return new GroupFormPoisFragment();
			case GROUP_TOURS_TAB:
				return new GroupFormToursFragment();
			default:
				return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Page " + position;
		}

	}

	public void createGroup(View button) {
		GroupFormInformationFragment infoFragment = (GroupFormInformationFragment) adapterViewPager
				.getRegisteredFragment(GROUP_INFORMATION_TAB);
		GroupFormInterestsFragment interestsFragment = (GroupFormInterestsFragment) adapterViewPager
				.getRegisteredFragment(GROUP_INTERESTS_TAB);
		GroupFormPoisFragment poisFragment = (GroupFormPoisFragment) adapterViewPager
				.getRegisteredFragment(GROUP_POIS_TAB);
		GroupFormToursFragment toursFragment = (GroupFormToursFragment) adapterViewPager
				.getRegisteredFragment(GROUP_TOURS_TAB);

		Boolean valid = infoFragment.validateFields();
		if (valid) {
			progress = ProgressDialog.show(this, getString(R.string.loading),
					getString(R.string.wait), true);

			ArrayList<GeneralItem> newSelectedGroupInterests = interestsFragment
					.getGroupInterests();
			ArrayList<GeneralItem> newSelectedGroupPois = poisFragment
					.getGroupPois();
			ArrayList<GeneralItem> newSelectedGroupTours = toursFragment
					.getGroupTours();

			Group groupToCreate = new Group(null,
					infoFragment.getGroupName(),
					infoFragment.getGroupDescription(),
					infoFragment.getGroupType(), infoFragment.getPassword(),
					user.getId(), newSelectedGroupInterests,
					newSelectedGroupPois, newSelectedGroupTours);

			String url = getResources().getString(R.string.api_url)
					+ "/groups/create";
			HttpAsyncTask httpAsyncTask = new HttpAsyncTask(ADD_GROUP_METHOD,
					groupToCreate);
			httpAsyncTask.execute(url);
			// sigue en onPostExecute, en la parte de ADD_TOUR_METHOD
		}

	}

	// cuando se clickea el boton actualizar viene aca!
	public void updateGroup(View button) {
		GroupFormInformationFragment infoFragment = (GroupFormInformationFragment) adapterViewPager
				.getRegisteredFragment(GROUP_INFORMATION_TAB);
		GroupFormInterestsFragment interestsFragment = (GroupFormInterestsFragment) adapterViewPager
				.getRegisteredFragment(GROUP_INTERESTS_TAB);
		GroupFormPoisFragment poisFragment = (GroupFormPoisFragment) adapterViewPager
				.getRegisteredFragment(GROUP_POIS_TAB);
		GroupFormToursFragment toursFragment = (GroupFormToursFragment) adapterViewPager
				.getRegisteredFragment(GROUP_TOURS_TAB);

		Boolean valid = infoFragment.validateFields();
		if (valid) {
			progress = ProgressDialog.show(this, getString(R.string.loading),
					getString(R.string.wait), true);

			ArrayList<GeneralItem> newSelectedGroupInterests = interestsFragment
					.getGroupInterests();
			ArrayList<GeneralItem> newSelectedGroupPois = poisFragment
					.getGroupPois();
			ArrayList<GeneralItem> newSelectedGroupTours = toursFragment
					.getGroupTours();

			Group groupToUpdate = new Group(group.getId(),
					infoFragment.getGroupName(),
					infoFragment.getGroupDescription(),
					infoFragment.getGroupType(), infoFragment.getPassword(),
					user.getId(), new ArrayList<GeneralItem>(),
					new ArrayList<GeneralItem>(), new ArrayList<GeneralItem>());

			groupToUpdate.updateGroupInterests(group.getGroupInterests(),
					newSelectedGroupInterests);
			groupToUpdate.updateGroupPois(group.getGroupPois(),
					newSelectedGroupPois);
			groupToUpdate.updateGroupTours(group.getGroupTours(),
					newSelectedGroupTours);

			String url = getResources().getString(R.string.api_url)
					+ "/groups/update";
			HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
					UPDATE_GROUP_METHOD, groupToUpdate);
			httpAsyncTask.execute(url);
			// sigue en HttpAsyncTask en doInBackground en UPDATE_TOUR_METHOD
		}

	}

	// cuando se clickea el boton cancelar viene aca!
	public void cancel(View button) {
		Intent output = new Intent();
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		private ApiInterface apiInterface = new ApiInterface();
		private Integer from_method;
		private Object object_to_send;
		private ApiResult api_result;

		public HttpAsyncTask(Integer from_method, Object object_to_send) {
			this.from_method = from_method;
			this.object_to_send = object_to_send;
		}

		@Override
		protected String doInBackground(String... urls) {
			switch (this.from_method) {
			case ADD_GROUP_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send,
						"create");
				break;
			case UPDATE_GROUP_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send,
						"update");
				break;
			}

			return api_result.getResult();
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("InputStream", result);
			switch (this.from_method) {
			case ADD_GROUP_METHOD:
				progress.dismiss();
				// para volver al mapa
				JSONObject groupJson;
				if (api_result.ok()) {
					try {
						groupJson = new JSONObject(result);
						Group group_created = Group.fromJSON(groupJson);
						closeActivity(group_created);
					} catch (JSONException e) {
						showExceptionError(e);
					} catch (ParseException e) {
						showExceptionError(e);
					}
				} else {
					showConnectionError();
				}

				break;
			case UPDATE_GROUP_METHOD:
				progress.dismiss();
				// para volver al mapa
				JSONObject groupUpdatedJson;
				if (api_result.ok()) {
					try {
						groupUpdatedJson = new JSONObject(result);
						Group group_updated = Group.fromJSON(groupUpdatedJson);
						closeActivity(group_updated);
					} catch (JSONException e) {
						showExceptionError(e);
					} catch (ParseException e) {
						showExceptionError(e);
					}
				} else {
					showConnectionError();
				}
				break;
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

	public void closeActivity(Group group_created_or_updated) {
		Intent output = new Intent();
		output.putExtra("group_created_or_updated", group_created_or_updated);
		setResult(Activity.RESULT_OK, output);
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// borrar la referencia a el cartelito del dialogo, sino trae problemas
		if (progress != null)
			progress.dismiss();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
