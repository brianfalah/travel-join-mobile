package com.example.traveljoin.activities;

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
import android.widget.Toast;

import com.example.traveljoin.R;

import android.content.DialogInterface;

import com.example.traveljoin.adapters.SmartFragmentStatePagerAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.TourDetailInformationFragment;
import com.example.traveljoin.fragments.TourDetailPoisFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Favorite;
import com.example.traveljoin.models.Tour;
import com.example.traveljoin.models.User;

public class TourDetailsActivity extends ActionBarActivity implements
	ActionBar.TabListener{
	
	private SmartFragmentStatePagerAdapter adapterViewPager;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private static final int EDIT_TOUR_REQUEST = 1;
	protected static final int DELETE_TOUR_METHOD = 2;
	protected static final int ADD_TO_FAVORITES_METHOD = 3;
	protected static final int REMOVE_FROM_FAVORITES_METHOD = 4;
	
	ProgressDialog progress;
	public Tour tour;
	User user;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);
        initializeUser();
        adapterViewPager = new MyPagerAdapter(
				getSupportFragmentManager());
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
        
                
        Bundle b = getIntent().getExtras(); // gets the previously created intent
        tour = (Tour) b.get("tour"); 
    }   
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tour_view_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (!user.getId().equals(tour.getUserId())) {
			menu.removeItem(R.id.action_edit);
			menu.removeItem(R.id.action_delete);
			menu.removeItem(R.id.action_calificate);
			menu.removeItem(R.id.action_denounce);
		}
		//si ya es favorito solo le va a aparecer la accion para borrarlo de favoritos
		if (tour.getIsFavorite()){
			menu.removeItem(R.id.action_add_to_favorites);
		}
		else{
			menu.removeItem(R.id.action_delete_from_favorites);
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
	
    public void calificateTour(){
    	final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.calificate);		
		dialog.setTitle(getString(R.string.rank_tour));	
		dialog.show();
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
		    	        tour = (Tour) b.get("tour_created_or_updated");
		    	        tour.setIsFavorite(isFavorite);
		    	        invalidateOptionsMenu();
		    	        TourDetailInformationFragment infoFragment = (TourDetailInformationFragment) adapterViewPager.getRegisteredFragment(0);
		    	        infoFragment.setFields();
		    	        TourDetailPoisFragment eventsFragment = (TourDetailPoisFragment) adapterViewPager.getRegisteredFragment(1);
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
    	
    	//contructor para setearle info extra
    	public HttpAsyncTask(Integer from_method, Object object_to_send) {
			this.from_method = from_method;
			this.object_to_send = object_to_send;  
		}
    	
        @Override
        protected String doInBackground(String... urls) {  
        	//despues de cualquiera de estos metodo vuelve al postexecute de aca
        	switch (this.from_method) {
	        	case DELETE_TOUR_METHOD :
	        		api_result = apiInterface.POST(urls[0], object_to_send, "delete");
	        	break;
				case ADD_TO_FAVORITES_METHOD:
					api_result = apiInterface.POST(urls[0], object_to_send, "");
				break;	
				case REMOVE_FROM_FAVORITES_METHOD:
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
}
