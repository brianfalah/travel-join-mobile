package com.example.traveljoin.fragments;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.EventFormActivity;
import com.example.traveljoin.activities.PoiFormActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.PoiEvent;

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

public class PoiFormEventsFragment extends ListFragment {
	PoiFormActivity activity;
	private GeneralItemListAdapter poiEventsAdapter;
	public ArrayList<GeneralItem> fragmentPoiEvents;
	private static final int ADD_EVENT_REQUEST = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = (PoiFormActivity) getActivity();		
		fragmentPoiEvents = new ArrayList<GeneralItem>();
		fragmentPoiEvents.clear();
		fragmentPoiEvents.addAll(activity.poiEvents);
		poiEventsAdapter = new GeneralItemListAdapter(activity, fragmentPoiEvents);
		setListAdapter(poiEventsAdapter);
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
			addEvent();
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
		PoiEvent selectedPoiEvent;
		switch (item.getItemId()) {
		case R.id.context_menu_delete:
			selectedPoiEvent = getPoiEventItem(item);
			selectedPoiEvent.setDeleted(true);
			activity.poiEventsToDelete.add(selectedPoiEvent);
			fragmentPoiEvents.remove(selectedPoiEvent);
			activity.poiEvents.remove(selectedPoiEvent);
			poiEventsAdapter.notifyDataSetChanged();			
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private PoiEvent getPoiEventItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		PoiEvent poiEvent = (PoiEvent) fragmentPoiEvents.get(info.position);
		return poiEvent;
	}
	
	// EVENTOS!!!
	public void addEvent() {
		Intent intent = new Intent(activity, EventFormActivity.class);
		if (activity.poi != null) {
			intent.putExtra("poi_id", activity.poi.getId());
		}
		// va al form para crear un evento y espera un result_code(para saber si
		// se creo o no)
		// y el evento creado
		startActivityForResult(intent, ADD_EVENT_REQUEST);
	}

	/*
	 * Cuando vuelve de un activity empezado con un startActivityForResult viene
	 * aca
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		// PARA CUANDO SE VUELVE DE CREAR UN EVENTO
		case ADD_EVENT_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, agregar punto al mapa
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				Bundle b = data.getExtras();
				PoiEvent poiEvent = (PoiEvent) b.get("poiEvent");
				activity.poiEvents.add(poiEvent);
				fragmentPoiEvents.add(poiEvent);
				poiEventsAdapter.notifyDataSetChanged();
				break;
			case Activity.RESULT_CANCELED:

				break;
			}
			break;

		}

	}
}
