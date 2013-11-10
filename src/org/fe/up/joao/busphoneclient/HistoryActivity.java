package org.fe.up.joao.busphoneclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.fe.up.joao.busphoneclient.model.BusPhoneClient;
import org.fe.up.joao.busphoneclient.model.UsedTicket;
import org.fe.up.joao.busphoneclient.model.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		
		// If there is no history just show a message
		if (User.ticketsHistory.isEmpty()) {
			HashMap<String, String> ticketItem = new HashMap<String, String>();
			ticketItem.put("first-line", "Sem histórico de viagens");
			ticketItem.put("second-line", "De momento ainda não utilizou nenhuma viagem.");
			usedTickets.add(ticketItem);
		} else { // Else lets add ticketItems
			// Each Map is one List entry with 2 lines
			for (UsedTicket usedTicket : User.ticketsHistory) {
				HashMap<String, String> ticketItem = new HashMap<String, String>();
				ticketItem.put("first-line", "T" + usedTicket.ticket_type);
				ticketItem.put("second-line", usedTicket.dateUsed);
				ticketItem.put("id", usedTicket.id + "");
				usedTickets.add(ticketItem);
			}
		}
		
		ListView lv = (ListView) findViewById(R.id.history_list_view);
		simpleAdpt = new SimpleAdapter(this, usedTickets, android.R.layout.simple_list_item_2,
				new String[] {"first-line", "second-line"}, new int[] {android.R.id.text1,android.R.id.text2});
		
		lv.setAdapter(simpleAdpt);
		
		// Create a message handling object as an anonymous class.
		OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				// Do something in response to the click
//				Log.e("mylog", "clicked a ticket " + position);
				Intent intent = new Intent(HistoryActivity.this, UsedTicketQRActivity.class);
				intent.putExtra("position", position + "");
				startActivity(intent);
			}
		};

		lv.setOnItemClickListener(mMessageClickedHandler); 
	}
	
}
