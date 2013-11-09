package org.fe.up.joao.busphoneclient.helper;

import org.fe.up.joao.busphoneclient.model.User;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;


public class QRCodeHelper {

	public Intent encodeBarcode(String type, String data) {
		Intent intent = new Intent("com.google.zxing.client.android.ENCODE");
		intent.putExtra("ENCODE_TYPE", type);
		intent.putExtra("ENCODE_DATA", data);
		return intent;
	}
	
	
	public static void updateQRCode(String qrMessage, ImageView imageView){

		if (!User.ticketsHistory.isEmpty()) {
			
			int qrCodeDimention = 500;
			QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrMessage, null,
					Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);

			try {
				Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
				imageView.setImageBitmap(bitmap);
			} catch (WriterException e) {
				e.printStackTrace();
			}
		}
	}

}
