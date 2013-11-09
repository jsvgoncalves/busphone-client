package org.fe.up.joao.busphoneclient.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class md5Helper {
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String hashMD5(String str) {
		byte[] bytesOfMessage;
		byte[] hashed = null;
		try {
			
			bytesOfMessage = str.getBytes("UTF-8");
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			hashed = md.digest(bytesOfMessage);
			StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < hashed.length; i++) {
	          sb.append(Integer.toString((hashed[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        return sb.toString();
	   
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
