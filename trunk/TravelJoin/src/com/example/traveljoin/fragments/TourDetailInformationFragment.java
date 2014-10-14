package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.TourDetailsActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TourDetailInformationFragment  extends Fragment {
	TextView tvName;
	TextView tvDesc;
	TourDetailsActivity activity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_tour_information,
				container, false);

		activity = (TourDetailsActivity) getActivity();
		// get reference to the views
		tvName = (TextView) view.findViewById(R.id.TourName);
		tvDesc = (TextView) view.findViewById(R.id.TourDescription);
		setFields();       

		return view;
	}
	
	public void setFields(){
        tvName.setText(activity.tour.getName());
        tvDesc.setText(activity.tour.getDescription());
	}	

}