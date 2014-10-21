package com.example.traveljoin.fragments;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.PoiDetailsActivity;
import com.example.traveljoin.activities.PoiEventDetailsActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.PoiEvent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PoiEventsFragment extends ListFragment {

	PoiDetailsActivity activity;
	private ArrayList<GeneralItem> events;
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
		activity = (PoiDetailsActivity) getActivity();              
				
		events = new ArrayList<GeneralItem>();
		events.addAll(activity.poi.getPoiEvents());

		adapter = new GeneralItemListAdapter(
				getActivity(), events);
		setListAdapter(adapter);
//		registerForContextMenu(getListView());
	}
	
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(menu, v, menuInfo);
//		MenuInflater inflater = getActivity().getMenuInflater();
//		inflater.inflate(R.menu.poi_event_list_item_context_menu, menu);
//		
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
//		PoiEvent poiEvent = (PoiEvent) listView.getItemAtPosition(info.position);
//		if (!poiEvent.getUserId().equals(user.getId())) {
//			menu.removeItem(R.id.poi_event_context_menu_edit);
//		}
//	}

//	OnItemClickListener groupItemClickListener = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			startPoiEventDetailActivity((Group) adapter.getItem(position));
//		}
//	};
	
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		final PoiEvent selectedPoiEvent = getPoiEventItem(item);
//		switch (item.getItemId()) {
//		case R.id.poi_event_context_menu_view:
//			startPoiEventDetailsActivity(selectedPoiEvent);
//			return true;
//		case R.id.poi_event_context_menu_edit:
//			Intent intent_edit = new Intent(activity, PoiEventFormActivity.class);
//			intent_edit.putExtra("poi_event", selectedPoiEvent);
//			startActivityForResult(intent_edit, EDIT|);
//			return true;
//		default:
//			return super.onContextItemSelected(item);
//		}
//	}
//
//	
//	private PoiEvent getPoiEventItem(MenuItem item) {
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//		PoiEvent poiEvent = (PoiEvent) events.get(info.position);
//		return poiEvent;
//	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		startPoiEventDetailsActivity((PoiEvent) adapter.getItem(position));
	}
	
	private void startPoiEventDetailsActivity(PoiEvent selectedPoiEvent) {
		Intent intent = new Intent(activity, PoiEventDetailsActivity.class);
		intent.putExtra("poi_event", selectedPoiEvent);
		intent.putExtra("poi_user_id", activity.poi.getUserId());
		startActivity(intent);
	}
	 
	//TODO ver esto porque cuando se refresque no va a tomar los cambios si edito un evento
	public void refreshList(){
		events.clear();
		events.addAll(activity.poi.getPoiEvents());
		adapter.notifyDataSetChanged();
	}
	
}
