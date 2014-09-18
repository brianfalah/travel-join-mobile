package com.example.traveljoin.models;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.example.traveljoin.R;

public class MapFilter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private String latitude;
	private String longitude;
	private Integer maxDistance;
	private ArrayList<Integer> categoriesIds;
	private ArrayList<Integer> groupsIds;
	
	public MapFilter(Integer userId, String latitude, String longitude, Integer maxDistance) {
		this.userId = userId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.maxDistance = maxDistance;
		this.categoriesIds = new ArrayList<Integer>();
		this.groupsIds = new ArrayList<Integer>();
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public Integer getMaxDistance() {
		return maxDistance;
	}
	public void setMaxDistance(Integer distance) {
		this.maxDistance = distance;
	}
	
	public ArrayList<Integer> getCategoriesIds() {
		return categoriesIds;
	}
	
	public String getCategoriesInString() {
		return categoriesIds.toString();
	}
	
	public void setCategoriesIds(ArrayList<Integer> categoriesIds) {
		this.categoriesIds = categoriesIds;
	}
	public ArrayList<Integer> getGroupsIds() {
		return groupsIds;
	}
	public void setGroupsIds(ArrayList<Integer> groupsIds) {
		this.groupsIds = groupsIds;
	}	
	
	public String getUrlParams(){		
		String defaultFilter = "latitude=" + latitude + "&longitude=" + longitude + "&user_id=" + userId + "&max_distance=" + this.maxDistance;		
		if (!this.categoriesIds.isEmpty()){
			try {
				defaultFilter += "&categories_ids=" + URLEncoder.encode(categoriesIds.toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				defaultFilter = "";
				e.printStackTrace();
			}			
		}
		if (!this.groupsIds.isEmpty()){
			try {
				defaultFilter += "&groups_ids=" + URLEncoder.encode(groupsIds.toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				defaultFilter = "";
				e.printStackTrace();
			}			
		}
		return defaultFilter;
	}
}
