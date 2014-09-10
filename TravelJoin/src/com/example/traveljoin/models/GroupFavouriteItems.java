package com.example.traveljoin.models;

import java.util.ArrayList;

public class GroupFavouriteItems {
	
	//TODO: cambiar esto para que sea accesible por metodos ya que se usa para saber el tamaño del group y todo eso
	public String string;
	public final ArrayList<GeneralListItem> children = new ArrayList<GeneralListItem>();

	public GroupFavouriteItems(String string) {
		this.string = string;
	}

}
