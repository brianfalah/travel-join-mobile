package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class Favorite implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer userId;
	//id del poi o del tour
	private Integer favoritableId;		
	private String favoritableType;

	public Favorite(Integer userId, Integer favoritableId, String favoritableType) {
		super();
		this.userId = userId;
		this.favoritableId = favoritableId;		
		this.favoritableType = favoritableType;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getFavoritableId() {
		return favoritableId;
	}

	public void setFavoritableId(Integer favoritableId) {
		this.favoritableId = favoritableId;
	}
	
	public String getFavoritableType() {
		return favoritableType;
	}

	public void setFavoritableType(String favoritableType) {
		this.favoritableType = favoritableType;
	}

	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {  		    		    	    	
	    	jsonObject.put("user_id", getUserId());
	        jsonObject.put("favoritable_id", getFavoritableId());
	        jsonObject.put("favoritable_type", getFavoritableType());
	        return jsonObject;
	    } catch (JSONException e) {
	        e.printStackTrace();
	        return jsonObject;
	    }

	}
	
}
