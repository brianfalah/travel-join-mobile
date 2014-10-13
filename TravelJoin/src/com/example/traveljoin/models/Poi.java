package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Poi implements Serializable, GeneralItem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Double latitude;
	private Double longitude;
	private String name;
	private String description;
	private String address;
	
	private Integer userId;
	private Integer categoryId;
	private String categoryName;
	private ArrayList<PoiEvent> poiEvents;
	private ArrayList<PoiEvent> poiEventsToDelete;
	
	//TODO: borrar este constructor. Solo se esta usando para la lista de Poi. Presentacion de interfaces de usuario
	public Poi(String name,String description) {
		super();
		this.name = name;
		this.description = description;
	}
		
	//constructor
	public Poi(Integer id, Double latitude, Double longitude, String name,
			String description,String address, Integer userId,
			Integer categoryId, String categoryName, ArrayList<GeneralItem> poiEvents) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.description = description;
		this.address = address;
		this.userId = userId;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.poiEvents = new ArrayList<PoiEvent>();
		this.poiEvents.clear();
		this.poiEvents.addAll((Collection<? extends PoiEvent>) poiEvents);
		this.poiEventsToDelete = new ArrayList<PoiEvent>();
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public void setPoiEvents(ArrayList<GeneralItem> poiEvents) {
		this.poiEvents.clear();
		this.poiEvents.addAll((Collection<? extends PoiEvent>) poiEvents);
	}

	public static Poi fromJSON(JSONObject poiJson) throws JSONException, ParseException{	
		ArrayList<GeneralItem> poiEventsToAdd = new ArrayList<GeneralItem>();
		JSONArray poiEventsJson = poiJson.getJSONArray("events");
		
		for (int i = 0; i < poiEventsJson.length(); i++) {
    	    JSONObject poiEventJson = poiEventsJson.getJSONObject(i);    	        
    	    PoiEvent poiEvent = PoiEvent.fromJSON(poiEventJson);
    	    poiEventsToAdd.add(poiEvent);    	    
    	}
		
		Poi poi = new Poi(poiJson.getInt("id"), poiJson.getDouble("latitude"), poiJson.getDouble("longitude"),
	    		poiJson.getString("name"), poiJson.getString("description"), poiJson.getString("address"),
	    		poiJson.getInt("user_id"), poiJson.getInt("category_id"), poiJson.getString("category_name"), poiEventsToAdd);				
		
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
	        jsonObject.put("address", getAddress());
	        jsonObject.put("latitude", getLatitude());
	        jsonObject.put("longitude", getLongitude());
	        jsonObject.put("category_id", getCategoryId());
	        jsonObject.put("user_id", getUserId());
	        
	        JSONArray eventsJson = new JSONArray();	        
	        for (int i = 0; i < getPoiEvents().size(); i++) {
	        	eventsJson.put(getPoiEvents().get(i).toJSON());
			}
	        	        
	        for (int i = 0; i < getPoiEventsToDelete().size(); i++) {
	        	eventsJson.put(getPoiEventsToDelete().get(i).toJSON());
			}
	        if(eventsJson.length() > 0){
	        	jsonObject.put("events_attributes", eventsJson);
	        }
	        
	        
	        JSONObject finalobject = new JSONObject();
	        finalobject.put("poi", jsonObject);	        
	        return finalobject;
	    } catch (JSONException e) {
	        e.printStackTrace();
	        return jsonObject;
	    }

	}

	public ArrayList<PoiEvent> getPoiEventsToDelete() {
		return poiEventsToDelete;
	}
	
	public void setPoiEventsToDelete(ArrayList<PoiEvent> poiEvents) {
		this.poiEventsToDelete = poiEvents;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Poi)) {
			return false;
		}
		Poi poi = (Poi) object;
		return this.name.equals(poi.name)
				&& this.description.equals(poi.description)
				&& this.latitude.equals(poi.latitude)
				&& this.longitude.equals(poi.longitude);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() + this.description.hashCode()
				+ this.latitude.hashCode() + this.longitude.hashCode();
	}
}
