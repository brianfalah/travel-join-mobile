package com.example.traveljoin.adapters;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.auxiliaries.CheckeableItem;

import android.util.SparseArray;
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

	private GeneralItemListAdapter wrappedAdapter;
	private SparseArray<CheckeableItem> checkedItems = new SparseArray<CheckeableItem>();

	public GeneralItemCheckeableListAdapter(
			GeneralItemListAdapter wrappedAdapter) {
		this(wrappedAdapter, new ArrayList<GeneralItem>());
	}

	public GeneralItemCheckeableListAdapter(
			GeneralItemListAdapter wrappedAdapter,
			ArrayList<GeneralItem> alreadySelectedItems) {
		this.wrappedAdapter = wrappedAdapter;
		ArrayList<GeneralItem> items = this.wrappedAdapter.getGeneralItemList(); 
		
		initializeCheckeableItems(alreadySelectedItems, items);
	}

	private void initializeCheckeableItems(
			ArrayList<GeneralItem> alreadySelectedItems,
			ArrayList<GeneralItem> items) {
		GeneralItem item;
		CheckeableItem checkeableItem;
		for (int index = 0; index < items.size(); index++) {
			item = items.get(index);
			if(alreadySelectedItems.contains(item))
				checkeableItem = new CheckeableItem(item, true);
			else
				checkeableItem = new CheckeableItem(item, false);
				
			checkedItems.put(index, checkeableItem);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return wrappedAdapter.getCount();
	}

	@Override
	public Object getItem(int position) {
		return wrappedAdapter.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return wrappedAdapter.getItemId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GeneralItem item = (GeneralItem) getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(wrappedAdapter.getContext())
					.inflate(R.layout.general_list_item_checkeable, parent,
							false);
		}

		CheckedTextView nameCheckedTextView = (CheckedTextView) convertView.findViewById(R.id.name);
		TextView descriptionTextView = (TextView) convertView
				.findViewById(R.id.description);
		

		nameCheckedTextView.setText(item.getName());
		nameCheckedTextView.setChecked(checkedItems.get(position).isChecked());
		descriptionTextView.setText(item.getDescription());

		return convertView;
	}

	@Override
	public Filter getFilter() {
		return wrappedAdapter.getFilter();
	}

	public ArrayList<GeneralItem> getSelectedItems() {
		ArrayList<GeneralItem> selectedItems = new ArrayList<GeneralItem>();

		CheckeableItem chekeableItem;
		for (int index = 0; index < checkedItems.size(); index++) {
			chekeableItem = checkedItems.get(index); 
			if (chekeableItem.isChecked())
				selectedItems.add(chekeableItem.getItem());
		}
		return selectedItems;
	}

	public void toggleChecked(int position) {
		CheckeableItem checkeableItem = checkedItems.get(position);
		if (checkeableItem.isChecked())
			checkeableItem.setChecked(false);
		else
			checkeableItem.setChecked(true);

		notifyDataSetChanged();
	}

}
