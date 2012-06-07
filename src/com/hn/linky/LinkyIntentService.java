package com.hn.linky;

import java.io.File;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;

public class LinkyIntentService extends IntentService
{
	public static final String ACTION_SEND_MESSAGE = "com.hn.linky.action.SEND_MESSAGE";
	public static final String ACTION_SEND_BUZZ = "com.hn.linky.action.SEND_BUZZ";
	public static final String ACTION_UPDATE_DRUNK_LEVEL = "com.hn.linky.action.UPDATE_DRUNK_LEVEL";
	public static final String ACTION_UPDATE_MWAH_LEVEL = "com.hn.linky.action.UPDATE_MWAH_LEVEL";
	public static final String ACTION_UPDATE_HUGGLE_LEVEL = "com.hn.linky.action.UPDATE_HUGGLE_LEVEL";		
	public static final String ACTION_UPDATE_ALL_LEVELS = "com.hn.linky.action.UPDATE_ALL_LEVELS";
	public static final String ACTION_WIDGET_UPDATE_DRUNK_LEVEL = "com.hn.linky.action.WIDGET_UPDATE_DRUNK_LEVEL";	
	public static final String ACTION_WIDGET_UPDATE_SLEEPY_LEVEL = "com.hn.linky.action.WIDGET_UPDATE_SLEEPY_LEVEL";
	public static final String ACTION_INSTAPIC = "com.hn.linky.action.INSTAPIC";
	
	public static final String EXTRA_MESSAGE = "messageType";
	public static final String EXTRA_DRUNK_LEVEL = "drunkLevel";
	public static final String EXTRA_MWAH_LEVEL = "mwahLevel";
	public static final String EXTRA_HUGGLE_LEVEL = "huggleLevel";
	public static final String EXTRA_SLEEPY_LEVEL = "sleepyLevel";
	
	public static final int MESSAGE_POKE = 0;	
	public static final int MESSAGE_KISS = 1;
	public static final int MESSAGE_HUG = 2;
	public static final int MESSAGE_TICKLE = 3;
	public static final int MESSAGE_HOLDHANDS = 4;
	public static final int MESSAGE_MISSESYOU = 5;
	public static final int MESSAGE_SMILE_LOW = 6;
	public static final int MESSAGE_SMILE_MEDIUM = 7;
	public static final int MESSAGE_SMILE_HIGH = 8;
	public static final int MESSAGE_WIDGET_POKE = 9;
	public static final int MESSAGE_WIDGET_KISS = 10;
	public static final int MESSAGE_WIDGET_HUG = 11;
	public static final int MESSAGE_WIDGET_TICKLE = 12;
		
	public static final String SHARED_PREFERENCES = "SharedPreferences";
	
	public static final String TAG = "LinkyIntentService";
	
	private Context mContext;
	private Handler mHandler;	
	private String mSourceNumber;
	private String mDestinationNumber = "7144682423"; //TODO: Upon first login/registration, prompt user to select "linked device" 
	private int mDrunkLevel = 0;
	private int mMwahLevel = 0;
	private int mHuggleLevel = 0;
	private int mSleepyLevel = 0;
	private String mMessage;

	private Uri mImageUri;
	public LinkyIntentService() 
	{
		super("LinkyIntentService");		
	}
	
