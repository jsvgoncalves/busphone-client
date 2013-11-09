package org.fe.up.joao.busphoneclient;

import org.fe.up.joao.busphoneclient.helper.QRCodeHelper;
import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
			Button bt1 = (Button) findViewById(R.id.t2button);
			bt1.getBackground().setColorFilter(0xFF33B5E5, PorterDuff.Mode.MULTIPLY);
			bt1.setEnabled(false);
			qrData += User.ticketsT2.get(0).uuid;
		} else if(ticket_type.equals("T3")) {
			Button bt1 = (Button) findViewById(R.id.t3button);
			bt1.getBackground().setColorFilter(0xFF33B5E5, PorterDuff.Mode.MULTIPLY);
			bt1.setEnabled(false);
			qrData += User.ticketsT3.get(0).uuid;
		}

		ImageView imageView = (ImageView) findViewById(R.id.qrCode);
		QRCodeHelper.updateQRCode(qrData, imageView);
	}

	public void buttonClicked(View view) {
		// Disable all the buttons
		Button bt1 = (Button) findViewById(R.id.t1button);
		Button bt2 = (Button) findViewById(R.id.t2button);
		Button bt3 = (Button) findViewById(R.id.t3button);
		bt1.setEnabled(false);
		bt2.setEnabled(false);
		bt3.setEnabled(false);
		
		final Button bt = (Button) view;
		//		Log.v("mylog", bt.getText().toString());

		Intent intent = getIntent();
		intent.putExtra("ticket_type", bt.getText().toString());
		// Removes the animation to prevent weird effects on multiple clicks
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		startActivity(intent);
	}


	
	public void cancelClicked(View v) {
		onBackPressed();
	}
	
	public void validateClicked(View v) {
		if(!User.validateTicket(this, ticket_type)) {
			Toast.makeText(this, "Falha a usar o bilhete.", Toast.LENGTH_LONG).show();
		}
		finish();
	}

/*	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case android.R.id.actio:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		
		}
		return super.onOptionsItemSelected(item);
	}*/

	@Override
	public void onPause() {
		overridePendingTransition(0, 0);
		super.onPause();
		
	}
}
