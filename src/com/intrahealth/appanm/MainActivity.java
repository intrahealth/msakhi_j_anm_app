package com.intrahealth.appanm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.intrahealth.appanm.GoogleAnalyticsApp.TrackerName;
import com.intrahealth.msakhi.appanm.R;

public class MainActivity extends Activity {
	EditText edtPasswd;
	// ImageButton imageButton;later
	Button imageButton;
	int a;
	SharedPreferences prefs = null;
	String anm_id = "";

	ProgressBar pb;
	TextView tvSlno;
	Dialog dialog;
	int downloadedSize = 0;
	int totalSize = 0;
	TextView cur_val;
	String dwnload_file_path = "http://msakhi.org/webcreate_anmdb.php?anm_id=";

	/*
	 * int pending=0;
	 * 
	 * private SmsService serviceBinder; Intent i; private ServiceConnection
	 * connection = new ServiceConnection() { public void
	 * onServiceConnected(ComponentName className, IBinder service) {
	 * //---called when the connection is made--- serviceBinder =
	 * ((SmsService.MyBinder)service).getService(); startService(i); }
	 * 
	 * public void onServiceDisconnected(ComponentName className) { //---called
	 * when the service disconnects--- serviceBinder = null; } };
	 */

	public String getVersion() {
		String mVersionNumber;
		Context mContext = getApplicationContext();
		try {
			String pkg = mContext.getPackageName();
			mVersionNumber = mContext.getPackageManager()
					.getPackageInfo(pkg, 0).versionName;
		} catch (NameNotFoundException e) {
			mVersionNumber = "?";
		}
		return " V " + mVersionNumber;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);
		// setContentView(R.layout.activity_main); later
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Tracker t = ((GoogleAnalyticsApp) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Home");
		t.send(new HitBuilders.AppViewBuilder().build());

		edtPasswd = (EditText) findViewById(R.id.edtPasswd);
		edtPasswd.setInputType(InputType.TYPE_CLASS_NUMBER);
		edtPasswd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		edtPasswd.setSelectAllOnFocus(true);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		tvSlno = (TextView) findViewById(R.id.slno);
		anm_id = prefs.getString("id", "1").trim();
		tvSlno.setText(anm_id);
		setTitle(getResources().getString(R.string.app_name) + getVersion());
		addListenerOnButton();
	}

	@Override
	protected void onResume() {
		anm_id = prefs.getString("id", "1").trim();
		tvSlno.setText(anm_id);
		super.onResume();
	}

	private void addListenerOnButton() {
		// ImageButton btnAbout=(ImageButton)findViewById(R.id.btnAbout); later
		// btnAbout.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Context ctx=v.getContext();
		// Intent intent = new Intent(ctx,Aboutus.class);
		// ctx.startActivity(intent);
		// }
		// });
		imageButton = (Button) findViewById(R.id.btnStart);
		// imageButton = (ImageButton) findViewById(R.id.btnStart); later

		imageButton.setOnClickListener(new OnClickListener() {

			// @Override
			public void onClick(View arg0) {
				Context ctx = arg0.getContext();
				String pass_str = prefs.getString("user_password", "0000");
				String admpass_str = prefs.getString("adm_password", "123456");
				// Boolean send_sms=prefs.getBoolean("send_sms", false);
				if (arg0.getId() == R.id.btnStart) {
					Intent intent = null;
					if (edtPasswd.getText().toString()
							.equalsIgnoreCase(admpass_str))
						intent = new Intent(ctx, EditPreference.class);
					else if (!edtPasswd.getText().toString()
							.equalsIgnoreCase(pass_str))
						Toast.makeText(ctx, getString(R.string.errInvPass),
								Toast.LENGTH_LONG).show();
					else {
						/*
						 * if (send_sms) { i = new Intent(MainActivity.this,
						 * SmsService.class); bindService(i, connection,
						 * Context.BIND_AUTO_CREATE);
						 * pending=serviceBinder.pending; //startService(new
						 * Intent(getBaseContext(), SmsService.class)); }
						 */
						// intent = new Intent(ctx,Workflow.class);

						// if (!anm_id.equalsIgnoreCase("1")){
						// File sd = Environment.getExternalStorageDirectory();
						//
						// if (sd.canWrite()) {
						// final File dbFile = new File(sd,
						// anm_id+"appanm_back.db");
						// final File restFile = new File(sd,
						// anm_id+"appanm_rest.db");
						// if (!dbFile.exists()&&!restFile.exists()) {
						//
						//
						//
						// showProgress(dwnload_file_path+anm_id);
						//
						// new Thread(new Runnable() {
						// public void run() {
						// downloadFile();
						// }
						//
						// }).start();
						// }
						//
						// else{
						// intent = new Intent(ctx,Workflow.class);
						// }
						// }
						// }

						intent = new Intent(ctx, Workflow.class);
					}
					// intent = new Intent(ctx,Workflow.class);
					if (intent != null)
						ctx.startActivity(intent);

				}
			}
		});

	}

