package com.ecs.android.sample.twitter;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Date;

import oauth.signpost.OAuth;
import twitter4j.ProfileImage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidTwitterSample extends Activity {

	public static SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();
	private TextView loginName;
	private ImageView myiconview;
	public static String UserName = null;
	public static Bitmap mybit = null;
	public static String imageURL= null;
    final Runnable mUpdateTwitterNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG).show();
        }
    };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        AndroidTwitterSample.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        loginName = (TextView)findViewById(R.id.login_status);
        myiconview= (ImageView)findViewById(R.id.myicon);
        
        Button tweet = (Button) findViewById(R.id.btn_login);
        Button clearCredentials = (Button) findViewById(R.id.btn_clear_credentials);
        
        
        
        tweet.setOnClickListener(new View.OnClickListener() {
        	/**
        	 * Send a tweet. If the user hasn't authenticated to Tweeter yet, he'll be redirected via a browser
        	 * to the twitter login page. Once the user authenticated, he'll authorize the Android application to send
        	 * tweets on the users behalf.
        	 */
            public void onClick(View v) {
            	if (TwitterUtils.isAuthenticated(prefs)) {
            		Intent gohome = new Intent(getApplicationContext(), MyHomeActivity.class);
                    
                    startActivity(gohome);
            	} else {
    				Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
    				i.putExtra("tweet_msg",getTweetMsg());
    				startActivity(i);
            	}
            }
        });

        clearCredentials.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	clearCredentials();
            
            }
        });
	}
	
	@Override
	protected void onResume() {
		super.onResume();
    if (TwitterUtils.isAuthenticated(prefs)) {
    	
	    updateLoginStatus();
    	}
    else{
    	loginName.setText("Logged into Twitter : " + TwitterUtils.isAuthenticated(prefs));
    	 myiconview.setImageDrawable(getResources().getDrawable(R.drawable.empty));
    }
	}
	
	public void updateLoginStatus() {
		
		
		ConfigurationBuilder confBuild =new ConfigurationBuilder();
        confBuild.setOAuthConsumerKey(Constants.CONSUMER_KEY);
        confBuild.setOAuthConsumerSecret(Constants.CONSUMER_SECRET);
        confBuild.setOAuthAccessToken(AndroidTwitterSample.prefs.getString("oauth_token", ""));
        confBuild.setOAuthAccessTokenSecret(AndroidTwitterSample.prefs.getString("oauth_token_secret", ""));
        ProfileImage.ImageSize imageSize = ProfileImage.NORMAL;
        
        try {
        Twitter twitter = new TwitterFactory(confBuild.build()).getInstance();        
        UserName=twitter.getScreenName();
        
        ProfileImage image = twitter.getProfileImage(UserName,imageSize);
        
        imageURL=image.getURL();
       
        } catch (TwitterException te) {
			// TODO Auto-generated catch block
			te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
		}
        
       
        	try {
        	URL url =new URL(imageURL);
        	mybit = BitmapFactory.decodeStream(url.openStream());
        	} catch (Exception e) {
        	e.printStackTrace();
        	}
        	
     
		loginName.setText("Logged into User Name : " +UserName );
		 myiconview.setImageBitmap(mybit);
		
	}
	

	private String getTweetMsg() {
		return "Tweeting from Android App at " + new Date().toLocaleString();
	}	
	
//	public void sendTweet() {
//		Thread t = new Thread() {
//	        public void run() {
//	        	
//	        	try {
//	        		TwitterUtils.sendTweet(prefs,getTweetMsg());
//	        		mTwitterHandler.post(mUpdateTwitterNotification);
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//	        }
//
//	    };
//	    t.start();
//	}

	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
		loginName.setText("Logged into Twitter : " + TwitterUtils.isAuthenticated(prefs));
		 myiconview.setImageDrawable(getResources().getDrawable(R.drawable.empty));
	}
}