package org.fe.up.joao.busphoneclient.model;

import java.util.ArrayList;
import java.util.Date;

import org.fe.up.joao.busphoneclient.helper.JSONHelper;
import org.fe.up.joao.busphoneclient.helper.TicketsDataSource;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Contains any values that are common
 * to multiple activities such as user
 * preferences stored locally or user
 * data downloaded on startup.
 * @author joao
 *
 */
public class User {
	
	// User preferences
	/**
	 * User's real name
	 */
	private static String name;
	/**
	 * Email is used as login name
	 */
	private static String email;
	/**
	 * Encrypted password
	 */
	private static String password;
	
	/**
	 * Session token
	 */
	private static String token;
	
	/**
	 * Database user ID
	 */
	private static int userID = -1;
	
	public static ArrayList<Ticket> ticketsT1 = new ArrayList<Ticket>();
	public static ArrayList<Ticket> ticketsT2 = new ArrayList<Ticket>();
	public static ArrayList<Ticket> ticketsT3 = new ArrayList<Ticket>();
	public static ArrayList<UsedTicket> ticketsHistory = new ArrayList<UsedTicket>();
	
	public static String getName() {
		return name;
	}


	public static void setName(String name) {
		User.name = name;
	}


	public static String getEmail() {
		return email;
	}


	public static void setEmail(String email) {
		User.email = email;
	}


	public static String getPassword() {
		return password;
	}


	public static void setPassword(String password) {
		User.password = password;
	}



	public static String getToken() {
		return token;
	}


	public static void setToken(String token) {
		User.token = token;
	}

	public static int getID() {
		return userID;
	}
	
	public static void setID(int id) {
		User.userID = id;
	}


	public static void resetTickets() {
		ticketsT1.clear();
		ticketsT2.clear();
		ticketsT3.clear();
	}
	
	public static void parseTickets(JSONObject json) {
		ArrayList<String> t1 = JSONHelper.getArray(json, "user", "tickets", "t1");
		ArrayList<String> t2 = JSONHelper.getArray(json, "user", "tickets", "t2");
		ArrayList<String> t3 = JSONHelper.getArray(json, "user", "tickets", "t3");
		
		ArrayList<String> tUsed = JSONHelper.getArray(json, "user", "tickets", "used");
		
		parseTicketArray(t1, ticketsT1);
		parseTicketArray(t2, ticketsT2);
		parseTicketArray(t3, ticketsT3);
		parseUsedTicketArray(tUsed, ticketsHistory);
	}


	private static void parseUsedTicketArray(ArrayList<String> tickets_str, ArrayList<UsedTicket> tickets) {
		JSONObject json;
		tickets.clear();
		Log.v("mylog", "Parsing used tickets (" + tickets_str.size() + ")");
		for (String ticketStr : tickets_str) {
			json = JSONHelper.string2JSON(ticketStr);
			try {
				String dateUsed = JSONHelper.changeDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd-MM-yyyy HH:mm", json.getString("date_used"));
				UsedTicket t = new UsedTicket(json.getString("id"),
						json.getString("ticket_type"),
						json.getString("uuid"),
						json.getString("created_at"),
						json.getString("updated_at"),
						dateUsed,
						json.getString("bus_id"));
				tickets.add(t);
			} catch (JSONException e) {
				System.err.println(e.toString());
				System.err.println("Invalid JSON while retrieving tickets!");
			}
		}
	}


	private static void parseTicketArray(ArrayList<String> tickets_str, ArrayList<Ticket> tickets) {
		JSONObject json;
		tickets.clear();
		for (String ticketStr : tickets_str) {
			json = JSONHelper.string2JSON(ticketStr);
			try {
				Ticket t = new Ticket(json.getString("id"),
						json.getString("ticket_type"),
						json.getString("uuid"),
						json.getString("created_at"),
						json.getString("updated_at"));
				tickets.add(t);
			} catch (JSONException e) {
				System.err.println(e.toString());
				System.err.println("Invalid JSON while retrieving tickets!");
			}
		}
		
	}

	/**
	 * Clears all the tickets in db and then insert the current ones
	 * 
	 */
	public static void updateTicketsDB(Context context) {
		TicketsDataSource db = new TicketsDataSource(context);

		// First clear all the records
		db.clearTickets();
		
		// Then insert
		for (Ticket ticket : ticketsT1) {
			db.insertTicket(ticket);
		}
		
		for (Ticket ticket : ticketsT2) {
			db.insertTicket(ticket);
		}
		
		for (Ticket ticket : ticketsT3) {
			db.insertTicket(ticket);
		}
	}


	/**
	 * Fetches the tickets from the database and updates the array list.
	 * @param context
	 */
	public static void fetchTicketsFromDB(Context context) {
		TicketsDataSource db = new TicketsDataSource(context);

		ticketsT1 = db.getAllTickets(1);
		ticketsT2 = db.getAllTickets(2);
		ticketsT3 = db.getAllTickets(3);
		
		
	}


	public static boolean validateTicket(Context context, String ticket_type) {
		TicketsDataSource db = new TicketsDataSource(context);
		
		UsedTicket t = null;
		if(ticket_type.equals("T1")) {
			Ticket oldT = User.ticketsT1.get(0);
			t = new UsedTicket(oldT, new Date());
			db.deleteTicket(ticketsT1.get(0));
			ticketsT1.remove(0);
		} else if(ticket_type.equals("T2")) {
			Ticket oldT = User.ticketsT2.get(0);
			t = new UsedTicket(oldT, new Date());
			db.deleteTicket(ticketsT2.get(0));
			ticketsT2.remove(0);
		} else if(ticket_type.equals("T3")) {
			Ticket oldT = User.ticketsT3.get(0);
			t = new UsedTicket(oldT, new Date());
			db.deleteTicket(ticketsT3.get(0));
			ticketsT3.remove(0);
		} else {
			return false;
		}
		User.ticketsHistory.add(0, t);
		return true;
	}
}
