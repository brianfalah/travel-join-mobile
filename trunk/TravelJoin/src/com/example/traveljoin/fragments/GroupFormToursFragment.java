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
import com.example.traveljoin.activities.GroupFormActivity;
import com.example.traveljoin.activities.PoisSelectorActivity;
import com.example.traveljoin.activities.ToursSelectorActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.GroupPoi;
import com.example.traveljoin.models.GroupTour;
import com.example.traveljoin.models.Tour;

public class GroupFormToursFragment extends ListFragment {
	GroupFormActivity groupFormActivity;
	private ArrayList<GeneralItem> fragmentGroupTours;
	private GeneralItemListAdapter groupToursAdapter;
	private static final int ADD_TOURS_REQUEST = 1;

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
		fragmentGroupTours.clear();
		fragmentGroupTours.addAll(groupFormActivity.groupTours);
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
		GroupTour selectedGroupTour;
		switch (item.getItemId()) {
		case R.id.context_menu_delete:
			selectedGroupTour = getGroupTourItem(item);
			selectedGroupTour.setDeleted(true);
			groupFormActivity.groupToursToDelete.add(selectedGroupTour);
			fragmentGroupTours.remove(selectedGroupTour);
			groupFormActivity.groupTours.remove(selectedGroupTour);
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
				ArrayList<GroupTour> newSelectedGroupTours = new ArrayList<GroupTour>();
				ArrayList<Integer> newSelectedTourIds = new ArrayList<Integer>();
				
				ArrayList<Integer> oldSelectedTourIds = new ArrayList<Integer>();
								
				
				for (int j = 0; j < newSelectedTours.size(); j++) {					
					Integer groupId = (groupFormActivity.group != null) ? groupFormActivity.group.getId() : null;
					GroupTour groupTourToAdd = new GroupTour(null, groupId, newSelectedTours.get(j).getId(), newSelectedTours.get(j).getName(), newSelectedTours.get(j).getDescription());
					newSelectedGroupTours.add(groupTourToAdd);
					newSelectedTourIds.add(newSelectedTours.get(j).getId());
				}
				
				for (int i = 0; i < fragmentGroupTours.size(); i++) {
					GroupTour groupTour = (GroupTour) fragmentGroupTours.get(i);
					oldSelectedTourIds.add(groupTour.getTourId());
					
					if(!newSelectedTourIds.contains(groupTour.getTourId()))
					{
						groupTour.setDeleted(true);
						fragmentGroupTours.remove(i);
						groupFormActivity.groupToursToDelete.add(groupTour);
					}
				}
				
				for (int i = 0; i < newSelectedGroupTours.size(); i++) {
					if ( !oldSelectedTourIds.contains(newSelectedGroupTours.get(i).getTourId() ) ){						
						fragmentGroupTours.add(newSelectedGroupTours.get(i));
						groupFormActivity.groupTours.add(newSelectedGroupTours.get(i));
					}
				}

				groupToursAdapter.notifyDataSetChanged();
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;

		}

	}

}