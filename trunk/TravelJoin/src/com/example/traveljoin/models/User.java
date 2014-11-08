package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private String facebookId;
	private Integer id;

	private String email;
	private String name;
	private String surname;
	private ArrayList<GeneralItem> favoritePois;
	private ArrayList<GeneralItem> ownPois;
	private ArrayList<GeneralItem> favoriteTours;
	private ArrayList<GeneralItem> ownTours;
	private ArrayList<GeneralItem> groups;
	private ArrayList<GeneralItem> ownGroups;
	

	public User(String facebookId, Integer id, String name, String surname) {
		super();
		this.facebookId = facebookId;
		this.id = id;
		this.name = name;
		this.surname = surname;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFullName() {
		return getName() + " " + getSurname();
	}

	public ArrayList<GeneralItem> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<GeneralItem> groups) {
		this.groups = groups;
	}
	
	public void addGroup(Group group) {
		this.groups.add(group);
	}
	
	public void deleteGroup(Group group) {
		int index = 0;
		for (int i = 0; i < this.groups.size(); i++) {
			if (this.groups.get(i).getId().equals(group.getId())){
				index = i;
				break;
			}
		}
		this.groups.remove(index);
	}
	
	public ArrayList<GeneralItem> getOwnGroups() {
		return ownGroups;
	}

	public void setOwnGroups(ArrayList<GeneralItem> ownGroups) {
		this.ownGroups = ownGroups;
	}
	
	private void setOwnPois(ArrayList<GeneralItem> ownPois) {
		this.ownPois = ownPois;		
	}
	
	public ArrayList<GeneralItem> getOwnPois() {
		return ownPois;
	}
	
	private void setFavoritePois(ArrayList<GeneralItem> favoritePoisToAdd) {
		this.favoritePois = favoritePoisToAdd;	
	}
	
	public ArrayList<GeneralItem> getFavoritePois() {
		return favoritePois;
	}
	
	private void setOwnTours(ArrayList<GeneralItem> ownTours) {
		this.ownTours = ownTours;		
	}
	
	public ArrayList<GeneralItem> getOwnTours() {
		return ownTours;
	}
	
	private void setFavoriteTours(ArrayList<GeneralItem> favoriteTours) {
		this.favoriteTours = favoriteTours;	
	}
	
	public ArrayList<GeneralItem> getFavoriteTours() {
		return favoriteTours;
	}

	public static User fromJSON(JSONObject jsonObject) throws JSONException, ParseException {
		User user = new User(jsonObject.getString("facebook_id"),
				jsonObject.getInt("id"), jsonObject.getString("name"),
				jsonObject.getString("surname"));
		
		if (jsonObject.has("pois") && !jsonObject.isNull("pois")){
			ArrayList<GeneralItem> ownedPoisToAdd = new ArrayList<GeneralItem>();
			JSONArray poisJson = jsonObject.getJSONArray("pois");
			
			for (int i = 0; i < poisJson.length(); i++) {
	    	    JSONObject poiJson = poisJson.getJSONObject(i);    	        
	    	    Poi poi = Poi.fromJSON(poiJson);
	    	    ownedPoisToAdd.add(poi);    	    
	    	}
			user.setOwnPois(ownedPoisToAdd);
		}
		
		if (jsonObject.has("favorite_pois") && !jsonObject.isNull("favorite_pois")){
			ArrayList<GeneralItem> favoritePoisToAdd = new ArrayList<GeneralItem>();
			JSONArray favoritePoisJson = jsonObject.getJSONArray("favorite_pois");
			
			for (int i = 0; i < favoritePoisJson.length(); i++) {
	    	    JSONObject favoritePoiJson = favoritePoisJson.getJSONObject(i);    	        
	    	    Poi favoritePoi = Poi.fromJSON(favoritePoiJson);
	    	    favoritePoisToAdd.add(favoritePoi);    	    
	    	}
			user.setFavoritePois(favoritePoisToAdd);
		}
		
		if (jsonObject.has("tours") && !jsonObject.isNull("tours")){
			ArrayList<GeneralItem> ownToursToAdd = new ArrayList<GeneralItem>();
			JSONArray toursJson = jsonObject.getJSONArray("tours");
			
			for (int i = 0; i < toursJson.length(); i++) {
	    	    JSONObject tourJson = toursJson.getJSONObject(i);    	        
	    	    Tour tour = Tour.fromJSON(tourJson);
	    	    ownToursToAdd.add(tour);    	    
	    	}
			user.setOwnTours(ownToursToAdd);
		}
		
		if (jsonObject.has("favorite_tours") && !jsonObject.isNull("favorite_tours")){
			ArrayList<GeneralItem> favoriteToursToAdd = new ArrayList<GeneralItem>();
			JSONArray favoriteToursJson = jsonObject.getJSONArray("favorite_tours");
			
			for (int i = 0; i < favoriteToursJson.length(); i++) {
	    	    JSONObject favoriteTourJson = favoriteToursJson.getJSONObject(i);    	        
	    	    Tour favoriteTour = Tour.fromJSON(favoriteTourJson);
	    	    favoriteToursToAdd.add(favoriteTour);    	    
	    	}
			user.setFavoriteTours(favoriteToursToAdd);
		}
		
		if (jsonObject.has("groups") && !jsonObject.isNull("groups")){
			ArrayList<GeneralItem> groupsToAdd = new ArrayList<GeneralItem>();
			JSONArray groupsJson = jsonObject.getJSONArray("groups");
			
			for (int i = 0; i < groupsJson.length(); i++) {
	    	    JSONObject groupJson = groupsJson.getJSONObject(i);    	        
	    	    Group group = Group.fromJSON(groupJson);
	    	    group.joined(true);
	    	    groupsToAdd.add(group);    	    
	    	}
			user.setGroups(groupsToAdd);
		}
		
		if (jsonObject.has("groups_owned") && !jsonObject.isNull("groups_owned")){
			ArrayList<GeneralItem> ownGroupsToAdd = new ArrayList<GeneralItem>();
			JSONArray groupsJson = jsonObject.getJSONArray("groups_owned");
			
			for (int i = 0; i < groupsJson.length(); i++) {
	    	    JSONObject groupJson = groupsJson.getJSONObject(i);    	        
	    	    Group ownGroup = Group.fromJSON(groupJson);
	    	    ownGroupsToAdd.add(ownGroup);    	    
	    	}
			user.setOwnGroups(ownGroupsToAdd);
		}
		
		return user;
	}

	public JSONObject toJSON() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("facebook_id", getFacebookId());
			jsonObject.put("name", getName());
			jsonObject.put("surname", getSurname());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
