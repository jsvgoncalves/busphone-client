package org.fe.up.joao.busphoneclient;

import java.util.ArrayList;

import org.fe.up.joao.busphoneclient.helper.ComHelper;
import org.fe.up.joao.busphoneclient.helper.Contents;
import org.fe.up.joao.busphoneclient.helper.JSONHelper;
import org.fe.up.joao.busphoneclient.helper.QRCodeEncoder;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		getUser();
		updateQRCode();
	}
	
	protected void updateQRCode(){
		// ImageView to display the QR code in.  This should be defined in 
		// your Activity's XML layout file
		ImageView imageView = (ImageView) findViewById(R.id.qrCode);
	
		if (!V.ticketsHistory.isEmpty()) {
			String qrData = V.ticketsHistory.get(0).uuid;
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
	
	/**
	 * Get user data.
	 */
	public void getUser(){
		(new GetUserTask()).getUser();
	}
	
	public void setUserName(String name) {
		TextView userName = (TextView) findViewById(R.id.using_ticket);
		userName.setText(String.format( getString(R.string.greeting), name));
	}
	
	public void updateTickets() {
		Button buttonT1 = (Button) findViewById(R.id.t1button);
		Button buttonT2 = (Button) findViewById(R.id.t2button);
		Button buttonT3 = (Button) findViewById(R.id.t3button);
		
		buttonT1.setText(getString(R.string.t1) + "\nx" + V.ticketsT1.size());
		buttonT2.setText(getString(R.string.t2) + "\nx" + V.ticketsT2.size());
		buttonT3.setText(getString(R.string.t3) + "\nx" + V.ticketsT3.size());
	}

	
	/**
	 * Retrieves the user profile
	 * and its tickets
	 */
	private class GetUserTask extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute(){}
		
		public void getUser() {
			String url = ComHelper.serverURL + "users/" + V.getID() + ComHelper.JSON_EXTENSION;
			this.execute("get", url);
		}

		@Override
		protected String doInBackground(String... params) {
		    return ComHelper.httpGet(params);
		}
		
		@Override
		protected void onPostExecute (String result){
			System.out.println(result);
			JSONObject json = JSONHelper.string2JSON(result);
			
			// Put the user's name in place.
			String name = JSONHelper.getValue(json, "user", "name");
			HomeActivity.this.setUserName(name);
			V.resetTickets();			
			ArrayList<String> tickets = JSONHelper.getArray(json, "user", "tickets");
			for (String ticketStr : tickets) {
				json = JSONHelper.string2JSON(ticketStr);
				try {
					Ticket t = new Ticket(json.getString("id"),
							json.getString("ticket_type"),
							json.getString("uuid"),
							json.getString("created_at"),
							json.getString("updated_at"));
					switch (t.ticket_type) {
					case 1:
						V.ticketsT1.add(t);
						break;
					case 2:
						V.ticketsT2.add(t);
						break;
					case 3:
						V.ticketsT3.add(t);
						break;
					default:
						throw new JSONException("Invalid ticket type while retrieving tickets! (type=" + t.ticket_type + ")");
					}
					
					HomeActivity.this.updateTickets();
				} catch (JSONException e) {
					System.err.println(e.toString());
					System.err.println("Invalid JSON while retrieving tickets!");
				}
			}
		}
	}
	
//	/**
//	 * Retrieves the user ticket data
//	 */
//	private class GetTicketsTask extends AsyncTask<String, String, String>{
//		
//		public void getTickets(){
//			String url = ComHelper.serverURL + "users/" + V.getID() + ComHelper.JSON_EXTENSION;
//			this.execute("get", url);
//		}
//		
//		@Override
//		protected String doInBackground(String... params) {
//		    return ComHelper.httpGet(params);
//		}
//		
//		@Override
//		protected void onPostExecute (String result){
//			System.out.println(result);
//			JSONObject json = JSONHelper.string2JSON(result);
//			
//			// Put the user's name in place.
//			String name = JSONHelper.getPath(json, "user", "name");
//			TextView userName = (TextView) HomeActivity.this.findViewById(R.id.userName);
//			userName.setText(String.format( getString(R.string.greeting), name));
//		}
//	}
}
