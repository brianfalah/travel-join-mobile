package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupTour implements Serializable, GeneralItem{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer groupId;
	private Integer tourId;
	private String tourName;
	private String tourDescription;
	private boolean deleted;		

	public GroupTour(Integer id, Integer groupId, Integer tourId,
			String tourName, String tourDescription) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.tourId = tourId;
		this.tourName = tourName;
		this.tourDescription = tourDescription;
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



	public Integer getTourId() {
		return tourId;
	}



	public void setTourId(Integer tourId) {
		this.tourId = tourId;
	}



	public String getTourName() {
		return tourName;
	}



	public void setTourName(String tourName) {
		this.tourName = tourName;
	}



	public String getTourDescription() {
		return tourDescription;
	}



	public void setTourDescription(String tourDescription) {
		this.tourDescription = tourDescription;
	}



	public static GroupTour fromJSON(JSONObject groupTourJson) throws JSONException, ParseException{	
		
		GroupTour groupTour = new GroupTour(groupTourJson.getInt("id"), groupTourJson.getInt("group_id"),
				groupTourJson.getInt("tour_id"), groupTourJson.getString("tour_name"),
				groupTourJson.getString("tour_description"));
						
		return groupTour;
	}
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}	    		    		    
	    	
	    	jsonObject.put("group_id", getGroupId());
	        jsonObject.put("tour_id", getTourId());
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
        return this.getTourName();            // Para que lo usen los adapters
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
		return this.getTourName();
	}

	@Override
	public String getDescription() {
		return this.getTourDescription();
	}
}
