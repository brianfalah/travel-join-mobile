package com.example.traveljoin.activities;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.traveljoin.R;
import com.example.traveljoin.fragments.DateTimePickerDialog;
import com.example.traveljoin.fragments.DateTimePickerDialog.DateTimePickerDialogListener;
import com.example.traveljoin.models.Category;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Poi;

public class EventFormActivity extends ActionBarActivity implements DateTimePickerDialogListener{
	
	ProgressDialog progress;
	EditText nameField;
	EditText descField;
	TextView dateFromtv;
	Button addButton;
	Button cancelButton;
	DatePicker dp;
	TimePicker tp;
	Calendar timeFrom;
	Calendar timeTo;
	
	private static final int ADD_EVENT_METHOD = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);        
        // get reference to the views
		nameField = (EditText) findViewById(R.id.EventName);
		descField = (EditText) findViewById(R.id.EventDescription);
		addButton = (Button) findViewById(R.id.EventAddButton);
		cancelButton = (Button) findViewById(R.id.EventCancelButton);   
		dateFromtv = (TextView) findViewById(R.id.dateFromTv);
    }
 
    
	//cuando se clickea el boton viene aca!
	public void addEvent(View button) { 
		
//			Poi poi_to_create = new Poi(null, Double.parseDouble(tvLatitude.getText().toString()),
//					Double.parseDouble(tvLongitude.getText().toString()), nameField.getText().toString(),
//					descField.getText().toString(), 0, ((Category)categoryField.getSelectedItem()).getId(),
//					"");
//			
//	        String url = getResources().getString(R.string.api_url) + "/pois/create";
//	        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(ADD_POI_METHOD, poi_to_create); 
//	        httpAsyncTask.execute(url);        
	        //sigue en onPostExecute, en la parte de ADD_POI_METHOD
		
	}
	
	//cuando se clickea el boton viene aca!
	public void cancel(View button) { 	
		
	}

	private Boolean validateFields() {		
		return validateField(nameField) && validateField(descField);
	}

	private Boolean validateField(View field) {
		Boolean valid = null;
		if (field instanceof TextView) {
			EditText edit_text_field = (EditText) field;
			if (TextUtils.isEmpty( edit_text_field.getText().toString() ) ){
				edit_text_field.setError(edit_text_field.getHint() + " is required!");
				valid = false;
			} else {
				edit_text_field.setError(null);
				valid = true;
			}
		} else {
			if (field instanceof Spinner) {
				Spinner spinner_field = (Spinner) field;
				if (TextUtils.isEmpty( ((Category) spinner_field.getSelectedItem()).getName() ) ){
					Toast.makeText(this, spinner_field.getPrompt() + " es requerido!",
			                Toast.LENGTH_SHORT).show();
					valid = false;
				}
				else{
					valid = true;
				}
			}
		}
		return valid;
	}
	
	public void showTimePickerDialog(View button) {		
		FragmentManager fm = getSupportFragmentManager();
		DateTimePickerDialog editNameDialog = new DateTimePickerDialog();
        editNameDialog.show(fm, "Seleccione el día y la hora del evento");
	}	       
	
	//click en OK: se setean la fecha y hora en el textview
	@Override
	public void onFinishDateTimeDialog(Calendar time){						
		dateFromtv.setText(time.get(Calendar.DATE) + "/" + time.get(Calendar.MONTH) + "/" + time.get(Calendar.YEAR)
				+ " " + time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));
	}
	
	public void showExceptionError(Exception e){
		CustomTravelJoinException exception = new CustomTravelJoinException(e.getMessage());
		exception.alertExceptionMessage(getApplicationContext());
		e.printStackTrace();
	}
	
	public void closeActivity(Poi poi_created_or_updated){
		Intent output = new Intent();	    
		output.putExtra("poi_created_or_updated", poi_created_or_updated);	    
		setResult(Activity.RESULT_OK, output);
		finish();
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
    	//borrar la referencia a el cartelito del dialogo, sino trae problemas
    	if(progress != null)
    		progress.dismiss();	
    }
    
}
