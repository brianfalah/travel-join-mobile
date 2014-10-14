package com.example.traveljoin.models;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class Interest {
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

}
