package com.example.traveljoin.activities;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.ExpandableAdapter;
import com.example.traveljoin.auxiliaries.Item;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.MapFilter;
import com.example.traveljoin.models.PoiEvent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.Spinner;

public class MapFilterActivity  extends ActionBarActivity{ 
	MapActivity activity;
	Button btnOK;
	Spinner spnDistances;
	private MapFilter mapFilter;
	
	private LinkedHashMap<Item, ArrayList<Item>> filtersList;
	private ExpandableListView listViewFilters;
	ExpandableAdapter adapterFilters;
	private Item categoryFatherItem;
	private final static int GET_CATEGORIES_METHOD = 1;
	
//	private LinkedHashMap<Item, ArrayList<Item>> groupsList;
//	private ExpandableListView listViewGroups;	
	private Item groupFatherItem;
	private final static int GET_GROUPS_METHOD = 2;
	
	private ProgressDialog progress;

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_filter);		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras(); // gets the previously created intent        
		mapFilter = (MapFilter) b.get("mapFilters");
		spnDistances = (Spinner) findViewById(R.id.spinnerDistances);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.distances_for_filters, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spnDistances.setAdapter(adapter);
		
		progress = ProgressDialog.show(this, "Cargando",
        	    "Por favor espere...", true);
		
		filtersList = new LinkedHashMap<Item,ArrayList<Item>>();
		listViewFilters = (ExpandableListView)findViewById(R.id.listFilters);    	  	
		initCategoriesList();
		initGroupsList();		
	 }

	 //cuando se clickea el boton OK
	public void setFilters(View button){
		Integer maxDistance = Integer.valueOf(spnDistances.getSelectedItem().toString()); 
		MapFilter mapFilterReturned = new MapFilter(mapFilter.getUserId(), mapFilter.getLatitude(), mapFilter.getLongitude(), maxDistance);				
		
		//seteamos los filtros de las categorias
		ArrayList<Integer> categoriesIds = new ArrayList<Integer>(); 		
		ArrayList<Item> categoriesItems = adapterFilters.getChild(categoryFatherItem);
		for (int i = 0; i < categoriesItems.size(); i++) {
			if (categoriesItems.get(i).isChecked){
				categoriesIds.add(categoriesItems.get(i).id);
			}
		}
		mapFilterReturned.setCategoriesIds(categoriesIds);
		
		//seteamos los filtros de los grupos
		ArrayList<Integer> groupIds = new ArrayList<Integer>(); 
		ArrayList<Item> groupItems = adapterFilters.getChild(groupFatherItem);
		for (int i = 0; i < groupItems.size(); i++) {
			if (groupItems.get(i).isChecked){
				groupIds.add(groupItems.get(i).id);
			}
		}
		mapFilterReturned.setGroupsIds(groupIds);
                
		Intent output = new Intent();	    
		output.putExtra("mapFiltersReturned", mapFilterReturned);	    
		setResult(Activity.RESULT_OK, output);
		finish();
	 }
    
    private void initCategoriesList(){    	    	    	
        String url = getResources().getString(R.string.api_url) + "/categories/index.json";
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(GET_CATEGORIES_METHOD); 
        httpAsyncTask.execute(url);   	    	
    }
    
    private void initGroupsList(){
    	//groupsList = new LinkedHashMap<Item,ArrayList<Item>>();    	
        String url = getResources().getString(R.string.api_url) + "/users/get_groups.json?user_id=" + mapFilter.getUserId();       
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(GET_GROUPS_METHOD); 
        httpAsyncTask.execute(url);   	    	
    }
    
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		private ApiInterface apiInterface = new ApiInterface();
    	private Integer from_method;
    	private ApiResult api_result;
    	
    	//contructor para setearle info extra
    	public HttpAsyncTask(Integer from_method) {
			this.from_method = from_method;  
		}
    	
        @Override
        protected String doInBackground(String... urls) {  
        	//despues de cualquiera de estos metodo vuelve al postexecute de aca
        	switch (this.from_method) {
	        	case GET_CATEGORIES_METHOD :
	        		api_result = apiInterface.GET(urls[0]);
	        	break;
	        	case GET_GROUPS_METHOD :
	        		api_result = apiInterface.GET(urls[0]);
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
			        		loadCategories(categories);			        		
						} catch (JSONException e) {							
							showExceptionError(e);
						}
	            	}
	            	else{	            		
	            		showConnectionError();
	            	}		        	
		        break;
	        	case GET_GROUPS_METHOD :
	            	if (api_result.ok()){
	            		try {
			        		JSONArray groups = new JSONArray(result);			        		 
			        		loadGroups(groups);			        		
						} catch (JSONException e) {  
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
	
	private void loadCategories(JSONArray categories) throws JSONException{
		categoryFatherItem = new Item(1, "Categorías");
		ArrayList<Item> categoriesItems = new ArrayList<Item>(); 
		for (int i = 0; i < categories.length(); i++) {
    	    JSONObject categoryJson = categories.getJSONObject(i);    	        	    
    	    Item categoryItem = new Item(categoryJson.getInt("id"), categoryJson.getString("name"));
    	    categoriesItems.add(categoryItem);    	    
    	}	
		
		filtersList.put(categoryFatherItem,categoriesItems);			
    	//adapterFilters.notifyDataSetChanged();
	}
	
	private void loadGroups(JSONArray groups) throws JSONException{
		groupFatherItem = new Item(2, "Grupos");
		ArrayList<Item> groupsItems = new ArrayList<Item>(); 
		for (int i = 0; i < groups.length(); i++) {
    	    JSONObject groupJson = groups.getJSONObject(i);    	        	    
    	    Item groupItem = new Item(groupJson.getInt("id"), groupJson.getString("name"));
    	    groupsItems.add(groupItem);    	    
    	}	
		
		filtersList.put(groupFatherItem, groupsItems);	
		//adapterFilters.notifyDataSetChanged();
		adapterFilters = new ExpandableAdapter(this, listViewFilters, filtersList);
    	listViewFilters.setAdapter(adapterFilters);
    	progress.dismiss();      	
	}
	
	public void showConnectionError(){
		CustomTravelJoinException exception = new CustomTravelJoinException();
		exception.alertConnectionProblem(this);
		//e.printStackTrace();
	}
	
	public void showExceptionError(Exception e){
		CustomTravelJoinException exception = new CustomTravelJoinException(e.getMessage());
		exception.alertExceptionMessage(this);
		e.printStackTrace();
	}
    
}

