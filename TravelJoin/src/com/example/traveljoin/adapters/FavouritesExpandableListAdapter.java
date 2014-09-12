package com.example.traveljoin.adapters;

import com.example.traveljoin.R;
import com.example.traveljoin.models.GeneralListItem;
import com.example.traveljoin.models.GroupFavouriteItems;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class FavouritesExpandableListAdapter extends BaseExpandableListAdapter {

	private final SparseArray<GroupFavouriteItems> groups;
	private LayoutInflater inflater;
	private Activity activity;

	public FavouritesExpandableListAdapter(Activity activity,
			SparseArray<GroupFavouriteItems> groups) {
		this.activity = activity;
		this.groups = groups;
		inflater = this.activity.getLayoutInflater();
	}

	@Override
	public GeneralListItem getChild(int groupPosition, int childPosition) {
		return ((GroupFavouriteItems) getGroup(groupPosition)).getItem(childPosition);
	}
	

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final GeneralListItem item = (GeneralListItem) getChild(groupPosition, childPosition);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.general_list_item, parent, false);
		}
		
		TextView nameTextView = (TextView) convertView
				.findViewById(R.id.name);
		TextView descriptionTextView = (TextView) convertView
				.findViewById(R.id.description);

		nameTextView.setText(item.getName());
		descriptionTextView.setText(item.getName());
		
		//TODO: Redireccionar a la actividad correspondiente
		// convertView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(activity, children,
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((GroupFavouriteItems) getGroup(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.general_group_list_item, parent, false);
		}
		GroupFavouriteItems group = (GroupFavouriteItems) getGroup(groupPosition);
		((CheckedTextView) convertView).setText(group.getName());
		((CheckedTextView) convertView).setChecked(isExpanded);
		((CheckedTextView) convertView).setCompoundDrawablesWithIntrinsicBounds(0, group.getDrawableId(), 0, 0);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		//Para el menu contextual de los items tiene que devolver true
		return true;
	}
	
}
