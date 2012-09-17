package com.hn.linky.activities;

import com.hn.linky.R;
import com.hn.linky.valueobjects.Constants;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener 
{	
	private EditTextPreference linkedNumberPreference;
	private String linkedNumber;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);
        getPrefs();
                
        //linkedNumberPreference = (EditTextPreference) findPreference(Constants.SHARED_PREF_LINKED_NUMBER);
        //TODO Implement SettingsActivity as Preference Fragment per Android 3.0+
        String sharedPrefLinkedNumber = getString(R.string.SHARED_PREF_LINKED_NUMBER);
        linkedNumberPreference = (EditTextPreference) findPreference(sharedPrefLinkedNumber);
        //linkedNumberPreference.setText(linkedNumber);
        linkedNumberPreference.setSummary(linkedNumber);
    }
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
	{
		if (key.equals(Constants.SHARED_PREF_LINKED_NUMBER))
		{
			linkedNumber = sharedPreferences.getString(key, null);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(Constants.SHARED_PREF_LINKED_NUMBER, linkedNumber);
			editor.commit();
			
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
	
    private void getPrefs() 
    {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	linkedNumber = preferences.getString(Constants.SHARED_PREF_LINKED_NUMBER, null);
    }
}