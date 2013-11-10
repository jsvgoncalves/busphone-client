package org.fe.up.joao.busphoneclient;

import org.fe.up.joao.busphoneclient.helper.QRCodeHelper;
import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.PorterDuff;

/**
 * Provides an enlarged version of the ticket
 * QR code and information about that ticket.
 * Must ask for a PIN upon creation. If the user
 * fails to introduce the PIN, the ticket will not be used
 * and the activity is destroyed.
 * @author joao
 *
 */
public class UseTicketActivity  extends Activity {
	String ticket_type = "T1";
	private boolean buttonPressed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_use_ticket);
		ticket_type = getIntent().getStringExtra("ticket_type");
		
//		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			ticket_type = extras.getString("ticket_type");
		}
		
		String qrData = ((BusPhoneClient) getApplicationContext()).getUserID()  + ";";
		// Log.v("mylog", "Ticket type is " + ticket_type);
		
		if(ticket_type.equals("T1")) {
			Button bt1 = (Button) findViewById(R.id.t1button);
			bt1.getBackground().setColorFilter(0xFF33B5E5, PorterDuff.Mode.MULTIPLY);
			bt1.setEnabled(false);
			qrData += User.ticketsT1.get(0).uuid;
		} else if(ticket_type.equals("T2")) {
			Button bt2 = (Button) findViewById(R.id.t2button);
			bt2.getBackground().setColorFilter(0xFF33B5E5, PorterDuff.Mode.MULTIPLY);
			bt2.setEnabled(false);
			qrData += User.ticketsT2.get(0).uuid;
		} else if(ticket_type.equals("T3")) {
			Button bt3 = (Button) findViewById(R.id.t3button);
			bt3.getBackground().setColorFilter(0xFF33B5E5, PorterDuff.Mode.MULTIPLY);
			bt3.setEnabled(false);
			qrData += User.ticketsT3.get(0).uuid;
		}
		
		// Check all tickets size to disable empty ones
		if(User.ticketsT1.size() < 1) {
			Button bt1 = (Button) findViewById(R.id.t1button);
			bt1.setEnabled(false);
		}
		if(User.ticketsT2.size() < 1) {
			Button bt2 = (Button) findViewById(R.id.t2button);
			bt2.setEnabled(false);
		}
		if(User.ticketsT3.size() < 1) {
			Button bt3 = (Button) findViewById(R.id.t3button);
			bt3.setEnabled(false);
		}

		ImageView imageView = (ImageView) findViewById(R.id.qrCode);
		QRCodeHelper.updateQRCode(qrData, imageView);
	}

	/**
	 * Handler for ticket1 button
	 * @param view
	 */
	public void t1Clicked(View view) {
		if(!buttonPressed) {
			buttonPressed = true;
			changeTicketType("T1");
		}
	}
	
	/**
	 * Handler for ticket2 button
	 * @param view
	 */
	public void t2Clicked(View view) {
		if(!buttonPressed) {
			buttonPressed = true;
			changeTicketType("T2");
		}
	}
	
	/**
	 * Handler for ticket3 button
	 * @param view
	 */
	public void t3Clicked(View view) {
		if(!buttonPressed) {
			buttonPressed = true;
			changeTicketType("T3");
		}
	}
	
	/**
	 * Starts a new activity of use tickets with the new type.
	 * @param type
	 */
	public void changeTicketType(String type) {
		Intent intent = getIntent();
		intent.putExtra("ticket_type", type);
		startActivity(intent);
		finish();
	}

	/**
	 * The cancel button just mimics the onBackPressed()
	 * @param v
	 */
	public void cancelClicked(View v) {
		onBackPressed();
	}
	
	/**
	 * Tries to validate a ticket and sends the user back to home screen
	 * @param v
	 */
	public void validateClicked(View v) {
		if(!User.validateTicket(this, ticket_type)) {
			Toast.makeText(this, "Falha a usar o bilhete.", Toast.LENGTH_LONG).show();
		}
		finish();
	}

	@Override
	public void onPause() {
		overridePendingTransition(0, 0);
		super.onPause();
		
	}
}
