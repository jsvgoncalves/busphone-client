package org.fe.up.joao.busphoneclient;

import org.fe.up.joao.busphoneclient.helper.QRCodeHelper;
import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.UsedTicket;
import org.fe.up.joao.busphoneclient.model.User;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UsedTicketQRActivity extends Activity {

	BusPhoneClient bus;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bus = (BusPhoneClient) getApplication();
		setContentView(R.layout.activity_usedticket);
		int pos = Integer.parseInt(getIntent().getStringExtra("position"));
		Log.e("mylog", ""+getIntent().getStringExtra("position"));
		UsedTicket t = User.ticketsHistory.get(pos);
		String pritiString = "T" + t.ticket_type + "   " + t.dateUsed;
		((TextView) findViewById(R.id.used_ticket_info)).setText(pritiString);
		
		setupQR();
	}
	private void setupQR() {
		String qrData = User.getID() + ";" + User.ticketsHistory.get(0).uuid;
		ImageView imageView = (ImageView) findViewById(R.id.qrCode_usedticket);
		QRCodeHelper.updateQRCode(qrData, imageView);
	}
}
