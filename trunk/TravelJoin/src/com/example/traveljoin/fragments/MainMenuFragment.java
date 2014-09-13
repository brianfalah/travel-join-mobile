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
import com.example.traveljoin.activities.TourFormActivity;
import com.example.traveljoin.activities.UserProfileActivity;

public class MainMenuFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_menu, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MainMenuItemAdapter adapter = new MainMenuItemAdapter(getActivity());
		adapter.add(new MainMenuItem(getString(R.string.profile), R.drawable.ic_action_person));
		adapter.add(new MainMenuItem(getString(R.string.pois), R.drawable.ic_action_place));
		adapter.add(new MainMenuItem(getString(R.string.tours), R.drawable.ic_action_split));
		adapter.add(new MainMenuItem(getString(R.string.groups), R.drawable.ic_action_group));
		setListAdapter(adapter);
	}

	private class MainMenuItem {
		public String tag;
		public int iconRes;
		
		public MainMenuItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id){
		Class<?> cls = null;
		String title = ((MainMenuItem) l.getItemAtPosition(position)).tag;		
		
		if (title.equals(getString(R.string.profile))) {
			cls = UserProfileActivity.class;	
		} else if (title.equals(getString(R.string.pois))) {
			cls = MapActivity.class;
		} else if (title.equals(getString(R.string.tours))) {
			cls = TourFormActivity.class;
		} else if (title.equals(getString(R.string.groups))) {
			cls = MainActivity.class;
		}
        
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }  

	private class MainMenuItemAdapter extends ArrayAdapter<MainMenuItem> {

		public MainMenuItemAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			MainMenuItem item = getItem(position);
			
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_menu_item, null);
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
