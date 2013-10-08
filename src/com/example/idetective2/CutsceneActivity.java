package com.example.idetective2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.SurfaceView;
import android.widget.VideoView;

public class CutsceneActivity extends Activity {
    VideoView video_player_view; // VideoPlayer
    DisplayMetrics dm; // Used to read size of screen
    SurfaceView sur_View;
    private int cutsceneNumber; // Fx 1, 2, 3, 4, 5
    private Intent i;
    private String cutsceneURL; // Url of desired video


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cutscene);
		getActionBar().hide();
		
		Bundle bundle = getIntent().getExtras();
		cutsceneNumber = bundle.getInt("cutsceneNumber");
		
		getInit();
		chooseScene();
		video_player_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				startActivity(i);
			}
		});
		playVideo();
	}
	/*
	 * Initializes screen size and sets video size
	 */
    private void getInit() {
        video_player_view = (VideoView) findViewById(R.id.video_player_view);
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        video_player_view.setMinimumWidth(width);
        video_player_view.setMinimumHeight(height);

    }
    
    /*
     * Method to play cutscene according to relevance
     */
    private void playVideo() {
			video_player_view.setVideoURI(Uri.parse(cutsceneURL));
			video_player_view.start();
    }
    
    private void chooseScene() {
    	switch (cutsceneNumber) {
    	// Intro cutscene
    	case 0:
    		cutsceneURL = ""; // Introcutscene
    		i = new Intent(getApplicationContext(), PhoneActivity.class); // Set the intent to the next activity
    		break;
		//
    	case 1:
    		cutsceneURL = ""; // Cutscene at policestation
    		i = new Intent(getApplicationContext(), Maps.class); // Set the intent to the next activity
    		i.putExtra("puzzleNumber", 1);
    		break;
    	case 2:
    		cutsceneURL = ""; //
    		break;
    	case 3:
    		// TO-DO
    		break;
    		
		default:
			break;
    	}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cutscene, menu);
		return false;
	}

}
