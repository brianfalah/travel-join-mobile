package com.example.traveljoin.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.traveljoin.R;
import com.example.traveljoin.models.GeneralItem;

public class GeneralItemListAdapter extends BaseAdapter implements Filterable {
	

	private Context context;
    private GeneralItemListFilter generalItemListFilter;
    private ArrayList<GeneralItem> generalItemList;
	private ArrayList<GeneralItem> filteredGeneralItemList;
    
	public GeneralItemListAdapter(Context context, ArrayList<GeneralItem> items) {
		this.context = context;
        this.generalItemList = items;
        this.filteredGeneralItemList = items;
        this.getFilter();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GeneralItem item = (GeneralItem) getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
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

	@Override
	public Filter getFilter() {
		if (generalItemListFilter == null) {
			generalItemListFilter = new GeneralItemListFilter();
        }
        return generalItemListFilter;
	}

	@Override
	public int getCount() {
		return filteredGeneralItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredGeneralItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public Context getContext() {
		return context;
	}
	
	public ArrayList<GeneralItem> getGeneralItemList() {
		return generalItemList;
	}
	
	private class GeneralItemListFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<GeneralItem> tempList = new ArrayList<GeneralItem>();

                for (GeneralItem generalItem : generalItemList) {
                    if (generalItem.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                        tempList.add(generalItem);
                }    
                
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = generalItemList.size();
                filterResults.values = generalItemList;
            }

            return filterResults;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredGeneralItemList = (ArrayList<GeneralItem>) results.values;
            notifyDataSetChanged();
			
		}
		
	}
}