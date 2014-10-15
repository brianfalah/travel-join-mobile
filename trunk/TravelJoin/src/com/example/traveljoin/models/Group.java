package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Group implements Serializable, GeneralItem {

	private Integer id;
	private String name;
	private String description;
	private Integer type;
	private String password;
	private Integer userId;
	
	private ArrayList<GroupInterest> groupInterests;
	private ArrayList<GroupInterest> groupInterestsToDelete;
	private ArrayList<GroupPoi> groupPois;
	private ArrayList<GroupPoi> groupPoisToDelete;	
	private ArrayList<GroupTour> groupTours;
	private ArrayList<GroupTour> groupToursToDelete;
	

	
	
	public Group(Integer id, String name, String description, Integer type, String password,
			Integer userId, ArrayList<GeneralItem> groupInterests,
			ArrayList<GeneralItem> groupPois, ArrayList<GeneralItem> groupTours) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.password = password;
		this.userId = userId;
		
		this.groupInterests = new ArrayList<GroupInterest>();
		this.groupInterests.clear();
		this.groupInterests.addAll((Collection<? extends GroupInterest>) groupInterests);
		this.groupInterestsToDelete = new ArrayList<GroupInterest>();
		
		this.groupPois= new ArrayList<GroupPoi>();
		this.groupPois.clear();
		this.groupPois.addAll((Collection<? extends GroupPoi>) groupPois);
		this.groupPoisToDelete = new ArrayList<GroupPoi>();		
		
		this.groupTours = new ArrayList<GroupTour>();
		this.groupTours.clear();
		this.groupTours.addAll((Collection<? extends GroupTour>) groupTours);
		this.groupToursToDelete = new ArrayList<GroupTour>();
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
	
	public ArrayList<GroupPoi> getGroupPois() {
		return groupPois;
	}

	public void setGroupPois(ArrayList<GeneralItem> groupPois) {
		this.groupPois.clear();
		this.groupPois.addAll((Collection<? extends GroupPoi>) groupPois);
	}

	public ArrayList<GroupPoi> getGroupPoisToDelete() {
		return groupPoisToDelete;
	}

	public void setGroupPoisToDelete(ArrayList<GroupPoi> groupPoisToDelete) {
		this.groupPoisToDelete = groupPoisToDelete;
	}
	
	

	public ArrayList<GroupInterest> getGroupInterests() {
		return groupInterests;
	}

	public void setGroupInterests(ArrayList<GeneralItem> groupInterests) {
		this.groupInterests.clear();
		this.groupInterests.addAll((Collection<? extends GroupInterest>) groupInterests);
	}

	public ArrayList<GroupInterest> getGroupInterestsToDelete() {
		return groupInterestsToDelete;
	}

	public void setGroupInterestsToDelete(
			ArrayList<GroupInterest> groupInterestsToDelete) {
		this.groupInterestsToDelete = groupInterestsToDelete;
	}

	public ArrayList<GroupTour> getGroupTours() {
		return groupTours;
	}

	public void setGroupTours(ArrayList<GeneralItem> groupTours) {
		this.groupTours.clear();
		this.groupTours.addAll((Collection<? extends GroupTour>) groupTours);
	}

	public ArrayList<GroupTour> getGroupToursToDelete() {
		return groupToursToDelete;
	}

	public void setGroupToursToDelete(ArrayList<GroupTour> groupToursToDelete) {
		this.groupToursToDelete = groupToursToDelete;
	}

	public static Group fromJSON(JSONObject groupJson) throws JSONException, ParseException {
		JSONArray groupsInterestsJson = groupJson.getJSONArray("groups_interests");
		JSONArray groupsPoisJson = groupJson.getJSONArray("groups_pois");
		JSONArray groupsToursJson = groupJson.getJSONArray("groups_tours");
				
		ArrayList<GeneralItem> groupInterestsToAdd = new ArrayList<GeneralItem>();
		ArrayList<GeneralItem> groupPoisToAdd = new ArrayList<GeneralItem>();
		ArrayList<GeneralItem> groupToursToAdd = new ArrayList<GeneralItem>();

		for (int index = 0; index < groupsInterestsJson.length(); index++) {
			JSONObject groupsInterestJson = groupsInterestsJson.getJSONObject(index);
			GroupInterest groupInterest = GroupInterest.fromJSON(groupsInterestJson);
			groupInterestsToAdd.add(groupInterest);
		}
		
		for (int index = 0; index < groupsPoisJson.length(); index++) {
			JSONObject groupsPoiJson = groupsPoisJson.getJSONObject(index);
			GroupPoi groupPoi = GroupPoi.fromJSON(groupsPoiJson);
			groupPoisToAdd.add(groupPoi);
		}
		
		for (int index = 0; index < groupsToursJson.length(); index++) {
			JSONObject groupsTourJson = groupsToursJson.getJSONObject(index);
			GroupTour groupTour = GroupTour.fromJSON(groupsTourJson);
			groupToursToAdd.add(groupTour);
		}

		Group group = new Group(groupJson.getInt("id"),
				groupJson.getString("name"),
				groupJson.getString("description"),
				groupJson.getInt("group_type"),
				groupJson.getString("password"),
				groupJson.getInt("user_id"),
				groupInterestsToAdd,
				groupPoisToAdd,
				groupToursToAdd);

		return group;
	}

	public JSONObject toJSON() {

		JSONObject jsonObject = new JSONObject();
		try {
			if (getId() != null) {
				jsonObject.put("id", getId());
			}

			jsonObject.put("name", getName());
			jsonObject.put("description", getDescription());
			jsonObject.put("group_type", getType());
			jsonObject.put("password", getPassword());
			jsonObject.put("user_id", getUserId());

			JSONArray groupInterestsJson = new JSONArray();	        
			for (int i = 0; i < getGroupInterests().size(); i++) {
	        	GroupInterest groupInterest = getGroupInterests().get(i);
	        	groupInterestsJson.put(groupInterest.toJSON());
			}
	        	        
	        for (int i = 0; i < getGroupInterestsToDelete().size(); i++) {
	        	groupInterestsJson.put(getGroupInterestsToDelete().get(i).toJSON());
			}
	        if(groupInterestsJson.length() > 0){
	        	jsonObject.put("groups_interests_attributes", groupInterestsJson);
	        }
	        
	        
	        JSONArray groupPoisJson = new JSONArray();	        
			for (int i = 0; i < getGroupPois().size(); i++) {
	        	GroupPoi groupPoi = getGroupPois().get(i);
	        	groupPoisJson.put(groupPoi.toJSON());
			}
	        	        
	        for (int i = 0; i < getGroupPoisToDelete().size(); i++) {
	        	groupPoisJson.put(getGroupPoisToDelete().get(i).toJSON());
			}
	        if(groupPoisJson.length() > 0){
	        	jsonObject.put("groups_pois_attributes", groupPoisJson);
	        }
	        
	        
	        JSONArray groupToursJson = new JSONArray();	        
			for (int i = 0; i < getGroupTours().size(); i++) {
	        	GroupTour groupTour = getGroupTours().get(i);
	        	groupToursJson.put(groupTour.toJSON());
			}
	        	        
	        for (int i = 0; i < getGroupToursToDelete().size(); i++) {
	        	groupToursJson.put(getGroupToursToDelete().get(i).toJSON());
			}
	        if(groupToursJson.length() > 0){
	        	jsonObject.put("groups_tours_attributes", groupToursJson);
	        }
	        
	        
			JSONObject groupJson = new JSONObject();
			groupJson.put("group", jsonObject);
			return groupJson;
		} catch (JSONException e) {
			// TODO: handlear error
			e.printStackTrace();
			return jsonObject;
		}

	}

}