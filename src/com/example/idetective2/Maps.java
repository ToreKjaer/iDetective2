package com.example.idetective2;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class Maps extends Activity {

	//Google Map
	private GoogleMap googleMap;
	private Marker currentLocation;
	
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
			
			googleMap.setMyLocationEnabled(true); // false to disable own location.
			
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			String provider = locationManager.getBestProvider(new Criteria(), true);
			Location location = locationManager.getLastKnownLocation(provider);
			
			LocationListener locationListener = new LocationListener() {
				
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLocationChanged(Location location) {
					// redraw the marker when get location update.
			          drawMarker(location);
					
				}
			};
			
			locationManager.requestLocationUpdates(provider, 20, 0, locationListener);
			
			if (location != null) {
				drawMarker(location);
			}
			
			//Latitude and Longitude.
			double latitude = 56.17017;
			double longitude = 10.21703;
			
			//Create marker on map.
			MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("My home lol");
			
			//Adding marker to map.
			googleMap.addMarker(marker);
			
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f));
			
			//Check if map is created successfully else throw error message.
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(), "Sorry. unable to create the map", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	private void drawMarker(Location location){
		if (currentLocation == null) {
			currentLocation = googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("ME").icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
		} else {
			currentLocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
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
