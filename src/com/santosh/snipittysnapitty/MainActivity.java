package com.santosh.snipittysnapitty;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String DEBUG_TAG = "HttpConnection";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		handleIntent(intent);
	}

	void handleIntent(Intent intent) {
	    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
	    String shortenedURL = null;
	    if (sharedText != null) {
	        // Update UI to reflect text being shared.
	    	// Have to work on this later.
	    	
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				// Concatenate URL Shortener API data with our URL
				shortenedURL = Constants.isGD+sharedText;
				new DownloadWebpageTask().execute(shortenedURL);
				Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();


			} else {
				Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_LONG).show();
				try {
					wait();
				} catch (InterruptedException e) {
					// Must implement a case for catch. Abruptly terminates as of now.
					e.printStackTrace();
				}
			}
	    }
	    else {
	    	Toast.makeText(getApplicationContext(), "Haven't shortened anything yet. :(", Toast.LENGTH_LONG).show();
		    TextView textView = new TextView(this);
		    textView.setTextSize(40);
		    textView.setText("Nothing to display here... yet.");
	    	setContentView(textView);
		}
	    }
	
	
	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
              
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        
        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // URL.
            int len = 500;
                
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                return contentAsString;
                
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                } 
            }
        }
        
        @Override
        protected void onPostExecute(String result) {
           // textView.setText(result);
        	Intent sendIntent = new Intent();
	    	sendIntent.setAction(Intent.ACTION_SEND);
	    	sendIntent.putExtra(Intent.EXTRA_TEXT, result);
	    	sendIntent.setType("text/plain");
	    	startActivity(Intent.createChooser(sendIntent, "Send shortened link via..."));	    	
       }
        
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");        
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

	
/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
	}
}
