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
import com.example.traveljoin.activities.GroupFormActivity;
import com.example.traveljoin.activities.InterestsSelectorActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.GroupInterest;
import com.example.traveljoin.models.Interest;

public class GroupFormInterestsFragment extends ListFragment {
	
	GroupFormActivity groupFormActivity;
	private ArrayList<GeneralItem> fragmentGroupInterests;
	private GeneralItemListAdapter groupInterestsAdapter;
	private static final int ADD_INTERESTS_REQUEST = 1;
	
    
    public GroupFormInterestsFragment(Group group){
		this.fragmentGroupInterests = new ArrayList<GeneralItem>();
		if (group != null){
			fragmentGroupInterests.addAll(group.getGroupInterests());
		}	
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
		groupInterestsAdapter = new GeneralItemListAdapter(groupFormActivity, fragmentGroupInterests);
		setListAdapter(groupInterestsAdapter);
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
			Intent intent = new Intent(groupFormActivity, InterestsSelectorActivity.class);
			intent.putExtra("alreadySelectedInterests", fragmentGroupInterests);
			startActivityForResult(intent, ADD_INTERESTS_REQUEST);
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
		GroupInterest selectedGroupInterest;
		switch (item.getItemId()) {
			case R.id.context_menu_delete:
				selectedGroupInterest = getGroupInterestItem(item);
				fragmentGroupInterests.remove(selectedGroupInterest);
				groupInterestsAdapter.notifyDataSetChanged();			
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	private GroupInterest getGroupInterestItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		GroupInterest groupInterest = (GroupInterest) fragmentGroupInterests.get(info.position);
		return groupInterest;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_INTERESTS_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Bundle bundle = data.getExtras();
				ArrayList<GeneralItem> newSelectedInterests = (ArrayList<GeneralItem>) bundle.get("newSelectedInterests");
				
				fragmentGroupInterests.clear();
				//vemos todos los nuevos seleccionados y armamos 1 array de TourPois y otro de Ids de Pois
				for (int j = 0; j < newSelectedInterests.size(); j++) {	
					Interest selectedInterest = (Interest) newSelectedInterests.get(j);
					Integer groupId = (groupFormActivity.group != null) ? groupFormActivity.group.getId() : null;
					GroupInterest groupInterestToAdd = new GroupInterest(null, groupId, selectedInterest.getId(), selectedInterest.getName());
					fragmentGroupInterests.add(groupInterestToAdd);
				}

				groupInterestsAdapter.notifyDataSetChanged();
				
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;
		}

	}
	
	public ArrayList<GeneralItem> getGroupInterests(){
		return fragmentGroupInterests;
	}

}