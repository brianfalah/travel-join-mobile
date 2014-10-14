package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.SmartFragmentStatePagerAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.TourFormInformationFragment;
import com.example.traveljoin.fragments.TourFormPoisFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Tour;
import com.example.traveljoin.models.TourPoi;
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
import android.widget.ArrayAdapter;
import android.widget.Button;

public class TourFormActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	User user;
	ProgressDialog progress;
	public Tour tour;
	Button createButton;
	Button updateButton;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private SmartFragmentStatePagerAdapter adapterViewPager;
	public ArrayList<GeneralItem> tourPois;
	public ArrayList<TourPoi> tourPoisToDelete;
	ArrayAdapter<TourPoi> tourPoisAdapter;

	private static final int ADD_TOUR_METHOD = 1;
	private static final int UPDATE_TOUR_METHOD = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour_form);
		initializeUser();
		initializeViewReferences();

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.TourPager);
		viewPager.setAdapter(adapterViewPager);
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tour_general_information_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tour_pois_tab))
				.setTabListener(this));

		tourPois = new ArrayList<GeneralItem>();
		tourPoisToDelete = new ArrayList<TourPoi>();

		if (getIntent().getExtras() != null) {
			actionBar.setSubtitle(R.string.tours_edition);
			tour = (Tour) getIntent().getExtras().get("tour");
			updateButton.setVisibility(View.VISIBLE);
			tourPois.addAll(tour.getTourPois());
		} else {
			actionBar.setSubtitle(R.string.tours_creation);
			tour = null;
			createButton.setVisibility(View.VISIBLE);
		}
	}

	private void initializeViewReferences() {
		createButton = (Button) findViewById(R.id.TourCreateButton);
		updateButton = (Button) findViewById(R.id.TourUpdateButton);
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
		private static int NUM_ITEMS = 2;
		public static final int TOUR_INFORMATION_TAB = 0;
		public static final int TOUR_POIS_TAB = 1;

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case TOUR_INFORMATION_TAB: // Fragment # 0 - This will show
										// FirstFragment
				return new TourFormInformationFragment();
			case TOUR_POIS_TAB: // Fragment # 0 - This will show FirstFragment
								// different title
				return new TourFormPoisFragment();
			default:
				return null;
			}
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {
			return "Page " + position;
		}

	}

	// cuando se clickea el boton crear viene aca!
	public void createTour(View button) {
		TourFormInformationFragment info_fragment = (TourFormInformationFragment) adapterViewPager
				.getRegisteredFragment(0);
		Boolean valid = info_fragment.validateFields();
		if (valid) {
			progress = ProgressDialog.show(this, getString(R.string.loading),
					getString(R.string.wait), true);

			Tour tour_to_create = new Tour(null, info_fragment.nameField
					.getText().toString(), info_fragment.descField.getText()
					.toString(), user.getId(), tourPois);

			String url = getResources().getString(R.string.api_url)
					+ "/tours/create";
			HttpAsyncTask httpAsyncTask = new HttpAsyncTask(ADD_TOUR_METHOD,
					tour_to_create);
			httpAsyncTask.execute(url);
			// sigue en onPostExecute, en la parte de ADD_TOUR_METHOD
		}

	}

	// cuando se clickea el boton actualizar viene aca!
	public void updateTour(View button) {
		TourFormInformationFragment info_fragment = (TourFormInformationFragment) adapterViewPager
				.getRegisteredFragment(0);
		Boolean valid = info_fragment.validateFields();
		if (valid) {
			progress = ProgressDialog.show(this, getString(R.string.loading),
					getString(R.string.wait), true);
			tour = new Tour(tour.getId(), info_fragment.nameField.getText()
					.toString(), info_fragment.descField.getText().toString(),
					user.getId(), tourPois);
			tour.setTourPoisToDelete(tourPoisToDelete);

			String url = getResources().getString(R.string.api_url)
					+ "/tours/update";
			HttpAsyncTask httpAsyncTask = new HttpAsyncTask(UPDATE_TOUR_METHOD,
					tour);
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
			case ADD_TOUR_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send,
						"create");
				break;
			case UPDATE_TOUR_METHOD:
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
			case ADD_TOUR_METHOD:
				progress.dismiss();
				// para volver al mapa
				JSONObject tourJson;
				if (api_result.ok()) {
					try {
						tourJson = new JSONObject(result);
						Tour tour_created = Tour.fromJSON(tourJson);
						closeActivity(tour_created);
					} catch (JSONException e) {
						showExceptionError(e);
					} catch (ParseException e) {
						showExceptionError(e);
					}
				} else {
					showConnectionError();
				}

				break;
			case UPDATE_TOUR_METHOD:
				progress.dismiss();
				// para volver al mapa
				JSONObject tourUpdatedJson;
				if (api_result.ok()) {
					try {
						tourUpdatedJson = new JSONObject(result);
						Tour tour_updated = Tour.fromJSON(tourUpdatedJson);
						closeActivity(tour_updated);
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

	public void closeActivity(Tour tour_created_or_updated) {
		Intent output = new Intent();
		tour_created_or_updated.setTourPois(tourPois);
		output.putExtra("tour_created_or_updated", tour_created_or_updated);
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