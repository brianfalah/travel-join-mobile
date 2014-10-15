package com.example.traveljoin.activities;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.AdapterView.OnItemClickListener;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.GeneralItemCheckeableListAdapter;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.GeneralItem;

public class InterestsSelectorActivity extends Activity implements
		OnQueryTextListener {

	private ProgressDialog progress;
	private ActionBar actionBar;
	private ListView listView;
	private GeneralItemCheckeableListAdapter adapter;
	private ArrayList<GeneralItem> selectedInterests;

	OnItemClickListener interestItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			adapter.toggleChecked(position);
			CheckedTextView checkedTextView = (CheckedTextView) view
					.findViewById(R.id.name);
			checkedTextView.toggle();
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_general_item_selector);

		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.interests_selector);

		selectedInterests = new ArrayList<GeneralItem>();
		ArrayList<GeneralItem> alreadySelectedInterests = (ArrayList<GeneralItem>) getIntent()
				.getExtras().get("alreadySelectedInterests");

		listView = (ListView) findViewById(R.id.list);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setTextFilterEnabled(true);
		getInterests(alreadySelectedInterests);		
		registerForContextMenu(listView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.general_item_selector_activity_actions, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search:
			// Already handled in search listener
			return true;
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

	private void getInterests(ArrayList<GeneralItem> alreadySelectedInterests) {
		GlobalContext globalContext = (GlobalContext) this.getApplicationContext();
		ArrayList<GeneralItem> interests = globalContext.getInterests();
		
		adapter = new GeneralItemCheckeableListAdapter(
				new GeneralItemListAdapter(
						InterestsSelectorActivity.this, interests),
				alreadySelectedInterests);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(interestItemClickListener);
		adapter.notifyDataSetChanged();
		
	}

	public void onCancelButtonClicked(View button) {
		Intent output = new Intent();
		setResult(Activity.RESULT_CANCELED, output);
		finish();
	}

	public void onAcceptButtonClicked(View button) {
		// TODO: Agregar validacion de que si no se agrego ningun item, al menos
		// se deba seleccionar uno

		Intent output = new Intent();
		output.putExtra("newSelectedInterests", adapter.getSelectedItems());

		setResult(Activity.RESULT_OK, output);
		finish();
	}

}