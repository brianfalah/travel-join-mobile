package com.example.traveljoin.fragments;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.GeneralListItemArrayAdapter;
import com.example.traveljoin.models.GeneralListItem;
import com.example.traveljoin.models.Poi;

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

public class PoiListFragment extends ListFragment {
	
	private ArrayList<GeneralListItem> pois;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		pois = new ArrayList<GeneralListItem>();

		pois.add(new Poi("Prueba 1", "Descripcion 1"));
		pois.add(new Poi("Prueba 2", "Descripcion 2"));
		pois.add(new Poi("Prueba 3", "Descripcion 3"));
		
		GeneralListItemArrayAdapter adapter = new GeneralListItemArrayAdapter(getActivity(), pois);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.general_list_item_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.context_menu_view:
	        	//TODO: Redirigir a la vista del POI de vista
	        	Toast.makeText(getActivity(), "View : " + pois.get(info.position).getName()  , Toast.LENGTH_SHORT).show();
	            return true;
	        case R.id.context_menu_edit:
	        	//TODO: Redirigir a la vista del POI de edicion
	        	Toast.makeText(getActivity(), "Edit : " + pois.get(info.position).getName()  , Toast.LENGTH_SHORT).show();
	            return true;
	        case R.id.context_menu_delete:
	        	//TODO: Ejecutar la misma funcion para eliminar un POI
	        	Toast.makeText(getActivity(), "Delete : " + pois.get(info.position).getName()  , Toast.LENGTH_SHORT).show();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		//TODO: Redirigir a la vista del POI de vista
	}
	
	
}
