package com.example.traveljoin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.UserProfileActivity;
import com.example.traveljoin.adapters.GeneralItemListExpandableAdapter;
import com.example.traveljoin.models.GeneralItemsGroup;
import com.example.traveljoin.models.User;

public class UserProfileGroupsFragment extends Fragment {

	private final static int OWN_GROUPS_GROUP_KEY = 0;
	private final static int JOINED_GROUPS_GROUP_KEY = 1;

	private GeneralItemsGroup ownGroupsGroup;
	private GeneralItemsGroup joinedGroupsGroup;
	private SparseArray<GeneralItemsGroup> groups;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_user_favourites, container,
				false);
		
		UserProfileActivity activity = (UserProfileActivity) getActivity();
		initializeGroupedList(activity.user);

		return view;
	}

	public void initializeGroupedList(User user) {
		try {
			createItemsGroups(user);
			ExpandableListView expadableListView = (ExpandableListView) view
					.findViewById(R.id.favouritesExpandableListView);
			GeneralItemListExpandableAdapter adapter = new GeneralItemListExpandableAdapter(
					getActivity(), groups);
			expadableListView.setAdapter(adapter);

			registerForContextMenu(expadableListView);
		} catch (IllegalStateException e) {
			// Do nothing
		}
	}

	public void createItemsGroups(User user) {
		ownGroupsGroup = new GeneralItemsGroup(
				getString(R.string.own_groups_group),
				R.drawable.ic_action_group, user.getOwnGroups());

		joinedGroupsGroup = new GeneralItemsGroup(
				getString(R.string.joined_groups_group),
				R.drawable.ic_action_important, user.getGroups());

		groups = new SparseArray<GeneralItemsGroup>();
		groups.put(OWN_GROUPS_GROUP_KEY, ownGroupsGroup);
		groups.put(JOINED_GROUPS_GROUP_KEY, joinedGroupsGroup);
	}

}