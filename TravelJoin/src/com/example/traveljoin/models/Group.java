package com.example.traveljoin.models;

public class Group implements GeneralItem {
	
	private Integer id;
	private String name;
	private String description;
	
	// TODO: borrar este constructor. Solo se esta usando para la lista de Grupos.
	// Presentacion de interfaces de usuario
	public Group(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		this.id = 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

}