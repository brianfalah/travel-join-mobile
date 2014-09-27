package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.Category;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.PoiEvent;
import com.example.traveljoin.models.User;
import com.google.android.gms.maps.model.LatLng;

public class PoiFormActivity extends ActionBarActivity{
	User user;
	ProgressDialog progress;
	TextView tvLatitude;
	TextView tvLongitude;
	EditText nameField;
	EditText descField;
	EditText addressField;
	Spinner categoryField;
	Button createButton;
	Button updateButton;
	Poi poi;
	//array de eventos del poi
	ArrayList<PoiEvent> poiEvents = new ArrayList<PoiEvent>();
	ArrayAdapter<PoiEvent> poiEventsAdapter;
	ListView lvEvents;	
	ArrayList<PoiEvent> poiEventsToDelete = new ArrayList<PoiEvent>();
	
	private final static int GET_CATEGORIES_METHOD = 1;
	private static final int ADD_POI_METHOD = 2;
	private static final int UPDATE_POI_METHOD = 3;
	private static final int ADD_EVENT_REQUEST = 2;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get current user
        GlobalContext globalContext = (GlobalContext) getApplicationContext();
		user = globalContext.getUser();
        // get reference to the views
        tvLatitude = (TextView) findViewById(R.id.PoiLatitude);
		tvLongitude = (TextView) findViewById(R.id.PoiLongitude);
		nameField = (EditText) findViewById(R.id.PoiName);
		addressField = (EditText) findViewById(R.id.PoiAddress);
		descField = (EditText) findViewById(R.id.PoiDescription);
		categoryField = (Spinner) findViewById(R.id.PoiCategories);
		createButton = (Button) findViewById(R.id.PoiCreateButton);
		updateButton = (Button) findViewById(R.id.PoiUpdateButton);
		lvEvents = (ListView) findViewById(R.id.lvEvents);
		lvEvents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
        Bundle b = getIntent().getExtras(); // gets the previously created intent
        
        //si viene un poi significa que estamos en el form para editarlo, rellenar todos los campos
        //Y MOSTRAR BOTON DE UPDATE
        poi = (Poi) b.get("poi");
        LatLng point = null;
        if (poi != null){
        	point = new LatLng(poi.getLatitude(), poi.getLongitude());
        	setHiddenFields(point);
        	nameField.setText(poi.getName());
        	descField.setText(poi.getDescription());
        	addressField.setText(poi.getAddress());
        	updateButton.setVisibility(View.VISIBLE);
        	poiEvents = poi.getPoiEvents();
        }
        //si no viene un poi, estamos creando uno nuevo, se rellenan solo la lat y long(que vienen del punto presionado en el mapa)
        //Y MOSTRAR BOTON DE CREATE
        else{
            point = (LatLng) b.get("point"); // devuelve la lat y long donde se quiere crear el POI
            setHiddenFields(point);
            createButton.setVisibility(View.VISIBLE);
        }
        
        poiEventsAdapter = new ArrayAdapter<PoiEvent>(this,
							android.R.layout.simple_list_item_multiple_choice, poiEvents);
        lvEvents.setAdapter(poiEventsAdapter);

