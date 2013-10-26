package org.fe.up.joao.busphoneclient;

import org.fe.up.joao.busphoneclient.com.ComHelper;
import org.fe.up.joao.busphoneclient.com.JSONHelper;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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
	}
	
	/**
	 * Get user data.
	 */
	public void getUser(){
		GetUserTask task = new GetUserTask();
		String url = ComHelper.serverURL + "users/" + V.getID();
		task.execute("get", url);
	}

	
	/**
	 * Retrieves the user profile
	 * @author joao
	 *
	 */
	private class GetUserTask extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute(){
			
		}
		
		@Override
		protected String doInBackground(String... params) {
		    return ComHelper.httpGet(params);
		}
		
		@Override
		protected void onPostExecute (String result){
			System.out.println(result);
			JSONObject json = JSONHelper.string2JSON(result);
			try {
				String name = json.getString("name");
				TextView userName = (TextView) HomeActivity.this.findViewById(R.id.userName);
				userName.setText(name);
				
			} catch (JSONException e) {
				System.err.println("GetUserTask: Error parsing JSON.");
				e.printStackTrace();
			}
		}

	}
}
