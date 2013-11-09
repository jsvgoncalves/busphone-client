package org.fe.up.joao.busphoneclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.fe.up.joao.busphoneclient.helper.ComService;
import org.fe.up.joao.busphoneclient.helper.JSONHelper;
import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * This activity display the history
 * of ticket usage, including bus number,
 * date and time.
 * @author joao
 *
 */
public class HistoryActivity extends Activity {

	ArrayList<Map<String,String>> usedTickets;
	private SimpleAdapter simpleAdpt;
	BusPhoneClient bus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		bus = (BusPhoneClient) getApplication();
		
		String url = String.format("users/%s/history/t/%s", bus.getUserID(), bus.getToken());
		Log.v("MyLog", "Getting used tickets...");
		new ComService(url, this, "initList", false);
	}

	/**
	 * Gets the used tickets from the server.
	 * Parses the JSON into and array.
	 * Creates a Map for each ticket and adds it to the ticket list.
	 * @param jsonString
	 */
	public void initList(String jsonString) {
		try {
			Log.v("MyLog", "JSON: " + jsonString);
			
			JSONArray json = new JSONArray(jsonString);
			usedTickets = new ArrayList<Map<String,String>>();
			
			for (int i = 0; i < json.length(); i++) {
				JSONObject ticketJSON = json.getJSONObject(i);
				
				String type = JSONHelper.getValue(ticketJSON, "ticket_type");
				
				// 2013-11-09T00:00:00.000Z
				String dateUsed = JSONHelper.getValue(ticketJSON, "date_used");
				dateUsed = JSONHelper.changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd-MM-yyyy HH:mm", dateUsed);
				
				Log.e("mylog", "buuuuuhh");
				// new Ticket(id,type,uuid, dateUsed);
				HashMap<String, String> ticket = new HashMap<String, String>();
				ticket.put("first-line", "T" + type);
				ticket.put("second-line", dateUsed);
				usedTickets.add(ticket);
			}
			Log.e("mylog", usedTickets.get(0).get("second-line"));
			// We get the ListView component from the layout
			ListView lv = (ListView) findViewById(R.id.history_list_view);
			Log.v("MyLog", "Creating ticket list.");
			simpleAdpt = new SimpleAdapter(this, usedTickets, android.R.layout.simple_list_item_2,
					new String[] {"first-line", "second-line"}, new int[] {android.R.id.text1,android.R.id.text2});
			
			lv.setAdapter(simpleAdpt);
			
		} catch (JSONException e) {
			Toast.makeText(this, R.string.no_tickets_history, Toast.LENGTH_SHORT).show();
		}
	}
}
