package com.example.traveljoin.fragments;

import com.NYXDigital.NiceSupportMapFragment;
import com.example.traveljoin.models.Poi;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMapFragement extends NiceSupportMapFragment {	 

	public Marker placeMarker(Poi poi) {

		Marker m  = getMap().addMarker (
				new MarkerOptions()
				.position(new LatLng(poi.getLatitude(), poi.getLongitude()))
				.title(poi.getName())
				.snippet(poi.getDescription())
				);  

		return m;

	}

}
