package com.example.traveljoin.auxiliaries;

public class Item {
	public Integer id;
	public String name;	
	public boolean isChecked;
	
	public Item(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Item(Integer id, String name, Boolean isChecked) {
		this.id = id;
		this.name = name;
		this.isChecked = isChecked; 
	}

}
