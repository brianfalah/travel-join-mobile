package com.example.traveljoin.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.traveljoin.R;
import com.example.traveljoin.models.Interest;

public class GroupFormInterestsFragment extends Fragment {
	
	private ListView interestsListView;
	private ArrayList<Interest> interests;
	private ArrayAdapter<Interest> interestsAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_group_form_interests,
				container, false);

		interests = new ArrayList<Interest>();

		interests.add(new Interest(1, "Interes 1"));
		interests.add(new Interest(2, "Interes 2"));
		interests.add(new Interest(3, "Interes 3"));
		interests.add(new Interest(4, "Interes 4"));
		interests.add(new Interest(5, "Interes 5"));
		interests.add(new Interest(6, "Interes 6"));
		interests.add(new Interest(7, "Interes 7"));
		interests.add(new Interest(8, "Interes 8"));

		interestsAdapter = new ArrayAdapter<Interest>(getActivity(),
				android.R.layout.simple_list_item_multiple_choice, interests);
		interestsListView = (ListView) view
				.findViewById(R.id.interestsListView);
		interestsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		interestsListView.setAdapter(interestsAdapter);

		return view;
	}
}