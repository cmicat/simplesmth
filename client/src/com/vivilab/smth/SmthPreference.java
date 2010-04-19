package com.vivilab.smth;

import com.vivilab.smth.helper.SmthHelper;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

public class SmthPreference extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	private ListPreference mBoardPreference;
	private EditTextPreference mApiUrl;
	private EditTextPreference mTextSize;
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the XML preferences file
	        addPreferencesFromResource(R.xml.preference);

	        // Get a reference to the preferences
	        mBoardPreference = (ListPreference)getPreferenceScreen().findPreference("BoardType");
	        mApiUrl = (EditTextPreference)getPreferenceScreen().findPreference("ServerUrl");
	        mTextSize = (EditTextPreference)getPreferenceScreen().findPreference("TextSize");
//	        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    }

	 
	 protected void onPause() {
	        super.onPause();
	        // Unregister the listener whenever a key changes            
	        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    
	 }
	 
	 protected void onResume() {
	        super.onResume();
	        //use digit only
	        EditText sizeEditText = (EditText)mTextSize.getEditText();
	        sizeEditText.setKeyListener(DigitsKeyListener.getInstance(false,true));
	        // Setup the initial values
	        mTextSize.setSummary(getPreferenceScreen().getSharedPreferences().getString("TextSize", "15")+"sp");
	        mApiUrl.setSummary(getPreferenceScreen().getSharedPreferences().getString("ServerUrl", "http://smth.vivilab.info/"));
	        int bvalue = Integer.parseInt(getPreferenceScreen().getSharedPreferences().getString("BoardType", "1"));
	        if(bvalue == 1)
	        {
	        	mBoardPreference.setSummary(getString(R.string.info_board_1));
	        }
	        else
	        {
	        	mBoardPreference.setSummary(getString(R.string.info_board_2));
	        }

	        // Set up a listener whenever a key changes            
	        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	    }	 
	 
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if(key.equals("BoardType"))
		{
	        int bvalue = Integer.parseInt(sharedPreferences.getString("BoardType", "1"));
	        if(bvalue == 1)
	        {
	        	mBoardPreference.setSummary(getString(R.string.info_board_1));
	        }
	        else
	        {
	        	mBoardPreference.setSummary(getString(R.string.info_board_2));
	        }
			
		}
		else if(key.equals("TextSize"))
		{
			mTextSize.setSummary(sharedPreferences.getString("TextSize", "15")+"sp");
		}
		else if(key.equals("ServerUrl"))
		{
			SmthHelper.setApiHost(sharedPreferences.getString("ServerUrl", "http://smth.vivilab.info/"));
			mApiUrl.setSummary(sharedPreferences.getString("ServerUrl", "http://smth.vivilab.info/"));
		}
		
	}

}
