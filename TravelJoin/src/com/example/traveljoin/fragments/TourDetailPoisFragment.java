package com.example.traveljoin.fragments;

import java.util.ArrayList;
import java.util.Collection;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.PoiDetailsActivity;
import com.example.traveljoin.activities.PoiEventDetailsActivity;
import com.example.traveljoin.activities.TourDetailsActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.PoiEvent;
import com.example.traveljoin.models.TourPoi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TourDetailPoisFragment extends ListFragment {

	TourDetailsActivity activity;
	private ArrayList<GeneralItem> tourPois;
	GeneralItemListAdapter adapter;
	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_general_list,
				container, false);
		listView = (ListView) view.findViewById(R.id.list);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = (TourDetailsActivity) getActivity();              
		
		tourPois = new ArrayList<GeneralItem>();
		//tourPois.addAll((Collection<? extends GeneralItem>) activity.tour.getTourPois());

		adapter = new GeneralItemListAdapter(
				getActivity(), tourPois);
		setListAdapter(adapter);
//		registerForContextMenu(getListView());
	}

//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(menu, v, menuInfo);
//		MenuInflater inflater = getActivity().getMenuInflater();
//		inflater.inflate(R.menu.group_list_item_context_menu, menu);
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		Group selectedGroup; 
//		switch (item.getItemId()) {
//		case R.id.group_context_menu_view:
//			// TODO: Redirigir a la vista del Grupo de vista
//			selectedGroup = getGroupItem(item);
//			Toast.makeText(getActivity(),
//					"View : " + selectedGroup.getName(),
//					Toast.LENGTH_SHORT).show();
//			return true;
//		case R.id.group_context_menu_edit:
//			// TODO: Redirigir a la vista del Grupo de edicion
//			selectedGroup = getGroupItem(item);
//			Toast.makeText(getActivity(),
//					"Edit : " + selectedGroup.getName(),
//					Toast.LENGTH_SHORT).show();
//			return true;
//		case R.id.group_context_menu_delete:
//			// TODO: Ejecutar la misma funcion para eliminar un Grupo
//			selectedGroup = getGroupItem(item);
//			Toast.makeText(getActivity(),
//					"Delete : " + selectedGroup.getName(),
//					Toast.LENGTH_SHORT).show();
//			return true;
//		default:
//			return super.onContextItemSelected(item);
//		}
//	}
//
//	
//	private Group getGroupItem(MenuItem item) {
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//		Group group = (Group) events.get(info.position);
//		return group;
//	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		startPoiDetailsActivity((TourPoi) adapter.getItem(position));
	}
	
	private void startPoiDetailsActivity(TourPoi selectedTourPoi) {
		Intent intent = new Intent(activity, PoiDetailsActivity.class);
		intent.putExtra("poi_id", selectedTourPoi.getPoiId());
		startActivity(intent);
	}
	
	public void refreshList(){
		tourPois.clear();
		tourPois.addAll((Collection<? extends GeneralItem>) activity.tour.getTourPois());
		adapter.notifyDataSetChanged();
	}
	
}
