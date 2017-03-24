package com.intrahealth.appanm;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.intrahealth.appanm.GoogleAnalyticsApp.TrackerName;
import com.intrahealth.msakhi.appanm.R;

public class Asha_list extends ListActivity {
	private DBAdapter mydb;
	TextView txtCount;
	// Typeface face;
	ListView lst;
	int asha_id = -1;
	int rep_ind = -1;
	Cursor c;
	String tv_from ,tv_to;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.asha_list_layout);

		Tracker t = ((GoogleAnalyticsApp) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Asha Lists");
		t.send(new HitBuilders.AppViewBuilder().build());

		TextView tvHead = (TextView) findViewById(R.id.tvHead);
		// TextView tvHead=(TextView)findViewById(R.id.tvHead);
		// face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
		// tvHead.setTypeface(face);

		txtCount = (TextView) findViewById(R.id.txtCount);
		// TextView txtC1=(TextView)findViewById(R.id.tvHeadC1);
		// TextView txtC2=(TextView)findViewById(R.id.tvHeadC2);
		TextView txtC3 = (TextView) findViewById(R.id.tvHeadC3);
		mydb = DBAdapter.getInstance(getApplicationContext());
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			rep_ind = extras.getInt("rep_ind");
		if(rep_ind==11||rep_ind==12){
			tv_from = extras.getString("from");
			tv_to = extras.getString("to");
		}
		String cnt = "";
		switch (rep_ind) {
		case 0:
			c = mydb.getAshaListP();
			cnt = "कुल गर्भवती : " + mydb.getAshaListPcnt();
			txtC3.setText("गर्भवती (संख्या)");
			tvHead.setText("गर्भवती महिलाएं");
			break;
		case 1:
			c = mydb.getAshaListB();
			cnt = "कुल जन्म : " + mydb.getAshaListBcnt();
			txtC3.setText("जन्म(संख्या)");
			tvHead.setText("जन्म सूची");
			break;
		case 2:
			c = mydb.getAshaListD();
			cnt = "कुल मृत्यु : " + mydb.getAshaListDcnt();
			txtC3.setText(Html
					.fromHtml("मृत्यु <font color=#8DB600>(बच्चा/</font> <font color=0xff00ff>माँ</font>)"));
			tvHead.setText("मृत्यु सूची");
			break;
		case 3:
			c = mydb.getOpenHVList();
			txtC3.setText(Html
					.fromHtml("गृह भ्रमण<font color=#8DB600>(कुल/</font><font color=0xff00ff>लक्ष्य/</font><font color=#007FFF>प्राप्ति)</font>"));
			tvHead.setText("चालू गृह भ्रमण");
			break;
		case 4:
			c = mydb.getCloseHVList();
			txtC3.setText(Html
					.fromHtml("गृह भ्रमण<font color=#8DB600>(कुल/</font><font color=0xff00ff>लक्ष्य/</font><font color=#007FFF>प्राप्ति)</font>"));
			tvHead.setText("समाप्त गृह भ्रमण");
			break;
		case 5:
			c = mydb.getAshaListB();
			cnt = "कुल जन्म :" + mydb.getAshaListBcnt();
			tvHead.setText("शिशु रिपोर्ट");
			txtC3.setText("जन्म(संख्या)");
			break;

		case 6:
			c = mydb.getAshaListVP();
			cnt = "कुल गर्भवती : " + mydb.getAshaListVPcnt();
			txtC3.setText("गर्भवती (संख्या)");
			tvHead.setText("गर्भवती महिलाएं");
			break;

		case 7:
			c = mydb.getAshaListVB();
			cnt = "कुल जन्म : " + mydb.getAshaListVBcnt();
			txtC3.setText("जन्म(संख्या)");
			tvHead.setText("जन्म सूची");
			break;
		case 11:
			c = mydb.getAshaListPreg(tv_from,tv_to);
			txtC3.setText("गर्भवती (संख्या)");
			tvHead.setText("गर्भवती पंजीकृत महिला");
			break;
		case 12:
			c = mydb.getAshaListnewchild(tv_from,tv_to);
			txtC3.setText("नए बच्चे (संख्या)");
			tvHead.setText("नए बच्चे पंजीकृत");
			break;

		default:
			c = mydb.getAshaListP();
			cnt = mydb.getAshaListPcnt();
			break;
		}
		startManagingCursor(c);
		txtCount.setText(cnt);
		// the desired columns to be bound
		// asha_id,asha_dets.name,count(preg_reg._id) pcnt
		String[] from = new String[] { "_id", "name", "cnt" };
		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.slno, R.id.name_entry, R.id.number_entry };

		// SimpleCursorAdapter ca=new
		// SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, c, new
		// String [] {DatabaseHelper.colDeptName}, new int
		// []{android.R.id.text1});
		if (rep_ind == 6) {
			Log.d("enter when value is", rep_ind + "");
			test ca = new test(this, R.layout.asha_list_row, c, from, to);
			Log.d("before setting adapter", rep_ind + "");
			setListAdapter(ca);
			Log.d("after setting adapter", rep_ind + "");
		} else {
			LazyCursorAdapter ca = new LazyCursorAdapter(this,
					R.layout.asha_list_row, c, from, to);
			setListAdapter(ca);
		}
		// ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// setListAdapter(ca);
		lst = (ListView) findViewById(android.R.id.list);
		lst.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View v,
					int position, long arg3) {
				Cursor cursor = (Cursor) adapterView
						.getItemAtPosition(position);
				int key_id = cursor.getInt(cursor.getColumnIndex("_id"));
				asha_id = key_id;
				Toast.makeText(getApplicationContext(),
						"" + key_id + " " + rep_ind, Toast.LENGTH_SHORT).show();
				Intent intent;
				// 0 - Pregenant list 1 - Birth 2 - Death
				switch (rep_ind) {
				case 0:
					intent = new Intent(getApplicationContext(),
							Preg_list.class);
					break;
				case 1:
					intent = new Intent(getApplicationContext(),
							Preg_list.class);
					break;
				case 2:
					intent = new Intent(getApplicationContext(),
							Preg_list.class);
					break;
				case 3:
					intent = new Intent(getApplicationContext(),
							Home_visits_list.class);
					break;
				case 4:
					intent = new Intent(getApplicationContext(),
							Home_visits_list.class);
					break;
				case 5:
					intent = new Intent(getApplicationContext(), Creport.class);
					break;
				case 6:
					intent = new Intent(getApplicationContext(),
							Preg_list.class);
					break;
				case 7:
					intent = new Intent(getApplicationContext(),
							Preg_list.class);
					break;
				case 11:
					intent = new Intent(getApplicationContext(),
							Preg_list.class);
					intent.putExtra("from", tv_from);
					intent.putExtra("to", tv_to);
					break;
				case 12:
					intent = new Intent(getApplicationContext(),
							Preg_list.class);
					intent.putExtra("from", tv_from);
					intent.putExtra("to", tv_to);
					break;
				default:
					intent = new Intent(getApplicationContext(),
							Preg_list.class);
					break;
				}
				intent.putExtra("asha_id", key_id);
				intent.putExtra("rep_ind", rep_ind);
				startActivity(intent);

				// return true;
			}
		});

		if (rep_ind == 10) {

			c = mydb.getAshaListDue();
			txtC3.setVisibility(android.view.View.GONE);
			tvHead.setText("महिलाएं");

			c = mydb.getAshaListDue();
			cnt = mydb.getAshaListPcnt();

			startManagingCursor(c);
			txtCount.setText(cnt);
			// the desired columns to be bound
			// asha_id,asha_dets.name,count(preg_reg._id) pcnt
			String[] from1 = new String[] { "_id", "name", "cnt" };
			// the XML defined views which the data will be bound to
			int[] to1 = new int[] { R.id.slno, R.id.name_entry,
					R.id.number_entry };

			// SimpleCursorAdapter ca=new
			// SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, c,
			// new String [] {DatabaseHelper.colDeptName}, new int
			// []{android.R.id.text1});
			LazyCursorAdapterdue ca1 = new LazyCursorAdapterdue(this,
					R.layout.asha_list_row, c, from1, to1);
			// ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			setListAdapter(ca1);
			lst = (ListView) findViewById(android.R.id.list);
			lst.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> adapterView, View v,
						int position, long arg3) {
					Cursor cursor = (Cursor) adapterView
							.getItemAtPosition(position);
					int key_id = cursor.getInt(cursor.getColumnIndex("_id"));
					asha_id = key_id;
					Toast.makeText(getApplicationContext(),
							"" + key_id + " " + rep_ind, Toast.LENGTH_SHORT)
							.show();
					Intent intent;
					// 0 - Pregenant list 1 - Birth 2 - Death

					intent = new Intent(getApplicationContext(),
							Vaccine_due.class);
					intent.putExtra("asha_id", key_id);
					intent.putExtra("rep_ind", rep_ind);
					startActivity(intent);

					// return true;
				}
			});

		}
	}

	@Override
	public void onDestroy() {
		if (c != null)
			c.close();
		mydb.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		try {
			asha_id = -1;
			super.onResume();
		} catch (Exception e) {

		}

	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(Asha_list.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(Asha_list.this).reportActivityStop(this);
	}

}
