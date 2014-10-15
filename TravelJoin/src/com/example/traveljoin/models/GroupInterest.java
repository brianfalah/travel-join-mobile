package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupInterest implements Serializable, GeneralItem{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer groupId;
	private Integer interestId;
	private String interestName;
	private boolean deleted;
	
	public GroupInterest(Integer id, Integer groupId, Integer interestId,
			String interestName) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.interestId = interestId;
		this.interestName = interestName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getInterestId() {
		return interestId;
	}

	public void setInterestId(Integer interestId) {
		this.interestId = interestId;
	}

	public String getInterestName() {
		return interestName;
	}

	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}

	public static GroupInterest fromJSON(JSONObject groupInterestJson) throws JSONException, ParseException{	
		
		GroupInterest groupInterest = new GroupInterest(
				groupInterestJson.getInt("id"),
				groupInterestJson.getInt("group_id"),
				groupInterestJson.getInt("interest_id"),
				groupInterestJson.getString("interest_name"));
						
		return groupInterest;
	}
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}	    		    		    
	    	
	    	jsonObject.put("group_id", getGroupId());
	        jsonObject.put("interest_id", getInterestId());
	        if (isDeleted() == true){
	    		jsonObject.put("_destroy", "true");
	    	}
	        
	        return jsonObject;
	    } catch (JSONException e) {
	        e.printStackTrace();
	        return jsonObject;
	    }

	}
	
    @Override
    public String toString() {
        return this.getInterestName();            // Para que lo usen los adapters
    }

    //marcamos el tour poi como borrado, porque ruby lo necesita asi     
	public void markAsDeleted() {
		this.setDeleted(true);		
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String getName() {
		return this.getInterestName();
	}

	@Override
	public String getDescription() {
		return "";
	}
}
