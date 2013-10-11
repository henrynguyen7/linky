package com.hn.linky.activities;

import com.hn.linky.R;
import com.hn.linky.valueobjects.ISharedPreferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Activity to allow user to change settings including the values for the linked number and
 * SMS forwarding number. 
 * 
 * @author henry@dxconcept.com
 *
 */
//TODO: Implement SettingsActivity as Preference Fragment per Android 3.0+
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener, ISharedPreferences
{	
    private EditTextPreference forwardingNumberPreference;
	private EditTextPreference linkedNumberPreference;
	private String forwardingNumber;
	private String linkedNumber;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);      
        
        addPreferencesFromResource(R.xml.settings_preferences);
        getPreferences();
        
        forwardingNumberPreference = (EditTextPreference) findPreference(SHARED_PREF_FORWARDING_NUMBER);
        forwardingNumberPreference.setSummary(forwardingNumber);
        
        linkedNumberPreference = (EditTextPreference) findPreference(SHARED_PREF_LINKED_NUMBER);
        linkedNumberPreference.setSummary(linkedNumber);
    }
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
	{
	    if (key.equals(SHARED_PREF_FORWARDING_NUMBER))
        {
            forwardingNumber = sharedPreferences.getString(key, null);
            forwardingNumberPreference.setSummary(forwardingNumber);
        }
	    else if (key.equals(SHARED_PREF_LINKED_NUMBER))
		{
			linkedNumber = sharedPreferences.getString(key, null);
			linkedNumberPreference.setSummary(linkedNumber);
		}
	}

	@Override
	protected void onResume() 
	{
	    super.onResume();
	    
	    // Set up a listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onPause() 
	{
	    super.onPause();
	    
	    // Unregister the listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
	
	/**
	 * Retrieves the settings from Shared Preferences and loads them onto the screen's input fields
	 */
    private void getPreferences() 
    {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	forwardingNumber = preferences.getString(SHARED_PREF_FORWARDING_NUMBER, null);
    	linkedNumber = preferences.getString(SHARED_PREF_LINKED_NUMBER, null);
    }
}