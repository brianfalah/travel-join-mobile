package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Poi;

public class PoisSelectorActivity extends Activity implements OnQueryTextListener {

	private ProgressDialog progress;
	private ActionBar actionBar;
	private ListView listView;
	private GeneralItemListAdapter adapter;
	private ArrayList<GeneralItem> pois;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pois_selector);

		actionBar = getActionBar();
//		actionBar.setSubtitle(R.string.pois);
		actionBar.setSubtitle("Cambiar este subtituto");
		
		pois = new ArrayList<GeneralItem>();
		getPoisFromServer();
		adapter = new GeneralItemListAdapter(this, pois);

		listView = (ListView) findViewById(R.id.poiList);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);
		registerForContextMenu(listView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.poi_activity_actions, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.poi_search)
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
		case R.id.poi_search:
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

	private void getPoisFromServer() {
		progress = ProgressDialog.show(this, getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/pois/indexAll.json";
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
			Log.d("InputStream", result);
			if (api_result.ok()) {
				try {
					JSONArray poisJson = new JSONArray(result);
					for (int i = 0; i < poisJson.length(); i++) {
						JSONObject poiJson = poisJson.getJSONObject(i);
						Poi poi = Poi.fromJSON(poiJson);
						pois.add(poi);
					}
					adapter.notifyDataSetChanged();
					progress.dismiss();

				} catch (JSONException e) {
					// TODO: Handlear
					progress.dismiss();
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