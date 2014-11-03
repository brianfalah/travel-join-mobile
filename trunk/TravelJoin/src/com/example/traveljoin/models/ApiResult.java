package com.example.traveljoin.models;

import org.apache.http.HttpStatus;

public class ApiResult {
	private int status_code;
	private String result;
	
	public ApiResult(int status_code, String result) {
		super();
		this.status_code = status_code;
		this.result = result;
	}
	
	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public boolean ok(){
		if (this.getStatus_code() == HttpStatus.SC_OK){
			return true;
		}
		else{
			return false;			
		}
	}
	
	public boolean unprocessableEntity(){
		if (this.getStatus_code() == HttpStatus.SC_UNPROCESSABLE_ENTITY){
			return true;
		}
		else{
			return false;			
		}
	}
	
	public boolean notAcceptable(){
		if (this.getStatus_code() == HttpStatus.SC_NOT_ACCEPTABLE){
			return true;
		}
		else{
			return false;			
		}
	}
}
