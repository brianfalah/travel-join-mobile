package com.example.traveljoin.auxiliaries;

import com.example.traveljoin.models.GeneralItem;

public class CheckeableItem implements GeneralItem{
	private GeneralItem item;	
	private boolean isChecked;
	
	public CheckeableItem(GeneralItem item) {
		this.item = item;
		this.isChecked = false;
	}
	
	public CheckeableItem(GeneralItem item, Boolean isChecked) {
		this.item = item;
		this.isChecked = isChecked; 
	}
	
	public boolean isChecked() {
		return isChecked;
	}
	
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	public Integer getId() {
		return item.getId();
	}
	
	public String getName() {
		return item.getName();
	}
	
	public String getDescription() {
		return item.getDescription();
	}

	public GeneralItem getItem() {
		return item;
	}

}
