package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupFormActivity;
import com.example.traveljoin.activities.TourFormActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class GroupFormInformationFragment extends Fragment {

	private static Integer PUBLIC = 0;
	private static Integer PRIVATE = 1;
	public EditText groupNameField;
	public EditText groupDescriptionField;
	public RadioGroup radioGroup;
	public EditText passwordField;
	GroupFormActivity groupFormActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_group_form_information,
				container, false);
		
		groupFormActivity = (GroupFormActivity) getActivity();
		
		groupNameField = (EditText) view
				.findViewById(R.id.groupName);
		groupDescriptionField = (EditText) view
				.findViewById(R.id.groupDescription);
		radioGroup = (RadioGroup) view
				.findViewById(R.id.radio_group_type);
		passwordField = (EditText) view
				.findViewById(R.id.private_group_password);
		passwordField.setEnabled(false);
		passwordField.setVisibility(View.GONE);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_public_group:
					// TODO
					passwordField.setEnabled(false);
					passwordField.setVisibility(View.GONE);
				case R.id.radio_private_group:
					// TODO
					passwordField.setEnabled(true);
					passwordField.setVisibility(View.VISIBLE);
					break;
				}

			}
		});
		
		if (groupFormActivity.group != null){
			setFields();
		}

		return view;
	}
	
	public void setFields(){
		groupNameField.setText(groupFormActivity.group.getName());
		groupDescriptionField.setText(groupFormActivity.group.getDescription());
		passwordField.setText(groupFormActivity.group.getPassword());
	}
	
	public Boolean validateFields() {
		return validateField(groupNameField) && validateField(groupDescriptionField);
	}

	public Boolean validateField(View field) {
		Boolean valid = null;
		if (field instanceof EditText) {
			EditText edit_text_field = (EditText) field;
			if (TextUtils.isEmpty(edit_text_field.getText().toString())) {
				edit_text_field.requestFocus();
				edit_text_field.setError(edit_text_field.getHint()
						+ getString(R.string.append_required));
				valid = false;
			} else {
				edit_text_field.setError(null);
				valid = true;
			}
		} 
		return valid;
	}
	
	public String getGroupName() {
		return groupNameField.getText().toString();
	}
	
	public String getGroupDescription() {
		return groupDescriptionField.getText().toString();
	}
	
	public Integer getGroupType() {
		Integer groupType = null;
		switch (radioGroup.getCheckedRadioButtonId()) {
		case R.id.radio_public_group:
			groupType = PUBLIC;
			break;
		case R.id.radio_private_group:
			groupType = PRIVATE;
			break;
		}
		return groupType;
	}
	
	public String getPassword() {
		return passwordField.getText().toString();
	}

}
