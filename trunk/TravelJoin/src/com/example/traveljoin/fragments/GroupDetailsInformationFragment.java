package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.User;
import com.facebook.widget.ProfilePictureView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroupDetailsInformationFragment extends Fragment {
	private TextView nameField;
	private TextView descriptionField;
	private TextView groupTypeField;
	private Group group;

	private static Integer PUBLIC_GROUP = 0;
	private static Integer PRIVATE_GROUP = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_group_information,
				container, false);

		GroupDetailsActivity activity = (GroupDetailsActivity) getActivity();
		group = activity.group;
		initializeViewReferences(view);
		initializeOwnerInformation(view);

		if (activity.group != null)
			setFields();

		return view;
	}

	private void initializeViewReferences(View view) {
		nameField = (TextView) view.findViewById(R.id.groupName);
		descriptionField = (TextView) view.findViewById(R.id.groupDescription);
		groupTypeField = (TextView) view.findViewById(R.id.groupType);
	}

	private void initializeOwnerInformation(View view) {
		ProfilePictureView profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		TextView userOwnerNameView = (TextView) view
				.findViewById(R.id.selection_owner_name);

		User owner = group.getOwner();
		profilePictureView.setProfileId(owner.getFacebookId());
		userOwnerNameView.setText(owner.getFullName());
	}

	public void setFields() {
		nameField.setText(group.getName());
		descriptionField.setText(group.getDescription());

		Integer groupType = group.getType();
		if (groupType.equals(PUBLIC_GROUP))
			groupTypeField.setText(getString(R.string.group_public));
		else if (groupType.equals(PRIVATE_GROUP))
			groupTypeField.setText(getString(R.string.group_private));

	}

}
