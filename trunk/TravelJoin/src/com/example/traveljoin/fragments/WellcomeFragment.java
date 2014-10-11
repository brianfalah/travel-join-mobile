package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.MapActivity;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.facebook.Session;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WellcomeFragment extends Fragment {

	public WellcomeFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_welcome, container,
				false);

		Button button_explore = (Button) view.findViewById(R.id.explore_button);
		
		button_explore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {			
				Intent intent = new Intent(getActivity(), MapActivity.class);
				startActivity(intent);
			}
		});
     
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	    inflater.inflate(R.menu.wellcome_fragment_actions, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_logout:
	            logout();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void logout() {
		Session session = Session.getActiveSession();
		session.closeAndClearTokenInformation();
		getActivity().recreate();
	}

}
