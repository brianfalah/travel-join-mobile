package com.example.traveljoin.adapters;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.GroupDetailsActivity;
import com.example.traveljoin.activities.PoiDetailsActivity;
import com.example.traveljoin.activities.TourDetailsActivity;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.GeneralItemsGroup;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.Tour;

import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class GeneralItemListExpandableAdapter extends BaseExpandableListAdapter {

	private final SparseArray<GeneralItemsGroup> groups;
	private LayoutInflater inflater;
	private Activity activity;

	public GeneralItemListExpandableAdapter(Activity activity,
			SparseArray<GeneralItemsGroup> groups) {
		this.activity = activity;
		this.groups = groups;
		inflater = this.activity.getLayoutInflater();
	}

	@Override
	public GeneralItem getChild(int groupPosition, int childPosition) {
		return ((GeneralItemsGroup) getGroup(groupPosition))
				.getItem(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final GeneralItem item = (GeneralItem) getChild(groupPosition,
				childPosition);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.general_list_item, parent,
					false);
		}

		TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
		TextView descriptionTextView = (TextView) convertView
				.findViewById(R.id.description);

		nameTextView.setText(item.getName());
		descriptionTextView.setText(item.getName());

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(item instanceof Poi) {
					Intent intent = new Intent(activity, PoiDetailsActivity.class);
					intent.putExtra("poi_id", item.getId());
					view.getContext().startActivity(intent);
				}
				if(item instanceof Tour) {
					Intent intent = new Intent(activity, TourDetailsActivity.class);
					intent.putExtra("tour_id", item.getId());
					view.getContext().startActivity(intent);
				}
				if(item instanceof Group) {
					Intent intent = new Intent(activity, GroupDetailsActivity.class);
					intent.putExtra("group", (Group)item);
					view.getContext().startActivity(intent);
				}
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((GeneralItemsGroup) getGroup(groupPosition)).size();
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
			convertView = inflater.inflate(R.layout.general_group_list_item,
					parent, false);
		}
		GeneralItemsGroup group = (GeneralItemsGroup) getGroup(groupPosition);
		((CheckedTextView) convertView).setText(group.getName());
		((CheckedTextView) convertView).setChecked(isExpanded);
		((CheckedTextView) convertView)
				.setCompoundDrawablesWithIntrinsicBounds(0,
						group.getDrawableId(), 0, 0);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// Para el menu contextual de los items tiene que devolver true
		return true;
	}

}
