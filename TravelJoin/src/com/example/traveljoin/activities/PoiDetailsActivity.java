package com.example.traveljoin.activities;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.example.traveljoin.R;

import android.content.DialogInterface;

import com.example.traveljoin.adapters.SmartFragmentStatePagerAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.PoiEventsFragment;
import com.example.traveljoin.fragments.PoiInformationFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Favorite;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.Rating;
import com.example.traveljoin.models.User;

public class PoiDetailsActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	private SmartFragmentStatePagerAdapter adapterViewPager;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private RatingBar ratingBar;
	private EditText editTextComment;	
	private Button btnRatingOk;	
	private Button btnRatingCancel;
		
	private static final int EDIT_POI_REQUEST = 1;
	protected static final int DELETE_POI_METHOD = 2;
	protected static final int ADD_TO_FAVORITES_METHOD = 3;		
	protected static final int REMOVE_FROM_FAVORITES_METHOD = 4;
	protected static final int ADD_RATING_METHOD = 5;
	protected static final int GET_POI_METHOD = 6;
	

	ProgressDialog progress;
	public Poi poi = null;
	User user;
	private Dialog rankDialog;	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_details);		
		initializeUser();
		Bundle b = getIntent().getExtras(); 
		Integer poiId = (Integer) b.get("poi_id");
		getPoiFromServer(poiId);
		
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
				.setText(getString(R.string.general_user_profile_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.events))
				.setTabListener(this));		
	}
	
	public void getPoiFromServer(Integer poiId) {
		progress = ProgressDialog.show(PoiDetailsActivity.this,
				getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/pois/show.json?poi_id=" + poiId + "&user_id=" + user.getId();
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(GET_POI_METHOD, null);
		httpAsyncTask.execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.poi_view_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (poi != null){					
			if (!user.getId().equals(poi.getUserId())) {
				//si no es el dueño
				menu.removeItem(R.id.action_edit);
				menu.removeItem(R.id.action_delete);				
			}
			else{
				//si es el dueño
				menu.removeItem(R.id.action_calificate);
				menu.removeItem(R.id.action_denounce);
			}
			//si ya es favorito solo le va a aparecer la accion para borrarlo de favoritos
			if (poi.getIsFavorite()){
				menu.removeItem(R.id.action_add_to_favorites);
			}
			else{
				menu.removeItem(R.id.action_delete_from_favorites);
			}
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
			editPoi();
			return true;
		case R.id.action_delete:
			deletePoi();
			return true;
		case R.id.action_calificate:
			calificatePoi();
			return true;
		case R.id.action_add_to_favorites:
			addToFavourites();
			return true;
		case R.id.action_delete_from_favorites:
			removeFromFavourites();
			return true;			
		case R.id.action_suggest:
			return true;
		case R.id.action_denounce:
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

	// Extend from SmartFragmentStatePagerAdapter now instead for more dynamic
	// ViewPager items
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
			case POI_INFORMATION_TAB: // Fragment # 0 - This will show
										// FirstFragment
				return new PoiInformationFragment();
			case POI_EVENTS_TAB: // Fragment # 0 - This will show FirstFragment
									// different title
				return new PoiEventsFragment();
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

	// cuando se clickea el boton editar viene aca!
	public void editPoi() {
		Intent intent = new Intent(this, PoiFormActivity.class);
		intent.putExtra("poi", poi); // le pasamos el punto al form
		// va al form para editarlo
		startActivityForResult(intent, EDIT_POI_REQUEST);
	}

	// cuando se clickea el boton borrar viene aca!
	public void deletePoi() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				PoiDetailsActivity.this);
		dialog.setTitle(getString(R.string.delete_poi))
				.setMessage(getString(R.string.delete_poi_message))
				.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						progress = ProgressDialog.show(PoiDetailsActivity.this,
								getString(R.string.loading),
								getString(R.string.wait), true);
						String url = getResources().getString(R.string.api_url)
								+ "/pois/destroy";
						HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
								DELETE_POI_METHOD, poi);
						httpAsyncTask.execute(url);
						// sigue en HttpAsyncTask en doInBackground en
						// DELETE_POI_METHOD

					}
				})
				.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	public void calificatePoi() {
		rankDialog = new Dialog(this);
		rankDialog.setContentView(R.layout.calificate);
		rankDialog.setTitle(getString(R.string.rank_poi));
		rankDialog.setCancelable(true);
		ratingBar = (RatingBar)rankDialog.findViewById(R.id.ratingBar);		
		editTextComment = (EditText)rankDialog.findViewById(R.id.comment);
		btnRatingOk = (Button)rankDialog.findViewById(R.id.btnRatingOk);
		btnRatingCancel = (Button)rankDialog.findViewById(R.id.btnRatingCancel);
		if (poi.getRating() != null){
			ratingBar.setRating(poi.getRating().getValue());
			editTextComment.setText(poi.getRating().getComment());
			btnRatingOk.setEnabled(true);
		}
		
		addListenerOnRatingBar();
		addListenerOnButtons();
		rankDialog.show();
	}
	
	  public void addListenerOnRatingBar() {		  	 
		//if rating value is changed,
		//display the current rating value in the result (textview) automatically
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
				if (rating > 0){
					btnRatingOk.setEnabled(true);
				}
				else{
					btnRatingOk.setEnabled(false);
				}
	 
			}
		});
	  }
	 
	  public void addListenerOnButtons() {	 
		  btnRatingOk.setOnClickListener(new OnClickListener() {	 
			public void onClick(View v) {				
				float value = ratingBar.getRating();
				if (value > 0){
					progress = ProgressDialog.show(PoiDetailsActivity.this,
							getString(R.string.loading),
							getString(R.string.wait), true);
					String url = getResources().getString(R.string.api_url)
							+ "/ratings/add";
					Rating rating = new Rating(user.getId(), poi.getId(), "Poi",
							value, editTextComment.getText().toString());
					HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
							ADD_RATING_METHOD, rating);
					httpAsyncTask.execute(url);
				}
			}	 
		  });
		  
		  btnRatingCancel.setOnClickListener(new OnClickListener() {	 
			public void onClick(View v) {				
				rankDialog.hide();
			}	 
		  });		  
	 
	  }
	
	public void addToFavourites() {
		progress = ProgressDialog.show(PoiDetailsActivity.this,
				getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/favorites/add";
		Favorite favorite = new Favorite(user.getId(), poi.getId(), "Poi");
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
				ADD_TO_FAVORITES_METHOD, favorite);
		httpAsyncTask.execute(url);
	}
	
	public void removeFromFavourites() {
		progress = ProgressDialog.show(PoiDetailsActivity.this,
				getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/favorites/remove";
		Favorite favorite = new Favorite(user.getId(), poi.getId(), "Poi");
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
				REMOVE_FROM_FAVORITES_METHOD, favorite);
		httpAsyncTask.execute(url);
	}


	/*
	 * Cuando vuelve de un activity empezado con un startActivityForResult viene
	 * aca
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		case EDIT_POI_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Bundle b = data.getExtras(); 
				Boolean isFavorite = poi.getIsFavorite();
				Rating rating = poi.getRating();
				
				poi = (Poi) b.get("poi_created_or_updated");
				poi.setIsFavorite(isFavorite);
				poi.setRating(rating);
				
				invalidateOptionsMenu();
				PoiInformationFragment infoFragment = (PoiInformationFragment) adapterViewPager
						.getRegisteredFragment(0);
				infoFragment.setFields();
				PoiEventsFragment eventsFragment = (PoiEventsFragment) adapterViewPager
						.getRegisteredFragment(1);
				eventsFragment.refreshList();				
				break;
			}
			break;
		}

	}

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
				case GET_POI_METHOD:
					api_result = apiInterface.GET(urls[0]);
				break;
				case DELETE_POI_METHOD:
					api_result = apiInterface.POST(urls[0], object_to_send,
						"delete");
				break;
				case ADD_TO_FAVORITES_METHOD:
					api_result = apiInterface.POST(urls[0], object_to_send, "");
				break;	
				case REMOVE_FROM_FAVORITES_METHOD:
					api_result = apiInterface.POST(urls[0], object_to_send, "");
				break;	
				case ADD_RATING_METHOD:
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
				case GET_POI_METHOD:
					progress.dismiss();
					
					JSONObject poiJson;
					if (api_result.ok()){
						try {
							poiJson = new JSONObject(result);
							poi = Poi.fromJSON(poiJson);
							PoiInformationFragment infoFragment = (PoiInformationFragment) adapterViewPager
									.getRegisteredFragment(0);
							infoFragment.setFields();
							infoFragment.setOwnerInformation();
							PoiEventsFragment eventsFragment = (PoiEventsFragment) adapterViewPager
									.getRegisteredFragment(1);
							eventsFragment.refreshList();
						} catch (JSONException e) {
							showExceptionError(e);
						} catch (ParseException e) {
							showExceptionError(e);
						}						
					}						
					else {
						CustomTravelJoinException exception = new CustomTravelJoinException(
								getString(R.string.connection_error_message));
						exception.alertExceptionMessage(PoiDetailsActivity.this);
					}
	
				break;
				case DELETE_POI_METHOD:
					progress.dismiss();
					if (api_result.ok())
						finish();
					else {
						CustomTravelJoinException exception = new CustomTravelJoinException(
								getString(R.string.delete_poi_error_message));
						exception.alertExceptionMessage(PoiDetailsActivity.this);
					}

				break;
				case ADD_TO_FAVORITES_METHOD:
					progress.dismiss();
					if (api_result.ok()){						
						poi.setIsFavorite(true);
						invalidateOptionsMenu();
						Toast.makeText(PoiDetailsActivity.this, R.string.poi_added_to_favorites_message, Toast.LENGTH_SHORT).show();
					}					
					else {
						CustomTravelJoinException exception = new CustomTravelJoinException(
								getString(R.string.poi_already_in_favorites_message));
						exception.alertExceptionMessage(PoiDetailsActivity.this);
					}			
				break;
				case REMOVE_FROM_FAVORITES_METHOD:
					progress.dismiss();
					if (api_result.ok()){						
						poi.setIsFavorite(false);
						invalidateOptionsMenu();
						Toast.makeText(PoiDetailsActivity.this, R.string.poi_removed_from_favorites_message, Toast.LENGTH_SHORT).show();
					}					
					else {
						CustomTravelJoinException exception = new CustomTravelJoinException(
								getString(R.string.poi_already_removed_from_favorites_message));
						exception.alertExceptionMessage(PoiDetailsActivity.this);
					}			
				break;
				case ADD_RATING_METHOD:
					JSONObject ratingJson;
					progress.dismiss();
					if (api_result.ok()){	
						try {
							ratingJson = new JSONObject(result);
							Rating rating = Rating.fromJSON(ratingJson);
							poi.setRating(rating);
							rankDialog.hide();
							Toast.makeText(PoiDetailsActivity.this, R.string.poi_ranked_ok_message, Toast.LENGTH_SHORT).show();
						} catch (JSONException e) {
							showExceptionError(e);
						}								
					}					
					else {
						CustomTravelJoinException exception = new CustomTravelJoinException(
								getString(R.string.connection_error_message));
						exception.alertExceptionMessage(PoiDetailsActivity.this);
					}			
				break;
			}								
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// borrar la referencia a el cartelito del dialogo, sino trae problemas
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
