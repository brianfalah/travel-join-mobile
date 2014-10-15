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
import com.example.traveljoin.activities.TourFormActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.GroupPoi;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.TourPoi;

public class GroupFormPoisFragment extends ListFragment {
	GroupFormActivity groupFormActivity;
	private ArrayList<GeneralItem> fragmentGroupPois;
	private GeneralItemListAdapter groupPoisAdapter;
	private static final int ADD_POIS_REQUEST = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		groupFormActivity = (GroupFormActivity) getActivity();
		fragmentGroupPois = new ArrayList<GeneralItem>();
		fragmentGroupPois.clear();
		fragmentGroupPois.addAll(groupFormActivity.groupPois);
		groupPoisAdapter = new GeneralItemListAdapter(groupFormActivity, fragmentGroupPois);
		setListAdapter(groupPoisAdapter);
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
			Intent intent = new Intent(groupFormActivity, PoisSelectorActivity.class);
			intent.putExtra("alreadySelectedPois", fragmentGroupPois);
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
		GroupPoi selectedGroupPoi;
		switch (item.getItemId()) {
		case R.id.context_menu_delete:
			selectedGroupPoi = getGroupPoiItem(item);
			selectedGroupPoi.setDeleted(true);
			groupFormActivity.groupPoisToDelete.add(selectedGroupPoi);
			fragmentGroupPois.remove(selectedGroupPoi);
			groupFormActivity.groupPois.remove(selectedGroupPoi);
			groupPoisAdapter.notifyDataSetChanged();			
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private GroupPoi getGroupPoiItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		GroupPoi groupPoi = (GroupPoi) fragmentGroupPois.get(info.position);
		return groupPoi;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_POIS_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Bundle bundle = data.getExtras();
				ArrayList<GeneralItem> newSelectedPois = (ArrayList<GeneralItem>) bundle.get("newSelectedPois");
				ArrayList<GroupPoi> newSelectedGroupPois = new ArrayList<GroupPoi>();
				ArrayList<Integer> newSelectedPoiIds = new ArrayList<Integer>();
				
				ArrayList<Integer> oldSelectedPoiIds = new ArrayList<Integer>();
								
				
				for (int j = 0; j < newSelectedPois.size(); j++) {					
					Integer groupId = (groupFormActivity.group != null) ? groupFormActivity.group.getId() : null;
					GroupPoi groupPoiToAdd = new GroupPoi(null, groupId, newSelectedPois.get(j).getId(), newSelectedPois.get(j).getName(), newSelectedPois.get(j).getDescription());
					newSelectedGroupPois.add(groupPoiToAdd);
					newSelectedPoiIds.add(newSelectedPois.get(j).getId());
				}
				
				for (int i = 0; i < fragmentGroupPois.size(); i++) {
					GroupPoi groupPoi = (GroupPoi) fragmentGroupPois.get(i);
					oldSelectedPoiIds.add(groupPoi.getPoiId());
					
					if(!newSelectedPoiIds.contains(groupPoi.getPoiId()))
					{
						groupPoi.setDeleted(true);
						fragmentGroupPois.remove(i);
						groupFormActivity.groupPoisToDelete.add(groupPoi);
					}
				}
				
				for (int i = 0; i < newSelectedGroupPois.size(); i++) {
					if ( !oldSelectedPoiIds.contains(newSelectedGroupPois.get(i).getPoiId() ) ){						
						fragmentGroupPois.add(newSelectedGroupPois.get(i));
						groupFormActivity.groupPois.add(newSelectedGroupPois.get(i));
					}
				}

				groupPoisAdapter.notifyDataSetChanged();
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;

		}

	}

}
