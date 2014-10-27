package com.example.traveljoin.fragments;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Group;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GroupDeatailsInterestFragment extends ListFragment {
	
	private Group group;
	private ArrayList<GeneralItem> groupInterests;
	private GroupDetailsActivity activity;
	private GeneralItemListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_general_list,
				container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = (GroupDetailsActivity) getActivity();              
		group = activity.group;	
		
		groupInterests = new ArrayList<GeneralItem>();
		groupInterests.addAll(group.getGroupInterests());

		adapter = new GeneralItemListAdapter(
				getActivity(), groupInterests);
		setListAdapter(adapter);
	}
}
