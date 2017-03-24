package com.intrahealth.appanm;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.intrahealth.msakhi.appanm.R;

public class AshaReportlist extends ListActivity {
	private DBAdapter mydb;
	TextView txtCount, txtCountR;
	// Typeface face;
	ListView lst;
	int asha_id = -1;
	int rep_ind = -1;
	Cursor c;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.report_asha);
		TextView tvHead = (TextView) findViewById(R.id.tvHead);

		txtCount = (TextView) findViewById(R.id.txtCount);
		txtCountR = (TextView) findViewById(R.id.txtCountR);
		TextView txtC3 = (TextView) findViewById(R.id.tvHeadC3);
		mydb = DBAdapter.getInstance(getApplicationContext());
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			rep_ind = extras.getInt("rep_ind");

		String cnt = "", cnt1 = "";
		switch (rep_ind) {
		case 8:
			c = mydb.getAshaReportP();
			cnt = "कुल गर्भवती : " + mydb.getAshaReportPcnt();
			cnt1 = "कुल रजिस्टर्ड : " + mydb.getAshaReportPRcnt();
			tvHead.setText("गर्भवती रिपोर्ट");
			break;
		case 9:
			c = mydb.getAshaReportB();
			cnt = "कुल जन्म : " + mydb.getAshaReportBcnt();
			cnt1 = "कुल रजिस्टर्ड : " + mydb.getAshaReportBRcnt();
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

		default:
			c = mydb.getAshaListP();
			cnt = mydb.getAshaListPcnt();
			break;
		}
		startManagingCursor(c);
		txtCount.setText(cnt);
		txtCountR.setText(cnt1);
		// the desired columns to be bound
		// asha_id,asha_dets.name,count(preg_reg._id) pcnt
		String[] from = new String[] { "_id", "name", "cnt1" };
		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.slno, R.id.name_entry, R.id.number_entry };

		// SimpleCursorAdapter ca=new
		// SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, c, new
		// String [] {DatabaseHelper.colDeptName}, new int
		// []{android.R.id.text1});
		LazyCursorAdapterReport ca = new LazyCursorAdapterReport(this,
				R.layout.asha_report_list_row, c, from, to);
		// ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		setListAdapter(ca);
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
				case 8:
					intent = new Intent(getApplicationContext(),
							Preg_list.class);
					break;
				case 9:
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
		asha_id = -1;
		super.onResume();
	}

}
