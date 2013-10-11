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

/**
 * The main IntentService for the app. Receives all intents from inside the app and 
 * processes them, either by creating and sending the SMSes or by receiving forwarded
 * SMSes, parsing them, and inserting them into the SMS local database.
 * 
 * Sends messages, takes Instapics, processes Buzzes, and handles insertion of forwarded SMSes.
 * 
 * @author henry@dxconcept.com
 *
 */
public class LinkyIntentService extends IntentService implements ISharedPreferences
{
	public static final String TAG = "LinkyIntentService";
	
	private static final int VIBRATION_DURATION = 450;
	
	private final IBinder mBinder = new LocalBinder<LinkyIntentService>(this);
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
	
	/**
	 * Starts an intent to capture a picture, add it to the gallery, and then send it as an MMS,
	 * all in one tap of the button.
	 * 
	 * @param instapicUri: the path to the captured picture
	 */
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

    /**
     * Sends a specially-formatted SMS to the Linked phone. If the phone has Linky installed,
     * the app will cause the phone to vibrate in a pre-defined rhythm and display
     * a toast notification.
     */
    public void sendBuzz()
    {   
    	try
    	{
    		mMessage = Constants.BUZZ_KEY;
    		Intent intent = new Intent(this, LinkyIntentService.class);
        	PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mLinkedNumber, null, mMessage, pendingIntent, null);
            
            vibrate();
            String message = "BUZZING " + mLinkedNumber + "!";
            Toast.makeText(LinkyIntentService.this, message, Toast.LENGTH_LONG).show();
    	}
    	catch (Exception e)
    	{
    		Log.e(TAG, "Buzz Failed.", e);
    	}
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
			    
			    // Must wait for the native sms app to insert the sms before we can delete it
			    new Thread()
                { 
                    public void run()
                    { 
                        try 
                        { 
                            // TODO: This is hacky... find better way to know when native app has inserted sms
                            sleep(1000);
                            purgeLinkyMessages();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }.start();
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
                        message = Constants.LINKY_KEY + "(" + origin + ")";
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
	
    /**
     * Queries the Shared Preferences for the stored forwarding number.
     * 
     * @return the number to forward all SMSes to.
     */
    private String getForwardingNumber()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        return sharedPreferences.getString(SHARED_PREF_FORWARDING_NUMBER, null);
    }
    
    /**
     * Queries the Shared Preferences for the stored linked number.
     * 
     * @return the number to send all Linky SMSes to.
     */
    private String getLinkedNumber()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        return sharedPreferences.getString(SHARED_PREF_LINKED_NUMBER, null);
    }
    
    /**
     * Sets the source number, a.k.a. the number of the phone the app is installed on.
     * 
     * @param sourceNumber: the source number
     */
    private void setSourceNumber(String sourceNumber)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_SOURCE_NUMBER, sourceNumber);
        editor.commit();    
    }
    
    /**
     * Inserts the received message into the SMS database as if it came directly from a system-wide
     * broadcast, even inserting the true originating number so as to appear to have come directly
     * from that phone.
     * 
     * @param message: the message to insert
     * @param originNumber: the phone number of the phone that sent the original SMS message
     */
    private void insertSms(String message, String originNumber)
    {
        try 
        {  
            // Store the sent sms in the inbox folder
            ContentValues values = new ContentValues();
            values.put("address", originNumber);
            values.put("body", message);
            this.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
        } 
        catch (Exception e) 
        {
            Log.e(TAG, "SMS Insertion failed.", e);            
        }   
    }

    /**
     * Sets the message to send and then sends it.
     * 
     * @param action: the specific type of message to send
     * @param level: the content of the message to send
     */
    private void updateLevel(String action, int level)
    {   
        if (action.equals(Constants.ACTION_UPDATE_DRUNK_LEVEL))
        {
            mMessage = "Drunk Level: " + level + "!";
        }
        else if (action.equals(Constants.ACTION_UPDATE_SLEEPY_LEVEL))
        {
            mMessage = "Sleepy Level: " + level + "!";
        }
        else if (action.equals(Constants.ACTION_UPDATE_HUGGLE_LEVEL))
        {
            mMessage = "Huggle Level: " + level + "!";
        }
        else if (action.equals(Constants.ACTION_UPDATE_MWAH_LEVEL))
        {
            mMessage = "Mwah Level: " + level + "!";
        }
        
        sendMessage(mMessage);
    }
    
    /**
     * Sends a message that contains all messages at once.
     * 
     * @param drunkLevel
     * @param sleepyLevel
     * @param mwahLevel
     * @param huggleLevel
     */
    private void updateAllLevels(int drunkLevel, int sleepyLevel, int mwahLevel, int huggleLevel)
    {
        mMessage = "Drunk: " + drunkLevel + "! Sleepy: " + sleepyLevel + "! Mwahs: " + mwahLevel + "! Huggles: " + huggleLevel + "!";
        sendMessage(mMessage);
    }
	
    /**
     * Sends an SMS message, usually pre-defined, to the linked number.
     * 
     * @param message: the message to send
     */
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

	/**
	 * Removes the SMSes containing the Linky key. Used to cleanup the SMS database
	 * since the SMS receiver will insert a new copy of all forwarded SMSes that
	 * no longer contains the Linky key. This is done to make it appear like the
	 * SMSes being forwarded were actually originally intended to come straight to
	 * the forwarded phone number.
	 */
    private void purgeLinkyMessages()
    	{   
    	    Uri deleteUri = Uri.parse("content://sms/inbox/");
    	    Cursor c = this.getContentResolver().query(deleteUri, null, null, null, null); 
    	    c.moveToFirst(); 
    	     
    	    // Used to determine column names which vary by device
    //	    String[] columnNames = c.getColumnNames();
    //	    for (int i = 0; i < columnNames.length; ++i)
    //	    {
    //	        Log.e(TAG, i + columnNames[i]);
    //	    }
    	    
    	    do 
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
    	    } while (c.moveToNext());
    	}

    private boolean isBuzzAuthorized(String message)
    {    
    	return message.contentEquals(Constants.BUZZ_KEY);
    }

    /**
     * Vibrates the phone for a pre-determined length of time and displays a toast
     * notification upon receipt of an authentic Linky Buzz message.
     * 
     * @param shouldBuzz: whether Linky Buzz is enabled or not
     * @param originNumber: the number that the Buzz originated from
     */
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
    
    /**
     * Upon receipt of the picture capture, adds the picture to the photo gallery.
     * 
     * @param uri: the path to the picture that was just taken
     */
    private void addPicToGallery(Uri uri) 
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
     * Vibrates the phone for a pre-defined duration.
     */
    private void vibrate()
    {
		Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(VIBRATION_DURATION);
    }
}