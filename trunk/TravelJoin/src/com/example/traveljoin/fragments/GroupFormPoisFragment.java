package com.example.traveljoin.fragments;

import java.util.ArrayList;
import java.util.Collection;

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
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Interest;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.Tour;

public class GroupFormPoisFragment extends ListFragment {
	private ArrayList<GeneralItem> pois;
	private GeneralItemListAdapter poisAdapter;
	private static final int ADD_POIS_REQUEST = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		pois = new ArrayList<GeneralItem>();
		poisAdapter = new GeneralItemListAdapter(getActivity(), pois);
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
			Intent intent = new Intent(getActivity(),
					PoisSelectorActivity.class);
			intent.putExtra("alreadySelectedPois", pois);
			startActivityForResult(intent, ADD_POIS_REQUEST);
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
			pois.remove(selectedPoi);
			poisAdapter.notifyDataSetChanged();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private Poi getPoiItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Poi poi = (Poi) pois.get(info.position);
		return poi;
	}

	public ArrayList<Poi> getPois() {
		ArrayList<Poi> auxPois = new ArrayList<Poi>();
		auxPois.addAll((Collection<? extends Poi>) pois);		
		return auxPois;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_POIS_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Bundle bundle = data.getExtras();
				pois.clear();
				pois.addAll((ArrayList<GeneralItem>) bundle.get("newSelectedPois"));
				poisAdapter.notifyDataSetChanged();
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;
		}
	}

}
