package com.santosh.snipittysnipitty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		handleIntent(intent);
	}

	void handleIntent(Intent intent) {
	    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
	    if (sharedText != null) {
	        // Update UI to reflect text being shared
	    	
	    	Intent sendIntent = new Intent();
	    	sendIntent.setAction(Intent.ACTION_SEND);
	    	sendIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
	    	sendIntent.setType("text/plain");
	    	startActivity(Intent.createChooser(sendIntent, "Send shortened link via..."));

		    TextView textView = new TextView(this);
		    textView.setTextSize(40);
		    textView.setText(sharedText);
	    	setContentView(textView);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
