package com.example.traveljoin.fragments;

import java.util.ArrayList;
import java.util.Collection;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.TourDetailsActivity;
import com.example.traveljoin.adapters.RatingsListAdapter;
import com.example.traveljoin.auxiliaries.Helper;
import com.example.traveljoin.models.Rating;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class TourInformationRatingsFragment extends ListFragment {
	
	private TourDetailsActivity activity;
	private ArrayList<Rating> ratings;
	private RatingsListAdapter adapter;
	ListView listView;

	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = (View) inflater.inflate(R.layout.ratings_list,
				container, false);
		
		activity = (TourDetailsActivity) getActivity();
		View ratingsListLayout = view.findViewById(R.id.ratingsListLayout); 
		listView = (ListView) ratingsListLayout.findViewWithTag("ratingsListView");

		ratings = new ArrayList<Rating>();
		ratings.addAll((Collection<Rating>) activity.tour.getLastRatings());

		adapter = new RatingsListAdapter(getActivity(), ratings);
		listView.setAdapter(adapter);
//		setListAdapter(adapter);
		Helper.getListViewSize(listView);
		
		return view;
	}	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
	}
}
