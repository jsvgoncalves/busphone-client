package org.fe.up.joao.busphoneclient;

/**
 * Contains any values that are common
 * to multiple activities such as user
 * preferences stored locally or user
 * data downloaded on startup.
 * @author joao
 *
 */
public class V {
	
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
	private static int userID = 1; //FIXME: temporary debug
	
	public static String getName() {
		return name;
	}


	public static void setName(String name) {
		V.name = name;
	}


	public static String getEmail() {
		return email;
	}


	public static void setEmail(String email) {
		V.email = email;
	}


	public static String getPassword() {
		return password;
	}


	public static void setPassword(String password) {
		V.password = password;
	}



	public static String getToken() {
		return token;
	}


	public static void setToken(String token) {
		V.token = token;
	}

	public static int getID() {
		return userID;
	}
	
	public static void setID(int id) {
		V.userID = id;
	}
	
}
