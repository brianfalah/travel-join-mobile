package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.fragments.MainMapFragment;
import com.example.traveljoin.fragments.MainMenuFragment;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.MapFilter;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MapActivity extends SlidingFragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener,
		OnMapLongClickListener, OnCameraChangeListener, OnMarkerClickListener {
	
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int CREATE_POI_REQUEST = 1;
    private static final int CHANGE_FILTERS_REQUEST = 2;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    
 // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;	
   
    ConnectionResult connectionResult;
    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;
    
	protected ListFragment mFrag;
	private static MainMapFragment mapFragment;
	private static GoogleMap mMap;
	public static HashMap<Marker, Poi> markerPoiMap;
	MapFilter mapFilters; 
	Marker lastOpened = null;
	private LocationClient mLocationClient;
    boolean mUpdatesRequested;
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    //para mostrar el gif de "Loading"
    ProgressDialog progress; 
    SlidingMenu menu;
    User user;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                	        
        setContentView(R.layout.activity_map);
        setBehindContentView(R.layout.menu_frame);
        //get current user
        GlobalContext globalContext = (GlobalContext) getApplicationContext();
		user = globalContext.getUser();
		mapFilters = new MapFilter(user.getId(), null, null, 1);
        
        if (savedInstanceState == null) {
	        android.support.v4.app.FragmentManager fragment_manager = getSupportFragmentManager();
	        markerPoiMap = new HashMap<Marker, Poi>();
	                
	    	//Barra LATERAL		    
	    	fragment_manager.beginTransaction()
			.replace(R.id.menu_frame, new MainMenuFragment())
			.commit();   		 	       
	        
	        // configure the SlidingMenu
	        menu = getSlidingMenu();        
	        menu.setMode(SlidingMenu.LEFT);
	        // Allows the SlidingMenu to be opened with a swipe gesture anywhere on the screen
	        //menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	        // Allows the SlidingMenu to be opened with a swipe gesture on the screen's margin
	        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	        //menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	        menu.setShadowWidthRes(R.dimen.shadow_width);
	        menu.setShadowDrawable(R.drawable.shadow);
	        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	        menu.setFadeDegree(0.35f);
	        setSlidingActionBarEnabled(true);   
	        
	        //FIN BARRA LATERAL
		    
	        if (servicesConnected()){
	        	// Create the LocationRequest object
		        mLocationRequest = LocationRequest.create();
		        // Use high accuracy
		        mLocationRequest.setPriority(
		                LocationRequest.PRIORITY_HIGH_ACCURACY);
		        // Set the update interval to 5 seconds
		        mLocationRequest.setInterval(UPDATE_INTERVAL);
		        // Set the fastest update interval to 1 second
		        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);		        		      
		                            	
	        	fragment_manager.beginTransaction()
	            .add(R.id.map_container, new MapFragmentView())
	            .commit();
	        }	        	           
        }
        
        //Enable home button
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:			
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Para cerrar el menu cuando se toca el boton atras
    @Override
	public void onBackPressed() {
	    if (menu.isMenuShowing()) {
	        menu.showContent(true);
	        return;
	    }
	
	    super.onBackPressed();
	}
    
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            toggle();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    

    public static class MapFragmentView extends Fragment {        

		public MapFragmentView() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_map, container, false);
            MapActivity map_activity  = (MapActivity)getActivity();
            android.support.v4.app.FragmentManager fragment_manager = map_activity.getSupportFragmentManager();
                        
            mapFragment = (MainMapFragment) fragment_manager.findFragmentById(R.id.map);                              
            if (mapFragment != null) {
                // Setup your map...
            	mMap = mapFragment.getMap();
            	mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            	//activamos el puntito azul en el mapa(el que te lleva a tu ubicaion cuando le haces click)
            	mMap.setMyLocationEnabled(true);
     
                // Setting OnClickEvent listener for the GoogleMap
            	mMap.setOnMapLongClickListener(map_activity);
            	
            	//para que cuando se hace long click sobre un marker no pase nada 
            	mMap.setOnMarkerDragListener(new OnMarkerDragListener() {

                    @Override
                    public void onMarkerDragStart(Marker marker) {
                    	Poi poi = markerPoiMap.get(marker);                    
                    	marker.setPosition(new LatLng(poi.getLatitude(), poi.getLongitude()));
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                    	Poi poi = markerPoiMap.get(marker);                    
                    	marker.setPosition(new LatLng(poi.getLatitude(), poi.getLongitude()));
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                    	Poi poi = markerPoiMap.get(marker);                    
                    	marker.setPosition(new LatLng(poi.getLatitude(), poi.getLongitude()));
                    }
                });                
            	
            	// Since we are consuming the event this is necessary to
            	// manage closing opened markers before opening new ones            	

            	mMap.setOnMarkerClickListener(map_activity);
            	
            	mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
                    
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                		//va al detalle del punto para que decida si quiere editarlo, borrarlo, etc		
                		Intent intent = new Intent(getActivity(), PoiDetailsActivity.class);
                		Poi poi = markerPoiMap.get(marker);
                		intent.putExtra("poi_id", poi.getId()); //le pasamos el punto a la activity
                		startActivity(intent);	
                    }
                });
            	
            	mMap.setOnCameraChangeListener(map_activity);
            }            
     
            return rootView;
        }     
        
    }
    
    @Override
    public boolean onMarkerClick(Marker marker) {
        // Check if there is an open info window
        if (lastOpened != null) {
            // Close the info window
            lastOpened.hideInfoWindow();

            // Is the marker the same marker that was already open
            if (lastOpened.equals(marker)) {
                // Nullify the lastOpened object
                lastOpened = null;
                // Return so that the info window isn't opened again
                return true;
            } 
        }

        // Open the info window for the marker
        marker.showInfoWindow();
        // Re-assign the last opened such that we can close it later
        lastOpened = marker;

        // Event was handled by our code do not launch default behaviour.
        return true;
    }
    
    @Override
    public void onCameraChange(CameraPosition position) {
    	LatLng target = position.target;
    	mapFilters.setLatitude(String.valueOf(target.latitude));
        mapFilters.setLongitude(String.valueOf(target.longitude));
    	getPois();
    }
    
	@Override
	public void onMapLongClick(LatLng point) {
		//va a un form y le pasamos el punto, vuelve y va a onActivityResult CREATE_POI_REQUEST		
		Intent intent = new Intent(this, PoiFormActivity.class);		
		intent.putExtra("point", point); //le pasamos el punto al form
		//va al form y espera un result_code(para saber si se creo o no)
		startActivityForResult(intent, CREATE_POI_REQUEST);		
	}
	
    /*Cuando vuelve de un activity empezado con un startActivityForResult viene aca*/
    @Override
    protected void onActivityResult(
    		int requestCode, int resultCode, Intent data) {
    	// Decide what to do based on the original request code
    	switch (requestCode) {
	    	case CONNECTION_FAILURE_RESOLUTION_REQUEST :
	    		/*
	    		 * Handle results returned to the FragmentActivity
	    		 * by Google Play services
	    		 * If the result code is Activity.RESULT_OK, try
	    		 * to connect again
	    		 */
	    		switch (resultCode) {
		    		case Activity.RESULT_OK :
		    			/*
		    			 * Try the request again
		    			 */   
		    			mLocationClient.connect();
		    		break;
	    		}
	    	break;
	    	//PARA CUANDO SE VUELVE DE CREAR UN POI
	    	case CREATE_POI_REQUEST :
	    		/*
	    		 * If the result code is Activity.RESULT_OK, agregar punto al mapa
	    		 */
	    		switch (resultCode) {
		    		case Activity.RESULT_OK :
		    			//Bundle bundle = data.getExtras();
		    			//Poi poi = (Poi) data.getSerializableExtra("poi_created_or_updated");
		    			//addMarker(poi);		    			
		    		break;
	    		}
	    	break;
	    	//PARA CUANDO SE VUELVE DE CAMBIAR LOS FILTROS
	    	case CHANGE_FILTERS_REQUEST :
	    		/*
	    		 * If the result code is Activity.RESULT_OK, agregar punto al mapa
	    		 */
	    		switch (resultCode) {
		    		case Activity.RESULT_OK :
		    			Bundle b = data.getExtras();		    			
		    			MapFilter mapFiltersReturned = (MapFilter) b.get("mapFiltersReturned");
		    			mapFilters = mapFiltersReturned;
		    			getPois();
		    		break;
	    		}
	    	break;	    		    		    	
	    }

    }
    
    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
    	// Display the connection status
        Toast.makeText(this, R.string.connected, Toast.LENGTH_SHORT).show();
        // If already requested, start periodic updates
        if (mUpdatesRequested) {
            mLocationClient.requestLocationUpdates(mLocationRequest, (com.google.android.gms.location.LocationListener) this);            
        }
        
        Location location = mLocationClient.getLastLocation();            
        onLocationChanged(location);
        mapFilters.setLatitude(String.valueOf(location.getLatitude()));
        mapFilters.setLongitude(String.valueOf(location.getLongitude()));
        getPois();        		
    }        
    
    private void getPois() {    	
        String url = getResources().getString(R.string.api_url) + "/pois/index.json?" + mapFilters.getUrlParams();
        HttpAsyncTask task = new HttpAsyncTask();
        task.execute(url);
	}


	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
    	private ApiInterface apiInterface = new ApiInterface();
    	private ApiResult api_result;
    	
        @Override
        protected String doInBackground(String... urls) {
        	api_result = apiInterface.GET(urls[0]);
        	return api_result.getResult();             
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	//Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        	Log.d("InputStream", result);
        	if (api_result.ok()){
        		try {
            		JSONArray pois = new JSONArray(result);
            		addMarkers(pois);
            		progress.dismiss();  
    			} catch (JSONException e) {
    				progress.dismiss();  
    				e.printStackTrace();
    			} catch (ParseException e) {
    				progress.dismiss();  
    				e.printStackTrace();
				}
        	}
        	else{
        		showConnectionError();
        		//TODO si no se pudieron obtener las categorias mostrar cartel para reintentar
        	}        	        	        	        	         	
       }
    }
    
    public void addMarkers(JSONArray pois) throws JSONException, ParseException{
    	mapFragment.clearMarkers();
    	markerPoiMap.clear();
    	for (int i = 0; i < pois.length(); i++) {
    	    JSONObject poiJson = pois.getJSONObject(i);    	        
    	    Poi poi = Poi.fromJSON(poiJson);
    	    addMarker(poi);    	    
    	}
    }
    
    public void addMarker(Poi poi){
    	Marker marker = mapFragment.placeMarker(poi);	    	    
	    markerPoiMap.put(marker, poi);
    }
    
    //Mueve la camara del mapa a una posicion 
    @Override
    public void onLocationChanged(Location location) {
        // Getting latitude of the current location
        double latitude = location.getLatitude();
 
        // Getting longitude of the current location
        double longitude = location.getLongitude();
 
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        
     // Report to the UI that the location was updated
//        String msg = "Updated Location: " +
//                Double.toString(latitude) + "," +
//                Double.toString(longitude);
//        System.out.println(msg);
 
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);        
    }              
  
    
    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
    	super.onStart();    	
    	progress = ProgressDialog.show(this, getString(R.string.loading),
    			getString(R.string.wait), true);
    	
    	if(mPrefs== null){    		    
	        // Open the shared preferences
	        mPrefs = getSharedPreferences("SharedPreferences",
	                Context.MODE_PRIVATE);
	        // Get a SharedPreferences editor
	        mEditor = mPrefs.edit();		        
	        // Start with updates turned off
	        mUpdatesRequested = false;
    	}
    	
    	if(mLocationClient == null){
    		/*
	         * Create a new location client, using the enclosing class to
	         * handle callbacks.
	         */
	        mLocationClient = new LocationClient(this, this, this);
    	}    	
    	
        if(mLocationClient.isConnected())
        	Log.d("DEBUG", "onStart in MapActiviy - LocationClient Is connected");
        else{
        	mLocationClient.connect();
        	Log.d("DEBUG", "onStart in MapActiviy - called LocationClient connect method");
        }
        
        if (menu == null){
        	menu = getSlidingMenu();
        }
        
        if (markerPoiMap == null){
        	markerPoiMap = new HashMap<Marker, Poi>();
        }
    }
    
    /* Request updates at startup */
    @Override
    protected void onResume() {      
		super.onResume();
		/*
		 * Get any previous setting for location updates
		 * Gets "false" if an error occurs
		 */
		if (mPrefs.contains("KEY_UPDATES_ON")) {
			mUpdatesRequested = mPrefs.getBoolean("KEY_UPDATES_ON", false);
		  // Otherwise, turn off location updates
		} else {
			mEditor.putBoolean("KEY_UPDATES_ON", false);
			mEditor.commit();
		}    
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
    	super.onPause();
    	//borrar la referencia a el cartelito del dialogo, sino trae problemas
    	if(progress != null)
    		progress.dismiss();
		// Save the current setting for updates
		mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
		mEditor.commit();		
    }
    
    /*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
    @Override
    protected void onStop() {
    	super.onStop();
        // If the client is connected
        if (mLocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
        	mLocationClient.removeLocationUpdates(this);
        }
        /*
         * After disconnect() is called, the client is
         * considered "dead".
         */
        mLocationClient.disconnect();        
    }
    
	public void showConnectionError(){
		CustomTravelJoinException exception = new CustomTravelJoinException();
		exception.alertConnectionProblem(this);
		//e.printStackTrace();
	}
	
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Get the error code
            int errorCode = connectionResult.getErrorCode();
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(
                        getSupportFragmentManager(),
                        "Location Updates");
            }
            return false;
        }
    }
    
    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, R.string.disconnected,
                Toast.LENGTH_SHORT).show();
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }
    }
    
    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    } 
    
    public void filter(View button){    	
		Intent intent = new Intent(this, MapFilterActivity.class);		
		intent.putExtra("mapFilters", mapFilters); //le pasamos los filtros actuales
		//va al form y espera un result_code(para saber si se creo o no)
		startActivityForResult(intent, CHANGE_FILTERS_REQUEST);		
    }
 
    
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
    
}
