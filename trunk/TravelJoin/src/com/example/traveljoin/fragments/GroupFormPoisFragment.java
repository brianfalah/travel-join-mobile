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
import com.example.traveljoin.activities.PoisSelectorActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.GroupPoi;
import com.example.traveljoin.models.Poi;

public class GroupFormPoisFragment extends ListFragment {
	GroupFormActivity groupFormActivity;
	private ArrayList<GeneralItem> fragmentGroupPois;
	private GeneralItemListAdapter groupPoisAdapter;
	private static final int ADD_POIS_REQUEST = 1;

    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_group_form_pois,
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
		fragmentGroupPois = new ArrayList<GeneralItem>();
		if (groupFormActivity.group != null){
			fragmentGroupPois.addAll(groupFormActivity.group.getGroupPois());
		}	
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
		if( getUserVisibleHint() == false ) 
	    {
	        return false;
	    }
		GroupPoi selectedGroupPoi;
		switch (item.getItemId()) {
		case R.id.context_menu_delete:
			selectedGroupPoi = getGroupPoiItem(item);
			fragmentGroupPois.remove(selectedGroupPoi);
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
				
				fragmentGroupPois.clear();
				//vemos todos los nuevos seleccionados y armamos 1 array de TourPois y otro de Ids de Pois
				for (int j = 0; j < newSelectedPois.size(); j++) {	
					Poi selectedPoi = (Poi) newSelectedPois.get(j);
					Integer groupId = (groupFormActivity.group != null) ? groupFormActivity.group.getId() : null;
					GroupPoi groupPoiToAdd = new GroupPoi(null, groupId, selectedPoi.getId(), selectedPoi.getName(), selectedPoi.getDescription());
					fragmentGroupPois.add(groupPoiToAdd);
				}

				groupPoisAdapter.notifyDataSetChanged();
				
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;

		}

	}
	
	public ArrayList<GeneralItem> getGroupPois(){
		return fragmentGroupPois;
	}
	
	 @Override
     public void onAttach(Activity activity)
     {
         super.onAttach(activity);
     }

     @Override
     public void onStart()
     {
         super.onStart();
     }

     @Override
     public void onResume()
     {
         super.onResume();
     }

}
