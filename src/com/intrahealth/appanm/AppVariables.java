package com.intrahealth.appanm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppVariables {

	public static String API(Context c) {
		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
		String url = prefs.getString("server_url", "localhost");
		String api = "http://" + url + "/";

		return api;

	}

	public static String AP(Context c) {
		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
		String url = prefs.getString("server_url", "localhost");

		return url;

	}

	/*
	 * Birth_reg_entry
	 * 
	 * http://192.168.1.34/msakhi/save.php http://192.168.1.34/msakhi/save.php
	 */
	/*
	 * DownloadDataAsync
	 * 
	 * http://192.168.1.34/msakhi/preg_reg_api.php
	 * "http://192.168.1.34/msakhi/download.php"
	 * "http://192.168.1.34/msakhi/download_success.php"
	 */
	/*
	 * preg_entry
	 * 
	 * http://192.168.1.34/msakhi/save.php
	 */
	/*
	 * sendDATASERVICE
	 * 
	 * http://192.168.1.34/msakhi/save.php http://192.168.1.34/msakhi/upload.php
	 */

}
