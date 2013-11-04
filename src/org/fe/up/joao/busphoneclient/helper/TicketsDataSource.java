package org.fe.up.joao.busphoneclient.helper;

import java.util.ArrayList;

import org.fe.up.joao.busphoneclient.model.Ticket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TicketsDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private SQLBusTickets dbHelper;
	  private String[] allColumns = { SQLBusTickets.FIELD_TICKET_ID,
			  SQLBusTickets.FIELD_TICKET_TYPE };

	  public TicketsDataSource(Context context) {
	    dbHelper = new SQLBusTickets(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
/*	  public Ticket createTicket(String comment) {
	    ContentValues values = new ContentValues();
	    values.put(SQLBusTickets.FIELD_TICKET_TYPE, comment);
	    long insertId = database.insert(SQLBusTickets.TABLE_TICKETS, null,
	        values);
	    Cursor cursor = database.query(SQLBusTickets.TABLE_TICKETS,
	        allColumns, SQLBusTickets.FIELD_TICKET_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Ticket newTicket = cursorToComment(cursor);
	    cursor.close();
	    return newTicket;
	  }*/
	  
	  public long insertTicket(Ticket ticket) {
		    ContentValues values = new ContentValues();
		    values.put(SQLBusTickets.FIELD_TICKET_TYPE, ticket.ticket_type);
		    values.put(SQLBusTickets.FIELD_TICKET_UUID, ticket.uuid);
		    long insertId = database.insert(SQLBusTickets.TABLE_TICKETS, null,
		        values);
		    return insertId;
		  }

//	  public void deleteComment(Ticket ticket) {
//	    long id = ticket.getId();
//	    System.out.println("Comment deleted with id: " + id);
//	    database.delete(SQLBusTickets.TABLE_TICKETS, SQLBusTickets.FIELD_TICKET_ID
//	        + " = " + id, null);
//	  }
	  
	  /*List<Contact> contactList = new ArrayList<Contact>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Contact contact = new Contact();
	            contact.setID(Integer.parseInt(cursor.getString(0)));
	            contact.setName(cursor.getString(1));
	            contact.setPhoneNumber(cursor.getString(2));
	            // Adding contact to list
	            contactList.add(contact);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return contactList;*/

	  public ArrayList<Ticket> getAllTickets() {
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();

		database.rawQuery("SELECT  * FROM " + SQLBusTickets.TABLE_TICKETS, null);
//	    Cursor cursor = database.query(SQLBusTickets.TABLE_TICKETS,
//	        allColumns, null, null, null, null, null);
/*
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	Ticket ticket = cursorToComment(cursor);
	    	tickets.add(ticket);
	    	cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();*/
	    return tickets;
	  }

	  private Ticket cursorToComment(Cursor cursor) {
	    Ticket ticket = new Ticket();
//	    ticket.setId(cursor.getLong(0));
//	    ticket.setTicket(cursor.getString(1));
	    return ticket;
	  }
	} 