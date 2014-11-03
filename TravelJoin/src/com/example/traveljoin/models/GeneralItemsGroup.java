package com.example.traveljoin.models;

import java.util.ArrayList;

public class GeneralItemsGroup {

	private String name;
	private ArrayList<GeneralItem> generalItems;
	private int drawableId;
	
	public GeneralItemsGroup(String name, int drawableId, ArrayList<GeneralItem> generalItems) {
		this.name = name;
		this.drawableId = drawableId;
		this.generalItems = generalItems;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<GeneralItem> getItems() {
		return generalItems;
	}

	public void setItems(ArrayList<GeneralItem> items) {
		this.generalItems = items;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

	public int getDrawableId() {
		return drawableId;
	}
	
	public GeneralItem getItem(int position) {
		return generalItems.get(position);
	}
	
	public int size() {
		return generalItems.size();
	}

	public void add(GeneralItem generalItem) {
		generalItems.add(generalItem);
	}

	public void addAll(ArrayList<GeneralItem> generalItems) {
		generalItems.addAll(generalItems);
		
	}

	public void clear() {
		generalItems.clear();
		
	}
}

