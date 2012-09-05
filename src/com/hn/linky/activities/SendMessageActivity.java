package com.hn.linky.activities;

import com.hn.linky.LinkyIntentService;
import com.hn.linky.R;
import com.hn.linky.utilities.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SendMessageActivity extends Activity
{		
	private Intent mIntent;	
	
    public void onCreate(Bundle savedInstanceState)
    {	
    	setContentView(R.layout.main);
    	mIntent = new Intent(SendMessageActivity.this, LinkyIntentService.class);
    	super.onCreate(savedInstanceState);
    }
    
    @Override
	protected void onStart() 
    {
    	Intent intent = new Intent(this, LinkyIntentService.class);
    	startService(intent);
				
		ImageView imageView = (ImageView) findViewById(R.id.imageView);
    	imageView.setOnClickListener(new OnClickListener()
    	{
			public void onClick(View v)
    		{    			
    			mIntent.setAction(Constants.ACTION_SEND_BUZZ);
				startService(mIntent);
    		}
    	});
    	
    	Button pokeButton = (Button) findViewById(R.id.pokeButton);
    	pokeButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {      	    	
    	    	mIntent.setAction(Constants.ACTION_SEND_MESSAGE);
				mIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_POKE);
				startService(mIntent);
    	    }  
    	});
    	
    	Button hugButton = (Button) findViewById(R.id.hugButton);
    	hugButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {  
    	    	mIntent.setAction(Constants.ACTION_SEND_MESSAGE);
				mIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_HUG);
				startService(mIntent);
    	    }  
    	});
    	
    	Button kissButton = (Button) findViewById(R.id.kissButton);
    	kissButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {  
    	    	mIntent.setAction(Constants.ACTION_SEND_MESSAGE);
				mIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_KISS);
				startService(mIntent);
    	    }  
    	});
    	
    	Button tickleButton = (Button) findViewById(R.id.tickleButton);
    	tickleButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {  
    	    	mIntent.setAction(Constants.ACTION_SEND_MESSAGE);
				mIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_TICKLE);
				startService(mIntent);
    	    }  
    	});
    	
    	Button holdHandsButton = (Button) findViewById(R.id.holdHandsButton);
    	holdHandsButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {  
    	    	mIntent.setAction(Constants.ACTION_SEND_MESSAGE);
				mIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_HOLDHANDS);
				startService(mIntent);
    	    }  
    	});
    	
    	Button missesYouButton = (Button) findViewById(R.id.missYouButton);
    	missesYouButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {  
    	    	mIntent.setAction(Constants.ACTION_SEND_MESSAGE);
				mIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_MISSESYOU);
				startService(mIntent);
    	    }  
    	});
    	
    	Button smileLowButton = (Button) findViewById(R.id.smileLowButton);
    	smileLowButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {  
    	    	mIntent.setAction(Constants.ACTION_SEND_MESSAGE);
				mIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_SMILE_LOW);
				startService(mIntent);
    	    }  
    	});
    	
    	Button smileMediumButton = (Button) findViewById(R.id.smileMediumButton);
    	smileMediumButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {  
    	    	mIntent.setAction(Constants.ACTION_SEND_MESSAGE);
				mIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_SMILE_MEDIUM);
				startService(mIntent);
    	    }  
    	});
    	
    	Button smileHighButton = (Button) findViewById(R.id.smileHighButton);
    	smileHighButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {  
    	    	mIntent.setAction(Constants.ACTION_SEND_MESSAGE);
				mIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_SMILE_HIGH);
				startService(mIntent);
    	    }  
    	});
    	
    	super.onStart();
	}
    
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
            moveTaskToBack(true);
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
}