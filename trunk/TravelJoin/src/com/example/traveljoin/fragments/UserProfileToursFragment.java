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

public class UserProfileToursFragment extends Fragment {

	private final static int OWN_TOURS_GROUP_KEY = 0;
	private final static int FAVOURITE_TOURS_GROUP_KEY = 1;

	private User user;
	private GeneralItemsGroup ownToursGroup;
	private GeneralItemsGroup favouriteToursGroup;
	private SparseArray<GeneralItemsGroup> groups;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_user_favourites,
				container, false);

		UserProfileActivity activity = (UserProfileActivity) getActivity();
		user = activity.user;
		createItemsGroups();

		ExpandableListView expadableListView = (ExpandableListView) view
				.findViewById(R.id.favouritesExpandableListView);
		GeneralItemListExpandableAdapter adapter = new GeneralItemListExpandableAdapter(
				getActivity(), groups);
		expadableListView.setAdapter(adapter);

		registerForContextMenu(expadableListView);

		return view;
	}

	public void createItemsGroups() {
		ownToursGroup = new GeneralItemsGroup(
				getString(R.string.own_tours_group),
				R.drawable.ic_action_split, user.getOwnTours());
		
		favouriteToursGroup = new GeneralItemsGroup(
				getString(R.string.favourite_tours_group),
				R.drawable.ic_action_important, user.getFavoriteTours());

		groups = new SparseArray<GeneralItemsGroup>();
		groups.put(OWN_TOURS_GROUP_KEY, ownToursGroup);
		groups.put(FAVOURITE_TOURS_GROUP_KEY, favouriteToursGroup);
	}

}