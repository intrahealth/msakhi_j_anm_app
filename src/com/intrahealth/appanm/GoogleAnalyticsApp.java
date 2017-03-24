/**
 * 
 */
package com.intrahealth.appanm;

import java.util.HashMap;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.intrahealth.msakhi.appanm.R;

/**
 * Created by Vivek Agrawal on May 2, 2015
 */
public class GoogleAnalyticsApp extends Application {

	public String getGlobaluserid() {
		return Globaluserid;
	}

	public void setGlobaluserid(String globaluserid) {
		Globaluserid = globaluserid;
	}

	public String getGlobalpassword() {
		return Globalpassword;
	}

	public void setGlobalpassword(String globalpassword) {
		Globalpassword = globalpassword;
	}

	private int globalWebViewBypass;

	public int getGlobalWebViewBypass() {
		return globalWebViewBypass;
	}

	public void setGlobalWebViewBypass(int globalWebViewBypass) {
		this.globalWebViewBypass = globalWebViewBypass;
	}

	// change the following line
//	private static final String PROPERTY_ID = "UA-62532404-1";
	//Hero Changes
	private static final String PROPERTY_ID = "UA-77946052-1";

	private String Globaluserid;
	private String Globalpassword;
	public static int GENERAL_TRACKER = 0;

	public enum TrackerName {
		APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
	}

	public HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public GoogleAnalyticsApp() {
		super();
	}

	public synchronized Tracker getTracker(TrackerName appTracker) {
		if (!mTrackers.containsKey(appTracker)) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = (appTracker == TrackerName.APP_TRACKER) ? analytics
					.newTracker(PROPERTY_ID)
					: (appTracker == TrackerName.GLOBAL_TRACKER) ? analytics
							.newTracker(R.xml.global_tracker) : analytics
							.newTracker(R.xml.ecommerce_tracker);
			mTrackers.put(appTracker, t);
		}
		return mTrackers.get(appTracker);
	}
}