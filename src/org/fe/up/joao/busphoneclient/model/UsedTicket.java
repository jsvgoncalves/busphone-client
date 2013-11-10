package org.fe.up.joao.busphoneclient.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.fe.up.joao.busphoneclient.R;

public class UsedTicket extends Ticket {

	
	public String dateUsed = "Undefined";
	public String bus_id = "Undefined";

	public UsedTicket(String id, String ticket_type, String uuid,
			String created_at, String updated_at, String date_used, String bus_id) {
		super(id, ticket_type, uuid, created_at, updated_at);
		this.dateUsed = date_used;
		this.bus_id = bus_id;
	}

	public UsedTicket(Ticket oldT, Date date) {
		super(oldT.id, oldT.ticket_type, oldT.uuid, null, null);
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-M-d H:m:s", Locale.getDefault());
		String formatedDate = dFormat.format(date).toString(); 
		this.dateUsed = formatedDate;
	}

}
