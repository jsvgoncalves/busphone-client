package org.fe.up.joao.busphoneclient.helper;

import android.content.Intent;


public class QRCodeHelper {

	public Intent encodeBarcode(String type, String data) {
		Intent intent = new Intent("com.google.zxing.client.android.ENCODE");
		intent.putExtra("ENCODE_TYPE", type);
		intent.putExtra("ENCODE_DATA", data);
		return intent;
	}

}
