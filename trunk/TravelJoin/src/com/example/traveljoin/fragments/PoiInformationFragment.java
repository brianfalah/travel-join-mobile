package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.PoiDetailsActivity;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.User;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PoiInformationFragment  extends Fragment {
	private TextView tvLatitude;
	private TextView tvLongitude;
	private TextView tvName;
	private TextView tvDesc;
	private TextView tvCategory;
	private TextView tvAddress;
	private Poi poi;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_poi_information,
				container, false);

		PoiDetailsActivity activity = (PoiDetailsActivity) getActivity();
		poi = activity.poi;
		
        initializeViewReferences(view);
		setFields();       
		initializeOwnerInformation(view);
		
		return view;
	}

	private void initializeViewReferences(View view) {
		tvLatitude = (TextView) view.findViewById(R.id.PoiLatitude);
		tvLongitude = (TextView) view.findViewById(R.id.PoiLongitude);
		tvName = (TextView) view.findViewById(R.id.PoiName);
		tvDesc = (TextView) view.findViewById(R.id.PoiDescription);
		tvCategory = (TextView) view.findViewById(R.id.PoiCategory);
		tvAddress = (TextView) view.findViewById(R.id.PoiAddress);
	}
	
	private void initializeOwnerInformation(View view) {
		ProfilePictureView  profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		TextView userOwnerNameView = (TextView) view.findViewById(R.id.selection_owner_name);

		User owner = poi.getUser();
		profilePictureView.setProfileId(owner.getFacebookId());
		userOwnerNameView.setText(owner.getFullName());
	}
	
	public void setFields(){
		LatLng point = new LatLng(poi.getLatitude(), poi.getLongitude());
        setHiddenFields(point);
        tvName.setText(poi.getName());
        tvDesc.setText(poi.getDescription());
        tvAddress.setText(poi.getAddress());
        tvCategory.setText(poi.getCategoryName());
	}
	
    private void setHiddenFields(LatLng point) {		
		tvLatitude.setText(String.valueOf(point.latitude));
		tvLongitude.setText(String.valueOf(point.longitude));		
	}

}
