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
import android.view.View;
import android.widget.ListView;

public class PoiEventsFragment extends ListFragment {

	PoiDetailsActivity activity;
	private ArrayList<GeneralItem> events;
	GeneralItemListAdapter adapter;
//	ListView listView;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//
//		View view = inflater.inflate(R.layout.fragment_general_list,
//				container, false);
//		listView = (ListView) view.findViewById(R.id.list);
//		return view;
//	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = (PoiDetailsActivity) getActivity();              
				
		events = new ArrayList<GeneralItem>();

		adapter = new GeneralItemListAdapter(
				getActivity(), events);
		setListAdapter(adapter);
		setEmptyText(getString(R.string.poi_events_empty_list));
	}

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
	 
	public void refreshList(){
		events.clear();
		events.addAll(activity.poi.getPoiEvents());
		adapter.notifyDataSetChanged();
	}
	
}
