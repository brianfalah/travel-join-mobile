package com.example.traveljoin.models;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;


import org.json.JSONException;
import org.json.JSONObject;

public class Complaint implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private Integer poiId;		
	private	String comment;
	
	public Complaint(Integer userId, Integer poiId,	String comment) {
		super();
		this.userId = userId;
		this.setPoiId(poiId);
		this.comment = comment;
		
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getPoiId() {
		return poiId;
	}

	public void setPoiId(Integer poiId) {
		this.poiId = poiId;
	}

	public static Complaint fromJSON(JSONObject jsonObject) throws JSONException, ParseException {
		
		
		return new Complaint (jsonObject.getInt("user_id"), jsonObject.getInt("poi_id"),
							jsonObject.getString("comment"));
	};
	
	public JSONObject toJSON() throws UnsupportedEncodingException {

	    JSONObject jsonObject= new JSONObject();
	    try {  		    		    	    	
	    	jsonObject.put("user_id", getUserId());
	        jsonObject.put("poi_id", getPoiId());
	        jsonObject.put("comment", getComment());	        
	        return jsonObject;
	    } catch (JSONException e) {
	        e.printStackTrace();
	        return jsonObject;
	    }

	}
	
}