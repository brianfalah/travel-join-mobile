package com.example.traveljoin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
//import java.util.Iterator;
import java.util.LinkedHashMap;

import com.example.traveljoin.R;
import com.example.traveljoin.models.GeneralItem;

public class StableArrayAdapter extends ArrayAdapter<GeneralItem> {

    final int INVALID_ID = -1;
    //private Context context;

    LinkedHashMap<GeneralItem, Integer> mIdMap = new LinkedHashMap<GeneralItem, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId, ArrayList<GeneralItem> objects) {
        super(context, textViewResourceId, objects);    
        setMap(objects);
    }
    
    public void setMap(ArrayList<GeneralItem> objects){
    	for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }
    
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GeneralItem item = (GeneralItem) getItem(position);
		
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
//
//	@Override
//	public int getCount() {
//		return mIdMap.size();
//	}

//	@Override
//	public GeneralItem getItem(int position) {
//		int i = 0;
//		for (GeneralItem generalItem : mIdMap.keySet()) {
//			if(i == position)
//			{
//				return generalItem;
//			}
//			else
//			{
//				i++;
//			}
//		}
//		return null;
//	}
	
//	public Context getContext() {
//		return context;
//	}
	
//	public ArrayList<GeneralItem> getGeneralItemList() {
//		return generalItemList;
//	}
    
    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        GeneralItem item = (GeneralItem) getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    
    @Override
    public void clear() {
    	mIdMap.clear();
    	super.clear();        
    }
}
