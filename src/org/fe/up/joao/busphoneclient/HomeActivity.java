package org.fe.up.joao.busphoneclient;

import java.util.ArrayList;

import org.fe.up.joao.busphoneclient.helper.ComHelper;
import org.fe.up.joao.busphoneclient.helper.Contents;
import org.fe.up.joao.busphoneclient.helper.JSONHelper;
import org.fe.up.joao.busphoneclient.helper.QRCodeEncoder;
import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.Ticket;
import org.fe.up.joao.busphoneclient.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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




	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		Log.v("mylog", "network: " + ComHelper.isOnline(this));
		super.onCreate(savedInstanceState);
		bus = (BusPhoneClient) getApplicationContext();
		setContentView(R.layout.activity_home);
		
		setUserName();
		updateTickets();
//		updateQRCode();
		
	}
	
	public void buttonClicked(View view) {
		final Button bt = (Button) view;
//		Log.v("mylog", bt.getText().toString());
		Intent intent = new Intent(this, UseTicketActivity.class);
		intent.putExtra("ticket_type", bt.getText().toString());
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
	
	
	public void setUserName() {
		TextView userName = (TextView) findViewById(R.id.using_ticket);
		userName.setText(String.format( getString(R.string.greeting), bus.getName()));
	}
	
	public void updateTickets() {
		Button buttonT1 = (Button) findViewById(R.id.t1button);
		Button buttonT2 = (Button) findViewById(R.id.t2button);
		Button buttonT3 = (Button) findViewById(R.id.t3button);
		
		buttonT1.setText(getString(R.string.t1) + "\nx" + User.ticketsT1.size());
		buttonT2.setText(getString(R.string.t2) + "\nx" + User.ticketsT2.size());
		buttonT3.setText(getString(R.string.t3) + "\nx" + User.ticketsT3.size());
	}

	
	
	
	@Override
	public void onStop() {
		super.onStop();
		Log.v("mylog", "stopping home");
	}
}
