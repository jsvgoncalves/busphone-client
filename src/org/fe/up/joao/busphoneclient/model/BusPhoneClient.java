package org.fe.up.joao.busphoneclient.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.fe.up.joao.busphoneclient.R;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class BusPhoneClient extends Application {
	private static BusPhoneClient instance;
	private static boolean hasLogin = false;
	
	private String user_id;
	private String token;
	private static String name;
	private String email;
	private String pw;
	private Date expirationDate;
	
	private String PREFS_NAME = "login";
	private boolean loadedPrefs = false;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		
		this.loadedPrefs = checkSharedPrefs();
	}
	
	public static Context context() {
		return instance.getApplicationContext();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public static boolean hasLogin() {
		return hasLogin;
	}

	public void setHasLogin(boolean hasLogin) {
		this.hasLogin = hasLogin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public boolean hasLoadedPrefs() {
		return loadedPrefs;
	}

	public void setLoadedPrefs(boolean loadedPrefs) {
		this.loadedPrefs = loadedPrefs;
	}
	
	/**
	 * Loads the sharedPreferences and returns if it was successfull
	 * @return boolean
	 */
	private boolean checkSharedPrefs() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		
		String email = settings.getString("email", "notset");
		String pw = settings.getString("pw", "notset");
		String token = settings.getString("token", "notset");
		String date = settings.getString("expirationDate", "1999-12-12 00:00:00");
//		date = "1999-12-12 00:00:01";
		
		try {
			if( email.equals("notset") || pw.equals("notset") || 
				token.equals("notset") || date.equals("1999-12-12 00:00:00") ) {
				throw new ParseException("Parse exception", 0);
			}
			Date sharedDate = new SimpleDateFormat(getString(R.string.time_format), Locale.ENGLISH).parse(date);
			
			setEmail(email);
			setPw(pw);
			setExpirationDate(sharedDate); 
			setToken(token);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * Saves the shared preferences
	 */
	public void saveSharedPrefs() { 
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString("email", email);
		editor.putString("pw", pw);
		editor.putString("token", token);
		SimpleDateFormat dFormat = new SimpleDateFormat(getString(R.string.time_format));
		editor.putString("expirationDate", dFormat.format(expirationDate).toString());
//		editor.putString("expirationDate", "1998-1-1 01:01:01");
		
		// Commit the edits!
		editor.commit();
	}

}