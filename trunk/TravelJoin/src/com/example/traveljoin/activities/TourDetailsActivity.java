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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.example.traveljoin.R;

import android.content.DialogInterface;

import com.example.traveljoin.adapters.SmartFragmentStatePagerAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.ListDialogSelectGroupFragment;
import com.example.traveljoin.fragments.ListDialogSelectGroupFragment.OnListDialogItemSelect;
import com.example.traveljoin.fragments.TourDetailInformationFragment;
import com.example.traveljoin.fragments.TourDetailPoisFragment;
import com.example.traveljoin.fragments.TourInformationRatingsFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Favorite;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Rating;
import com.example.traveljoin.models.Suggestion;
import com.example.traveljoin.models.Tour;
import com.example.traveljoin.models.User;

public class TourDetailsActivity extends ActionBarActivity implements
	ActionBar.TabListener, OnListDialogItemSelect{
	
	private MyPagerAdapter adapterViewPager;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private RatingBar ratingBar;
	private EditText editTextComment;	
	private Button btnRatingOk;	
	private Button btnRatingCancel;
	
	private static final int EDIT_TOUR_REQUEST = 1;
	protected static final int DELETE_TOUR_METHOD = 2;
	protected static final int ADD_TO_FAVORITES_METHOD = 3;
	protected static final int REMOVE_FROM_FAVORITES_METHOD = 4;
	protected static final int ADD_RATING_METHOD = 5;
	protected static final int GET_TOUR_METHOD = 6;
	protected static final int ADD_SUGGESTION_METHOD = 7;
	
	ProgressDialog progress;
	public Tour tour = null;
	User user;
	private Dialog rankDialog;
	private boolean mIsMenuFirstClick = true;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);
        initializeUser();
        Bundle b = getIntent().getExtras(); // gets the previously created intent
        Integer tourId = (Integer) b.get("tour_id");
		getTourFromServer(tourId);
        
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.tours_view);
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
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.pois))
				.setTabListener(this));                        
    }   
    
	public void getTourFromServer(Integer tourId) {
		progress = ProgressDialog.show(TourDetailsActivity.this,
				getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/tours/show.json?tour_id=" + tourId + "&user_id=" + user.getId();
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(GET_TOUR_METHOD, null);
		httpAsyncTask.execute(url);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tour_view_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (tour != null){				
			if (!user.getId().equals(tour.getUserId())) {
				//si no es el due��o
				menu.removeItem(R.id.action_edit);
				menu.removeItem(R.id.action_delete);
			}
			else{
				//si es el due��o
				menu.removeItem(R.id.action_calificate);
			}
			//si ya es favorito solo le va a aparecer la accion para borrarlo de favoritos
			if (tour.getIsFavorite()){
				menu.removeItem(R.id.action_add_to_favorites);
			}
			else{
				menu.removeItem(R.id.action_delete_from_favorites);
			}
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch(keyCode) {
	    case KeyEvent.KEYCODE_MENU:
	        if(mIsMenuFirstClick  ) {
	            mIsMenuFirstClick = false;
	            supportInvalidateOptionsMenu();
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: 
			onBackPressed();
			return true;
		case R.id.action_edit:
			editTour();
			return true;
		case R.id.action_delete:
			deleteTour();
			return true;
		case R.id.action_calificate:
			calificateTour();
			return true;
		case R.id.action_add_to_favorites:
			addToFavourites();
			return true;
		case R.id.action_delete_from_favorites:
			removeFromFavourites();
			return true;	
		case R.id.action_suggest:
			suggestTour();
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
	
    // Extend from SmartFragmentStatePagerAdapter now instead for more dynamic ViewPager items
    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
    	private static int NUM_ITEMS = 2;
		public static final int TOUR_INFORMATION_TAB = 0;
		public static final int POIS_TAB = 1;

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
            case TOUR_INFORMATION_TAB: // Fragment # 0 - This will show FirstFragment
                return new TourDetailInformationFragment();
            case POIS_TAB: // Fragment # 0 - This will show FirstFragment different title
                return new TourDetailPoisFragment();
            default:
                return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
        
		public TourDetailInformationFragment getInfoFragment(){
			return (TourDetailInformationFragment) getRegisteredFragment(TOUR_INFORMATION_TAB);
		}
		
		public TourDetailPoisFragment getPoisFragment(){
			return (TourDetailPoisFragment) getRegisteredFragment(POIS_TAB);
		}

    }	
    
	//cuando se clickea el boton editar viene aca!
	public void editTour() { 
		Intent intent = new Intent(this, TourFormActivity.class);
		intent.putExtra("tour", tour); //le pasamos el punto al form
		//va al form para editarlo
		startActivityForResult(intent, EDIT_TOUR_REQUEST);
	}
	
	//cuando se clickea el boton borrar viene aca!
	public void deleteTour() { 
		AlertDialog.Builder dialog = new AlertDialog.Builder(TourDetailsActivity.this);
		dialog.setTitle(getString(R.string.delete_tour))
        .setMessage(getString(R.string.delete_poi_message))
        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	progress = ProgressDialog.show(TourDetailsActivity.this, getString(R.string.loading),
            			getString(R.string.wait), true);
            	String url = getResources().getString(R.string.api_url) + "/tours/destroy";
    	        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(DELETE_TOUR_METHOD, tour); 
    	        httpAsyncTask.execute(url);
    	        //sigue en HttpAsyncTask en doInBackground en DELETE_POI_METHOD
            	
            }
         })
         .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // do nothing
            }
         })
		.setIcon(android.R.drawable.ic_dialog_alert)
         .show();
	}
    
	public void calificateTour() {
		rankDialog = new Dialog(this);
		rankDialog.setContentView(R.layout.calificate);
		rankDialog.setTitle(getString(R.string.rank_tour));
		rankDialog.setCancelable(true);
		ratingBar = (RatingBar)rankDialog.findViewById(R.id.ratingBar);		
		editTextComment = (EditText)rankDialog.findViewById(R.id.comment);
		btnRatingOk = (Button)rankDialog.findViewById(R.id.btnRatingOk);
		btnRatingCancel = (Button)rankDialog.findViewById(R.id.btnRatingCancel);
		if (tour.getRating() != null){
			ratingBar.setRating(tour.getRating().getValue());
			editTextComment.setText(tour.getRating().getComment());
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
					progress = ProgressDialog.show(TourDetailsActivity.this,
							getString(R.string.loading),
							getString(R.string.wait), true);
					String url = getResources().getString(R.string.api_url)
							+ "/ratings/add";
					Rating rating = new Rating(user.getId(), tour.getId(), "Tour",
							value, editTextComment.getText().toString(), null, null);
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
	  
	public void suggestTour(){
		FragmentManager fm = getSupportFragmentManager();
		ListDialogSelectGroupFragment groupsFragment = 
				new ListDialogSelectGroupFragment(this, user.getGroups(), "��A qu�� grupo desea sugerir este circuito?");
		groupsFragment.show(fm, "groups_picker");	
	}
	
	//cuando se selecciona el grupo al cual sugerir el Poi
    @Override
    public void onListItemSelected(GeneralItem selection) {    
		progress = ProgressDialog.show(TourDetailsActivity.this,
				getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/suggestions/add";
		Suggestion suggestion = new Suggestion(null, user.getId(),selection.getId(), tour.getId(), "Tour");
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
				ADD_SUGGESTION_METHOD, suggestion);
		httpAsyncTask.execute(url);
    }  
    
	public void addToFavourites() {
		progress = ProgressDialog.show(TourDetailsActivity.this,
				getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/favorites/add";
		Favorite favorite = new Favorite(user.getId(), tour.getId(), "Tour");
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
				ADD_TO_FAVORITES_METHOD, favorite);
		httpAsyncTask.execute(url);
	}
	
	public void removeFromFavourites() {
		progress = ProgressDialog.show(TourDetailsActivity.this,
				getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/favorites/remove";
		Favorite favorite = new Favorite(user.getId(), tour.getId(), "Tour");
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
				REMOVE_FROM_FAVORITES_METHOD, favorite);
		httpAsyncTask.execute(url);
	}
    
    /*Cuando vuelve de un activity empezado con un startActivityForResult viene aca*/
    @Override
    protected void onActivityResult(
    		int requestCode, int resultCode, Intent data) {
    	// Decide what to do based on the original request code
    	switch (requestCode) {
	    	case EDIT_TOUR_REQUEST :
	    		switch (resultCode) {
		    		case Activity.RESULT_OK :
		    	        Bundle b = data.getExtras(); // gets the previously created intent
		    	        Boolean isFavorite = tour.getIsFavorite();
		    	        Rating rating = tour.getRating();
		    	        
		    	        tour = (Tour) b.get("tour_created_or_updated");
		    	        tour.setIsFavorite(isFavorite);
		    	        tour.setRating(rating);
		    	        
		    	        invalidateOptionsMenu();
		    	        adapterViewPager.getInfoFragment().setFields();
		    	        adapterViewPager.getPoisFragment().refreshList();		    	        
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
    	
    	//contructor para setearle info extra
    	public HttpAsyncTask(Integer from_method, Object object_to_send) {
			this.from_method = from_method;
			this.object_to_send = object_to_send;  
		}
    	
        @Override
        protected String doInBackground(String... urls) {  
        	//despues de cualquiera de estos metodo vuelve al postexecute de aca
        	switch (this.from_method) {
	        	case GET_TOUR_METHOD:
					api_result = apiInterface.GET(urls[0]);
				break;
	        	case DELETE_TOUR_METHOD:
	        		api_result = apiInterface.POST(urls[0], object_to_send, "delete");
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
				case ADD_SUGGESTION_METHOD:
					api_result = apiInterface.POST(urls[0], object_to_send, "");
				break;				
        	}
        	
        	return api_result.getResult();             
        }
        
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {        	
        	//Log.d("InputStream", result);
        	switch (this.from_method) {   
				case GET_TOUR_METHOD:
					progress.dismiss();
					
					JSONObject tourJson;
					if (api_result.ok()){
						try {
							tourJson = new JSONObject(result);
							tour = Tour.fromJSON(tourJson);
							TourDetailInformationFragment infoFragment = adapterViewPager.getInfoFragment();
			    	        infoFragment.setFields();
			    	        infoFragment.setOwnerInformation();
			    	        adapterViewPager.getPoisFragment().refreshList();
							if (tour.getLastRatings() != null && !tour.getLastRatings().isEmpty()){
								android.support.v4.app.FragmentManager fragment_manager = getSupportFragmentManager();
								fragment_manager.beginTransaction().add(R.id.fragmentTourInformation, new TourInformationRatingsFragment()).commit();
							}							
							infoFragment.scrollTop();
						} catch (JSONException e) {
							showExceptionError(e);
						} catch (ParseException e) {
							showExceptionError(e);
						}						
					}						
					else {
						CustomTravelJoinException exception = new CustomTravelJoinException(
								getString(R.string.connection_error_message));
						exception.alertExceptionMessage(TourDetailsActivity.this);
					}
	
				break;
	        	case DELETE_TOUR_METHOD :
	        		progress.dismiss(); 
						if (api_result.ok())												
							finish();
						else{
							CustomTravelJoinException exception = new CustomTravelJoinException(getString(R.string.delete_tour_error_message));
							exception.alertExceptionMessage(TourDetailsActivity.this);
						}
							
		        break;	
				case ADD_TO_FAVORITES_METHOD:
					progress.dismiss();
					if (api_result.ok()){						
						tour.setIsFavorite(true);
						invalidateOptionsMenu();
						Toast.makeText(TourDetailsActivity.this, R.string.tour_added_to_favorites_message, Toast.LENGTH_SHORT).show();
					}					
					else {
						CustomTravelJoinException exception = new CustomTravelJoinException(
								getString(R.string.tour_already_in_favorites_message));
						exception.alertExceptionMessage(TourDetailsActivity.this);
					}			
				break;
				case REMOVE_FROM_FAVORITES_METHOD:
					progress.dismiss();
					if (api_result.ok()){						
						tour.setIsFavorite(false);
						invalidateOptionsMenu();
						Toast.makeText(TourDetailsActivity.this, R.string.tour_removed_from_favorites_message, Toast.LENGTH_SHORT).show();
					}					
					else {
						CustomTravelJoinException exception = new CustomTravelJoinException(
								getString(R.string.tour_already_removed_from_favorites_message));
						exception.alertExceptionMessage(TourDetailsActivity.this);
					}			
				break;
				case ADD_RATING_METHOD:
					JSONObject ratingJson;
					progress.dismiss();
					if (api_result.ok()){	
						try {
							ratingJson = new JSONObject(result);
							Rating rating = Rating.fromJSON(ratingJson);
							if (tour.getRating() != null){
								//si antes teniamos un rating, significa que acabamos de actualizarlo, le restamos el viejo y sumamos el nuevo
								tour.setRatingsSum(tour.getRatingsSum() - tour.getRating().getValue() + rating.getValue());
							}
							else{
								//sino significa que acabamos de crear un nuevo rating
								tour.setRatingsCount(tour.getRatingsCount() + 1);
								tour.setRatingsSum(tour.getRatingsSum() + rating.getValue());
							}
							tour.setRating(rating);
							adapterViewPager.getInfoFragment().setFields();
							rankDialog.hide();
							Toast.makeText(TourDetailsActivity.this, R.string.poi_ranked_ok_message, Toast.LENGTH_SHORT).show();
						} catch (JSONException e) {
							showExceptionError(e);
						} catch (ParseException e) {
							showExceptionError(e);
						}								
					}					
					else {
						CustomTravelJoinException exception = new CustomTravelJoinException(
								getString(R.string.connection_error_message));
						exception.alertExceptionMessage(TourDetailsActivity.this);
					}			
				break;
                case ADD_SUGGESTION_METHOD:					
					progress.dismiss();
					if (api_result.ok()){												
						Toast.makeText(TourDetailsActivity.this, R.string.tour_suggested_ok_message, Toast.LENGTH_SHORT).show();																				
					}					
					else{
						if (api_result.unprocessableEntity()){
							CustomTravelJoinException exception = new CustomTravelJoinException(
									getString(R.string.error_message), api_result.getResult());
							exception.alertExceptionMessage(TourDetailsActivity.this);
						}
						else{
							CustomTravelJoinException exception = new CustomTravelJoinException(
									getString(R.string.connection_error_message));
							exception.alertExceptionMessage(TourDetailsActivity.this);
						}
					}			
				break;				
        	}
       }        
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	//borrar la referencia a el cartelito del dialogo, sino trae problemas
    	if(progress != null)
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
