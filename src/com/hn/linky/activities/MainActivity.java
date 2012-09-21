package com.hn.linky.activities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hn.linky.LinkyIntentService;
import com.hn.linky.LocalBinder;
import com.hn.linky.R;
import com.hn.linky.valueobjects.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{	
	//private final String TAG = "MainActivity";
	
	private int mDrunkLevel = 0;	
	private TextView mDrunkLevelTextView;
	private SeekBar mDrunkLevelSeekBar;
	
	private int mSleepyLevel = 0;	
	private TextView mSleepyLevelTextView;
	private SeekBar mSleepyLevelSeekBar;
	
	private int mMwahLevel = 0;	
	private TextView mMwahLevelTextView;
	private SeekBar mMwahLevelSeekBar;
	
	private int mHuggleLevel = 0;	
	private TextView mHuggleLevelTextView;
	private SeekBar mHuggleLevelSeekBar;
	
	private boolean mBound;
	private LinkyIntentService mService;
	private ServiceConnection mConnection = new ServiceConnection() 
	{
		public void onServiceConnected(ComponentName className, IBinder service) 
	    {
			@SuppressWarnings("unchecked")
			LocalBinder<LinkyIntentService> binder = (LocalBinder<LinkyIntentService>) service;
			mService = binder.getService();
			mBound = true;
	    }		

		public void onServiceDisconnected(ComponentName name) 
		{
			mBound = false;
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main_activity);    
    }
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		Intent receivedIntent = getIntent();
		if (receivedIntent.getBooleanExtra("shouldStartInstapic", false) == true)
		{
		    try
            {
                dispatchTakePictureIntent();
            }
            catch(IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}
		
		// Bind to LinkyIntentService
		Intent intent = new Intent(this, LinkyIntentService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
				
		// Set Seekbar OnProgressChanged Listeners		
		/* Drunk level */
    	mDrunkLevelTextView = (TextView) findViewById(R.id.drunkLevelTextView);
    	mDrunkLevelSeekBar = (SeekBar) findViewById(R.id.drunkLevelSeekBar);
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
    	
    	/* Sleepy level */
    	mSleepyLevelTextView = (TextView) findViewById(R.id.sleepyLevelTextView);
    	mSleepyLevelSeekBar = (SeekBar) findViewById(R.id.sleepyLevelSeekBar);    	
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
    	
    	/* Mwah level */
    	mMwahLevelTextView = (TextView) findViewById(R.id.mwahLevelTextView);
    	mMwahLevelSeekBar = (SeekBar) findViewById(R.id.mwahLevelSeekBar);    	
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
    	
    	/* Huggle level */
    	mHuggleLevelTextView = (TextView) findViewById(R.id.huggleLevelTextView);    	    	
    	mHuggleLevelSeekBar = (SeekBar) findViewById(R.id.huggleLevelSeekBar);
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
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		
		// Unbind from LinkyIntentService
		if (mBound)
		{
			unbindService(mConnection);
			mBound = false;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    return true;
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case R.id.menuSettings:
	        	Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
	    		startActivity(intent);
	            return true;
	        case R.id.menuHelp:
	            showHelp();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void showHelp()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Sorry... help's not available yet :(")
				.setCancelable(false)
				.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface arg0, int arg1) 
					{
					}
		       	});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/** Button onClick methods 
	 * Defined in main_activity.xml **/	
	
	public void updateDrunkLevelButton(final View view)
	{
		mService.updateDrunkLevel(mDrunkLevel);
	}
	
	public void updateSleepyLevelButton(final View view)
	{
		mService.updateSleepyLevel(mSleepyLevel);
	}
	
	public void updateMwahLevelButton(final View view)
	{
		mService.updateMwahLevel(mMwahLevel);
	}
	
	public void updateHuggleLevelButton(final View view)
	{
		mService.updateHuggleLevel(mHuggleLevel);
	}	
	
	public void updateAllLevelsButton(final View view)
	{
		mService.updateAllLevels(mDrunkLevel, mSleepyLevel, mMwahLevel, mHuggleLevel);
	}
	
	public void instapicButton(final View view)
	{
	    try
        {
            dispatchTakePictureIntent();
        }
        catch(IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    
		// mService.instapic();
	}
	
	private Uri instapticUri;
	
	private void dispatchTakePictureIntent() throws IOException 
	{
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    Uri fileUri = getOutputMediaFileUri();
	    instapticUri = fileUri; //Saved locally so it can be broadcast to gallery app for saving onActivityResult
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);        
        startActivityForResult(takePictureIntent, Constants.INSTAPIC_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	    if (requestCode == Constants.INSTAPIC_REQUEST_CODE) 
	    {
	        if (resultCode == RESULT_OK) 
	        {
	            // Image captured and saved to fileUri specified in the Intent
                mService.sendInstapic(instapticUri);
	            Toast.makeText(this, "Instapic saved and ready to send!", Toast.LENGTH_LONG).show();
	        } 
	        else if (resultCode == RESULT_CANCELED) 
	        {
	            this.finish();
	        } 
	        else 
	        {
	            // Image capture failed, advise user
	        }
	    }
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri()
	{
	      return Uri.fromFile(getOutputMediaFile());
	}

	/** Create a File for saving an image or video */
	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	private static File getOutputMediaFile()
	{
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "Linky");

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists())
	    {
	        if (! mediaStorageDir.mkdirs())
	        {
	            Log.d("Linky", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");

	    return mediaFile;
	}


	public void buzzButton(final View view)
	{
		mService.sendBuzz();
	}
}