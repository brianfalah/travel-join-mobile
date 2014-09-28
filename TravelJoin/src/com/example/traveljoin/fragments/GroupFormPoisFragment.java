package com.example.traveljoin.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Poi;

//TODO: Generalizar en una superclase junto con GroupFormToursFragment. Y luego ver si no convertir a todas los fragmentos en privados
public class GroupFormPoisFragment extends ListFragment {
	private ArrayList<GeneralItem> pois;
	private GeneralItemListAdapter poisAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		pois = new ArrayList<GeneralItem>();

		pois.add(new Poi("POI 1", "Descripcion 1"));
		pois.add(new Poi("POI 2", "Descripcion 2"));
		pois.add(new Poi("POI 3", "Descripcion 3"));

		poisAdapter = new GeneralItemListAdapter(
				getActivity(), pois);
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
	        	pois.add(new Poi("POI Nuevo", "Descripcion nueva"));
	        	poisAdapter.notifyDataSetChanged();
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
			Toast.makeText(getActivity(),
					"Delete : " + selectedPoi.getName(), Toast.LENGTH_SHORT)
					.show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private Poi getPoiItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Poi poi = (Poi) pois.get(info.position);
		return poi;
	}

}
