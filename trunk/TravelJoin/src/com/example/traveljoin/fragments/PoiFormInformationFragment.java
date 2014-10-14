package com.example.traveljoin.fragments;

import java.util.List;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.PoiFormActivity;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.Category;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PoiFormInformationFragment extends Fragment {
	PoiFormActivity activity;
	public TextView tvLatitude;
	public TextView tvLongitude;
	public EditText nameField;
	public EditText descField;
	public EditText addressField;
	public Spinner poiCategoriesSpinnerField;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_poi_form_information,
				container, false);

		activity = (PoiFormActivity) getActivity();
		initializeViewReferences(view);

		if (activity.poi != null) {
			setHiddenFields(new LatLng(activity.poi.getLatitude(),
					activity.poi.getLongitude()));
			setFields();
		} else {
			setHiddenFields(activity.point);
		}

		return view;
	}

	private void initializeViewReferences(View view) {
		tvLatitude = (TextView) view.findViewById(R.id.PoiLatitude);
		tvLongitude = (TextView) view.findViewById(R.id.PoiLongitude);
		nameField = (EditText) view.findViewById(R.id.PoiName);
		addressField = (EditText) view.findViewById(R.id.PoiAddress);
		descField = (EditText) view.findViewById(R.id.PoiDescription);
		poiCategoriesSpinnerField = (Spinner) view
				.findViewById(R.id.PoiCategories);
		initializePoiCategoriesSpinner(view);
	}

	public void setFields() {
		nameField.setText(activity.poi.getName());
		descField.setText(activity.poi.getDescription());
		addressField.setText(activity.poi.getAddress());
		poiCategoriesSpinnerField.setSelection(getIndex(
				poiCategoriesSpinnerField, activity.poi.getCategoryId()));
	}

	private int getIndex(Spinner spinner, Integer category_id) {
		int index = 0;

		for (int i = 0; i < spinner.getCount(); i++) {
			if (((Category) spinner.getItemAtPosition(i)).getId().equals(
					category_id)) {
				index = i;
				i = spinner.getCount();// will stop the loop, kind of break, by
										// making condition false
			}
		}
		return index;
	}

	private void initializePoiCategoriesSpinner(View view) {
		GlobalContext globalContext = (GlobalContext) activity
				.getApplicationContext();
		List<Category> categories = globalContext.getCategories();
		ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(
				activity, android.R.layout.simple_spinner_item, categories);
		categoryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		poiCategoriesSpinnerField = (Spinner) view
				.findViewById(R.id.PoiCategories);
		poiCategoriesSpinnerField.setAdapter(categoryAdapter);
	}

	public void setHiddenFields(LatLng point) {
		tvLatitude.setText(String.valueOf(point.latitude));
		tvLongitude.setText(String.valueOf(point.longitude));
	}

	public Boolean validateFields() {
		return validateField(nameField) && validateField(addressField)
				&& validateField(descField)
				&& validateField(poiCategoriesSpinnerField);
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
		} else {
			if (field instanceof Spinner) {
				Spinner spinner_field = (Spinner) field;
				if (TextUtils.isEmpty(((Category) spinner_field
						.getSelectedItem()).getName())) {
					Toast.makeText(
							activity,
							spinner_field.getPrompt()
									+ getString(R.string.append_required),
							Toast.LENGTH_SHORT).show();
					valid = false;
				} else {
					valid = true;
				}
			}
		}
		return valid;
	}

}
