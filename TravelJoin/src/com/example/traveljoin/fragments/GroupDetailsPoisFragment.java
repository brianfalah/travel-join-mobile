package com.example.traveljoin.fragments;

import java.util.ArrayList;
import java.util.Collection;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.activities.PoiDetailsActivity;

import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.GroupPoi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class GroupDetailsPoisFragment extends ListFragment {

	private GroupDetailsActivity activity;
	private GeneralItemListAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = (GroupDetailsActivity) getActivity();
		Group group = activity.group;

		initializeAdapterWithData(group);
	}

	private void initializeAdapterWithData(Group group) {
		ArrayList<GeneralItem> groupPois = new ArrayList<GeneralItem>();
		groupPois.addAll((Collection<? extends GeneralItem>) group
				.getGroupPois());

		adapter = new GeneralItemListAdapter(getActivity(), groupPois);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		startPoiDetailActivity((GroupPoi) adapter.getItem(position));
	}

	private void startPoiDetailActivity(GroupPoi selectedGroupPoi) {
		Intent intent = new Intent(activity, PoiDetailsActivity.class);
		intent.putExtra("poi_id", selectedGroupPoi.getPoiId());
		startActivity(intent);
	}

	public void refreshList(Group group) {
		if (activity != null)
			initializeAdapterWithData(group);
	}

}
