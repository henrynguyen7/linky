package com.hn.linky;

import java.io.File;

import com.hn.linky.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{	
	private int MenuShootImage = 0;
	private Uri mImageUri;
	
	private final String TAG = "MainActivity";
	
	private final int MAX_DRUNK_LEVEL = 10;
	private final int MAX_MWAH_LEVEL = 10;
	private final int MAX_HUGGLE_LEVEL = 10;
	
	private Context mContext;
	private Intent mIntent;	
	
	private int mDrunkLevel;	
	private TextView mDrunkLevelTextView;
	private SeekBar mDrunkLevelSeekBar;
	private Button mUpdateDrunkLevelButton;

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
	
	private int mRequestCode = 1;
	
    public void onCreate(Bundle savedInstanceState)
    {	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main_activity);
    	mContext = this.getApplicationContext();
    	mIntent = new Intent(MainActivity.this, LinkyIntentService.class);    	
    	
    	
    	
    	/** Drunk level **/
    	mDrunkLevelTextView = (TextView) findViewById(R.id.drunkLevelTextView);
    	mDrunkLevelTextView.setText("Drunk level: 0");    	
    	
    	mDrunkLevelSeekBar = (SeekBar) findViewById(R.id.drunkLevelSeekBar);
    	mDrunkLevelSeekBar.setMax(MAX_DRUNK_LEVEL);
    	mDrunkLevelSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
    	{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
			{
				mDrunkLevel = progress;
				mDrunkLevelTextView.setText("Drunk Level: " + progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}

			@Override
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
    	    	mIntent.setAction(LinkyIntentService.ACTION_UPDATE_DRUNK_LEVEL);
				mIntent.putExtra(LinkyIntentService.EXTRA_DRUNK_LEVEL, mDrunkLevel);
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
			@Override
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

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}

			@Override
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
    	    	mIntent.setAction(LinkyIntentService.ACTION_UPDATE_MWAH_LEVEL);
				mIntent.putExtra(LinkyIntentService.EXTRA_MWAH_LEVEL, mMwahLevel);
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
			@Override
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

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) 
			{			
				//do nothing
			}

			@Override
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
    	    	mIntent.setAction(LinkyIntentService.ACTION_UPDATE_HUGGLE_LEVEL);
				mIntent.putExtra(LinkyIntentService.EXTRA_HUGGLE_LEVEL, mHuggleLevel);
				startService(mIntent);
    	    }  
    	});
    	
    	
    	
    	/** UpdateAllLevels **/
    	mUpdateAllLevelsButton = (Button) findViewById(R.id.updateAllLevelsButton);
    	mUpdateAllLevelsButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {      	    	
    	    	mIntent.setAction(LinkyIntentService.ACTION_UPDATE_ALL_LEVELS);
    	    	mIntent.putExtra(LinkyIntentService.EXTRA_DRUNK_LEVEL, mDrunkLevel);
    	    	mIntent.putExtra(LinkyIntentService.EXTRA_MWAH_LEVEL, mMwahLevel);
				mIntent.putExtra(LinkyIntentService.EXTRA_HUGGLE_LEVEL, mHuggleLevel);
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
    }
    
    @Override
	protected void onStart() 
    {    	    	
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
	
	
//	private void sendInstapic()
//	{
//		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//	    File photo = null;
//	    try
//	    {
//	        // place where to store camera taken picture
//	        photo = this.createTemporaryFile("picture", ".jpg");
//	        photo.delete();
//	        Log.v(TAG, "Attempting instapic");
//	    }
//	    catch(Exception e)
//	    {
//	        Log.v(TAG, "Can't create file to take picture!");
//	        Toast.makeText(mContext, "Please check SD card! Image shot is impossible!", 10000);
//	    }
//	    
//	    mImageUri = Uri.fromFile(photo);	    
//	    
//	    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
//	    
//	    //start camera intent
//	    startActivityForResult(intent, mRequestCode);
//	}
	
	private void sendInstapic()
	{
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
	    startActivityForResult(intent, mRequestCode);
	}

	
	private File createTemporaryFile(String part, String ext) throws Exception
	{
	    File tempDir = Environment.getExternalStorageDirectory();
	    tempDir = new File(tempDir.getAbsolutePath()+ "/.temp/");
	    if (!tempDir.exists())
	    {
	        tempDir.mkdir();
	    }
	    return File.createTempFile(part, ext, tempDir);
	}
	

	//called after camera intent finished
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
	    if (requestCode == mRequestCode && resultCode == RESULT_OK)
	    {
	    	Intent sendMMSIntent = new Intent(Intent.ACTION_SEND); 
	    	sendMMSIntent.putExtra("address", "17144682423");
	    	sendMMSIntent.putExtra("sms_body", "test mms");
	    	sendMMSIntent.putExtra(Intent.EXTRA_STREAM, mImageUri); 
	    	sendMMSIntent.setType("image/png"); 
//	    	startActivity(sendMMSIntent);
	    }
	    super.onActivityResult(requestCode, resultCode, intent);
	}
}