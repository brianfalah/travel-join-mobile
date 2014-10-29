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
	
	private ArrayList<User> members;
	private UsersListAdapter adapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		GroupDetailsActivity activity = (GroupDetailsActivity) getActivity();
		Group group = activity.group;
		
		members = new ArrayList<User>();
		members.addAll((Collection<User>) group
				.getMembers());

		adapter = new UsersListAdapter(getActivity(), members);
		setListAdapter(adapter);
	}
}
