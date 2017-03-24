package com.intrahealth.appanm;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.intrahealth.msakhi.appanm.R;

public class EditPreference extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.app_settings);
	}	
}
