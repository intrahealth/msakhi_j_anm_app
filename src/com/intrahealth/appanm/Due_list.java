package com.intrahealth.appanm;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.intrahealth.msakhi.appanm.R;

public class Due_list extends ListActivity {
	private DBAdapter mydb;
	// TextView txtCount;
	// Typeface face;
	int id = 0, seq = 0, pid, pcaste, preligion, dstat = 0;
	Long pmobile;
	private int year;
	private int month;
	private int day, rep_ind = -1;
	static final int DATE_DIALOG_ID = 999;
	Button btnDate;
	ListView lst;
	String mydt, asha_id, phname;
	Cursor c;
	MediaPlayer mediaPlayer = new MediaPlayer();
	public String hv_str = "", mname = "";
	boolean learn = false, death = false, fvisit = false, current = true;
	Button btnNewTest;
	Button btnModBirth, btn;
	static int lastSel = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_visit_list);
		TextView tvHead = (TextView) findViewById(R.id.tvHead);
		TextView txtC3 = (TextView) findViewById(R.id.tvHeadC3);
		final Calendar clnd = Calendar.getInstance();
		year = clnd.get(Calendar.YEAR);
		month = clnd.get(Calendar.MONTH);
		day = clnd.get(Calendar.DAY_OF_MONTH);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			asha_id = extras.getString("asha_id");
			rep_ind = extras.getInt("rep_ind");

		}
		mydb = DBAdapter.getInstance(getApplicationContext());
		mydt = String.format("'%04d-%02d-%02d'", year, month + 1, day);
		// c=mydb.getPregVisits(mydt,asha_id);
		// Cursor c;
		switch (rep_ind) {
		//Changes all condition in Query Hero 
		case 0:
			c = mydb.getBcgList(asha_id);
			tvHead.setText("BCG Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 1:
			c = mydb.getDPT1List(asha_id);
			tvHead.setText("Dpt1 Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 2:
			c = mydb.getDPT2List(asha_id);
			tvHead.setText("Dpt2 Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 3:
			c = mydb.getDPT3List(asha_id);
			tvHead.setText("Dpt3 Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 4:
			c = mydb.getHepb1List(asha_id);
			tvHead.setText("Hep B1 Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 5:
			c = mydb.getHepb2List(asha_id);
			tvHead.setText("Hep B2 Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 6:
			c = mydb.getHepb3List(asha_id);
			tvHead.setText("Hep B3 Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 7:
			c = mydb.getMeasealsList(asha_id);
			tvHead.setText("Measeals Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 8:
			c = mydb.getVitaminaList(asha_id);
			tvHead.setText("Vitamina Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 9:
			c = mydb.getOpv1List(asha_id);
			tvHead.setText("Opv1 Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 10:
			c = mydb.getOpv2List(asha_id);
			tvHead.setText("Opv2 Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 11:
			c = mydb.getOpv3List(asha_id);
			tvHead.setText("Opv3 Due List");
			txtC3.setText("जन्म तिथि");
			break;
		case 12:
			c = mydb.getAnc1List(asha_id);
			tvHead.setText("ANC1 Due List");
			txtC3.setText("ई.डी.डी. (महिना)");
			break;
		case 13:
			c = mydb.getAnc2List(asha_id);
			tvHead.setText("ANC2 Due List");
			txtC3.setText("ई.डी.डी. (महिना)");
			break;
		case 14:
			c = mydb.getAnc3List(asha_id);
			tvHead.setText("ANC3 Due List");
			txtC3.setText("ई.डी.डी. (महिना)");
			break;
		case 15:
			c = mydb.getAnc4List(asha_id);
			tvHead.setText("ANC4 Due List");
			txtC3.setText("ई.डी.डी. (महिना)");
			break;

		}
		startManagingCursor(c);
		// the desired columns to be bound
		String[] from = new String[] { "_id", "name", "edd" };
		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.slno, R.id.name_entry, R.id.edd_entry };

		LazyCursorAdapterPreg ca = new LazyCursorAdapterPreg(this,
				R.layout.preg_list_row, c, from, to,0);
		setListAdapter(ca);
		ListView lst = (ListView) findViewById(android.R.id.list);

	}

	public void showAlert(String msg) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle("mSakhi").setMessage(msg)
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
}
