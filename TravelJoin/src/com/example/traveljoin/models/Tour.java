package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tour implements Serializable, GeneralItem {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String description;
	private Integer userId;
	private User user;
	private Boolean isFavorite;
	private Rating rating;
	private Integer ratingsCount;
	private Double ratingsSum;
	private ArrayList<Rating> lastRatings;
	
	private ArrayList<TourPoi> tourPois;
	private ArrayList<TourPoi> tourPoisToDelete;
		
	public Tour(Integer id, String name, String description, Integer userId, ArrayList<GeneralItem> tourPois) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.tourPois = new ArrayList<TourPoi>();
		this.tourPois.clear();
		this.tourPois.addAll((Collection<? extends TourPoi>) tourPois);
		this.tourPoisToDelete = new ArrayList<TourPoi>();
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public ArrayList<TourPoi> getTourPois() {
		return tourPois;
	}

	public void setTourPois(ArrayList<GeneralItem> tourPois) {
		this.tourPois.clear();
		this.tourPois.addAll((Collection<? extends TourPoi>) tourPois);
	}

	public ArrayList<TourPoi> getTourPoisToDelete() {
		return tourPoisToDelete;
	}

	public void setTourPoisToDelete(ArrayList<TourPoi> tourPoisToDelete) {
		this.tourPoisToDelete = tourPoisToDelete;
	}
	
	public void updateTourPois(ArrayList<TourPoi> oldTourPois, ArrayList<GeneralItem> newTourPois){
		this.tourPois.addAll(oldTourPois);
		
		ArrayList<TourPoi> tourPoisToAdd = new ArrayList<TourPoi>();		
		ArrayList<Integer> newSelectedPoiIds = new ArrayList<Integer>();		
		ArrayList<Integer> oldSelectedPoiIds = new ArrayList<Integer>();
		
		//agregar los que no estaban y borrar los que no estan
		
		//vemos todos los nuevos seleccionados y armamos 1 array de TourPois y otro de Ids de Pois
		for (int j = 0; j < newTourPois.size(); j++) {
			newSelectedPoiIds.add(((TourPoi) newTourPois.get(j)).getPoiId());
		}
		
		 //vemos cuales vamos a borrar(los que estaban antes y no vinieron seleccionados ahora)
		for (int i = 0; i < getTourPois().size(); i++) {
			TourPoi tourPoi = getTourPois().get(i);
			oldSelectedPoiIds.add(tourPoi.getPoiId());
			
			//si no esta entre los nuevos lo seteamos como borrado
			if(!newSelectedPoiIds.contains(tourPoi.getPoiId()))
			{
				getTourPois().get(i).setDeleted(true);
			}
		}
		
		//vemos cuales vamos a crear(los que vinieron seleccionados ahora y no estaban antes)
		for (int i = 0; i < newTourPois.size(); i++) {
			TourPoi tourPoi = (TourPoi) newTourPois.get(i);
			tourPoi.setTourId(this.getId());
			if ( !oldSelectedPoiIds.contains(tourPoi.getPoiId()) ){						
				tourPoisToAdd.add(tourPoi);
			}
		}
		
		this.tourPois.addAll(tourPoisToAdd);
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

	public static Tour fromJSON(JSONObject tourJson) throws JSONException, ParseException{	
		ArrayList<GeneralItem> tourPoisToAdd = new ArrayList<GeneralItem>();
		JSONArray tourPoisJson = tourJson.getJSONArray("tours_pois");
		
		for (int i = 0; i < tourPoisJson.length(); i++) {
    	    JSONObject tourPoiJson = tourPoisJson.getJSONObject(i);    	        
    	    TourPoi tourPoi = TourPoi.fromJSON(tourPoiJson);
    	    tourPoisToAdd.add(tourPoi);    	    
    	}
		
		Tour tour = new Tour(tourJson.getInt("id"), tourJson.getString("name"),
				tourJson.getString("description"), tourJson.getInt("user_id"), tourPoisToAdd);
		
		User user = User.fromJSON(tourJson.getJSONObject("user"));
		tour.setUser(user);
		tour.setIsFavorite(tourJson.getBoolean("is_favorite"));
		
		if (tourJson.has("rating") && !tourJson.isNull("rating")){
			Rating rating = Rating.fromJSON(tourJson.getJSONObject("rating"));
			tour.setRating(rating);
		}
		
		if (tourJson.has("ratings_count") && !tourJson.isNull("ratings_count")){
			tour.setRatingsCount(tourJson.getInt("ratings_count"));
		}	
		
		if (tourJson.has("ratings_sum") && !tourJson.isNull("ratings_sum")){
			tour.setRatingsSum(tourJson.getDouble("ratings_sum"));
		}	
		
		if (tourJson.has("last_ratings") && !tourJson.isNull("last_ratings")){
			ArrayList<Rating> ratingsToAdd = new ArrayList<Rating>();
			JSONArray ratingsJson = tourJson.getJSONArray("last_ratings");
			
			for (int i = 0; i < ratingsJson.length(); i++) {
	    	    JSONObject ratingJson = ratingsJson.getJSONObject(i);    	        
	    	    Rating rating = Rating.fromJSON(ratingJson);
	    	    ratingsToAdd.add(rating);    	    
	    	}
			
			tour.setLastRatings(ratingsToAdd);
		}
		
		return tour;
	}
	
	public JSONObject toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	    	if (getId() != null){
	    		jsonObject.put("id", getId());
	    	}	    	
	    	
	        jsonObject.put("name", getName());
	        jsonObject.put("description", getDescription());
	        jsonObject.put("user_id", getUserId());
	        
	        //Integer orderNumber = 1;
	        
	        JSONArray tourPoisJson = new JSONArray();	        
	        for (int i = 0; i < getTourPois().size(); i++) {
	        	TourPoi tourPoi = getTourPois().get(i);
	        	
//	        	if (!tourPoi.isDeleted()){
//	        		tourPoi.setOrderNumber(orderNumber);
//	        		orderNumber += 1;
//	        	}
	        	tourPoisJson.put(tourPoi.toJSON());
			}
	        	        
//	        for (int i = 0; i < getTourPoisToDelete().size(); i++) {
//	        	tourPoisJson.put(getTourPoisToDelete().get(i).toJSON());
//			}
	        if(tourPoisJson.length() > 0){
	        	jsonObject.put("tours_pois_attributes", tourPoisJson);
	        }
	        
	        
	        JSONObject finalobject = new JSONObject();
	        finalobject.put("tour", jsonObject);	        
	        return finalobject;
	    } catch (JSONException e) {
	        e.printStackTrace();
	        return jsonObject;
	    }

	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Tour) {
			Tour tour = (Tour) object;
			return this.id.equals(tour.id);
		}
		else{
			if (object instanceof GroupTour) {
				GroupTour groupTour = (GroupTour) object;
				return this.id.equals(groupTour.getTourId());
			}
			else{
				return false;
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
