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
		fragmentTourPois.clear();
		fragmentTourPois.addAll(tourFormActivity.tourPois);
		tourPoisAdapter = new GeneralItemListAdapter(tourFormActivity, fragmentTourPois);
		setListAdapter(tourPoisAdapter);
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
			selectedTourPoi.setDeleted(true);
			tourFormActivity.tourPoisToDelete.add(selectedTourPoi);
			fragmentTourPois.remove(selectedTourPoi);
			tourFormActivity.tourPois.remove(selectedTourPoi);
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
				ArrayList<TourPoi> newSelectedTourPois = new ArrayList<TourPoi>();
				ArrayList<Integer> newSelectedPoiIds = new ArrayList<Integer>();
				
				ArrayList<Integer> oldSelectedPoiIds = new ArrayList<Integer>();
								
				
				for (int j = 0; j < newSelectedPois.size(); j++) {					
					Integer tourId = (tourFormActivity.tour != null) ? tourFormActivity.tour.getId() : null;
					TourPoi tourPoiToAdd = new TourPoi(null, tourId, newSelectedPois.get(j).getId(), newSelectedPois.get(j).getName(), newSelectedPois.get(j).getDescription(), null);
					newSelectedTourPois.add(tourPoiToAdd);
					newSelectedPoiIds.add(newSelectedPois.get(j).getId());
				}
				
				for (int i = 0; i < fragmentTourPois.size(); i++) {
					TourPoi tourPoi = (TourPoi) fragmentTourPois.get(i);
					oldSelectedPoiIds.add(tourPoi.getPoiId());
					
					if(!newSelectedPoiIds.contains(tourPoi.getPoiId()))
					{
						tourPoi.setDeleted(true);
						fragmentTourPois.remove(i);
						tourFormActivity.tourPoisToDelete.add(tourPoi);
					}
				}
				
				for (int i = 0; i < newSelectedTourPois.size(); i++) {
					if ( !oldSelectedPoiIds.contains(newSelectedTourPois.get(i).getPoiId() ) ){						
						fragmentTourPois.add(newSelectedTourPois.get(i));
						tourFormActivity.tourPois.add(newSelectedTourPois.get(i));
					}
				}

				tourPoisAdapter.notifyDataSetChanged();
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;

		}

	}
}
