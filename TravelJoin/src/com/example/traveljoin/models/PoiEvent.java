package com.example.traveljoin.models;

import java.io.Serializable;
import java.util.Calendar;

public class PoiEvent implements Serializable{
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
	
    @Override
    public String toString() {
        return this.getName();            // Para que lo usen los adapters
    }
}
