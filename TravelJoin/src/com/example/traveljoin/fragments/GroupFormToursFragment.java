package com.example.traveljoin.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupFormActivity;
import com.example.traveljoin.activities.ToursSelectorActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.GroupTour;
import com.example.traveljoin.models.Tour;

public class GroupFormToursFragment extends ListFragment {
	private GroupFormActivity groupFormActivity;
	private ArrayList<GeneralItem> fragmentGroupTours;
	private GeneralItemListAdapter groupToursAdapter;
	private static final int ADD_TOURS_REQUEST = 1;
	
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_group_form_tours,
				container, false);		
		return view;
	}
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		groupFormActivity = (GroupFormActivity) getActivity();
		fragmentGroupTours = new ArrayList<GeneralItem>();
		if (groupFormActivity.group != null){
			fragmentGroupTours.addAll(groupFormActivity.group.getGroupTours());
		}	
		groupToursAdapter = new GeneralItemListAdapter(groupFormActivity, fragmentGroupTours);
		setListAdapter(groupToursAdapter);
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
			Intent intent = new Intent(groupFormActivity, ToursSelectorActivity.class);
			intent.putExtra("alreadySelectedTours", fragmentGroupTours);
			startActivityForResult(intent, ADD_TOURS_REQUEST);
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
		if( getUserVisibleHint() == false ) 
	    {
	        return false;
	    }
		GroupTour selectedGroupTour;
		switch (item.getItemId()) {
		case R.id.context_menu_delete:
			selectedGroupTour = getGroupTourItem(item);			
			fragmentGroupTours.remove(selectedGroupTour);
			groupToursAdapter.notifyDataSetChanged();			
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private GroupTour getGroupTourItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		GroupTour groupTour = (GroupTour) fragmentGroupTours.get(info.position);
		return groupTour;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_TOURS_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Bundle bundle = data.getExtras();
				ArrayList<GeneralItem> newSelectedTours = (ArrayList<GeneralItem>) bundle.get("newSelectedTours");
				
				fragmentGroupTours.clear();
				//vemos todos los nuevos seleccionados y armamos 1 array de TourPois y otro de Ids de Pois
				for (int j = 0; j < newSelectedTours.size(); j++) {	
					Tour selectedTour = (Tour) newSelectedTours.get(j);
					Integer groupId = (groupFormActivity.group != null) ? groupFormActivity.group.getId() : null;
					GroupTour groupTourToAdd = new GroupTour(null, groupId, selectedTour.getId(), selectedTour.getName(), selectedTour.getDescription());
					fragmentGroupTours.add(groupTourToAdd);
				}

				groupToursAdapter.notifyDataSetChanged();

				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;

		}

	}
	
	public ArrayList<GeneralItem> getGroupTours(){
		return fragmentGroupTours;
	}

}