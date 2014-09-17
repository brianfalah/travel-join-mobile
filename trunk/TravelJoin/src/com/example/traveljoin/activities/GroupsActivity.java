package com.example.traveljoin.activities;

import java.util.ArrayList;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Group;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class GroupsActivity extends Activity implements
		OnQueryTextListener {

	private ActionBar actionBar;
	private ListView listView;
	private GeneralItemListAdapter adapter;
	private ArrayList<GeneralItem> groups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);

		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.groups);
		
		groups = new ArrayList<GeneralItem>();

		groups.add(new Group("Prueba 1", "Descripcion 1"));
		groups.add(new Group("Sorongo", "Descripcion 2"));
		groups.add(new Group("Todos los perros van al cielo", "Descripcion 3"));

		adapter = new GeneralItemListAdapter(this, groups);

		listView = (ListView) findViewById(R.id.groups_list);
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		getMenuInflater().inflate(R.menu.groups_menu, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.group_search)
				.getActionView();

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) { 
		switch (item.getItemId()) {
		case R.id.group_search:
			onSearchRequested();
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	@Override
	public boolean onQueryTextChange(String insertedText) {
		adapter.getFilter().filter(insertedText);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}

}
