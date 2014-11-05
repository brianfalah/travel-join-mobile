package com.example.traveljoin.fragments;

import java.util.ArrayList;
import java.util.Collection;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GenericUserDetailsActivity;
import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.adapters.UsersListAdapter;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

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
		setEmptyText(getString(R.string.group_members_empty_list));
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		startMemberDetailActivity((User) adapter.getItem(position));
	}

	private void startMemberDetailActivity(User selectedMember) {
		Intent intent = new Intent(activity, GenericUserDetailsActivity.class);
		intent.putExtra("user", selectedMember);
		startActivity(intent);
	}

	public void refreshList(Group group) {
		if(activity != null)
			initializeAdapterWithData(group);	
	}
}
