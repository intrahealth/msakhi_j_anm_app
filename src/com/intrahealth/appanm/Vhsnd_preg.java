package com.intrahealth.appanm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.intrahealth.appanm.control.NumberPicker;
import com.intrahealth.appanm.control.NumberPickerInt;
import com.intrahealth.msakhi.appanm.R;

public class Vhsnd_preg extends Activity {

	EditText name, sys, dia, institution, mother_mcts, hname;
	TextView checkup_date1, checkup_date2, checkup_date3, checkup_date4, tt1,
			tt2, booster, abortation_date, death_date, reg_date, lmp;
	String strcheckup_date1, strcheckup_date2, strcheckup_date3,
			strcheckup_date4, strtt1, strtt2, strbooster, strabortation_date,
			strdeath_date;
	Button date_checkup1, date_checkup2, date_checkup3, date_checkup4,
			date_tt1, date_tt2, date_booster, submit, date_abortation,
			anc1_view, anc2_view, anc3_view, anc4_view, date_death;
	RadioGroup abortion, urine_test, death, reffered, danger_signs, albumin,
			sugar;

	Spinner anc_place;
	NumberPicker weight, hb;
	NumberPickerInt ifa;
	Dialog myDialog;
	String asha_id, id, danger_signs1;
	Spinner type;
	Integer anc_visit;
	private DBAdapter mydb;
	Cursor c;
	Date dtvisit1, dtvisit2, dtvisit3, dtvisit4, dttt1, dttt2, dtreg;
	LinearLayout lay_death, lay1, lay2, lay3, lay4, lay5;
	String msg_title;
	JSONObject payload1 = new JSONObject();

	RadioButton radiobutton;
	DataSetter ds = new DataSetter();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");

	private int year;
	private int month;
	private int day;
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

	static final int DATE_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID1 = 1;
	static final int DATE_DIALOG_ID2 = 2;
	static final int DATE_DIALOG_ID3 = 3;
	static final int DATE_DIALOG_ID4 = 4;
	static final int DATE_DIALOG_ID5 = 5;
	static final int DATE_DIALOG_ID6 = 6;
	static final int DATE_DIALOG_ID7 = 7;
	static final int DATE_DIALOG_ID8 = 8;

