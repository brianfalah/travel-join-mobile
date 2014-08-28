package com.example.traveljoin.models;

import android.app.AlertDialog;
import android.content.Context;

public class CustomTravelJoinException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
    } 

    public void alertExceptionMessage(Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context); 
        dialog.setTitle("Intente de nuevo");
        dialog.setMessage(this.getMessage());
        dialog.setNeutralButton("Ok", null);
        dialog.create().show();
    }
    
    public void alertConnectionProblem(Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context); 
        dialog.setTitle("Intente de nuevo");
        dialog.setMessage("Ha ocurrido un problema con la conexión a los servidores. Por favor, intente de nuevo.");
        dialog.setNeutralButton("Ok", null);
        dialog.create().show();
    }
}
