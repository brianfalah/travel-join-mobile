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
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Tour;

public class GroupFormToursFragment extends ListFragment {
	private ArrayList<GeneralItem> tours;
	private GeneralItemListAdapter toursAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tours = new ArrayList<GeneralItem>();

		tours.add(new Tour("Tour 1", "Descripcion 1"));
		tours.add(new Tour("Tour 2", "Descripcion 2"));
		tours.add(new Tour("Tour 3", "Descripcion 3"));

		toursAdapter = new GeneralItemListAdapter(
				getActivity(), tours);
		setListAdapter(toursAdapter);
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
	        	tours.add(new Tour("Tour Nuevo", "Descripcion nueva"));
	        	toursAdapter.notifyDataSetChanged();
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
		Tour selectedTour;
		switch (item.getItemId()) {
		case R.id.context_menu_delete:
			selectedTour = getTourItem(item);
			tours.remove(selectedTour);
        	toursAdapter.notifyDataSetChanged();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private Tour getTourItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Tour tour = (Tour) tours.get(info.position);
		return tour;
	}

}