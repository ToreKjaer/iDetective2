package com.example.idetective2;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewGameActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		
		// Hide the actionbar title
		getActionBar().setDisplayShowTitleEnabled(false);
		
		// Get the textfielt from the activity
		final EditText gameName = (EditText) findViewById(R.id.NewGameEditGameName);
		
		// Get the button from the activity and apply an onClickListener
		Button createGameBtn = (Button) findViewById(R.id.NewGameCreateBtn);
		createGameBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String nameOfGame = gameName.getText().toString();
				
				// The name of the game must be at least 1 characters long - if so, add it to the database
				if (nameOfGame.length() > 0) {
					new AddGame().execute(nameOfGame);
				} else {
					Toast.makeText(getBaseContext(), "Navnet er for kort...", Toast.LENGTH_LONG).show();
				}
			}
				
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}
	
	/*
	 * AsyncTast to add a new game to the database
	 */
	private class AddGame extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
		private int success;
		private String message, gameId, gameOwner;
		
		/*
		 * Make a progress dialog on the screen and show it just before the app starts adding data 
		 */
		@Override
		public void onPreExecute() {
			progress = ProgressDialog.show(NewGameActivity.this, "", "Tilføjer spil");
		}

		@Override
		/*
		 * Try to post the new game data to the database
		 */
		protected String doInBackground(String... params) {
			
			gameId = new GenerateRandomId().getId();
			
			final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			gameOwner = settings.getString("playerID", "NULL"); // Get the player id to identify the owner of the game
				
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gameId", gameId)); // The game ID
			nameValuePairs.add(new BasicNameValuePair("gameName", params[0])); // The game name
			nameValuePairs.add(new BasicNameValuePair("gameOwner", gameOwner)); // The owner of the game
			nameValuePairs.add(new BasicNameValuePair("appPassword", "t8cZ5L5eBRM7TUDDTCg2GY4DKbrVR5")); // Used for verification
				
			JSONObject jObject = new JSONParser().addToDatabase("http://users-cs.au.dk/legaard/create_new_game.php", nameValuePairs);
			
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
			// Dismiss the progress dialog, so it's not on the screen anymore
			progress.dismiss();
			
			if (success == 1) {
				// Success!
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				
				Intent i = new Intent(getApplicationContext(), WaitingForPlayersList.class);
				i.putExtra("gameId", gameId);
				i.putExtra("gameOwner", gameOwner);
				startActivity(i);
				finish();
			} else {
				// Failed!
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
		}
	}

}
