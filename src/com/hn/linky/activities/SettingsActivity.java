package com.hn.linky.activities;

import com.hn.linky.R;
import com.hn.linky.utilities.Constants;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener 
{	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);
        
        Preference linkedNumber = (Preference) findPreference("linkedNumberPref");
        //linkedNumber.onDialogClosed(true);
        
    }
	
	public void onStart(Bundle savedInstanceState) 
    {	
		
        
    }

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
	{
		
		
	}
	
	
    String ListPreference;
    String linkedNumberPreference;
    
    String customPref;

    private void getPrefs() 
    {
            // Get the xml/preferences.xml preferences
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            linkedNumberPreference = preferences.getString(Constants.SHARED_PREF_LINKED_NUMBER, null);
           
            
            // Get the custom preference
            //SharedPreferences mySharedPreferences = getSharedPreferences("myCustomSharedPrefs", Activity.MODE_PRIVATE);
            //customPref = mySharedPreferences.getString("myCusomPref", "");
    }
}