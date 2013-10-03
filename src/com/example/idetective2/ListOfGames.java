package com.example.idetective2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListOfGames extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_of_games);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		connectToJSON();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_of_games, menu);
		return false;
		//this is a test
	}
	
	public void connectToJSON() {
		JSONArray jArray = null;
		String result = "";
		StringBuilder stringBuilder = new StringBuilder();
		InputStream inputStream = null;
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		ArrayList<String> nameList = new ArrayList<String>(); 
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			
			HttpPost httpPost = new HttpPost("#");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			HttpResponse response = httpClient.execute(httpPost);
			
			HttpEntity httpEntity = response.getEntity();
			
			inputStream = httpEntity.getContent();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "HTTP: " + e.toString(), Toast.LENGTH_LONG).show();
		}
		
		// Convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"),8);
			
			stringBuilder.append(reader.readLine() + "\n");
			
			String line = "0";
			
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			
			inputStream.close();
			
			result = stringBuilder.toString();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Conversion: " + e.toString(), Toast.LENGTH_LONG).show();
		}
		
		try {
			jArray = new JSONArray(result);
			
			JSONObject json_data = null;
			
			for (int i = 0; i < jArray.length(); i++) {
				json_data = jArray.getJSONObject(i);
				nameList.add(json_data.getString("name"));
			}
			
			ListView listView = (ListView) findViewById(R.id.JoinGameList);
			listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList));
		} catch (JSONException e) {
			Toast.makeText(getBaseContext(), "JSON: " + e.toString(), Toast.LENGTH_LONG).show();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
