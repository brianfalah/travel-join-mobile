package com.example.traveljoin.auxiliaries;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.example.traveljoin.R;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.User;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class GlobalContext extends Application {

	private User user;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void initializeContext(final FragmentActivity requesterActivity,
			ProgressDialog progressDialog) {
		initializeUser(requesterActivity, progressDialog);
	}

	private void initializeUser(final FragmentActivity requesterActivity,
			final ProgressDialog progressDialog) {
		final Session session = Session.getActiveSession();

		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser facebookUser,
							Response response) {
						if (session == Session.getActiveSession()) {
							if (facebookUser != null) {
								String url = requesterActivity.getResources()
										.getString(R.string.api_url)
										+ "/users/get_or_create";
								User user = new User(facebookUser.getId(),
										facebookUser.getFirstName(),
										facebookUser.getLastName());

								GetOrCreateUserIfNotExistTask httpAsyncTask = new GetOrCreateUserIfNotExistTask(
										user, requesterActivity, progressDialog);
								httpAsyncTask.execute(url);
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
						}

					}
				});
		request.executeAsync();

	}

	private class GetOrCreateUserIfNotExistTask extends
			AsyncTask<String, Void, String> {

		private ApiInterface apiInterface = new ApiInterface();
		private Object objectToSend;
		private ApiResult apiResult;
		private FragmentActivity requesterActivity;
		private ProgressDialog progressDialog;

		public GetOrCreateUserIfNotExistTask(Object objectToSend,
				FragmentActivity requesterActivity,
				ProgressDialog progressDialog) {
			this.objectToSend = objectToSend;
			this.requesterActivity = requesterActivity;
			this.progressDialog = progressDialog;
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
					GlobalContext globalContext = (GlobalContext) getApplicationContext();
					globalContext.setUser(User.fromJSON(jsonObject));
				} else {
					CustomTravelJoinException exception = new CustomTravelJoinException(
							getString(R.string.fetching_user_error));
					exception.alertExceptionMessage(requesterActivity);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
		}
	}

}
