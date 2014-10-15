package com.example.traveljoin.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupFormActivity;
import com.example.traveljoin.activities.InterestsSelectorActivity;
import com.example.traveljoin.activities.PoisSelectorActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.GroupInterest;
import com.example.traveljoin.models.GroupPoi;
import com.example.traveljoin.models.Interest;

public class GroupFormInterestsFragment extends ListFragment {
	
	GroupFormActivity groupFormActivity;
	private ArrayList<GeneralItem> fragmentGroupInterests;
	private GeneralItemListAdapter groupInterestsAdapter;
	private static final int ADD_INTERESTS_REQUEST = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		groupFormActivity = (GroupFormActivity) getActivity();
		fragmentGroupInterests = new ArrayList<GeneralItem>();
		fragmentGroupInterests.clear();
		fragmentGroupInterests.addAll(groupFormActivity.groupInterests);
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
		GroupInterest selectedGroupInterest;
		switch (item.getItemId()) {
		case R.id.context_menu_delete:
			selectedGroupInterest = getGroupInterestItem(item);
			selectedGroupInterest.setDeleted(true);
			groupFormActivity.groupInterestsToDelete.add(selectedGroupInterest);
			fragmentGroupInterests.remove(selectedGroupInterest);
			groupFormActivity.groupInterests.remove(selectedGroupInterest);
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
				ArrayList<GroupInterest> newSelectedGroupInterests = new ArrayList<GroupInterest>();
				ArrayList<Integer> newSelectedInterestIds = new ArrayList<Integer>();
				
				ArrayList<Integer> oldSelectedInterestIds = new ArrayList<Integer>();
								
				
				for (int j = 0; j < newSelectedInterests.size(); j++) {					
					Integer groupId = (groupFormActivity.group != null) ? groupFormActivity.group.getId() : null;
					GroupInterest groupInterestToAdd = new GroupInterest(null, groupId, newSelectedInterests.get(j).getId(), newSelectedInterests.get(j).getName());
					newSelectedGroupInterests.add(groupInterestToAdd);
					newSelectedInterestIds.add(newSelectedInterests.get(j).getId());
				}
				
				for (int i = 0; i < fragmentGroupInterests.size(); i++) {
					GroupInterest groupInterest = (GroupInterest) fragmentGroupInterests.get(i);
					oldSelectedInterestIds.add(groupInterest.getInterestId());
					
					if(!newSelectedInterestIds.contains(groupInterest.getInterestId()))
					{
						groupInterest.setDeleted(true);
						fragmentGroupInterests.remove(i);
						groupFormActivity.groupInterestsToDelete.add(groupInterest);
					}
				}
				
				for (int i = 0; i < newSelectedGroupInterests.size(); i++) {
					if ( !oldSelectedInterestIds.contains(newSelectedGroupInterests.get(i).getInterestId() ) ){						
						fragmentGroupInterests.add(newSelectedGroupInterests.get(i));
						groupFormActivity.groupInterests.add(newSelectedGroupInterests.get(i));
					}
				}

				groupInterestsAdapter.notifyDataSetChanged();
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;

		}

	}

}