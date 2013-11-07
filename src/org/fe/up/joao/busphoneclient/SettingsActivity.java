package org.fe.up.joao.busphoneclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends Activity {
	
	public final static String KEY_PREF_DATAUSAGE_MOBILEDATA = "pref_datausage_mobiledata";
	public final static String KEY_PREF_ALARM_ENABLED = "pref_alarm_enabled";
	public final static String KEY_PREF_ALARM_SOUND = "pref_alarm_sound";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			this.finish();
			return true;		
		}
		return super.onOptionsItemSelected(item);
	}
	
	

}
