package com.ecs.android.sample.twitter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class updatetweet extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtweet);
        ImageButton back2 = (ImageButton) findViewById(R.id.backBtnfromadd);
        Button confirm = (Button) findViewById(R.id.confirmupdatetweet);
        ImageView  myicon2=(ImageView)findViewById(R.id.myicon2);
        TextView user_name = (TextView)findViewById(R.id.showName2);
        final EditText tweetb =(EditText)findViewById(R.id.tweetbody);
      
        
        user_name.setText(AndroidTwitterSample.UserName );
        myicon2.setImageBitmap(AndroidTwitterSample.mybit);
        
         back2.setOnClickListener(new View.OnClickListener() {
       	
            public void onClick(View v) {
            	goback();
            	  
            }
        });
         
         confirm.setOnClickListener(new View.OnClickListener() {
         	
             public void onClick(View v) {
            	 ConfigurationBuilder confBuild =new ConfigurationBuilder();
                 confBuild.setOAuthConsumerKey(Constants.CONSUMER_KEY);
                 confBuild.setOAuthConsumerSecret(Constants.CONSUMER_SECRET);
                 confBuild.setOAuthAccessToken(AndroidTwitterSample.prefs.getString("oauth_token", ""));
                 confBuild.setOAuthAccessTokenSecret(AndroidTwitterSample.prefs.getString("oauth_token_secret", ""));  
             
                 String textbody= tweetb.getText().toString();
                 Twitter twitter = new TwitterFactory(confBuild.build()).getInstance();
                 try {
					Status status = twitter.updateStatus(textbody);
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                 System.out.println("Successfully updated the status");
                 goback();
             }
         });
        
	}
	public void goback(){
	Intent intent2=new Intent();              
	intent2.setClass(getApplicationContext(), MyHomeActivity.class);            
	startActivity(intent2);              
	updatetweet.this.finish(); 
	}
}
