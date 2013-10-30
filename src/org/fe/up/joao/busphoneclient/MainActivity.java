package org.fe.up.joao.busphoneclient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

/**
 * This activity includes has the login screen.
 * It'll be skipped if there is already login information
 * on the device (auto login) and launch the Home Activity
 * @author joao
 *
 */
public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(hasLogin()){
			Intent intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME); // Clears the Main Activity
			startActivity(intent);
			finish();
			
		} else {
			setContentView(R.layout.activity_main);
		}
	}

	private boolean hasLogin() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
