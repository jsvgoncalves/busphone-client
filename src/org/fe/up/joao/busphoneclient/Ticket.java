package org.fe.up.joao.busphoneclient;

public class Ticket {

	public int id = -1;
    public int ticket_type = -1;
    public String uuid = "Undefined";
    public String created_at = "Undefined";
    public String updated_at = "Undefined";
    
    public Ticket(String... params){
    	if (params.length != 5) {
			return;
		}
    	this.id = Integer.valueOf(params[0]);
		this.ticket_type = Integer.valueOf(params[1]);
		this.uuid = params[2];
		this.created_at = params[3];
		this.updated_at = params[4];
    }
    
	public Ticket(int id, int ticket_type, String uuid, String created_at, String updated_at) {
		this.id = id;
		this.ticket_type = ticket_type;
		this.uuid = uuid;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}
    
}
