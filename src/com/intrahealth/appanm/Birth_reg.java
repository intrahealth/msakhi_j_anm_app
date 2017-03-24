package com.intrahealth.appanm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.intrahealth.appanm.GoogleAnalyticsApp.TrackerName;
import com.intrahealth.appanm.control.NumberPicker;
import com.intrahealth.msakhi.appanm.R;

public class Birth_reg extends Activity {

	private DBAdapter mydb;
	EditText child_id, mother_id, child_name, mother_name, father_name,
			mobile_no;
	TextView reg_date, birth_date;
	String strreg_date, strlmp, m_stat, c_stat, dob, pob, weight, last_visit,
			m_death, c_death, strdob;
	Button date_reg, date_birth, submit;
	NumberPicker birth_weight;
	Dialog myDialog;
	RadioGroup birth_place, religion, caste, child_sex, breastfeding,
			vaccination1;
	CheckBox bcg, opv, hepatitisb, vitamink;
	String asha_id, id, pname, pdob, psex, pweight, anm_stat, phname, pmobile,
			pcaste, preligion;
	Cursor c, c1;
	String strmid;
	Date dtdob;
	int m;
	Context con;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");

	RadioButton radiobutton, hospital, home, hindu, muslim, sikh, other, sc,
			st, obc, other1;
	DataSetter ds = new DataSetter();

	private int year;
	private int month;
	private int day;
	private int year1;
	private int month1;
	private int day1;

