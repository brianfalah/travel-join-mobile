package com.example.traveljoin.fragments;

import java.util.ArrayList;
import java.util.Collection;

import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.adapters.UsersListAdapter;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.User;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class GroupDetailsMembersFragment extends ListFragment {
	
	private GroupDetailsActivity activity;
	private ArrayList<User> members;
	private UsersListAdapter adapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		activity = (GroupDetailsActivity) getActivity();
		Group group = activity.group;
		
		initializeAdapterWithData(group);
	}

	private void initializeAdapterWithData(Group group) {
		members = new ArrayList<User>();
		members.addAll((Collection<User>) group
				.getMembers());
		getActivity();
		adapter = new UsersListAdapter(activity, members);
		setListAdapter(adapter);
	}

	public void refreshList(Group group) {
		if(activity != null)
			initializeAdapterWithData(group);	
	}
}
