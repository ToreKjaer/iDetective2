package com.example.idetective2;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
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
		
		final EditText gameName = (EditText) findViewById(R.id.NewGameEditGameName);
		
		Button createGameBtn = (Button) findViewById(R.id.NewGameCreateBtn);
		createGameBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String nameOfGame = gameName.getText().toString();
				
				if (nameOfGame.length() > 0) {
					Network network = new Network();
					network.execute(nameOfGame);
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
	
	private class Network extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		@Override
		protected String doInBackground(String... params) {
			NetworkHandler network = new NetworkHandler();
			network.addGameToDB("http://users-cs.au.dk/legaard/create_new_game.php", params[0]);
			return null;
		}
			
		@Override
		/*
		 * Convert the ArrayList to fit in the ListView in the Android Activity 
		 */
		protected void onPostExecute(String result) {
			progress.dismiss(); // Dismiss the progress dialog, so it's not on the screen anymore
			Toast.makeText(getApplicationContext(), "Dit spil er blevet oprettet!", Toast.LENGTH_LONG).show();
		}
		
		/*
		 * Make a progress dialog on the screen and show it just before the app starts adding data 
		 */
		@Override
		public void onPreExecute() {
			progress = ProgressDialog.show(NewGameActivity.this, "", "Tilføjer spil");
		}

	}

}
