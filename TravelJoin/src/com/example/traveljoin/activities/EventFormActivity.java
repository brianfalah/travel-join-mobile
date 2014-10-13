package com.example.traveljoin.activities;

import java.util.Calendar;

import android.app.Activity;
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
import com.example.traveljoin.models.PoiEvent;;

public class EventFormActivity extends ActionBarActivity implements DateTimePickerDialogListener{
	
	ProgressDialog progress;
	EditText nameField;
	EditText descField;
	TextView dateFromTv;
	TextView dateToTv;
	DatePicker dp;
	TimePicker tp;
	Calendar timeFrom;
	Calendar timeTo;
	Integer poi_id = null; 
	
	//campos
	private static final Integer DATE_FROM = 1;
	private static final Integer DATE_TO = 2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);        
        // get reference to the views
		nameField = (EditText) findViewById(R.id.EventName);
		descField = (EditText) findViewById(R.id.EventDescription);
		dateFromTv = (TextView) findViewById(R.id.dateFromTv);
		dateToTv = (TextView) findViewById(R.id.dateToTv);
		Bundle b = getIntent().getExtras();
		if (b != null){
			poi_id = (Integer) b.get("poi_id");
		}
    }
 
    
	//cuando se clickea el boton viene aca!
	public void addEvent(View button) { 		
		Intent output = new Intent();	 
		if (validateFields()){
			PoiEvent poiEvent = new PoiEvent(null, nameField.getText().toString(),
					descField.getText().toString(), poi_id, timeFrom, timeTo);
			output.putExtra("poiEvent", poiEvent);	    
			setResult(Activity.RESULT_OK, output);
			finish();
		}
	}
	
	//cuando se clickea el boton viene aca!
	public void cancel(View button) { 	
		Intent output = new Intent();	    			    
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}

	private Boolean validateFields() {				
		return validateField(nameField) && validateField(descField) &&
				validateDateField(dateFromTv, timeFrom) && validateDateField(dateToTv, timeTo)
				&& validateTimeToBiggest(dateToTv, timeFrom, timeTo);
	}

	private Boolean validateField(View field) {
		Boolean valid = null;
		if (field instanceof EditText) {
			EditText edit_text_field = (EditText) field;
			if (TextUtils.isEmpty( edit_text_field.getText().toString() ) ){
				edit_text_field.requestFocus();
				edit_text_field.setError(edit_text_field.getHint() + " es requerido!");
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
	
	private Boolean validateDateField(View field, Calendar time){
		Boolean valid = null;
		
		TextView edit_text_field = (TextView) field;
		if (time == null){
			edit_text_field.requestFocus();
			edit_text_field.setError(edit_text_field.getText() + " es requerido!");
			valid = false;
		} else {
			Calendar now = Calendar.getInstance();
			now.add(Calendar.MONTH ,1);
			if(time.compareTo(now) < 0){
				edit_text_field.requestFocus();
				edit_text_field.setError("La fecha y hora debe ser mayor a la hora actual");
				valid = false;
			}
			else{
				edit_text_field.setError(null);
				valid = true;
			}
		}
		
		return valid;		
	}
	
	private Boolean validateTimeToBiggest(View field, Calendar timeFrom, Calendar timeTo){
		Boolean valid = null;
		
		TextView edit_text_field = (TextView) field;
		if (timeFrom != null && timeTo != null && timeTo.compareTo(timeFrom) <= 0){
			edit_text_field.requestFocus();
			edit_text_field.setError("El día y la hora de fin debe ser mayor al día y la hora de inicio");
			valid = false;
		}	
		else{
			valid = true;
		}
		
		return valid;		
	}
	
	public void showTimePickerDialog(View v) {		
		FragmentManager fm = getSupportFragmentManager();
		DateTimePickerDialog editNameDialog;
		switch (v.getId()) {
		    case (R.id.dateFromImg):
		    	editNameDialog = new DateTimePickerDialog(DATE_FROM);
	        	editNameDialog.show(fm, "Seleccione el día y la hora de inicio del evento");
		    break;
		    case (R.id.dateToImg):
		    	editNameDialog = new DateTimePickerDialog(DATE_TO);
        		editNameDialog.show(fm, "Seleccione el día y la hora de fin del evento");
		    break;
	    }		
	}	       
	
	//click en OK: se setean la fecha y hora en el textview
	@Override
	public void onFinishDateTimeDialog(Calendar time, Integer field){		
		if ( DATE_FROM.equals(field) ){
			timeFrom = time;
			dateFromTv.setText(time.get(Calendar.DATE) + "/" + time.get(Calendar.MONTH) + "/" + time.get(Calendar.YEAR)
				+ " " + String.format("%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE)));
			dateFromTv.setError(null);
		}
		else{
			if ( DATE_TO.equals(field) ){
				timeTo = time;
				dateToTv.setText(time.get(Calendar.DATE) + "/" + time.get(Calendar.MONTH) + "/" + time.get(Calendar.YEAR)
						+ " " + String.format("%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE)));
				dateToTv.setError(null);
			}
		}
	}
	
	public void showExceptionError(Exception e){
		CustomTravelJoinException exception = new CustomTravelJoinException(e.getMessage());
		exception.alertExceptionMessage(getApplicationContext());
		e.printStackTrace();
	}
	
//	public void closeActivity(Poi poi_created_or_updated){
//		Intent output = new Intent();	    
//		output.putExtra("poi_created_or_updated", poi_created_or_updated);	    
//		setResult(Activity.RESULT_OK, output);
//		finish();
//	}
	
    @Override
    protected void onPause() {
    	super.onPause();
    	//borrar la referencia a el cartelito del dialogo, sino trae problemas
    	if(progress != null)
    		progress.dismiss();	
    }
    
}
