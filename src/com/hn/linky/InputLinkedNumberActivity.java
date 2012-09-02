package com.hn.linky;

import com.hn.linky.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InputLinkedNumberActivity extends Activity
{	
    public void onCreate(Bundle savedInstanceState)
    {    	
    	if (isLoggedIn())
    	{	
    		logIn();
    	}
    	else
    	{
        	setContentView(R.layout.passwordform);
        	
        	final EditText linkedNumberEditText = (EditText) findViewById(R.id.passwordText);
        	
        	Button submitButton = (Button) findViewById(R.id.unlockButton);
        	submitButton.setOnClickListener(new OnClickListener()
        	{  
        	    public void onClick(View v) 
        	    {  
        	    	saveLoggedInStatus(true);
        	    	String phoneNumber = linkedNumberEditText.getText().toString();        	    	
        	    	saveLinkedNumber(phoneNumber);
        	    	logIn();
        	    }  
        	});
    	}
    	
    	super.onCreate(savedInstanceState);
    }
        
    private boolean isLoggedIn()
    {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	return sharedPreferences.getBoolean(Constants.SHARED_PREF_IS_LOGGED_IN, false);
    }
    
    private void logIn()
    {
    	Intent intent = new Intent(InputLinkedNumberActivity.this, MainActivity.class);
		startActivity(intent);
		finish();	
    }
    
    private void saveLoggedInStatus(boolean status)
    {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(Constants.SHARED_PREF_IS_LOGGED_IN, status);
		editor.commit();		
    }
    
    private void saveLinkedNumber(String phoneNumber)
    {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Constants.SHARED_PREF_LINKED_NUMBER, phoneNumber);
		editor.commit();
    }
}