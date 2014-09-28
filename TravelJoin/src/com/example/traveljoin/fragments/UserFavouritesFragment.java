package com.example.traveljoin.fragments;

import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.FavouritesExpandableListAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.GroupFavouriteItems;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.Tour;
import com.example.traveljoin.models.User;

public class UserFavouritesFragment extends Fragment {

	private final static int POIS_GROUP_KEY = 0;
	private final static int TOURS_GROUP_KEY = 1;
	private GroupFavouriteItems poisGroup;
	private GroupFavouriteItems toursGroup;
	private SparseArray<GroupFavouriteItems> groups;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_user_favourites,
				container, false);

		poisGroup = new GroupFavouriteItems(
				getString(R.string.favourite_pois_group),
				R.drawable.ic_action_place);
		toursGroup = new GroupFavouriteItems(
				getString(R.string.favourite_tours_group),
				R.drawable.ic_action_split);

		createItemsGroups();

		ExpandableListView expadableListView = (ExpandableListView) view
				.findViewById(R.id.favouritesExpandableListView);
		FavouritesExpandableListAdapter adapter = new FavouritesExpandableListAdapter(
				getActivity(), groups);
		expadableListView.setAdapter(adapter);

		registerForContextMenu(expadableListView);

		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);

		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			MenuInflater inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.fovourite_list_item_context_menu, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		GeneralItem favouriteItem;
		switch (item.getItemId()) {
		case R.id.favourite_context_menu_view:
			// TODO: Redirigir a la vista
			favouriteItem = getFavouriteItem(item);
			Toast.makeText(getActivity(), "View: " + favouriteItem.getName(),
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.favourite_context_menu_edit:
			// TODO: Redirigir a la vista de edicion
			favouriteItem = getFavouriteItem(item);
			Toast.makeText(getActivity(), "Edit: " + favouriteItem.getName(),
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.favourite_context_menu_delete:
			// TODO: Ejecutar la misma funcion para eliminarlo
			favouriteItem = getFavouriteItem(item);
			Toast.makeText(getActivity(), "Delete: " + favouriteItem.getName(),
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private GeneralItem getFavouriteItem(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item
				.getMenuInfo();
		int groupPosition = ExpandableListView
				.getPackedPositionGroup(info.packedPosition);
		int childPosition = ExpandableListView
				.getPackedPositionChild(info.packedPosition);
		GroupFavouriteItems group = (GroupFavouriteItems) groups
				.get(groupPosition);
		GeneralItem favouriteItem = (GeneralItem) group.getItem(childPosition);
		return favouriteItem;
	}

	public void createItemsGroups() {
		groups = new SparseArray<GroupFavouriteItems>();
		User user = ((GlobalContext) getActivity().getApplicationContext())
				.getUser();
		// TODO: Realizar la llamada al servidor para los Tours
		for (int i = 0; i < 5; i++) {
			toursGroup.add(new Tour("Prueba " + i, "Descripcion " + i));
		}
		getPoisFromServer(user.getId());
		groups.put(POIS_GROUP_KEY, poisGroup);
		groups.put(TOURS_GROUP_KEY, toursGroup);
	}

	private void getPoisFromServer(int userId) {

		String url = getResources().getString(R.string.api_url)
				+ "/pois/get_created_by_user.json?user_id=" + userId;
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
			// Toast.makeText(getBaseContext(), "Received!",
			// Toast.LENGTH_LONG).show();
			Log.d("InputStream", result);
			if (api_result.ok()) {
				try {
					JSONArray pois = new JSONArray(result);
					for (int i = 0; i < pois.length(); i++) {
						JSONObject poiJson = pois.getJSONObject(i);
						Poi poi = Poi.fromJSON(poiJson);
						poisGroup.add(poi);
					}

				} catch (JSONException e) {
					// TODO: Handlear
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