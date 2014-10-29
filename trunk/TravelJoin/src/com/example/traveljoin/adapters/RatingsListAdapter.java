package com.example.traveljoin.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.models.Rating;
import com.example.traveljoin.models.User;
import com.facebook.widget.ProfilePictureView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;


public class RatingsListAdapter extends ArrayAdapter<Rating> {

	public RatingsListAdapter(Context context, ArrayList<Rating> ratings) {
        super(context, 0, ratings);
     }
	
	public View getView(int position, View convertView, ViewGroup parent) {
	       
	       Rating rating = getItem(position);    
	       
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.rating_list_item, parent, false);
	       }

	       	ProfilePictureView profilePictureView = (ProfilePictureView) convertView
					.findViewById(R.id.commentProfilePic);			
			TextView commentOwnerNameView = (TextView) convertView
					.findViewById(R.id.commentOwnerName);
			RatingBar commentRatingBar = (RatingBar) convertView
					.findViewById(R.id.commentRatingBar);
			TextView commentDate = (TextView) convertView
					.findViewById(R.id.commentDate);
			TextView commentText = (TextView) convertView
					.findViewById(R.id.commentText);
			
			profilePictureView.setCropped(true);
			profilePictureView.setProfileId(rating.getUser().getFacebookId());
			commentOwnerNameView.setText(rating.getUser().getFullName());
			commentRatingBar.setRating(rating.getValue());
			
			SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm");           
			commentDate.setText(formatter.format(rating.getUpdatedAt().getTime()));
			commentText.setText(rating.getComment());

	       return convertView;
	   }
}



   
    