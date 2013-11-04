package org.fe.up.joao.busphoneclient;

import org.fe.up.joao.busphoneclient.helper.Contents;
import org.fe.up.joao.busphoneclient.helper.QRCodeEncoder;
import org.fe.up.joao.busphoneclient.model.Ticket;
import org.fe.up.joao.busphoneclient.model.User;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String ticket_type = "0";
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_use_ticket);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    ticket_type = extras.getString("ticket_type");
		}
		
		
		Log.v("mylog", "Ticket type is " + ticket_type);
		if(ticket_type.equals("T1")) {
			Button bt1 = (Button) findViewById(R.id.t1button);
			bt1.setBackgroundColor(0xFF0000FF);
		} else if(ticket_type.equals("T2")) {
			Button bt1 = (Button) findViewById(R.id.t2button);
			bt1.setBackgroundColor(0xFF0000FF);
		} else if(ticket_type.equals("T3")) {
			Button bt1 = (Button) findViewById(R.id.t3button);
			bt1.setBackgroundColor(0xFF0000FF);
		}
	 
		updateQRCode();
	}
	
	public void buttonClicked(View view) {
		final Button bt = (Button) view;
//		Log.v("mylog", bt.getText().toString());
		
		Intent intent = getIntent();
		intent.putExtra("ticket_type", bt.getText().toString());
		finish();
		startActivity(intent);
	}
	
	protected void updateQRCode(){
		// ImageView to display the QR code in.  This should be defined in 
		// your Activity's XML layout file
		ImageView imageView = (ImageView) findViewById(R.id.qrCode);
	
		if (!User.ticketsHistory.isEmpty()) {
			String qrData = User.ticketsHistory.get(0).uuid;
			int qrCodeDimention = 500;
			System.out.println("Ticket history is empty.");
			QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
			        Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);
		
			try {
			    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
			    imageView.setImageBitmap(bitmap);
			} catch (WriterException e) {
			    e.printStackTrace();
			}
		} else {
			int qrCodeDimention = 500;
			QRCodeEncoder qrCodeEncoder = new QRCodeEncoder("bananas", null,
			        Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);
		
			try {
			    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
			    imageView.setImageBitmap(bitmap);
			} catch (WriterException e) {
			    e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
