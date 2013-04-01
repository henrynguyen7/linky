package com.hn.linky;

import java.util.ArrayList;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.hn.linky.valueobjects.Constants;
import com.hn.linky.valueobjects.ISharedPreferences;

public class LinkyIntentService extends IntentService implements ISharedPreferences
{
	public static final String TAG = "LinkyIntentService";
	
	private final IBinder mBinder = new LocalBinder<LinkyIntentService>(this);
	private final int VIBRATION_DURATION = 450;
	private String mLinkedNumber;	
	private String mForwardingNumber;
	private String mMessage;	
		
	public LinkyIntentService() 
	{
		super("LinkyIntentService");		
	}	

	@Override
	public void onCreate() 
	{	
		super.onCreate();
		
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		setSourceNumber(telephonyManager.getLine1Number());		
		mForwardingNumber = getForwardingNumber();
		mLinkedNumber = getLinkedNumber();
		
		BugSenseHandler.setup(this, "abc580a4");
	}	
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return mBinder;
	}
	
	private String getForwardingNumber()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(SHARED_PREF_FORWARDING_NUMBER, null);
    }
	
	private String getLinkedNumber()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(SHARED_PREF_LINKED_NUMBER, null);
    }
    
    private void setSourceNumber(String phoneNumber)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_SOURCE_NUMBER, phoneNumber);
        editor.commit();    
    }
	
    private void updateLevel(String action, int level)
    {
        if (action.equals(Constants.ACTION_UPDATE_DRUNK_LEVEL))
        {
            mMessage = "Drunk Level: " + level + "!";
            sendMessage(mMessage);
        }
        else if (action.equals(Constants.ACTION_UPDATE_SLEEPY_LEVEL))
        {
            mMessage = "Sleepy Level: " + level + "!";
            sendMessage(mMessage);
        }
        else if (action.equals(Constants.ACTION_UPDATE_HUGGLE_LEVEL))
        {
            mMessage = "Huggle Level: " + level + "!";
            sendMessage(mMessage);
        }
        else if (action.equals(Constants.ACTION_UPDATE_MWAH_LEVEL))
        {
            mMessage = "Mwah Level: " + level + "!";
            sendMessage(mMessage);
        }
    }
    
    private void updateAllLevels(int drunkLevel, int sleepyLevel, int mwahLevel, int huggleLevel)
    {
    	mMessage = "Drunk: " + drunkLevel + "! Sleepy: " + sleepyLevel + "! Mwahs: " + mwahLevel + "! Huggles: " + huggleLevel + "!";
    	sendMessage(mMessage);
    }
    
    public void sendBuzz()
    {   
    	try
    	{
    		mMessage = Constants.BUZZ_KEY;
        	PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, LinkyIntentService.class), 0);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mLinkedNumber, null, mMessage, pendingIntent, null);
    	}
    	catch (Exception e)
    	{
    		Log.e(TAG, "Buzz Failed.", e);
    	}    	
    	        	
        vibrate();
        String message = "BUZZING " + mLinkedNumber + "!";
        Toast.makeText(LinkyIntentService.this, message, Toast.LENGTH_LONG).show();
    }    
	
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		if (intent.getAction() != null)
		{
			String action = intent.getAction();

			/*
			 * Widget actions 
			 */
			if (action.equals(Constants.ACTION_SEND_WIDGET_POKE))
			{
				mMessage = "*poke!* (w)";
		    	sendMessage(mMessage);
			}
			else if (action.equals(Constants.ACTION_SEND_WIDGET_HUGGLE))
			{
				mMessage = "*huggle!* (w)";
		    	sendMessage(mMessage);
			}
			else if (action.equals(Constants.ACTION_SEND_WIDGET_MWAH))
			{
				mMessage = "*mwah!* (w)";
		    	sendMessage(mMessage);
			}
			else if (action.equals(Constants.ACTION_SEND_BUZZ))
			{
				sendBuzz();
			}
			else if (action.equals(Constants.ACTION_AUTHENTICATE_BUZZ))
            {
                String message = intent.getStringExtra(Constants.EXTRA_SMS_MESSAGE);
                String origin = intent.getStringExtra(Constants.EXTRA_ORIGINATING_ADDRESS);
                if (isBuzzAuthorized(message))
                {
                    buzzSelf(true, origin);
                }               
            }
			
			/*
			 * MainActivity actions
			 */
			else if (action.equals(Constants.ACTION_UPDATE_DRUNK_LEVEL))
            {
                int level = intent.getIntExtra(Constants.EXTRA_LEVEL, 0);
                updateLevel(Constants.ACTION_UPDATE_DRUNK_LEVEL, level);
            }
			else if (action.equals(Constants.ACTION_UPDATE_SLEEPY_LEVEL))
            {
                int level = intent.getIntExtra(Constants.EXTRA_LEVEL, 0);
                updateLevel(Constants.ACTION_UPDATE_SLEEPY_LEVEL, level);
            }
			else if (action.equals(Constants.ACTION_UPDATE_MWAH_LEVEL))
            {
                int level = intent.getIntExtra(Constants.EXTRA_LEVEL, 0);
                updateLevel(Constants.ACTION_UPDATE_MWAH_LEVEL, level);
            } 
			else if (action.equals(Constants.ACTION_UPDATE_HUGGLE_LEVEL))
            {
                int level = intent.getIntExtra(Constants.EXTRA_LEVEL, 0);
                updateLevel(Constants.ACTION_UPDATE_HUGGLE_LEVEL, level);
            }
			else if (action.equals(Constants.ACTION_UPDATE_ALL_LEVELS))
            {
			    int drunkLevel = intent.getIntExtra(Constants.EXTRA_DRUNK_LEVEL, 0);
			    int sleepyLevel = intent.getIntExtra(Constants.EXTRA_SLEEPY_LEVEL, 0);
			    int mwahLevel = intent.getIntExtra(Constants.EXTRA_MWAH_LEVEL, 0);
			    int huggleLevel = intent.getIntExtra(Constants.EXTRA_HUGGLE_LEVEL, 0);
			    updateAllLevels(drunkLevel, sleepyLevel, mwahLevel, huggleLevel);
            }
			
			/* 
			 * SMS Forwarding Actions
			 */
			else if (action.equals(Constants.ACTION_INSERT_SMS))
            {
                String message = intent.getStringExtra(Constants.EXTRA_SMS_MESSAGE);
                String origin = intent.getStringExtra(Constants.EXTRA_ORIGINATING_ADDRESS);
                insertSms(message, origin);      
            } 
			else if (action.equals(Constants.ACTION_FORWARD_SMS))
            {
                Bundle bundle = intent.getBundleExtra(Constants.EXTRA_BUNDLE);
                
                SmsMessage[] messages = null;
                String message = null;
                String origin = null;
                
                Object[] pdus = (Object[]) bundle.get("pdus");
                messages = new SmsMessage[pdus.length]; 
                
                for (int i = 0; i < messages.length; i++)
                {
                    if (i == 0) 
                    {
                        messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        origin = messages[i].getOriginatingAddress();
                        message = "<LINKY>(" + origin + ")";
                        message += messages[i].getMessageBody().toString();
                    }
                    else
                    {
                        messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        message += messages[i].getMessageBody().toString();
                    }
                }
                
                try 
                {   
                    // prepare message to send
                    SmsManager smsManager = SmsManager.getDefault();
                    ArrayList<String> messagesArrayList = smsManager.divideMessage(message);
                    int messageCount = messagesArrayList.size();
                    ArrayList<PendingIntent> sendIntents = new ArrayList<PendingIntent>(messageCount);
                    
                    // send as multipart message
                    smsManager.sendMultipartTextMessage(mForwardingNumber, null, messagesArrayList, sendIntents, null);
                    
                    // store the sent sms in the sent folder
                    ContentValues values = new ContentValues();
                    values.put("address", mForwardingNumber);
                    values.put("body", message);
                    this.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
                } 
                catch (Exception e) 
                {
                    Log.e(TAG, "SMS Outgoing failed.", e);            
                }
            }
		}
	}
    
	private void insertSms(String message, String originNumber)
    {
	    try 
        {  
            // Store the sent sms in the inbox folder
            ContentValues values = new ContentValues();
            values.put("address", originNumber);
            values.put("body", message);
            this.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
            purgeLinkyMessages();
        } 
        catch (Exception e) 
        {
            Log.e(TAG, "SMS Insertion failed.", e);            
        }   
    }
	
	private void purgeLinkyMessages()
	{
	    Uri deleteUri = Uri.parse("content://sms/inbox");
	    Cursor c = getContentResolver().query(deleteUri, null, null, null, null); 
	    c.moveToFirst(); 
	    
	    /* Used to determine column names which vary by device
	    String[] columnNames = c.getColumnNames();
	    for (int i = 0; i < columnNames.length; ++i)
	    {
	        Log.e(TAG, i + columnNames[i]);
	    }
	    */
	    
	    while (c.moveToNext())
	    {
	        try
	        {
	            String body = c.getString(12);

	            if (body.startsWith(Constants.LINKY_KEY))
	            {   
	                String pid = c.getString(1);
	                String uri = "content://sms/conversations/" + pid;
	                getContentResolver().delete(Uri.parse(uri), null, null);
	                stopSelf();
	            }
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	    } 
	}
    
    private void sendMessage(String message)
    {
        try 
        {	
        	PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, LinkyIntentService.class), 0);
        	SmsManager smsManager = SmsManager.getDefault();
        	smsManager.sendTextMessage(mLinkedNumber, null, message, pendingIntent, null);
        	
        	// Store the sent sms in the sent folder
        	ContentValues values = new ContentValues();
        	values.put("address", mLinkedNumber);
        	values.put("body", message);
        	this.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
        	
        	Toast.makeText(LinkyIntentService.this, message, Toast.LENGTH_SHORT).show();
        	vibrate();
        } 
        catch (Exception e) 
        {
            Log.e(TAG, "SMS Outgoing failed.", e);            
        } 
    }
    
    private boolean isBuzzAuthorized(String message)
    {    
    	if (message.contentEquals(Constants.BUZZ_KEY))
    	{
    		return true;
    	}    	
    	else
    	{
    		return false;
    	}
    }

    private void buzzSelf(boolean shouldBuzz, String originNumber)
    {    
    	if (shouldBuzz)
    	{	
    		int dot = 50;      
    		int dash = 125;     
    		int short_gap = 50;
    		int medium_gap = 125;
    		int long_gap = 500;
    		long[] pattern = {
    		    2000,  // Start immediately
    		    dash, medium_gap, 
    		    dot, short_gap, dot, short_gap, dash, medium_gap, dot, long_gap, 
    		    dash, short_gap, dash
    		};
    		
    		Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
    		vibrator.vibrate(pattern, -1);
    		
    		String message = "BUZZ from " + originNumber + "!";
    		Toast.makeText(LinkyIntentService.this, message, Toast.LENGTH_LONG).show();
    	}    	
    }
    
    private void vibrate()
    {
		Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(VIBRATION_DURATION);
    }

	
	public void sendInstapic(Uri instapicUri)
    {
	    Intent intent = new Intent(Intent.ACTION_SEND); 
	    intent.putExtra("address", mLinkedNumber);
        intent.putExtra("sms_body", "Linky Instapic!");
        intent.putExtra(Intent.EXTRA_STREAM, instapicUri); 
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/png"); 
        startActivity(intent);
        
        addPicToGallery(instapicUri);
    }
	
    private void addPicToGallery(Uri uri) 
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        this.sendBroadcast(mediaScanIntent);
    }
}