package com.example.traveljoin.activities;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.PoiEvent;
import com.example.traveljoin.models.User;

public class PoiEventDetailsActivity extends ActionBarActivity {
	private static final int EDIT_POI_REQUEST = 1;
	protected static final int DELETE_POI_EVENT_METHOD = 2;

	ProgressDialog progress;
	public PoiEvent poiEvent;
	User user;
	Integer poiUserId;
	private TextView tvName;
	private TextView tvDesc;
	private TextView tvTimeStart;
	private TextView tvTimeEnd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_event_details);
		initializeUser();
		Bundle b = getIntent().getExtras(); 		
		poiEvent = (PoiEvent) b.get("poi_event");
		poiUserId = (Integer) b.get("poi_user_id");
		// get reference to the views
		tvName = (TextView) findViewById(R.id.poiEventName);
		tvDesc = (TextView) findViewById(R.id.poiEventDescription);
		tvTimeStart = (TextView) findViewById(R.id.poiEventTimeStart);
		tvTimeEnd = (TextView) findViewById(R.id.poiEventTimeEnd);
		setFields();
	}
	
	public void setFields(){
        tvName.setText(poiEvent.getName());
        tvDesc.setText(poiEvent.getDescription());
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm");           
        tvTimeStart.setText(formatter.format(poiEvent.getFromDate().getTime()));
        tvTimeEnd.setText(formatter.format(poiEvent.getToDate().getTime()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.poi_event_view_actions, menu);
		if (!user.getId().equals(poiUserId)) {
			menu.removeItem(R.id.action_edit);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_edit:
			editPoiEvent();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// cuando se clickea el boton editar viene aca!
	public void editPoiEvent() {
		Intent intent = new Intent(this, PoiEventFormActivity.class);
		intent.putExtra("poi_event", poiEvent);
		intent.putExtra("poi_id", poiEvent.getPoiId());
		// va al form para editarlo
		startActivityForResult(intent, EDIT_POI_REQUEST);
	}

	/*
	 * Cuando vuelve de un activity empezado con un startActivityForResult viene
	 * aca
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		case EDIT_POI_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Bundle b = data.getExtras(); // gets the previously created
												// intent
				poiEvent = (PoiEvent) b.get("poiEvent");				
				setFields();				
				break;
			}
			break;
		}

	}


	@Override
	protected void onPause() {
		super.onPause();
		// borrar la referencia a el cartelito del dialogo, sino trae problemas
		if (progress != null)
			progress.dismiss();
	}

	private void initializeUser() {
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		user = globalContext.getUser();
	}
}
