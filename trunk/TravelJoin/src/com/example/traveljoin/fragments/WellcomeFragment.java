package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.MapActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WellcomeFragment extends Fragment {

	public WellcomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_welcome, container,
				false);

		Button button_explore = (Button) view.findViewById(R.id.explore_button);
		button_explore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), MapActivity.class);
				startActivity(intent);
			}
		});

		return view;
	}

}
