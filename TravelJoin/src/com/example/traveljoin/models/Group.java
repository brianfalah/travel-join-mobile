package com.example.traveljoin.models;

public class Group implements GeneralListItem {
	
	private String name;
	private String description;
	
	// TODO: borrar este constructor. Solo se esta usando para la lista de Grupos.
	// Presentacion de interfaces de usuario
	public Group(String name, String description) {
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