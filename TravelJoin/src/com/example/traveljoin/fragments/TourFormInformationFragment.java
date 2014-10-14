package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.TourFormActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class TourFormInformationFragment extends Fragment {
	
	TourFormActivity tourFormActivity;
	public EditText nameField;
	public EditText descField;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_tour_form_information,
				container, false);
		
		tourFormActivity = (TourFormActivity) getActivity();
		initializeViewReferences(view);
		
		if (tourFormActivity.tour != null){
			setFields();
		}

		return view;
	}

	private void initializeViewReferences(View view) {
		nameField = (EditText) view.findViewById(R.id.TourName);
		descField = (EditText) view.findViewById(R.id.TourDescription);
	}
	
	public void setFields(){
		nameField.setText(tourFormActivity.tour.getName());
		descField.setText(tourFormActivity.tour.getDescription());
	}
	
	public Boolean validateFields() {
		return validateField(nameField) && validateField(descField);
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

}
