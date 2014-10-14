package com.example.traveljoin.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Group implements Serializable, GeneralItem {

	private Integer id;	
	private String name;
	private String description;
	private String type;
	private String password;
	private ArrayList<Interest> interests;
	private ArrayList<Poi> pois;
	private ArrayList<Tour> tours;
	private User owner;
	
	public Group(String name, String description, String type, String password,
			ArrayList<Interest> interests, ArrayList<Poi> pois,
			ArrayList<Tour> tours, User owner) {
		this.name = name  ;
		this.description = description;
		this.type = type;
		this.password = password;
		this.interests = interests;
		this.pois = pois;
		this.tours = tours;
		this.owner = owner;
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
	
	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Interest> getInterests() {
		return interests;
	}

	public void setInterests(ArrayList<Interest> interests) {
		this.interests = interests;
	}

	public ArrayList<Poi> getPois() {
		return pois;
	}

	public void setPois(ArrayList<Poi> pois) {
		this.pois = pois;
	}

	public ArrayList<Tour> getTours() {
		return tours;
	}

	public void setTours(ArrayList<Tour> tours) {
		this.tours = tours;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public static Group fromJSON(JSONObject groupJson) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public JSONObject toJSON(){
		
		JSONObject jsonObject= new JSONObject();
	    try {
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}	    	
	    	
	        jsonObject.put("name", getName());
	        jsonObject.put("description", getDescription());
	        jsonObject.put("group_type", getType());
	        jsonObject.put("password", getPassword());
	        jsonObject.put("user_id", getOwner().getId());
	        
	        JSONArray interestsJson = new JSONArray();	        
	        JSONObject interestJson= new JSONObject();
	        for (Interest interest : getInterests()) {
	        	interestJson.put("group_id", getId());
	        	interestJson.put("interest_id", interest.getId());
	        	interestsJson.put(interestJson);
			}
	        
	        if(interestsJson.length() > 0){
	        	jsonObject.put("groups_interests_attributes", interestsJson);
	        }
	        
	        JSONArray poisJson = new JSONArray();	        
	        JSONObject poiJson= new JSONObject();
	        for (Poi poi : getPois()) {
	        	poiJson.put("group_id", getId());
	        	poiJson.put("poi_id", poi.getId());
	        	poisJson.put(poiJson);
			}
	        
	        if(poisJson.length() > 0){
	        	jsonObject.put("groups_pois_attributes", poisJson);
	        }
	        
	        JSONObject groupJson = new JSONObject();
	        groupJson.put("group", jsonObject);	        
	        return groupJson;
	    } catch (JSONException e) {
	        //TODO: handlear error
	    	e.printStackTrace();
	        return jsonObject;
	    }
	    
	}

	

}