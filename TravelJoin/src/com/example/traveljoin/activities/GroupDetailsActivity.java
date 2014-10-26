package com.example.traveljoin.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.SmartFragmentStatePagerAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.GroupDetailsInformationFragment;
import com.example.traveljoin.fragments.GroupDeatailsInterestFragment;
import com.example.traveljoin.fragments.GroupDetailsPoisFragment;
import com.example.traveljoin.fragments.GroupDetailsToursFragment;
import com.example.traveljoin.fragments.PoiEventsFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Favorite;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.User;

public class GroupDetailsActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	private SmartFragmentStatePagerAdapter adapterViewPager;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private static final int EDIT_GROUP_REQUEST = 1;
	protected static final int DELETE_GROUP_METHOD = 2;
	protected static final int JOIN_GROUP_METHOD = 3;
	protected static final int DISJOIN_GROUP_METHOD = 4;

	ProgressDialog progress;
	public Group group;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_details);
		initializeUser();
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.pois_view);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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

		Bundle b = getIntent().getExtras();

		group = (Group) b.get("group");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.poi_view_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (!user.getId().equals(group.getUserId())) {
			menu.removeItem(R.id.action_edit);
			menu.removeItem(R.id.action_delete);
		}

		if (group.getIsJoined()) {
			menu.removeItem(R.id.action_join_group);
		} else {
			menu.removeItem(R.id.action_disjoin_group);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_edit:
			editGroup();
			return true;
		case R.id.action_delete:
			deleteGroup();
			return true;
		case R.id.action_join_group:
			joinGroup();
			return true;
		case R.id.action_disjoin_group:
			disjoinGroup();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

	public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
		private static int NUM_ITEMS = 4;
		public static final int GROUP_INFORMATION_TAB = 0;
		public static final int GROUP_INTERESTS_TAB = 1;
		public static final int GROUP_POIS_TAB = 2;
		public static final int GROUP_TOURS_TAB = 3;
		
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
				return new GroupDetailsInformationFragment();
			case GROUP_INTERESTS_TAB: 
				return new GroupDeatailsInterestFragment();
			case GROUP_POIS_TAB: 
				return new GroupDetailsPoisFragment();
			case GROUP_TOURS_TAB: 
				return new GroupDetailsToursFragment();
			default:
				return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Page " + position;
		}

	}

	public void editGroup() {
		Intent intent = new Intent(this, GroupFormActivity.class);
		startActivityForResult(intent, EDIT_GROUP_REQUEST);
	}

	public void deleteGroup() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				GroupDetailsActivity.this);
		dialog.setTitle(getString(R.string.delete_poi))
				.setMessage(getString(R.string.delete_poi_message))
				.setPositiveButton(getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								progress = ProgressDialog.show(
										GroupDetailsActivity.this,
										getString(R.string.loading),
										getString(R.string.wait), true);
								String url = getResources().getString(
										R.string.api_url)
										+ "/pois/destroy";
								HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
										DELETE_GROUP_METHOD, group);
								httpAsyncTask.execute(url);
								// sigue en HttpAsyncTask en doInBackground en
								// DELETE_POI_METHOD

							}
						})
				.setNegativeButton(getString(R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	public void joinGroup() {
		progress = ProgressDialog.show(GroupDetailsActivity.this,
				getString(R.string.loading), getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/favorites/add";
		Favorite favorite = new Favorite(user.getId(), group.getId(), "group");
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(JOIN_GROUP_METHOD,
				favorite);
		httpAsyncTask.execute(url);
	}

	public void disjoinGroup() {
		progress = ProgressDialog.show(GroupDetailsActivity.this,
				getString(R.string.loading), getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/favorites/remove";
		Favorite favorite = new Favorite(user.getId(), group.getId(), "group");
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(DISJOIN_GROUP_METHOD,
				favorite);
		httpAsyncTask.execute(url);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case EDIT_GROUP_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Bundle b = data.getExtras();
				group = (Group) b.get("group_created_or_updated");
				group.setIsJoined(group.getIsJoined());
				invalidateOptionsMenu();
				GroupDetailsInformationFragment infoFragment = (GroupDetailsInformationFragment) adapterViewPager
						.getRegisteredFragment(0);
				infoFragment.setFields();
				//TODO: Setear los demas fragments y eliminar el de events
				PoiEventsFragment eventsFragment = (PoiEventsFragment) adapterViewPager
						.getRegisteredFragment(1);
				eventsFragment.refreshList();
				break;
			}
			break;
		}

	}
	
	//TODO: Modificar esta parte del llamado a la API
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		private ApiInterface apiInterface = new ApiInterface();
		private Integer from_method;
		private Object object_to_send;
		private ApiResult api_result;

		// contructor para setearle info extra
		public HttpAsyncTask(Integer from_method, Object object_to_send) {
			this.from_method = from_method;
			this.object_to_send = object_to_send;
		}

		@Override
		protected String doInBackground(String... urls) {
			// despues de cualquiera de estos metodo vuelve al postexecute de
			// aca
			switch (this.from_method) {
			case DELETE_GROUP_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send,
						"delete");
				break;
			case JOIN_GROUP_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send, "");
				break;
			case DISJOIN_GROUP_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send, "");
				break;
			}

			return api_result.getResult();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			// Log.d("InputStream", result);
			switch (this.from_method) {
			case DELETE_GROUP_METHOD:
				progress.dismiss();
				if (api_result.ok())
					finish();
				else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.delete_poi_error_message));
					exception.alertExceptionMessage(GroupDetailsActivity.this);
				}

				break;
			case JOIN_GROUP_METHOD:
				progress.dismiss();
				if (api_result.ok()) {
					group.setIsJoined(true);
					invalidateOptionsMenu();
					Toast.makeText(GroupDetailsActivity.this,
							R.string.poi_added_to_favorites_message,
							Toast.LENGTH_SHORT).show();
				} else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.poi_already_in_favorites_message));
					exception.alertExceptionMessage(GroupDetailsActivity.this);
				}
				break;
			case DISJOIN_GROUP_METHOD:
				progress.dismiss();
				if (api_result.ok()) {
					group.setIsJoined(false);
					invalidateOptionsMenu();
					Toast.makeText(GroupDetailsActivity.this,
							R.string.poi_removed_from_favorites_message,
							Toast.LENGTH_SHORT).show();
				} else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.poi_already_removed_from_favorites_message));
					exception.alertExceptionMessage(GroupDetailsActivity.this);
				}
				break;
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (progress != null)
			progress.dismiss();
	}

	private void initializeUser() {
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		user = globalContext.getUser();
	}

	public void showExceptionError(Exception e) {
		CustomTravelJoinException exception = new CustomTravelJoinException(
				e.getMessage());
		exception.alertExceptionMessage(this);
		e.printStackTrace();
	}

}
