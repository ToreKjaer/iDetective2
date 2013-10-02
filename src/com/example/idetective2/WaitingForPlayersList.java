package com.example.idetective2;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WaitingForPlayersList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting_for_players_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waiting_for_players_list, menu);
		return false;
	}

}
//TESTCOMMENT