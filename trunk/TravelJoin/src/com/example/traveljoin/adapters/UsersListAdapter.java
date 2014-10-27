package com.example.traveljoin.adapters;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.models.User;
import com.facebook.widget.ProfilePictureView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class UsersListAdapter extends ArrayAdapter<User> {

	public UsersListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
     }
	
	public View getView(int position, View convertView, ViewGroup parent) {
	       
	       User user = getItem(position);    
	       
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);
	       }

	       	ProfilePictureView profilePictureView = (ProfilePictureView) convertView
					.findViewById(R.id.selection_profile_pic);
			profilePictureView.setCropped(true);
			TextView userOwnerNameView = (TextView) convertView
					.findViewById(R.id.selection_owner_name);

			profilePictureView.setProfileId(user.getFacebookId());
			userOwnerNameView.setText(user.getFullName());

	       return convertView;
	   }
}



   
    