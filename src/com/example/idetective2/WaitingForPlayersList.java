package com.example.idetective2;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class WaitingForPlayersList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting_for_players_list);
		
		// Hide the actionbar title
		getActionBar().setDisplayShowTitleEnabled(false);
		
		Bundle extras = getIntent().getExtras();
		
		TextView gameTitle = (TextView) findViewById(R.id.WaitingGameTitle);
		gameTitle.setText("Spilnavn: \"" + extras.getString("gameName") + "\"");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waiting_for_players_list, menu);
		return false;
	}

}