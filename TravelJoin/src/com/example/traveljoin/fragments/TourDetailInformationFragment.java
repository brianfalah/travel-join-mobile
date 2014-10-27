package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.TourDetailsActivity;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.User;
import com.facebook.widget.ProfilePictureView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TourDetailInformationFragment  extends Fragment {
	TextView tvName;
	TextView tvDesc;
	private ProfilePictureView profilePictureView;
	private TextView userOwnerNameView;
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
		profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		userOwnerNameView = (TextView) view.findViewById(R.id.selection_owner_name);
		
		return view;
	}
	
	public void setOwnerInformation() {		
		User owner = activity.tour.getUser();
		profilePictureView.setProfileId(owner.getFacebookId());
		userOwnerNameView.setText(owner.getFullName());
	}
	
	public void setFields(){
        tvName.setText(activity.tour.getName());
        tvDesc.setText(activity.tour.getDescription());
	}	

}