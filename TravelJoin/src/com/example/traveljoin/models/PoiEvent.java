package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class PoiEvent implements Serializable, GeneralItem{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String description;	
	private Integer poiId;
	private Calendar fromDate;
	private Calendar toDate;
	private boolean deleted;
	
	public PoiEvent(Integer id, String name, String description, Integer poiId,
			Calendar fromDate, Calendar toDate) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.poiId = poiId;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPoiId() {
		return poiId;
	}

	public void setPoiId(Integer poiId) {
		this.poiId = poiId;
	}

	public Calendar getFromDate() {
		return fromDate;
	}

	public void setFromDate(Calendar fromDate) {
		this.fromDate = fromDate;
	}

	public Calendar getToDate() {
		return toDate;
	}

	public void setToDate(Calendar toDate) {
		this.toDate = toDate;
	}
	
	public static PoiEvent fromJSON(JSONObject poiEventJson) throws JSONException, ParseException{	
		Calendar dateFrom = TimeFormatter.toCalendar(poiEventJson.getString("from_date"));		
		Calendar dateTo = TimeFormatter.toCalendar(poiEventJson.getString("to_date"));
		
		PoiEvent poiEvent = new PoiEvent(poiEventJson.getInt("id"), poiEventJson.getString("name"),
				poiEventJson.getString("description"), poiEventJson.getInt("poi_id"), dateFrom, dateTo);
						
		return poiEvent;
	}
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}	    		    		    
	    	
	        jsonObject.put("name", getName());
	        jsonObject.put("description", getDescription());
	        jsonObject.put("poi_id", getPoiId());
	        jsonObject.put("from_date", TimeFormatter.fromCalendar(getFromDate()));
	        jsonObject.put("to_date", TimeFormatter.fromCalendar(getToDate()));
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
        return this.getName();            // Para que lo usen los adapters
    }

    //marcamos el evento como borrado, porque ruby lo necesita asi     
	public void markAsDeleted() {
		this.setDeleted(true);		
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
