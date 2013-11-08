package org.fe.up.joao.busphoneclient;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.fe.up.joao.busphoneclient.helper.ComHelper;
import org.fe.up.joao.busphoneclient.helper.ComService;
import org.fe.up.joao.busphoneclient.helper.JSONHelper;
import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.Ticket;
import org.fe.up.joao.busphoneclient.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This activity includes has the login screen.
 * It'll be skipped if there is already login information
 * on the device (auto login) and launch the Home Activity
 * @author joao
 *
 */
public class MainActivity extends Activity {

	BusPhoneClient bus;
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("mylog", "FODASSE");
		super.onCreate(savedInstanceState);
		bus = (BusPhoneClient) getApplication();
		
		// Check for proper sharePreferences 
		if(!bus.hasLoadedPrefs() || bus.isLoggedOut()) {
			// show login screen
			setContentView(R.layout.activity_main);
		} // If the user has saved prefs then check for expirationDate 
		else if(hasLoginExpired()){
			// If it has, then try to login
			doLogin();
		} else {
			// If everything is cool, then just move on to the other activity
			startHome();
		}
	}
	
	/**
	 * Login button action handler
	 */
	public void loginAction(View v){
		v.setEnabled(false);
		Button createAccbt = (Button) findViewById(R.id.button_register);
		createAccbt.setEnabled(false);
		String email = ((EditText) findViewById(R.id.form_email)).getText().toString();
		String pw = ((EditText) findViewById(R.id.form_pw)).getText().toString();
		
		if (!email.equals("") && !pw.equals("")) {
			
			bus.setEmail(hashMD5(email));
			bus.setPw(hashMD5(pw));
			doLogin();
		} else {
			v.setEnabled(true);
			createAccbt.setEnabled(true);
		}
	}
	
	/**
	 * Register button action handler
	 * @param v
	 */
	public void registerAction(View v) {
		Intent intent = new Intent(this, RegisterActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME); // Clears the Main Activity
		startActivity(intent);
//		finish();
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	private String hashMD5(String str) {
		byte[] bytesOfMessage;
		byte[] hashed = null;
		try {
			
			bytesOfMessage = str.getBytes("UTF-8");
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			hashed = md.digest(bytesOfMessage);
			StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < hashed.length; i++) {
	          sb.append(Integer.toString((hashed[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        return sb.toString();
	   
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Starts a new "service" (not android service) and provides the callback
	 */
	private void doLogin() {
		new ComService(
				"login/" + bus.getEmail() + "/" + bus.getPw(), 
				MainActivity.this, 
				"loginDone", 
				false); // Hide progress bar cuz we will set it manually
		dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.fetching_data));
        dialog.setCancelable(false);
        dialog.show();
	}
	
	public void loginDone(String result) {
		JSONObject json = JSONHelper.string2JSON(result);
		String status = JSONHelper.getValue(json, "status");
		
		if(status.equals("0")) {
			String token = JSONHelper.getValue(json, "token");
			String user_id = JSONHelper.getValue(json, "user_id");
			String expirationDate_string = JSONHelper.getValue(json, "expirationDate");
			Date expirationDate = new Date();
			try {
				expirationDate = new SimpleDateFormat(getString(R.string.time_format), Locale.ENGLISH).parse(expirationDate_string);
			} catch (ParseException e) {
				e.printStackTrace();
				Toast.makeText(this, getString(R.string.loginexception), Toast.LENGTH_LONG).show();
				findViewById(R.id.button_login).setEnabled(true);
			}
			
			bus.setToken(token);
			bus.setUser_id(user_id);
			bus.setExpirationDate(expirationDate);
			bus.setLoggedOut(false);
			
			// Everything is fine so load the user info
			new ComService(
					"users/" + bus.getUserID() + "/t/" + bus.getToken(), 
					MainActivity.this, 
					"getUserInfoDone", 
					false); // A progress dialog is already set at this point
		} else {
			dialog.dismiss();
			Toast.makeText(this, getString(R.string.loginexception), Toast.LENGTH_LONG).show();
			findViewById(R.id.button_login).setEnabled(true);
			findViewById(R.id.button_register).setEnabled(true);
		}
	}
	
	public void getUserInfoDone(String result) {
		dialog.dismiss();
		JSONObject json = JSONHelper.string2JSON(result);
		String status = JSONHelper.getValue(json, "status");
		if(status.equals("0")) {
			String name = JSONHelper.getValue(json, "user", "name");
			bus.setName(name);
			
			// Parse the tickets
			User.parseTickets(json);
			User.updateTicketsDB(this);
			
			Toast.makeText(this, "Bem vindo " + bus.getName(), Toast.LENGTH_LONG).show();
			startHome();
		} else {
			Toast.makeText(getApplicationContext(), "Qualquer coisa correu mal :(", Toast.LENGTH_LONG).show();
		}
	}


	/**
	 * Launches the home activity and finishes this.
	 */
	private void startHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME); // Clears the Main Activity
		startActivity(intent);
		finish();
	}
	
	/**
	 * Is the token expired?
	 * @return
	 */
	private boolean hasLoginExpired() {
		if(new Date().before(bus.getExpirationDate())) {
			// Not expired
			return false;
		} 
		return true;
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// Should we always save no matter what? or save only expiration and token?
//		if(!bus.hasLoadedPrefs()) {
			bus.saveSharedPrefs();
//		}
	}

}
