package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class Interest implements Serializable, GeneralItem{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Integer id;
	public String name;

	public Interest(Integer id, String name) {
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return this.getName();
	}

	public static Interest fromJSON(JSONObject interestJson)
			throws JSONException, ParseException {
		return new Interest(interestJson.getInt("interest_id"),
				interestJson.getString("name"));
	}

	@Override
	public String getDescription() {
		return "";
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Interest) {
			Interest interest = (Interest) object;
			return this.id.equals(interest.id);
		}
		else{
			if (object instanceof GroupInterest) {
				GroupInterest groupInterest = (GroupInterest) object;
				return this.id.equals(groupInterest.getInterestId());
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

}
