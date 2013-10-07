package com.example.idetective2;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class WaitingForPlayersList extends Activity {
	private ListView listView;
	private String gameId, gameOwner;
	
	private String playerId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting_for_players_list);
		
		// Hide the actionbar title
		getActionBar().setDisplayShowTitleEnabled(false);
		
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		playerId = settings.getString("playerID", "NULL"); // Get the player id to identify the owner of the game
		
		listView = (ListView) findViewById(R.id.WaitingForPlayersList);
		
		Bundle extras = getIntent().getExtras();
		gameId = extras.getString("gameId");
		gameOwner = extras.getString("gameOwner");
		
		new AddPlayer().execute();
		
		Button startButton = (Button) findViewById(R.id.startbtn);
		startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Maps.class);
		    	startActivity(intent);
				
			}
		});
		
		// If the player is the owner of the game, then it's possible for him only to start the actual game
		if (checkIfOwner()) {
			startButton.setEnabled(true);
		} else {
			startButton.setEnabled(false);
		}
		
		// Refresh the view displaying all the players attending the current game
		Button refreshButton = (Button) findViewById(R.id.refreshbtn);
		refreshButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new GetPlayers().execute();
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waiting_for_players_list, menu);
		return false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		new DeletePlayer().execute();
	}
	
	/*
	 * Check if the player is the owner of the game
	 */
	private boolean checkIfOwner() {
		boolean res = true;
		
		if (playerId.equals(gameOwner)) {
			res = true;
		} else {
			res = false;
		}
		
		return res;
	}
	
	/*
	 * AsyncTast to add a user to the game at hand
	 */
	private class AddPlayer extends AsyncTask<String, Void, String> {
		private int success;
		private String message;
		
		@Override
		/*
		 * Try to post the user data to the database
		 */
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gameId", gameId)); // The game ID
			nameValuePairs.add(new BasicNameValuePair("playerId", playerId)); // The player ID
			nameValuePairs.add(new BasicNameValuePair("appPassword", "t8cZ5L5eBRM7TUDDTCg2GY4DKbrVR5")); // Used for verification
				
			JSONObject jObject = new JSONParser().addToDatabase("http://users-cs.au.dk/legaard/add_to_game.php", nameValuePairs);
			
			try {
				// Get response from PHP script
				success = jObject.getInt("success");
				message = jObject.getString("message");
			} catch (Exception e) {
				// Error
				Log.e("Error", e.toString());
			}
			
			return null;
		}
			
		@Override
		/*
		 * Convert the ArrayList to fit in the ListView in the Android Activity 
		 */
		protected void onPostExecute(String result) {
			if (success == 1) {
				// Success!
				Log.e("Add user", message);
				
				new GetPlayers().execute();
			} else {
				// Failed!
				Log.e("Add user", message);
			}
		}
	}

	/*
	 * Get data via AsyncTask
	 */
	private class GetPlayers extends AsyncTask<String, Void, String> {
		// Create an ArrayList to hold the names of the games
		private ArrayList<String> nameList = new ArrayList<String>();
		
		// Progress to show that the app is fetching some data
		private ProgressDialog progress;
		
		/*
		 * Make a progress dialog on the screen and show it just before the app starts fetching data 
		 */
		@Override
		public void onPreExecute() {
			progress = ProgressDialog.show(WaitingForPlayersList.this, "", "Finder spillere");
		}

		@Override
		protected String doInBackground(String... params) {
			// Create JSONParser instance
			JSONParser jParser = new JSONParser();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gameId", gameId)); // Used for verification
				
			JSONObject json = jParser.addToDatabase("http://users-cs.au.dk/legaard/get_players.php", nameValuePairs);
			
			try {
				// Get the JSONArray called "names" - ex: {"names":[{"name":"First game ever!"}, ......
				JSONArray names = json.getJSONArray("names");
				
				for (int i = 0; i < names.length(); i++) {
					// Get the first JSONObject in the JSONArray - ex: {"name":"First game ever!"}
					JSONObject jObject = names.getJSONObject(i);
					
					// Get the string - ex: "First game ever!"
					String name = jObject.getString("name");
					
					// Add it to the ArrayList
					nameList.add(name);
				}
			} catch (JSONException e) {
				Log.e("JSONArray error:", "Error getting JSONArray. Error: " + e.toString());
			} catch (Exception e) {
				Log.e("Error:", "Error: " + e.toString());
			}
			
			return null;
		}

		/*
		 * Convert the ArrayList to fit in the ListView in the Android Activity 
		 */
		@Override
		public void onPostExecute(String result) {
			// If the game has been removed by the owner, go back to the gameslistview
			if (nameList.size() > 0) {
				listView.setAdapter(new ArrayAdapter<String>(WaitingForPlayersList.this, android.R.layout.simple_list_item_1, nameList));
				
				// Dismiss the progress dialog, so it's not on the screen anymore
				progress.dismiss(); 
			} else {
				// Dismiss the progress dialog, so it's not on the screen anymore
				progress.dismiss(); 
				
				finish();
			}
		}
	}
	
	/*
	 * AsyncTask for deletion of entries in Plays table in MySQL
	 */
	private class DeletePlayer extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// Create JSONParser instance
			JSONParser jParser = new JSONParser();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("playerId", playerId));
			nameValuePairs.add(new BasicNameValuePair("gameId", gameId));
				
			jParser.addToDatabase("http://users-cs.au.dk/legaard/delete_player.php", nameValuePairs);
			
			return null;
		}
	}
}