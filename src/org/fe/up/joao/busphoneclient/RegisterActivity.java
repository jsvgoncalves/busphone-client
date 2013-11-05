package org.fe.up.joao.busphoneclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class RegisterActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	public void cancelAction(View v) {
		onBackPressed();
	}
	
	public void createAccountAction(View v) {
		Log.v("mylog", "Creating acc");
	}
}
