package com.example.idetective2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	public JSONParser() {
		// Constructor
	}

	public JSONObject getJSONFromURL(String url) {
		String JSONString = "";
		StringBuilder stringBuilder = new StringBuilder();
		InputStream inputStream = null;
		JSONObject jObject = null;

		/*
		 * Setup the HTTP connection to get the data from the server.
		 * Send a password along so that not everybody can see the data on the URL
		 */
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("appPassword", "t8cZ5L5eBRM7TUDDTCg2GY4DKbrVR5")); // Used for verification
			
			HttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(url);
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity httpEntity = response.getEntity();

			inputStream = httpEntity.getContent();
		} catch (Exception e) {
			Log.e("HTTP Client error:",
					"Error fetching data. Error: " + e.toString());
		}

		/*
		 * Saves the string that is displayed on the URL (the JSON data from
		 * MySQL on the PHP site)
		 */
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "iso-8859-1"), 8);

			stringBuilder.append(reader.readLine() + "\n");

			String line = "0";

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}

			inputStream.close();

			JSONString = stringBuilder.toString();
		} catch (Exception e) {
			Log.e("Buffer error:",
					"Error parsing data to JSON. Error: " + e.toString());
		}

		/*
		 * Convert the JSON data to be saved in an ArrayList
		 */
		try {
			jObject = new JSONObject(JSONString);
		} catch (JSONException e) {
			Log.e("JSON parser error:",
					"Error getting JSONObject. Error: " + e.toString());
		}

		return jObject;
	}
	
	public JSONObject addToDatabase(String url, ArrayList<NameValuePair> list) {
		String JSONString = "";
		StringBuilder stringBuilder = new StringBuilder();
		InputStream inputStream = null;
		JSONObject jObject = null;

		/*
		 * Setup the HTTP connection to get the data from the server.
		 * Send a password along so that not everybody can see the data on the URL
		 */
		try {
			ArrayList<NameValuePair> nameValuePairs = list;
			nameValuePairs.add(new BasicNameValuePair("appPassword", "t8cZ5L5eBRM7TUDDTCg2GY4DKbrVR5")); // Used for verification
			
			HttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(url);
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity httpEntity = response.getEntity();

			inputStream = httpEntity.getContent();
		} catch (Exception e) {
			Log.e("HTTP Client error:",
					"Error fetching data. Error: " + e.toString());
		}

		/*
		 * Saves the string that is displayed on the URL (the JSON data from
		 * MySQL on the PHP site)
		 */
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "iso-8859-1"), 8);

			stringBuilder.append(reader.readLine() + "\n");

			String line = "0";

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}

			inputStream.close();

			JSONString = stringBuilder.toString();
		} catch (Exception e) {
			Log.e("Buffer error:",
					"Error parsing data to JSON. Error: " + e.toString());
		}

		/*
		 * Convert the JSON data to be saved in an ArrayList
		 */
		try {
			jObject = new JSONObject(JSONString);
		} catch (JSONException e) {
			Log.e("JSON parser error:",
					"Error getting JSONObject. Error: " + e.toString());
		}

		return jObject;
	}
}
