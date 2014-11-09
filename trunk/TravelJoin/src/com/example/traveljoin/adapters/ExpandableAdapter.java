package com.example.traveljoin.adapters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.DataHolder;
import com.example.traveljoin.auxiliaries.CheckeableItem;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableAdapter extends BaseExpandableListAdapter {

	private LayoutInflater layoutInflater;
	private LinkedHashMap<CheckeableItem, ArrayList<CheckeableItem>> groupList;
	private ArrayList<CheckeableItem> mainGroup;
	private int[] groupStatus;

	public ExpandableAdapter(Context context, ExpandableListView listView,
			LinkedHashMap<CheckeableItem, ArrayList<CheckeableItem>> groupsList) {
		layoutInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		this.groupList = groupsList;
		groupStatus = new int[groupsList.size()];

		listView.setOnGroupExpandListener(new OnGroupExpandListener() {

			public void onGroupExpand(int groupPosition) {
				CheckeableItem group = mainGroup.get(groupPosition);
				if (groupList.get(group).size() > 0)
					groupStatus[groupPosition] = 1;

			}
		});

		listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			public void onGroupCollapse(int groupPosition) {
				CheckeableItem group = mainGroup.get(groupPosition);
				if (groupList.get(group).size() > 0)
					groupStatus[groupPosition] = 0;

			}
		});

		mainGroup = new ArrayList<CheckeableItem>();
		for (Map.Entry<CheckeableItem, ArrayList<CheckeableItem>> mapEntry : groupList.entrySet()) {
			mainGroup.add(mapEntry.getKey());
		}

	}

	public CheckeableItem getChild(int groupPosition, int childPosition) {
		CheckeableItem item = mainGroup.get(groupPosition);
		return groupList.get(item).get(childPosition);

	}

	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final ChildHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.group_item, null);
			holder = new ChildHolder();
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}
		final CheckeableItem child = getChild(groupPosition, childPosition);
		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				CheckeableItem parentGroup = getGroup(groupPosition);
				child.setChecked(isChecked);
				if (isChecked) {
					// child.isChecked =true;
					ArrayList<CheckeableItem> childList = getChild(parentGroup);
					int childIndex = childList.indexOf(child);
					boolean isAllChildClicked = true;
					for (int i = 0; i < childList.size(); i++) {
						if (i != childIndex) {
							CheckeableItem siblings = childList.get(i);
							if (!siblings.isChecked()) {
								isAllChildClicked = false;
								//if(DataHolder.checkedChilds.containsKey(child.name)==false){
									DataHolder.checkedChilds.put(child.getName(),
											parentGroup.getName());
							//	}
								break;
							}
						}
					}

					if (isAllChildClicked) {
						parentGroup.setChecked(true);
						if(!(DataHolder.checkedChilds.containsKey(child.getName())==true)){
							DataHolder.checkedChilds.put(child.getName(),
									parentGroup.getName());
						}
						checkAll = false;
					}

				} else {
					if (parentGroup.isChecked()) {
						parentGroup.setChecked(false);
						checkAll = false;
						DataHolder.checkedChilds.remove(child.getName());
					} else {
						checkAll = true;
						DataHolder.checkedChilds.remove(child.getName());
					}
					// child.isChecked =false;
				}
				notifyDataSetChanged();

			}

		});

		holder.cb.setChecked(child.isChecked());
		holder.title.setText(child.getName());
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		CheckeableItem item = mainGroup.get(groupPosition);
		return groupList.get(item).size();
	}

	public CheckeableItem getGroup(int groupPosition) {
		return mainGroup.get(groupPosition);
	}

	public int getGroupCount() {
		return mainGroup.size();
	}

	public long getGroupId(int groupPosition) {
		return 0;
	}

	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		final GroupHolder holder;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.group_list, null);
			holder = new GroupHolder();
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.label_indicator);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (GroupHolder) convertView.getTag();
		}

		holder.imageView
				.setImageResource(groupStatus[groupPosition] == 0 ? R.drawable.ic_action_expand
						: R.drawable.ic_action_collapse);
		final CheckeableItem groupItem = getGroup(groupPosition);

		holder.title.setText(groupItem.getName());

		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (checkAll) {
					ArrayList<CheckeableItem> childItem = getChild(groupItem);
					for (CheckeableItem children : childItem)
						children.setChecked(isChecked);
				}
				groupItem.setChecked(isChecked);
				notifyDataSetChanged();
				new Handler().postDelayed(new Runnable() {

					public void run() {
						if (!checkAll)
							checkAll = true;
					}
				}, 50);

			}

		});
		holder.cb.setChecked(groupItem.isChecked());
		return convertView;
	}

	private boolean checkAll = true;

	public ArrayList<CheckeableItem> getChild(CheckeableItem group) {
		return groupList.get(group);
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private class GroupHolder {
		public ImageView imageView;
		public CheckBox cb;
		public TextView title;

	}

	private class ChildHolder {
		public TextView title;
		public CheckBox cb;
	}
}
