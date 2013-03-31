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
    @Override
    public void onReceive(Context context, Intent intent)
    {
    	//Pass the intent along to the LinkyIntentService
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
                                  
            if (message.startsWith(Constants.LINKY_KEY))
            { 
                abortBroadcast();
                    
                //Parse the Linky-forwarded message for the true originating address
                int originStart = message.indexOf("(") + 1;
                int originEnd = message.indexOf(")");
                String trueOrigin = message.substring(originStart, originEnd);
                
                for (int i = 0; i < messages.length; i++)
                {
                    if (i == 0) 
                    {
                        messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        message = messages[i].getMessageBody().toString().substring(originEnd + 1);
                    }
                    else
                    {
                        messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        message += messages[i].getMessageBody().toString();
                    }
                }
                    
                Intent insertIntent = new Intent(context, LinkyIntentService.class);
                insertIntent.setAction(Constants.ACTION_INSERT_SMS);
                insertIntent.putExtra(Constants.EXTRA_SMS_MESSAGE, message);
                insertIntent.putExtra(Constants.EXTRA_ORIGINATING_ADDRESS, trueOrigin);
                context.startService(insertIntent);  
            } 
            else
            {
                Intent receiveSmsIntent = new Intent(context, LinkyIntentService.class);
                receiveSmsIntent.setAction(Constants.ACTION_FORWARD_SMS);
                receiveSmsIntent.putExtra(Constants.EXTRA_BUNDLE, bundle);
                context.startService(receiveSmsIntent);
            }
        }
    }
}