package com.example.idetective2;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListOfGames extends Activity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_of_games);
		
		// Get data
		Network network = new Network();
		network.execute("");
		
		// Hide the actionbar title
		getActionBar().setDisplayShowTitleEnabled(false);
		
		listView = (ListView) findViewById(R.id.JoinGameList);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(getApplicationContext(), WaitingForPlayersList.class);
				i.putExtra("gameName", listView.getItemAtPosition(arg2).toString());
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

	private class Network extends AsyncTask<String, Void, String> {
		private ArrayList<String> nameList;
		private ProgressDialog progress;

		@Override
		protected String doInBackground(String... params) {
			nameList = new NetworkHandler().getFromDB("http://users-cs.au.dk/legaard/get_new_games.php");
			return null;
		}

		/*
		 * Convert the ArrayList to fit in the ListView in the Android Activity 
		 */
		@Override
		public void onPostExecute(String result) {
			listView.setAdapter(new ArrayAdapter(ListOfGames.this, android.R.layout.simple_list_item_1, nameList));
			progress.dismiss(); // Dismiss the progress dialog, so it's not on the screen anymore 
		}
		
		/*
		 * Make a progress dialog on the screen and show it just before the app starts fetching data 
		 */
		@Override
		public void onPreExecute() {
			progress = ProgressDialog.show(ListOfGames.this, "", "Henter spil");
		}
		
	}
}
