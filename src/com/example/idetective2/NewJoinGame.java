package com.example.idetective2;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class NewJoinGame extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_join_game);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Button connectToGameBtn = (Button) findViewById(R.id.joinGame);
        connectToGameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Go to list of games activity
				gotoJoinGameActivity();
			}
		});
        
        Button newGameBtn = (Button) findViewById(R.id.newGame);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Go to new game activity
				gotoNewGameActivity();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_join_game, menu);
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == android.R.id.home) {
	        finish();
	        overridePendingTransition(R.anim.slide_right, R.anim.push_right_out);
	    }
	    
	    return true;
	}
	
	private void gotoJoinGameActivity() {
    	Intent intent = new Intent(getApplicationContext(), ListOfGames.class);
    	startActivity(intent);
    }

	private void gotoNewGameActivity() {
		// TODO
    	/*Intent intent = new Intent(getApplicationContext(), XXX.class);
    	startActivity(intent);*/
    }
	
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_right, R.anim.push_right_out);
	}
}
