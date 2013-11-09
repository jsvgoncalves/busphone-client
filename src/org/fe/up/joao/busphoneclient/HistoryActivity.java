package org.fe.up.joao.busphoneclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.UsedTicket;
import org.fe.up.joao.busphoneclient.model.User;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
		
		initList();
	}

	/**
	 * Gets the used tickets from the server.
	 * Parses the JSON into and array.
	 * Creates a Map for each ticket and adds it to the ticket list.
	 * @param jsonString
	 */
	public void initList() {
		Log.v("MyLog", "Loading used tickets to view");
		usedTickets = new ArrayList<Map<String,String>>();
		
		if (User.ticketsHistory.isEmpty()) {
			HashMap<String, String> ticketItem = new HashMap<String, String>();
			ticketItem.put("first-line", "Sem histórico de viagens");
			ticketItem.put("second-line", "De momento ainda não utilizou nenhuma viagem.");
			usedTickets.add(ticketItem);
		} else {
			// Each Map is one List entry with 2 lines
			for (UsedTicket usedTicket : User.ticketsHistory) {
				HashMap<String, String> ticketItem = new HashMap<String, String>();
				ticketItem.put("first-line", "T" + usedTicket.ticket_type);
				ticketItem.put("second-line", usedTicket.dateUsed);
				usedTickets.add(ticketItem);
			}
		}
		ListView lv = (ListView) findViewById(R.id.history_list_view);
		simpleAdpt = new SimpleAdapter(this, usedTickets, android.R.layout.simple_list_item_2,
				new String[] {"first-line", "second-line"}, new int[] {android.R.id.text1,android.R.id.text2});
		
		lv.setAdapter(simpleAdpt);
	}
}
