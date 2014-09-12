package com.example.traveljoin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.FavouritesExpandableListAdapter;
import com.example.traveljoin.models.GeneralListItem;
import com.example.traveljoin.models.GroupFavouriteItems;
import com.example.traveljoin.models.Poi;
import com.example.traveljoin.models.Tour;

public class FavouritesFragment extends Fragment {

	private final static int POIS_GROUP_KEY = 0;
	private final static int TOURS_GROUP_KEY = 1;
	private SparseArray<GroupFavouriteItems> groups = new SparseArray<GroupFavouriteItems>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_user_favourites,
				container, false);

		createItemsGroups();
		ExpandableListView expadableListView = (ExpandableListView) view
				.findViewById(R.id.favouritesExpandableListView);
		FavouritesExpandableListAdapter adapter = new FavouritesExpandableListAdapter(
				getActivity(), groups);
		expadableListView.setAdapter(adapter);

		registerForContextMenu(expadableListView);

		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);

		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			MenuInflater inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.fovourite_list_item_context_menu, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		GeneralListItem favouriteItem;
		switch (item.getItemId()) {
			case R.id.favourite_context_menu_view:
				// TODO: Redirigir a la vista
				favouriteItem = getFavouriteItem(item);
				Toast.makeText(getActivity(), "View: " + favouriteItem.getName(),
						Toast.LENGTH_SHORT).show();
				return true;
			case R.id.favourite_context_menu_edit:
				// TODO: Redirigir a la vista de edicion
				favouriteItem = getFavouriteItem(item);
				Toast.makeText(getActivity(), "Edit: " + favouriteItem.getName(),
						Toast.LENGTH_SHORT).show();
				return true;
			case R.id.favourite_context_menu_delete:
				// TODO: Ejecutar la misma funcion para eliminarlo
				favouriteItem = getFavouriteItem(item);
				Toast.makeText(getActivity(), "Delete: " + favouriteItem.getName(),
						Toast.LENGTH_SHORT).show();
				return true;
			default:
				return super.onContextItemSelected(item);
			}
	}

	private GeneralListItem getFavouriteItem(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item
				.getMenuInfo();
		int groupPosition = ExpandableListView
				.getPackedPositionGroup(info.packedPosition);
		int childPosition = ExpandableListView
				.getPackedPositionChild(info.packedPosition);
		GroupFavouriteItems group = (GroupFavouriteItems) groups
				.get(groupPosition);
		GeneralListItem favouriteItem = (GeneralListItem) group
				.getItem(childPosition);
		return favouriteItem;
	}

	public void createItemsGroups() {
		GroupFavouriteItems poisGroup = new GroupFavouriteItems(
				getString(R.string.favourite_pois_group),
				R.drawable.ic_action_place);
		GroupFavouriteItems toursGroup = new GroupFavouriteItems(
				getString(R.string.favourite_tours_group),
				R.drawable.ic_action_split);
		// TODO: Realizar la llamada al servidor para cada uno
		for (int i = 0; i < 5; i++) {
			poisGroup.add(new Poi("Prueba " + i, "Descripcion " + i));
			toursGroup.add(new Tour("Prueba " + i, "Descripcion " + i));
		}
		groups.put(POIS_GROUP_KEY, poisGroup);
		groups.put(TOURS_GROUP_KEY, toursGroup);
	}

}