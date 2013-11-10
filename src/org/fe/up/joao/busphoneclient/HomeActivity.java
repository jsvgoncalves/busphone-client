package org.fe.up.joao.busphoneclient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.fe.up.joao.busphoneclient.helper.ComService;
import org.fe.up.joao.busphoneclient.helper.JSONHelper;
import org.fe.up.joao.busphoneclient.helper.QRCodeHelper;
import org.fe.up.joao.busphoneclient.helper.TicketsDataSource;
import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.Ticket;
import org.fe.up.joao.busphoneclient.model.User;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * This activity is display upon login.
 * It displays how many tickets of each type the user
 * has and provides access to history, ticket buying,
 * and the information about last validation (for quick
 * access if the inspector shows up).
 * 
 * @author joao
 *
 */
public class HomeActivity extends Activity {
	
	BusPhoneClient bus;
	private boolean buttonPressed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		Log.v("mylog", "network: " + ComHelper.isOnline(this)); // TODO check if the user has net.
		super.onCreate(savedInstanceState);
		bus = (BusPhoneClient) getApplicationContext();
		setContentView(R.layout.activity_home);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

		// If the user set the app to auto-update.
		if(sharedPref.getBoolean(SettingsActivity.KEY_PREF_DATAUSAGE_AUTOUPDATE,  true)){
			// TODO Check if the user pref of mobile usage is set
			// and if the client is connect via mobile data
			//if(sharedPref.getBoolean(SettingsActivity.KEY_PREF_DATAUSAGE_MOBILEDATA,  true) && on3g()) {
			refreshData();
		}
		
		User.fetchTicketsFromDB(this);
		// updateUsernameDisplay(); //  I removed the name from header
		updateTicketsDisplay();

		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_logout:
	            logout();
	        	Log.v("mylog", "logging out");
	            return true;
	        case R.id.action_refresh:
	            refreshData();
	        	Log.v("mylog", "refreshing");
	            return true;
	        case R.id.action_settings:
	        	showSettings();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void showSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	private void refreshData() {
		// Everything is fine so load the user info
		new ComService(
				"users/" + bus.getUserID() + "/t/" + bus.getToken(), 
				HomeActivity.this, 
				"getUserInfoDone", 
				true);
		
	}
	
	public void getUserInfoDone(String result) {
		JSONObject json = JSONHelper.string2JSON(result);
		String status = JSONHelper.getValue(json, "status");
		if(status.equals("0")) {
			String name = JSONHelper.getValue(json, "user", "name");
			bus.setName(name);
			
			// Parse the tickets
			User.parseTickets(json);
			updateTicketsDisplay();
			User.updateTicketsDB(this);
			Toast.makeText(getApplicationContext(), "Atualizado", Toast.LENGTH_LONG).show();
		} else if(status.equals("2") || status.equals("1")) {
			bus.setExpirationDate(new Date());
			new ComService(
					"login/" + bus.getEmail() + "/" + bus.getPw(), 
					HomeActivity.this, 
					"loginDone", 
					true);
		}
		else {
			Toast.makeText(getApplicationContext(), "Falha ao atualizar", Toast.LENGTH_LONG).show();
		}
	}
	
	/** 
	 * Tries to relogin the user.
	 * @param result
	 */
	public void loginDone(String result) {
		JSONObject json = JSONHelper.string2JSON(result);
		String status = JSONHelper.getValue(json, "status");
		
		if(status.equals("0")) {
			String token = JSONHelper.getValue(json, "token");
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
			bus.setExpirationDate(expirationDate);
			bus.setLoggedOut(false);
			bus.saveSharedPrefs();
			
		} else {
			bus.setLoggedOut(true);
			Toast.makeText(this, getString(R.string.loginexception), Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME); // Clears the Main Activity
			startActivity(intent);
			finish();
		}
	}

	/**
	 * Handler for ticket button T1 
	 */
	public void buttonClickedT1(View view) {
		if(!buttonPressed) {
			buttonPressed = true;
			startUseTicket(User.ticketsT1, "T1");
		}
	}
	
	/**
	 * Handler for ticket button T2
	 */ 
	public void buttonClickedT2(View view) {
		if(!buttonPressed) {
			buttonPressed = true;
			startUseTicket(User.ticketsT2, "T2");
		}
	}
	
	/**
	 * Handler for ticket button T3
	 */
	public void buttonClickedT3(View view) {
		if(!buttonPressed) {
			buttonPressed = true;
			startUseTicket(User.ticketsT3, "T3");
		}
	}

	/**
	 *
	 */
	private void startUseTicket(ArrayList<Ticket> tickets, String type) {
		if(tickets.isEmpty()) {
			Toast.makeText(this, getString(R.string.no_tickets_for_you), Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent(this, UseTicketActivity.class);
			intent.putExtra("ticket_type", type);
			startActivity(intent);
		}
	}
	
	
	/**
	 * Handler for the buy tickets button
	 */
	public void buyTicketsAction(View v) {
		if(!buttonPressed) {
			buttonPressed = true;
			Intent intent = new Intent(this, BuyTicketsActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * Handler for the history button
	 */
	public void historyAction(View v) {
		if(!buttonPressed) {
			buttonPressed = true;
			Intent intent = new Intent(this, HistoryActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * Updates the number of tickets available in each ticket button.
	 * The data is fetched from the User.tickets and not from the API.
	 */
	public void updateTicketsDisplay() {
		Button buttonT1 = (Button) findViewById(R.id.t1button);
		Button buttonT2 = (Button) findViewById(R.id.t2button);
		Button buttonT3 = (Button) findViewById(R.id.t3button);
		
		buttonT1.setText("x" + User.ticketsT1.size());
		if (User.ticketsT1.size() < 1) {
			buttonT1.setEnabled(false);
		} else {
			buttonT1.setEnabled(true);
		}
		buttonT2.setText("x" + User.ticketsT2.size());
		if (User.ticketsT2.size() < 1) {
			buttonT2.setEnabled(false);
		} else {
			buttonT2.setEnabled(true);
		}
		buttonT3.setText("x" + User.ticketsT3.size());
		if (User.ticketsT3.size() < 1) {
			buttonT3.setEnabled(false);
		} else {
			buttonT3.setEnabled(true);
		}
		
		// If there is no ticket history then don't show QR code
		if (User.ticketsHistory.isEmpty()) {
			((LinearLayout) findViewById(R.id.last_used_ticket_layout)).setVisibility(View.GONE);
		} else {// Updates QRCode and last ticket info
			((LinearLayout) findViewById(R.id.last_used_ticket_layout)).setVisibility(View.VISIBLE);
			String qrData = User.getID() + ";" + User.ticketsHistory.get(0).uuid;
			ImageView imageView = (ImageView) findViewById(R.id.qrCode);
			QRCodeHelper.updateQRCode(qrData, imageView);
			
			String ticketText = "T" + User.ticketsHistory.get(0).ticket_type + " - " + User.ticketsHistory.get(0).dateUsed;
			((TextView) findViewById(R.id.lastTripBus)).setText(ticketText);
		}
	}

	public void logout(){
		bus.setLoggedOut(true);
		bus.saveSharedPrefs();
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onStop() {
		super.onStop();
//		Log.v("mylog", "stopping home");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		TicketsDataSource db = new TicketsDataSource(getApplicationContext());
		for (Ticket ticket : User.ticketsT1) {
			db.insertTicket(ticket);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.v("mylog", "resuming");
		updateTicketsDisplay();
		buttonPressed = false;
	}
}
