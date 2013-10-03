package com.example.idetective2;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    	final SharedPreferences.Editor prefEditor = settings.edit();
        
        // If it's the first time the application is being started, prompt the user for a name to use in game
        if (settings.getBoolean("AppFirstTime", true)) {
        	AlertDialog.Builder getName = new AlertDialog.Builder(this);
        	
        	getName.setTitle("Indtast navn");
        	getName.setMessage("Skriv det navn du ønsker at benytte i spillet:");
        	
        	// Text field the user can enter his/her name and connect it to the AlertDialog
        	final EditText name = new EditText(this);
        	getName.setView(name);
        	
        	// Set what the positive button will do
        	getName.setPositiveButton("Gem", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Get the string from text field
					Editable value = name.getText();
					
					// Save the name in shared preferences, so it can be used throughout the game
					prefEditor.putString("Name", value.toString()).commit();
					
					updateWelcomeText();
				}
			});
        	
        	// Set what the negative button will do (if the user wants to cancel)
        	getName.setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Close the application!
					finish();
				}
			});
        	
        	getName.show();
        	
        	// Now the application has been run once, so we don't need this prompt anymore
        	prefEditor.putBoolean("AppFirstTime", false);
        	prefEditor.commit();
        }
        
        Button newGameBtn = (Button) findViewById(R.id.newGameBtn);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Start the NewJoinGame activity
				gotoNewJoinGameActivity();
			}
		});
        
        Button settingsBtn = (Button) findViewById(R.id.settingBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Start the NewJoinGame activity
				gotoSettingsActivity();
			}
		});
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	updateWelcomeText();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }
    
    /*
     * Updates the welcome text to include the name saved in the preferences
     */
    private void updateWelcomeText() {
    	final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    	
    	TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
    	
    	// Update the welcome text at main screen
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        welcomeText.setText("Velkommen agent " + settings.getString("Name", "ukendt"));
    }
    
    private void gotoNewJoinGameActivity() {
    	Intent intent = new Intent(getApplicationContext(), NewJoinGame.class);
    	startActivity(intent);
    	overridePendingTransition(R.anim.slide_left, R.anim.push_left_out);
    }
    
    private void gotoSettingsActivity() {
    	Intent intent = new Intent(getApplicationContext(), PrefsActivity.class);
    	startActivity(intent);
    }
    
}
