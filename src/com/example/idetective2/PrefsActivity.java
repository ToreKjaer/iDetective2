package com.example.idetective2;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class PrefsActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		
		EditTextPreference editPref = (EditTextPreference) findPreference("Name");
		editPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			// Update the new value in shared preferences
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				settings.edit().putString("Name", newValue.toString()).commit();
				
				Network network = new Network();
				network.execute(settings.getString("playerID", "NULL"), newValue.toString());
				
				return false;
			}
		});
		
		Preference pref = (Preference) findPreference("playerID");
		pref.setSummary(settings.getString("playerID", "NULL"));
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	private class Network extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			NetworkHandler nHandler = new NetworkHandler();
			nHandler.manageUserInDB("http://users-cs.au.dk/legaard/update_user.php", params[0], params[1]);
			return null;
		}
		
		@Override
		public void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), "Navnet blev ændret", Toast.LENGTH_LONG).show();
		}
    	
    }
	
}
