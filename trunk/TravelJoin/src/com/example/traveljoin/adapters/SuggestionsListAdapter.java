package com.example.traveljoin.adapters;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.models.Suggestion;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestionsListAdapter extends ArrayAdapter<Suggestion> {
	Context context;
	int layoutResourceId;
	ArrayList<Suggestion> data = new ArrayList<Suggestion>();

	public SuggestionsListAdapter(Context context, int layoutResourceId,
			ArrayList<Suggestion> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SuggestionHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new SuggestionHolder();
			holder.textName = (TextView) row.findViewById(R.id.suggestionableName);
			holder.textDescription = (TextView) row.findViewById(R.id.suggestionableDescription);
			holder.textType = (TextView) row.findViewById(R.id.suggestionableType);
			holder.btnAccept = (Button) row.findViewById(R.id.btnAcceptSuggestion);
			holder.btnReject = (Button) row.findViewById(R.id.btnRejectSuggestion);
			row.setTag(holder);
		} else {
			holder = (SuggestionHolder) row.getTag();
		}
		final Suggestion suggestion = data.get(position);
		holder.textName.setText(suggestion.getSuggestionableName());
		holder.textDescription.setText(suggestion.getSuggestionableDescription());
		holder.textType.setText(suggestion.getSuggestionableTypeDescription());
		holder.btnAccept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Suggestion suggestion = data.get(position);
				((GroupDetailsActivity)context).acceptSuggestion(suggestion);
			}
		});
		holder.btnReject.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((GroupDetailsActivity)context).rejectSuggestion(suggestion);
			}
		});
		return row;

	}

	static class SuggestionHolder {
		TextView textName;
		TextView textDescription;
		TextView textType;
		Button btnAccept;
		Button btnReject;
	}
}
