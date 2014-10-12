package com.example.traveljoin.activities;

import java.util.Collection;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.example.traveljoin.R;
import com.example.traveljoin.wikitude.LocationProvider;
import com.example.traveljoin.wikitude.SampleCamActivity;
import com.google.android.gms.maps.model.Marker;
import com.example.traveljoin.models.Poi;

public class AugmentedRealityActivity extends SampleCamActivity {

	protected JSONArray poiData;
	protected boolean isLoading = false;
	protected Collection poisCurrentSelection;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		HashMap<Marker,Poi> markerPoiMap = (HashMap<Marker,Poi>) b.get("pois");
			
		poisCurrentSelection = markerPoiMap.values();
		
		
		this.locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onLocationChanged(final Location location) {
				if (location != null) {
					AugmentedRealityActivity.this.lastKnownLocaton = location;
					if (AugmentedRealityActivity.this.architectView != null) {
						if (location.hasAltitude()) {
							AugmentedRealityActivity.this.architectView
									.setLocation(location.getLatitude(),
											location.getLongitude(),
											location.getAltitude(),
											location.getAccuracy());
						} else {
							AugmentedRealityActivity.this.architectView
									.setLocation(location.getLatitude(),
											location.getLongitude(),
											location.getAccuracy());
						}
					}
				}
			}
		};

		this.architectView
				.registerSensorAccuracyChangeListener(this.sensorAccuracyListener);
		this.locationProvider = new LocationProvider(this,
				this.locationListener);
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		this.loadData();

	}

	final Runnable loadData = new Runnable() {

		@Override
		public void run() {

			AugmentedRealityActivity.this.isLoading = true;

			final int WAIT_FOR_LOCATION_STEP_MS = 2000;

			while (AugmentedRealityActivity.this.lastKnownLocaton == null
					&& !AugmentedRealityActivity.this.isFinishing()) {

				AugmentedRealityActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(AugmentedRealityActivity.this,
								"Encontrando ubicación", Toast.LENGTH_SHORT).show();
					}
				});

				try {
					Thread.sleep(WAIT_FOR_LOCATION_STEP_MS);
				} catch (InterruptedException e) {
					break;
				}
			}

			if (AugmentedRealityActivity.this.lastKnownLocaton != null
					&& !AugmentedRealityActivity.this.isFinishing()) {
				// TODO: you may replace this dummy implementation and instead
				// load POI information e.g. from your database
				AugmentedRealityActivity.this.poiData = AugmentedRealityActivity
						.getPoiInformation(poisCurrentSelection);
				AugmentedRealityActivity.this.callJavaScript(
						"World.loadPoisFromJsonData",
						new String[] { AugmentedRealityActivity.this.poiData
								.toString() });
			}

			AugmentedRealityActivity.this.isLoading = false;
		}
	};

	protected void loadData() {
		if (!isLoading) {
			final Thread t = new Thread(loadData);
			t.start();
		}
	}

	/**
	 * call JacaScript in architectView
	 * 
	 * @param methodName
	 * @param arguments
	 */
	private void callJavaScript(final String methodName,
			final String[] arguments) {
		final StringBuilder argumentsString = new StringBuilder("");
		for (int i = 0; i < arguments.length; i++) {
			argumentsString.append(arguments[i]);
			if (i < arguments.length - 1) {
				argumentsString.append(", ");
			}
		}

		if (this.architectView != null) {
			final String js = (methodName + "( " + argumentsString.toString() + " );");
			this.architectView.callJavascript(js);
		}
	}

	/**
	 * loads poiInformation and returns them as JSONArray. Ensure attributeNames
	 * of JSON POIs are well known in JavaScript, so you can parse them easily
	 * 
	 * @param userLocation
	 *            the location of the user
	 * @param numberOfPlaces
	 *            number of places to load (at max)
	 * @return POI information in JSONArray
	 */
	public static JSONArray getPoiInformation(final Collection<Poi> poisCollection) {

	 

		final JSONArray pois = new JSONArray();

		// ensure these attributes are also used in JavaScript when extracting
		// POI data
		final String ATTR_ID = "id";
		final String ATTR_NAME = "name";
		final String ATTR_DESCRIPTION = "description";
		final String ATTR_LATITUDE = "latitude";
		final String ATTR_LONGITUDE = "longitude";
		final String ATTR_ALTITUDE = "altitude";

		for (Poi poi : poisCollection) {
			final HashMap<String, String> poiInformation = new HashMap<String, String>();
			poiInformation.put(ATTR_ID, poi.getId().toString());
			poiInformation.put(ATTR_NAME, poi.getName());
			poiInformation.put(ATTR_DESCRIPTION, poi.getDescription());
			poiInformation.put(ATTR_LATITUDE,poi.getLatitude().toString());
			poiInformation.put(ATTR_LONGITUDE, poi.getLongitude().toString());	
			
			final float UNKNOWN_ALTITUDE = -32768f; // equals
													// "AR.CONST.UNKNOWN_ALTITUDE"
													// in JavaScript (compare
													// AR.GeoLocation
													// specification)
			// Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude
			// of places should be on user level. Be aware to handle altitude
			// properly in locationManager in case you use valid POI altitude
			// value (e.g. pass altitude only if GPS accuracy is <7m).
			poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE));
			pois.put(new JSONObject(poiInformation));
		}

		return pois;
	}

	

}