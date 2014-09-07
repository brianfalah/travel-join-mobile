package com.example.traveljoin.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Poi implements Serializable, GeneralListItem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Double latitude;
	private Double longitude;
	private String name;
	private String description;
	
	private Integer userId;
	private Integer categoryId;
	private String categoryName;
	private ArrayList<PoiEvent> poiEvents;
	
	//TODO: borrar este constructor. Solo se esta usando para la lista de Poi. Presentacion de interfaces de usuario
	public Poi(String name,String description) {
		super();
		this.name = name;
		this.description = description;
	}
		
	//constructor
	public Poi(Integer id, Double latitude, Double longitude, String name,
			String description, Integer userId,
			Integer categoryId, String categoryName) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public ArrayList<PoiEvent> getPoiEvents() {
		return poiEvents;
	}

	public void setPoiEvents(ArrayList<PoiEvent> poiEvents) {
		this.poiEvents = poiEvents;
	}

	public static Poi fromJSON(JSONObject poiJson) throws JSONException{		
		Poi poi = new Poi(poiJson.getInt("id"), poiJson.getDouble("latitude"), poiJson.getDouble("longitude"),
	    		poiJson.getString("name"), poiJson.getString("description"),
	    		0, poiJson.getInt("category_id"), poiJson.getString("category_name"));
		return poi;
	}
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}
	        jsonObject.put("name", getName());
	        jsonObject.put("description", getDescription());
	        jsonObject.put("latitude", getLatitude());
	        jsonObject.put("longitude", getLongitude());
	        jsonObject.put("category_id", getCategoryId());
	        
	        JSONObject finalobject = new JSONObject();
	        finalobject.put("poi", jsonObject);	        
	        return finalobject;
	    } catch (JSONException e) {
	        e.printStackTrace();
	        return jsonObject;
	    }

	}
}
