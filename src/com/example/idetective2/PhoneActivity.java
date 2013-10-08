package com.example.idetective2;

import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class PhoneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		
		// Hide the actionbar, so that it looks more like the phone is actually ringing
		getActionBar().hide();
		
		// Get the default ringtone from the phone and play it
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		r.play();
		
		// "Pick up" the call, stop the ringtone and start playing the message in the speaker
		ImageButton callerBtn = (ImageButton) findViewById(R.id.callerButton);
		callerBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				r.stop();
				
				/*
				 * Use AudioManager
				MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound_file);
				mediaPlayer.start();
				*/
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.phone, menu);
		return true;
	}

}
