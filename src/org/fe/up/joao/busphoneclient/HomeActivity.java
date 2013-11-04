package org.fe.up.joao.busphoneclient;

import org.fe.up.joao.busphoneclient.helper.Contents;
import org.fe.up.joao.busphoneclient.helper.QRCodeEncoder;
import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import android.app.ActionBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		Log.v("mylog", "network: " + ComHelper.isOnline(this));
		super.onCreate(savedInstanceState);
		setResult(RESULT_OK); // ->RESULT_OK means it's not a logout. THe user just left the app.
		bus = (BusPhoneClient) getApplicationContext();
		setContentView(R.layout.activity_home);
		
		setUserName();
		updateTickets();
//		updateQRCode();	
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
	        case R.id.logout:
	            logout();
	        	Log.v("mylog", "logging out");
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Handler for ticket button T1 
	 */
	public void buttonClickedT1(View view) {
		if(User.ticketsT1.isEmpty()) {
			Toast toast = new Toast(getBaseContext());
			toast.setText(getString(R.string.no_tickets_for_you));
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Intent intent = new Intent(this, UseTicketActivity.class);
			intent.putExtra("ticket_type", "T1");
			startActivity(intent);
		}
	}
	
	/**
	 * Handler for ticket button T2
	 */ 
	public void buttonClickedT2(View view) {
		if(User.ticketsT2.isEmpty()) {
			Toast toast = new Toast(getBaseContext());
			toast.setText(getString(R.string.no_tickets_for_you));
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Intent intent = new Intent(this, UseTicketActivity.class);
			intent.putExtra("ticket_type", "T2");
			startActivity(intent);
		}
	}
	
	/**
	 * Handler for ticket button T3
	 */
	public void buttonClickedT3(View view) {
		if(User.ticketsT3.isEmpty()) {
			Toast toast = new Toast(getBaseContext());
			toast.setText(getString(R.string.no_tickets_for_you));
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Intent intent = new Intent(this, UseTicketActivity.class);
			intent.putExtra("ticket_type", "T3");
			startActivity(intent);
		}
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
		Log.v("mylog", "stopping home");
	}
}