	// Add Hero
	Button lastvisitrecord;
	TextView tv_lastinquiry, tv_lastcheckuplocation, tv_lastweight_preg,
			tv_lasthemoglobin_preg, tv_lasturinetest_preg, tv_last_ttdose,
			tv_last_IFA;
	RadioButton urine_testy, urine_testn;
	String inquiry_1 = "", checkuploc = "", weights = "", hemoglobin = "",
			urine = "", ttdose = "", ifaa = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vhsnd_preg);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			asha_id = extras.getString("asha_id");
			id = String.valueOf(extras.getInt("pid"));
			// pname=extras.getString("pname");
			// showAlert(asha_id+" "+id);

		}
		mydb = DBAdapter.getInstance(getApplicationContext());

		// add hero
		urine_testy = (RadioButton) findViewById(R.id.urine_testy);
		urine_testn = (RadioButton) findViewById(R.id.urine_testn);

		name = (EditText) findViewById(R.id.name);
		sys = (EditText) findViewById(R.id.sys);
		dia = (EditText) findViewById(R.id.dia);
		hname = (EditText) findViewById(R.id.hname);
		mother_mcts = (EditText) findViewById(R.id.mother_mcts);
		albumin = (RadioGroup) findViewById(R.id.albumin);
		sugar = (RadioGroup) findViewById(R.id.sugar);
		checkup_date1 = (TextView) findViewById(R.id.checkup_date1);
		checkup_date2 = (TextView) findViewById(R.id.checkup_date2);
		checkup_date3 = (TextView) findViewById(R.id.checkup_date3);
		checkup_date4 = (TextView) findViewById(R.id.checkup_date4);
		reg_date = (TextView) findViewById(R.id.reg_date);
		lmp = (TextView) findViewById(R.id.lmp);
		death_date = (TextView) findViewById(R.id.death_date);
		tt1 = (TextView) findViewById(R.id.tt1);
		tt2 = (TextView) findViewById(R.id.tt2);
		booster = (TextView) findViewById(R.id.booster);
		anc_place = (Spinner) findViewById(R.id.anc_place);
		abortion = (RadioGroup) findViewById(R.id.abortion);
		urine_test = (RadioGroup) findViewById(R.id.urine_test);
		reffered = (RadioGroup) findViewById(R.id.reffered);
		death = (RadioGroup) findViewById(R.id.death);
		weight = (NumberPicker) findViewById(R.id.weight);
		weight.setValue(Float.parseFloat("30"));
		hb = (NumberPicker) findViewById(R.id.hb);
		ifa = (NumberPickerInt) findViewById(R.id.ifa);
		lay_death = (LinearLayout) findViewById(R.id.lay_death);
		lay1 = (LinearLayout) findViewById(R.id.lay1);
		lay2 = (LinearLayout) findViewById(R.id.lay2);
		lay3 = (LinearLayout) findViewById(R.id.lay3);
		lay4 = (LinearLayout) findViewById(R.id.lay4);
		lay5 = (LinearLayout) findViewById(R.id.lay5);

		abortation_date = (TextView) findViewById(R.id.abortation_date);
		type = (Spinner) findViewById(R.id.type);
		type = (Spinner) findViewById(R.id.type);
		List<String> list3 = new ArrayList<String>();
		list3.add("APHC");
		list3.add("CHC");
		list3.add("Medical college");
		list3.add("Pvt.Hospital");
		list3.add("govt. hospital");
		list3.add("Other");
		ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list3);
		dataAdapter3
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type.setAdapter(dataAdapter3);

		institution = (EditText) findViewById(R.id.institution);

		anc_place = (Spinner) findViewById(R.id.anc_place);
		List<String> list1 = new ArrayList<String>();
		list1.add("घर");
		list1.add("अस्पताल");
		list1.add("उपकेन्द्र");
		list1.add("आंगनबाड़ी ");
		list1.add("केंद्र");
		list1.add("अन्य");
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list1);
		dataAdapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		anc_place.setAdapter(dataAdapter1);

		date_checkup1 = (Button) findViewById(R.id.date_checkup1);
		date_checkup2 = (Button) findViewById(R.id.date_checkup2);
		date_checkup3 = (Button) findViewById(R.id.date_checkup3);
		date_checkup4 = (Button) findViewById(R.id.date_checkup4);
		date_tt1 = (Button) findViewById(R.id.date_tt1);
		date_tt2 = (Button) findViewById(R.id.date_tt2);
		date_booster = (Button) findViewById(R.id.date_booster);
		anc1_view = (Button) findViewById(R.id.anc1_view);
		anc2_view = (Button) findViewById(R.id.anc2_view);
		anc3_view = (Button) findViewById(R.id.anc3_view);
		anc4_view = (Button) findViewById(R.id.anc4_view);

		c = mydb.getVhsndPreg(asha_id, id);
		if (c.moveToFirst()) {
			if (!c.isNull(c.getColumnIndex("reg_date"))) {
				// showAlert(c.getString(c.getColumnIndex("reg_date")));
				try {
					dtreg = formatter.parse(c.getString(c
							.getColumnIndex("reg_date")));
					reg_date.setText(DBAdapter.strtodate(c.getString(c
							.getColumnIndex("reg_date"))));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (!c.isNull(c.getColumnIndex("visit1_date"))) {
				strcheckup_date1 = c.getString(c.getColumnIndex("visit1_date"));
				checkup_date1.setText(DBAdapter.strtodate(c.getString(c
						.getColumnIndex("visit1_date"))));
				try {
					dtvisit1 = formatter.parse(c.getString(c
							.getColumnIndex("visit1_date")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("visit2_date"))) {
				strcheckup_date2 = c.getString(c.getColumnIndex("visit2_date"));
				checkup_date2.setText(DBAdapter.strtodate(c.getString(c
						.getColumnIndex("visit2_date"))));
				try {
					dtvisit2 = formatter.parse(c.getString(c
							.getColumnIndex("visit2_date")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("visit3_date"))) {
				strcheckup_date3 = c.getString(c.getColumnIndex("visit3_date"));
				checkup_date3.setText(DBAdapter.strtodate(c.getString(c
						.getColumnIndex("visit3_date"))));
				try {
					dtvisit3 = formatter.parse(c.getString(c
							.getColumnIndex("visit3_date")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("visit4_date"))) {
				strcheckup_date4 = c.getString(c.getColumnIndex("visit4_date"));
				checkup_date4.setText(DBAdapter.strtodate(c.getString(c
						.getColumnIndex("visit4_date"))));
				try {
					dtvisit4 = formatter.parse(c.getString(c
							.getColumnIndex("visit4_date")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("tt1_date"))) {
				strtt1 = c.getString(c.getColumnIndex("tt1_date"));
				tt1.setText(DBAdapter.strtodate(c.getString(c
						.getColumnIndex("tt1_date"))));
				// date_tt1.setVisibility(android.view.View.GONE);
				try {
					dttt1 = formatter.parse(c.getString(c
							.getColumnIndex("tt1_date")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("tt2_date"))) {
				strtt2 = c.getString(c.getColumnIndex("tt2_date"));
				tt2.setText(DBAdapter.strtodate(c.getString(c
						.getColumnIndex("tt2_date"))));
				// date_tt2.setVisibility(android.view.View.GONE);
				try {
					dttt2 = formatter.parse(c.getString(c
							.getColumnIndex("tt2_date")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!c.isNull(c.getColumnIndex("ttbooster_date"))) {
				strbooster = c.getString(c.getColumnIndex("ttbooster_date"));
				booster.setText(DBAdapter.strtodate(c.getString(c
						.getColumnIndex("ttbooster_date"))));
				// date_booster.setVisibility(android.view.View.GONE);
			}

			mother_mcts.setText(c.getString(c.getColumnIndex("mother_mcts")));

			anc_visit = c.getInt(c.getColumnIndex("anc_visit"));

			if (anc_visit == 0) {
				date_checkup2.setEnabled(false);
				checkup_date2.setEnabled(false);
				date_checkup3.setEnabled(false);
				checkup_date3.setEnabled(false);
				date_checkup4.setEnabled(false);
				checkup_date4.setEnabled(false);
			}

			if (anc_visit == 1) {
				date_checkup3.setEnabled(false);
				checkup_date3.setEnabled(false);
				date_checkup4.setEnabled(false);
				checkup_date4.setEnabled(false);
				date_checkup1.setVisibility(android.view.View.GONE);
				anc1_view.setVisibility(android.view.View.GONE);

				try {
					dtvisit1 = formatter.parse(c.getString(
							c.getColumnIndex("visit1_date")).trim());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (anc_visit == 2) {
				date_checkup4.setEnabled(false);
				checkup_date4.setEnabled(false);
				date_checkup1.setVisibility(android.view.View.GONE);
				anc1_view.setVisibility(android.view.View.GONE);
				date_checkup2.setVisibility(android.view.View.GONE);
				anc2_view.setVisibility(android.view.View.GONE);

				try {
					dtvisit2 = formatter.parse(c.getString(
							c.getColumnIndex("visit2_date")).trim());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (anc_visit == 3) {

				date_checkup1.setVisibility(android.view.View.GONE);
				anc1_view.setVisibility(android.view.View.GONE);
				date_checkup2.setVisibility(android.view.View.GONE);
				anc2_view.setVisibility(android.view.View.GONE);
				date_checkup3.setVisibility(android.view.View.GONE);
				anc3_view.setVisibility(android.view.View.GONE);

				try {
					dtvisit3 = formatter.parse(c.getString(
							c.getColumnIndex("visit3_date")).trim());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			c = mydb.getAnmPreg(asha_id, id);
			if (c.moveToFirst()) {

				name.setText(c.getString(c.getColumnIndex("name")));
				hname.setText(c.getString(c.getColumnIndex("hname")));
				// Add Hero
				weights = c.getString(c.getColumnIndex("weight"));
				if (weights != null && !weights.equalsIgnoreCase("")) {
					weight.setValue(Float.valueOf(weights));
				} else {
					weights = "0.0";
				}
				if (!c.isNull(c.getColumnIndex("lmp"))) {
					// showAlert(c.getString(c.getColumnIndex("reg_date")));

					lmp.setText(DBAdapter.strtodate(c.getString(c
							.getColumnIndex("lmp"))));

				}
			}

		}

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

		danger_signs = (RadioGroup) findViewById(R.id.danger_signs);

		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				int selectedId1 = abortion.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId1);
				switch (selectedId1) {
				case R.id.abortiony:
					ds.setAbortion(ds.getAbortionis());
					break;
				case R.id.abortionn:
					ds.setAbortion(ds.getAbortionis());
					break;
				}

				int selectedId11 = danger_signs.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId11);
				switch (selectedId11) {
				case R.id.signy:
					danger_signs1 = ds.getPast_health();
					break;
				case R.id.signn:
					danger_signs1 = "N";
					break;
				}

				int selectedId6 = sugar.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId6);
				switch (selectedId6) {
				case R.id.sugara:
					ds.setSugar("A");
					break;
				case R.id.sugarp:
					ds.setSugar("P");
					break;
				}
				int selectedId7 = albumin.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId7);
				switch (selectedId7) {
				case R.id.albumina:
					ds.setAlbumin("A");
					break;
				case R.id.albuminp:
					ds.setAlbumin("P");
					break;
				}

				int selectedId3 = urine_test.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId3);
				switch (selectedId3) {
				case R.id.urine_testy:
					ds.setUrine_test("A=" + ds.getAlbumin() + " " + "S="
							+ ds.getSugar());
					break;
				case R.id.urine_testn:
					ds.setUrine_test("No");
					break;
				}

				int selectedId4 = reffered.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId4);
				switch (selectedId4) {
				case R.id.refferedy:
					ds.setReffered("Yes");
					break;
				case R.id.refferedn:
					ds.setReffered("No");
					break;
				}

				int selectedId5 = death.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId5);
				switch (selectedId5) {
				case R.id.deathy:
					ds.setDeath("Yes");
					break;
				case R.id.deathn:
					ds.setDeath("No");
					break;
				}
				// ds.setAlbumin(albumin.getText().toString());
				// ds.setSugar(sugar.getText().toString());
				ds.setAbortation_date(abortation_date.getText().toString());
				ds.setType(type.getSelectedItem().toString());
				ds.setInstitution(institution.getText().toString());

				StringBuffer result1 = new StringBuffer();
				result1 = result1.append(sys.getText().toString()).append("/")
						.append(dia.getText().toString()).append(" ");
				ds.setBp(result1.toString());

				mydb.vhsndpreg(asha_id, id, strcheckup_date1, strcheckup_date2,
						strcheckup_date3, strcheckup_date4, ds.getAbortion(),
						strtt1, strtt2, strbooster, ds.getReffered(),
						danger_signs1, String.valueOf(anc_visit + 1));
				// mydb.sendGPRS("/aprego/"+asha_id+"/"+id,
				// payload1.toString(),1);
				try {

					c = mydb.getUpdatePrego(asha_id, id);
					if (c.moveToFirst()) {

						payload1.put("aid", asha_id);
						payload1.put("id", id);
						payload1.put("mother_mcts",
								c.getString(c.getColumnIndex("mother_mcts")));
						payload1.put("aadhar_card",
								c.getString(c.getColumnIndex("aadhar_card")));
						payload1.put("bank_name",
								c.getString(c.getColumnIndex("bank_name")));
						payload1.put("branch",
								c.getString(c.getColumnIndex("branch")));
						payload1.put("jsy",
								c.getString(c.getColumnIndex("jsy")));
						payload1.put("bpl",
								c.getString(c.getColumnIndex("bpl")));
						payload1.put("age",
								c.getString(c.getColumnIndex("age")));
						payload1.put("reg_date",
								c.getString(c.getColumnIndex("reg_date")));
						payload1.put("month_reg",
								c.getString(c.getColumnIndex("month_reg")));
						payload1.put("week_reg",
								c.getString(c.getColumnIndex("week_reg")));
						payload1.put("mother_weight",
								c.getString(c.getColumnIndex("mother_weight")));
						payload1.put("blood_group",
								c.getString(c.getColumnIndex("blood_group")));
						payload1.put("past_health",
								c.getString(c.getColumnIndex("past_health")));
						payload1.put("total_preg",
								c.getString(c.getColumnIndex("total_preg")));
						payload1.put("lastpreg_result", c.getString(c
								.getColumnIndex("lastpreg_result")));
						payload1.put("lastpreg_comp",
								c.getString(c.getColumnIndex("lastpreg_comp")));
						payload1.put("ltolpreg_result", c.getString(c
								.getColumnIndex("ltolpreg_result")));
						payload1.put("ltolpreg_comp",
								c.getString(c.getColumnIndex("ltolpreg_comp")));
						payload1.put("delivery_plan",
								c.getString(c.getColumnIndex("delivery_plan")));
						payload1.put("vdrl",
								c.getString(c.getColumnIndex("vdrl")));
						payload1.put("hiv",
								c.getString(c.getColumnIndex("hiv")));
						payload1.put("visit1_date", strcheckup_date1);
						payload1.put("visit2_date", strcheckup_date2);
						payload1.put("visit3_date", strcheckup_date3);
						payload1.put("visit4_date", strcheckup_date4);
						payload1.put("abortion", ds.getAbortion());
						payload1.put("tt1_date", strtt1);
						payload1.put("tt2_date", strtt2);
						payload1.put("ttbooster_date", strbooster);
						payload1.put("reffered", ds.getReffered());
						payload1.put("danger_signs", danger_signs1);
						payload1.put("anc_visit", String.valueOf(anc_visit + 1));
						payload1.put("child_mcts",
								c.getString(c.getColumnIndex("child_mcts")));
						payload1.put("breastfeeding",
								c.getString(c.getColumnIndex("breastfeeding")));
						payload1.put("child_name",
								c.getString(c.getColumnIndex("child_name")));
						payload1.put("bcg",
								c.getString(c.getColumnIndex("bcg")));
						payload1.put("opv1",
								c.getString(c.getColumnIndex("opv1")));
						payload1.put("dpt1",
								c.getString(c.getColumnIndex("dpt1")));
						payload1.put("hepb1",
								c.getString(c.getColumnIndex("hepb1")));
						payload1.put("opv2",
								c.getString(c.getColumnIndex("opv2")));
						payload1.put("dpt2",
								c.getString(c.getColumnIndex("dpt2")));
						payload1.put("hepb2",
								c.getString(c.getColumnIndex("hepb2")));
						payload1.put("opv3",
								c.getString(c.getColumnIndex("opv3")));
						payload1.put("dpt3",
								c.getString(c.getColumnIndex("dpt3")));
						payload1.put("hepb3",
								c.getString(c.getColumnIndex("hepb3")));
						payload1.put("measeals",
								c.getString(c.getColumnIndex("measeals")));
						payload1.put("vitamina",
								c.getString(c.getColumnIndex("vitamina")));
					}
					mydb.sendGPRS("uprego", payload1.toString(), 1);
					// mydb.sendGPRS("/aprego/"+asha_id+"/"+id,
					// payload1.toString(),1);
				} catch (Exception e) {
					Log.d("error", e.getMessage());
					Toast.makeText(getApplicationContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

				if (anc_visit == 0) {

					if (strcheckup_date1 == null
							|| strcheckup_date1.equalsIgnoreCase("DD/MM/YYYY")) {
						showAlert("Please Enter Check Up Date");
					} else {
						mydb.vhsndpreg(asha_id, id, strcheckup_date1,
								strcheckup_date2, strcheckup_date3,
								strcheckup_date4, ds.getAbortion(), strtt1,
								strtt2, strbooster, ds.getReffered(),
								danger_signs1, String.valueOf(anc_visit + 1));
						mydb.sendGPRS("uprego", payload1.toString(), 1);

						mydb.insertAncVisit1(asha_id, id,
								Float.toString((float) weight.getValue()),
								ds.getBp(),
								Float.toString((float) hb.getValue()),
								ds.getUrine_test(),
								Float.toString((float) ifa.getValue()));
						Toast.makeText(getApplicationContext(),
								"जानकारी बदल गयी", Toast.LENGTH_SHORT).show();
						try {
							JSONObject payload = new JSONObject();
							payload.put("asha_id", asha_id);
							payload.put("_id", id);
							payload.put("weight1",
									Float.toString((float) weight.getValue()));
							payload.put("bp1", ds.getBp());
							payload.put("hb1",
									Float.toString((float) hb.getValue()));
							payload.put("urine1", ds.getUrine_test());
							payload.put("ifa1",
									Float.toString((float) ifa.getValue()));

							Log.d("anc", "visit");
							mydb.sendGPRS("/avisit", payload.toString(), 0);

						} catch (Exception e) {
							Log.d("error", e.getMessage());
							Toast.makeText(getApplicationContext(),
									e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						finish();
					}
				}

				if (anc_visit == 1) {
					if (strcheckup_date2 == null
							|| strcheckup_date2.equalsIgnoreCase("DD/MM/YYYY")) {
						showAlert("Please Enter Second Check Up Date");
					} else {
						mydb.vhsndpreg(asha_id, id, strcheckup_date1,
								strcheckup_date2, strcheckup_date3,
								strcheckup_date4, ds.getAbortion(), strtt1,
								strtt2, strbooster, ds.getReffered(),
								danger_signs1, String.valueOf(anc_visit + 1));
						mydb.sendGPRS("uprego", payload1.toString(), 1);

						mydb.updateAncVisit2(asha_id, id,
								Float.toString((float) weight.getValue()),
								ds.getBp(),
								Float.toString((float) hb.getValue()),
								ds.getUrine_test(),
								Float.toString((float) ifa.getValue()));
						Toast.makeText(getApplicationContext(),
								"जानकारी बदल गयी", Toast.LENGTH_SHORT).show();
						try {
							JSONObject payload = new JSONObject();

							c = mydb.getAncVisit(asha_id, id);
							if (c.moveToFirst()) {

								payload.put("asha_id", asha_id);
								payload.put("_id", id);
								payload.put("weight1", c.getString(c
										.getColumnIndex("weight1")));
								payload.put("bp1",
										c.getString(c.getColumnIndex("bp1")));
								payload.put("hb1",
										c.getString(c.getColumnIndex("hb1")));
								payload.put("urine1",
										c.getString(c.getColumnIndex("urine1")));
								payload.put("ifa1",
										c.getString(c.getColumnIndex("ifa1")));
								payload.put("weight2", Float
										.toString((float) weight.getValue()));
								payload.put("bp2", ds.getBp());
								payload.put("hb2",
										Float.toString((float) hb.getValue()));
								payload.put("urine2", ds.getUrine_test());
								payload.put("ifa2",
										Float.toString((float) ifa.getValue()));

							}

							mydb.sendGPRS("updateAnc", payload.toString(), 1);
						} catch (Exception e) {
							Log.d("error", e.getMessage());
							Toast.makeText(getApplicationContext(),
									e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						finish();
					}
				}

				if (anc_visit == 2) {
					if (strcheckup_date3 == null
							|| strcheckup_date3.equalsIgnoreCase("DD/MM/YYYY")) {
						showAlert("Please Enter Third Check Up Date");
					} else {
						mydb.vhsndpreg(asha_id, id, strcheckup_date1,
								strcheckup_date2, strcheckup_date3,
								strcheckup_date4, ds.getAbortion(), strtt1,
								strtt2, strbooster, ds.getReffered(),
								danger_signs1, String.valueOf(anc_visit + 1));
						mydb.sendGPRS("uprego", payload1.toString(), 1);

						mydb.updateAncVisit3(asha_id, id,
								Float.toString((float) weight.getValue()),
								ds.getBp(),
								Float.toString((float) hb.getValue()),
								ds.getUrine_test(),
								Float.toString((float) ifa.getValue()));
						Toast.makeText(getApplicationContext(),
								"जानकारी बदल गयी", Toast.LENGTH_SHORT).show();
						try {
							JSONObject payload = new JSONObject();
							c = mydb.getAncVisit(asha_id, id);
							if (c.moveToFirst()) {
								payload.put("asha_id", asha_id);
								payload.put("_id", id);
								payload.put("weight1", c.getString(c
										.getColumnIndex("weight1")));
								payload.put("bp1",
										c.getString(c.getColumnIndex("bp1")));
								payload.put("hb1",
										c.getString(c.getColumnIndex("hb1")));
								payload.put("urine1",
										c.getString(c.getColumnIndex("urine1")));
								payload.put("ifa1",
										c.getString(c.getColumnIndex("ifa1")));
								payload.put("weight2", c.getString(c
										.getColumnIndex("weight2")));
								payload.put("bp2",
										c.getString(c.getColumnIndex("bp2")));
								payload.put("hb2",
										c.getString(c.getColumnIndex("hb2")));
								payload.put("urine2",
										c.getString(c.getColumnIndex("urine2")));
								payload.put("ifa2",
										c.getString(c.getColumnIndex("ifa2")));
								payload.put("weight3", Float
										.toString((float) weight.getValue()));
								payload.put("bp3", ds.getBp());
								payload.put("hb3",
										Float.toString((float) hb.getValue()));
								payload.put("urine3", ds.getUrine_test());
								payload.put("ifa3",
										Float.toString((float) ifa.getValue()));

							}

							mydb.sendGPRS("updateAnc", payload.toString(), 1);
						} catch (Exception e) {
							Log.d("error", e.getMessage());
							Toast.makeText(getApplicationContext(),
									e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						finish();
					}
				}

				if (anc_visit == 3) {
					if (strcheckup_date4 == null
							|| strcheckup_date4.equalsIgnoreCase("DD/MM/YYYY")) {
						showAlert("Please Enter Forth Check Up Date");
					} else {

						mydb.vhsndpreg(asha_id, id, strcheckup_date1,
								strcheckup_date2, strcheckup_date3,
								strcheckup_date4, ds.getAbortion(), strtt1,
								strtt2, strbooster, ds.getReffered(),
								danger_signs1, String.valueOf(anc_visit + 1));
						mydb.sendGPRS("uprego", payload1.toString(), 1);

						mydb.updateAncVisit4(asha_id, id,
								Float.toString((float) weight.getValue()),
								ds.getBp(),
								Float.toString((float) hb.getValue()),
								ds.getUrine_test(),
								Float.toString((float) ifa.getValue()));

						Toast.makeText(getApplicationContext(),
								"जानकारी बदल गयी", Toast.LENGTH_SHORT).show();
						try {
							JSONObject payload = new JSONObject();
							c = mydb.getAncVisit(asha_id, id);
							if (c.moveToFirst()) {
								payload.put("asha_id", asha_id);
								payload.put("_id", id);
								payload.put("weight1", c.getString(c
										.getColumnIndex("weight1")));
								payload.put("bp1",
										c.getString(c.getColumnIndex("bp1")));
								payload.put("hb1",
										c.getString(c.getColumnIndex("hb1")));
								payload.put("urine1",
										c.getString(c.getColumnIndex("urine1")));
								payload.put("ifa1",
										c.getString(c.getColumnIndex("ifa1")));
								payload.put("weight2", c.getString(c
										.getColumnIndex("weight2")));
								payload.put("bp2",
										c.getString(c.getColumnIndex("bp2")));
								payload.put("hb2",
										c.getString(c.getColumnIndex("hb2")));
								payload.put("urine2",
										c.getString(c.getColumnIndex("urine2")));
								payload.put("ifa2",
										c.getString(c.getColumnIndex("ifa2")));
								payload.put("weight3", c.getString(c
										.getColumnIndex("weight3")));
								payload.put("bp3",
										c.getString(c.getColumnIndex("bp3")));
								payload.put("hb3",
										c.getString(c.getColumnIndex("hb3")));
								payload.put("urine3",
										c.getString(c.getColumnIndex("urine3")));
								payload.put("ifa3",
										c.getString(c.getColumnIndex("ifa3")));
								payload.put("weight4", Float
										.toString((float) weight.getValue()));
								payload.put("bp4", ds.getBp());
								payload.put("hb4",
										Float.toString((float) hb.getValue()));
								payload.put("urine4", ds.getUrine_test());
								payload.put("ifa4",
										Float.toString((float) ifa.getValue()));

							}
							mydb.sendGPRS("updateAnc", payload.toString(), 1);
						} catch (Exception e) {
							Log.d("error", e.getMessage());
							Toast.makeText(getApplicationContext(),
									e.getMessage(), Toast.LENGTH_SHORT).show();
						}

						finish();
					}
				}
				/*
				 * String response1 = null; String vhsnd_preg
				 * =name.getText().toString(),
				 * checkup_date1.getText().toString(),
				 * checkup_date2.getText().toString() ,
				 * checkup_date3.getText().toString(),
				 * checkup_date4.getText().toString(), ds.getAnc_place(),
				 * ds.getAbortion() , Float.toString((float)weight.getValue()),
				 * ds.getBp(), Float.toString((float)hb.getValue()),
				 * ds.getUrine_test(), ds.getAlbumin(), ds.getSugar() ,
				 * tt1.getText().toString(), tt2.getText().toString(),
				 * booster.getText().toString(),
				 * Float.toString((float)ifa.getValue()) , ds.getReffered(),
				 * ds.getType(), ds.getAbortation_date(), ds.getInstitution(),
				 * ds.getDeath();
				 */

			}
		});

		anc1_view.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				c = mydb.getAncVisit(asha_id, id);
				Toast.makeText(getApplicationContext(), "null " + c.getCount(),
						Toast.LENGTH_SHORT).show();
				if (c.moveToFirst()) {
					msg_title = "ANC1";
					showAlert("गर्भवती का वज़न:-  "
							+ c.getString(c.getColumnIndex("weight1"))
							+ "\nबी.पी:- "
							+ c.getString(c.getColumnIndex("bp1"))
							+ "\nएच.बी (ग्राम%):- "
							+ c.getString(c.getColumnIndex("hb1"))
							+ "\nमूत्र शुगर:- "
							+ c.getString(c.getColumnIndex("urine1"))
							+ "\nआयरन फोलिक एसिड (गोलियां):- "
							+ c.getString(c.getColumnIndex("ifa1")));
				}
			}
		});

		anc2_view.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				c = mydb.getAncVisit(asha_id, id);
				if (c.moveToFirst()) {
					msg_title = "ANC2";
					showAlert("गर्भवती का वज़न:-  "
							+ c.getString(c.getColumnIndex("weight2"))
							+ "\nबी.पी:- "
							+ c.getString(c.getColumnIndex("bp2"))
							+ "\nएच.बी (ग्राम%):- "
							+ c.getString(c.getColumnIndex("hb2"))
							+ "\nमूत्र शुगर:- "
							+ c.getString(c.getColumnIndex("urine2"))
							+ "\nआयरन फोलिक एसिड (गोलियां):- "
							+ c.getString(c.getColumnIndex("ifa2")));
				}
			}
		});

		anc3_view.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				c = mydb.getAncVisit(asha_id, id);
				if (c.moveToFirst()) {
					msg_title = "ANC3";
					showAlert("गर्भवती का वज़न:-  "
							+ c.getString(c.getColumnIndex("weight3"))
							+ "\nबी.पी:- "
							+ c.getString(c.getColumnIndex("bp3"))
							+ "\nएच.बी (ग्राम%):- "
							+ c.getString(c.getColumnIndex("hb3"))
							+ "\nमूत्र शुगर:- "
							+ c.getString(c.getColumnIndex("urine3"))
							+ "\nआयरन फोलिक एसिड (गोलियां):- "
							+ c.getString(c.getColumnIndex("ifa3")));
				}

			}
		});

		// Add Hero
		valuelastrecord();
		lastvisitrecord = (Button) findViewById(R.id.lastvisitrecord);
		lastvisitrecord.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popup_lastrecord();
			}
		});

	}

	// Add Hero Popup
	public void valuelastrecord() {

		if (!checkup_date4.getText().toString().equalsIgnoreCase("")) {
			inquiry_1 = checkup_date4.getText().toString();
		} else if (!checkup_date3.getText().toString().equalsIgnoreCase("")) {
			inquiry_1 = checkup_date3.getText().toString();
		} else if (!checkup_date2.getText().toString().equalsIgnoreCase("")) {
			inquiry_1 = checkup_date2.getText().toString();
		} else if (!checkup_date1.getText().toString().equalsIgnoreCase("")) {
			inquiry_1 = checkup_date1.getText().toString();
		}
		if (!booster.getText().toString().equalsIgnoreCase("")) {
			ttdose = booster.getText().toString();
		} else if (!tt2.getText().toString().equalsIgnoreCase("")) {
			ttdose = tt2.getText().toString();
		} else if (!tt1.getText().toString().equalsIgnoreCase("")) {
			ttdose = tt1.getText().toString();
		}
		hemoglobin = String.valueOf(hb.getValue());
		// weights = String.valueOf(weight.getValue());
		if (urine_testy.isChecked()) {
			urine = "Yes";
		}
		if (urine_testn.isChecked()) {
			urine = "No";
		}
		int value = anc_place.getSelectedItemPosition();
		if (value == 0) {
			checkuploc = "घर";
		} else if (value == 1) {
			checkuploc = "अस्पताल";
		} else if (value == 2) {
			checkuploc = "उपकेन्द्र";
		} else if (value == 3) {
			checkuploc = "आंगनबाड़ी";
		} else if (value == 4) {
			checkuploc = "केंद्र";
		} else if (value == 5) {
			checkuploc = "अन्य";
		}
		ifaa = String.valueOf(ifa.getValue());
	}

	public void popup_lastrecord() {

		final Dialog dialogshow = new Dialog(this);
		dialogshow.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popup_anclastvisitrecord_mw,
				null, false);
		tv_lastinquiry = (TextView) view.findViewById(R.id.tv_lastinquiry);
		tv_lastcheckuplocation = (TextView) view
				.findViewById(R.id.tv_lastcheckuplocation);
		tv_lastweight_preg = (TextView) view
				.findViewById(R.id.tv_lastweight_preg);
		tv_lasthemoglobin_preg = (TextView) view
				.findViewById(R.id.tv_lasthemoglobin_preg);
		tv_lasturinetest_preg = (TextView) view
				.findViewById(R.id.tv_lasturinetest_preg);
		tv_last_ttdose = (TextView) view.findViewById(R.id.tv_last_ttdose);
		tv_last_IFA = (TextView) view.findViewById(R.id.tv_last_IFA);
		Button closebtn = (Button) view.findViewById(R.id.closebtn);

		tv_lastinquiry.setText(inquiry_1);
		tv_last_ttdose.setText(ttdose);
		tv_lasthemoglobin_preg.setText(hemoglobin);
		tv_lastweight_preg.setText(weights);
		// int selectedId3 = urine_test.getCheckedRadioButtonId();
		// radiobutton = (RadioButton) findViewById(selectedId3);
		// switch (selectedId3) {
		// case R.id.urine_testy:
		// // ds.setUrine_test("A=" + ds.getAlbumin() + " " + "S="
		// // + ds.getSugar());
		// urine = "Yes";
		// break;
		// case R.id.urine_testn:
		// // ds.setUrine_test("No");
		// urine = "No";
		// break;
		// }

		tv_lasturinetest_preg.setText(urine);

		tv_lastcheckuplocation.setText(checkuploc);

		tv_last_IFA.setText(ifaa);

		closebtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialogshow.dismiss();
			}
		});

//		String lastweight = "", lastBP = "", lasthemoglobin = "", lasturine = "", lastIFA = "";
//		if (asha_id != null && id != null) {
//			Cursor cc = mydb.getPopUp_VhsndPreg(asha_id, id);
//			if (cc.moveToFirst()) {
//				if (cc != null) {
//					if (!cc.isNull(cc.getColumnIndex("weight4"))
//							&& !cc.getString(cc.getColumnIndex("weight4"))
//									.equalsIgnoreCase("")) {
//						lastweight = cc.getString(cc.getColumnIndex("weight4"));
//					} else if (!cc.isNull(cc.getColumnIndex("weight3"))
//							&& !cc.getString(cc.getColumnIndex("weight3"))
//									.equalsIgnoreCase("")) {
//						lastweight = cc.getString(cc.getColumnIndex("weight3"));
//					} else if (!cc.isNull(cc.getColumnIndex("weight2"))
//							&& !cc.getString(cc.getColumnIndex("weight2"))
//									.equalsIgnoreCase("")) {
//						lastweight = cc.getString(cc.getColumnIndex("weight2"));
//					} else if (!cc.isNull(cc.getColumnIndex("weight1"))
//							&& !cc.getString(cc.getColumnIndex("weight1"))
//									.equalsIgnoreCase("")) {
//						lastweight = cc.getString(cc.getColumnIndex("weight1"));
//					}
//					// visit1_date =
//					// cc.getString(cc.getColumnIndex("visit1_date"));
//					// visit2_date =
//					// cc.getString(cc.getColumnIndex("visit2_date"));
//					// visit3_date =
//					// cc.getString(cc.getColumnIndex("visit3_date"));
//					// visit4_date =
//					// cc.getString(cc.getColumnIndex("visit4_date"));
//				}
//			}
//		}

		dialogshow.setCanceledOnTouchOutside(true);
		dialogshow.setContentView(view);
		dialogshow.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		dialogshow.setCancelable(true);
		dialogshow.show();

	}

	public void showAlert(String msg) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(msg_title).setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// Some stuff to do when ok got clicked
					}
				})

				.show();
	}

	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {

		case R.id.urine_testy:
			if (checked) {

				lay1.setVisibility(android.view.View.VISIBLE);
				lay2.setVisibility(android.view.View.VISIBLE);
			}

			break;
		case R.id.urine_testn:
			if (checked) {

				lay1.setVisibility(android.view.View.GONE);
				lay2.setVisibility(android.view.View.GONE);
			}

			break;
		case R.id.deathy:
			if (checked) {
				lay_death.setVisibility(android.view.View.VISIBLE);
			}

			break;
		case R.id.deathn:
			if (checked) {
				lay_death.setVisibility(android.view.View.GONE);
			}

			break;

		case R.id.abortiony:
			if (checked) {

				myDialog = new Dialog(Vhsnd_preg.this);
				myDialog.setContentView(R.layout.abortion);
				myDialog.setCancelable(true);

				RadioButton abortionisy = (RadioButton) myDialog
						.findViewById(R.id.abortionisy);
				RadioButton abortionisn = (RadioButton) myDialog
						.findViewById(R.id.abortionisn);

				abortionisy.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {

						ds.setAbortionis("Induced");

						myDialog.dismiss();
					}
				});

				abortionisn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {

						ds.setAbortionis("Spontaneous");

						myDialog.dismiss();
					}
				});

				myDialog.show();

			}

			break;

		case R.id.refferedy:
			if (checked) {
				lay3.setVisibility(android.view.View.VISIBLE);
				lay4.setVisibility(android.view.View.VISIBLE);
				lay5.setVisibility(android.view.View.VISIBLE);
			}

			break;
		case R.id.refferedn:
			if (checked) {
				lay3.setVisibility(android.view.View.GONE);
				lay4.setVisibility(android.view.View.GONE);
				lay5.setVisibility(android.view.View.GONE);
			}

			break;

		case R.id.signy:
			if (checked) {

				myDialog = new Dialog(Vhsnd_preg.this);
				myDialog.setContentView(R.layout.past_health);
				myDialog.setCancelable(true);
				final CheckBox checkBox1 = (CheckBox) myDialog
						.findViewById(R.id.checkBox1);
				final CheckBox checkBox2 = (CheckBox) myDialog
						.findViewById(R.id.checkBox2);
				final CheckBox checkBox3 = (CheckBox) myDialog
						.findViewById(R.id.checkBox3);
				final CheckBox checkBox4 = (CheckBox) myDialog
						.findViewById(R.id.checkBox4);
				final CheckBox checkBox5 = (CheckBox) myDialog
						.findViewById(R.id.checkBox5);
				final CheckBox checkBox6 = (CheckBox) myDialog
						.findViewById(R.id.checkBox6);
				final CheckBox checkBox7 = (CheckBox) myDialog
						.findViewById(R.id.checkBox7);
				final CheckBox checkBox8 = (CheckBox) myDialog
						.findViewById(R.id.checkBox8);
				final CheckBox checkBox9 = (CheckBox) myDialog
						.findViewById(R.id.checkBox9);

				checkBox1.setText("उच्च रक्तचाप");
				checkBox2.setText("योनी रक्तस्राव");
				checkBox3.setText("दुर्गन्ध युक्त स्राव");
				checkBox4.setText("अतिरक्तालाप्ता");
				checkBox5.setText("मधुमेह");
				checkBox6.setText("जुडवा बच्चे");
				checkBox7.setText("अन्य");
				checkBox8.setVisibility(android.view.View.GONE);
				checkBox9.setVisibility(android.view.View.GONE);

				Button ok = (Button) myDialog.findViewById(R.id.ok);

				ok.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						StringBuffer result = new StringBuffer();
						if (checkBox1.isChecked()) {
							result = result.append("1");
						}
						if (checkBox2.isChecked()) {
							result = result.append("2");
						}
						if (checkBox3.isChecked()) {
							result = result.append("3");
						}
						if (checkBox4.isChecked()) {
							result = result.append("4");
						}
						if (checkBox5.isChecked()) {
							result = result.append("5");
						}
						if (checkBox6.isChecked()) {
							result = result.append("6");
						}
						if (checkBox7.isChecked()) {
							result = result.append("7");
						}

						ds.setPast_health(result.toString());

						myDialog.dismiss();
					}
				});

				myDialog.show();

			}
			break;
		}
	}

	public void setCurrentDateOnView() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		// checkup_date1.setText(new
		// StringBuilder().append(day).append("-").append(month +
		// 1).append("-").append(year).append(" "));

	}

	public void addListenerOnButton() {

		date_checkup1 = (Button) findViewById(R.id.date_checkup1);

		date_checkup1.setOnClickListener(new OnClickListener() {

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

			try {
				// dtvisit2 =
				// formatter.parse(String.format("%04d-%02d-%02d",year,month+1,day));
				dtvisit1 = formatter.parse(new StringBuilder().append(year)
						.append("-").append(month + 1).append("-").append(day)
						.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar mc = Calendar.getInstance();

			if (c.after(mc) || dtvisit1.before(dtreg)) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			}

			else {
				checkup_date1.setText(String.format("%d-%s-%d", day,
						DBAdapter.hmstr[month], year));
				strcheckup_date1 = new StringBuilder().append(year).append("-")
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

		date_checkup2 = (Button) findViewById(R.id.date_checkup2);

		date_checkup2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID1);

			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear1,
				int selectedMonth1, int selectedDay1) {
			Calendar c = Calendar.getInstance();
			c.set(selectedYear1, selectedMonth1, selectedDay1);
			year1 = selectedYear1;
			month1 = selectedMonth1;
			day1 = selectedDay1;

			try {
				// dtvisit2 =
				// formatter.parse(String.format("%04d-%02d-%02d",year,month+1,day));
				dtvisit2 = formatter.parse(new StringBuilder().append(year1)
						.append("-").append(month1 + 1).append("-")
						.append(day1).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar mc = Calendar.getInstance();

			if (c.after(mc) || dtvisit2.before(dtvisit1)) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				checkup_date2.setText(String.format("%d-%s-%d", day1,
						DBAdapter.hmstr[month1], year1));
				strcheckup_date2 = new StringBuilder().append(year1)
						.append("-").append(month1 + 1).append("-")
						.append(day1).toString();

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

		date_checkup3 = (Button) findViewById(R.id.date_checkup3);

		date_checkup3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID2);

			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear2,
				int selectedMonth2, int selectedDay2) {
			Calendar c = Calendar.getInstance();
			c.set(selectedYear2, selectedMonth2, selectedDay2);
			year2 = selectedYear2;
			month2 = selectedMonth2;
			day2 = selectedDay2;

			try {
				// dtvisit2 =
				// formatter.parse(String.format("%04d-%02d-%02d",year,month+1,day));
				dtvisit3 = formatter.parse(new StringBuilder().append(year2)
						.append("-").append(month2 + 1).append("-")
						.append(day2).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar mc = Calendar.getInstance();

			if (c.after(mc) || dtvisit3.before(dtvisit2)) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				checkup_date3.setText(String.format("%d-%s-%d", day2,
						DBAdapter.hmstr[month2], year2));
				strcheckup_date3 = new StringBuilder().append(year2)
						.append("-").append(month2 + 1).append("-")
						.append(day2).toString();
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

		date_checkup4 = (Button) findViewById(R.id.date_checkup4);

		date_checkup4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID3);

			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener3 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear3,
				int selectedMonth3, int selectedDay3) {
			Calendar c = Calendar.getInstance();
			c.set(selectedYear3, selectedMonth3, selectedDay3);
			year3 = selectedYear3;
			month3 = selectedMonth3;
			day3 = selectedDay3;

			try {
				// dtvisit2 =
				// formatter.parse(String.format("%04d-%02d-%02d",year,month+1,day));
				dtvisit4 = formatter.parse(new StringBuilder().append(year3)
						.append("-").append(month3 + 1).append("-")
						.append(day3).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar mc = Calendar.getInstance();

			if (c.after(mc) || dtvisit4.before(dtvisit3)) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				checkup_date4.setText(String.format("%d-%s-%d", day3,
						DBAdapter.hmstr[month3], year3));
				strcheckup_date4 = new StringBuilder().append(year3)
						.append("-").append(month3 + 1).append("-")
						.append(day3).toString();
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

		date_tt1 = (Button) findViewById(R.id.date_tt1);

		date_tt1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID4);

			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener4 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear4,
				int selectedMonth4, int selectedDay4) {
			Calendar c = Calendar.getInstance();
			c.set(selectedYear4, selectedMonth4, selectedDay4);
			year4 = selectedYear4;
			month4 = selectedMonth4;
			day4 = selectedDay4;

			try {
				// dtvisit2 =
				// formatter.parse(String.format("%04d-%02d-%02d",year,month+1,day));
				dttt1 = formatter.parse(new StringBuilder().append(year4)
						.append("-").append(month4 + 1).append("-")
						.append(day4).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar mc = Calendar.getInstance();

			if (c.after(mc) || dttt1.before(dtreg)) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				tt1.setText(String.format("%d-%s-%d", day4,
						DBAdapter.hmstr[month4], year4));
				strtt1 = new StringBuilder().append(year4).append("-")
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

		date_tt2 = (Button) findViewById(R.id.date_tt2);

		date_tt2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID5);

			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener5 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear5,
				int selectedMonth5, int selectedDay5) {
			Calendar c = Calendar.getInstance();
			c.set(selectedYear5, selectedMonth5, selectedDay5);
			year5 = selectedYear5;
			month5 = selectedMonth5;
			day5 = selectedDay5;

			try {
				// dtvisit2 =
				// formatter.parse(String.format("%04d-%02d-%02d",year,month+1,day));
				// dttt1 = formatter.parse(tt1.getText().toString());
				dttt2 = formatter.parse(new StringBuilder().append(year5)
						.append("-").append(month5 + 1).append("-")
						.append(day5).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar mc = Calendar.getInstance();

			if (c.after(mc) || dttt2.before(dttt1)) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				tt2.setText(String.format("%d-%s-%d", day5,
						DBAdapter.hmstr[month5], year5));
				strtt2 = new StringBuilder().append(year5).append("-")
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

		date_booster = (Button) findViewById(R.id.date_booster);

		date_booster.setOnClickListener(new OnClickListener() {

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
			booster.setText(String.format("%d-%s-%d", day6,
					DBAdapter.hmstr[month6], year6));
			strbooster = new StringBuilder().append(year6).append("-")
					.append(month6 + 1).append("-").append(day6).toString();

		}
	};

	public void setCurrentDateOnView7() {

		final Calendar c = Calendar.getInstance();
		year7 = c.get(Calendar.YEAR);
		month7 = c.get(Calendar.MONTH);
		day7 = c.get(Calendar.DAY_OF_MONTH);

		// ttdate2.setText(new
		// StringBuilder().append(day6).append("-").append(month6 +
		// 1).append("-").append(year6).append(" "));

	}

	public void addListenerOnButton7() {

		date_abortation = (Button) findViewById(R.id.date_abortation);

		date_abortation.setOnClickListener(new OnClickListener() {

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
			abortation_date.setText(String.format("%d-%s-%d", day7,
					DBAdapter.hmstr[month7], year7));
			strabortation_date = new StringBuilder().append(year7).append("-")
					.append(month7 + 1).append("-").append(day7).toString();

		}
	};

	public void setCurrentDateOnView8() {

		final Calendar c = Calendar.getInstance();
		year8 = c.get(Calendar.YEAR);
		month8 = c.get(Calendar.MONTH);
		day8 = c.get(Calendar.DAY_OF_MONTH);

		// ttdate2.setText(new
		// StringBuilder().append(day6).append("-").append(month6 +
		// 1).append("-").append(year6).append(" "));

	}

	public void addListenerOnButton8() {

		date_death = (Button) findViewById(R.id.date_death);

		date_death.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID8);

			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener8 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear8,
				int selectedMonth8, int selectedDay8) {
			year8 = selectedYear8;
			month8 = selectedMonth8;
			day8 = selectedDay8;
			death_date.setText(String.format("%d-%s-%d", day8,
					DBAdapter.hmstr[month8], year8));
			strdeath_date = new StringBuilder().append(year8).append("-")
					.append(month8 + 1).append("-").append(day8).toString();

		}
	};

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
