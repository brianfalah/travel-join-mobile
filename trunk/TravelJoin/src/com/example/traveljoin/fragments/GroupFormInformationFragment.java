package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupFormActivity;

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
	private EditText groupNameField;
	private EditText groupDescriptionField;
	private RadioGroup radioGroup;
	private EditText groupPasswordField;
	private GroupFormActivity groupFormActivity;
	
    
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
		groupPasswordField = (EditText) view
				.findViewById(R.id.private_group_password);
		groupPasswordField.setEnabled(false);
		groupPasswordField.setVisibility(View.GONE);
		
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_public_group:
					// TODO
					groupPasswordField.setEnabled(false);
					groupPasswordField.setVisibility(View.GONE);
					break;
				case R.id.radio_private_group:
					// TODO
					groupPasswordField.setEnabled(true);
					groupPasswordField.setVisibility(View.VISIBLE);
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
		if (groupFormActivity.group.getType().equals(PUBLIC)){
			radioGroup.check(R.id.radio_public_group);
		}
		else{
			radioGroup.check(R.id.radio_private_group);
			groupPasswordField.setText(groupFormActivity.group.getPassword());
		}
		
	}
	
	public Boolean validateFields() {
		if (getGroupType().equals(PUBLIC)){
			return validateField(groupNameField) && validateField(groupDescriptionField);
		}
		else{
			return validateField(groupNameField) && validateField(groupDescriptionField) && validateField(groupPasswordField);
		}
		
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
		return groupPasswordField.getText().toString();
	}

}
