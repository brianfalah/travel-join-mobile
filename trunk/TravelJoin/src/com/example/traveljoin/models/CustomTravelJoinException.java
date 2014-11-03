package com.example.traveljoin.models;

import com.example.traveljoin.R;

import android.app.AlertDialog;
import android.content.Context;

public class CustomTravelJoinException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	 // Default constructor 
    // initializes custom exception variable to none
    public CustomTravelJoinException() {
        // call superclass constructor
        super();            
    }


	// Custom Exception Constructor
    public CustomTravelJoinException(String message) {
        // Call super class constructor
        super(message);  
        this.title = null;
    } 
    
	// Custom Exception Constructor
    public CustomTravelJoinException(String title, String message) {
        // Call super class constructor
    	super(message);  
    	this.title = title;        
    } 

    public void alertExceptionMessage(Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (getTitle() == null){
        	dialog.setTitle(R.string.retry_message);
        }        
        else{
        	dialog.setTitle(getTitle());
        }
        dialog.setMessage(this.getMessage());
        dialog.setNeutralButton("Ok", null);
        dialog.create().show();
    }
    
    public void alertConnectionProblem(Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context); 
        dialog.setTitle(R.string.retry_message);
        dialog.setMessage(R.string.connection_error_message);
        dialog.setNeutralButton("Ok", null);
        dialog.create().show();
    }
}
