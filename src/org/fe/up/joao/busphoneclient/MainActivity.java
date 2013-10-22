package org.fe.up.joao.busphoneclient;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.view.Menu;
import android.widget.TextView;

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
		setContentView(R.layout.activity_main);
		
		String z2Num = "<br><small>x3</small>";
		String ticket_label = getString(R.string.z2) + z2Num;
		((TextView)findViewById(R.id.z2button)).setText(Html.fromHtml(ticket_label));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
