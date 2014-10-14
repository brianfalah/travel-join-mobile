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
	private GeneralItemListAdapter poisAdapter;
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
		poisAdapter = new GeneralItemListAdapter(tourFormActivity, fragmentTourPois);
		setListAdapter(poisAdapter);
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
		Poi selectedPoi;
		switch (item.getItemId()) {
		case R.id.context_menu_delete:
			selectedPoi = getPoiItem(item);
			fragmentTourPois.remove(selectedPoi);
			poisAdapter.notifyDataSetChanged();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private Poi getPoiItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Poi poi = (Poi) fragmentTourPois.get(info.position);
		return poi;
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
				for (int i = 0; i < fragmentTourPois.size(); i++) {
					TourPoi tourPoi = (TourPoi) fragmentTourPois.get(i);
					if(!newSelectedPois.contains(tourPoi))
					{
						tourPoi.setDeleted(true);
						tourFormActivity.tourPoisToDelete.add(tourPoi);
					}
				}
				fragmentTourPois.clear();
				tourFormActivity.tourPois.clear();
				fragmentTourPois.addAll(newSelectedPois);
				tourFormActivity.tourPois.addAll(newSelectedPois);
				poisAdapter.notifyDataSetChanged();
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;

		}

	}
}
