package com.example.traveljoin.models;

import java.util.Date;

public class Event {
	private Integer id;
	private String name;
	private String description;	
	private Integer poiId;
	private Date fromDate;
	private Date toDate;
	
	public Event(Integer id, String name, String description, Integer poiId,
			Date fromDate, Date toDate) {
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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}
