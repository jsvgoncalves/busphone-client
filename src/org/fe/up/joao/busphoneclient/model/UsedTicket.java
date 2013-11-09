package org.fe.up.joao.busphoneclient.model;

public class UsedTicket extends Ticket {

    
    public String dateUsed = "Undefined";
    public String bus_id = "Undefined";
    
    public UsedTicket(String id, String ticket_type, String uuid,
    		String created_at, String updated_at, String date_used, String bus_id) {
		super(id, ticket_type, uuid, created_at, updated_at);
		this.dateUsed = date_used;
		this.bus_id = bus_id;
	}

}
