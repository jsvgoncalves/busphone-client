package org.fe.up.joao.busphoneclient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.fe.up.joao.busphoneclient.helper.ComHelper;
import org.fe.up.joao.busphoneclient.helper.ComTasks;
import org.fe.up.joao.busphoneclient.helper.JSONHelper;
import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.Ticket;
import org.fe.up.joao.busphoneclient.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

/**
 * This activity includes has the login screen.
 * It'll be skipped if there is already login information
 * on the device (auto login) and launch the Home Activity
 * @author joao
 *
 */
public class MainActivity extends Activity {

	private String PREFS_NAME = "login";
	BusPhoneClient bus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bus = (BusPhoneClient) getApplicationContext();
		bus.saveSharedPrefs();
		// Check for proper sharePreferences 
		if(!bus.hasLoadedPrefs()) {
			// show login screen
			setContentView(R.layout.activity_main);
			
		} // If the user has saved prefs then check for expirationDate 
		else if(hasLoginExpired()){
			// If it has, then try to login
//			ComTasks.doLogin();
//			Log.v("mylog", "doing login");
			doLogin();
		} else {
			// If everything is cool, then just move on to the other activity
			startHome();
		}
	}

	private void doLogin() {
		(new LoginTask()).doIt();
		
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
		
//		if(!bus.hasLoadedPrefs()) {
			bus.saveSharedPrefs();
//		}
	}

	/**
	 * Retrieves the user profile
	 * and its tickets
	 */
	private class LoginTask extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute(){}
		
		public void doIt() {
			String url = ComHelper.serverURL + "login/" + bus.getEmail() + "/" + bus.getPw();
			this.execute("get", url);
		}

		@Override
		protected String doInBackground(String... params) {
		    return ComHelper.httpGet(params);
		}
		
		@Override
		protected void onPostExecute (String result){
			System.out.println(result);
			Log.v("mylog", "result " + result);
			JSONObject json = JSONHelper.string2JSON(result);
//			
//			// Put the user's name in place.
//			String name = JSONHelper.getValue(json, "user", "name");
////			MainActivity.this.setUserName(name);
//			User.resetTickets();			
//			ArrayList<String> tickets = JSONHelper.getArray(json, "user", "tickets");
//			
//			for (String ticketStr : tickets) {
//				json = JSONHelper.string2JSON(ticketStr);
//				try {
//					Ticket t = new Ticket(json.getString("id"),
//							json.getString("ticket_type"),
//							json.getString("uuid"),
//							json.getString("created_at"),
//							json.getString("updated_at"));
//					switch (t.ticket_type) {
//					case 1:
//						User.ticketsT1.add(t);
//						break;
//					case 2:
//						User.ticketsT2.add(t);
//						break;
//					case 3:
//						User.ticketsT3.add(t);
//						break;
//					default:
//						throw new JSONException("Invalid ticket type while retrieving tickets! (type=" + t.ticket_type + ")");
//					}
//					
////					HomeActivity.this.updateTickets();
//				} catch (JSONException e) {
//					System.err.println(e.toString());
//					System.err.println("Invalid JSON while retrieving tickets!");
//				}
//			}
			MainActivity.this.startHome();
		}
	}
}
