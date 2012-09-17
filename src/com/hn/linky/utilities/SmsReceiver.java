package com.hn.linky.utilities;

import com.hn.linky.LinkyIntentService;
import com.hn.linky.valueobjects.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver
{
	private final String stringToCompare = "LINKY BUZZ!";
	
	@Override
	public void onReceive(Context context, Intent intent) 
    {	
		//Get the SMS message passed in
        Bundle bundle = intent.getExtras();        
        SmsMessage[] messages = null;
        String message = null;
        String origin = null;
        
        if (bundle != null)
        {
            //Retrieve the SMS message received
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];     
            
            for (int i = 0; i < messages.length; i++)
            {
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                
                message = messages[i].getMessageBody().toString();                
                
                if (message.contains(stringToCompare))
                {
                	abortBroadcast();
                	
                	origin = messages[i].getOriginatingAddress();
                	
                	Intent authenticateIntent = new Intent(context, LinkyIntentService.class);
                	authenticateIntent.setAction(Constants.ACTION_AUTHENTICATE_BUZZ);
                	authenticateIntent.putExtra(Constants.EXTRA_SMS_MESSAGE, message);
                	authenticateIntent.putExtra(Constants.EXTRA_ORIGINATING_ADDRESS, origin);
                	context.startService(authenticateIntent);
                }
                
                // str += "SMS from " + messages[i].getOriginatingAddress();
                // str += messages[i].getMessageBody().toString();
                        
            }
        }
    }
}