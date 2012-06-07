package com.hn.linky;

import com.hn.linky.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PreferenceSelectionActivity extends PreferenceActivity
{
	private Context mContext;
	private static final String PASSWORD = "poopoohead";
	
    public void onCreate(Bundle savedInstanceState)
    {
    
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