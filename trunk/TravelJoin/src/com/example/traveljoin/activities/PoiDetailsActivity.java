package com.example.traveljoin.activities;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.traveljoin.R;

import android.content.DialogInterface;

import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Poi;
import com.google.android.gms.maps.model.LatLng;

public class PoiDetailsActivity extends ActionBarActivity{
	
	private static final int EDIT_POI_REQUEST = 1;
	protected static final int DELETE_POI_METHOD = 2;
	
	ProgressDialog progress;
	Poi poi;
	TextView tvLatitude;
	TextView tvLongitude;
	TextView tvName;
	TextView tvDesc;
	TextView tvCategory;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);        
        // get reference to the views
        tvLatitude = (TextView) findViewById(R.id.PoiLatitude);
		tvLongitude = (TextView) findViewById(R.id.PoiLongitude);
		tvName = (TextView) findViewById(R.id.PoiName);
		tvDesc = (TextView) findViewById(R.id.PoiDescription);
		tvCategory = (TextView) findViewById(R.id.PoiCategory);
        Bundle b = getIntent().getExtras(); // gets the previously created intent
        poi = (Poi) b.get("poi"); 
        LatLng point = new LatLng(poi.getLatitude(), poi.getLongitude()); // devuelve la lat y long donde se quiere crear el POI
        setHiddenFields(point);
        tvName.setText(poi.getName());
        tvDesc.setText(poi.getDescription());
        tvCategory.setText(poi.getCategoryName());
    }    
    
    private void setHiddenFields(LatLng point) {		
		tvLatitude.setText(String.valueOf(point.latitude));
		tvLongitude.setText(String.valueOf(point.longitude));		
	}
			
    
	//cuando se clickea el boton editar viene aca!
	public void editPoi(View button) { 
		Intent intent = new Intent(this, PoiFormActivity.class);
		intent.putExtra("poi", poi); //le pasamos el punto al form
		//va al form para editarlo
		startActivityForResult(intent, EDIT_POI_REQUEST);
	}
	
	//cuando se clickea el boton borrar viene aca!
	public void deletePoi(View button) { 
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
		    	        LatLng point = new LatLng(poi.getLatitude(), poi.getLongitude()); // devuelve la lat y long donde se quiere crear el POI
		    	        setHiddenFields(point);
		    	        tvName.setText(poi.getName());
		    	        tvDesc.setText(poi.getDescription());
		    	        tvCategory.setText(poi.getCategoryName());
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
    
    public void calificate(View button){
    	final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.calificate);		
		dialog.setTitle("Calificar punto");	
		dialog.show();
    }		
}
