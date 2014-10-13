package com.example.traveljoin.activities;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.PoiEvent;
import com.example.traveljoin.models.Tour;
import com.example.traveljoin.models.TourPoi;
import com.example.traveljoin.models.User;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class TourFormActivityOld extends ActionBarActivity {	
	User user;
	ProgressDialog progress;
	EditText nameField;
	EditText descField;
	Button createButton;
	Button updateButton;
	Tour tour;
	ArrayList<TourPoi> tourPois; // = new ArrayList<TourPoi>();
	ArrayAdapter<TourPoi> tourPoisAdapter;
	ListView lvTourPois;	
	ArrayList<TourPoi> tourPoisToDelete; // = new ArrayList<TourPoi>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_tour_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
		tourPois = new ArrayList<TourPoi>();
		tourPoisToDelete = new ArrayList<TourPoi>();
		initializeViewReferences();
		initializeUser();
		
		tour = (Tour) getIntent().getExtras().get("tour");
		if (tour != null)
			initializeViewForEditingMode();
		else
			initializeViewForCreatingMode();
		
		tourPoisAdapter = new ArrayAdapter<TourPoi>(this,
				android.R.layout.simple_list_item_multiple_choice, tourPois);
		lvTourPois.setAdapter(tourPoisAdapter);
	}
	
	private void initializeUser() {
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		user = globalContext.getUser();
	}
	
	private void initializeViewForCreatingMode() {
		createButton.setVisibility(View.VISIBLE);
	}
	
	private void initializeViewForEditingMode() {
		nameField.setText(tour.getName());
		descField.setText(tour.getDescription());
		updateButton.setVisibility(View.VISIBLE);
		//tourPois = tour.getTourPois();
	}
	
	private void initializeViewReferences() {
		nameField = (EditText) findViewById(R.id.TourName);
		descField = (EditText) findViewById(R.id.TourDescription);
		//createButton = (Button) findViewById(R.id.TourCreateButton);
		//updateButton = (Button) findViewById(R.id.TourUpdateButton);
		//lvTourPois = (ListView) findViewById(R.id.lvTourPois);
		lvTourPois.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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