	void downloadFile() {

		try {
			URL url = new URL(dwnload_file_path + anm_id);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);

			// connect
			urlConnection.connect();

			// set the path where we want to save the file
			File SDCardRoot = Environment.getExternalStorageDirectory();
			// create a new file, to save the downloaded file
			File file = new File(SDCardRoot, anm_id + "appanm_rest.db");

			FileOutputStream fileOutput = new FileOutputStream(file);

			// Stream used for reading the data from the internet
			InputStream inputStream = urlConnection.getInputStream();

			// this is the total size of the file which we are downloading
			totalSize = urlConnection.getContentLength();

			runOnUiThread(new Runnable() {
				public void run() {
					pb.setMax(totalSize);
				}
			});

			// create a buffer...
			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				// update the progressbar //
				runOnUiThread(new Runnable() {
					public void run() {
						pb.setProgress(downloadedSize);
						float per = ((float) downloadedSize / totalSize) * 100;
						cur_val.setText("Downloaded " + downloadedSize
								+ "KB / " + totalSize + "KB (" + (int) per
								+ "%)");
					}
				});
			}
			// close the output stream when complete //
			fileOutput.close();
			runOnUiThread(new Runnable() {
				public void run() {
					// pb.dismiss(); // if you want close it..
				}
			});
		} catch (final MalformedURLException e) {
			showError("Error : MalformedURLException " + e);
			e.printStackTrace();
		} catch (final IOException e) {
			showError("Error : IOException " + e);
			e.printStackTrace();
		} catch (final Exception e) {
			showError("Error : Please check your internet connection " + e);
		}
	}

	void showError(final String err) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	void showProgress(String file_path) {
		dialog = new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.myprogressdialog);
		dialog.setCancelable(false);
		dialog.setTitle("Download Progress");

		TextView text = (TextView) dialog.findViewById(R.id.tv1);
		Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
		text.setText("Downloading file from ... " + file_path);
		cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
		cur_val.setText("Starting download...");
		dialog.show();

		pb = (ProgressBar) dialog.findViewById(R.id.progress_bar);
		pb.setProgress(0);
		pb.setProgressDrawable(getResources().getDrawable(
				R.drawable.green_progress));

		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				dialog.dismiss();
			}
		});
	}

	@Override
	public void onBackPressed() {

		new AlertDialog.Builder(this)
				.setMessage("सोच लीजिए! क्या आपको बाहर निकलना है?")
				.setCancelable(false)
				.setPositiveButton("हां",
						new DialogInterface.OnClickListener() {
							@SuppressWarnings("resource")
							public void onClick(DialogInterface dialog, int id) {
								try {
									stopService(new Intent(getBaseContext(),
											SmsService.class));
									String pnm = getApplicationContext()
											.getPackageName();
									File sd = Environment
											.getExternalStorageDirectory();
									// String pfname=getFilesDir().getPath()+
									// "../shared_prefs/"+pnm+"_preferences.xml";

									if (sd.canWrite()) {
										File locDB = new File(getFilesDir(),
												"../databases/");
										String backupDBPath = anm_id
												+ "appanm_back.db";
										File currentDB = new File(locDB,
												DBAdapter.DATABASE_NAME);
										File backupDB = new File(sd,
												backupDBPath);

										if (currentDB.exists()) {
											@SuppressWarnings("resource")
											FileChannel src = new FileInputStream(
													currentDB).getChannel();
											@SuppressWarnings("resource")
											FileChannel dst = new FileOutputStream(
													backupDB).getChannel();
											dst.transferFrom(src, 0, src.size());
											src.close();
											dst.close();
										}
										if (true) {
											File locPF = new File(
													getFilesDir(),
													"../shared_prefs/");
											File currentPF = new File(locPF,
													pnm + "_preferences.xml");
											File backupPF = new File(sd,
													"prefs.xml");
											FileChannel src = new FileInputStream(
													currentPF).getChannel();
											FileChannel dst = new FileOutputStream(
													backupPF).getChannel();
											dst.transferFrom(src, 0, src.size());
											src.close();
											dst.close();
										}

									}
								} catch (Exception e) {
									e.printStackTrace();

								}
								finish();
							}
						}).setNegativeButton("ना", null).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// The activity is about to be destroyed.
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(MainActivity.this)
				.reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(MainActivity.this).reportActivityStop(this);
	}

}
