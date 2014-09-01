package com.example.traveljoin.fragments;

import java.util.Calendar;

import com.example.traveljoin.R;
import com.example.traveljoin.activities.EventFormActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DateTimePickerDialog  extends DialogFragment{ 
	DatePicker dp;
	TimePicker tp;
	Button btnOK;
	Calendar time;
	
	public interface DateTimePickerDialogListener {
        void onFinishDateTimeDialog(Calendar timeCalendar);
    }

    public DateTimePickerDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.custom_datetime_picker, container);
		dp = (DatePicker)view.findViewById(R.id.datePicker);
		tp = (TimePicker)view.findViewById(R.id.timePicker);
        getDialog().setTitle("Seleccione el día y la hora del evento");
        
        btnOK = (Button) view.findViewById(R.id.setDateTime);
        btnOK.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	EventFormActivity activity = (EventFormActivity) getActivity();
                    	int day = dp.getDayOfMonth();
                		int month = dp.getMonth() + 1;
                		int year = dp.getYear();
                		int hour = tp.getCurrentHour();
                		int minute = tp.getCurrentMinute();
                				
                		time = Calendar.getInstance();
                		time.set(Calendar.YEAR, year);
                		time.set(Calendar.MONTH, month);
                		time.set(Calendar.DATE, day);
                		time.set(Calendar.HOUR_OF_DAY, hour);
                		time.set(Calendar.MINUTE, minute);
                		time.set(Calendar.SECOND, 0);
                		time.set(Calendar.MILLISECOND, 0);
                        activity.onFinishDateTimeDialog(time);
                        getDialog().dismiss();
                        //this.dismiss();
                    }
                });

        return view;        		        
    }
}
