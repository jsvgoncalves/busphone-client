package org.fe.up.joao.busphoneclient;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bus = (BusPhoneClient) getApplicationContext();
		
		// Check for proper sharePreferences 
		if(!bus.hasLoadedPrefs()) {
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
		String email = ((EditText) findViewById(R.id.form_email)).getText().toString();
		String pw = ((EditText) findViewById(R.id.form_pw)).getText().toString();
		
		if (email != "" && pw != "") {
			bus.setEmail(email);
			bus.setPw(pw);
			doLogin();
		}
	}

	/**
	 * Starts a new "service" (not android service) and provides the callback
	 */
	private void doLogin() {
		new ComService(
				"login/" + bus.getEmail() + "/" + bus.getPw(), 
				MainActivity.this, 
				"loginDone");
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
				Toast.makeText(getApplicationContext(), getString(R.string.loginexception), Toast.LENGTH_LONG).show();
			}
			
			bus.setToken(token);
			bus.setUser_id(user_id);
			bus.setExpirationDate(expirationDate);

			// Everything is fine so load the user info
			new ComService(
					"users/" + bus.getUser_id() + "/t/" + bus.getToken(), 
					MainActivity.this, 
					"getUserInfoDone");
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.loginexception), Toast.LENGTH_LONG).show();
		}
	}
	
	public void getUserInfoDone(String result) {
		JSONObject json = JSONHelper.string2JSON(result);
		String status = JSONHelper.getValue(json, "status");
		if(status.equals("0")) {
			String name = JSONHelper.getValue(json, "user", "name");
			bus.setName(name);
			
			// Parse the tickets
			parseTickets(json);
			
			Toast.makeText(getApplicationContext(), "Bem vindo " + bus.getName(), Toast.LENGTH_LONG).show();
			startHome();
		} else {
			Toast.makeText(getApplicationContext(), "Qualquer coisa correu mal :(", Toast.LENGTH_LONG).show();
		}
	}

	private void parseTickets(JSONObject json) {
		ArrayList<String> t1 = JSONHelper.getArray(json, "user", "tickets", "t1");
		ArrayList<String> t2 = JSONHelper.getArray(json, "user", "tickets", "t2");
		ArrayList<String> t3 = JSONHelper.getArray(json, "user", "tickets", "t3");
		
		for (String ticketStr : t1) {
			json = JSONHelper.string2JSON(ticketStr);
			try {
				Ticket t = new Ticket(json.getString("id"),
						json.getString("ticket_type"),
						json.getString("uuid"),
						json.getString("created_at"),
						json.getString("updated_at"));
				User.ticketsT1.add(t);
			} catch (JSONException e) {
				System.err.println(e.toString());
				System.err.println("Invalid JSON while retrieving tickets!");
			}
		}
		
		for (String ticketStr : t2) {
			json = JSONHelper.string2JSON(ticketStr);
			try {
				Ticket t = new Ticket(json.getString("id"),
						json.getString("ticket_type"),
						json.getString("uuid"),
						json.getString("created_at"),
						json.getString("updated_at"));
				User.ticketsT2.add(t);
			} catch (JSONException e) {
				System.err.println(e.toString());
				System.err.println("Invalid JSON while retrieving tickets!");
			}
		}
		
		for (String ticketStr : t3) {
			json = JSONHelper.string2JSON(ticketStr);
			try {
				Ticket t = new Ticket(json.getString("id"),
						json.getString("ticket_type"),
						json.getString("uuid"),
						json.getString("created_at"),
						json.getString("updated_at"));
				User.ticketsT3.add(t);
			} catch (JSONException e) {
				System.err.println(e.toString());
				System.err.println("Invalid JSON while retrieving tickets!");
			}
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
