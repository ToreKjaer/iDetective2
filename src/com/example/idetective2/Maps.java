package com.example.idetective2;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class Maps extends Activity {
	private int puzzleNumber; // Holds which puzzle is the current

	//Google Map
	private GoogleMap googleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		
		Bundle bundle = getIntent().getExtras();
		puzzleNumber = bundle.getInt("puzzleNumber");
		
		try {
			// Loading Map
			initializeMap();
			setMarker();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Function to load map. If map is not created then do so.
	private void initializeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			
			// Get the layer of own location
			googleMap.setMyLocationEnabled(true);
			
			//Latitude and Longitude for Aarhus
			double latitude = 56.162939;
			double longitude = 10.203921;
			
			// Set default zoom and focus for the camera
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13.0f));
			
			// Check if map is created successfully else throw error message.
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(), "Beklager, kunne ikke vise kort.", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	/*
	 * Method to set marker on the map according to which puzzle is current
	 */
	private void setMarker() {
		switch (puzzleNumber) {
		case 0: // The first location to go to - Aarhus Politistation
			double latitude = 56.152900;
			double longitude = 10.211209;
			
			googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Aarhus Politistation").snippet("Gå hertil for at blive briefet om sagen"));
			break;

		default:
			break;
		}
	}
	
	protected void onResume() {
		super.onResume();
		initializeMap();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maps, menu);
		return false;
	}

}
