package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Group implements Serializable, GeneralItem {

	private Integer id;
	private String name;
	private String description;
	private Integer type;
	private String password;
	private ArrayList<Interest> interests;
	private ArrayList<Poi> pois;
	private ArrayList<Tour> tours;
	private Integer userId;
	
	public Group(String name, String description, Integer type, String password,
			ArrayList<Interest> interests, ArrayList<Poi> pois,
			ArrayList<Tour> tours, Integer userId) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.password = password;
		this.interests = interests;
		this.pois = pois;
		this.tours = tours;
		this.userId = userId;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public static Group fromJSON(JSONObject groupJson) throws JSONException, ParseException {
		JSONArray interestsJson = groupJson.getJSONArray("interests");
		JSONArray poisJson = groupJson.getJSONArray("pois");
		JSONArray toursJson = groupJson.getJSONArray("tours");
		ArrayList<Interest> interests = new ArrayList<Interest>();
		ArrayList<Poi> pois = new ArrayList<Poi>();
		ArrayList<Tour> tours = new ArrayList<Tour>();

		for (int index = 0; index < interestsJson.length(); index++) {
			JSONObject interestJson = interestsJson.getJSONObject(index);
			Interest interest = Interest.fromJSON(interestJson);
			interests.add(interest);
		}
		
		for (int index = 0; index < poisJson.length(); index++) {
			JSONObject poiJson = poisJson.getJSONObject(index);
			Poi poi = Poi.fromJSON(poiJson);
			pois.add(poi);
		}
		
		for (int index = 0; index < toursJson.length(); index++) {
			JSONObject tourJson = toursJson.getJSONObject(index);
			Tour tour = Tour.fromJSON(tourJson);
			tours.add(tour);
		}

		Group group = new Group(groupJson.getString("name"),
				groupJson.getString("description"),
				groupJson.getInt("group_type"),
				groupJson.getString("password"), interests, pois, tours,
				groupJson.getInt("user_id"));

		group.setId(groupJson.getInt("id"));
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

			JSONArray interestsJson = new JSONArray();
			JSONObject interestJson = new JSONObject();
			for (Interest interest : getInterests()) {
				interestJson.put("group_id", getId());
				interestJson.put("interest_id", interest.getId());
				interestsJson.put(interestJson);
			}

			if (interestsJson.length() > 0) {
				jsonObject.put("groups_interests_attributes", interestsJson);
			}

			JSONArray poisJson = new JSONArray();
			JSONObject poiJson = new JSONObject();
			for (Poi poi : getPois()) {
				poiJson.put("group_id", getId());
				poiJson.put("poi_id", poi.getId());
				poisJson.put(poiJson);
			}

			if (poisJson.length() > 0) {
				jsonObject.put("groups_pois_attributes", poisJson);
			}
			
			JSONArray toursJson = new JSONArray();
			JSONObject tourJson = new JSONObject();
			for (Poi poi : getPois()) {
				tourJson.put("group_id", getId());
				tourJson.put("tour_id", poi.getId());
				toursJson.put(tourJson);
			}

			if (toursJson.length() > 0) {
				jsonObject.put("groups_tours_attributes", tourJson);
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