package com.example.traveljoin.fragments;

import java.util.ArrayList;
import java.util.Collection;

import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.activities.TourDetailsActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.GroupTour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class GroupDetailsToursFragment extends ListFragment {
	
	private GroupDetailsActivity activity;
	private ArrayList<GeneralItem> groupTours;
	private GeneralItemListAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = (GroupDetailsActivity) getActivity();

		groupTours = new ArrayList<GeneralItem>();
		groupTours.addAll((Collection<? extends GeneralItem>) activity.group
				.getGroupPois());

		adapter = new GeneralItemListAdapter(getActivity(), groupTours);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		startTourDetailActivity((GroupTour) adapter.getItem(position));
	}
	
	private void startTourDetailActivity(GroupTour selectedGroupTour) {
		Intent intent = new Intent(activity, TourDetailsActivity.class);
		intent.putExtra("tour_id", selectedGroupTour.getTourId()); 
		startActivity(intent);
	}
	
	public void refreshList() {
		groupTours.clear();
		groupTours.addAll((Collection<? extends GeneralItem>) activity.group
				.getGroupTours());
		adapter.notifyDataSetChanged();
	}

}
