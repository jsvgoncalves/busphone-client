package org.fe.up.joao.busphoneclient;

import org.fe.up.joao.busphoneclient.helper.ComService;
import org.fe.up.joao.busphoneclient.helper.JSONHelper;
import org.fe.up.joao.busphoneclient.helper.md5Helper;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
		
		// get 'users/create/:name/:email/:pw'
		String name = ((EditText) findViewById(R.id.form_name)).getText().toString();
		String email = ((EditText) findViewById(R.id.form_email)).getText().toString();
		String password = ((EditText) findViewById(R.id.form_pw)).getText().toString();
		String ccnum = ((EditText) findViewById(R.id.form_ccnum)).getText().toString();
		String ccmonth = ((EditText) findViewById(R.id.form_month)).getText().toString();
		String ccyear = ((EditText) findViewById(R.id.form_year)).getText().toString();
		String cvv = ((EditText) findViewById(R.id.form_cvv)).getText().toString();
		
		String url = String.format("users/create/%s/%s/%s/%s/%s/%s/%s",
				name, md5Helper.hashMD5(email), md5Helper.hashMD5(password),
				md5Helper.hashMD5(ccnum), md5Helper.hashMD5(ccmonth), md5Helper.hashMD5(ccyear), md5Helper.hashMD5(cvv));
		
		new ComService(url, this, "postRegister", true, getString(R.string.registring));
	}
	
	public void postRegister(String result){
		try {
			if (JSONHelper.string2JSON(result).getInt("status") == 0) {
				finish();
			} else {
				String msg = JSONHelper.string2JSON(result).getString("msg");
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			Toast.makeText(this, "Criação falhou", Toast.LENGTH_SHORT).show();
		}
	}
}
