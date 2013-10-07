package com.example.idetective2;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListOfGames extends Activity {
	private ListView listView;
	private ArrayList<String> idList = new ArrayList<String>();
	private ArrayList<String> ownerList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_of_games);
		
		// Get data from URL
		new GetGamesList().execute();
		
		// Hide the actionbar title
		getActionBar().setDisplayShowTitleEnabled(false);
		
		// Find the ListView
		listView = (ListView) findViewById(R.id.JoinGameList);
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			/*
			 * When user clicks an item in the list, take him/her to a new intent where it's shown whom else is attending the same game
			 * Pass the game id and the owner of the game along, so that these information doesn't need to be fetched in the next activity as well
			 */
			public void onItemClick(AdapterView<?> arg0, View arg1, int id,
					long arg2) {
				Intent i = new Intent(getApplicationContext(), WaitingForPlayersList.class);
				i.putExtra("gameId", idList.get(id));
				i.putExtra("gameOwner", ownerList.get(id));
				startActivity(i);
			}
		});

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_of_games, menu);
		return false;
	}

	/*
	 * Get data via AsyncTask
	 */
	private class GetGamesList extends AsyncTask<String, Void, String> {
		// Create an ArrayList to hold the names of the games
		private ArrayList<String> nameList = new ArrayList<String>();
		
		// Progress to show that the app is fetching some data
		private ProgressDialog progress;
		
		/*
		 * Make a progress dialog on the screen and show it just before the app starts fetching data 
		 */
		@Override
		public void onPreExecute() {
			progress = ProgressDialog.show(ListOfGames.this, "", "Henter spil");
		}

		@Override
		protected String doInBackground(String... params) {
			// Create JSONParser instance
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromURL("http://users-cs.au.dk/legaard/get_new_games.php");
			
			try {
				// Get the JSONArray called "names" - ex: {"names":[{"name":"First game ever!"}, ......
				JSONArray names = json.getJSONArray("names");
				
				for (int i = 0; i < names.length(); i++) {
					// Get the first JSONObject in the JSONArray - ex: {"name":"First game ever!"}
					JSONObject jObject = names.getJSONObject(i);
					
					// Get the string - ex: "First game ever!"
					String id = jObject.getString("id");
					String name = jObject.getString("name");
					String owner = jObject.getString("owner");
					
					// Add it to the ArrayList
					idList.add(id);
					nameList.add(name);
					ownerList.add(owner);
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
			listView.setAdapter(new ArrayAdapter<String>(ListOfGames.this, android.R.layout.simple_list_item_1, nameList));
			
			// Dismiss the progress dialog, so it's not on the screen anymore
			progress.dismiss(); 
		}
	}
}
