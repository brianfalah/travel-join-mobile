package com.example.traveljoin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.UserProfileActivity;
import com.example.traveljoin.models.User;
import com.facebook.widget.ProfilePictureView;

public class UserProfileInformationFragment extends Fragment {

	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_user_information,
				container, false);

		profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		userNameView = (TextView) view.findViewById(R.id.selection_user_name);
		
		UserProfileActivity activity = (UserProfileActivity) getActivity();
		User user = activity.user;
		
		profilePictureView.setProfileId(user.getFacebookId());
		userNameView.setText(user.getFullName());

		return view;
	}

}