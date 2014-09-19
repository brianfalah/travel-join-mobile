package com.example.traveljoin.activities;

import com.example.traveljoin.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GroupFormActivity extends Activity {
	
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_form);	
		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.groups_creation);
	}
	
	public void cancel(View button) { 	
		Intent output = new Intent();	    			    
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}
	
	public void onCancelButtonClicked(View button) { 	
		Intent output = new Intent();	    			    
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}
	
	public void onAcceptButtonClicked(View button) { 	
		//TODO
	}
}
