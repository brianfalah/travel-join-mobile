package com.example.traveljoin.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.ExpandableAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.auxiliaries.CheckeableItem;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.Category;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.MapFilter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;

public class MapFilterActivity extends ActionBarActivity {
	MapActivity activity;
	Button btnOK;
	Spinner spnDistances;
	private MapFilter mapFilter;

	private LinkedHashMap<CheckeableItem, ArrayList<CheckeableItem>> filtersList;
	private ExpandableListView listViewFilters;
	ExpandableAdapter adapterFilters;
	private CheckeableItem categoryFatherItem;

	// private LinkedHashMap<Item, ArrayList<Item>> groupsList;
	// private ExpandableListView listViewGroups;
	private CheckeableItem groupFatherItem;
	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_filter);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras(); // gets the previously created
											// intent
		mapFilter = (MapFilter) b.get("mapFilters");

		spnDistances = (Spinner) findViewById(R.id.spinnerDistances);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.distances_for_filters,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDistances.setAdapter(adapter);
		// se selecciona el filtro aplicado actualmente
		int selectedPosition = adapter.getPosition(mapFilter.getMaxDistance()
				.toString());
		spnDistances.setSelection(selectedPosition);

		progress = ProgressDialog.show(this, "Cargando", "Por favor espere...",
				true);

		filtersList = new LinkedHashMap<CheckeableItem, ArrayList<CheckeableItem>>();
		listViewFilters = (ExpandableListView) findViewById(R.id.listFilters);
		initCategoriesList();
		initGroupsList();
	}

	// cuando se clickea el boton OK
	public void setFilters(View button) {
		Integer maxDistance = Integer.valueOf(spnDistances.getSelectedItem()
				.toString());
		MapFilter mapFilterReturned = new MapFilter(mapFilter.getUserId(),
				mapFilter.getLatitude(), mapFilter.getLongitude(), maxDistance);

		// seteamos los filtros de las categorias
		ArrayList<Integer> categoriesIds = new ArrayList<Integer>();
		ArrayList<CheckeableItem> categoriesItems = adapterFilters
				.getChild(categoryFatherItem);
		for (int i = 0; i < categoriesItems.size(); i++) {
			if (categoriesItems.get(i).isChecked()) {
				categoriesIds.add(categoriesItems.get(i).getId());
			}
		}
		mapFilterReturned.setCategoriesIds(categoriesIds);

		// seteamos los filtros de los grupos
		ArrayList<Integer> groupIds = new ArrayList<Integer>();
		ArrayList<CheckeableItem> groupItems = adapterFilters
				.getChild(groupFatherItem);
		for (int i = 0; i < groupItems.size(); i++) {
			if (groupItems.get(i).isChecked()) {
				groupIds.add(groupItems.get(i).getId());
			}
		}
		mapFilterReturned.setGroupsIds(groupIds);

		Intent output = new Intent();
		output.putExtra("mapFiltersReturned", mapFilterReturned);
		setResult(Activity.RESULT_OK, output);
		finish();
	}

	private void initCategoriesList() {
		ArrayList<Integer> categoriesIds = mapFilter.getCategoriesIds();
		categoryFatherItem = new CheckeableItem(new Category(1, "Categorías"));
		ArrayList<CheckeableItem> categoriesItems = new ArrayList<CheckeableItem>();
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		for (Category category : globalContext.getCategories()) {
			Boolean checked = false;
			if (!((Integer) categoriesIds.indexOf(category.getId())).equals(-1)) {
				checked = true;
			}
			CheckeableItem categoryItem = new CheckeableItem(category, checked);
			categoriesItems.add(categoryItem);
		}

		filtersList.put(categoryFatherItem, categoriesItems);
		// adapterFilters.notifyDataSetChanged();
	}

	private void initGroupsList() {
		// groupsList = new LinkedHashMap<Item,ArrayList<Item>>();
		String url = getResources().getString(R.string.api_url)
				+ "/users/get_groups.json?user_id=" + mapFilter.getUserId();
		new GetGroupsTask().execute(url);
	}

	private class GetGroupsTask extends AsyncTask<String, Void, String> {
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
			Log.d("InputStream", result);
			if (api_result.ok()) {
				try {
					JSONArray groups = new JSONArray(result);
					loadGroups(groups);
				} catch (JSONException e) {
					showExceptionError(e);
				}
			} else {
				showConnectionError();
			}
		}
	}

	private void loadGroups(JSONArray groups) throws JSONException {
		ArrayList<Integer> groupIds = mapFilter.getGroupsIds();
		groupFatherItem = new CheckeableItem(new Category(2, "Grupos"));
		ArrayList<CheckeableItem> groupsItems = new ArrayList<CheckeableItem>();
		for (int i = 0; i < groups.length(); i++) {
			JSONObject groupJson = groups.getJSONObject(i);
			// TODO: Cambiar por Group.fromJSON
			Group group = new Group(groupJson.getString("name"), null);
			group.setId(groupJson.getInt("id"));
			// si en los filtros actuales estaba seleccionada este grupo, lo
			// checkeamos
			Boolean checked = false;
			if (!((Integer) groupIds.indexOf(group.getId())).equals(-1)) {
				checked = true;
			}
			CheckeableItem groupItem = new CheckeableItem(group, checked);
			groupsItems.add(groupItem);
		}

		filtersList.put(groupFatherItem, groupsItems);
		// adapterFilters.notifyDataSetChanged();
		adapterFilters = new ExpandableAdapter(this, listViewFilters,
				filtersList);
		listViewFilters.setAdapter(adapterFilters);
		if (!mapFilter.getCategoriesIds().isEmpty()) {
			listViewFilters.expandGroup(0);
		}
		if (!mapFilter.getGroupsIds().isEmpty()) {
			listViewFilters.expandGroup(1);
		}
		progress.dismiss();
	}

	public void showConnectionError() {
		CustomTravelJoinException exception = new CustomTravelJoinException();
		exception.alertConnectionProblem(this);
		// e.printStackTrace();
	}

	public void showExceptionError(Exception e) {
		CustomTravelJoinException exception = new CustomTravelJoinException(
				e.getMessage());
		exception.alertExceptionMessage(this);
		e.printStackTrace();
	}

	// cuando se clickea el boton cancelar viene aca!
	public void cancel(View button) {
		Intent output = new Intent();
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}

}
