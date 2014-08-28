package com.example.traveljoin.models;

public class Category {

	private Integer id;
    public String name;
    
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
        return this.getName();            // What to display in the Spinner list.
    }
}
