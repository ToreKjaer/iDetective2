package com.example.idetective2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Highscores extends Activity {
	private ListView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscores);
		
		// Get data
		new GetHighscores().execute();
		
		// Hide the actionbar title
		getActionBar().setDisplayShowTitleEnabled(false);
		
		listview = (ListView) findViewById(R.id.highscoresList);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.highscores, menu);
		return false;
	}
	
	private class GetHighscores extends AsyncTask<String, Void, String> {
		private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		private ProgressDialog progress;
		
		@Override
		protected String doInBackground(String... arg0) {
			// Create JSONParser instance
						JSONParser jParser = new JSONParser();
						JSONObject json = jParser.getJSONFromURL("http://users-cs.au.dk/legaard/get_highscores.php");
						
						try {
							// Get the JSONArray called "names" - ex: {"names":[{"name":"First game ever!"}, ......
							JSONArray names = json.getJSONArray("scores");
							
							for (int i = 0; i < names.length(); i++) {
								// Get the first JSONObject in the JSONArray - ex: {"name":"First game ever!"}
								JSONObject jObject = names.getJSONObject(i);
								
								// Get the string - ex: "First game ever!"
								String name = jObject.getString("name");
								String score = "Score: " + jObject.getString("score");
								
								HashMap<String, String> map = new HashMap<String, String>();
								
								map.put("name", name);
								map.put("score", score);
								
								// Add it to the ArrayList
								list.add(map);
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
			listview.setAdapter(new SimpleAdapter(Highscores.this, list, R.layout.list_row, new String[] {"name", "score"}, new int[] {R.id.name, R.id.score}));
			progress.dismiss(); // Dismiss the progress dialog, so it's not on the screen anymore 
		}
		
		/*
		 * Make a progress dialog on the screen and show it just before the app starts fetching data 
		 */
		@Override
		public void onPreExecute() {
			progress = ProgressDialog.show(Highscores.this, "", "Loading");
		}
		
	}

}
