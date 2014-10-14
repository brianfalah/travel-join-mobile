package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class TourPoi implements Serializable, GeneralItem{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer tourId;
	private Integer poiId;
	private String poiName;
	private String poiDescription;
	private Integer orderNumber;
	private boolean deleted;
	
	public TourPoi(Integer id, Integer tourId, Integer poiId,
			String poiName, String poiDescription, Integer orderNumber) {
		super();
		this.id = id;
		this.tourId = tourId;
		this.poiId = poiId;
		this.poiName = poiName;
		this.poiDescription = poiDescription;
		this.orderNumber = orderNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTourId() {
		return tourId;
	}

	public void setTourId(Integer tourId) {
		this.tourId = tourId;
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

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getPoiDescription() {
		return poiDescription;
	}

	public void setPoiDescription(String poiDescription) {
		this.poiDescription = poiDescription;
	}

	public static TourPoi fromJSON(JSONObject tourPoiJson) throws JSONException, ParseException{	
		
		TourPoi tourPoi = new TourPoi(tourPoiJson.getInt("id"), tourPoiJson.getInt("tour_id"),
				tourPoiJson.getInt("poi_id"), tourPoiJson.getString("poi_name"),
				tourPoiJson.getString("poi_description"), tourPoiJson.getInt("order_number"));
						
		return tourPoi;
	}
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}	    		    		    
	    	
	    	jsonObject.put("tour_id", getTourId());
	        jsonObject.put("poi_id", getPoiId());
	        jsonObject.put("order_number", getOrderNumber());
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
