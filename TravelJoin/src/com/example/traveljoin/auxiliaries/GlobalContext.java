package com.example.traveljoin.auxiliaries;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.traveljoin.R;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.Category;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.Interest;
import com.example.traveljoin.models.User;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class GlobalContext extends Application {

	private User user;
	private List<Category> categories;
	private List<Interest> interests;
	private final int TASK_TO_EXECUTED = 3;
	private int taskCurrentExcutingInProgressDialog;

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

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	public List<Interest> getInterests() {
		return interests;
	}

	public void setInterests(List<Interest> interests) {
		this.interests = interests;
	}
	
	public void initializeContext(FragmentActivity requesterActivity) {
		ProgressDialog progressDialog = new ProgressDialog(requesterActivity);
		progressDialog.setTitle(getString(R.string.loading));
		progressDialog.setMessage(getString(R.string.wait));
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		resetRunningTaskCounter();
		initializeUser(requesterActivity, progressDialog);
		initializeCategories(requesterActivity, progressDialog);
		initializeInterests(requesterActivity, progressDialog);
	}

	private void resetRunningTaskCounter() {
		taskCurrentExcutingInProgressDialog = TASK_TO_EXECUTED;
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
										null, facebookUser.getFirstName(),
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

	private void initializeCategories(FragmentActivity requesterActivity,
			ProgressDialog progressDialog) {
		String url = getResources().getString(R.string.api_url)
				+ "/categories/index.json";
		new GetCategoriesTask(progressDialog).execute(url);
	}
	
	private void initializeInterests(FragmentActivity requesterActivity,
			ProgressDialog progressDialog) {
		String url = getResources().getString(R.string.api_url)
				+ "/interests/index.json";
		new GetInterestsTask(progressDialog).execute(url);
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
				taskCurrentExcutingInProgressDialog--;
				if (progressDialog.isShowing()
						&& taskCurrentExcutingInProgressDialog == 0) {
					progressDialog.dismiss();
				}
			}
		}
	}

	private class GetCategoriesTask extends AsyncTask<String, Void, String> {
		private ApiInterface apiInterface = new ApiInterface();
		private ApiResult apiResult;
		private ProgressDialog progressDialog;

		public GetCategoriesTask(ProgressDialog progressDialog) {
			this.progressDialog = progressDialog;
		}

		@Override
		protected String doInBackground(String... urls) {
			apiResult = apiInterface.GET(urls[0]);
			return apiResult.getResult();
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("InputStream", result);
			try {
				if (apiResult.ok()) {
					List<Category> categories = getCategoriesFromJSON(new JSONArray(
							result));
					GlobalContext globalContext = (GlobalContext) getApplicationContext();
					globalContext.setCategories(categories);
				}
			} catch (JSONException e) {
				// TODO
				showExceptionError(e);

			} finally {
				taskCurrentExcutingInProgressDialog--;
				if (progressDialog.isShowing()
						&& taskCurrentExcutingInProgressDialog == 0) {
					progressDialog.dismiss();
				}
			}

		}

		private List<Category> getCategoriesFromJSON(JSONArray categoriesJSON)
				throws JSONException {
			List<Category> categories = new ArrayList<Category>();

			for (int i = 0; i < categoriesJSON.length(); i++) {
				JSONObject categoryJSON = categoriesJSON.getJSONObject(i);
				Category category = new Category(categoryJSON.getInt("id"),
						categoryJSON.getString("name"));
				categories.add(category);
			}

			return categories;
		}
	}
	
	private class GetInterestsTask extends AsyncTask<String, Void, String> {
		private ApiInterface apiInterface = new ApiInterface();
		private ApiResult apiResult;
		private ProgressDialog progressDialog;

		public GetInterestsTask(ProgressDialog progressDialog) {
			this.progressDialog = progressDialog;
		}

		@Override
		protected String doInBackground(String... urls) {
			apiResult = apiInterface.GET(urls[0]);
			return apiResult.getResult();
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("InputStream", result);
			try {
				if (apiResult.ok()) {
					List<Interest> interests = getInterestsFromJSON(new JSONArray(
							result));
					GlobalContext globalContext = (GlobalContext) getApplicationContext();
					globalContext.setInterests(interests);
				}
			} catch (JSONException e) {
				// TODO
				showExceptionError(e);

			} finally {
				taskCurrentExcutingInProgressDialog--;
				if (progressDialog.isShowing()
						&& taskCurrentExcutingInProgressDialog == 0) {
					progressDialog.dismiss();
				}
			}

		}

		private List<Interest> getInterestsFromJSON(JSONArray interestsJSON)
				throws JSONException {
			List<Interest> interests = new ArrayList<Interest>();

			for (int i = 0; i < interestsJSON.length(); i++) {
				JSONObject interestJSON = interestsJSON.getJSONObject(i);
				Interest interest = new Interest(interestJSON.getInt("id"),
						interestJSON.getString("name"));
				interests.add(interest);
			}

			return interests;
		}
	}
	
	// TODO Refactor de showConnectionError y showExceptionError a un Error
	// Handler si es que siempre se hace lo mismo con las excepciones. Buscar
	// esos metodos en todo el proyecto
	public void showConnectionError() {
		CustomTravelJoinException exception = new CustomTravelJoinException();
		exception.alertConnectionProblem(this);
		// e.printStackTrace();
	}

	public void showExceptionError(Exception e) {
		CustomTravelJoinException exception = new CustomTravelJoinException(
				e.getMessage());
		exception.alertExceptionMessage(this);
		e.printStackTrace();
	}
}
