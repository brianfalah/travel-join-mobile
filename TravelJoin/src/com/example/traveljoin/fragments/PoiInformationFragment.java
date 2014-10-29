package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.PoiDetailsActivity;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.User;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class PoiInformationFragment  extends Fragment {
	private TextView tvLatitude;
	private TextView tvLongitude;
	private TextView tvName;
	private TextView tvDesc;
	private TextView tvCategory;
	private TextView tvAddress;
	private ProfilePictureView profilePictureView;
	private TextView userOwnerNameView;
	private TextView  tvRatingAvg;
	private RatingBar ratingBar;
	private ScrollView scrollView;		
	
	PoiDetailsActivity activity;	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_poi_information,
				container, false);

		activity = (PoiDetailsActivity) getActivity();
				
		// get reference to the views
		scrollView = (ScrollView) view.findViewById(R.id.ScrollViewPoiDetails);		
        tvLatitude = (TextView) view.findViewById(R.id.PoiLatitude);
		tvLongitude = (TextView) view.findViewById(R.id.PoiLongitude);
		tvName = (TextView) view.findViewById(R.id.PoiName);
		tvDesc = (TextView) view.findViewById(R.id.PoiDescription);
		tvCategory = (TextView) view.findViewById(R.id.PoiCategory);
		tvAddress = (TextView) view.findViewById(R.id.PoiAddress);
		tvRatingAvg = (TextView) view.findViewById(R.id.ratingAvg);
		ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
		
		profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		userOwnerNameView = (TextView) view.findViewById(R.id.selection_owner_name);
		//initializeOwnerInformation(view);
		
		return view;
	}	
	
	public void setOwnerInformation() {		
		User owner = activity.poi.getUser();
		profilePictureView.setProfileId(owner.getFacebookId());
		userOwnerNameView.setText(owner.getFullName());
	}
	
	public void setFields(){
		LatLng point = new LatLng(activity.poi.getLatitude(), activity.poi.getLongitude());
        setHiddenFields(point);
        tvName.setText(activity.poi.getName());
        tvDesc.setText(activity.poi.getDescription());
        tvAddress.setText(activity.poi.getAddress());
        tvCategory.setText(activity.poi.getCategoryName());
        tvRatingAvg.setText(String.format("%.1f", activity.poi.getRatingAvg()));
        ratingBar.setRating(activity.poi.getRatingForBar());
	}
	
    private void setHiddenFields(LatLng point) {		
		tvLatitude.setText(String.valueOf(point.latitude));
		tvLongitude.setText(String.valueOf(point.longitude));		
	}
    
    public void scrollTop(){
    	 //scrollView.fullScroll(ScrollView.FOCUS_UP);
    	scrollView.smoothScrollTo(0,0);
    }

}
