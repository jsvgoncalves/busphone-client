package org.fe.up.joao.busphoneclient;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.fe.up.joao.busphoneclient.model.BusPhoneClient;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

public class UserProfileFragment extends PreferenceFragment {
	BusPhoneClient bus;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bus = (BusPhoneClient) getActivity().getApplication();
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.userprofile);
		EditTextPreference profile_name = (EditTextPreference) findPreference("pref_profile_name");
		EditTextPreference profile_email = (EditTextPreference) findPreference("pref_profile_email");
		profile_name.setSummary(bus.getName());
		profile_email.setSummary(bus.getRealEmail());
		
		
		EditTextPreference profile_expires = (EditTextPreference) findPreference("pref_profile_expires");
		SimpleDateFormat dFormat = new SimpleDateFormat(getString(R.string.time_format), Locale.getDefault());
		profile_expires.setSummary(dFormat.format(bus.getExpirationDate()).toString());
		EditTextPreference profile_last_update = (EditTextPreference) findPreference("pref_profile_lastUpdate");
		profile_last_update.setSummary(bus.getLastUpdate());
	}
}
