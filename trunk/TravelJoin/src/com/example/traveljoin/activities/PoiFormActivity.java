package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
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
import android.widget.ListView;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.SmartFragmentStatePagerAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.PoiFormEventsFragment;
import com.example.traveljoin.fragments.PoiFormInformationFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.Category;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.PoiEvent;
import com.example.traveljoin.models.User;
import com.google.android.gms.maps.model.LatLng;

public class PoiFormActivity extends ActionBarActivity implements
	ActionBar.TabListener{
	
	private SmartFragmentStatePagerAdapter adapterViewPager;
	private ViewPager viewPager;
	private ActionBar actionBar;
	PoiFormInformationFragment info_fragment;
	
	User user;
	ProgressDialog progress;
	
	Button createButton;
	Button updateButton;
	public LatLng point;
	public Poi poi;
	public ArrayList<GeneralItem> poiEvents;
	public ArrayList<PoiEvent> poiEventsToDelete;
	ArrayAdapter<PoiEvent> poiEventsAdapter;
//	ListView lvEvents;

	private static final int ADD_POI_METHOD = 1;
	private static final int UPDATE_POI_METHOD = 2;
	private static final int ADD_EVENT_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_poi_form);
		initializeUser();
		initializeViewReferences();
		
		adapterViewPager = new MyPagerAdapter(
				getSupportFragmentManager());
		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.edit_poi);
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
		
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.general_user_profile_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.events))
				.setTabListener(this));				
		
		poiEvents = new ArrayList<GeneralItem>();
		poiEventsToDelete = new ArrayList<PoiEvent>();
				
		//si viene del mapa, se esta creando, y por eso solo llega el latlng
		point = (LatLng) getIntent().getExtras().get("point");
		poi = (Poi) getIntent().getExtras().get("poi");
		

//		poiEventsAdapter = new ArrayAdapter<PoiEvent>(this,
//				android.R.layout.simple_list_item_multiple_choice, poiEvents);
//		lvEvents.setAdapter(poiEventsAdapter);
	}
	
	@Override
	protected void onStart() {
	    super.onStart();  // Always call the superclass method first
//	    info_fragment = (PoiFormInformationFragment) adapterViewPager.getRegisteredFragment(0);
		if (poi != null)
			initializeViewForEditingMode();
		else
			initializeViewForCreatingMode();	    
	}

	private void initializeUser() {
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		user = globalContext.getUser();
	}
	
	// Extend from SmartFragmentStatePagerAdapter now instead for more dynamic ViewPager items
    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
    	private static int NUM_ITEMS = 2;
		public static final int POI_INFORMATION_TAB = 0;
		public static final int POI_EVENTS_TAB = 1;

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
            case POI_INFORMATION_TAB: // Fragment # 0 - This will show FirstFragment
                return new PoiFormInformationFragment();
            case POI_EVENTS_TAB: // Fragment # 0 - This will show FirstFragment different title
                return new PoiFormEventsFragment();
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

	private void initializeViewForCreatingMode() {		
		createButton.setVisibility(View.VISIBLE);
//		LatLng point = new LatLng(poi.getLatitude(), poi.getLongitude());
//		info_fragment.setHiddenFields(point);
	}

	private void initializeViewForEditingMode() {
//		LatLng point = new LatLng(poi.getLatitude(), poi.getLongitude());
//		info_fragment.setHiddenFields(point);
//        info_fragment.setFields();        
		
		updateButton.setVisibility(View.VISIBLE);
		poiEvents.addAll(poi.getPoiEvents());		
	}

	private void initializeViewReferences() {
		createButton = (Button) findViewById(R.id.PoiCreateButton);
		updateButton = (Button) findViewById(R.id.PoiUpdateButton);	
	}

	// cuando se clickea el boton crear viene aca!
	public void createPoi(View button) {
		Boolean valid = info_fragment.validateFields();
		if (valid) {
			progress = ProgressDialog.show(this, "Cargando",
					"Por favor espere...", true);
			
			Poi poi_to_create = new Poi(null, Double.parseDouble(info_fragment.tvLatitude
					.getText().toString()), Double.parseDouble(info_fragment.tvLongitude
					.getText().toString()), info_fragment.nameField.getText().toString(),
					info_fragment.descField.getText().toString(), info_fragment.addressField.getText()
							.toString(), user.getId(),
					((Category) info_fragment.poiCategoriesSpinnerField.getSelectedItem()).getId(), "",
					poiEvents);

			String url = getResources().getString(R.string.api_url)
					+ "/pois/create";
			HttpAsyncTask httpAsyncTask = new HttpAsyncTask(ADD_POI_METHOD,
					poi_to_create);
			httpAsyncTask.execute(url);
			// sigue en onPostExecute, en la parte de ADD_POI_METHOD
		}
	}

	// cuando se clickea el boton actualizar viene aca!
	public void updatePoi(View button) {
		Boolean valid = info_fragment.validateFields();
		if (valid) {
			progress = ProgressDialog.show(this, "Cargando",
					"Por favor espere...", true);
			poi = new Poi(poi.getId(), Double.parseDouble(info_fragment.tvLatitude.getText()
					.toString()), Double.parseDouble(info_fragment.tvLongitude.getText()
					.toString()), info_fragment.nameField.getText().toString(), info_fragment.descField
					.getText().toString(), info_fragment.addressField.getText().toString(),
					user.getId(),
					((Category) info_fragment.poiCategoriesSpinnerField.getSelectedItem()).getId(), "",
					poiEvents);
			poi.setPoiEventsToDelete(poiEventsToDelete);

			String url = getResources().getString(R.string.api_url)
					+ "/pois/update";
			HttpAsyncTask httpAsyncTask = new HttpAsyncTask(UPDATE_POI_METHOD,
					poi);
			httpAsyncTask.execute(url);
			// sigue en HttpAsyncTask en doInBackground en UPDATE_POI_METHOD
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
			case ADD_POI_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send,
						"create");
				break;
			case UPDATE_POI_METHOD:
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
			case ADD_POI_METHOD:
				progress.dismiss();
				// para volver al mapa
				JSONObject poiJson;
				if (api_result.ok()) {
					try {
						poiJson = new JSONObject(result);
						Poi poi_created = Poi.fromJSON(poiJson);
						closeActivity(poi_created);
					} catch (JSONException e) {
						showExceptionError(e);
					} catch (ParseException e) {
						showExceptionError(e);
					}
				} else {
					showConnectionError();
				}

				break;
			case UPDATE_POI_METHOD:
				progress.dismiss();
				// para volver al mapa
				JSONObject poiUpdatedJson;
				if (api_result.ok()) {
					try {
						poiUpdatedJson = new JSONObject(result);
						Poi poi_updated = Poi.fromJSON(poiUpdatedJson);
						closeActivity(poi_updated);
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
		exception.alertConnectionProblem(getApplicationContext());
		// e.printStackTrace();
	}

	public void showExceptionError(Exception e) {
		CustomTravelJoinException exception = new CustomTravelJoinException(
				e.getMessage());
		exception.alertExceptionMessage(getApplicationContext());
		e.printStackTrace();
	}

	public void closeActivity(Poi poi_created_or_updated) {
		Intent output = new Intent();
		poi_created_or_updated.setPoiEvents(poiEvents);
		output.putExtra("poi_created_or_updated", poi_created_or_updated);
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
	
}
