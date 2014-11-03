package com.example.traveljoin.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.PoisSelectorActivity;
import com.example.traveljoin.activities.TourFormActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.TourPoi;

public class TourFormPoisFragment extends ListFragment {
	TourFormActivity tourFormActivity;
	private ArrayList<GeneralItem> fragmentTourPois;
	private GeneralItemListAdapter tourPoisAdapter;
	private static final int ADD_POI_REQUEST = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tourFormActivity = (TourFormActivity) getActivity();
		fragmentTourPois = new ArrayList<GeneralItem>();
		if (tourFormActivity.tour != null){
			fragmentTourPois.addAll(tourFormActivity.tour.getTourPois());
		}
		
		tourPoisAdapter = new GeneralItemListAdapter(tourFormActivity, fragmentTourPois);
		setListAdapter(tourPoisAdapter);
		setEmptyText("Este circuito aún no tiene puntos de interés.");
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.only_add_item_action, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_add:			
			Intent intent = new Intent(tourFormActivity, PoisSelectorActivity.class);
			intent.putExtra("alreadySelectedPois", fragmentTourPois);
			startActivityForResult(intent, ADD_POI_REQUEST);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.only_delete_item_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		TourPoi selectedTourPoi;
		switch (item.getItemId()) {
		case R.id.context_menu_delete:
			selectedTourPoi = getTourPoiItem(item);			
			fragmentTourPois.remove(selectedTourPoi);
			tourPoisAdapter.notifyDataSetChanged();			
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private TourPoi getTourPoiItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		TourPoi tourPoi = (TourPoi) fragmentTourPois.get(info.position);
		return tourPoi;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_POI_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Bundle bundle = data.getExtras();
				ArrayList<GeneralItem> newSelectedPois = (ArrayList<GeneralItem>) bundle.get("newSelectedPois");
				
				fragmentTourPois.clear();
				//vemos todos los nuevos seleccionados y armamos 1 array de TourPois y otro de Ids de Pois
				for (int j = 0; j < newSelectedPois.size(); j++) {	
					Poi selectedPoi = (Poi) newSelectedPois.get(j);
					Integer tourId = (tourFormActivity.tour != null) ? tourFormActivity.tour.getId() : null;
					TourPoi tourPoiToAdd = new TourPoi(null, tourId, selectedPoi.getId(), selectedPoi.getName(), selectedPoi.getDescription(), null);
					fragmentTourPois.add(tourPoiToAdd);
				}

				tourPoisAdapter.notifyDataSetChanged();
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;

		}

	}
	
	
	public ArrayList<GeneralItem> getTourPois(){
		return fragmentTourPois;
	}
}
