package com.intrahealth.appanm;

//import com.intrahealth.mnewborncare.control.BadgeView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
//import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
//import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
//import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.StrictMode;
//import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.intrahealth.msakhi.appanm.R;

public class Workflow extends Activity {
	Context context = this;
	int l = 0;
	int inte;
	int j;
	Cursor c;
	Cursor c1;
	Intent itent1;
	String[] image = new String[5000];
	int pending = 0;
	boolean send_sms = false;
	String anm_id = "";
	String server_id, image_folder;
	int k = 0;
	String[] array_of_server_id = new String[5000];
	private DBAdapter mydb;
	boolean isBound = false;
	String str = "";
	Button btnUpdate;
	Button btnAnmUpdate;
	String TimeStamp = "2013-01-23 10:20:56";
	Date DateMs;
	long timeInMilliseconds;
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String TIMESTAMPMS = "utime";
	public static final String LastTime = "ltime";
	SharedPreferences sharedpreferences;
	String link;
	boolean gprs_enabled_fromapp = false;

	// private SmsService serviceBinder;
	private SmsService serviceBinder;
	Intent i;
	/*
	 * private ServiceConnection connection = new ServiceConnection() { public
	 * void onServiceConnected(ComponentName className, IBinder service) {
	 * //---called when the connection is made--- serviceBinder =
	 * ((SmsService.LocalBinder)service).getService(); startService(i);
	 * pending=serviceBinder.getPending(); }
	 * 
	 * public void onServiceDisconnected(ComponentName className) { //---called
	 * when the service disconnects--- serviceBinder = null; } };
	 */
	private ServiceConnection connection = new ServiceConnection() {

		@SuppressWarnings("unchecked")
		public void onServiceConnected(ComponentName className, IBinder service) {
			// ---called when the connection is made---
			serviceBinder = ((LocalBinder<SmsService>) service).getService();
			pending = serviceBinder.getPending();
			// if (pending==0) {
			// btnPendSms.setText("Clear");btnPendSms.setBackgroundResource(R.drawable.custom_button_green);}
			// else
			// {btnPendSms.setText("SMS pending");btnPendSms.setBackgroundResource(R.drawable.custom_button_red);}
			startService(i);

		}

		public void onServiceDisconnected(ComponentName className) {
			// ---called when the service disconnects---
			serviceBinder = null;
			isBound = false;
		}
	};

	@Override
	public void onStop() {

		super.onStop();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		stopService(itent1);
		super.onBackPressed();
	}

