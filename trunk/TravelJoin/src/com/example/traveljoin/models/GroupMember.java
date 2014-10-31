package com.example.traveljoin.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupMember implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private Integer groupId;		

	public GroupMember(Integer userId, Integer groupId) {
		this.userId = userId;
		this.groupId = groupId;		
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
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {  		    		    	    	
	    	jsonObject.put("user_id", getUserId());
	        jsonObject.put("group_id", getGroupId());
	        return jsonObject;
	    } catch (JSONException e) {
	        //TODO: Handlear?
	    	e.printStackTrace();
	        return jsonObject;
	    }

	}

}
