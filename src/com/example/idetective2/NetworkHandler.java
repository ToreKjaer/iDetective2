package com.example.idetective2;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class NetworkHandler {
	
	/*
	 * Method to be used when create a user or update the user.
	 * Different use of method depends on which URL that is given as a parameter.
	 */
	public void manageUserInDB(String URL, String playerID, String name) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);
		
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("playerID", playerID)); // The players ID
			nameValuePairs.add(new BasicNameValuePair("playerName", name)); // The players name
			nameValuePairs.add(new BasicNameValuePair("appPassword", "t8cZ5L5eBRM7TUDDTCg2GY4DKbrVR5")); // Used for verification
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpClient.execute(httpPost);
			
		} catch (Exception e) {
			// Failed
			Log.e("Error", e.toString());
		}
	}
	
}
