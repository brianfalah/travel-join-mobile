package com.example.traveljoin.fragments;

import java.util.ArrayList;

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
import com.example.traveljoin.activities.AugmentedRealityActivity;
import com.example.traveljoin.activities.GroupsMainActivity;
import com.example.traveljoin.activities.HelpActivity;
import com.example.traveljoin.activities.MapActivity;
import com.example.traveljoin.activities.PoisMainActivity;
import com.example.traveljoin.activities.ToursMainActivity;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.Poi;

public class MainMenuFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_menu, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MainMenuItemAdapter adapter = new MainMenuItemAdapter(getActivity());
		adapter.add(new MainMenuItem(getString(R.string.profile),
				R.drawable.ic_action_person));
		adapter.add(new MainMenuItem(getString(R.string.pois),
				R.drawable.ic_action_place));
		adapter.add(new MainMenuItem(getString(R.string.tours),
				R.drawable.ic_action_split));
		adapter.add(new MainMenuItem(getString(R.string.groups),
				R.drawable.ic_action_group));
		adapter.add(new MainMenuItem(getString(R.string.augmented_reality),
				R.drawable.ic_action_full_screen));
		adapter.add(new MainMenuItem(getString(R.string.help),
				R.drawable.ic_action_help_dark));
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
	public void onListItemClick(ListView l, View v, int position, long id) {
		Class<?> cls = null;
		String title = ((MainMenuItem) l.getItemAtPosition(position)).tag;

		Bundle b = new Bundle();
		if (title.equals(getString(R.string.profile))) {
			GlobalContext globalContext = (GlobalContext) getActivity()
					.getApplicationContext();
			globalContext
					.refreshUserAndLaunchUserProfile((MapActivity) getActivity());
			return;
		} else if (title.equals(getString(R.string.pois))) {
			cls = PoisMainActivity.class;
		} else if (title.equals(getString(R.string.tours))) {
			cls = ToursMainActivity.class;
		} else if (title.equals(getString(R.string.groups))) {
			cls = GroupsMainActivity.class;
		} else if (title.equals(getString(R.string.augmented_reality))) {
			cls = AugmentedRealityActivity.class;
			ArrayList<Poi> pois = new ArrayList<Poi>(
					MapActivity.markerPoiMap.values());
			b.putSerializable("pois", pois);
		} else if (title.equals(getString(R.string.help))) {
			cls = HelpActivity.class;
		}

		Intent intent = new Intent(getActivity(), cls);
		intent.putExtra("b", b); // le pasamos el punto al form
		startActivity(intent);
	}

	private class MainMenuItemAdapter extends ArrayAdapter<MainMenuItem> {

		public MainMenuItemAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			MainMenuItem item = getItem(position);

			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.main_menu_item, null);
			}

			convertView.setBackgroundColor(getResources().getColor(
					R.color.common_signin_btn_dark_text_pressed));

			ImageView icon = (ImageView) convertView
					.findViewById(R.id.row_icon);
			icon.setImageResource(item.iconRes);
			TextView title = (TextView) convertView
					.findViewById(R.id.row_title);
			title.setText(item.tag);

			return convertView;
		}

	}
}
