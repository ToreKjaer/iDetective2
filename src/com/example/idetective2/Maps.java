package com.example.idetective2;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class Maps extends Activity {

	//Google Map
	private GoogleMap googleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		
		try {
			//Loading Map
			initilizeMap();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Function to load map. If map is not created then do so.
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			
			//Latitude and Longitude.
			double latitude = 56.17017;
			double longitude = 10.21703;
			
			//Create marker on map.
			MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("My home lol");
			
			//Adding marker to map.
			googleMap.addMarker(marker);
			
			googleMap.setMyLocationEnabled(true); // false to disable own location.
			
			//Check if map is created successfully else throw error message.
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(), "Sorry. unable to create the map", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}

}
