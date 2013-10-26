package org.fe.up.joao.busphoneclient.com;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {

	public static JSONObject string2JSON(String json){
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			return new JSONObject();
		}
	}
	
}
