package com.example.traveljoin.fragments;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Group;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class UserGroupListFragment extends ListFragment {

	private ArrayList<GeneralItem> groups;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		groups = new ArrayList<GeneralItem>();

		groups.add(new Group("Prueba 1", "Descripcion 1"));
		groups.add(new Group("Prueba 2", "Descripcion 2"));
		groups.add(new Group("Prueba 3", "Descripcion 3"));

		GeneralItemListAdapter adapter = new GeneralItemListAdapter(
				getActivity(), groups);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.group_list_item_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Group selectedGroup; 
		switch (item.getItemId()) {
		case R.id.group_context_menu_view:
			// TODO: Redirigir a la vista del Grupo de vista
			selectedGroup = getGroupItem(item);
			Toast.makeText(getActivity(),
					"View : " + selectedGroup.getName(),
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.group_context_menu_edit:
			// TODO: Redirigir a la vista del Grupo de edicion
			selectedGroup = getGroupItem(item);
			Toast.makeText(getActivity(),
					"Edit : " + selectedGroup.getName(),
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.group_context_menu_delete:
			// TODO: Ejecutar la misma funcion para eliminar un Grupo
			selectedGroup = getGroupItem(item);
			Toast.makeText(getActivity(),
					"Delete : " + selectedGroup.getName(),
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	
	private Group getGroupItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Group group = (Group) groups.get(info.position);
		return group;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		// TODO: Redirigir a la vista del POI de vista
	}

}