        //pedimos a la API todas las categorias para rellenar el Spinner
        progress = ProgressDialog.show(this, "Cargando",
        	    "Por favor espere...", true);
        String url = getResources().getString(R.string.api_url) + "/categories/index.json";
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(GET_CATEGORIES_METHOD, null); 
        httpAsyncTask.execute(url);        
    }

	private void setHiddenFields(LatLng point) {		
		tvLatitude.setText(String.valueOf(point.latitude));
		tvLongitude.setText(String.valueOf(point.longitude));		
	}
	
	
	
	private void addCategoriesToSpinner(JSONArray categories) throws JSONException{
		List<Category> categories_objects = new ArrayList<Category>(); 
		for (int i = 0; i < categories.length(); i++) {
    	    JSONObject categoryJson = categories.getJSONObject(i);    	        	    
    	    Category category = new Category(categoryJson.getInt("id"), categoryJson.getString("name"));
    	    categories_objects.add(category);    	    
    	}	

		ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories_objects);
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner categoriesSpinner = (Spinner) findViewById(R.id.PoiCategories);		
		categoriesSpinner.setAdapter(categoryAdapter);
		//si estamos editando, seleccionar la categoria del poi
		if (poi != null){
			categoriesSpinner.setSelection(getIndex(categoriesSpinner, poi.getCategoryId()));
		}
		
	}
	
	private int getIndex(Spinner spinner, Integer category_id){
		int index = 0;

		for (int i=0;i<spinner.getCount();i++){
			if ( ((Category) spinner.getItemAtPosition(i)).getId().equals(category_id) ){
				index = i;
				i=spinner.getCount();//will stop the loop, kind of break, by making condition false
			}
		}
		return index;
	} 
    
	//cuando se clickea el boton crear viene aca!
	public void createPoi(View button) { 
		Boolean valid = validateFields();
		if (valid){					
			progress = ProgressDialog.show(this, "Cargando",
	        	    "Por favor espere...", true);
			Poi poi_to_create = new Poi(null, Double.parseDouble(tvLatitude.getText().toString()),
					Double.parseDouble(tvLongitude.getText().toString()), nameField.getText().toString(),
					descField.getText().toString(), addressField.getText().toString(),
					user.getId(), ((Category)categoryField.getSelectedItem()).getId(),
					"", poiEvents);
			
	        String url = getResources().getString(R.string.api_url) + "/pois/create";
	        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(ADD_POI_METHOD, poi_to_create); 
	        httpAsyncTask.execute(url);        
	        //sigue en onPostExecute, en la parte de ADD_POI_METHOD
		}
	}
	
	//cuando se clickea el boton actualizar viene aca!
	public void updatePoi(View button) { 	
		Boolean valid = validateFields();
		if (valid){			
			progress = ProgressDialog.show(this, "Cargando",
	        	    "Por favor espere...", true);
			poi = new Poi(poi.getId(), Double.parseDouble(tvLatitude.getText().toString()),
					Double.parseDouble(tvLongitude.getText().toString()), nameField.getText().toString(),
					descField.getText().toString(), addressField.getText().toString(), user.getId(),
					((Category)categoryField.getSelectedItem()).getId(), "", poiEvents);
			poi.setPoiEventsToDelete(poiEventsToDelete);
			
	        String url = getResources().getString(R.string.api_url) + "/pois/update";
	        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(UPDATE_POI_METHOD, poi); 
	        httpAsyncTask.execute(url);
	        //sigue en HttpAsyncTask en doInBackground en UPDATE_POI_METHOD
		}
	}
	
	//cuando se clickea el boton cancelar viene aca!
	public void cancel(View button) { 	
		Intent output = new Intent();	    			    
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}

	private Boolean validateFields() {		
		return validateField(nameField) && validateField(addressField) && validateField(descField) && validateField(categoryField);
	}

	private Boolean validateField(View field) {
		Boolean valid = null;
		if (field instanceof EditText) {
			EditText edit_text_field = (EditText) field;
			if (TextUtils.isEmpty( edit_text_field.getText().toString() ) ){
				edit_text_field.setError(edit_text_field.getHint() + " es requerido!");
				valid = false;
			} else {
				edit_text_field.setError(null);
				valid = true;
			}
		} else {
			if (field instanceof Spinner) {
				Spinner spinner_field = (Spinner) field;
				if (TextUtils.isEmpty( ((Category) spinner_field.getSelectedItem()).getName() ) ){
					Toast.makeText(this, spinner_field.getPrompt() + " es requerido!",
			                Toast.LENGTH_SHORT).show();
					valid = false;
				}
				else{
					valid = true;
				}
			}
		}
		return valid;
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
	        	case GET_CATEGORIES_METHOD :
	        		api_result = apiInterface.GET(urls[0]);
	        	break;	
	        	case ADD_POI_METHOD :
	        		api_result = apiInterface.POST(urls[0], object_to_send, "create");
	        	break;
	        	case UPDATE_POI_METHOD :
	        		api_result = apiInterface.POST(urls[0], object_to_send, "update");
	        	break;
        	}
        	
        	return api_result.getResult();             
        }
        
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {        	
        	Log.d("InputStream", result);
        	switch (this.from_method) {        	
	        	case GET_CATEGORIES_METHOD :
	            	if (api_result.ok()){
	            		try {
			        		JSONArray categories = new JSONArray(result);
			        		addCategoriesToSpinner(categories);
			        		progress.dismiss();  
						} catch (JSONException e) {
							progress.dismiss();  
							showExceptionError(e);
						}
	            	}
	            	else{
	            		progress.dismiss();
	            		showConnectionError();
	            	}		        	
		        break;	
	        	case ADD_POI_METHOD :
	        		progress.dismiss(); 
	        	    //para volver al mapa
					JSONObject poiJson;
					if (api_result.ok()){
						try {
							poiJson = new JSONObject(result);
							Poi poi_created = Poi.fromJSON(poiJson);
			        	    closeActivity(poi_created);
						} catch (JSONException e) {
							showExceptionError(e);						
						} catch (ParseException e) {
							showExceptionError(e);
						}
					}
					else {
						showConnectionError();
					}
					    	        	        	    
		        break;	
	        	case UPDATE_POI_METHOD :
	        		progress.dismiss(); 
	        	    //para volver al mapa
					JSONObject poiUpdatedJson;
					if (api_result.ok()){
						try {
							poiUpdatedJson = new JSONObject(result);
							Poi poi_updated = Poi.fromJSON(poiUpdatedJson);
			        	    closeActivity(poi_updated);
						} catch (JSONException e) {
							showExceptionError(e);
						} catch (ParseException e) {
							showExceptionError(e);
						}    	  
					}
					else{
						showConnectionError();
					}					      	        	   
		        break;	
        	}
       }
    }
	
	public void showConnectionError(){
		CustomTravelJoinException exception = new CustomTravelJoinException();
		exception.alertConnectionProblem(getApplicationContext());
		//e.printStackTrace();
	}
	
	public void showExceptionError(Exception e){
		CustomTravelJoinException exception = new CustomTravelJoinException(e.getMessage());
		exception.alertExceptionMessage(getApplicationContext());
		e.printStackTrace();
	}
	
	public void closeActivity(Poi poi_created_or_updated){
		Intent output = new Intent();	    
		poi_created_or_updated.setPoiEvents(poiEvents);
		output.putExtra("poi_created_or_updated", poi_created_or_updated);	    
		setResult(Activity.RESULT_OK, output);
		finish();
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
    	//borrar la referencia a el cartelito del dialogo, sino trae problemas
    	if(progress != null)
    		progress.dismiss();	
    }
    
    //EVENTOS!!!
    public void addEvent(View button){
    	Intent intent = new Intent(this, EventFormActivity.class);	
    	if (poi != null){
    		intent.putExtra("poi_id", poi.getId());    		
    	}
		//va al form para crear un evento y espera un result_code(para saber si se creo o no)
    	//y el evento creado
		startActivityForResult(intent, ADD_EVENT_REQUEST);
    }
    
    public void deleteEvent(View button){
    		SparseBooleanArray checked = lvEvents.getCheckedItemPositions();
    		ArrayList<PoiEvent> eventsToDelete = new ArrayList<PoiEvent>();
    		for (int i = 0; i < checked.size(); i++) {
                // Item position in adapter
                int position = checked.keyAt(i);
                // borrar evento si esta checkeado
                if (checked.valueAt(i)){
                	PoiEvent poiEventToDelete = (PoiEvent) lvEvents.getItemAtPosition(position);
                	poiEventToDelete.markAsDeleted();
                	eventsToDelete.add(poiEventToDelete);          
    			}                
            }    
    		poiEvents.removeAll(eventsToDelete);    		
            poiEventsAdapter.notifyDataSetChanged();
            poiEventsToDelete.addAll(eventsToDelete);
    	}
    
    /*Cuando vuelve de un activity empezado con un startActivityForResult viene aca*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// Decide what to do based on the original request code
    	switch (requestCode) {
	    	//PARA CUANDO SE VUELVE DE CREAR UN EVENTO
	    	case ADD_EVENT_REQUEST :
	    		/*
	    		 * If the result code is Activity.RESULT_OK, agregar punto al mapa
	    		 */
	    		switch (resultCode) {
		    		case Activity.RESULT_OK :		    			
		    			Bundle b = data.getExtras();		    			
		    			PoiEvent poiEvent = (PoiEvent) b.get("poiEvent");		    			
		    			poiEvents.add(poiEvent);		    					    		
		    			poiEventsAdapter.notifyDataSetChanged();
		    		break;
		    		case Activity.RESULT_CANCELED :
		    			
		    		break;
	    		}
	    	break;
	    	
	    }

    }
}
