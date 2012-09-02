package com.hn.linky;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;

public class LinkyIntentService extends IntentService
{
	public static final String TAG = "LinkyIntentService";
	
	private String mSourceNumber;
	private String mLinkedNumber;	
	
	private Handler mHandler;	
	private String mMessage;
	private Uri mImageUri;
	
	public LinkyIntentService() 
	{
		super("LinkyIntentService");		
	}
	
	@Override
	public void onCreate() 
	{	
		mHandler = new Handler();		
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		mSourceNumber = telephonyManager.getLine1Number();
		mLinkedNumber = getLinkedNumber();
		
		BugSenseHandler.setup(this, "abc580a4");
		
		super.onCreate();
	}	
	
	private String getLinkedNumber()
    {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	return sharedPreferences.getString(Constants.SHARED_PREF_LINKED_NUMBER, null);
    }
		
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		if (intent.getExtras() != null)
		{
			Bundle bundle = intent.getExtras();
			String action = intent.getAction();		
			
			if (action.equals(Constants.ACTION_SEND_MESSAGE))
			{
				int messageType = bundle.getInt(Constants.EXTRA_MESSAGE);
				updateMessage(messageType);
			}
			else if (action.equals(Constants.ACTION_SEND_BUZZ))
			{
				sendBuzz(true);
			}
			else if (action.equals(Constants.ACTION_UPDATE_DRUNK_LEVEL))
			{
				int drunkLevel = bundle.getInt(Constants.EXTRA_DRUNK_LEVEL);
				updateDrunkLevel(drunkLevel);
			}
			else if (action.equals(Constants.ACTION_UPDATE_MWAH_LEVEL))
			{
				int mwahLevel = bundle.getInt(Constants.EXTRA_MWAH_LEVEL);
				updateMwahLevel(mwahLevel);
			}			
			else if (action.equals(Constants.ACTION_UPDATE_HUGGLE_LEVEL))
			{
				int huggleLevel = bundle.getInt(Constants.EXTRA_HUGGLE_LEVEL);
				updateHuggleLevel(huggleLevel);
			}
			else if (action.equals(Constants.ACTION_UPDATE_ALL_LEVELS))
			{
				int drunkLevel = bundle.getInt(Constants.EXTRA_DRUNK_LEVEL);
				int mwahLevel = bundle.getInt(Constants.EXTRA_MWAH_LEVEL);
				int huggleLevel = bundle.getInt(Constants.EXTRA_HUGGLE_LEVEL);
				updateAllLevels(drunkLevel, mwahLevel, huggleLevel);
			}
			else if (action.equals(Constants.ACTION_WIDGET_UPDATE_DRUNK_LEVEL))
			{
				int drunkLevel = bundle.getInt(Constants.EXTRA_DRUNK_LEVEL);
				updateDrunkLevel(drunkLevel);
			}
			else if (action.equals(Constants.ACTION_WIDGET_UPDATE_SLEEPY_LEVEL))
			{
				int sleepyLevel = bundle.getInt(Constants.EXTRA_SLEEPY_LEVEL);
				updateSleepyLevel(sleepyLevel);
			}
			else if (action.equals(Constants.ACTION_INSTAPIC))
			{
				
			}
		}
	}
	
    private void updateDrunkLevel(int drunkLevel)
    {
    	mMessage = "Drunk Level: " + drunkLevel + "!";
    	sendMessage(mMessage);
    }
    
    private void updateMwahLevel(int mwahLevel)
    {
    	mMessage = "Mwah Level: " + mwahLevel + "!";
    	sendMessage(mMessage);
    }
    
    private void updateHuggleLevel(int huggleLevel)
    {
    	mMessage = "Huggle Level: " + huggleLevel + "!";
    	sendMessage(mMessage);
    }
        
    private void updateSleepyLevel(int sleepyLevel)
    {
    	mMessage = "Sleepy Level: " + sleepyLevel + "!";
    	sendMessage(mMessage);
    }
    
    private void updateAllLevels(int drunkLevel, int mwahLevel, int huggleLevel)
    {
    	mMessage = "Drunk: " + drunkLevel + "! Mwahs: " + mwahLevel + "! Huggles: " + huggleLevel + "!";
    	sendMessage(mMessage);;
    }
    
    private void updateMessage(int messageType) 
    {            	
    	switch (messageType)
    	{
    		case Constants.MESSAGE_POKE: 			mMessage = "*poke*"; 			break;
    		case Constants.MESSAGE_KISS: 			mMessage = "*mwaaah*"; 			break;
    		case Constants.MESSAGE_HUG:  			mMessage = "*huggle*"; 			break;
    		case Constants.MESSAGE_TICKLE: 			mMessage = "*tickle*";			break;
    		case Constants.MESSAGE_HOLDHANDS: 		mMessage = "*holds hands*";		break;    		
    		case Constants.MESSAGE_MISSESYOU: 		mMessage = ":( I miss you.";	break;
    		case Constants.MESSAGE_SMILE_LOW: 		mMessage = ":)";				break;
    		case Constants.MESSAGE_SMILE_MEDIUM:	mMessage = ":D";				break;
    		case Constants.MESSAGE_SMILE_HIGH: 		mMessage = "^_^";				break;
    		
    		case Constants.MESSAGE_WIDGET_POKE: 	mMessage = "*poke!* (w)"; 		break;
    		case Constants.MESSAGE_WIDGET_KISS: 	mMessage = "*mwaaah!* (w)"; 	break;
    		case Constants.MESSAGE_WIDGET_HUG:  	mMessage = "*huggle!* (w)"; 	break;
    		case Constants.MESSAGE_WIDGET_TICKLE:	mMessage = "*tickle!* (w)";		break;
    		    		
    		default: 								mMessage = "*poke* from Tiff!";	break;
    	}
    	
        sendMessage(mMessage);
    }
    
    private void sendMessage(String message)
    {
        try 
        {	
        	PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, LinkyIntentService.class), 0);
        	SmsManager smsManager = SmsManager.getDefault();
        	smsManager.sendTextMessage(mLinkedNumber, null, message, pendingIntent, null);
        	
        	// store the sent sms in the sent folder
        	ContentValues values = new ContentValues();
        	values.put("address", mLinkedNumber);
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
		Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
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
			  Toast.makeText(LinkyIntentService.this, mText, Toast.LENGTH_SHORT).show();			  
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