	static final int DATE_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID1 = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.birth_reg);

		Tracker t = ((GoogleAnalyticsApp) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Birth Reg");
		t.send(new HitBuilders.AppViewBuilder().build());

		con = this;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			asha_id = extras.getString("asha_id");
			id = String.valueOf(extras.getInt("pid"));
			pname = extras.getString("pname");
			pdob = extras.getString("pdob");
			// m=extras.getInt("m_stat");
			pweight = extras.getString("pweight");
			psex = extras.getString("psex");
			anm_stat = extras.getString("anm_stat");
			phname = extras.getString("phname");
			pmobile = String.valueOf(extras.getLong("pmobile"));
			preligion = String.valueOf(extras.getInt("preligion"));
			pcaste = String.valueOf(extras.getInt("pcaste"));

		}
		// showAlert(pweight + " "+ psex+" "+ anm_stat);
		try {
			dtdob = formatter.parse(pdob);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		child_id = (EditText) findViewById(R.id.child_id);
		child_name = (EditText) findViewById(R.id.child_name);
		mother_id = (EditText) findViewById(R.id.mother_id);
		// mother_id.setText(m);
		mother_name = (EditText) findViewById(R.id.mother_name);
		mother_name.setText(pname);
		father_name = (EditText) findViewById(R.id.father_name);
		father_name.setText(phname);
		mobile_no = (EditText) findViewById(R.id.mobile_no);
		mobile_no.setText(pmobile);
		reg_date = (TextView) findViewById(R.id.reg_date);
		birth_date = (TextView) findViewById(R.id.birth_date);
		birth_date.setText(DBAdapter.strtodate(pdob));
		birth_weight = (NumberPicker) findViewById(R.id.birth_weight);
		if (pweight.isEmpty()) {
			pweight = "0";
			birth_weight.setValue(Float.parseFloat(pweight));
		}
		birth_place = (RadioGroup) findViewById(R.id.birth_place);
		religion = (RadioGroup) findViewById(R.id.religion);
		caste = (RadioGroup) findViewById(R.id.caste);
		child_sex = (RadioGroup) findViewById(R.id.child_sex);
		RadioButton boy = (RadioButton) findViewById(R.id.boy);
		RadioButton girl = (RadioButton) findViewById(R.id.girl);
		sc = (RadioButton) findViewById(R.id.sc);
		st = (RadioButton) findViewById(R.id.st);
		obc = (RadioButton) findViewById(R.id.obc);
		other1 = (RadioButton) findViewById(R.id.other1);
		hindu = (RadioButton) findViewById(R.id.hindu);
		muslim = (RadioButton) findViewById(R.id.muslim);
		sikh = (RadioButton) findViewById(R.id.sikh);
		other = (RadioButton) findViewById(R.id.other);
		home = (RadioButton) findViewById(R.id.home);
		hospital = (RadioButton) findViewById(R.id.hospital);
		switch (Integer.parseInt(preligion)) {

		case 0:
			ds.setReligion("0");

			hindu.setChecked(true);
			break;
		case 1:
			ds.setReligion("1");

			muslim.setChecked(true);
			break;
		case 2:
			ds.setReligion("2");

			sikh.setChecked(true);
			break;
		case 3:
			ds.setReligion("3");

			other.setChecked(true);
			break;

		}
		switch (Integer.parseInt(pcaste)) {

		case 0:
			ds.setCaste("0");

			sc.setChecked(true);
			break;
		case 1:
			ds.setCaste("0");

			st.setChecked(true);
			break;
		case 2:
			ds.setCaste("0");

			obc.setChecked(true);
			break;
		case 3:
			ds.setCaste("0");

			other1.setChecked(true);
			break;

		}
		if (psex.equalsIgnoreCase("B")) {
			boy.setChecked(true);
		} else {
			girl.setChecked(true);
		}
		breastfeding = (RadioGroup) findViewById(R.id.breastfeding);
		vaccination1 = (RadioGroup) findViewById(R.id.vaccination);

		date_reg = (Button) findViewById(R.id.date_reg);
		date_birth = (Button) findViewById(R.id.date_birth);

		setCurrentDateOnView();
		addListenerOnButton();
		setCurrentDateOnView1();
		addListenerOnButton1();

		mydb = DBAdapter.getInstance(getApplicationContext());

		c1 = mydb.getVhsndPreg(asha_id, id);
		if (c1.moveToFirst()) {

			// mother_id.setText(c.getString(c.getColumnIndex("mother_mcts")));
			// strmid = (c.getString(c.getColumnIndex("mother_mcts")));
			// Log.d("id of mother",strmid+"");

			// Add hero
			mother_id.setText(c1.getString(c1.getColumnIndex("mother_mcts")));
			strmid = (c1.getString(c1.getColumnIndex("mother_mcts")));
			Log.d("id of mother", strmid + "");
		}
		c = mydb.getAnmPreg(asha_id, id);
		if (c.moveToFirst()) {
			// name = c.getString(c.getColumnIndex("name"));
			// Hero Comment
			strdob = c.getString(c.getColumnIndex("dob"));

			strlmp = c.getString(c.getColumnIndex("lmp"));
			m_stat = c.getString(c.getColumnIndex("m_stat"));
			c_stat = c.getString(c.getColumnIndex("c_stat"));
			pob = c.getString(c.getColumnIndex("pob"));
			weight = c.getString(c.getColumnIndex("weight"));
			last_visit = c.getString(c.getColumnIndex("last_visit"));
			m_death = c.getString(c.getColumnIndex("m_death"));
			c_death = c.getString(c.getColumnIndex("c_death"));
		}

		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int selectedId2 = religion.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId2);
				switch (selectedId2) {
				case R.id.hindu:
					ds.setReligion("0");
					break;
				case R.id.muslim:
					ds.setReligion("1");
					break;
				case R.id.sikh:
					ds.setReligion("2");
					break;
				case R.id.other:
					ds.setReligion("3");
					break;
				}

				int selectedId3 = caste.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId3);
				switch (selectedId3) {
				case R.id.sc:
					ds.setCaste("0");
					break;
				case R.id.st:
					ds.setCaste("1");
					break;
				case R.id.obc:
					ds.setCaste("2");
					break;
				case R.id.other1:
					ds.setCaste("3");
					break;
				}
				// TODO Auto-generated method stub
				if (child_id.getText().toString().length() != 18) {
					showAlert("सही एम०सी०टी०एस० आई०डी० डाले ");
				} else if (ds.getReligion() == null
						|| ds.getReligion().equalsIgnoreCase("")
						|| ds.getCaste() == null
						|| ds.getCaste().equalsIgnoreCase("")) {
					showAlert("प्लीज  एंटर  कासते  एंड   रिलिजन ");
				}

				else {
					int selectedId1 = birth_place.getCheckedRadioButtonId();
					radiobutton = (RadioButton) findViewById(selectedId1);
					switch (selectedId1) {
					case R.id.hospital:
						ds.setBirth_place("Hospital");
						break;
					case R.id.home:
						ds.setBirth_place("Home");
						break;
					}

					int selectedId21 = religion.getCheckedRadioButtonId();
					radiobutton = (RadioButton) findViewById(selectedId21);
					switch (selectedId21) {
					case R.id.hindu:
						ds.setReligion("0");
						break;
					case R.id.muslim:
						ds.setReligion("1");
						break;
					case R.id.sikh:
						ds.setReligion("2");
						break;
					case R.id.other:
						ds.setReligion("3");
						break;
					}

					int selectedId31 = caste.getCheckedRadioButtonId();
					radiobutton = (RadioButton) findViewById(selectedId31);
					switch (selectedId31) {
					case R.id.sc:
						ds.setCaste("0");
						break;
					case R.id.st:
						ds.setCaste("1");
						break;
					case R.id.obc:
						ds.setCaste("2");
						break;
					case R.id.other1:
						ds.setCaste("3");
						break;
					}

					int selectedId4 = child_sex.getCheckedRadioButtonId();
					radiobutton = (RadioButton) findViewById(selectedId4);
					switch (selectedId4) {
					case R.id.boy:
						ds.setChild_sex("Boy");
						break;
					case R.id.girl:
						ds.setChild_sex("Girl");
						break;
					}

					int selectedId5 = breastfeding.getCheckedRadioButtonId();
					radiobutton = (RadioButton) findViewById(selectedId5);
					switch (selectedId5) {
					case R.id.breastfedingy:
						ds.setBreastfeding("Yes");
						break;
					case R.id.breastfedingn:
						ds.setBreastfeding("No");
						break;
					}

					int selectedId6 = vaccination1.getCheckedRadioButtonId();
					radiobutton = (RadioButton) findViewById(selectedId6);
					switch (selectedId6) {
					case R.id.vaccinationy:
						ds.setVaccination1(ds.getVaccination());
						break;
					case R.id.vaccinationn:
						ds.setVaccination1(ds.getVaccination());
						break;
					}

					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							con);

					alertDialog.setTitle("Save Data...");

					alertDialog.setMessage("Are you sure you want save data?");

					// alertDialog.setIcon(R.drawable.delete);

					alertDialog.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									if (strmid == null
											|| strmid.equalsIgnoreCase("")) {
										Log.e("Child_val", child_id
												.getEditableText().toString());
										Long returnvalue2 = mydb
												.insertBirthOther(
														asha_id,
														id,
														child_id.getEditableText()
																.toString(),
														child_name
																.getEditableText()
																.toString(),
														ds.getBreastfeding(),
														mother_id.getText()
																.toString(),
														ds.getChild_sex(),
														ds.getCaste(),
														ds.getBirth_place(),
														ds.getReligion(),
														Float.parseFloat(pweight));
										boolean returnvalu = mydb.updateChildAnmStat(
												asha_id, id, mother_name
														.getText().toString(),
												father_name.getText()
														.toString(), mobile_no
														.getText().toString(),
												ds.getChild_sex(), ds
														.getCaste(), ds
														.getBirth_place(), ds
														.getReligion(), Float
														.parseFloat(pweight));
										Toast.makeText(getApplicationContext(),
												"जानकारी बदल गयी",
												Toast.LENGTH_SHORT).show();
										finish();
										try {
											JSONObject payload = new JSONObject();
											payload.put("aid", asha_id);
											payload.put("id", id);
											payload.put("child_mcts", child_id
													.getText().toString());
											payload.put("child_name",
													child_name.getText()
															.toString());
											payload.put("breastfeeding",
													ds.getBreastfeding());
											payload.put("mother_mcts",
													mother_id.getText()
															.toString());
											mydb.sendGPRS("/aprego",
													payload.toString(), 0);
											JSONObject payload1 = new JSONObject();
											payload1.put("aid", asha_id);
											payload1.put("id", id);
											payload1.put("name", mother_name
													.getText().toString());
											payload1.put("hname", father_name
													.getText().toString());
											payload1.put("mobile", mobile_no
													.getText().toString());
											payload1.put("caste", ds.getCaste());
											payload1.put("religion",
													ds.getReligion());
											payload1.put("m_stat", m_stat);
											payload1.put("c_stat", c_stat);
											payload1.put("dob", dob);
											payload1.put("pob",
													ds.getBirth_place());
											payload1.put("sex",
													ds.getChild_sex());
											payload1.put("weight",
													Float.parseFloat(pweight));
											payload1.put("last_visit",
													last_visit);
											payload1.put("m_death", m_death);
											payload1.put("c_death", c_death);
											payload1.put("lmp", strlmp);

											mydb.sendGPRS("apreg",
													payload1.toString(), 1);
											Toast.makeText(
													getApplicationContext(),
													"पंजीकरण हो गया",
													Toast.LENGTH_SHORT).show();

										} catch (Exception e) {
											Log.d("error", e.getMessage());
											Toast.makeText(
													getApplicationContext(),
													e.getMessage(),
													Toast.LENGTH_SHORT).show();
										}
									} else {
										Log.d("mother_cts", strmid + "");
										mydb.anmbirth(
												asha_id,
												id,
												child_id.getText().toString(),
												child_name.getText().toString(),
												ds.getBreastfeding(), strmid);
										mydb.updateChildAnmStat(asha_id, id,
												mother_name.getText()
														.toString(),
												father_name.getText()
														.toString(), mobile_no
														.getText().toString(),
												ds.getChild_sex(), ds
														.getCaste(), pob, ds
														.getReligion(), Float
														.parseFloat(pweight));
										Toast.makeText(getApplicationContext(),
												"जानकारी बदल गयी",
												Toast.LENGTH_SHORT).show();

										try {
											JSONObject payload = new JSONObject();
											/*
											 * payload.put("aid", asha_id);
											 * payload.put("id", id);
											 * payload.put("child_mcts",
											 * child_id.getText().toString());
											 * payload.put("child_name",
											 * child_name.getText().toString());
											 * payload.put("breastfeeding",
											 * ds.getBreastfeding());
											 */
											c = mydb.getUpdatePrego(asha_id, id);
											if (c.moveToFirst()) {

												payload.put("aid", asha_id);
												payload.put("id", id);
												payload.put(
														"mother_mcts",
														c.getString(c
																.getColumnIndex("mother_mcts")));
												payload.put(
														"aadhar_card",
														c.getString(c
																.getColumnIndex("aadhar_card")));
												payload.put(
														"bank_name",
														c.getString(c
																.getColumnIndex("bank_name")));
												payload.put(
														"branch",
														c.getString(c
																.getColumnIndex("branch")));
												payload.put(
														"jsy",
														c.getString(c
																.getColumnIndex("jsy")));
												payload.put(
														"bpl",
														c.getString(c
																.getColumnIndex("bpl")));
												payload.put(
														"age",
														c.getString(c
																.getColumnIndex("age")));
												payload.put(
														"reg_date",
														c.getString(c
																.getColumnIndex("reg_date")));
												payload.put(
														"month_reg",
														c.getString(c
																.getColumnIndex("month_reg")));
												payload.put(
														"week_reg",
														c.getString(c
																.getColumnIndex("week_reg")));
												payload.put(
														"mother_weight",
														c.getString(c
																.getColumnIndex("mother_weight")));
												payload.put(
														"blood_group",
														c.getString(c
																.getColumnIndex("blood_group")));
												payload.put(
														"past_health",
														c.getString(c
																.getColumnIndex("past_health")));
												payload.put(
														"total_preg",
														c.getString(c
																.getColumnIndex("total_preg")));
												payload.put(
														"lastpreg_result",
														c.getString(c
																.getColumnIndex("lastpreg_result")));
												payload.put(
														"lastpreg_comp",
														c.getString(c
																.getColumnIndex("lastpreg_comp")));
												payload.put(
														"ltolpreg_result",
														c.getString(c
																.getColumnIndex("ltolpreg_result")));
												payload.put(
														"ltolpreg_comp",
														c.getString(c
																.getColumnIndex("ltolpreg_comp")));
												payload.put(
														"delivery_plan",
														c.getString(c
																.getColumnIndex("delivery_plan")));
												payload.put(
														"vdrl",
														c.getString(c
																.getColumnIndex("vdrl")));
												payload.put(
														"hiv",
														c.getString(c
																.getColumnIndex("hiv")));
												payload.put(
														"visit1_date",
														c.getString(c
																.getColumnIndex("visit1_date")));
												payload.put(
														"visit2_date",
														c.getString(c
																.getColumnIndex("visit2_date")));
												payload.put(
														"visit3_date",
														c.getString(c
																.getColumnIndex("visit3_date")));
												payload.put(
														"visit4_date",
														c.getString(c
																.getColumnIndex("visit4_date")));
												payload.put(
														"abortion",
														c.getString(c
																.getColumnIndex("abortion")));
												payload.put(
														"tt1_date",
														c.getString(c
																.getColumnIndex("tt1_date")));
												payload.put(
														"tt2_date",
														c.getString(c
																.getColumnIndex("tt2_date")));
												payload.put(
														"ttbooster_date",
														c.getString(c
																.getColumnIndex("ttbooster_date")));
												payload.put(
														"reffered",
														c.getString(c
																.getColumnIndex("reffered")));
												payload.put(
														"danger_signs",
														c.getString(c
																.getColumnIndex("danger_signs")));
												payload.put(
														"anc_visit",
														c.getString(c
																.getColumnIndex("anc_visit")));
												payload.put("child_mcts",
														child_id.getText()
																.toString());
												payload.put("breastfeeding",
														ds.getBreastfeding());
												payload.put("child_name",
														child_name.getText()
																.toString());
												payload.put(
														"bcg",
														c.getString(c
																.getColumnIndex("bcg")));
												payload.put(
														"opv1",
														c.getString(c
																.getColumnIndex("opv1")));
												payload.put(
														"dpt1",
														c.getString(c
																.getColumnIndex("dpt1")));
												payload.put(
														"hepb1",
														c.getString(c
																.getColumnIndex("hepb1")));
												payload.put(
														"opv2",
														c.getString(c
																.getColumnIndex("opv2")));
												payload.put(
														"dpt2",
														c.getString(c
																.getColumnIndex("dpt2")));
												payload.put(
														"hepb2",
														c.getString(c
																.getColumnIndex("hepb2")));
												payload.put(
														"opv3",
														c.getString(c
																.getColumnIndex("opv3")));
												payload.put(
														"dpt3",
														c.getString(c
																.getColumnIndex("dpt3")));
												payload.put(
														"hepb3",
														c.getString(c
																.getColumnIndex("hepb3")));
												payload.put(
														"measeals",
														c.getString(c
																.getColumnIndex("measeals")));
												payload.put(
														"vitamina",
														c.getString(c
																.getColumnIndex("vitamina")));
												payload.put("child_caste",
														ds.getCaste());
												payload.put("child_religion",
														ds.getReligion());
												payload.put("child_sex",
														ds.getChild_sex());
												payload.put(
														"child_weight",
														Float.parseFloat(pweight));
												payload.put("child_pob",
														ds.getBirth_place());
											}

											mydb.sendGPRS("/aprego",
													payload.toString(), 0);

											JSONObject payload1 = new JSONObject();
											payload1.put("aid", asha_id);
											payload1.put("id", id);
											payload1.put("name", mother_name
													.getText().toString());
											payload1.put("hname", father_name
													.getText().toString());
											payload1.put("mobile", mobile_no
													.getText().toString());

											mydb.sendGPRS("apreg",
													payload1.toString(), 1);
											Toast.makeText(
													getApplicationContext(),
													"जानकारी बदल गयी",
													Toast.LENGTH_SHORT).show();
											// Toast.makeText(getApplicationContext(),"पंजीकरण हो गया",
											// Toast.LENGTH_SHORT).show();
											// Toast.makeText(getApplicationContext(),
											// "Successfully uploaded",
											// Toast.LENGTH_SHORT).show();
										} catch (Exception e) {
											Log.d("error", e.getMessage());
											Toast.makeText(
													getApplicationContext(),
													e.getMessage(),
													Toast.LENGTH_SHORT).show();
										}
										finish();
									}

								}
							});

					alertDialog.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.cancel();
								}
							});

					alertDialog.show();

					// mydb.(asha_id, id, child_id.getText().toString(),
					// child_name.getText().toString(), ds.getBreastfeding(),
					// ds.setVaccination1() ,ds.setChild_sex(), ds.setCaste(),
					// ds.setReligion(), ds.setBirth_place());

				}
			}
		});

	}

	public void showAlert(String msg) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle("अलर्ट ")
				.setMessage(msg)
				.setPositiveButton("ओके ",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								// Some stuff to do when ok got clicked
							}
						})

				.show();
	}

	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {

		case R.id.vaccinationy:
			if (checked) {

				myDialog = new Dialog(Birth_reg.this);
				myDialog.setContentView(R.layout.immunized_birth);
				myDialog.setCancelable(true);
				bcg = (CheckBox) myDialog.findViewById(R.id.bcg);
				opv = (CheckBox) myDialog.findViewById(R.id.opv);
				vitamink = (CheckBox) myDialog.findViewById(R.id.vitamink);
				hepatitisb = (CheckBox) myDialog.findViewById(R.id.hepatitisb);

				Button ok = (Button) myDialog.findViewById(R.id.ok);

				ok.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						StringBuffer result = new StringBuffer();
						if (opv.isChecked()) {
							result = result.append("OPV");
						}
						if (bcg.isChecked()) {
							result = result.append(" BCG");
						}
						if (vitamink.isChecked()) {
							result = result.append("  vitamink");
						}
						if (hepatitisb.isChecked()) {
							result = result.append("  hepatitisb");
						}

						ds.setVaccination(result.toString());
						myDialog.dismiss();
					}
				});

				myDialog.show();

			}
		}
	}

	public void setCurrentDateOnView() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		// reg_date.setText(new
		// StringBuilder().append(year).append("-").append(month +
		// 1).append("-").append(day).append(" "));
		// reg_date.setText(DBAdapter.strtodate(reg_date.getText().toString()));

	}

	public void addListenerOnButton() {

		date_reg = (Button) findViewById(R.id.date_reg);

		date_reg.setOnClickListener(new OnClickListener() {

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
			// set selected date into textview
			Date dtreg = null;
			try {
				dtreg = formatter.parse(new StringBuilder().append(year)
						.append("-").append(month + 1).append("-").append(day)
						.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (c.after(mc) || dtreg.before(dtdob)) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {
				reg_date.setText(new StringBuilder().append(year).append("-")
						.append(month + 1).append("-").append(day).append(" "));
				reg_date.setText(DBAdapter.strtodate(reg_date.getText()
						.toString()));

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

		date_birth = (Button) findViewById(R.id.date_birth);

		date_birth.setOnClickListener(new OnClickListener() {

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

			birth_date.setText(new StringBuilder().append(day1).append("-")
					.append(month1 + 1).append("-").append(year1).append(" "));

		}
	};

	public void showMessage(String title, String message) {
		Builder builder = new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(Birth_reg.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(Birth_reg.this).reportActivityStop(this);
	}

}
