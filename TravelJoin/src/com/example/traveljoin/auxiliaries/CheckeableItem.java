package com.example.traveljoin.auxiliaries;

import com.example.traveljoin.models.GeneralItem;

public class CheckeableItem implements GeneralItem{
	private GeneralItem wrappedItem;	
	private boolean isChecked;
	
	public CheckeableItem(GeneralItem item) {
		this.wrappedItem = item;
		this.isChecked = false;
	}
	
	public CheckeableItem(GeneralItem item, Boolean isChecked) {
		this.wrappedItem = item;
		this.isChecked = isChecked; 
	}
	
	public boolean isChecked() {
		return isChecked;
	}
	
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	public Integer getId() {
		return wrappedItem.getId();
	}
	
	public String getName() {
		return wrappedItem.getName();
	}
	
	public String getDescription() {
		return wrappedItem.getDescription();
	}

}
