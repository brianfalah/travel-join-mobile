package com.example.traveljoin.models;

import java.util.ArrayList;

public class GroupFavouriteItems {

	private String name;
	private ArrayList<GeneralItem> favouriteItems;
	private int drawableId;
	
	public GroupFavouriteItems(String name, int drawableId) {
		this.name = name;
		this.favouriteItems = new ArrayList<GeneralItem>();
		this.drawableId = drawableId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<GeneralItem> getFavouriteItems() {
		return favouriteItems;
	}

	public void setFavouriteItems(ArrayList<GeneralItem> favouriteItems) {
		this.favouriteItems = favouriteItems;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

	public int getDrawableId() {
		return drawableId;
	}
	
	public GeneralItem getItem(int position) {
		return favouriteItems.get(position);
	}
	
	public int size() {
		return favouriteItems.size();
	}

	public void add(GeneralItem favouriteItem) {
		favouriteItems.add(favouriteItem);
	}
}

