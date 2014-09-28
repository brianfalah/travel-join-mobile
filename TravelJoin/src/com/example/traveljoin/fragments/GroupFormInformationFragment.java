package com.example.traveljoin.fragments;

import com.example.traveljoin.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class GroupFormInformationFragment extends Fragment {

	private EditText passwordField;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_group_form_information,
				container, false);

		RadioGroup radioGroup = (RadioGroup) view
				.findViewById(R.id.radio_group_type);

		passwordField = (EditText) view
				.findViewById(R.id.private_group_password);
		passwordField.setEnabled(false);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_public_group:
					// TODO
					passwordField.setEnabled(false);
					Toast.makeText(getActivity(), "Publico", Toast.LENGTH_SHORT)
							.show();
					break;
				case R.id.radio_private_group:
					// TODO
					passwordField.setEnabled(true);
					Toast.makeText(getActivity(), "Privado", Toast.LENGTH_SHORT)
							.show();
					break;
				}

			}
		});

		return view;
	}

}
