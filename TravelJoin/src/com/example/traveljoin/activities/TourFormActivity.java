package com.example.traveljoin.activities;

import com.example.traveljoin.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class TourFormActivity extends ActionBarActivity {
	
	ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
	}
	
	public void createPoi(View button) { 
//		Boolean valid = validateFields();
//		if (valid){					
//			progress = ProgressDialog.show(this, "Cargando",
//	        	    "Por favor espere...", true);
//			Poi poi_to_create = new Poi(null, Double.parseDouble(tvLatitude.getText().toString()),
//					Double.parseDouble(tvLongitude.getText().toString()), nameField.getText().toString(),
//					descField.getText().toString(), 0, ((Category)categoryField.getSelectedItem()).getId(),
//					"", poiEvents);
//			
//	        String url = getResources().getString(R.string.api_url) + "/pois/create";
//	        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(ADD_POI_METHOD, poi_to_create); 
//	        httpAsyncTask.execute(url);        
//	        //sigue en onPostExecute, en la parte de ADD_POI_METHOD
//		}
	}
	
	//cuando se clickea el boton actualizar viene aca!
	public void updatePoi(View button) { 	
//		Boolean valid = validateFields();
//		if (valid){			
//			progress = ProgressDialog.show(this, "Cargando",
//	        	    "Por favor espere...", true);
//			poi = new Poi(poi.getId(), Double.parseDouble(tvLatitude.getText().toString()),
//					Double.parseDouble(tvLongitude.getText().toString()), nameField.getText().toString(),
//					descField.getText().toString(), 0, ((Category)categoryField.getSelectedItem()).getId(),
//					"", poiEvents);
//			poi.setPoiEventsToDelete(poiEventsToDelete);
//			
//	        String url = getResources().getString(R.string.api_url) + "/pois/update";
//	        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(UPDATE_POI_METHOD, poi); 
//	        httpAsyncTask.execute(url);
//	        //sigue en HttpAsyncTask en doInBackground en UPDATE_POI_METHOD
//		}
	}
	
	//cuando se clickea el boton cancelar viene aca!
	public void cancel(View button) { 	
		Intent output = new Intent();	    			    
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}

	
}