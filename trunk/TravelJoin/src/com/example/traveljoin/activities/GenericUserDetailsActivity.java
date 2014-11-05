package com.example.traveljoin.activities;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.traveljoin.R;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.User;
import com.facebook.widget.ProfilePictureView;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class GenericUserDetailsActivity extends ActionBarActivity {

	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	private TextView ownGroupsView;
	private TextView joinedGroupsView;
	private TextView ownPoisView;
	private TextView favoritePoisView;
	private TextView ownToursView;
	private TextView favoriteToursView;
	public ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_user_information);
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(R.string.user_view);
		
		profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		userNameView = (TextView) findViewById(R.id.selection_user_name);

		ownGroupsView = (TextView) findViewById(R.id.ownGroupsText);
		joinedGroupsView = (TextView) findViewById(R.id.joinedGroupsText);
		ownPoisView = (TextView) findViewById(R.id.ownPoisText);
		favoritePoisView = (TextView) findViewById(R.id.favoritePoisText);
		ownToursView = (TextView) findViewById(R.id.ownToursText);
		favoriteToursView = (TextView) findViewById(R.id.favoriteToursText);

		Bundle bundle = getIntent().getExtras();
		User user = (User) bundle.get("user");

		getUserFromServer(user);
	}

	private void getUserFromServer(User user) {
		progressDialog = ProgressDialog.show(GenericUserDetailsActivity.this,
				getString(R.string.loading), getString(R.string.wait), true);
		String url = getString(R.string.api_url) + "/users/get_or_create";

		GetOrCreateUserIfNotExistTask httpAsyncTask = new GetOrCreateUserIfNotExistTask(
				user);
		httpAsyncTask.execute(url);
	}

	public void fillInformation(User user) {
		profilePictureView.setProfileId(user.getFacebookId());
		userNameView.setText(user.getFullName());

		ownGroupsView.setText(getString(R.string.own_groups) + " "
				+ user.getOwnGroups().size());
		joinedGroupsView.setText(getString(R.string.joined_groups) + " "
				+ user.getGroups().size());

		ownPoisView.setText(getString(R.string.own_pois) + " "
				+ user.getOwnPois().size());
		favoritePoisView.setText(getString(R.string.favorite_pois) + " "
				+ user.getFavoritePois().size());

		ownToursView.setText(getString(R.string.own_tours) + " "
				+ user.getOwnTours().size());
		favoriteToursView.setText(getString(R.string.favorite_tours) + " "
				+ user.getFavoriteTours().size());
	}

	private class GetOrCreateUserIfNotExistTask extends
			AsyncTask<String, Void, String> {

		private ApiInterface apiInterface = new ApiInterface();
		private Object objectToSend;
		private ApiResult apiResult;
		private FragmentActivity requesterActivity;

		public GetOrCreateUserIfNotExistTask(Object objectToSend) {
			this.objectToSend = objectToSend;
		}

		@Override
		protected String doInBackground(String... urls) {
			apiResult = apiInterface.POST(urls[0], objectToSend, null);
			return apiResult.getResult();
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				if (apiResult.ok()) {
					JSONObject jsonObject = new JSONObject(result);
					User user =User.fromJSON(jsonObject);
					fillInformation(user);
				} else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.fetching_user_error));
					exception.alertExceptionMessage(requesterActivity);
				}
			} catch (JSONException e) {
				showExceptionError(e);
			} catch (ParseException e) {
				showExceptionError(e);
			} finally {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
		}
	}

	public void showConnectionError() {
		CustomTravelJoinException exception = new CustomTravelJoinException();
		exception.alertConnectionProblem(this);
	}

	public void showExceptionError(Exception e) {
		CustomTravelJoinException exception = new CustomTravelJoinException(
				e.getMessage());
		exception.alertExceptionMessage(this);
		e.printStackTrace();
	}

}
