package com.example.traveljoin.fragments;

import java.util.ArrayList;

import com.example.traveljoin.adapters.GeneralListItemArrayAdapter;
import com.example.traveljoin.models.GeneralListItem;
import com.example.traveljoin.models.Poi;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class PoiListFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayList<GeneralListItem> pois = new ArrayList<GeneralListItem>();

		pois.add(new Poi("Prueba 1", "Descripcion 1"));
		pois.add(new Poi("Prueba 2", "Descripcion 2"));
		pois.add(new Poi("Prueba 3", "Descripcion 3"));
		
		GeneralListItemArrayAdapter adapter = new GeneralListItemArrayAdapter(getActivity(), pois);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		// do something with the data
	}
}
