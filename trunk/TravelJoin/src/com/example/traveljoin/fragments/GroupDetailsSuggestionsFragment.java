package com.example.traveljoin.fragments;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.activities.PoiDetailsActivity;
import com.example.traveljoin.activities.TourDetailsActivity;
import com.example.traveljoin.adapters.SuggestionsListAdapter;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.Suggestion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class GroupDetailsSuggestionsFragment extends ListFragment {
	private GroupDetailsActivity activity;
	private SuggestionsListAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = (GroupDetailsActivity) getActivity();
		Group group = activity.group;

		initializeAdapterWithData(group);
	}

	private void initializeAdapterWithData(Group group) {
		final ArrayList<Suggestion> groupSuggestions = new ArrayList<Suggestion>();
		groupSuggestions.addAll(group.getPendingSuggestions());

		adapter = new SuggestionsListAdapter(activity, R.layout.suggestion_row_item, groupSuggestions);
		
		getListView().setItemsCanFocus(false);
		setListAdapter(adapter);
		setEmptyText(getString(R.string.group_suggestions_empty_list));
		/**
		 * get on item click listener
		 */
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long id) {
				final Suggestion suggestion = groupSuggestions.get(position);
				Intent intent = null;
				if (suggestion.getSuggestionableType().equals("Poi")){
					intent = new Intent(activity, PoiDetailsActivity.class);
					intent.putExtra("poi_id", suggestion.getSuggestionableId());
				}
				else{
					if (suggestion.getSuggestionableType().equals("Tour")){
						intent = new Intent(activity, TourDetailsActivity.class);
						intent.putExtra("tour_id", suggestion.getSuggestionableId());
					}						
				}
				
				startActivity(intent);
			}
		});
	}

	public void refreshList(Group group) {
		if (activity != null)
			initializeAdapterWithData(group);
	}

}