	@Override
	public void onCreate() 
	{	
		super.onCreate();
		
		mHandler = new Handler();
		TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		mSourceNumber = telephonyManager.getLine1Number();
		mContext = this;
		
		BugSenseHandler.setup(this, "abc580a4");
	}	
		
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		if (intent.getExtras() != null)
		{
			Bundle bundle = intent.getExtras();
			String action = intent.getAction();		
				
			if (action.equals(ACTION_SEND_MESSAGE))
			{
				int messageType = bundle.getInt(EXTRA_MESSAGE);
				updateMessage(messageType);
			}
			else if (action.equals(ACTION_SEND_BUZZ))
			{
				sendBuzz(true);
			}
			else if (action.equals(ACTION_UPDATE_DRUNK_LEVEL))
			{
				int drunkLevel = bundle.getInt(EXTRA_DRUNK_LEVEL);
				updateDrunkLevel(drunkLevel);
			}
			else if (action.equals(ACTION_UPDATE_MWAH_LEVEL))
			{
				int mwahLevel = bundle.getInt(EXTRA_MWAH_LEVEL);
				updateMwahLevel(mwahLevel);
			}			
			else if (action.equals(ACTION_UPDATE_HUGGLE_LEVEL))
			{
				int huggleLevel = bundle.getInt(EXTRA_HUGGLE_LEVEL);
				updateHuggleLevel(huggleLevel);
			}
			else if (action.equals(ACTION_UPDATE_ALL_LEVELS))
			{
				int drunkLevel = bundle.getInt(EXTRA_DRUNK_LEVEL);
				int mwahLevel = bundle.getInt(EXTRA_MWAH_LEVEL);
				int huggleLevel = bundle.getInt(EXTRA_HUGGLE_LEVEL);
				updateAllLevels(drunkLevel, mwahLevel, huggleLevel);
			}
			else if (action.equals(ACTION_WIDGET_UPDATE_DRUNK_LEVEL))
			{
				int drunkLevel = bundle.getInt(EXTRA_DRUNK_LEVEL);
				updateDrunkLevel(drunkLevel);
			}
			else if (action.equals(ACTION_WIDGET_UPDATE_SLEEPY_LEVEL))
			{
				int sleepyLevel = bundle.getInt(EXTRA_SLEEPY_LEVEL);
				updateSleepyLevel(sleepyLevel);
			}
			else if (action.equals(ACTION_INSTAPIC))
			{
				
			}
		}
	}
	
    private void updateDrunkLevel(int drunkLevel)
    {
    	mDrunkLevel = drunkLevel;
    	mMessage = "Drunk Level: " + drunkLevel + "!";
    	sendMessage(mMessage);
    }
    
    private void updateMwahLevel(int mwahLevel)
    {
    	mMwahLevel = mwahLevel;
    	mMessage = "Mwah Level: " + mwahLevel + "!";
    	sendMessage(mMessage);
    }
    
    private void updateHuggleLevel(int huggleLevel)
    {
    	mHuggleLevel = huggleLevel;
    	mMessage = "Huggle Level: " + huggleLevel + "!";
    	sendMessage(mMessage);
    }
        
    private void updateSleepyLevel(int sleepyLevel)
    {
    	mSleepyLevel = sleepyLevel;
    	mMessage = "Sleepy Level: " + sleepyLevel + "!";
    	sendMessage(mMessage);
    }
    
    private void updateAllLevels(int drunkLevel, int mwahLevel, int huggleLevel)
    {
    	mDrunkLevel = drunkLevel;
    	mMwahLevel = mwahLevel;
    	mHuggleLevel = huggleLevel;
    	mMessage = "Drunk: " + drunkLevel + "! Mwahs: " + mwahLevel + "! Huggles: " + huggleLevel + "!";
    	sendMessage(mMessage);;
    }
    
    private void updateMessage(int messageType) 
    {            	
    	switch (messageType)
    	{
    		case MESSAGE_POKE: 			mMessage = "*poke* from Tiff!"; 		break;
    		case MESSAGE_KISS: 			mMessage = "*mwaaah* from Tiff!"; 		break;
    		case MESSAGE_HUG:  			mMessage = "*huggle* from Tiff!"; 		break;
    		case MESSAGE_TICKLE: 		mMessage = "*tickle* from Tiff!";		break;
    		case MESSAGE_HOLDHANDS: 	mMessage = "*holds hands* from Tiff!";	break;    		
    		case MESSAGE_MISSESYOU: 	mMessage = ":( I miss you.";			break;
    		case MESSAGE_SMILE_LOW: 	mMessage = ":) from Tiff!";				break;
    		case MESSAGE_SMILE_MEDIUM:	mMessage = ":D from Tiff!";				break;
    		case MESSAGE_SMILE_HIGH: 	mMessage = "^_^ from Tiff!";			break;
    		
    		case MESSAGE_WIDGET_POKE: 	mMessage = "*poke* from Tiff! (w)"; 	break;
    		case MESSAGE_WIDGET_KISS: 	mMessage = "*mwaaah* from Tiff! (w)"; 	break;
    		case MESSAGE_WIDGET_HUG:  	mMessage = "*huggle* from Tiff! (w)"; 	break;
    		case MESSAGE_WIDGET_TICKLE:	mMessage = "*tickle* from Tiff! (w)";	break;
    		    		
    		default: 					mMessage = "*poke* from Tiff!";			break;
    	}
    	
        sendMessage(mMessage);
    }
    
    private void sendMessage(String message)
    {
        try 
        {	
        	PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, LinkyIntentService.class), 0);
        	SmsManager smsManager = SmsManager.getDefault();
        	smsManager.sendTextMessage(mDestinationNumber, null, message, pendingIntent, null);
        	
        	// store the sent sms in the sent folder
        	ContentValues values = new ContentValues();
        	values.put("address", mDestinationNumber);
        	values.put("body", message);
        	this.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
        	
        	displayToast(message);
        	vibrate();
        } 
        catch (Exception e) 
        {
        	String logMessage = "SMS Outgoing failed.";
            Log.e(TAG, logMessage, e);            
        } 
    }
    
    private void sendBuzz(boolean shouldSendBuzz)
    {    
    	if(shouldSendBuzz)
    	{
    		mMessage = "BUZZ!";
        	sendMessage(mMessage);
    	}    	
    }
    
    private void displayToast(String message)
    {
    	mHandler.post(new DisplayToast(message));
    }
    
    private void vibrate()
    {
		Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(500);
    }

	private class DisplayToast implements Runnable
	{
		  String mText;

		  public DisplayToast(String text)
		  {
			  mText = text;
		  }

		  public void run()
		  {
			  Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
		  }
	}
	
	private void instapic()
	{
//		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//	    File photo;
//	    try
//	    {
//	        // place where to store camera taken picture
//	        photo = this.createTemporaryFile("picture", ".jpg");
//	        photo.delete();
//	    }
//	    catch(Exception e)
//	    {
//	        Log.v(TAG, "Can't create file to take picture!");
//	        //Toast.makeText(mContext, "Please check SD card! Image shot is impossible!", 10000);	        
//	    }
//	    mImageUri = Uri.fromFile(photo);
//	    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
//	    
//	    //start camera intent
//	    startActivityForResult(this, intent, MenuShootImage);
	}
}