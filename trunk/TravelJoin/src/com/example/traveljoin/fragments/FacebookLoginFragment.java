package com.example.traveljoin.fragments;

import com.example.traveljoin.R;
import com.example.traveljoin.models.ApiInterface;
import com.example.traveljoin.models.ApiResult;
import com.example.traveljoin.models.CustomTravelJoinException;
import com.example.traveljoin.models.User;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FacebookLoginFragment extends Fragment {
	
	private static final int CREATE_USER_METHOD = 1;
	ProgressDialog progress;
	
	public FacebookLoginFragment() {}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);

        return view;
    }

	@Override
	public void onDetach() {
	    super.onDetach();
	    createUserIfNotExist();
	}
	
	private void createUserIfNotExist() {
		final Session session = Session.getActiveSession();
		
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser facebookUser, Response response) {
						if (session == Session.getActiveSession()) {
							if (facebookUser != null) {
								progress = ProgressDialog.show(getActivity(), "Cargando", "Por favor espere...", true);
				            	String url = getResources().getString(R.string.api_url) + "/users/get_or_create";
				            	User user = new User(facebookUser.getId());
				    	        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(CREATE_USER_METHOD, user); 
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
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		private ApiInterface apiInterface = new ApiInterface();
    	private Integer from_method;
    	private Object object_to_send;
    	private ApiResult api_result;
    	
    	//contructor para setearle info extra
    	public HttpAsyncTask(Integer from_method, Object object_to_send) {
			this.from_method = from_method;
			this.object_to_send = object_to_send;  
		}
    	
        @Override
        protected String doInBackground(String... urls) {  
        	//despues de cualquiera de estos metodo vuelve al postexecute de aca
        	switch (this.from_method) {
	        	case CREATE_USER_METHOD :
	        		api_result = apiInterface.POST(urls[0], object_to_send, null);
	        	break;
        	}
        	
        	return api_result.getResult();             
        }
        
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {        	
        	//Log.d("InputStream", result);
        	switch (this.from_method) {        	
	        	case CREATE_USER_METHOD :
	        		progress.dismiss(); 
					if (!api_result.ok()) {												
						CustomTravelJoinException exception = new CustomTravelJoinException("No se ha podido crear el usuario.");
						exception.alertExceptionMessage(getActivity());
					}						
		        break;	
        	}
       }        
    }
}
