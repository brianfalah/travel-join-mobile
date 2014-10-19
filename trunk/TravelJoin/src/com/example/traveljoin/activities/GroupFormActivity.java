package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.SmartFragmentStatePagerAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.GroupFormInformationFragment;
import com.example.traveljoin.fragments.GroupFormInterestsFragment;
import com.example.traveljoin.fragments.GroupFormPoisFragment;
import com.example.traveljoin.fragments.GroupFormToursFragment;
import com.example.traveljoin.fragments.TourFormPoisFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.GroupInterest;
import com.example.traveljoin.models.GroupTour;
import com.example.traveljoin.models.GroupPoi;
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
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GroupFormActivity extends ActionBarActivity implements
		ActionBar.TabListener {
	
	User user;
	ProgressDialog progress;
	public Group group;
	Button createButton;
	Button updateButton;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private MyPagerAdapter adapterViewPager;	
	private List<Fragment> listFragments;
	
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
			actionBar.setSubtitle(R.string.groups_edition);
			group = (Group) getIntent().getExtras().get("group");
			updateButton.setVisibility(View.VISIBLE);
		} else {
			actionBar.setSubtitle(R.string.groups_creation);
			group = null;
			createButton.setVisibility(View.VISIBLE);
		}

		listFragments = new ArrayList<Fragment>();
		listFragments.add(new GroupFormInformationFragment());
		listFragments.add(new GroupFormInterestsFragment(group));
		listFragments.add(new GroupFormPoisFragment(group));
		listFragments.add(new GroupFormToursFragment(group));

		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), listFragments);

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
	public static class MyPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;
		private static int NUM_ITEMS = 4;		
		private static final int GROUP_INFORMATION_TAB = 0;
		private static final int GROUP_INTERESTS_TAB = 1;
		private static final int GROUP_POIS_TAB = 2;
		private static final int GROUP_TOURS_TAB = 3;
		

		public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
	        this.fragments = fragments;
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
//			switch (position) {
//			case GROUP_INFORMATION_TAB:
//				if (infoFragment == null){
//					infoFragment = GroupFormInformationFragment.newInstance();
//				}				
//				return infoFragment;
//			case GROUP_INTERESTS_TAB:
//				if (interestsFragment == null){
//					interestsFragment = GroupFormInterestsFragment.newInstance();	
//				}				
//				return interestsFragment;
//			case GROUP_POIS_TAB:
//				if (poisFragment == null){
//					poisFragment = GroupFormPoisFragment.newInstance();	
//				}								
//				return poisFragment;
//			case GROUP_TOURS_TAB:
//				if (toursFragment == null){
//					toursFragment = GroupFormToursFragment.newInstance();
//				}
//				return toursFragment;
//			default:
//				return null;
//			}
		}
		
		@Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        // TODO Auto-generated method stub
	        //super.destroyItem(container, position, object);
	    }
		
		public GroupFormInformationFragment getInfoFragment(){
			return (GroupFormInformationFragment) fragments.get(GROUP_INFORMATION_TAB);
//			return infoFragment;
		}
		
		public GroupFormInterestsFragment getInterestsFragment(){
			return (GroupFormInterestsFragment) fragments.get(GROUP_INTERESTS_TAB);
//			return interestsFragment;
		}
		
		public GroupFormPoisFragment getPoisFragment(){
			return (GroupFormPoisFragment) fragments.get(GROUP_POIS_TAB);
//			return poisFragment;
		}
		
		public GroupFormToursFragment getToursFragment(){
			return (GroupFormToursFragment) fragments.get(GROUP_TOURS_TAB);
//			return toursFragment;
		}

	}
		
	// cuando se clickea el boton crear viene aca!
	public void createGroup(View button) {
//		GroupFormInformationFragment infoFragment = (GroupFormInformationFragment) adapterViewPager
//				.getRegisteredFragment(0);
//		GroupFormInterestsFragment interestsFragment = (GroupFormInterestsFragment) adapterViewPager.getRegisteredFragment(1);
//		GroupFormPoisFragment poisFragment = (GroupFormPoisFragment) adapterViewPager.getRegisteredFragment(2);
//		GroupFormToursFragment toursFragment = (GroupFormToursFragment) adapterViewPager.getRegisteredFragment(3);
		
		Boolean valid = adapterViewPager.getInfoFragment().validateFields();
		if (valid) {
			progress = ProgressDialog.show(this, getString(R.string.loading),
					getString(R.string.wait), true);
			
			ArrayList<GeneralItem> newSelectedGroupInterests = adapterViewPager.getInterestsFragment().getGroupInterests();
			ArrayList<GeneralItem> newSelectedGroupPois = adapterViewPager.getPoisFragment().getGroupPois();
			ArrayList<GeneralItem> newSelectedGroupTours = adapterViewPager.getToursFragment().getGroupTours();
			
			Group group_to_create = new Group(null, adapterViewPager.getInfoFragment().getGroupName(),
					adapterViewPager.getInfoFragment().getGroupDescription(),
					adapterViewPager.getInfoFragment().getGroupType(),
					adapterViewPager.getInfoFragment().getPassword() ,user.getId(),
					newSelectedGroupInterests, newSelectedGroupPois, newSelectedGroupTours);

			String url = getResources().getString(R.string.api_url)
					+ "/groups/create";
			HttpAsyncTask httpAsyncTask = new HttpAsyncTask(ADD_GROUP_METHOD,
					group_to_create);
			httpAsyncTask.execute(url);
			// sigue en onPostExecute, en la parte de ADD_TOUR_METHOD
		}

	}

	// cuando se clickea el boton actualizar viene aca!
	public void updateGroup(View button) {
//		GroupFormInformationFragment infoFragment = (GroupFormInformationFragment) adapterViewPager
//				.getRegisteredFragment(0);
//		GroupFormInterestsFragment interestsFragment = (GroupFormInterestsFragment) adapterViewPager.getRegisteredFragment(1);
//		GroupFormPoisFragment poisFragment = (GroupFormPoisFragment) adapterViewPager.getRegisteredFragment(2);
//		GroupFormToursFragment toursFragment = (GroupFormToursFragment) adapterViewPager.getRegisteredFragment(3);
		
		Boolean valid = adapterViewPager.getInfoFragment().validateFields();
		if (valid) {
			progress = ProgressDialog.show(this, getString(R.string.loading),
					getString(R.string.wait), true);
			
			ArrayList<GeneralItem> newSelectedGroupInterests = adapterViewPager.getInterestsFragment().getGroupInterests();
			ArrayList<GeneralItem> newSelectedGroupPois = adapterViewPager.getPoisFragment().getGroupPois();
			ArrayList<GeneralItem> newSelectedGroupTours = adapterViewPager.getToursFragment().getGroupTours();
			
			Group groupToUpdate = new Group(group.getId(), adapterViewPager.getInfoFragment().getGroupName(),
					adapterViewPager.getInfoFragment().getGroupDescription(), adapterViewPager.getInfoFragment().getGroupType(),
					adapterViewPager.getInfoFragment().getPassword() ,user.getId(),
					new ArrayList<GeneralItem>(), new ArrayList<GeneralItem>(), new ArrayList<GeneralItem>());
			
			groupToUpdate.updateGroupInterests(group.getGroupInterests(), newSelectedGroupInterests);
			groupToUpdate.updateGroupPois(group.getGroupPois(), newSelectedGroupPois);
			groupToUpdate.updateGroupTours(group.getGroupTours(), newSelectedGroupTours);

			String url = getResources().getString(R.string.api_url)
					+ "/groups/update";
			HttpAsyncTask httpAsyncTask = new HttpAsyncTask(UPDATE_GROUP_METHOD,
					groupToUpdate);
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
