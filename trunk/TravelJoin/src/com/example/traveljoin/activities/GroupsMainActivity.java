package com.example.traveljoin.activities;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.traveljoin.R;
import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.GeneralItem;
import com.example.traveljoin.models.Group;
import com.example.traveljoin.models.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;

public class GroupsMainActivity extends Activity implements OnQueryTextListener {
	
	private ProgressDialog progress;
	private ActionBar actionBar;
	private ListView listView;
	private GeneralItemListAdapter adapter;
	private ArrayList<GeneralItem> groups;
	private User user;
	
	private static final int CREATE_GROUP_REQUEST = 1;
	private static final int EDIT_GROUP_REQUEST = 2;
	// para el asynctask
	protected static final int GET_GROUPS_METHOD = 1;
	protected static final int DELETE_GROUP_METHOD = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		actionBar = getActionBar();
		actionBar.setSubtitle(R.string.groups);

		initializeUser();
		groups = new ArrayList<GeneralItem>();
		getGroupsFromServer();
		adapter = new GeneralItemListAdapter(this, groups);

		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);
		registerForContextMenu(listView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.groups_activity_actions, menu);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.group_search:
			// Already handled in search listener
			return true;
		case R.id.group_add:
			Intent intent = new Intent(this, GroupFormActivity.class);
			startActivityForResult(intent, CREATE_GROUP_REQUEST);
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.group_list_item_context_menu, menu);
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		Group group = (Group) listView.getItemAtPosition(info.position);
		if (!group.getUserId().equals(user.getId())) {
			menu.removeItem(R.id.group_context_menu_edit);
			menu.removeItem(R.id.group_context_menu_delete);
		}
		else{
			menu.removeItem(R.id.group_context_menu_join);
			menu.removeItem(R.id.group_context_menu_disjoin);
		}
	}

	OnItemClickListener groupItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			startGroupDetailActivity((Group) adapter.getItem(position));
		}
	};
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Group selectedGroup = getGroupItem(item);
		switch (item.getItemId()) {
		case R.id.group_context_menu_view:
			startGroupDetailActivity(selectedGroup);
			return true;
		case R.id.group_context_menu_edit:
			Intent intent_edit = new Intent(this, GroupFormActivity.class);
			intent_edit.putExtra("group", selectedGroup);
			startActivityForResult(intent_edit, EDIT_GROUP_REQUEST);
			return true;
		case R.id.group_context_menu_delete:
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					GroupsMainActivity.this);
			dialog.setTitle(getString(R.string.delete_group))
					.setMessage(getString(R.string.delete_group_message))
					.setPositiveButton(getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									progress = ProgressDialog.show(
											GroupsMainActivity.this,
											getString(R.string.loading),
											getString(R.string.wait), true);
									String url = getResources().getString(
											R.string.api_url)
											+ "/groups/destroy";
									HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
											DELETE_GROUP_METHOD, selectedGroup);
									httpAsyncTask.execute(url);
									// sigue en HttpAsyncTask en doInBackground
									// en DELETE_POI_METHOD

								}
							})
					.setNegativeButton(getString(R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();
			return true;
		case R.id.group_context_menu_join:
			// TODO: Ejecutar la misma funcion para eliminar un Grupo
			Toast.makeText(this, "Unirse", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.group_context_menu_disjoin:
			// TODO: Ejecutar la misma funcion para eliminar un Grupo
			Toast.makeText(this, "Dejar el grupo", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	private void startGroupDetailActivity(final Group selectedGroup) {
		Intent intent = new Intent(this, GroupFormActivity.class);
		intent.putExtra("group", selectedGroup);
		startActivity(intent);
	}

	private Group getGroupItem(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Group group = (Group) groups.get(info.position);
		return group;
	}

	private void initializeUser() {
		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		user = globalContext.getUser();
	}

	private void getGroupsFromServer() {
		progress = ProgressDialog.show(this, getString(R.string.loading),
				getString(R.string.wait), true);
		String url = getResources().getString(R.string.api_url)
				+ "/groups/index.json";
		HttpAsyncTask task = new HttpAsyncTask(GET_GROUPS_METHOD, null);
		task.execute(url);
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		private ApiInterface apiInterface = new ApiInterface();
		private Integer from_method;
		private Object object_to_send;
		private ApiResult api_result;

		// contructor para setearle info extra
		public HttpAsyncTask(Integer from_method, Object object_to_send) {
			this.from_method = from_method;
			this.object_to_send = object_to_send;
		}

		@Override
		protected String doInBackground(String... urls) {
			// despues de cualquiera de estos metodo vuelve al postexecute de
			// aca
			switch (this.from_method) {
			case GET_GROUPS_METHOD:
				api_result = apiInterface.GET(urls[0]);
				break;
			case DELETE_GROUP_METHOD:
				api_result = apiInterface.POST(urls[0], object_to_send,
						"delete");
				break;
			}

			return api_result.getResult();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Log.d("InputStream", result);
			switch (this.from_method) {
			case GET_GROUPS_METHOD:
				if (api_result.ok()) {
					try {
						groups.clear();
						JSONArray groupsJson = new JSONArray(result);
						for (int i = 0; i < groupsJson.length(); i++) {
							JSONObject groupJson = groupsJson.getJSONObject(i);
							Group group = Group.fromJSON(groupJson);
							groups.add(group);
						}
						listView.setOnItemClickListener(groupItemClickListener);
						adapter.notifyDataSetChanged();
						progress.dismiss();

					} catch (JSONException e) {
						// TODO: Handlear
						progress.dismiss();
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO: Handlear
						e.printStackTrace();
					}
				} else {
					// showConnectionError();
					// TODO si no se pudieron obtener las categorias mostrar
					// cartel
					// para reintentar
				}
				break;
			case DELETE_GROUP_METHOD:
				progress.dismiss();
				if (api_result.ok())
					getGroupsFromServer();
				else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.delete_group_error_message));
					exception.alertExceptionMessage(GroupsMainActivity.this);
				}
				break;
			}
		}
	}

}
