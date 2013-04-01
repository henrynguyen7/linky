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

public class InputLinkedNumberActivity extends Activity implements ISharedPreferences
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

	private boolean isLinked()
    {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	if (sharedPreferences.getString(SHARED_PREF_LINKED_NUMBER, null) == null)
    	{
    		return false;
    	}
    	else 
    	{
    		return true;
    	}
    }
    
    private void logIn()
    {
    	Intent intent = new Intent(InputLinkedNumberActivity.this, MainActivity.class);
		startActivity(intent);
		finish();	
    }
    
    private void saveForwardingNumber(String phoneNumber)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_FORWARDING_NUMBER, phoneNumber);
        editor.commit();
    }
    
    private void saveLinkedNumber(String phoneNumber)
    {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(SHARED_PREF_LINKED_NUMBER, phoneNumber);
		editor.commit();
    }
}