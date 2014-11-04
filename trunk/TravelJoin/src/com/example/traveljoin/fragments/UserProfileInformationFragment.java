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
	private TextView ownGroupsView;
	private TextView joinedGroupsView;
	private TextView ownPoisView;
	private TextView favoritePoisView;
	private TextView ownToursView;
	private TextView favoriteToursView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_user_information,
				container, false);

		profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		userNameView = (TextView) view.findViewById(R.id.selection_user_name);
		
		ownGroupsView = (TextView) view.findViewById(R.id.ownGroupsText);
		joinedGroupsView = (TextView) view.findViewById(R.id.joinedGroupsText);
		ownPoisView = (TextView) view.findViewById(R.id.ownPoisText);
		favoritePoisView = (TextView) view.findViewById(R.id.favoritePoisText);
		ownToursView = (TextView) view.findViewById(R.id.ownToursText);
		favoriteToursView = (TextView) view.findViewById(R.id.favoriteToursText);
		
		UserProfileActivity activity = (UserProfileActivity) getActivity();
		fillGeneralUserInformation(activity.user);

		return view;
	}
	
	public void fillGeneralUserInformation(User user) {
		profilePictureView.setProfileId(user.getFacebookId());
		userNameView.setText(user.getFullName());
		
		ownGroupsView.setText(getString(R.string.own_groups)
				+ " " + user.getOwnGroups().size());
		joinedGroupsView.setText(getString(R.string.joined_groups)
				+ " " + user.getGroups().size());
		
		ownPoisView.setText(getString(R.string.own_pois)
				+ " " + user.getOwnPois().size());
		favoritePoisView.setText(getString(R.string.favorite_pois)
				+ " " + user.getFavoritePois().size());
		
		ownToursView.setText(getString(R.string.own_tours)
				+ " " + user.getOwnTours().size());
		favoriteToursView.setText(getString(R.string.favorite_tours)
				+ " " + user.getFavoriteTours().size());	
	}

}