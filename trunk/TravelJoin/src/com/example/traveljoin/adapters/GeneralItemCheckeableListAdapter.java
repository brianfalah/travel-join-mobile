package com.example.traveljoin.adapters;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.activities.PoisSelectorActivity;
import com.example.traveljoin.auxiliaries.CheckableLinearLayout;
import com.example.traveljoin.auxiliaries.CheckeableItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class GeneralItemCheckeableListAdapter extends BaseAdapter implements
		Filterable {

	private Context context;
	private GeneralCheckeableItemListFilter generalcheckeableItemListFilter;
	private ArrayList<CheckeableItem> checkeableItems;
	private ArrayList<CheckeableItem> filteredCheckeableItems;

	public GeneralItemCheckeableListAdapter(Context context,
			ArrayList<GeneralItem> items,
			ArrayList<GeneralItem> previousSelectedItems) {
		this.context = context;
		this.initializeCheckeableItems(items, previousSelectedItems);
		this.getFilter();
	}

	private void initializeCheckeableItems(ArrayList<GeneralItem> items,
			ArrayList<GeneralItem> previousSelectedItems) {

		this.checkeableItems = new ArrayList<CheckeableItem>();
		this.filteredCheckeableItems = new ArrayList<CheckeableItem>();

		CheckeableItem checkeableItem;

		for (GeneralItem item : items) {
			if (previousSelectedItems.contains(item))
				checkeableItem = new CheckeableItem(item, true);
			else
				checkeableItem = new CheckeableItem(item, false);

			checkeableItems.add(checkeableItem);
			filteredCheckeableItems.add(checkeableItem);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return filteredCheckeableItems.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredCheckeableItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public Context getContext() {
		return context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckeableItem checkeableItem = (CheckeableItem) getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.general_list_item_checkeable, parent, false);
		}

		CheckedTextView nameCheckedTextView = (CheckedTextView) convertView
				.findViewById(R.id.name);
		TextView descriptionTextView = (TextView) convertView
				.findViewById(R.id.description);

		nameCheckedTextView.setText(checkeableItem.getName());
		nameCheckedTextView.setChecked(checkeableItems.get(position)
				.isChecked());
		descriptionTextView.setText(checkeableItem.getDescription());

		return convertView;
	}

	@Override
	public Filter getFilter() {
		if (generalcheckeableItemListFilter == null) {
			generalcheckeableItemListFilter = new GeneralCheckeableItemListFilter();
		}
		return generalcheckeableItemListFilter;
	}

	public ArrayList<GeneralItem> getSelectedItems() {
		ArrayList<GeneralItem> selectedItems = new ArrayList<GeneralItem>();

		for (CheckeableItem chekeableItem : checkeableItems) {
			if (chekeableItem.isChecked())
				selectedItems.add(chekeableItem.getItem());
		}
		return selectedItems;
	}

	public void toggleChecked(int position) {
		CheckeableItem checkeableItem = checkeableItems.get(position);
		if (checkeableItem.isChecked())
			checkeableItem.setChecked(false);
		else
			checkeableItem.setChecked(true);
	}

	private class GeneralCheckeableItemListFilter extends Filter {

		@SuppressLint("DefaultLocale")
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			if (constraint != null && constraint.length() > 0) {
				ArrayList<CheckeableItem> tempList = new ArrayList<CheckeableItem>();

				for (CheckeableItem checkeableItem : checkeableItems) {
					if (checkeableItem.getName().toLowerCase()
							.contains(constraint.toString().toLowerCase()))
						tempList.add(checkeableItem);
				}

				filterResults.count = tempList.size();
				filterResults.values = tempList;
			} else {
				filterResults.count = checkeableItems.size();
				filterResults.values = checkeableItems;
			}

			return filterResults;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredCheckeableItems = (ArrayList<CheckeableItem>) results.values;
			PoisSelectorActivity activity = (PoisSelectorActivity) context;

			for (int index = 0; index < activity.getListView().getChildCount(); index++) {
				CheckableLinearLayout view = (CheckableLinearLayout) activity
						.getListView().getChildAt(index);

				view.setChecked(false);

				// CheckedTextView checkedTextView = (CheckedTextView)
				// activity.getListView(). .findViewById(R.id.name);
				// checkedTextView.setChecked(false);
			}

			// CheckedTextView checkedTextView = (CheckedTextView)
			// activity.getListView(). .findViewById(R.id.name);
			// checkedTextView.setChecked(false);
			// }

			//activity.getListView().clearChoices();
			notifyDataSetChanged();
		}
	}

}
