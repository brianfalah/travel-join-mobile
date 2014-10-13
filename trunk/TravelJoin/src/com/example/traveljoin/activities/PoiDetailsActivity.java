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
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.traveljoin.R;

import android.content.DialogInterface;

import com.example.traveljoin.activities.UserProfileActivity.AppSectionsPagerAdapter;
import com.example.traveljoin.adapters.SmartFragmentStatePagerAdapter;
import com.example.traveljoin.fragments.PoiInformationFragment;
import com.example.traveljoin.fragments.UserFavouritesFragment;
import com.example.traveljoin.fragments.UserGroupListFragment;
import com.example.traveljoin.fragments.UserInformationFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Poi;
import com.google.android.gms.maps.model.LatLng;

public class PoiDetailsActivity extends ActionBarActivity implements
	ActionBar.TabListener{
	
	private SmartFragmentStatePagerAdapter adapterViewPager;
	private ViewPager viewPager;
	private ActionBar actionBar;
	private static final int EDIT_POI_REQUEST = 1;
	protected static final int DELETE_POI_METHOD = 2;
	
	ProgressDialog progress;
	public Poi poi;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_details);
        adapterViewPager = new MyPagerAdapter(
				getSupportFragmentManager());
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
		
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.general_user_profile_tab))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.events))
				.setTabListener(this));
        
                
        Bundle b = getIntent().getExtras(); // gets the previously created intent
        poi = (Poi) b.get("poi"); 
    }   
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.poi_view_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			editPoi();
			return true;
		case R.id.action_delete:
			deletePoi();
			return true;
		case R.id.action_calificate:
			calificatePoi();
			return true;
		case R.id.action_add_to_favourites:
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
                return new PoiInformationFragment();
            case POI_EVENTS_TAB: // Fragment # 0 - This will show FirstFragment different title
                return new PoiInformationFragment();
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
	public void editPoi() { 
		Intent intent = new Intent(this, PoiFormActivity.class);
		intent.putExtra("poi", poi); //le pasamos el punto al form
		//va al form para editarlo
		startActivityForResult(intent, EDIT_POI_REQUEST);
	}
	
	//cuando se clickea el boton borrar viene aca!
	public void deletePoi() { 
		AlertDialog.Builder dialog = new AlertDialog.Builder(PoiDetailsActivity.this);
		dialog.setTitle("Borrar punto")
        .setMessage("¿Esta seguro de que desea borrar este punto?")
        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	progress = ProgressDialog.show(PoiDetailsActivity.this, "Cargando",
                	    "Por favor espere...", true);
            	String url = getResources().getString(R.string.api_url) + "/pois/destroy";
    	        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(DELETE_POI_METHOD, poi); 
    	        httpAsyncTask.execute(url);
    	        //sigue en HttpAsyncTask en doInBackground en DELETE_POI_METHOD
            	
            }
         })
         .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // do nothing
            }
         })
		.setIcon(android.R.drawable.ic_dialog_alert)
         .show();
	}
	
    public void calificatePoi(){
    	final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.calificate);		
		dialog.setTitle("Calificar punto");	
		dialog.show();
    }	
    
    /*Cuando vuelve de un activity empezado con un startActivityForResult viene aca*/
    @Override
    protected void onActivityResult(
    		int requestCode, int resultCode, Intent data) {
    	// Decide what to do based on the original request code
    	switch (requestCode) {
	    	case EDIT_POI_REQUEST :
	    		/*
	    		 * Handle results returned to the FragmentActivity
	    		 * by Google Play services
	    		 * If the result code is Activity.RESULT_OK, try
	    		 * to connect again
	    		 */
	    		switch (resultCode) {
		    		case Activity.RESULT_OK :
		    	        Bundle b = data.getExtras(); // gets the previously created intent
		    	        poi = (Poi) b.get("poi_created_or_updated"); 
		    	        PoiInformationFragment info_fragment = (PoiInformationFragment) adapterViewPager.getRegisteredFragment(0);
		    	        info_fragment.setFields();
//		    	        LatLng point = new LatLng(poi.getLatitude(), poi.getLongitude());
//		    	        info_fragment.setHiddenFields(point);
//		    	        info_fragment.tvName.setText(poi.getName());
//		    	        info_fragment.tvDesc.setText(poi.getDescription());
//		    	        info_fragment.tvCategory.setText(poi.getCategoryName());
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
        	String result = "";
        	//despues de cualquiera de estos metodo vuelve al postexecute de aca
        	switch (this.from_method) {
	        	case DELETE_POI_METHOD :
	        		api_result = apiInterface.POST(urls[0], object_to_send, "delete");
	        	break;
        	}
        	
        	return api_result.getResult();             
        }
        
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {        	
        	//Log.d("InputStream", result);
        	switch (this.from_method) {        	
	        	case DELETE_POI_METHOD :
	        		progress.dismiss(); 
						if (api_result.ok())												
							finish();
						else{
							CustomTravelJoinException exception = new CustomTravelJoinException("No se ha podido borrar el punto correctamente.");
							exception.alertExceptionMessage(PoiDetailsActivity.this);
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
}
