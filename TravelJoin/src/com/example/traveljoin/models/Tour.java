package com.example.traveljoin.models;

public class Tour implements GeneralItem {
	
	private String name;
	private String description;
	
	// TODO: borrar este constructor. Solo se esta usando para la lista de Poi.
	// Presentacion de interfaces de usuario
	public Tour(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
