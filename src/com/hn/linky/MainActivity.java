package com.hn.linky;

import com.hn.linky.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity
{	
	private final String TAG = "MainActivity";
	private Intent mIntent;	
	private String mImageUri;
	
	private final int MAX_DRUNK_LEVEL = 10;
	private final int MAX_SLEEPY_LEVEL = 10;
	private final int MAX_MWAH_LEVEL = 10;
	private final int MAX_HUGGLE_LEVEL = 10;
	private final int mRequestCode = 1;
	
	private int mDrunkLevel;	
	private TextView mDrunkLevelTextView;
	private SeekBar mDrunkLevelSeekBar;
	private Button mUpdateDrunkLevelButton;
	
	private int mSleepyLevel;	
	private TextView mSleepyLevelTextView;
	private SeekBar mSleepyLevelSeekBar;
	private Button mUpdateSleepyLevelButton;

	private int mMwahLevel;	
	private TextView mMwahLevelTextView;
	private SeekBar mMwahLevelSeekBar;
	private Button mUpdateMwahLevelButton;
	
	private int mHuggleLevel;	
	private TextView mHuggleLevelTextView;
	private SeekBar mHuggleLevelSeekBar;
	private Button mUpdateHuggleLevelButton;
	
	private Button mUpdateAllLevelsButton;
	private Button mInstapicButton;
	private Button mBuzzButton;
	
    public void onCreate(Bundle savedInstanceState)
    {	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main_activity);    	
    	mIntent = new Intent(MainActivity.this, LinkyIntentService.class);
    	
    	/** Drunk level **/
    	mDrunkLevelTextView = (TextView) findViewById(R.id.drunkLevelTextView);
    	mDrunkLevelTextView.setText("Drunk level: 0");    	
    	
    	mDrunkLevelSeekBar = (SeekBar) findViewById(R.id.drunkLevelSeekBar);
    	mDrunkLevelSeekBar.setMax(MAX_DRUNK_LEVEL);
    	mDrunkLevelSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
    	{
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
			{
				mDrunkLevel = progress;
				mDrunkLevelTextView.setText("Drunk Level: " + progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}

			public void onStopTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}
    	});    	
    	
    	mUpdateDrunkLevelButton = (Button) findViewById(R.id.updateDrunkLevelButton);
    	mUpdateDrunkLevelButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {      	    	
    	    	mIntent.setAction(Constants.ACTION_UPDATE_DRUNK_LEVEL);
				mIntent.putExtra(Constants.EXTRA_DRUNK_LEVEL, mDrunkLevel);
				startService(mIntent);
    	    }  
    	});
    	
    	
    	/** Sleepy level **/
    	mSleepyLevelTextView = (TextView) findViewById(R.id.sleepyLevelTextView);
    	mSleepyLevelTextView.setText("Sleepy level: 0");    	
    	
    	mSleepyLevelSeekBar = (SeekBar) findViewById(R.id.sleepyLevelSeekBar);
    	mSleepyLevelSeekBar.setMax(MAX_SLEEPY_LEVEL);
    	mSleepyLevelSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
    	{
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
			{
				mSleepyLevel = progress;
				mSleepyLevelTextView.setText("Sleepy Level: " + progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}

			public void onStopTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}
    	});    	
    	
    	mUpdateSleepyLevelButton = (Button) findViewById(R.id.updateSleepyLevelButton);
    	mUpdateSleepyLevelButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {      	    	
    	    	mIntent.setAction(Constants.ACTION_UPDATE_SLEEPY_LEVEL);
				mIntent.putExtra(Constants.EXTRA_SLEEPY_LEVEL, mSleepyLevel);
				startService(mIntent);
    	    }  
    	});
    	
    	
    	/** Mwah level **/
    	mMwahLevelTextView = (TextView) findViewById(R.id.mwahLevelTextView);
    	mMwahLevelTextView.setText("Mwah!");    	
    	
    	mMwahLevelSeekBar = (SeekBar) findViewById(R.id.mwahLevelSeekBar);
    	mMwahLevelSeekBar.setMax(MAX_MWAH_LEVEL);
    	mMwahLevelSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
    	{
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
			{
				mMwahLevel = progress;
				
				String appendText = "";
				for (int i = 0; i <= progress; ++i)
				{
					appendText = appendText.concat("a");
				}
				
				mMwahLevelTextView.setText("Mw" + appendText + "h!");
			}

			public void onStartTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}

			public void onStopTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}
    	});    	
    	
    	mUpdateMwahLevelButton = (Button) findViewById(R.id.updateMwahLevelButton);
    	mUpdateMwahLevelButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {      	    	
    	    	mIntent.setAction(Constants.ACTION_UPDATE_MWAH_LEVEL);
				mIntent.putExtra(Constants.EXTRA_MWAH_LEVEL, mMwahLevel);
				startService(mIntent);
    	    }  
    	});
    
    	
    	
    	/** Huggle level **/
    	mHuggleLevelTextView = (TextView) findViewById(R.id.huggleLevelTextView);
    	mHuggleLevelTextView.setText("Huggle!");    	
    	
    	mHuggleLevelSeekBar = (SeekBar) findViewById(R.id.huggleLevelSeekBar);
    	mHuggleLevelSeekBar.setMax(MAX_HUGGLE_LEVEL);
    	mHuggleLevelSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
    	{
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
			{
				mHuggleLevel = progress;
				
				String appendText = "";
				for (int i = 0; i <= progress; ++i)
				{
					appendText = appendText.concat("u");
				}
				
				mHuggleLevelTextView.setText("H" + appendText + "ggle!");
			}

			public void onStartTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}

			public void onStopTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}
    	});    	
    	
    	mUpdateHuggleLevelButton = (Button) findViewById(R.id.updateHuggleLevelButton);
    	mUpdateHuggleLevelButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {      	    	
    	    	mIntent.setAction(Constants.ACTION_UPDATE_HUGGLE_LEVEL);
				mIntent.putExtra(Constants.EXTRA_HUGGLE_LEVEL, mHuggleLevel);
				startService(mIntent);
    	    }  
    	});
    	
    	
    	
    	/** UpdateAllLevels **/
    	mUpdateAllLevelsButton = (Button) findViewById(R.id.updateAllLevelsButton);
    	mUpdateAllLevelsButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {      	    	
    	    	mIntent.setAction(Constants.ACTION_UPDATE_ALL_LEVELS);
    	    	mIntent.putExtra(Constants.EXTRA_DRUNK_LEVEL, mDrunkLevel);
    	    	mIntent.putExtra(Constants.EXTRA_SLEEPY_LEVEL, mSleepyLevel);
    	    	mIntent.putExtra(Constants.EXTRA_MWAH_LEVEL, mMwahLevel);
				mIntent.putExtra(Constants.EXTRA_HUGGLE_LEVEL, mHuggleLevel);
				startService(mIntent);
    	    }  
    	});
    	
    	
    	
    	/** Instapic **/
    	mInstapicButton = (Button) findViewById(R.id.instapicButton);
    	mInstapicButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {      	    	
    	    	try 
    	    	{
					sendInstapic();
				} 
    	    	catch (Exception e) 
				{
					e.printStackTrace();
				}
    	    }  
    	});
    	
    	
    	/** Buzz **/
    	mBuzzButton = (Button) findViewById(R.id.buzzButton);
    	mBuzzButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {      	    	
    	    	try 
    	    	{
        	    	mIntent.setAction(Constants.ACTION_SEND_BUZZ);        	    	
    				startService(mIntent);
				} 
    	    	catch (Exception e) 
				{
					Log.e(TAG, "Buzz failed.", e);
				}
    	    }  
    	});
    }
    
	protected void onStart() 
    {    	    	
    	super.onStart();
	}
    
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
            moveTaskToBack(true);
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
	
	private void sendInstapic()
	{
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
	    startActivityForResult(intent, mRequestCode);
	}

	//called after camera intent finished
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
	    if (requestCode == mRequestCode && resultCode == RESULT_OK)
	    {
	    	Intent sendMMSIntent = new Intent(Intent.ACTION_SEND); 
	    	sendMMSIntent.putExtra("address", "17144682423");
	    	sendMMSIntent.putExtra("sms_body", "test mms");
	    	sendMMSIntent.putExtra(Intent.EXTRA_STREAM, mImageUri); 
	    	sendMMSIntent.setType("image/png"); 
	    	startActivity(sendMMSIntent);
	    }
	    super.onActivityResult(requestCode, resultCode, intent);
	}
}