	@Override
	public void onDestroy() {
		try {

			if (isBound)
				unbindService(connection);
			if (gprs_enabled_fromapp)
				Connectivity.setMobileDataEnabled(getApplicationContext(),
						false);
			mydb.myclose();

		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.workflow);
		setContentView(R.layout.updated_buttons);
		link = AppVariables.API(context);

		itent1 = new Intent(getApplicationContext(), UpdateService.class);
		startService(itent1);

		Button btnMPreg = (Button) findViewById(R.id.btnMPreg);
		Button btnMBirth = (Button) findViewById(R.id.btnMBirth);
		Button btnMVisit = (Button) findViewById(R.id.btnMVisit);
		Button btnCreport = (Button) findViewById(R.id.btnReport);
		Button btnMDeath = (Button) findViewById(R.id.btnMDeath);
		Button btnSync = (Button) findViewById(R.id.btnDreport);

		Button vhsndpreg = (Button) findViewById(R.id.vhsndpreg);
		Button vhsndbirth = (Button) findViewById(R.id.vhsndbirth);
		// New hero Add
		Button btndashboard = (Button) findViewById(R.id.btnWebview);
		btnAnmUpdate = (Button) findViewById(R.id.btnAnmUpdate);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnUpdate.setText("अपडेट ");
		// Button btnPendSms=(Button)findViewById(R.id.btnSMSind);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		// send_sms=prefs.getBoolean("send_sms", false);
		send_sms = prefs.getBoolean("gprs_sync", true);
		if (send_sms)
			if (!Connectivity.isConnectingToInternet(getApplicationContext())) {
				Connectivity
						.setMobileDataEnabled(getApplicationContext(), true);
				gprs_enabled_fromapp = true;
			}
		anm_id = prefs.getString("id", "1").trim();
		restoreDb();

		mydb = DBAdapter.getInstance(getApplicationContext());
		// Hero uncomment
		btnAnmUpdate.setText("सिंक अन्म डेटा(" + mydb.getPendWeb() + ")");

		HandlerThread hThread = new HandlerThread("HandlerThread");
		hThread.start();

		final Handler handler1 = new Handler(hThread.getLooper());
		final long oneMinuteMs = 4000;

		Runnable eachMinute = new Runnable() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						// Hero Uncomment
						mydb = DBAdapter.getInstance(getApplicationContext());
						btnAnmUpdate.setText("सिंक अन्म डेटा("
								+ mydb.getPendWeb() + ")");

					}
				});
				handler1.postDelayed(this, oneMinuteMs);
			}
		};

		// Schedule the first execution
		handler1.postDelayed(eachMinute, oneMinuteMs);

		if (send_sms) {
			// btnPendSms.setVisibility(android.view.View.INVISIBLE);
			i = new Intent(Workflow.this, SmsService.class);
			isBound = getApplicationContext().bindService(i, connection,
					Context.BIND_AUTO_CREATE);
			// btnPendSms=(Button)findViewById(R.id.btnSMSind);
			// startService(new Intent(getBaseContext(), SmsService.class));
		}

		// TimeStamp = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new
		// java.util.Date()).toString());

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		/*
		 * if (sharedpreferences.contains(TIMESTAMPMS)) {
		 * btnUpdate.setText(sharedpreferences.getString(TIMESTAMPMS, ""));
		 * TimeStamp = sharedpreferences.getString(LastTime, ""); }
		 */

		btnMPreg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Asha_list.class);
				intent.putExtra("rep_ind", 0);
				startActivity(intent);
			}
		});

		btnMBirth.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Asha_list.class);
				intent.putExtra("rep_ind", 1);
				startActivity(intent);
			}
		});

		btnMDeath.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Asha_list.class);
				intent.putExtra("rep_ind", 2);
				startActivity(intent);
			}
		});

		vhsndpreg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Asha_list.class);
				intent.putExtra("rep_ind", 6);
				startActivity(intent);
			}
		});
		// New Hero Add
		btndashboard.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						GuestDonationWebView.class);
				intent.putExtra("rep_ind", 6);
				startActivity(intent);
			}
		});

		vhsndbirth.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Asha_list.class);
				intent.putExtra("rep_ind", 7);
				startActivity(intent);
			}
		});

		btnMVisit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Report_list.class);
				startActivity(intent);
			}
		});

		btnSync.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*
				 * SmsManager sms = SmsManager.getDefault(); String
				 * pktHeader=getString
				 * (R.string.sms_prefix)+" "+getString(R.string
				 * .signature)+" "+anm_id; String
				 * phoneNumber=getString(R.string.smsno); String
				 * longMessage=String
				 * .format("%s:AN:SYNC:%s",pktHeader,mydb.get_pend_sms());
				 * sms.sendTextMessage(phoneNumber, null, longMessage, null,
				 * null); Toast.makeText(getApplicationContext(),
				 * "Sync request sent to server", Toast.LENGTH_SHORT).show();
				 */
				Intent intent = new Intent(getApplicationContext(),
						Report_worklist.class);
				startActivity(intent);
			}
		});

		btnCreport.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Asha_list.class);
				intent.putExtra("rep_ind", 5);
				startActivity(intent);
			}
		});

		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				try {
					DateMs = sdf.parse(TimeStamp);
					Log.e("date", DateMs + "");
					timeInMilliseconds = DateMs.getTime();
					Log.e("TimeStamp", timeInMilliseconds + "");
					System.out.println("timeinMilliseconds"
							+ timeInMilliseconds);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// showAlert(TimeStamp+" "+Long.toString(timeInMilliseconds));
				TimeStamp = (DateFormat.format("yyyy-MM-dd hh:mm:ss",
						new java.util.Date()).toString());
				btnUpdate.setText("अपडेट");
				String n = TimeStamp;

				Editor editor = sharedpreferences.edit();
				editor.putString(TIMESTAMPMS, "Update(" + n + ")");
				editor.putString(LastTime, n);
				editor.commit();
				new JSONParse().execute();

			}
		});
		// Hero Add for Auto Login Update
		// LoginUpdate();
		exportDB();
	}

	// Hero Add for auto Login Update
	public void LoginUpdate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			DateMs = sdf.parse(TimeStamp);
			Log.e("date", DateMs + "");
			timeInMilliseconds = DateMs.getTime();
			Log.e("TimeStamp", timeInMilliseconds + "");
			System.out.println("timeinMilliseconds" + timeInMilliseconds);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// showAlert(TimeStamp+" "+Long.toString(timeInMilliseconds));
		TimeStamp = (DateFormat.format("yyyy-MM-dd hh:mm:ss",
				new java.util.Date()).toString());
		btnUpdate.setText("अपडेट");
		String n = TimeStamp;

		Editor editor = sharedpreferences.edit();
		editor.putString(TIMESTAMPMS, "Update(" + n + ")");
		editor.putString(LastTime, n);
		editor.commit();
		new JSONParse().execute();
	}

	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Workflow.this);
			pDialog.setMessage("Updating Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			JSONObject json = null;
			HttpResponse response;
			HttpClient myClient = new DefaultHttpClient();
			String s = link + "webget_anmdata.php?anm_id=" + anm_id + "&dtime="
					+ timeInMilliseconds;
			Log.e("URL_MSAKHI", s);
			HttpPost myConnection = new HttpPost(s);
			System.out.println(s);
			try {

				response = myClient.execute(myConnection);
				str = EntityUtils.toString(response.getEntity(), "UTF-8");
				Log.e("parsed data", str);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				JSONArray jArray = new JSONArray(str);

				json = jArray.getJSONObject(0);
				String stmt = json.getString("stmt");
				Log.e("string query", stmt);
				String[] str_array = stmt.split(";");
				for (inte = 0; inte < str_array.length; inte++) {
					System.out.println(str_array[inte]);
				}
				mydb.updateAshaData(str_array);
				c1 = mydb.getserverid();
				if (c1.moveToFirst()) {
					do {

						server_id = (c1.getString(c1
								.getColumnIndex("server_id")));
						array_of_server_id[j] = server_id;
						downlaodfile(link + "download.php", server_id);
						j++;
						c1.moveToNext();
					} while (c1.moveToNext());
				}

			} catch (JSONException e) {

				e.printStackTrace();

			}

			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
		}
	}

	public String downlaodfile(String FileDownloadPath, String fileName) {
		try {
			File root = android.os.Environment.getExternalStorageDirectory();
			File dir = new File(Environment.getExternalStorageDirectory() + "/"
					+ "mcare_hindi" + "/pdata/");
			if (dir.exists() == false) {
				dir.mkdirs();
			}
			File file = new File(dir, fileName + ".jpg");

			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 800;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 3000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(FileDownloadPath);

			String json = "";

			long startTime = System.currentTimeMillis();
			System.out.println(json);
			StringEntity se = new StringEntity(fileName);
			httpPost.setEntity(se);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedInputStream bufferinstream = new BufferedInputStream(
					inputStream);

			ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			int current = 0;
			while ((current = bufferinstream.read()) != -1) {
				baf.append((byte) current);
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.flush();
			fos.close();

			Log.d("DownloadManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ "sec");
			int dotindex = fileName.lastIndexOf('.');
			if (dotindex >= 0) {
				fileName = fileName.substring(0, dotindex);
			}
		} catch (IOException e) {
			Log.d("DownloadManager", "Error:" + e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public void restoreDb() {
		File sd = Environment.getExternalStorageDirectory();
		// String pfname=getFilesDir().getPath()+
		// "../shared_prefs/"+pnm+"_preferences.xml";
		String backupDBPath = anm_id + "appanm_rest.db";
		if (sd.canWrite()) {
			File locDB = new File(getFilesDir(), "../databases/");
			// Toast.makeText(getApplicationContext(), backupDBPath,
			// Toast.LENGTH_LONG).show();
			// String backupDBPath = "restoredata.db";
			final File currentDB = new File(locDB, DBAdapter.DATABASE_NAME);
			final File backupDB = new File(sd, backupDBPath);
			// Log.e("dbrest",backupDB.getPath());
			if (backupDB.exists()) {
				// Log.e("dbrest1",backupDB.getPath());
				new AlertDialog.Builder(this)
						.setMessage("Do you want to restore ?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										try {
											FileChannel dst = new FileOutputStream(
													currentDB).getChannel();
											FileChannel src = new FileInputStream(
													backupDB).getChannel();
											dst.transferFrom(src, 0, src.size());
											src.close();
											dst.close();
											backupDB.delete();
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}).setNegativeButton("No", null).show();
			}
		}
	}

	public void showAlert(String msg) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle("Alert").setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// Some stuff to do when ok got clicked
					}
				}).show();
	}

	private void exportDB() {

		// File sd = Environment.getDataDirectory();
		File f = new File(Environment.getExternalStorageDirectory().toString()
				+ "/com.intrahealth.msakhi.appanm");
		if (!f.isDirectory()) {
			f.mkdir();
		}
		String SAMPLE_DB_NAME = "appanm.db";
		File data = Environment.getDataDirectory();
		FileChannel source = null;
		FileChannel destination = null;
		String currentDBPath = "/data/" + "com.intrahealth.msakhi.appanm"
				+ "/databases/" + SAMPLE_DB_NAME;
		String backupDBPath = SAMPLE_DB_NAME;
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(f, backupDBPath);

		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
			// alerttoast(R.string.DBexport);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
