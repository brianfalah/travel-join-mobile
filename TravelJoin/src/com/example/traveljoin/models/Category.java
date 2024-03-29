package com.example.traveljoin.models;

public class Category implements GeneralItem{

	public Integer id;
    public String name;
    public String description;
    
	public Category(Integer id, String name) {
		super();
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
        return this.getName();            // Para que lo usen los adapters
    }

	@Override
	public String getDescription() {
		return null;
	}
}
