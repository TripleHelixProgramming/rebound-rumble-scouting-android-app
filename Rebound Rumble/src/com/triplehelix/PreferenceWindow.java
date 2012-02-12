package com.triplehelix;

import java.io.File;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class PreferenceWindow extends PreferenceActivity {
		
    SharedPreferences getPreference;
		
	public void onCreate(Bundle savedInstanceState)
	{
	    	//get preference data
	    	getPreference = PreferenceManager.getDefaultSharedPreferences(this);
	    	
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.preference);
	        
	        EditTextPreference device = (EditTextPreference) findPreference("dev");
	        device.setText(getPreference.getString("dev", "device"));
	        
	        
	 }
	    
	    
	    

}
