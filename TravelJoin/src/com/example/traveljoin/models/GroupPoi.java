package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupPoi implements Serializable, GeneralItem{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer groupId;
	private Integer poiId;
	private String poiName;
	private String poiDescription;
	private boolean deleted;
	
	public GroupPoi(Integer id, Integer groupId, Integer poiId,
			String poiName, String poiDescription) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.poiId = poiId;
		this.poiName = poiName;
		this.poiDescription = poiDescription;
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

	public Integer getPoiId() {
		return poiId;
	}

	public void setPoiId(Integer poiId) {
		this.poiId = poiId;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public String getPoiDescription() {
		return poiDescription;
	}

	public void setPoiDescription(String poiDescription) {
		this.poiDescription = poiDescription;
	}

	public static GroupPoi fromJSON(JSONObject groupPoiJson) throws JSONException, ParseException{	
		
		GroupPoi groupPoi = new GroupPoi(groupPoiJson.getInt("id"), groupPoiJson.getInt("group_id"),
				groupPoiJson.getInt("poi_id"), groupPoiJson.getString("poi_name"),
				groupPoiJson.getString("poi_description"));
						
		return groupPoi;
	}
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}	    		    		    
	    	
	    	jsonObject.put("group_id", getGroupId());
	        jsonObject.put("poi_id", getPoiId());
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
        return this.getPoiName();            // Para que lo usen los adapters
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
		return this.getPoiName();
	}

	@Override
	public String getDescription() {
		return this.getPoiDescription();
	}
}
