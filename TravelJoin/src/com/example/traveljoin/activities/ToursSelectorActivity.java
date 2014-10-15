package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.GeneralItemCheckeableListAdapter;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Tour;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class ToursSelectorActivity extends Activity implements
		OnQueryTextListener {

	private ProgressDialog progressDialog;
	private ActionBar actionBar;
	private ListView listView;
	private GeneralItemCheckeableListAdapter adapter;
	private ArrayList<GeneralItem> selectedTours;

	OnItemClickListener tourItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			adapter.toggleChecked(position);
			CheckedTextView checkedTextView = (CheckedTextView) view
					.findViewById(R.id.name);
			checkedTextView.toggle();
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_general_item_selector);

		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.tours_selector);

		selectedTours = new ArrayList<GeneralItem>();
		ArrayList<GeneralItem> alreadySelectedTours = (ArrayList<GeneralItem>) getIntent()
				.getExtras().get("alreadySelectedTours");

		listView = (ListView) findViewById(R.id.list);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setTextFilterEnabled(true);
		getToursFromServer(alreadySelectedTours);
		registerForContextMenu(listView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.general_item_selector_activity_actions, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search:
			// Already handled in search listener
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onQueryTextChange(String insertedText) {
		adapter.getFilter().filter(insertedText);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}

	private void getToursFromServer(ArrayList<GeneralItem> alreadySelectedTours) {
		progressDialog = ProgressDialog.show(this, getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/tours/indexAll.json";
		GetToursTask getToursTask = new GetToursTask(alreadySelectedTours);
		getToursTask.execute(url);
	}

	public void onCancelButtonClicked(View button) {
		Intent output = new Intent();
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}

	public void onAcceptButtonClicked(View button) {
		// TODO: Agregar validacion de que si no se agrego ningun item, al menos
		// se deba seleccionar uno

		Intent output = new Intent();
		output.putExtra("newSelectedTours", adapter.getSelectedItems());

		setResult(Activity.RESULT_OK, output);
		finish();
	}

	private class GetToursTask extends AsyncTask<String, Void, String> {
		private ApiInterface apiInterface = new ApiInterface();
		private ApiResult api_result;
		private ArrayList<GeneralItem> alreadySelectedTours;

		public GetToursTask(ArrayList<GeneralItem> alreadySelectedTours) {
			this.alreadySelectedTours = alreadySelectedTours;
		}

		@Override
		protected String doInBackground(String... urls) {
			api_result = apiInterface.GET(urls[0]);
			return api_result.getResult();
		}

		@Override
		protected void onPostExecute(String result) {
			if (api_result.ok()) {
				try {
					JSONArray toursJson = new JSONArray(result);
					for (int i = 0; i < toursJson.length(); i++) {
						JSONObject tourJson = toursJson.getJSONObject(i);
						Tour tour = Tour.fromJSON(tourJson);
						selectedTours.add(tour);
					}
					adapter = new GeneralItemCheckeableListAdapter(
							new GeneralItemListAdapter(
									ToursSelectorActivity.this, selectedTours),
									alreadySelectedTours);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(tourItemClickListener);
					adapter.notifyDataSetChanged();
					progressDialog.dismiss();

				} catch (JSONException e) {
					// TODO: Handlear
					progressDialog.dismiss();
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO: Handlear
					e.printStackTrace();
				}
			} else {
				// showConnectionError();
				// TODO si no se pudieron obtener las categorias mostrar cartel
				// para reintentar
			}
		}
	}

}
