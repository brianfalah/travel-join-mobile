package com.example.traveljoin.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private String facebookId;
	private Integer id;

	private String email;
	private String name;
	private String surname;

	public User(String facebookId, Integer id, String name, String surname) {
		super();
		this.facebookId = facebookId;
		this.id = id;
		this.name = name;
		this.surname = surname;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFullName() {
		return getName() + " " + getSurname();
	}

	public static User fromJSON(JSONObject jsonObject) throws JSONException {
		return new User(jsonObject.getString("facebook_id"),
				jsonObject.getInt("id"), jsonObject.getString("name"),
				jsonObject.getString("surname"));
	}

	public JSONObject toJSON() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("facebook_id", getFacebookId());
			jsonObject.put("name", getName());
			jsonObject.put("surname", getSurname());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
