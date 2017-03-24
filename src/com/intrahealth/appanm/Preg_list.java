package com.intrahealth.appanm;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.intrahealth.appanm.GoogleAnalyticsApp.TrackerName;
import com.intrahealth.msakhi.appanm.R;

public class Preg_list extends ListActivity {
	private DBAdapter mydb;
	// TextView txtCount;
	// Typeface face;
	int id = 0, seq = 0, pid, pcaste, preligion, dstat = 0;
	Long pmobile;
	String tv_from, tv_to;
	private int year;
	private int month;
	private int day, rep_ind = -1;
	static final int DATE_DIALOG_ID = 999;
	Button btnDate;
	ListView lst;
	String alertMsg;
	String mydt, asha_id, phname;
	Cursor c;
	int aa=0;
	MediaPlayer mediaPlayer = new MediaPlayer();
	public String hv_str = "", mname = "";
	boolean learn = false, death = false, fvisit = false, current = true;
	Button btnNewTest;
	Button btnModBirth, btn;

	// static int lastSel = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_visit_list);

		Tracker t = ((GoogleAnalyticsApp) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Preg List");
		t.send(new HitBuilders.AppViewBuilder().build());

		TextView tvHead = (TextView) findViewById(R.id.tvHead);
		TextView txtC3 = (TextView) findViewById(R.id.tvHeadC3);
		TextView txtC4 = (TextView) findViewById(R.id.tvHeadC4);
		TextView tvHeadC2 = (TextView) findViewById(R.id.tvHeadC2);
			
		// btn=(Button) findViewById(R.id.btnNewBirth);

		// face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
		// tvHead.setTypeface(face);
		// txtCount=(TextView)findViewById(R.id.txtCount);
		// btnImmun=(Button) findViewById(R.id.btnImmun);
		final Calendar clnd = Calendar.getInstance();
		year = clnd.get(Calendar.YEAR);
		month = clnd.get(Calendar.MONTH);
		day = clnd.get(Calendar.DAY_OF_MONTH);
		// cy=year;cm=month;cd=day;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			asha_id = String.valueOf(extras.getInt("asha_id"));
			rep_ind = extras.getInt("rep_ind");

		}
		if (rep_ind == 11 || rep_ind == 12) {
			tv_from = extras.getString("from");
			tv_to = extras.getString("to");
		}
		mydb = DBAdapter.getInstance(getApplicationContext());
		// txtCount.setText(String.valueOf(mydb.getPregNo()));
		mydt = String.format("'%04d-%02d-%02d'", year, month + 1, day);
		// c=mydb.getPregVisits(mydt,asha_id);
		// Cursor c;
		switch (rep_ind) {
		case 0:
			c = mydb.getAllPregList(asha_id);
			tvHead.setText("गर्भवती महिलाएं");
			txtC3.setText("ई.डी.डी. (महिना)");
			break;
		case 1:
			c = mydb.getAllBirthList(asha_id);
			tvHead.setText("जन्म सूची");
			txtC3.setText("जन्म तिथि ");
			break;
		case 2:
			c = mydb.getAllDeathList(asha_id);
			tvHead.setText("मृत्यु सूची");
			txtC3.setText("मृत्यु तिथि");
			break;
		case 6:
			c = mydb.getVhsndPregList(asha_id);
			tvHead.setText("गर्भवती महिलाएं");
			txtC3.setText("ई.डी.डी. (महिना)");
			break;
		case 7:
			c = mydb.getVhsndBirthList(asha_id);
			tvHead.setText("जन्म सूची");
			txtC3.setText("जन्म तिथि ");
			break;
		case 8:
			c = mydb.getRegPregList(asha_id);
			tvHead.setText("रजिस्टर्ड गर्भवती महिलाएं");
			txtC3.setText("ई.डी.डी. (महिना)");
			break;
		case 9:
			c = mydb.getRegBirthList(asha_id);
			tvHead.setText("रजिस्टर्ड जन्म सूची");
			txtC3.setText("जन्म तिथि ");
			break;
		case 11:
			c = null;
			c = mydb.getRegPregListreport(asha_id, tv_from, tv_to);
			tvHead.setText("गर्भवती महिला का नाम");
//			txtC4.setVisibility(View.VISIBLE);
//			txtC4.setText("पति का नाम");
			txtC3.setText("ई.डी.डी. (महिना)");
			break;
		case 12:
			c = null;
			c = mydb.getAshaListnewchildreport(asha_id, tv_from, tv_to);
			tvHead.setText("बच्चे का नाम");
			txtC4.setVisibility(View.VISIBLE);
			tvHeadC2.setText("बच्चे का नाम");
			txtC4.setText("माँ का नाम");
			txtC3.setText("जन्म तिथि");
			aa=11;
			
			break;
		default:
			c = mydb.getAllPregList(asha_id);
			tvHead.setText("गर्भवती महिलाएं");
			txtC3.setText("गर्भवती (संख्या)");
			break;
		}
		startManagingCursor(c);
		// the desired columns to be bound
		String[] from = new String[] { "_id", "name", "edd" };
		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.slno, R.id.name_entry, R.id.edd_entry };

		LazyCursorAdapterPreg ca = new LazyCursorAdapterPreg(this,
				R.layout.preg_list_row, c, from, to,aa);
		setListAdapter(ca);
		ListView lst = (ListView) findViewById(android.R.id.list);
		lst.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View v,
					int position, long arg3) {
				Cursor cursor = (Cursor) adapterView
						.getItemAtPosition(position);
				Intent intent;
				switch (rep_ind) {
				case 0:
					intent = new Intent(getApplicationContext(), Preg_reg.class);
					mname = cursor.getString(cursor.getColumnIndex("name"));
					pid = cursor.getInt(cursor.getColumnIndex("_id"));
					phname = cursor.getString(cursor.getColumnIndex("hname"));
					pcaste = cursor.getInt(cursor.getColumnIndex("caste"));
					preligion = cursor.getInt(cursor.getColumnIndex("religion"));
					pmobile = cursor.getLong(cursor.getColumnIndex("mobile"));
					Toast.makeText(
							getApplicationContext(),
							"" + mname + " " + pid + " " + asha_id + " "
									+ rep_ind, Toast.LENGTH_SHORT).show();
					intent.putExtra("pname", mname);
					intent.putExtra("pid", pid);
					intent.putExtra("asha_id", asha_id);
					intent.putExtra("rep_ind", rep_ind);
					intent.putExtra("phname", phname);
					intent.putExtra("pcaste", pcaste);
					intent.putExtra("preligion", preligion);
					intent.putExtra("pmobile", pmobile);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(getApplicationContext(),
							Birth_reg.class);
					mname = cursor.getString(cursor.getColumnIndex("name"));
					// int mcts=cursor.getInt(cursor.getColumnIndex("m_stat"));
					pid = cursor.getInt(cursor.getColumnIndex("_id"));
					String dob = cursor.getString(cursor.getColumnIndex("dob"));
					String weight = cursor.getString(cursor
							.getColumnIndex("weight"));
					String sex = cursor.getString(cursor.getColumnIndex("sex"));
					String rslt = cursor.getString(cursor
							.getColumnIndex("rslt"));
					String anm_stat = cursor.getString(cursor
							.getColumnIndex("anm_stat"));
					phname = cursor.getString(cursor.getColumnIndex("hname"));
					pcaste = cursor.getInt(cursor.getColumnIndex("caste"));
					preligion = cursor.getInt(cursor.getColumnIndex("religion"));
					pmobile = cursor.getLong(cursor.getColumnIndex("mobile"));
					Toast.makeText(
							getApplicationContext(),
							"" + pid + " " + asha_id + " " + weight + " "
									+ anm_stat, Toast.LENGTH_SHORT).show();
					intent.putExtra("pname", mname);
					intent.putExtra("pid", pid);
					intent.putExtra("asha_id", asha_id);
					intent.putExtra("rep_ind", rep_ind);
					intent.putExtra("pdob", dob);
					intent.putExtra("pweight", weight);
					intent.putExtra("psex", sex);
					intent.putExtra("anm_stat", anm_stat);
					intent.putExtra("phname", phname);
					intent.putExtra("pcaste", pcaste);
					intent.putExtra("preligion", preligion);
					intent.putExtra("pmobile", pmobile);
					// intent.putExtra("m_stat",mcts);
					startActivity(intent);
					break;
				case 6:
					intent = new Intent(getApplicationContext(),
							Vhsnd_preg.class);
					mname = cursor.getString(cursor.getColumnIndex("name"));
					pid = cursor.getInt(cursor.getColumnIndex("_id"));
					Toast.makeText(
							getApplicationContext(),
							"" + mname + " " + pid + " " + asha_id + " "
									+ rep_ind, Toast.LENGTH_SHORT).show();
					intent.putExtra("pname", mname);
					intent.putExtra("pid", pid);
					intent.putExtra("asha_id", asha_id);
					intent.putExtra("rep_ind", rep_ind);
					startActivity(intent);
					break;
				case 7:
					intent = new Intent(getApplicationContext(),
							Vhsnd_birth.class);
					mname = cursor.getString(cursor.getColumnIndex("name"));
					pid = cursor.getInt(cursor.getColumnIndex("_id"));
					String dob1 = cursor.getString(cursor.getColumnIndex("dob"));
					String rslt1 = cursor.getString(cursor
							.getColumnIndex("rslt"));
					Toast.makeText(
							getApplicationContext(),
							"" + mname + " " + pid + " " + asha_id + " "
									+ rslt1, Toast.LENGTH_SHORT).show();
					intent.putExtra("pname", mname);
					intent.putExtra("pid", pid);
					intent.putExtra("asha_id", asha_id);
					intent.putExtra("rep_ind", rep_ind);
					startActivity(intent);
					break;

				case 8:
					mname = cursor.getString(cursor.getColumnIndex("name"));
					pid = cursor.getInt(cursor.getColumnIndex("_id"));
					phname = cursor.getString(cursor.getColumnIndex("hname"));
					pcaste = cursor.getInt(cursor.getColumnIndex("caste"));
					preligion = cursor.getInt(cursor.getColumnIndex("religion"));
					pmobile = cursor.getLong(cursor.getColumnIndex("mobile"));

					alertMsg = "गर्भवती विवरण";
					showAlert("गर्भवती का वज़न:-  "
							+ cursor.getString(cursor
									.getColumnIndex("mother_weight"))
							+ "\nएम०सी०टी०एस० आई०डी०:- "
							+ cursor.getString(c.getColumnIndex("mother_mcts"))
							+ "\nरजिस्ट्रेशन की दिनांक:- "
							+ cursor.getString(c.getColumnIndex("reg_date"))
							+ "\nकुल जॉच:- "
							+ c.getString(c.getColumnIndex("anc_visit")));

					break;
				case 9:
					mname = cursor.getString(cursor.getColumnIndex("name"));
					pid = cursor.getInt(cursor.getColumnIndex("_id"));

					alertMsg = "शिशु का विवरण";
					showAlert("वज़न:-  "
							+ cursor.getString(cursor.getColumnIndex("weight"))
							+ "\nएम०सी०टी०एस० आई०डी०:- "
							+ cursor.getString(c.getColumnIndex("child_mcts"))
							+ "\nबच्चे का नाम:- "
							+ cursor.getString(c.getColumnIndex("child_name")));

					break;
				case 11:

					mname = cursor.getString(cursor.getColumnIndex("name"));
					pid = cursor.getInt(cursor.getColumnIndex("_id"));
					phname = cursor.getString(cursor.getColumnIndex("hname"));

					Toast.makeText(
							getApplicationContext(),
							"" + mname + " " + pid + " " + asha_id + " "
									+ rep_ind, Toast.LENGTH_SHORT).show();

					break;
				// case 2 :intent = new
				// Intent(getApplicationContext(),Preg_list.class);break;
				// case 3 :intent = new
				// Intent(getApplicationContext(),Home_visits_list.class);break;
				// case 4 :intent = new
				// Intent(getApplicationContext(),Home_visits_list.class);break;
				// case 5 :intent = new
				// Intent(getApplicationContext(),Creport.class);break;

				default:
					intent = new Intent(getApplicationContext(),
							Preg_list.class);
					break;
				}

			}
		});// addListenerOnButton();
	}

	/*
	 * private void addListenerOnButton() { // TODO Auto-generated method stub
	 * 
	 * 
	 * //btn.setTypeface(face); btn.setOnClickListener(new OnClickListener() {
	 * 
	 * public void onClick(View arg0) { // TODO Auto-generated method stub if
	 * (id==0) Toast.makeText(getApplicationContext(), "Select Beneficiary",
	 * Toast.LENGTH_LONG).show(); else { Context ctx=arg0.getContext(); Intent
	 * intent = new Intent(ctx,Preg_reg.class); intent.putExtra("id", id);
	 * intent.putExtra("pname", mname); intent.putExtra("hv_str", hv_str);
	 * intent.putExtra("seq", seq); intent.putExtra("pid", pid);
	 * intent.putExtra("bflag", true); intent.putExtra("lmp", lmp_str);
	 * 
	 * ctx.startActivity(intent); } }
	 * 
	 * });
	 * 
	 * 
	 * 
	 * 
	 * }
	 */

	/*
	 * protected void onListItemClick(ListView l, View v, int position, long id)
	 * {
	 * 
	 * //get selected items //String selectedValue = (String)
	 * getListAdapter().getItem(position); //Toast.makeText(this, selectedValue,
	 * Toast.LENGTH_SHORT).show();
	 * 
	 * //String[] val = selectedValue.split("~");
	 * 
	 * Intent k = new Intent(getApplicationContext(),Preg_reg.class);
	 * //k.putExtra("pid",val[0]);
	 * 
	 * startActivity(k);
	 * 
	 * }
	 */

	public void showAlert(String msg) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(alertMsg).setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// Some stuff to do when ok got clicked
					}
				})

				.show();
	}

	@Override
	public void onDestroy() {
		if (c != null)
			c.close();
		mydb.close();
		super.onDestroy();
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(Preg_list.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(Preg_list.this).reportActivityStop(this);
	}
}
