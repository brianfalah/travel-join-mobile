package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.GroupDetailsInformationFragment;
import com.example.traveljoin.fragments.GroupDetailsInterestsFragment;
import com.example.traveljoin.fragments.GroupDetailsMembersFragment;
import com.example.traveljoin.fragments.GroupDetailsPoisFragment;
import com.example.traveljoin.fragments.GroupDetailsSuggestionsFragment;
import com.example.traveljoin.fragments.GroupDetailsToursFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.GroupMember;
import com.example.traveljoin.models.GroupPoi;
import com.example.traveljoin.models.GroupTour;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.Suggestion;
import com.example.traveljoin.models.User;

public class GroupDetailsActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	private MyPagerAdapter adapterViewPager;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private Dialog passwordDialog;
	private EditText insertedPrivateGroupPassword;
	private Button okButton;
	private Button cancelButton;

	public static final int GROUP_INFORMATION_TAB = 0;
	public static final int GROUP_INTERESTS_TAB = 1;
	public static final int GROUP_POIS_TAB = 2;
	public static final int GROUP_TOURS_TAB = 3;
	public static final int GROUP_MEMBERS_TAB = 4;
	public static final int GROUP_SUGGESTIONS_TAB = 5;

	private static final int EDIT_GROUP_REQUEST = 1;
	protected static final int DELETE_GROUP_METHOD = 2;
	protected static final int JOIN_GROUP_METHOD = 3;
	protected static final int DISJOIN_GROUP_METHOD = 4;
	protected static final int ACCEPT_SUGGESTION_METHOD = 5;
	protected static final int REJECT_SUGGESTION_METHOD = 6;

	public ProgressDialog progress;
	public Group group;
	public User user;
	private List<Fragment> listFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_details);
		initializeUser();

		Bundle bundle = getIntent().getExtras();
		group = (Group) bundle.get("group");

		listFragments = new ArrayList<Fragment>();
		listFragments.add(new GroupDetailsInformationFragment());
		listFragments.add(new GroupDetailsInterestsFragment());
		
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),
				listFragments);

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(adapterViewPager);
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.group_view);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.group_general_information_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.group_interests_tab))
				.setTabListener(this));

		if (group.isPublic() || group.isJoined())
			initializePublicGroupContent();
		
		if (group.isOwner(user)) {
			initializeOwnerContent();
		}
	}

	private void initializePublicGroupContent() {
		listFragments.add(new GroupDetailsPoisFragment());
		listFragments.add(new GroupDetailsToursFragment());
		listFragments.add(new GroupDetailsMembersFragment());
		adapterViewPager.notifyDataSetChanged();

		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.group_pois_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.group_tours_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.group_members_tab))
				.setTabListener(this));
	}
	
	public void initializeOwnerContent(){
		listFragments.add(new GroupDetailsSuggestionsFragment());
		adapterViewPager.notifyDataSetChanged();

		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.group_suggestions_tab))
				.setTabListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.group_view_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (!group.isOwner(user)) {
			menu.removeItem(R.id.action_edit);
			menu.removeItem(R.id.action_delete);
			menu.removeItem(R.id.action_view_suggestions);
		}
		
		if (group.isOwner(user)) {
			menu.removeItem(R.id.action_join_group);
			menu.removeItem(R.id.action_disjoin_group);
		}
		
		if (group.isJoined())
			menu.removeItem(R.id.action_join_group);
		else
			menu.removeItem(R.id.action_disjoin_group);

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
		case R.id.action_view_suggestions:			
			actionBar.setSelectedNavigationItem((listFragments.size() - 1)); 
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

	public static class MyPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPagerAdapter(FragmentManager fragmentManager,
				List<Fragment> fragments) {
			super(fragmentManager);
			this.fragments = fragments;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Page " + position;
		}

	}

	public void editGroup() {
		Intent intent = new Intent(this, GroupFormActivity.class);
		intent.putExtra("group", group);
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
										+ "/groups/destroy";
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
		if (group.isPrivate()) {
			performJoinPrivateGroup();
		} else
			performJoinPublicGroup();
	}

	private void performJoinPublicGroup() {
		performJoinGroup();
	}

	private void performJoinPrivateGroup() {
		passwordDialog = new Dialog(this);
		passwordDialog.setContentView(R.layout.private_group_password_request);
		passwordDialog.setTitle(getString(R.string.private_group_dialog_title));
		passwordDialog.setCancelable(true);
		insertedPrivateGroupPassword = (EditText) passwordDialog
				.findViewById(R.id.private_group_password);
		okButton = (Button) passwordDialog.findViewById(R.id.btnOk);
		cancelButton = (Button) passwordDialog.findViewById(R.id.btnCancel);

		addListenerOnPasswordField();
		addListenerOnButtons();
		passwordDialog.show();
	}

	private void performJoinGroup() {
		progress = ProgressDialog.show(GroupDetailsActivity.this,
				getString(R.string.loading), getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/groups/join_user";
		GroupMember groupMember = new GroupMember(user.getId(), group.getId());
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(JOIN_GROUP_METHOD,
				groupMember);
		httpAsyncTask.execute(url);
	}

	public void addListenerOnPasswordField() {
		insertedPrivateGroupPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (insertedPrivateGroupPassword.getText().length() > 0)
					okButton.setEnabled(true);
				else
					okButton.setEnabled(false);
			}
		});
	}

	public void addListenerOnButtons() {
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (insertedPrivateGroupPassword.getText().toString().equals(
						group.getPassword())) {
					passwordDialog.hide();
					performJoinGroup();
				} else {
					passwordDialog.hide();
					showError(getString(R.string.invalid_private_group_password));
				}
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				passwordDialog.hide();
			}
		});

	}

	public void disjoinGroup() {
		progress = ProgressDialog.show(GroupDetailsActivity.this,
				getString(R.string.loading), getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/groups/disjoin_group";
		GroupMember groupMember = new GroupMember(user.getId(), group.getId());
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(DISJOIN_GROUP_METHOD,
				groupMember);
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

				invalidateOptionsMenu();

				refreshInformationFragment();
				refreshInterestsFragment();
				refreshPoisFragment();
				refreshToursFragment();
				refreshMembersFragment();				
				break;
			}
			break;
		}

	}

	public void acceptSuggestion(Suggestion suggestion){
		progress = ProgressDialog.show(GroupDetailsActivity.this,
				getString(R.string.loading), getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/suggestions/accept";
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(ACCEPT_SUGGESTION_METHOD,
				suggestion);
		httpAsyncTask.execute(url);
	}
	
	public void rejectSuggestion(Suggestion suggestion){
		progress = ProgressDialog.show(GroupDetailsActivity.this,
				getString(R.string.loading), getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/suggestions/reject";
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(REJECT_SUGGESTION_METHOD,
				suggestion);
		httpAsyncTask.execute(url);
	}
	
	private void refreshMembersFragment() {
		GroupDetailsMembersFragment membersFragment = (GroupDetailsMembersFragment) listFragments
				.get(GROUP_MEMBERS_TAB);
		membersFragment.refreshList(group);
	}

	private void refreshToursFragment() {
		GroupDetailsToursFragment toursFragment = (GroupDetailsToursFragment) listFragments
				.get(GROUP_TOURS_TAB);
		toursFragment.refreshList(group);
	}

	private void refreshPoisFragment() {
		GroupDetailsPoisFragment poisFragment = (GroupDetailsPoisFragment) listFragments
				.get(GROUP_POIS_TAB);
		poisFragment.refreshList(group);
	}

	private void refreshInterestsFragment() {
		GroupDetailsInterestsFragment interestsFragment = (GroupDetailsInterestsFragment) listFragments
				.get(GROUP_INTERESTS_TAB);
		interestsFragment.refreshList(group);
	}

	private void refreshInformationFragment() {
		GroupDetailsInformationFragment informationFragment = (GroupDetailsInformationFragment) listFragments
				.get(GROUP_INFORMATION_TAB);
		informationFragment.setFields(group);
	}
	
	private void refreshSuggestionsFragment() {
		GroupDetailsSuggestionsFragment suggestionsFragment = (GroupDetailsSuggestionsFragment) listFragments
				.get((listFragments.size() - 1));
		suggestionsFragment.refreshList(group);
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
			case ACCEPT_SUGGESTION_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send, "");
			break;
			case REJECT_SUGGESTION_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send, "");
			break;
			}

			return api_result.getResult();
		}

		@Override
		protected void onPostExecute(String result) {
			switch (this.from_method) {
			case DELETE_GROUP_METHOD:
				progress.dismiss();
				if (api_result.ok())
					finish();
				else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.delete_group_message));
					exception.alertExceptionMessage(GroupDetailsActivity.this);
				}

				break;
			case JOIN_GROUP_METHOD:
				progress.dismiss();
				if (api_result.ok()) {
					group.addMember(user);
					group.joined(true);
					invalidateOptionsMenu();

					if(group.isPrivate())
						initializePublicGroupContent();
					else
						refreshMembersFragment();

					Toast.makeText(GroupDetailsActivity.this,
							R.string.group_joined_message,
							Toast.LENGTH_SHORT).show();
				} else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.group_already_joined_message));
					exception.alertExceptionMessage(GroupDetailsActivity.this);
				}
				break;
			case DISJOIN_GROUP_METHOD:
				progress.dismiss();
				if (api_result.ok()) {
					group.findAndRemoveMember(user);
					group.joined(false);
					invalidateOptionsMenu();
					refreshMembersFragment();
					Toast.makeText(GroupDetailsActivity.this,
							R.string.group_disjoined_message,
							Toast.LENGTH_SHORT).show();
				} else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.group_already_disjoined_message));
					exception.alertExceptionMessage(GroupDetailsActivity.this);
				}
				break;
			case ACCEPT_SUGGESTION_METHOD:
				progress.dismiss();
				if (api_result.ok()) {					
					Suggestion suggestion = (Suggestion) object_to_send;
					try {
						if (suggestion.ofPoi()){
							JSONObject groupPoiJson;
							groupPoiJson = new JSONObject(result);						
							GroupPoi groupPoi = GroupPoi.fromJSON(groupPoiJson);
							group.addGroupPoi(groupPoi);
							refreshPoisFragment();
						}					
						if (suggestion.ofTour()){
							JSONObject groupTourJson = new JSONObject(result);
							GroupTour groupTour = GroupTour.fromJSON(groupTourJson);
							group.addGroupTour(groupTour);
							refreshToursFragment();
						}
					} catch (JSONException e) {
						showExceptionError(e);
					} catch (ParseException e) {
						showExceptionError(e);
					}
																				
					group.removeSuggestion(suggestion);
					refreshSuggestionsFragment();
					Toast.makeText(GroupDetailsActivity.this,
							R.string.suggestion_accepted,
							Toast.LENGTH_SHORT).show();
				} else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.group_already_disjoined_message));
					exception.alertExceptionMessage(GroupDetailsActivity.this);
				}
				break;	
			case REJECT_SUGGESTION_METHOD:
				progress.dismiss();
				if (api_result.ok()) {
					group.removeSuggestion((Suggestion) object_to_send);
					refreshSuggestionsFragment();
					Toast.makeText(GroupDetailsActivity.this,
							R.string.suggestion_rejected,
							Toast.LENGTH_SHORT).show();
				} else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.group_already_disjoined_message));
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
		showError(e.getMessage());
	}

	public void showError(String errorMessage) {
		CustomTravelJoinException exception = new CustomTravelJoinException(
				errorMessage);
		exception.alertExceptionMessage(this);
	}
}