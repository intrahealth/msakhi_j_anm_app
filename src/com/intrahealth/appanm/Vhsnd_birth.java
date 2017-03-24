package com.intrahealth.appanm;

//import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.intrahealth.msakhi.appanm.R;

public class Vhsnd_birth extends Activity {

	EditText pg_name, child_name, hname;
	TextView bcg, opv1, dpt1, hepatitisb1, opv2, dpt2, hepatitisb2, opv3, dpt3,
			hepatitisb3, measles, vitamina, dob, mcts;
	String strbcg = null, stropv1 = null, strdpt1 = null,
			strhepatitisb1 = null, stropv2 = null, strdpt2 = null,
			strhepatitisb2 = null, stropv3 = null, strdpt3 = null,
			strhepatitisb3 = null, strmeasles = null, strvitamina = null;
	Button date_bcg, date_opv1, date_dpt1, date_hepatitisb1, date_opv2,
			date_dpt2, date_hepatitisb2, date_opv3, date_dpt3,
			date_hepatitisb3, date_measles, date_vitamina, submit;
	RadioGroup immunization;
	String asha_id, id;
	RadioButton radiobutton;
	ToggleButton toggleButton1;
	private DBAdapter mydb;
	Cursor c;
	JSONObject payload = new JSONObject();
	Date dtbcg, dtopv1, dtdpt1, dthepatitisb1, dtopv2, dtdpt2, dthepatitisb2,
			dtopv3, dtdpt3, dthepatitisb3, dtmeasles, dtvitamina, dtdob,
			dtcdate;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");

	private int year;
	private int month;
	private int day;
	private int yearmc;
	private int monthmc;
	private int daymc;
	private int year1;
	private int month1;
	private int day1;
	private int year2;
	private int month2;
	private int day2;
	private int year3;
	private int month3;
	private int day3;
	private int year4;
	private int month4;
	private int day4;
	private int year5;
	private int month5;
	private int day5;
	private int year6;
	private int month6;
	private int day6;
	private int year7;
	private int month7;
	private int day7;
	private int year8;
	private int month8;
	private int day8;
	private int year9;
	private int month9;
	private int day9;
	private int year10;
	private int month10;
	private int day10;
	private int year11;
	private int month11;
	private int day11;

