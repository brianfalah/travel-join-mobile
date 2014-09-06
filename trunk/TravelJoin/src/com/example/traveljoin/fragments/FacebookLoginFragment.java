package com.example.traveljoin.fragments;

import com.example.traveljoin.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FacebookLoginFragment extends Fragment {
	
	public FacebookLoginFragment() {}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);

        return view;
    }
	
}
