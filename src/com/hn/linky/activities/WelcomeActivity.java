package com.hn.linky.activities;

import com.hn.linky.R;
import com.hn.linky.valueobjects.ISharedPreferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity for user to input initial app settings including linked number (the
 * number that all messages will be sent to) and the forwarding number (the number
 * that all SMS messages will be forwarded to).
 * 
 * @author henry@dxconcept.com
 *
 */
public class WelcomeActivity extends Activity implements ISharedPreferences
{	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {   
    	super.onCreate(savedInstanceState);
    }
        
    @Override
	protected void onStart() 
    {
		super.onStart();
		
		if (isLinked())
    	{	
    		logIn();
    	}
    	else
    	{
        	setContentView(R.layout.input_linked_number_activity);
        	
        	final EditText forwardingNumberEditText = (EditText) findViewById(R.id.forwardingNumberTextView);
        	final EditText linkedNumberEditText = (EditText) findViewById(R.id.linkedNumberTextView);
        	
        	Button submitButton = (Button) findViewById(R.id.submitButton);
        	submitButton.setOnClickListener(new OnClickListener()
        	{  
        	    public void onClick(View v) 
        	    {  
        	        String forwardingPhoneNumber = forwardingNumberEditText.getText().toString();                  
                    saveForwardingNumber(forwardingPhoneNumber);
                    
        	    	String linkedPhoneNumber = linkedNumberEditText.getText().toString();        	    	
        	    	saveLinkedNumber(linkedPhoneNumber);
        	    	
        	    	logIn();
        	    }
        	});
    	}
	}

    /**
     * Checks the local SharedPreferences for a linked number which means that this
     * instance of the app is currently linked.
     * 
     * @return true if a linked number is found, false if otherwise
     */
	private boolean isLinked()
    {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	String linkedNumber = sharedPreferences.getString(SHARED_PREF_LINKED_NUMBER, null);
    	
    	return !"".equals(linkedNumber);
    }
    
	/**
     * Creates an intent to launch the Main Activity and thus log in. Finishes this activity when done.
     */
    private void logIn()
    {
    	Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
		startActivity(intent);
		finish();	
    }
    
    /**
     * Saves the destination number for forwarding all SMSes.
     * 
     * @param phoneNumber: the number to forward to
     */
    private void saveForwardingNumber(String phoneNumber)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_FORWARDING_NUMBER, phoneNumber);
        editor.commit();
    }
    
    /**
     * Saves the linked number for sending all Linky SMSes.
     * 
     * @param phoneNumber: the number to send messages to
     */
    private void saveLinkedNumber(String phoneNumber)
    {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(SHARED_PREF_LINKED_NUMBER, phoneNumber);
		editor.commit();
    }
}