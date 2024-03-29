package com.example.traveljoin.fragments;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Group;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class GroupDetailsInterestsFragment extends ListFragment {

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
		ArrayList<GeneralItem> groupInterests = new ArrayList<GeneralItem>();
		groupInterests.addAll(group.getGroupInterests());

		adapter = new GeneralItemListAdapter(getActivity(), groupInterests);
		setListAdapter(adapter);
		setEmptyText(getString(R.string.group_interests_empty_list));
	}

	public void refreshList(Group group) {
		if (activity != null)
			initializeAdapterWithData(group);
	}
}
