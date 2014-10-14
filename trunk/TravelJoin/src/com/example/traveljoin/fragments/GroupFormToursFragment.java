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
import com.example.traveljoin.activities.ToursSelectorActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Tour;

public class GroupFormToursFragment extends ListFragment {
	private ArrayList<GeneralItem> tours;
	private GeneralItemListAdapter toursAdapter;
	private static final int ADD_TOURS_REQUEST = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tours = new ArrayList<GeneralItem>();
		toursAdapter = new GeneralItemListAdapter(getActivity(), tours);
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
			Intent intent = new Intent(getActivity(),
					ToursSelectorActivity.class);
			intent.putExtra("alreadySelectedTours", tours);
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
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Tour tour = (Tour) tours.get(info.position);
		return tour;
	}

	public ArrayList<Tour> getTours() {
		ArrayList<Tour> auxTours = new ArrayList<Tour>();
		auxTours.addAll((Collection<? extends Tour>) tours);
		return auxTours;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_TOURS_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Bundle bundle = data.getExtras();
				tours.clear();
				tours.addAll((ArrayList<GeneralItem>) bundle.get("newSelectedTours"));
				toursAdapter.notifyDataSetChanged();
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
			break;
		}
	}

}