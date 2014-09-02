package com.example.traveljoin.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.traveljoin.R;
import com.example.traveljoin.models.GeneralListItem;

public class GeneralListItemArrayAdapter extends ArrayAdapter<GeneralListItem> {

	public GeneralListItemArrayAdapter(Context context, ArrayList<GeneralListItem> items) {
		super(context, R.layout.general_list_item, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GeneralListItem item = getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.general_list_item, parent, false);
		}

		TextView nameTextView = (TextView) convertView
				.findViewById(R.id.name);
		TextView descriptionTextView = (TextView) convertView
				.findViewById(R.id.description);

		nameTextView.setText(item.getName());
		descriptionTextView.setText(item.getDescription());

		return convertView;
	}
}