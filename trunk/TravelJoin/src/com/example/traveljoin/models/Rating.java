package com.example.traveljoin.models;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class Rating implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer userId;
	//id del poi o del tour
	private Integer ratingableId;		
	private String ratingableType;
	private Float value;
	private	String comment;
	private Calendar updatedAt;
	private User user;
	
	public Rating(Integer userId, Integer ratingableId, String ratingableType,
			Float value, String comment, Calendar updatedAt, User user) {
		super();
		this.userId = userId;
		this.ratingableId = ratingableId;
		this.ratingableType = ratingableType;
		this.value = value;
		this.comment = comment;
		this.updatedAt = updatedAt;
		this.user = user;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRatingableId() {
		return ratingableId;
	}

	public void setRatingableId(Integer ratingableId) {
		this.ratingableId = ratingableId;
	}

	public String getRatingableType() {
		return ratingableType;
	}

	public void setRatingableType(String ratingableType) {
		this.ratingableType = ratingableType;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static Rating fromJSON(JSONObject jsonObject) throws JSONException, ParseException {
		Calendar updatedAt = TimeFormatter.toCalendar(jsonObject.getString("updated_at"));
		User user = User.fromJSON(jsonObject.getJSONObject("user"));
		
		return new Rating(jsonObject.getInt("user_id"), jsonObject.getInt("ratingable_id"),
				jsonObject.getString("ratingable_type"), (float) jsonObject.getDouble("value"),
				jsonObject.getString("comment"), updatedAt, user);
	}
	
	public JSONObject toJSON() throws UnsupportedEncodingException{

	    JSONObject jsonObject= new JSONObject();
	    try {  		    		    	    	
	    	jsonObject.put("user_id", getUserId());
	        jsonObject.put("ratingable_id", getRatingableId());
	        jsonObject.put("ratingable_type", getRatingableType());
	        jsonObject.put("value", getValue());
	        jsonObject.put("comment", getComment());	        
	        return jsonObject;
	    } catch (JSONException e) {
	        e.printStackTrace();
	        return jsonObject;
	    }

	}
	
}
