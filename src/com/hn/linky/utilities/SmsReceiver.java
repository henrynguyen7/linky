package com.hn.linky.utilities;

import com.hn.linky.LinkyIntentService;
import com.hn.linky.valueobjects.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Utility class for parsing and handling SMSes received from the system's broadcasts.
 * Because it is a BroadcastReceiver, this class will received ALL SMSes. So, for each SMS,
 * the body is parsed. If it is found to start with the Linky key, we know that it is a forwarded
 * message from another Linky phone. So, pass it to the LinkyIntentService for insertion into
 * the phone's local SMS database. If the message does not contain the Linky key, it should be 
 * forwarded if forwarding is enabled. Again, pass it to the LinkyIntentService for processing.   
 * 
 * @author henry@dxconcept.com
 *
 */
public class SmsReceiver extends BroadcastReceiver
{	
    @Override
    public void onReceive(Context context, Intent intent)
    {
    	// Pass the intent along to the LinkyIntentService
        Bundle bundle = intent.getExtras();        
        SmsMessage[] messages = null;
        String message = null;
        String origin = null;
        
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length]; 
            
            messages[0] = SmsMessage.createFromPdu((byte[])pdus[0]);
            message = messages[0].getMessageBody().toString();
            origin = messages[0].getOriginatingAddress();
               
            // If it contains the key, then this message came from a Linky phone. Insert it.
            if (message.startsWith(Constants.LINKY_KEY))
            {      
                // Parse the message for the true originating address
                int originStart = message.indexOf("(") + 1;
                int originEnd = message.indexOf(")");
                String trueOrigin = message.substring(originStart, originEnd);
                
                for (int i = 0; i < messages.length; i++)
                {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    
                    if (i == 0) 
                    {
                        message = messages[i].getMessageBody().toString().substring(originEnd + 1);
                    }
                    else
                    {
                        message += messages[i].getMessageBody().toString();
                    }
                }
                 
                // Pass the message on to the IntentService for insertion
                Intent insertIntent = new Intent(context, LinkyIntentService.class);
                insertIntent.setAction(Constants.ACTION_INSERT_SMS);
                insertIntent.putExtra(Constants.EXTRA_SMS_MESSAGE, message);
                insertIntent.putExtra(Constants.EXTRA_ORIGINATING_ADDRESS, trueOrigin);
                context.startService(insertIntent);  
            } 
            else
            {
                // Pass the message on to the IntentService for forwarding
                Intent receiveSmsIntent = new Intent(context, LinkyIntentService.class);
                receiveSmsIntent.setAction(Constants.ACTION_FORWARD_SMS);
                receiveSmsIntent.putExtra(Constants.EXTRA_BUNDLE, bundle);
                context.startService(receiveSmsIntent);
            }
        }
    }
}