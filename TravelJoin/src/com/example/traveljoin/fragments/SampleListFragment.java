package com.example.traveljoin.fragments;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.MainActivity;
import com.example.traveljoin.activities.MapActivity;
import com.example.traveljoin.activities.UserProfileActivity;

public class SampleListFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		adapter.add(new SampleItem(getString(R.string.profile), android.R.drawable.ic_menu_preferences));
		adapter.add(new SampleItem(getString(R.string.pois), android.R.drawable.ic_menu_myplaces));
		adapter.add(new SampleItem(getString(R.string.circuits), android.R.drawable.ic_menu_myplaces));
		adapter.add(new SampleItem(getString(R.string.groups), android.R.drawable.ic_menu_agenda));
		setListAdapter(adapter);
	}

	private class SampleItem {
		public String tag;
		public int iconRes;
		
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id){
		Class<?> cls = null;
		String title = ((SampleItem) l.getItemAtPosition(position)).tag;		
		if (title.equals(getString(R.string.profile))) {
			cls = UserProfileActivity.class;	
		} else if (title.equals(getString(R.string.pois))) {
			cls = MapActivity.class;
		} else if (title.equals(getString(R.string.circuits))) {
			cls = MainActivity.class;
		} else if (title.equals(getString(R.string.groups))) {
			cls = MainActivity.class;
		}
        

        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }  

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			SampleItem item = getItem(position);
			
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			
			convertView.setBackgroundColor(getResources().getColor(R.color.common_signin_btn_dark_text_pressed));
			
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(item.iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(item.tag);

			return convertView;
		}

	}
}
