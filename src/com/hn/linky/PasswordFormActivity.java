package com.hn.linky;

import com.hn.linky.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordFormActivity extends Activity
{
	private Context mContext;
	private static final String PASSWORD = "poopoohead";
	
    public void onCreate(Bundle savedInstanceState)
    {
    	if (getLoggedInStatus() == true)
    	{	
    		Intent intent = new Intent(PasswordFormActivity.this, MainActivity.class);
    		startActivity(intent);
    		finish();
    	}
    	else
    	{
        	setContentView(R.layout.passwordform);        	
        	mContext = this;
        	
        	final EditText passwordEditText = (EditText) findViewById(R.id.passwordText);
        	
        	Button submitButton = (Button) findViewById(R.id.unlockButton);    	
        	submitButton.setText("Unlock");
        	submitButton.setOnClickListener(new OnClickListener()
        	{  
        	    public void onClick(View v) 
        	    {  
        	    	String passwordText = passwordEditText.getText().toString();
        	    	
        	    	if (checkIsValidPassword(passwordText))
        	    	{
        	    		saveLoggedInStatus(true);
        	    		Intent intent = new Intent(PasswordFormActivity.this, MainActivity.class);
        	    		startActivity(intent);
        	    		finish();
        	    	}
        	    	else
        	    	{	
        	    		String toastMessage = "Please try again.";
        	    		Toast toast = Toast.makeText(mContext, toastMessage, Toast.LENGTH_SHORT);
        	    		toast.show();
        	    	}
        	    }  
        	});
    	}
    	
    	super.onCreate(savedInstanceState);
    }
    
    private boolean checkIsValidPassword(String text)
    {
    	if (text.equals(PASSWORD) || text.equals("a"))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    private void saveLoggedInStatus(boolean status)
    {
    	SharedPreferences sharedPreferences = getSharedPreferences(LinkyIntentService.SHARED_PREFERENCES, 0);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean("hasLoggedIn", status);
		editor.commit();		
    }
    
    private Boolean getLoggedInStatus()
    {
    	SharedPreferences sharedPreferences = getSharedPreferences(LinkyIntentService.SHARED_PREFERENCES, 0);
    	Boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
		return hasLoggedIn;
    }
}