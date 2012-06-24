package com.ecs.android.sample.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.Paging;
import twitter4j.ProfileImage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.content.Intent;
import android.content.SharedPreferences;

public class MyHomeActivity extends Activity  {
	public TextView user_name;
	public ListView list;
	public int pagenumber;
	public SimpleAdapter listItemAdapter;
	public ArrayList<HashMap<String, Object>> listItem;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myhome);
        list = (ListView) findViewById(R.id.tweetlist); 
        ImageButton back = (ImageButton) findViewById(R.id.backBtn);
        ImageButton write = (ImageButton) findViewById(R.id.writeBtn);
        ImageButton refresh = (ImageButton) findViewById(R.id.refreshBtn);
        user_name = (TextView)findViewById(R.id.showName);
        ImageButton last = (ImageButton) findViewById(R.id.golast);
        ImageButton next = (ImageButton) findViewById(R.id.gonext);
        pagenumber=1;
        
    
        
        
        write.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View v) {
            	Intent intent=new Intent();              
            	intent.setClass(getApplicationContext(), updatetweet.class);            
            	startActivity(intent);              
            	MyHomeActivity.this.finish();   
            }
        });
 
        back.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View v) {
            	Intent intent=new Intent();              
            	intent.setClass(getApplicationContext(), AndroidTwitterSample.class);            
            	startActivity(intent);              
            	MyHomeActivity.this.finish();   
            }
        });
        
        
       next.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View v) {
            	 pagenumber++;
            	 int size = listItem.size();
            	 if( size > 0 )
            	 {
            	 listItem.remove(listItem.size() - 1);
            	 }
            	
            	 gettweets();
            }
        });
       
       last.setOnClickListener(new View.OnClickListener() {
       	
           public void onClick(View v) {
        	   if (pagenumber>1){
        		   pagenumber--;
        		   
        	   }
        	   int size = listItem.size();
          	 if( size > 0 )
          	 {
          	 listItem.remove(listItem.size() - 1);
          	 }
        	   gettweets();
        	   
           }
       });
       
	
	 refresh.setOnClickListener(new View.OnClickListener() {
     	
         public void onClick(View v) {
        	 int size = listItem.size();
        	 if( size > 0 )
        	 {
        	 listItem.remove(listItem.size() - 1);
        	 }
        	 gettweets();
         }
     });
	}
	 protected void onResume() {
 		super.onResume();
 		gettweets();
 	}
	
	public void gettweets(){
		ConfigurationBuilder confBuild =new ConfigurationBuilder();
        confBuild.setOAuthConsumerKey(Constants.CONSUMER_KEY);
        confBuild.setOAuthConsumerSecret(Constants.CONSUMER_SECRET);
        confBuild.setOAuthAccessToken(AndroidTwitterSample.prefs.getString("oauth_token", ""));
        confBuild.setOAuthAccessTokenSecret(AndroidTwitterSample.prefs.getString("oauth_token_secret", ""));
        listItem = new ArrayList<HashMap<String, Object>>();
        
       
        try {
        Twitter twitter = new TwitterFactory(confBuild.build()).getInstance();
        user_name.setText(twitter.getScreenName());
        Paging paging = new Paging(pagenumber, 10);
        List<Status> statuses = twitter.getFriendsTimeline(paging);
        for (Status status : statuses) {
           
            HashMap<String, Object> map = new HashMap<String, Object>();       
            map.put("username",status.getUser().getName() );  
            map.put("tweettext", status.getText()); 
            listItem.add(map);
        }
		} catch (TwitterException te) {
			// TODO Auto-generated catch block
			te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
		}
        
  
        
        
        listItemAdapter = new SimpleAdapter(this,listItem,   
                R.layout.tweets,
                new String[] {"username", "tweettext"},   
                new int[] {R.id.webuser,R.id.wbtext}
        ); 
        list.setAdapter(listItemAdapter); 
     }
		
}
