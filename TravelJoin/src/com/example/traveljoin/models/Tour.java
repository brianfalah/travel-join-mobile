package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tour implements Serializable, GeneralItem {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String description;
	private Integer userId;
	
	private ArrayList<TourPoi> tourPois;
	private ArrayList<TourPoi> tourPoisToDelete;
	
	// TODO: borrar este constructor. Solo se esta usando para la lista de Poi.
	// Presentacion de interfaces de usuario
	public Tour(String name, String description) {
		super();
		this.setName(name);
		this.setDescription(description);
		this.setId(0);
	}
	
	public Tour(Integer id, String name, String description, Integer userId, ArrayList<TourPoi> tourPois) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.setTourPois(tourPois);
		setTourPoisToDelete(new ArrayList<TourPoi>());
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
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

	public ArrayList<TourPoi> getTourPois() {
		return tourPois;
	}

	public void setTourPois(ArrayList<TourPoi> tourPois) {
		this.tourPois = tourPois;
	}

	public ArrayList<TourPoi> getTourPoisToDelete() {
		return tourPoisToDelete;
	}

	public void setTourPoisToDelete(ArrayList<TourPoi> tourPoisToDelete) {
		this.tourPoisToDelete = tourPoisToDelete;
	}

	public static Tour fromJSON(JSONObject tourJson) throws JSONException, ParseException{	
		ArrayList<TourPoi> tourPoisToAdd = new ArrayList<TourPoi>();
		JSONArray tourPoisJson = tourJson.getJSONArray("pois");
		
		for (int i = 0; i < tourPoisJson.length(); i++) {
    	    JSONObject tourPoiJson = tourPoisJson.getJSONObject(i);    	        
    	    TourPoi tourPoi = TourPoi.fromJSON(tourPoiJson);
    	    tourPoisToAdd.add(tourPoi);    	    
    	}
		
		Tour tour = new Tour(tourJson.getInt("id"), tourJson.getString("name"),
				tourJson.getString("description"), tourJson.getInt("user_id"), tourPoisToAdd);				
		
		return tour;
	}
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}	    	
	    	
	        jsonObject.put("name", getName());
	        jsonObject.put("description", getDescription());
	        jsonObject.put("user_id", getUserId());
	        
	        JSONArray tourPoisJson = new JSONArray();	        
	        for (int i = 0; i < getTourPois().size(); i++) {
	        	tourPoisJson.put(getTourPois().get(i).toJSON());
			}
	        	        
	        for (int i = 0; i < getTourPoisToDelete().size(); i++) {
	        	tourPoisJson.put(getTourPoisToDelete().get(i).toJSON());
			}
	        if(tourPoisJson.length() > 0){
	        	jsonObject.put("tours_pois_attributes", tourPoisJson);
	        }
	        
	        
	        JSONObject finalobject = new JSONObject();
	        finalobject.put("tour", jsonObject);	        
	        return finalobject;
	    } catch (JSONException e) {
	        e.printStackTrace();
	        return jsonObject;
	    }

	}
}
