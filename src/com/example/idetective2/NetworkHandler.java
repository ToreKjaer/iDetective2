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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NetworkHandler {
	
	/*
	 * Add new game to list of current games.
	 */
	public void addGameToDB(String URL, String name) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);
		
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gameName", name)); // The game name
			nameValuePairs.add(new BasicNameValuePair("appPassword", "t8cZ5L5eBRM7TUDDTCg2GY4DKbrVR5")); // Used for verification
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpClient.execute(httpPost);
			
		} catch (Exception e) {
			// Failed
			Log.e("Error", e.toString());
		}
	}
	
	/*
	 * Method to be used when create a user or update the user.
	 * Different use of method depends on which URL that is given as a parameter.
	 */
	public void manageUserInDB(String URL, String playerID, String name) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);
		
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gameName", playerID)); // The players ID
			nameValuePairs.add(new BasicNameValuePair("gameName", name)); // The players name
			nameValuePairs.add(new BasicNameValuePair("appPassword", "t8cZ5L5eBRM7TUDDTCg2GY4DKbrVR5")); // Used for verification
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpClient.execute(httpPost);
			
		} catch (Exception e) {
			// Failed
			Log.e("Error", e.toString());
		}
	}
	
	/*
	 * Get data from database.
	 * Returns an ArrayList with the data - needs to be adapted to fit in a ListView.
	 */
	public ArrayList<String> getFromDB(String URL) {
		JSONArray jArray = null;
		String result = "";
		StringBuilder stringBuilder = new StringBuilder();
		InputStream inputStream = null;
		ArrayList<String> nameList;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameList = new ArrayList<String>();
		
		/*
		 * Setup the HTTP connection to get the data from the server
		 */
		try {
			HttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(URL);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity httpEntity = response.getEntity();

			inputStream = httpEntity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		/*
		 * Saves the string that is displayed on the URL (the JSON data from MySQL on the PHP site)
		 */
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);

			stringBuilder.append(reader.readLine() + "\n");
			
			String line = "0";

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			
			inputStream.close();

			result = stringBuilder.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		
		/*
		 * Convert the JSON data to be saved in an ArrayList
		 */
		try {
			jArray = new JSONArray(result);

			JSONObject jsonData = null;

			for (int i = 0; i < jArray.length(); i++) {
				jsonData = jArray.getJSONObject(i);
				nameList.add(jsonData.getString("name"));
			}
			
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return nameList;
	}
}
