package com.example.traveljoin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.example.traveljoin.R;
import com.example.traveljoin.auxiliaries.GlobalContext;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class MainActivity extends ActionBarActivity {

	private FragmentManager fragmentManager = getSupportFragmentManager();
	private Fragment facebookLoginFragment;
	private Fragment wellcomeFragment;
	private boolean isResumed = false;
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializeFragments();
	}
	
	private void initializeFragments() {
		facebookLoginFragment = fragmentManager
				.findFragmentById(R.id.facebookLoginFragment);
		wellcomeFragment = fragmentManager
				.findFragmentById(R.id.welcomeFragment);
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Session session = Session.getActiveSession();

		if (session != null && session.isOpened())
			showWellcomeFragment();
		else
			showFacebookLoginFragment();
	}

	private void showAndHideFragment(Fragment fragmentToShow,
			Fragment fragmentToHide) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.hide(fragmentToHide);
		transaction.show(fragmentToShow);
		transaction.commit();
	}

	private void showFacebookLoginFragment() {
		showAndHideFragment(facebookLoginFragment, wellcomeFragment);
	}

	private void showWellcomeFragment() {
		showAndHideFragment(wellcomeFragment, facebookLoginFragment);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		// Only make changes if the activity is visible
		if (isResumed) {
			clearBackStack();
			if (state.isOpened()) {
				showWellcomeFragment();
				GlobalContext globalContext = (GlobalContext) getApplicationContext();
				globalContext.initializeContext(this);
			}
			else if (state.isClosed())
				showFacebookLoginFragment();
		}
	}

	private void clearBackStack() {
		int backStackSize = fragmentManager.getBackStackEntryCount();
		for (int i = 0; i < backStackSize; i++)
			fragmentManager.popBackStack();
	}
}
