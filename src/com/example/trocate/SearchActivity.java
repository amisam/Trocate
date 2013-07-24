package com.example.trocate;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.example.trocate.SettingActivity.XmlParseTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchActivity extends FragmentActivity implements LocationListener, OnMarkerClickListener{

	private LocationManager locationManager;
	private String provider;
	private Criteria criteria;
	private Location location;
	private GoogleMap binMap;
	private String[] binLocations;
	private XMLParser xmlParser;
	private String[] searchArgs;
	private XmlParseTask mAuthTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowTitleEnabled(false);

		setContentView(R.layout.main_page);

		ImageButton search = (ImageButton) findViewById(R.id.findBtn);
		ImageButton settings = (ImageButton) findViewById(R.id.settingsBtn);
		ImageButton about = (ImageButton) findViewById(R.id.aboutBtn);

		about.setImageResource(R.drawable.eyeicon);
		search.setImageResource(R.drawable.search);
		settings.setImageResource(R.drawable.settings);

		 if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		FragmentManager myFragmentManager = getSupportFragmentManager();
		SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.map);
		binMap = mySupportMapFragment.getMap();
		binMap.setOnMarkerClickListener(this);
		refresh(null);

	}

	public void refresh(View view){
		// get the GPS location
		// Get the location manager
		binMap.setMyLocationEnabled(true);
		binMap.clear();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use default
		criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		location = locationManager.getLastKnownLocation(provider);

		searchArgs = new String[4];
		searchArgs[0] = String.valueOf(location.getLatitude());
		searchArgs[1] = String.valueOf(location.getLongitude());
		searchArgs[2] = String.valueOf(GlobalVariables.displayNumber);
		searchArgs[3] = String.valueOf(GlobalVariables.filter.name());

		LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation, 18);

		MarkerOptions marker = new MarkerOptions();
		marker.title("you");
		binMap.animateCamera(cameraUpdate);
		marker.position(myLocation);
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.usermarker));
		binMap.addMarker(marker);

		binMap.setMyLocationEnabled(false);

		mAuthTask = new XmlParseTask();
		mAuthTask.execute((Void) null);



	}

	public void about(View view){
		Intent aboutIntent = new Intent(this, AboutActivityPageOne.class);
		this.startActivity(aboutIntent);
	}

	public void goToSettingsPage(View view) {
		Intent intent = new Intent(this, SettingActivity.class);
		this.startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//	Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;

	}

	@Override public void onLocationChanged(Location location) {}
	@Override public void onProviderDisabled(String provider) {}
	@Override public void onProviderEnabled(String provider) {}
	@Override public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public boolean onMarkerClick(Marker mark) {
		if(mark == null) return false;
		mark.showInfoWindow();
		GlobalVariables.currentMarker = mark;
		return true;
	}


	public class XmlParseTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			xmlParser = new XMLParser(URLFactory.createURL(URLFactory.SEARCH, searchArgs));
			binLocations = xmlParser.parse();
			return true;
		}



	@Override
	protected void onPostExecute(final Boolean success){
		for(int i = 0; i < binLocations.length; i++){
			String[] split = binLocations[i].split("\t");
			LatLng llMarker = new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));

			MarkerOptions binMarker = new MarkerOptions();
			binMarker.position(llMarker);
			String[] titleSplit = split[2].trim().split("_");
			binMarker.title(titleSplit[0]+" "+titleSplit[1]);


			if(split[2].equalsIgnoreCase(GlobalVariables.LITTER_BIN))
				binMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.trashlogo));
				//litter icon
			else if (split[2].equalsIgnoreCase(GlobalVariables.RECYCLING_BIN))
				binMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.recyclelogo));
				// recycling icon
			else if (split[2].equalsIgnoreCase(GlobalVariables.SKIP_BIN))
				binMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.skipbinlogo));
				// skip bin icon
			else
				// default icon
				binMarker.icon(BitmapDescriptorFactory.fromResource( R.drawable.trashlogo));

			binMap.addMarker(binMarker);

		}
	}
		@Override
		protected void onCancelled(){
			mAuthTask = null;
		}
	}

}