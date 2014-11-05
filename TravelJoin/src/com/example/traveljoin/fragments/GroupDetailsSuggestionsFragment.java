package com.example.traveljoin.fragments;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.adapters.SuggestionsListAdapter;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.Suggestion;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

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
		ArrayList<Suggestion> groupSuggestions = new ArrayList<Suggestion>();
		groupSuggestions.addAll(group.getPendingSuggestions());

		adapter = new SuggestionsListAdapter(activity, R.layout.suggestion_row_item, groupSuggestions);
//		userList = (ListView) findViewById(R.id.listView);
//		userList.setItemsCanFocus(false);
//		userList.setAdapter(adapter);
		
		getListView().setItemsCanFocus(false);
		setListAdapter(adapter);
		setEmptyText(getString(R.string.group_suggestions_empty_list));
		/**
		 * get on item click listener
		 */
//		getListView().setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View v,
//					final int position, long id) {
//				Log.i("List View Clicked", "**********");
//				Toast.makeText(MainActivity.this,
//						"List View Clicked:" + position, Toast.LENGTH_LONG)
//						.show();
//			}
//		});
	}

	public void refreshList(Group group) {
		if (activity != null)
			initializeAdapterWithData(group);
	}

}
