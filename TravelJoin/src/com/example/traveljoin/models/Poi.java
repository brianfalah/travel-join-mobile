package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Poi implements Serializable, GeneralItem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Double latitude;
	private Double longitude;
	private String name;
	private String description;
	private String address;	
	private Integer userId;
	private User user;
	private Integer categoryId;
	private String categoryName;
	private Boolean isFavorite;
	private Rating rating;
	private Integer ratingsCount;
	private Double ratingsSum;
	private ArrayList<Rating> lastRatings;
	private Boolean denounced; 
	
	private ArrayList<PoiEvent> poiEvents;
	private ArrayList<PoiEvent> poiEventsToDelete;	
		
	//constructor
	public Poi(Integer id, Double latitude, Double longitude, String name,
			String description,String address, Integer userId,
			Integer categoryId, String categoryName, ArrayList<GeneralItem> poiEvents) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.description = description;
		this.address = address;
		this.userId = userId;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.poiEvents = new ArrayList<PoiEvent>();
		this.poiEvents.clear();
		this.poiEvents.addAll((Collection<? extends PoiEvent>) poiEvents);
		this.poiEventsToDelete = new ArrayList<PoiEvent>();
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public ArrayList<PoiEvent> getPoiEvents() {
		return poiEvents;
	}

	public void setPoiEvents(ArrayList<GeneralItem> poiEvents) {
		this.poiEvents.clear();
		this.poiEvents.addAll((Collection<? extends PoiEvent>) poiEvents);
	}

	public Boolean getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public Integer getRatingsCount() {
		return ratingsCount;
	}


	public void setRatingsCount(Integer ratingsCount) {
		this.ratingsCount = ratingsCount;
	}


	public Double getRatingsSum() {
		return ratingsSum;
	}


	public void setRatingsSum(Double ratingsSum) {
		this.ratingsSum = ratingsSum;
	}


	public ArrayList<Rating> getLastRatings() {
		return lastRatings;
	}


	public void setLastRatings(ArrayList<Rating> lastRatings) {
		this.lastRatings = lastRatings;
	}


	public Boolean getDenounced() {
		return denounced;
	}


	public void setDenounced(Boolean denounced) {
		this.denounced = denounced;
	}


	public static Poi fromJSON(JSONObject poiJson) throws JSONException, ParseException{	
		ArrayList<GeneralItem> poiEventsToAdd = new ArrayList<GeneralItem>();
		if (poiJson.has("active_events") && !poiJson.isNull("active_events")){								
			JSONArray poiEventsJson = poiJson.getJSONArray("active_events");
			
			for (int i = 0; i < poiEventsJson.length(); i++) {
	    	    JSONObject poiEventJson = poiEventsJson.getJSONObject(i);    	        
	    	    PoiEvent poiEvent = PoiEvent.fromJSON(poiEventJson);    	    
	    	    poiEventsToAdd.add(poiEvent);    	
	    	        	       
	    	}
		}
		
		Poi poi = new Poi(poiJson.getInt("id"), poiJson.getDouble("latitude"), poiJson.getDouble("longitude"),
	    		poiJson.getString("name"), poiJson.getString("description"), poiJson.getString("address"),
	    		poiJson.getInt("user_id"), poiJson.getInt("category_id"), poiJson.getString("category_name"), poiEventsToAdd);
		
		User user = User.fromJSON(poiJson.getJSONObject("user"));
		poi.setUser(user);
		
		poi.setIsFavorite(poiJson.getBoolean("is_favorite"));
		
		//esto es el objeto rating, si es que el usuario logueado hizo una calificacion, sino viene en null
		if (poiJson.has("rating") && !poiJson.isNull("rating")){
			Rating rating = Rating.fromJSON(poiJson.getJSONObject("rating"));
			poi.setRating(rating);
		}
	
		
		if (poiJson.has("ratings_count") && !poiJson.isNull("ratings_count")){
			poi.setRatingsCount(poiJson.getInt("ratings_count"));
		}	
		
		if (poiJson.has("ratings_sum") && !poiJson.isNull("ratings_sum")){
			poi.setRatingsSum(poiJson.getDouble("ratings_sum"));
		}	
		
		if (poiJson.has("last_ratings") && !poiJson.isNull("last_ratings")){
			ArrayList<Rating> ratingsToAdd = new ArrayList<Rating>();
			JSONArray ratingsJson = poiJson.getJSONArray("last_ratings");
			
			for (int i = 0; i < ratingsJson.length(); i++) {
	    	    JSONObject ratingJson = ratingsJson.getJSONObject(i);    	        
	    	    Rating rating = Rating.fromJSON(ratingJson);
	    	    ratingsToAdd.add(rating);    	    
	    	}
			
			poi.setLastRatings(ratingsToAdd);
		}		
	
		if (poiJson.has("denounced") && !poiJson.isNull("denounced")){
			poi.setDenounced(poiJson.getBoolean("denounced"));
		}
		return poi;
	}
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}	    	
	    	
	        jsonObject.put("name", getName());
	        jsonObject.put("description", getDescription());
	        jsonObject.put("address", getAddress());
	        jsonObject.put("latitude", getLatitude());
	        jsonObject.put("longitude", getLongitude());
	        jsonObject.put("category_id", getCategoryId());
	        jsonObject.put("user_id", getUserId());
	        
	        JSONArray eventsJson = new JSONArray();	        
	        for (int i = 0; i < getPoiEvents().size(); i++) {
	        	eventsJson.put(getPoiEvents().get(i).toJSON());
			}
	        	        
	        for (int i = 0; i < getPoiEventsToDelete().size(); i++) {
	        	eventsJson.put(getPoiEventsToDelete().get(i).toJSON());
			}
	        if(eventsJson.length() > 0){
	        	jsonObject.put("events_attributes", eventsJson);
	        }
	        
	        
	        JSONObject finalobject = new JSONObject();
	        finalobject.put("poi", jsonObject);	        
	        return finalobject;
	    } catch (JSONException e) {
	        e.printStackTrace();
	        return jsonObject;
	    }

	}

	public ArrayList<PoiEvent> getPoiEventsToDelete() {
		return poiEventsToDelete;
	}
	
	public void setPoiEventsToDelete(ArrayList<PoiEvent> poiEvents) {
		this.poiEventsToDelete = poiEvents;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Poi) {
			Poi poi = (Poi) object;
			return this.id.equals(poi.id);
		}
		else{
			if (object instanceof TourPoi) {
				TourPoi tourPoi = (TourPoi) object;
				return this.id.equals(tourPoi.getPoiId());
			}
			else{
				if (object instanceof GroupPoi) {
					GroupPoi groupPoi = (GroupPoi) object;
					return this.id.equals(groupPoi.getPoiId());
				}
				else{
					return false;
				}
			}			
		}		
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}


	public Double getRatingAvg() {
		if (getRatingsCount().equals(0)){
			return 0.0;
		}
		else{
			return (getRatingsSum() / getRatingsCount());
		}		
	}


	public float getRatingForBar() {
		return (float) (Math.ceil(getRatingAvg() * 2) / 2);		
	}
}
