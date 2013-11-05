package org.fe.up.joao.busphoneclient.helper;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ComService extends AsyncTask<String, String, String> {
	
	public static String serverURL = "http://busphone-service.herokuapp.com/";
	ProgressDialog Asycdialog;
	String methodName;
	Object object;
	
	@Override
	protected void onPreExecute(){}
	
	public ComService(String url, Object object, String methodName) {
		Asycdialog = new ProgressDialog((Context) object);
		String full_url = serverURL + url;
		this.methodName = methodName;
		this.object = object;
		this.execute(full_url);
		//set message of the dialog
        Asycdialog.setMessage("Fetching data.");
        //show dialog
        Asycdialog.show();
        super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
	    return ComHelper.httpGet(params);
	}
	
	@Override
	protected void onPostExecute (String result){
//		System.out.println(result);
//		Log.v("mylog", "result " + result);
		JSONObject json = JSONHelper.string2JSON(result);
//		String status = JSONHelper.getValue(json, "status");
		Asycdialog.dismiss();
		try {
			Method method = object.getClass().getMethod(methodName, String.class);
			method.invoke(object, result);
//			callback.invoke(object, json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
/**
 * Retrieves the user profile
 * and its tickets
 */