	static final int DATE_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID1 = 1;
	static final int DATE_DIALOG_ID2 = 2;
	static final int DATE_DIALOG_ID3 = 3;
	static final int DATE_DIALOG_ID4 = 4;
	static final int DATE_DIALOG_ID5 = 5;
	static final int DATE_DIALOG_ID6 = 6;
	static final int DATE_DIALOG_ID7 = 7;
	static final int DATE_DIALOG_ID8 = 8;
	static final int DATE_DIALOG_ID9 = 9;
	static final int DATE_DIALOG_ID10 = 10;
	static final int DATE_DIALOG_ID11 = 11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vhsnd_child);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			asha_id = extras.getString("asha_id");
			id = String.valueOf(extras.getInt("pid"));
			// pname=extras.getString("pname");
			// showAlert(asha_id+" "+id);

		}

		final Calendar cal = Calendar.getInstance();
		yearmc = cal.get(Calendar.YEAR);
		monthmc = cal.get(Calendar.MONTH);
		daymc = cal.get(Calendar.DAY_OF_MONTH);
		try {
			dtcdate = formatter.parse(new StringBuilder().append(yearmc)
					.append("-").append(monthmc + 1).append("-").append(daymc)
					.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pg_name = (EditText) findViewById(R.id.pg_name);
		child_name = (EditText) findViewById(R.id.child_name);
		hname = (EditText) findViewById(R.id.hname);
		bcg = (TextView) findViewById(R.id.bcg);
		opv1 = (TextView) findViewById(R.id.opv1);
		dpt1 = (TextView) findViewById(R.id.dpt1);
		hepatitisb1 = (TextView) findViewById(R.id.hepatitisb1);
		opv2 = (TextView) findViewById(R.id.opv2);
		dpt2 = (TextView) findViewById(R.id.dpt2);
		hepatitisb2 = (TextView) findViewById(R.id.hepatitisb2);
		opv3 = (TextView) findViewById(R.id.opv3);
		dpt3 = (TextView) findViewById(R.id.dpt3);
		hepatitisb3 = (TextView) findViewById(R.id.hepatitisb3);
		measles = (TextView) findViewById(R.id.measles);
		vitamina = (TextView) findViewById(R.id.vitamina);
		immunization = (RadioGroup) findViewById(R.id.immunization);
		dob = (TextView) findViewById(R.id.dob);
		mcts = (TextView) findViewById(R.id.mcts);

		date_bcg = (Button) findViewById(R.id.date_bcg);
		date_opv1 = (Button) findViewById(R.id.date_opv1);
		date_dpt1 = (Button) findViewById(R.id.date_dpt1);
		date_hepatitisb1 = (Button) findViewById(R.id.date_hepatitisb1);
		date_opv2 = (Button) findViewById(R.id.date_opv2);
		date_dpt2 = (Button) findViewById(R.id.date_dpt2);
		date_hepatitisb2 = (Button) findViewById(R.id.date_hepatitisb2);
		date_opv3 = (Button) findViewById(R.id.date_opv3);
		date_dpt3 = (Button) findViewById(R.id.date_dpt3);
		date_hepatitisb3 = (Button) findViewById(R.id.date_hepatitisb3);
		date_measles = (Button) findViewById(R.id.date_measles);
		date_vitamina = (Button) findViewById(R.id.date_vitamina);

		// date_opv2.setVisibility(android.view.View.GONE);
		// date_dpt2.setVisibility(android.view.View.GONE);
		// date_hepatitisb2.setVisibility(android.view.View.GONE);
		// date_opv3.setVisibility(android.view.View.GONE);
		// date_dpt3.setVisibility(android.view.View.GONE);
		// date_hepatitisb3.setVisibility(android.view.View.GONE);
		// date_measles.setVisibility(android.view.View.GONE);
		// date_vitamina.setVisibility(android.view.View.GONE);

		setCurrentDateOnView();
		addListenerOnButton();
		setCurrentDateOnView1();
		addListenerOnButton1();
		setCurrentDateOnView2();
		addListenerOnButton2();
		setCurrentDateOnView3();
		addListenerOnButton3();
		setCurrentDateOnView4();
		addListenerOnButton4();
		setCurrentDateOnView5();
		addListenerOnButton5();
		setCurrentDateOnView6();
		addListenerOnButton6();
		setCurrentDateOnView7();
		addListenerOnButton7();
		setCurrentDateOnView8();
		addListenerOnButton8();
		setCurrentDateOnView9();
		addListenerOnButton9();
		setCurrentDateOnView10();
		addListenerOnButton10();
		setCurrentDateOnView11();
		addListenerOnButton11();
		mydb = DBAdapter.getInstance(getApplicationContext());

		c = mydb.getVhsndChild(asha_id, id);
		if (c.moveToFirst()) {

			child_name.setText(c.getString(c.getColumnIndex("child_name")));
			bcg.setText(DBAdapter.strtodate(c.getString(c.getColumnIndex("bcg"))));
			opv1.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("opv1"))));
			dpt1.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("dpt1"))));
			hepatitisb1.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("hepb1"))));
			opv2.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("opv2"))));
			dpt2.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("dpt2"))));
			hepatitisb2.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("hepb2"))));
			opv3.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("opv3"))));
			dpt3.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("dpt3"))));
			hepatitisb3.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("hepb3"))));
			measles.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("measeals"))));
			vitamina.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("vitamina"))));

			Log.e("MCTS NUMBER",
					"Nummm" + c.getString(c.getColumnIndex("child_mcts")));

			mcts.setText("MCTS ID: "
					+ c.getString(c.getColumnIndex("child_mcts")));
			if (!c.isNull(c.getColumnIndex("bcg"))) {
				strbcg = c.getString(c.getColumnIndex("bcg"));
				//date_bcg.setVisibility(android.view.View.GONE);//Hero Comment
				date_bcg.setVisibility(android.view.View.VISIBLE);//Hero Add
				try {
					dtbcg = formatter
							.parse(c.getString(c.getColumnIndex("bcg")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("opv1"))) {
				stropv1 = c.getString(c.getColumnIndex("opv1"));
				// date_opv1.setVisibility(android.view.View.GONE);
				date_opv2.setVisibility(android.view.View.VISIBLE);
				try {
					dtopv1 = formatter.parse(c.getString(c
							.getColumnIndex("opv1")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("dpt1"))) {
				strdpt1 = c.getString(c.getColumnIndex("dpt1"));
				// date_dpt1.setVisibility(android.view.View.GONE);
				date_dpt2.setVisibility(android.view.View.VISIBLE);
				try {
					dtdpt1 = formatter.parse(c.getString(c
							.getColumnIndex("dpt1")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("hepb1"))) {
				strhepatitisb1 = c.getString(c.getColumnIndex("hepb1"));
				// date_hepatitisb1.setVisibility(android.view.View.GONE);
				date_hepatitisb2.setVisibility(android.view.View.VISIBLE);
				try {
					dthepatitisb1 = formatter.parse(c.getString(c
							.getColumnIndex("hepb1")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("opv2"))) {
				stropv2 = c.getString(c.getColumnIndex("opv2"));
				// date_opv2.setVisibility(android.view.View.GONE);
				date_opv3.setVisibility(android.view.View.VISIBLE);
				try {
					dtopv2 = formatter.parse(c.getString(c
							.getColumnIndex("opv2")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("dpt2"))) {
				strdpt2 = c.getString(c.getColumnIndex("dpt2"));
				// date_dpt2.setVisibility(android.view.View.GONE);
				date_dpt3.setVisibility(android.view.View.VISIBLE);
				try {
					dtdpt2 = formatter.parse(c.getString(c
							.getColumnIndex("dpt2")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("hepb2"))) {
				strhepatitisb2 = c.getString(c.getColumnIndex("hepb2"));
				// date_hepatitisb2.setVisibility(android.view.View.GONE);
				date_hepatitisb3.setVisibility(android.view.View.VISIBLE);
				try {
					dthepatitisb2 = formatter.parse(c.getString(c
							.getColumnIndex("hepb2")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("opv3"))) {
				stropv3 = c.getString(c.getColumnIndex("opv3"));
				// date_opv3.setVisibility(android.view.View.GONE);
				date_measles.setVisibility(android.view.View.VISIBLE);
				date_vitamina.setVisibility(android.view.View.VISIBLE);
				try {
					dtopv3 = formatter.parse(c.getString(c
							.getColumnIndex("opv3")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("dpt3"))) {
				strdpt3 = c.getString(c.getColumnIndex("dpt3"));
				// date_dpt3.setVisibility(android.view.View.GONE);
				try {
					dtdpt3 = formatter.parse(c.getString(c
							.getColumnIndex("dpt3")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("hepb3"))) {
				strhepatitisb3 = c.getString(c.getColumnIndex("hepb3"));
				// date_hepatitisb3.setVisibility(android.view.View.GONE);
				try {
					dthepatitisb3 = formatter.parse(c.getString(c
							.getColumnIndex("hepb3")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("measeals"))) {
				strmeasles = c.getString(c.getColumnIndex("measeals"));
				// date_measles.setVisibility(android.view.View.GONE);
				try {
					dtmeasles = formatter.parse(c.getString(c
							.getColumnIndex("measeals")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("vitamina"))) {
				strvitamina = c.getString(c.getColumnIndex("vitamina"));
				// date_vitamina.setVisibility(android.view.View.GONE);
				try {
					dtvitamina = formatter.parse(c.getString(c
							.getColumnIndex("measeals")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		c = mydb.getAnmPreg(asha_id, id);
		if (c.moveToFirst()) {

			pg_name.setText(c.getString(c.getColumnIndex("name")));
			hname.setText(c.getString(c.getColumnIndex("hname")));
			dob.setText("DOB: "
					+ DBAdapter.strtodate(c.getString(c.getColumnIndex("dob"))));
			try {
				dtdob = formatter.parse(c.getString(c.getColumnIndex("dob")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (child_name.getText().toString().isEmpty()) {
					showAlert("बच्चे का नाम डाले");

				} else if (pg_name.getText().toString().isEmpty()) {
					showAlert("माँ का नाम डाले");
				} else if (hname.getText().toString().isEmpty()) {
					showAlert("पिता का नाम डाले");
				} else {
					mydb.vhsndbirth(asha_id, id, child_name.getText()
							.toString(), strbcg, stropv1, strdpt1,
							strhepatitisb1, stropv2, strdpt2, strhepatitisb2,
							stropv3, strdpt3, strhepatitisb3, strmeasles,
							strvitamina);

					Toast.makeText(getApplicationContext(), "जानकारी बदल गयी",
							Toast.LENGTH_SHORT).show();
					try {

						c = mydb.getUpdatePrego(asha_id, id);
						if (c.moveToFirst()) {
							payload.put("aid", asha_id);
							payload.put("id", id);
							Log.e("ASHA ID", asha_id);
							/*
							 * payload.put("child_name",
							 * child_name.getText().toString());
							 * payload.put("bcg", strbcg); payload.put("opv1",
							 * stropv1); payload.put("dpt1", strdpt1);
							 * payload.put("hepb1", strhepatitisb1);
							 * payload.put("opv2", stropv2); payload.put("dpt2",
							 * strdpt2); payload.put("hepb2", strhepatitisb2);
							 * payload.put("opv3", stropv3); payload.put("dpt3",
							 * strdpt3); payload.put("hepb3", strhepatitisb3);
							 * payload.put("measeals", strmeasles);
							 * payload.put("vitamina", strvitamina);
							 */
							payload.put("mother_mcts", c.getString(c
									.getColumnIndex("mother_mcts")));
							payload.put("aadhar_card", c.getString(c
									.getColumnIndex("aadhar_card")));
							payload.put("bank_name",
									c.getString(c.getColumnIndex("bank_name")));
							payload.put("branch",
									c.getString(c.getColumnIndex("branch")));
							payload.put("jsy",
									c.getString(c.getColumnIndex("jsy")));
							payload.put("bpl",
									c.getString(c.getColumnIndex("bpl")));
							payload.put("age",
									c.getString(c.getColumnIndex("age")));
							payload.put("reg_date",
									c.getString(c.getColumnIndex("reg_date")));
							payload.put("month_reg",
									c.getString(c.getColumnIndex("month_reg")));
							payload.put("week_reg",
									c.getString(c.getColumnIndex("week_reg")));
							payload.put("mother_weight", c.getString(c
									.getColumnIndex("mother_weight")));
							payload.put("blood_group", c.getString(c
									.getColumnIndex("blood_group")));
							payload.put("past_health", c.getString(c
									.getColumnIndex("past_health")));
							payload.put("total_preg",
									c.getString(c.getColumnIndex("total_preg")));
							payload.put("lastpreg_result", c.getString(c
									.getColumnIndex("lastpreg_result")));
							payload.put("lastpreg_comp", c.getString(c
									.getColumnIndex("lastpreg_comp")));
							payload.put("ltolpreg_result", c.getString(c
									.getColumnIndex("ltolpreg_result")));
							payload.put("ltolpreg_comp", c.getString(c
									.getColumnIndex("ltolpreg_comp")));
							payload.put("delivery_plan", c.getString(c
									.getColumnIndex("delivery_plan")));
							payload.put("vdrl",
									c.getString(c.getColumnIndex("vdrl")));
							payload.put("hiv",
									c.getString(c.getColumnIndex("hiv")));
							payload.put("visit1_date", c.getString(c
									.getColumnIndex("visit1_date")));
							payload.put("visit2_date", c.getString(c
									.getColumnIndex("visit2_date")));
							payload.put("visit3_date", c.getString(c
									.getColumnIndex("visit3_date")));
							payload.put("visit4_date", c.getString(c
									.getColumnIndex("visit4_date")));
							payload.put("abortion",
									c.getString(c.getColumnIndex("abortion")));
							payload.put("tt1_date",
									c.getString(c.getColumnIndex("tt1_date")));
							payload.put("tt2_date",
									c.getString(c.getColumnIndex("tt2_date")));
							payload.put("ttbooster_date", c.getString(c
									.getColumnIndex("ttbooster_date")));
							payload.put("reffered",
									c.getString(c.getColumnIndex("reffered")));
							payload.put("danger_signs", c.getString(c
									.getColumnIndex("danger_signs")));
							payload.put("anc_visit",
									c.getString(c.getColumnIndex("anc_visit")));
							payload.put("child_mcts",
									c.getString(c.getColumnIndex("child_mcts")));
							payload.put("breastfeeding", c.getString(c
									.getColumnIndex("breastfeeding")));
							payload.put("child_name", child_name.getText()
									.toString());
							payload.put("bcg", strbcg);
							payload.put("opv1", stropv1);
							payload.put("dpt1", strdpt1);
							payload.put("hepb1", strhepatitisb1);
							payload.put("opv2", stropv2);
							payload.put("dpt2", strdpt2);
							payload.put("hepb2", strhepatitisb2);
							payload.put("opv3", stropv3);
							payload.put("dpt3", strdpt3);
							payload.put("hepb3", strhepatitisb3);
							payload.put("measeals", strmeasles);
							payload.put("vitamina", strvitamina);
						}
						System.out.print(payload.toString());
						mydb.sendGPRS("uprego", payload.toString(), 1);
						// Toast.makeText(getApplicationContext(),payload.toString(),
						// Toast.LENGTH_SHORT).show();
						// Toast.makeText(getApplicationContext(),
						// "Successfully uploaded", Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						Log.d("error", e.getMessage());
						Toast.makeText(getApplicationContext(), e.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
					finish();

				}
			}
		});

	}

	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {

		}

	}

	public void setCurrentDateOnView() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		// bcg.setText(new StringBuilder().append(day).append("-").append(month
		// + 1).append("-").append(year).append(" "));

	}

	public void addListenerOnButton() {

		date_bcg = (Button) findViewById(R.id.date_bcg);

		date_bcg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID);

			}

		});

		bcg = (TextView) findViewById(R.id.bcg);

		bcg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID);

			}

		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		case DATE_DIALOG_ID1:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener1, year1,
					month1, day1);
		case DATE_DIALOG_ID2:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener2, year2,
					month2, day2);
		case DATE_DIALOG_ID3:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener3, year3,
					month3, day3);
		case DATE_DIALOG_ID4:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener4, year4,
					month4, day4);
		case DATE_DIALOG_ID5:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener5, year5,
					month5, day5);
		case DATE_DIALOG_ID6:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener6, year6,
					month6, day6);
		case DATE_DIALOG_ID7:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener7, year7,
					month7, day7);
		case DATE_DIALOG_ID8:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener8, year8,
					month8, day8);

		case DATE_DIALOG_ID9:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener9, year9,
					month9, day9);
		case DATE_DIALOG_ID10:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener10, year10,
					month10, day10);
		case DATE_DIALOG_ID11:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener11, year11,
					month11, day11);

		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			Calendar c = Calendar.getInstance();
			c.set(selectedYear, selectedMonth, selectedDay);
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			Calendar mc = Calendar.getInstance();

			try {
				dtbcg = formatter.parse(new StringBuilder().append(year)
						.append("-").append(month + 1).append("-").append(day)
						.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (c.after(mc) || dtbcg.before(dtdob)) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {

				bcg.setText(String.format("%d-%s-%d", day,
						DBAdapter.hmstr[month], year));
				strbcg = new StringBuilder().append(year).append("-")
						.append(month + 1).append("-").append(day).toString();

			}

		}
	};

	// **************** For I checkup Date ******************

	public void setCurrentDateOnView1() {

		final Calendar c = Calendar.getInstance();
		year1 = c.get(Calendar.YEAR);
		month1 = c.get(Calendar.MONTH);
		day1 = c.get(Calendar.DAY_OF_MONTH);

	}

	public void addListenerOnButton1() {

		date_opv1 = (Button) findViewById(R.id.date_opv1);

		date_opv1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID1);

			}

		});
		opv1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID1);
			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear1,
				int selectedMonth1, int selectedDay1) {
			year1 = selectedYear1;
			month1 = selectedMonth1;
			day1 = selectedDay1;

			try {
				dtopv1 = formatter.parse(new StringBuilder().append(year1)
						.append("-").append(month1 + 1).append("-")
						.append(day1).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math.abs(dtopv1.getTime() - dtdob.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);

			if (dtopv1.before(dtdob) || diff_current < 44) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				opv1.setText(String.format("%d-%s-%d", day1,
						DBAdapter.hmstr[month1], year1));
				stropv1 = new StringBuilder().append(year1).append("-")
						.append(month1 + 1).append("-").append(day1).toString();
			}

		}
	};

	// ****************************************************************************************

	// *******************For II checkup
	// date**************************************************

	public void setCurrentDateOnView2() {

		final Calendar c = Calendar.getInstance();
		year2 = c.get(Calendar.YEAR);
		month2 = c.get(Calendar.MONTH);
		day2 = c.get(Calendar.DAY_OF_MONTH);

		// set current date into textview

		// checkup_date2.setText(new
		// StringBuilder().append(day2).append("-").append(month2 +
		// 1).append("-").append(year2).append(" "));

	}

	public void addListenerOnButton2() {

		date_dpt1 = (Button) findViewById(R.id.date_dpt1);

		date_dpt1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID2);

			}

		});
		dpt1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID2);

			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear2,
				int selectedMonth2, int selectedDay2) {
			year2 = selectedYear2;
			month2 = selectedMonth2;
			day2 = selectedDay2;
			try {
				dtdpt1 = formatter.parse(new StringBuilder().append(year2)
						.append("-").append(month2 + 1).append("-")
						.append(day2).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math.abs(dtdpt1.getTime() - dtdob.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);
			// showAlert(String.valueOf(diff_current));
			if (dtdpt1.before(dtdob) || diff_current < 44) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				// dpt1.setText(new
				// StringBuilder().append(year2).append("-").append(month2 +
				// 1).append("-").append(day2));
				dpt1.setText(String.format("%d-%s-%d", day2,
						DBAdapter.hmstr[month2], year2));
				strdpt1 = new StringBuilder().append(year2).append("-")
						.append(month2 + 1).append("-").append(day2).toString();

			}
		}
	};

	// ***********************************************************************************************************************************************************

	// *************************************For III Check UP date
	// ************************************************************************************************

	public void setCurrentDateOnView3() {

		final Calendar c = Calendar.getInstance();
		year3 = c.get(Calendar.YEAR);
		month3 = c.get(Calendar.MONTH);
		day3 = c.get(Calendar.DAY_OF_MONTH);

		// checkup_date3.setText(new
		// StringBuilder().append(day3).append("-").append(month3 +
		// 1).append("-").append(year3).append(" "));

	}

	public void addListenerOnButton3() {

		date_hepatitisb1 = (Button) findViewById(R.id.date_hepatitisb1);

		date_hepatitisb1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID3);

			}

		});
		hepatitisb1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID3);

			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener3 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear3,
				int selectedMonth3, int selectedDay3) {
			year3 = selectedYear3;
			month3 = selectedMonth3;
			day3 = selectedDay3;
			try {
				dthepatitisb1 = formatter.parse(new StringBuilder()
						.append(year3).append("-").append(month3 + 1)
						.append("-").append(day3).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math
					.abs(dthepatitisb1.getTime() - dtdob.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);
			// showAlert(String.valueOf(diff_current));
			if (dthepatitisb1.before(dtdob) || diff_current < 44) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {

				// hepatitisb1.setText(new
				// StringBuilder().append(year3).append("-").append(month3 +
				// 1).append("-").append(day3));
				hepatitisb1.setText(String.format("%d-%s-%d", day3,
						DBAdapter.hmstr[month3], year3));
				strhepatitisb1 = new StringBuilder().append(year3).append("-")
						.append(month3 + 1).append("-").append(day3).toString();
			}

		}
	};

	// ****************************************************************************************************************************

	// ********************************IV checkup
	// date****************************************************************************
	public void setCurrentDateOnView4() {

		final Calendar c = Calendar.getInstance();
		year4 = c.get(Calendar.YEAR);
		month4 = c.get(Calendar.MONTH);
		day4 = c.get(Calendar.DAY_OF_MONTH);

		// checkup_date4.setText(new
		// StringBuilder().append(day4).append("-").append(month4 +
		// 1).append("-").append(year4).append(" "));

	}

	public void addListenerOnButton4() {

		date_opv2 = (Button) findViewById(R.id.date_opv2);

		date_opv2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID4);

			}

		});
		opv2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID4);

			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener4 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear4,
				int selectedMonth4, int selectedDay4) {
			year4 = selectedYear4;
			month4 = selectedMonth4;
			day4 = selectedDay4;

			try {
				dtopv2 = formatter.parse(new StringBuilder().append(year4)
						.append("-").append(month4 + 1).append("-")
						.append(day4).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math.abs(dtopv2.getTime() - dtopv1.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);
			// showAlert(String.valueOf(diff_current));
			if (dtopv2.before(dtopv1) || diff_current < 28) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getBaseContext(), "hmmmmm", Toast.LENGTH_SHORT)
						.show();
				opv2.setText(String.format("%d-%s-%d", day4,
						DBAdapter.hmstr[month4], year4));
				stropv2 = new StringBuilder().append(year4).append("-")
						.append(month4 + 1).append("-").append(day4).toString();
			}

		}
	};

	// *************************************************************************************************************************************************************

	// ********************************I TT
	// date********************************************************************************************************************
	public void setCurrentDateOnView5() {

		final Calendar c = Calendar.getInstance();
		year5 = c.get(Calendar.YEAR);
		month5 = c.get(Calendar.MONTH);
		day5 = c.get(Calendar.DAY_OF_MONTH);

		// ttdate1.setText(new
		// StringBuilder().append(day5).append("-").append(month5 +
		// 1).append("-").append(year5).append(" "));

	}

	public void addListenerOnButton5() {

		date_dpt2 = (Button) findViewById(R.id.date_dpt2);

		date_dpt2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID5);

			}

		});
		dpt2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID5);

			}

		});
	}

	private DatePickerDialog.OnDateSetListener datePickerListener5 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear5,
				int selectedMonth5, int selectedDay5) {
			year5 = selectedYear5;
			month5 = selectedMonth5;
			day5 = selectedDay5;
			try {
				dtdpt2 = formatter.parse(new StringBuilder().append(year5)
						.append("-").append(month5 + 1).append("-")
						.append(day5).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math.abs(dtdpt2.getTime() - dtdpt1.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);
			// showAlert(String.valueOf(diff_current));
			if (dtdpt2.before(dtdpt1) || diff_current < 28) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				// dpt2.setText(new
				// StringBuilder().append(year5).append("-").append(month5 +
				// 1).append("-").append(day5));
				dpt2.setText(String.format("%d-%s-%d", day5,
						DBAdapter.hmstr[month5], year5));
				strdpt2 = new StringBuilder().append(year5).append("-")
						.append(month5 + 1).append("-").append(day5).toString();
			}

		}
	};

	// *************************************************************************************************************************************************************

	// ********************************II TT
	// date****************************************************************************

	public void setCurrentDateOnView6() {

		final Calendar c = Calendar.getInstance();
		year6 = c.get(Calendar.YEAR);
		month6 = c.get(Calendar.MONTH);
		day6 = c.get(Calendar.DAY_OF_MONTH);

		// ttdate2.setText(new
		// StringBuilder().append(day6).append("-").append(month6 +
		// 1).append("-").append(year6).append(" "));

	}

	public void addListenerOnButton6() {

		date_hepatitisb2 = (Button) findViewById(R.id.date_hepatitisb2);

		date_hepatitisb2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID6);

			}

		});
		hepatitisb2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID6);

			}

		});
	}

	private DatePickerDialog.OnDateSetListener datePickerListener6 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear6,
				int selectedMonth6, int selectedDay6) {
			year6 = selectedYear6;
			month6 = selectedMonth6;
			day6 = selectedDay6;
			try {
				dthepatitisb2 = formatter.parse(new StringBuilder()
						.append(year6).append("-").append(month6 + 1)
						.append("-").append(day6).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math.abs(dthepatitisb2.getTime()
					- dthepatitisb1.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);
			// showAlert(String.valueOf(diff_current));
			if (dthepatitisb2.before(dthepatitisb1) || diff_current < 28) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				// hepatitisb2.setText(new
				// StringBuilder().append(year6).append("-").append(month6 +
				// 1).append("-").append(day6));
				hepatitisb2.setText(String.format("%d-%s-%d", day6,
						DBAdapter.hmstr[month6], year6));
				strhepatitisb2 = new StringBuilder().append(year6).append("-")
						.append(month6 + 1).append("-").append(day6).toString();

			}

		}
	};

	// *************************************************************************************************************************************************************

	// ********************************Miscarriage
	// date****************************************************************************

	public void setCurrentDateOnView7() {

		final Calendar c = Calendar.getInstance();
		year7 = c.get(Calendar.YEAR);
		month7 = c.get(Calendar.MONTH);
		day7 = c.get(Calendar.DAY_OF_MONTH);

		// miscarriage_date.setText(new
		// StringBuilder().append(day7).append("-").append(month7 +
		// 1).append("-").append(year7).append(" "));

	}

	public void addListenerOnButton7() {

		date_opv3 = (Button) findViewById(R.id.date_opv3);

		date_opv3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID7);

			}

		});
		opv3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID7);

			}

		});
	}

	private DatePickerDialog.OnDateSetListener datePickerListener7 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear7,
				int selectedMonth7, int selectedDay7) {
			year7 = selectedYear7;
			month7 = selectedMonth7;
			day7 = selectedDay7;
			try {
				dtopv3 = formatter.parse(new StringBuilder().append(year7)
						.append("-").append(month7 + 1).append("-")
						.append(day7).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math.abs(dtopv3.getTime() - dtopv2.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);
			// showAlert(String.valueOf(diff_current));
			if (dtopv3.before(dtopv2) || diff_current < 28) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				opv3.setText(String.format("%d-%s-%d", day7,
						DBAdapter.hmstr[month7], year7));
				stropv3 = new StringBuilder().append(year7).append("-")
						.append(month7 + 1).append("-").append(day7).toString();
			}

		}
	};

	// *************************************************************************************************************************************************************

	public void setCurrentDateOnView8() {

		final Calendar c = Calendar.getInstance();
		year8 = c.get(Calendar.YEAR);
		month8 = c.get(Calendar.MONTH);
		day8 = c.get(Calendar.DAY_OF_MONTH);

	}

	public void addListenerOnButton8() {

		date_dpt3 = (Button) findViewById(R.id.date_dpt3);

		date_dpt3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID8);

			}

		});
		dpt3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID8);

			}

		});
	}

	private DatePickerDialog.OnDateSetListener datePickerListener8 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear8,
				int selectedMonth8, int selectedDay8) {
			year8 = selectedYear8;
			month8 = selectedMonth8;
			day8 = selectedDay8;
			try {
				dtdpt3 = formatter.parse(new StringBuilder().append(year8)
						.append("-").append(month8 + 1).append("-")
						.append(day8).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math.abs(dtdpt3.getTime() - dtdpt2.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);
			// showAlert(String.valueOf(diff_current));
			if (dtdpt3.before(dtdpt2) || diff_current < 28) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				// dpt3.setText(new
				// StringBuilder().append(year8).append("-").append(month8 +
				// 1).append("-").append(day8));
				dpt3.setText(String.format("%d-%s-%d", day8,
						DBAdapter.hmstr[month8], year8));
				strdpt3 = new StringBuilder().append(year8).append("-")
						.append(month8 + 1).append("-").append(day8).toString();

			}
		}
	};

	// ********************************************************************
	public void setCurrentDateOnView9() {

		final Calendar c = Calendar.getInstance();
		year9 = c.get(Calendar.YEAR);
		month9 = c.get(Calendar.MONTH);
		day9 = c.get(Calendar.DAY_OF_MONTH);

	}

	public void addListenerOnButton9() {

		date_hepatitisb3 = (Button) findViewById(R.id.date_hepatitisb3);

		date_hepatitisb3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID9);

			}

		});

		hepatitisb3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID9);

			}

		});
	}

	private DatePickerDialog.OnDateSetListener datePickerListener9 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear9,
				int selectedMonth9, int selectedDay9) {
			year9 = selectedYear9;
			month9 = selectedMonth9;
			day9 = selectedDay9;
			try {
				dthepatitisb3 = formatter.parse(new StringBuilder()
						.append(year9).append("-").append(month9 + 1)
						.append("-").append(day9).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math.abs(dthepatitisb3.getTime()
					- dthepatitisb2.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);
			// showAlert(String.valueOf(diff_current));
			if (dthepatitisb3.before(dthepatitisb2) || diff_current < 28) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {

				// hepatitisb3.setText(new
				// StringBuilder().append(year9).append("-").append(month9 +
				// 1).append("-").append(day9));
				hepatitisb3.setText(String.format("%d-%s-%d", day9,
						DBAdapter.hmstr[month9], year9));
				strhepatitisb3 = new StringBuilder().append(year9).append("-")
						.append(month9 + 1).append("-").append(day9).toString();
			}

		}
	};

	public void setCurrentDateOnView10() {

		final Calendar c = Calendar.getInstance();
		year10 = c.get(Calendar.YEAR);
		month10 = c.get(Calendar.MONTH);
		day10 = c.get(Calendar.DAY_OF_MONTH);

	}

	public void addListenerOnButton10() {

		date_measles = (Button) findViewById(R.id.date_measles);

		date_measles.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID10);

			}

		});

		// measles.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		//
		// showDialog(DATE_DIALOG_ID);
		//
		// }
		//
		// });
	}

	private DatePickerDialog.OnDateSetListener datePickerListener10 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear10,
				int selectedMonth10, int selectedDay10) {
			year10 = selectedYear10;
			month10 = selectedMonth10;
			day10 = selectedDay10;
			try {
				dtmeasles = formatter.parse(new StringBuilder().append(year10)
						.append("-").append(month10 + 1).append("-")
						.append(day10).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math.abs(dtmeasles.getTime() - dtdob.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);
			// showAlert(String.valueOf(diff_current));
			if (dtmeasles.before(dtdob) || diff_current < 270) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {

				// measles.setText(new
				// StringBuilder().append(year10).append("-").append(month10 +
				// 1).append("-").append(day10).append(" "));
				measles.setText(String.format("%d-%s-%d", day10,
						DBAdapter.hmstr[month10], year10));
				strmeasles = new StringBuilder().append(year10).append("-")
						.append(month10 + 1).append("-").append(day10)
						.toString();
			}

		}
	};

	// ********************************************************************************

	public void setCurrentDateOnView11() {

		final Calendar c = Calendar.getInstance();
		year11 = c.get(Calendar.YEAR);
		month11 = c.get(Calendar.MONTH);
		day11 = c.get(Calendar.DAY_OF_MONTH);

	}

	public void addListenerOnButton11() {

		date_vitamina = (Button) findViewById(R.id.date_vitamina);

		date_vitamina.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID11);

			}

		});
		// vitamina.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		//
		// showDialog(DATE_DIALOG_ID11);
		//
		// }
		//
		// });
	}

	private DatePickerDialog.OnDateSetListener datePickerListener11 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear11,
				int selectedMonth11, int selectedDay11) {
			year11 = selectedYear11;
			month11 = selectedMonth11;
			day11 = selectedDay11;
			try {
				dtvitamina = formatter.parse(new StringBuilder().append(year11)
						.append("-").append(month11 + 1).append("-")
						.append(day11).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diffDays1 = Math.abs(dtvitamina.getTime() - dtdob.getTime());
			long diff_current = diffDays1 / (24 * 60 * 60 * 1000);
			// showAlert(String.valueOf(diff_current));
			if (dtvitamina.before(dtdob) || diff_current < 270) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {

				// vitamina.setText(new
				// StringBuilder().append(year11).append("-").append(month11 +
				// 1).append("-").append(day11));
				vitamina.setText(String.format("%d-%s-%d", day11,
						DBAdapter.hmstr[month11], year11));
				strvitamina = new StringBuilder().append(year11).append("-")
						.append(month11 + 1).append("-").append(day11)
						.toString();
			}

		}
	};

	public void showAlert(String msg) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle("Alert").setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// Some stuff to do when ok got clicked
					}
				})

				.show();
	}

	public void onBackPressed() {

		new AlertDialog.Builder(this)
				.setMessage("क्या आप बाहर निकलना चाहती हैं ?")
				.setCancelable(false)
				.setPositiveButton("हां",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								finish();
							}
						}).setNegativeButton("नहीं", null).show();
	}
}
