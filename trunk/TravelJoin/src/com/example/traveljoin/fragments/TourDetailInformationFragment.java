package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.TourDetailsActivity;
import com.example.traveljoin.models.Tour;
import com.example.traveljoin.models.User;
import com.facebook.widget.ProfilePictureView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TourDetailInformationFragment  extends Fragment {
	private TextView tvName;
	private TextView tvDesc;
	private Tour tour;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_tour_information,
				container, false);

		TourDetailsActivity activity = (TourDetailsActivity) getActivity();
		tour =  activity.tour;
		
		initializeViewReferences(view);
		setFields();       
		initializeOwnerInformation(view);
		
		return view;
	}

	private void initializeViewReferences(View view) {
		tvName = (TextView) view.findViewById(R.id.TourName);
		tvDesc = (TextView) view.findViewById(R.id.TourDescription);
	}
	
	private void initializeOwnerInformation(View view) {
		ProfilePictureView  profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		TextView userOwnerNameView = (TextView) view.findViewById(R.id.selection_owner_name);

		User owner = tour.getUser();
		profilePictureView.setProfileId(owner.getFacebookId());
		userOwnerNameView.setText(owner.getFullName());
	}
	
	public void setFields(){
        tvName.setText(tour.getName());
        tvDesc.setText(tour.getDescription());
	}	

}