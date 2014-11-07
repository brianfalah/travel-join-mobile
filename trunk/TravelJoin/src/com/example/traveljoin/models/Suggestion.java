package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.example.traveljoin.R;

public class Suggestion implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId;
	private Integer groupId;	
	//id del poi o del tour
	private Integer suggestionableId;
	//tipo (Poi o Tour)
	private String suggestionableType;
	private String suggestionableName;
	private String suggestionableDescription;

	
	public Suggestion(Integer id, Integer userId, Integer groupId,
			Integer suggestionableId, String suggestionableType) {
		super();
		this.id = id;
		this.userId = userId;
		this.groupId = groupId;
		this.suggestionableId = suggestionableId;
		this.suggestionableType = suggestionableType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getSuggestionableId() {
		return suggestionableId;
	}

	public void setSuggestionableId(Integer suggestionableId) {
		this.suggestionableId = suggestionableId;
	}

	public String getSuggestionableType() {
		return suggestionableType;
	}

	public void setSuggestionableType(String suggestionableType) {
		this.suggestionableType = suggestionableType;
	}

	public String getSuggestionableName() {
		return suggestionableName;
	}

	public void setSuggestionableName(String suggestionableName) {
		this.suggestionableName = suggestionableName;
	}

	public String getSuggestionableDescription() {
		return suggestionableDescription;
	}

	public void setSuggestionableDescription(String suggestionableDescription) {
		this.suggestionableDescription = suggestionableDescription;
	}
	
	public Boolean ofPoi(){
		return getSuggestionableType().equals("Poi");
	}
	
	public Boolean ofTour(){
		return getSuggestionableType().equals("Tour");
	}
	
	public String getSuggestionableTypeDescription(Context context){
		if (ofPoi())
			return context.getString(R.string.poi);
		if (ofTour())
			return context.getString(R.string.tour);
		return "";
	}

	public static Suggestion fromJSON(JSONObject jsonObject) throws JSONException, ParseException {
		Suggestion suggestion = new Suggestion(jsonObject.getInt("id"), jsonObject.getInt("user_id"), jsonObject.getInt("group_id"),
				jsonObject.getInt("suggestionable_id"), jsonObject.getString("suggestionable_type"));
		
		if (jsonObject.has("suggestionable_name") && !jsonObject.isNull("suggestionable_name")){
			suggestion.setSuggestionableName(jsonObject.getString("suggestionable_name"));
		}
		
		if (jsonObject.has("suggestionable_description") && !jsonObject.isNull("suggestionable_description")){
			suggestion.setSuggestionableDescription(jsonObject.getString("suggestionable_description"));
		}
		
		return suggestion;
	}
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try { 
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}
	    	jsonObject.put("user_id", getUserId());
	    	jsonObject.put("group_id", getGroupId());
	        jsonObject.put("suggestionable_id", getSuggestionableId());
	        jsonObject.put("suggestionable_type", getSuggestionableType());
	        return jsonObject;
	    } catch (JSONException e) {
	        //TODO: Handlear?
	        e.printStackTrace();
	        return jsonObject;
	    }

	}
	
}
