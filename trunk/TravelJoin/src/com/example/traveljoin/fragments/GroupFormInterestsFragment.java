package com.example.traveljoin.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.Interest;

public class GroupFormInterestsFragment extends Fragment {
	
	private ListView interestsListView;
	private ArrayAdapter<Interest> interestsAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_group_form_interests,
				container, false);
		
		GlobalContext globalContext = (GlobalContext) getActivity().getApplicationContext();
		ArrayList<Interest> interests = (ArrayList<Interest>) globalContext.getInterests();

		interestsAdapter = new ArrayAdapter<Interest>(getActivity(),
				android.R.layout.simple_list_item_multiple_choice, interests);
		interestsListView = (ListView) view
				.findViewById(R.id.interestsListView);
		interestsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		interestsListView.setAdapter(interestsAdapter);

		return view;
	}
	
	public ArrayList<Interest> getInterests() {	
		ArrayList<Interest> interests = new ArrayList<Interest>();
		SparseBooleanArray checkedInterests = interestsListView.getCheckedItemPositions();
		for (int index = 0; index < interestsListView.getCount(); index++) {
		    if(checkedInterests.get(index))
		    	interests.add(interestsAdapter.getItem(index));	        
		}
		return interests;
	}
}