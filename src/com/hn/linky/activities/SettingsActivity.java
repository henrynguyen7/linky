package com.hn.linky.activities;

import com.hn.linky.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener 
{
	public static final String KEY_PREF_SYNC_CONN = "pref_syncConnectionType";
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);
    }

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
	{
		
		
	}
}