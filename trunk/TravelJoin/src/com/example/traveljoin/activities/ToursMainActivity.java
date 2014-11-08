package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Tour;
import com.example.traveljoin.models.User;

public class ToursMainActivity extends Activity implements OnQueryTextListener {

	private ProgressDialog progress;
	User user;
	private ActionBar actionBar;
	private ListView listView;
	private GeneralItemListAdapter adapter;
	private ArrayList<GeneralItem> tours;
	private static final int CREATE_TOUR_REQUEST = 1;
	private static final int EDIT_TOUR_REQUEST = 2;
	// para el asynctask
	protected static final int GET_TOURS_METHOD = 1;
	protected static final int DELETE_TOUR_METHOD = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		initializeUser();
		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.tours);

		tours = new ArrayList<GeneralItem>();
		adapter = new GeneralItemListAdapter(this, tours);

		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);
		registerForContextMenu(listView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getToursFromServer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tours_activity_actions, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.tour_search)
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
		case R.id.tour_search:
			// Already handled in search listener
			return true;
		case R.id.tour_add:
			Intent intent = new Intent(this, TourFormActivity.class);
			startActivityForResult(intent, CREATE_TOUR_REQUEST);
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.tour_list_item_context_menu, menu);

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		Tour tour = (Tour) listView.getItemAtPosition(info.position);
		if (!tour.getUserId().equals(user.getId())) {
			menu.removeItem(R.id.tour_context_menu_edit);
			menu.removeItem(R.id.tour_context_menu_delete);
		}
	}

	OnItemClickListener tourItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			startTourDetailActivity((Tour) adapter.getItem(position));
		}
	};

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Tour selectedTour = getTourItem(item);
		switch (item.getItemId()) {
		case R.id.tour_context_menu_view:
			startTourDetailActivity(selectedTour);
			return true;
		case R.id.tour_context_menu_edit:
			Intent intent_edit = new Intent(this, TourFormActivity.class);
			intent_edit.putExtra("tour", selectedTour);
			startActivityForResult(intent_edit, EDIT_TOUR_REQUEST);
			return true;
		case R.id.tour_context_menu_delete:
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					ToursMainActivity.this);
			dialog.setTitle(getString(R.string.delete_tour))
					.setMessage(getString(R.string.delete_tour_message))
					.setPositiveButton(getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									progress = ProgressDialog.show(
											ToursMainActivity.this,
											getString(R.string.loading),
											getString(R.string.wait), true);
									String url = getResources().getString(
											R.string.api_url)
											+ "/tours/destroy";
									HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
											DELETE_TOUR_METHOD, selectedTour);
									httpAsyncTask.execute(url);
									// sigue en HttpAsyncTask en doInBackground
									// en DELETE_POI_METHOD

								}
							})
					.setNegativeButton(getString(R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void startTourDetailActivity(final Tour selectedTour) {
		Intent intent = new Intent(this, TourDetailsActivity.class);
		intent.putExtra("tour_id", selectedTour.getId()); // le pasamos el punto
															// a la
		// activity
		startActivity(intent);
	}

	private Tour getTourItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Tour tour = (Tour) tours.get(info.position);
		return tour;
	}

	/*
	 * Cuando vuelve de un activity empezado con un startActivityForResult viene
	 * aca
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		case CREATE_TOUR_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				// Se actualiza la lista de tours en el onResume
				break;
			}
			break;
		case EDIT_TOUR_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				// Se actualiza la lista de tours en el onResume
				break;
			}
			break;
		}

	}

	private void getToursFromServer() {
		progress = ProgressDialog.show(this, getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/tours/indexAll.json?user_id=" + user.getId();
		HttpAsyncTask task = new HttpAsyncTask(GET_TOURS_METHOD, null);
		task.execute(url);
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		private ApiInterface apiInterface = new ApiInterface();
		private Integer from_method;
		private Object object_to_send;
		private ApiResult api_result;

		// contructor para setearle info extra
		public HttpAsyncTask(Integer from_method, Object object_to_send) {
			this.from_method = from_method;
			this.object_to_send = object_to_send;
		}

		@Override
		protected String doInBackground(String... urls) {
			// despues de cualquiera de estos metodo vuelve al postexecute de
			// aca
			switch (this.from_method) {
			case GET_TOURS_METHOD:
				api_result = apiInterface.GET(urls[0]);
				break;
			case DELETE_TOUR_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send,
						"delete");
				break;
			}

			return api_result.getResult();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Log.d("InputStream", result);
			switch (this.from_method) {
			case GET_TOURS_METHOD:
				if (api_result.ok()) {
					try {
						tours.clear();
						JSONArray toursJson = new JSONArray(result);
						for (int i = 0; i < toursJson.length(); i++) {
							JSONObject tourJson = toursJson.getJSONObject(i);
							Tour tour = Tour.fromJSON(tourJson);
							tours.add(tour);
						}
						listView.setOnItemClickListener(tourItemClickListener);
						adapter.notifyDataSetChanged();
						progress.dismiss();

					} catch (JSONException e) {
						progress.dismiss();
						showExceptionError(e);
					} catch (ParseException e) {
						showExceptionError(e);
					}
				} else {
					showConnectionError();

				}
				break;
			case DELETE_TOUR_METHOD:
				progress.dismiss();
				if (api_result.ok())
					getToursFromServer();
				else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.delete_tour_error_message));
					exception.alertExceptionMessage(ToursMainActivity.this);
				}
				break;
			}
		}
	}

	public void showConnectionError() {
		CustomTravelJoinException exception = new CustomTravelJoinException();
		exception.alertConnectionProblem(this);
	}
	
	public void showExceptionError(Exception e) {
		CustomTravelJoinException exception = new CustomTravelJoinException(
				e.getMessage());
		exception.alertExceptionMessage(this);
		e.printStackTrace();
	}

	private void initializeUser() {
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		user = globalContext.getUser();
	}

}