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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.intrahealth.appanm.GoogleAnalyticsApp.TrackerName;
import com.intrahealth.appanm.control.NumberPicker;
import com.intrahealth.appanm.control.NumberPickerInt;
import com.intrahealth.msakhi.appanm.R;

public class Preg_reg extends Activity {
	private DBAdapter mydb;
	Cursor c;

	EditText name, hname, mcts_id, adhar, branch, mobile;
	TextView lmp_date, reg_date, reg_month, reg_week;
	String strreg_date;
	Button date_lmp, date_reg, submit, submit2, save;
	NumberPickerInt age, total_preg;
	NumberPicker reg_weight;
	String asha_id, pid, strlmp, m_stat, c_stat, dob, pob, weight, last_visit,
			m_death, c_death;
	Dialog myDialog;
	DataSetter ds = new DataSetter();
	Spinner blood_group, planned, bank_name;
	RadioGroup religion, caste, bpl, result_preg, result_preg1, vdrl, hiv, jsy,
			past_health, preg_comp, preg_comp1;
	String religion1, caste1, bpl1, result_preg0, result_preg11, vdrl1, hiv1,
			jsy1, past_health1, str_preg_comp, str_preg_comp1;
	// AbsoluteLayout AbsoluteLayout1, AbsoluteLayout2;
	LinearLayout LinearLayout1, LinearLayout2, buttonsave, buttonnext;
	Context con;
	RadioButton radiobutton;
	String pname, phname, pmobile, pcaste, preligion, server_id, dor, awc_id;
	Date dtlmp;
	RadioButton s_Bpl, s_Apl, jsyy, jsyn;
	CheckBox cb_adhar_dnk, cb_bankn_dnk, cb_bankA_dnk;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");
	CheckBox dn_adahar, dn_bank, dn_bank_account;
	private int year;
	private int month;
	private int day;
	private int year1;
	private int month1;
	private int day1;
	boolean dateflag = false;
	private RadioButton dn_jsy, dn_pl;

	static final int DATE_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID1 = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preg_reg);

		Tracker t = ((GoogleAnalyticsApp) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Preg Reg");
		t.send(new HitBuilders.AppViewBuilder().build());

		con = this;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			asha_id = extras.getString("asha_id");
			pid = String.valueOf(extras.getInt("pid"));
			pname = extras.getString("pname");
			// asha_id=extras.getString("asha_id");
			phname = extras.getString("phname");
			pmobile = String.valueOf(extras.getLong("pmobile"));
			preligion = String.valueOf(extras.getInt("preligion"));
			pcaste = String.valueOf(extras.getInt("pcaste"));
			Log.e("re", "Preg_reg  " + preligion);
			Log.e("caste", "Preg_reg  " + pcaste);

		}
		mydb = DBAdapter.getInstance(getApplicationContext());
		// c=mydb.getAnmPreg(asha_id, pid);
		/*
		 * if (c.moveToFirst()) {
		 * //name.setText("Date of visit :"+c.getString(c.
		 * getColumnIndex("name"))); viv =
		 * c.getString(c.getColumnIndex("name")); }
		 */

		cb_adhar_dnk = (CheckBox) findViewById(R.id.donotknow_adhar);
		cb_bankn_dnk = (CheckBox) findViewById(R.id.donotknow_bankname);
		cb_bankA_dnk = (CheckBox) findViewById(R.id.donotknow_bank_account);
		cb_adhar_dnk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (cb_adhar_dnk.isChecked()) {
					adhar.setEnabled(false);
				} else {
					adhar.setEnabled(true);
				}

			}
		});
		cb_bankn_dnk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (cb_bankn_dnk.isChecked()) {
					bank_name.setEnabled(false);
				} else {
					bank_name.setEnabled(true);
				}

			}
		});
		cb_bankA_dnk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (cb_bankA_dnk.isChecked()) {
					branch.setEnabled(false);
				} else {
					branch.setEnabled(true);
				}

			}
		});
		name = (EditText) findViewById(R.id.name);
		name.setText(pname);
		hname = (EditText) findViewById(R.id.hname);
		hname.setText(phname);
		s_Bpl = (RadioButton) findViewById(R.id.bply);
		s_Apl = (RadioButton) findViewById(R.id.bpln);
		mcts_id = (EditText) findViewById(R.id.mcts_id);
		adhar = (EditText) findViewById(R.id.adhar);
		branch = (EditText) findViewById(R.id.branch);
		mobile = (EditText) findViewById(R.id.mobile);
		mobile.setText(pmobile);
		reg_date = (TextView) findViewById(R.id.reg_date);
		lmp_date = (TextView) findViewById(R.id.lmp_date);
		age = (NumberPickerInt) findViewById(R.id.age);
		age.setValue(Float.parseFloat("20"));
		total_preg = (NumberPickerInt) findViewById(R.id.total_preg);
		total_preg.SetMax(4);
		reg_weight = (NumberPicker) findViewById(R.id.reg_weight);
		reg_weight.setValue(Float.parseFloat("30"));
		religion = (RadioGroup) findViewById(R.id.religion);
		caste = (RadioGroup) findViewById(R.id.caste);
		bpl = (RadioGroup) findViewById(R.id.bpl);
		vdrl = (RadioGroup) findViewById(R.id.vdrl);
		hiv = (RadioGroup) findViewById(R.id.hiv);
		result_preg = (RadioGroup) findViewById(R.id.result_preg);
		result_preg1 = (RadioGroup) findViewById(R.id.result_preg1);
		jsy = (RadioGroup) findViewById(R.id.jsy);
		blood_group = (Spinner) findViewById(R.id.blood_group);

		reg_month = (TextView) findViewById(R.id.reg_month);
		reg_week = (TextView) findViewById(R.id.reg_week);
		preg_comp = (RadioGroup) findViewById(R.id.preg_comp);
		preg_comp1 = (RadioGroup) findViewById(R.id.preg_comp1);
		jsyy = (RadioButton) findViewById(R.id.jsyy);
		jsyn = (RadioButton) findViewById(R.id.jsyn);
		dn_jsy = (RadioButton) findViewById(R.id.donotknow_jsy);
		dn_pl = (RadioButton) findViewById(R.id.donotknow_pl);
		int selectedId2 = religion.getCheckedRadioButtonId();
		radiobutton = (RadioButton) findViewById(selectedId2);
		switch (Integer.parseInt(preligion)) {

		case 0:
			religion1 = "0";
			RadioButton hindu = (RadioButton) findViewById(R.id.hindu);
			hindu.setChecked(true);
			break;
		case 1:
			religion1 = "1";
			RadioButton muslim = (RadioButton) findViewById(R.id.muslim);
			muslim.setChecked(true);
			break;
		case 2:
			religion1 = "2";
			RadioButton cris = (RadioButton) findViewById(R.id.sikh);
			cris.setChecked(true);
			break;
		case 3:
			religion1 = "3";
			RadioButton other = (RadioButton) findViewById(R.id.other);
			other.setChecked(true);
			break;

		}
		switch (Integer.parseInt(pcaste)) {

		case 0:
			caste1 = "0";
			RadioButton ge = (RadioButton) findViewById(R.id.sc);
			ge.setChecked(true);
			break;
		case 1:
			caste1 = "1";
			RadioButton sc = (RadioButton) findViewById(R.id.st);
			sc.setChecked(true);
			break;
		case 2:
			caste1 = "2";
			RadioButton st = (RadioButton) findViewById(R.id.obc);
			st.setChecked(true);
			break;
		case 3:
			caste1 = "3";
			RadioButton obc = (RadioButton) findViewById(R.id.other1);
			obc.setChecked(true);
			break;

		}

		/*
		 * Float.toString((float)total_preg.getValue()).addTextChangedListener(new
		 * TextWatcher(){ public void afterTextChanged(Editable s) { }
		 * 
		 * public void beforeTextChanged(CharSequence s, int start, int count,
		 * int after) { }
		 * 
		 * public void onTextChanged(CharSequence s, int start, int before, int
		 * count) {
		 * 
		 * 
		 * 
		 * } });
		 */

		List<String> list11 = new ArrayList<String>();
		list11.add("चुने");

		list11.add("A-");
		list11.add("A+");
		list11.add("B+");
		list11.add("B-");
		list11.add("O+");
		list11.add("O-");
		list11.add("AB+");
		list11.add("AB-");
		list11.add("जांच नहीं किया");

		ArrayAdapter<String> dataAdapter11 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list11);
		dataAdapter11
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		blood_group.setAdapter(dataAdapter11);

		past_health = (RadioGroup) findViewById(R.id.past_health);

		planned = (Spinner) findViewById(R.id.planned);
		List<String> list3 = new ArrayList<String>();
		list3.add("--चुनिये--");
		list3.add("जिला अस्पताल");
		list3.add("सी.एच.सी");
		list3.add("पी.एच.सी");
		list3.add("उप-केंद्र");
		list3.add("अन्य-शासकीय संस्था");
		list3.add("मान्यताप्राप्त गैर-शासकीय संस्था");
		list3.add("घर में");
		ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list3);
		dataAdapter3
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		planned.setAdapter(dataAdapter3);

		bank_name = (Spinner) findViewById(R.id.bank_name);
		List<String> list4 = new ArrayList<String>();
		list4.add("--चुनिये--");
		list4.add("एस.बी.आई");
		list4.add("सेंट्रल बैंक");
		list4.add("पी.एन.बी");
		list4.add("एच.डी.ऍफ़.सी");
		list4.add("आई.सी.आई.सी.आई");
		list4.add("सीओ-ऑपरेटिव बैंक");
		list4.add("अलाहाबाद बैंक");
		list4.add("बैंक ऑफ़ बरोडा");
		list4.add("सिटीबैंक");
		list4.add("रानी लक्ष्मीबाई ग्रामीण बैंक");
		ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list4);
		dataAdapter4
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bank_name.setAdapter(dataAdapter4);

		LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
		LinearLayout2 = (LinearLayout) findViewById(R.id.LinearLayout2);
		LinearLayout2.setVisibility(android.view.View.GONE);
		LinearLayout1.setVisibility(android.view.View.VISIBLE);
		buttonnext = (LinearLayout) findViewById(R.id.buttonnext);
		buttonsave = (LinearLayout) findViewById(R.id.buttonsave);
		buttonnext.setVisibility(android.view.View.VISIBLE);
		buttonsave.setVisibility(android.view.View.GONE);
		date_reg = (Button) findViewById(R.id.date_reg);
		date_lmp = (Button) findViewById(R.id.date_lmp);
		c = mydb.getAnmPreg(asha_id, pid);
		if (c.moveToFirst()) {
			name.setText(c.getString(c.getColumnIndex("name")));
			server_id = (c.getString(c.getColumnIndex("server_id")));// later
			dor = (c.getString(c.getColumnIndex("dor")));
			awc_id = (c.getString(c.getColumnIndex("awc_id")));
			lmp_date.setText(DBAdapter.strtodate(c.getString(c
					.getColumnIndex("lmp"))));
			strlmp = c.getString(c.getColumnIndex("lmp"));
			m_stat = c.getString(c.getColumnIndex("m_stat"));
			c_stat = c.getString(c.getColumnIndex("c_stat"));
			pob = c.getString(c.getColumnIndex("pob"));
			weight = c.getString(c.getColumnIndex("weight"));
			last_visit = c.getString(c.getColumnIndex("last_visit"));
			m_death = c.getString(c.getColumnIndex("m_death"));
			c_death = c.getString(c.getColumnIndex("c_death"));
			try {
				dtlmp = formatter.parse(c.getString(c.getColumnIndex("lmp")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setCurrentDateOnView();
		addListenerOnButton();
		setCurrentDateOnView1();
		addListenerOnButton1();

		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				int selectedId2 = religion.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId2);
				switch (selectedId2) {
				case R.id.hindu:
					religion1 = "0";
					break;
				case R.id.muslim:
					religion1 = "1";
					break;
				case R.id.sikh:
					religion1 = "2";
					break;
				case R.id.other:
					religion1 = "3";
					break;
				}

				int selectedId3 = caste.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId3);
				switch (selectedId3) {
				case R.id.sc:
					caste1 = "0";
					break;
				case R.id.st:
					caste1 = "1";
					break;
				case R.id.obc:
					caste1 = "2";
					break;
				case R.id.other1:
					caste1 = "3";
					break;
				}
				Log.e("freg", reg_date.getText().toString());
				if (mcts_id.getText().toString().length() != 18) {
					showAlert("सही एम०सी०टी०एस० आई०डी० डाले! ");
				} else if (caste1 == null || caste1.equalsIgnoreCase("")
						|| religion1 == null || religion1.equalsIgnoreCase("")) {
					showAlert("Please enter Caste and Religion");
				} else if (reg_date.equals(null)
						|| reg_date.getText().toString()
								.equalsIgnoreCase("DD/MM/YYYY")) {
					showAlert("रजिस्ट्रेशन की दिनांक डाले!");
				} else if (!s_Bpl.isChecked() && !s_Apl.isChecked()
						&& !dn_pl.isChecked()) {
					showAlert("गरीबी रेखा डाले!");
				} else if (!jsyy.isChecked() && !jsyn.isChecked()
						&& !dn_jsy.isChecked()) {
					showAlert("जे.एस.वाई डाले!");
				} else {
					LinearLayout2.setVisibility(android.view.View.VISIBLE);
					LinearLayout1.setVisibility(android.view.View.GONE);
					buttonnext.setVisibility(android.view.View.GONE);
					buttonsave.setVisibility(android.view.View.VISIBLE);
					// showAlert(asha_id+" "+pid);
				}

			}
		});

		submit2 = (Button) findViewById(R.id.submit2);
		submit2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				LinearLayout2.setVisibility(android.view.View.GONE);
				LinearLayout1.setVisibility(android.view.View.VISIBLE);
				buttonnext.setVisibility(android.view.View.VISIBLE);
				buttonsave.setVisibility(android.view.View.GONE);

			}
		});

		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int selectedId1 = bpl.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId1);
				switch (selectedId1) {
				case R.id.bply:
					// bpl1.s("Yes");
					bpl1 = "B";
					break;
				case R.id.bpln:
					bpl1 = "A";
				case R.id.donotknow_pl:
					bpl1 = "dn";
					break;
				}

				int selectedId2 = religion.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId2);
				switch (selectedId2) {
				case R.id.hindu:
					religion1 = "0";
					break;
				case R.id.muslim:
					religion1 = "1";
					break;
				case R.id.sikh:
					religion1 = "2";
					break;
				case R.id.other:
					religion1 = "3";
					break;
				}

				int selectedId3 = caste.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId3);
				switch (selectedId3) {
				case R.id.sc:
					caste1 = "0";
					break;
				case R.id.st:
					caste1 = "1";
					break;
				case R.id.obc:
					caste1 = "2";
					break;
				case R.id.other:
					caste1 = "3";
					break;
				}

				int selectedId4 = jsy.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId4);
				switch (selectedId4) {
				case R.id.jsyy:
					jsy1 = "Y";
					break;
				case R.id.jsyn:
					jsy1 = "N";
				case R.id.donotknow_jsy:
					jsy1 = "dnk";
					break;
				}

				int selectedId5 = result_preg.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId5);
				switch (selectedId5) {
				case R.id.alive:
					result_preg0 = "0";
					break;
				case R.id.dead:
					result_preg0 = "1";
					break;
				case R.id.mis:
					result_preg0 = "2";
					break;
				}

				int selectedId6 = result_preg1.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId6);
				switch (selectedId6) {
				case R.id.alive1:
					result_preg11 = "0";
					break;
				case R.id.dead1:
					result_preg11 = "1";
					break;
				case R.id.mis1:
					result_preg11 = "2";
					break;
				}

				int selectedId7 = vdrl.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId7);
				switch (selectedId7) {
				case R.id.vdrly:
					vdrl1 = "Y";
					break;
				case R.id.vdrln:
					vdrl1 = "N";
					break;
				}

				int selectedId8 = hiv.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId8);
				switch (selectedId8) {
				case R.id.hivy:
					hiv1 = "Y";
					break;
				case R.id.hivn:
					hiv1 = "N";
					break;
				}
				int selectedId9 = past_health.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId9);
				switch (selectedId9) {
				case R.id.normal:
					past_health1 = "0";
					break;
				case R.id.hivn:
					past_health1 = ds.getPast_health();
					break;
				}

				int selectedId10 = preg_comp.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId10);
				switch (selectedId10) {
				case R.id.preg_comp_no:
					str_preg_comp = "0";
					break;
				case R.id.preg_comp_yes:
					str_preg_comp = ds.getPreg_comp();
					break;
				}

				int selectedId11 = preg_comp1.getCheckedRadioButtonId();
				radiobutton = (RadioButton) findViewById(selectedId11);
				switch (selectedId11) {
				case R.id.preg_comp1_no:
					str_preg_comp1 = "0";
					break;
				case R.id.preg_comp1_yes:
					str_preg_comp1 = ds.getPreg_comp1();
					break;
				}

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(con);

				alertDialog.setTitle("Save Data...");

				alertDialog.setMessage("Are you sure you want save data?");

				// alertDialog.setIcon(R.drawable.delete);

				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								mydb.insertPregother(asha_id, pid, mcts_id
										.getText().toString(), adhar.getText()
										.toString(), bank_name
										.getSelectedItem().toString(), branch
										.getText().toString(), jsy1, bpl1,
										Float.toString((float) age.getValue()),
										strreg_date, reg_month.getText()
												.toString(), reg_week.getText()
												.toString(), Float
												.toString((float) reg_weight
														.getValue()),
										blood_group.getSelectedItem()
												.toString(), past_health1,
										Float.toString((float) total_preg
												.getValue()), result_preg0,
										str_preg_comp, result_preg11,
										str_preg_comp1, planned
												.getSelectedItem().toString(),
										vdrl1, hiv1);
								mydb.updateAnmStat(asha_id, pid, name.getText()
										.toString(),
										hname.getText().toString(), mobile
												.getTextColors().toString(),
										awc_id, dor);
								Toast.makeText(getApplicationContext(),
										"जानकारी बदल गयी", Toast.LENGTH_SHORT)
										.show();
								finish();
								// Toast.makeText(getApplicationContext(), I,
								// Toast.LENGTH_SHORT).show();

								try {

									// DefaultHttpClient httpClient1 = new
									// DefaultHttpClient();
									// HttpEntity httpEntity1 = null;
									// HttpResponse httpResponse1 = null;
									// HttpPost httpPost1 = new
									// HttpPost("http://192.168.1.29/msakhi/resturl/index.php/aprego");
									// httpResponse1 =
									// httpClient1.execute(httpPost1);
									JSONObject payload = new JSONObject();
									payload.put("aid", asha_id);
									payload.put("id", pid);
									payload.put("mother_mcts", mcts_id
											.getText().toString());
									payload.put("aadhar_card", adhar.getText()
											.toString());
									payload.put("bank_name", bank_name
											.getSelectedItem().toString());
									payload.put("branch", branch.getText()
											.toString());
									payload.put("jsy", jsy1);
									payload.put("bpl", bpl1);
									payload.put("age", Float
											.toString((float) age.getValue()));
									payload.put("reg_date", strreg_date);
									payload.put("month_reg", reg_month
											.getText().toString());
									payload.put("week_reg", reg_week.getText()
											.toString());
									payload.put("mother_weight", Float
											.toString((float) reg_weight
													.getValue()));
									payload.put("blood_group", blood_group
											.getSelectedItem().toString());
									payload.put("past_health", past_health1);
									payload.put("total_preg", Float
											.toString((float) total_preg
													.getValue()));
									payload.put("lastpreg_result", result_preg0);
									payload.put("lastpreg_comp", str_preg_comp);
									payload.put("ltolpreg_result",
											result_preg11);
									payload.put("ltolpreg_comp", str_preg_comp1);
									payload.put("delivery_plan", planned
											.getSelectedItem().toString());
									payload.put("vdrl", vdrl1);
									payload.put("hiv", hiv1);
									payload.put("anc_visit", 0);

									Log.d("oooooooooooo", payload.toString());
									mydb.sendGPRS("/aprego",
											payload.toString(), 0);

									// DefaultHttpClient httpClient = new
									// DefaultHttpClient();
									// HttpEntity httpEntity = null;
									// HttpResponse httpResponse= null;
									// HttpPost httpPost = new
									// HttpPost("http://192.168.1.29/msakhi/resturl/index.php/aprego/preg/"+asha_id+"/"+pid);
									// httpResponse =
									// httpClient.execute(httpPost);

									JSONObject payload1 = new JSONObject();
									payload1.put("server_id", server_id);// later
									payload1.put("aid", asha_id);
									payload1.put("dor", dor);
									payload1.put("awid", awc_id);
									payload1.put("id", pid);
									payload1.put("name", name.getText()
											.toString());
									payload1.put("hname", hname.getText()
											.toString());
									payload1.put("mobile", mobile.getText()
											.toString());
									payload1.put("caste", caste1);
									payload1.put("religion", religion1);
									payload1.put("m_stat", m_stat);
									payload1.put("c_stat", c_stat);
									payload1.put("pob", pob);
									payload1.put("weight", weight);
									payload1.put("last_visit", last_visit);
									payload1.put("m_death", m_death);
									payload1.put("c_death", c_death);
									payload1.put("lmp", strlmp);
									mydb.sendGPRS("apreg", payload1.toString(),
											1);
									// mydb.sendGPRS("/preg/"+asha_id+"/"+pid,
									// payload1.toString(),1);
									Log.d("asha iddddd", asha_id + "pid" + pid);
									Toast.makeText(getApplicationContext(),
											"पंजीकरण हो गया",
											Toast.LENGTH_SHORT).show();

								} catch (Exception e) {
									Log.d("error", e.getMessage());
									Toast.makeText(getApplicationContext(),
											e.getMessage(), Toast.LENGTH_SHORT)
											.show();
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

			}
		});
	}

	private boolean hasConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}
		Toast.makeText(Preg_reg.this, "No network", Toast.LENGTH_LONG).show();
		return false;
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

	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();
		switch (view.getId()) {
		case R.id.abnormal:
			if (checked) {
				myDialog = new Dialog(Preg_reg.this);
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
						if (checkBox8.isChecked()) {
							result = result.append("8");
						}
						if (checkBox9.isChecked()) {
							result = result.append("9");
						}

						ds.setPast_health(result.toString());

						myDialog.dismiss();
					}
				});

				myDialog.show();

			}
			break;

		case R.id.preg_comp_yes:
			if (checked) {

				myDialog = new Dialog(Preg_reg.this);
				myDialog.setContentView(R.layout.preg_comp);
				myDialog.setCancelable(true);
				final CheckBox check1 = (CheckBox) myDialog
						.findViewById(R.id.check1);
				final CheckBox check2 = (CheckBox) myDialog
						.findViewById(R.id.check2);
				final CheckBox check3 = (CheckBox) myDialog
						.findViewById(R.id.check3);
				final CheckBox check4 = (CheckBox) myDialog
						.findViewById(R.id.check4);
				final CheckBox check5 = (CheckBox) myDialog
						.findViewById(R.id.check5);
				final CheckBox check6 = (CheckBox) myDialog
						.findViewById(R.id.check6);
				final CheckBox check7 = (CheckBox) myDialog
						.findViewById(R.id.check7);
				final CheckBox check8 = (CheckBox) myDialog
						.findViewById(R.id.check8);
				final CheckBox check9 = (CheckBox) myDialog
						.findViewById(R.id.check9);
				final CheckBox check10 = (CheckBox) myDialog
						.findViewById(R.id.check10);
				final CheckBox check11 = (CheckBox) myDialog
						.findViewById(R.id.check11);

				Button ok = (Button) myDialog.findViewById(R.id.ok);

				ok.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						StringBuffer result = new StringBuffer();
						if (check1.isChecked()) {
							result = result.append("1");
						}
						if (check2.isChecked()) {
							result = result.append("2");
						}
						if (check3.isChecked()) {
							result = result.append("3");
						}
						if (check4.isChecked()) {
							result = result.append("4");
						}
						if (check5.isChecked()) {
							result = result.append("5");
						}
						if (check6.isChecked()) {
							result = result.append("6");
						}
						if (check7.isChecked()) {
							result = result.append("7");
						}
						if (check8.isChecked()) {
							result = result.append("8");
						}
						if (check9.isChecked()) {
							result = result.append("9");
						}
						if (check10.isChecked()) {
							result = result.append("A");
						}
						if (check11.isChecked()) {
							result = result.append("B");
						}

						ds.setPreg_comp(result.toString());

						myDialog.dismiss();
					}
				});

				myDialog.show();

			}
			break;

		case R.id.preg_comp1_yes:
			if (checked) {

				myDialog = new Dialog(Preg_reg.this);
				myDialog.setContentView(R.layout.preg_comp);
				myDialog.setCancelable(true);
				final CheckBox check1 = (CheckBox) myDialog
						.findViewById(R.id.check1);
				final CheckBox check2 = (CheckBox) myDialog
						.findViewById(R.id.check2);
				final CheckBox check3 = (CheckBox) myDialog
						.findViewById(R.id.check3);
				final CheckBox check4 = (CheckBox) myDialog
						.findViewById(R.id.check4);
				final CheckBox check5 = (CheckBox) myDialog
						.findViewById(R.id.check5);
				final CheckBox check6 = (CheckBox) myDialog
						.findViewById(R.id.check6);
				final CheckBox check7 = (CheckBox) myDialog
						.findViewById(R.id.check7);
				final CheckBox check8 = (CheckBox) myDialog
						.findViewById(R.id.check8);
				final CheckBox check9 = (CheckBox) myDialog
						.findViewById(R.id.check9);
				final CheckBox check10 = (CheckBox) myDialog
						.findViewById(R.id.check10);
				final CheckBox check11 = (CheckBox) myDialog
						.findViewById(R.id.check11);

				Button ok = (Button) myDialog.findViewById(R.id.ok);

				ok.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						StringBuffer result = new StringBuffer();
						if (check1.isChecked()) {
							result = result.append("1");
						}
						if (check2.isChecked()) {
							result = result.append("2");
						}
						if (check3.isChecked()) {
							result = result.append("3");
						}
						if (check4.isChecked()) {
							result = result.append("4");
						}
						if (check5.isChecked()) {
							result = result.append("5");
						}
						if (check6.isChecked()) {
							result = result.append("6");
						}
						if (check7.isChecked()) {
							result = result.append("7");
						}
						if (check8.isChecked()) {
							result = result.append("8");
						}
						if (check9.isChecked()) {
							result = result.append("9");
						}
						if (check10.isChecked()) {
							result = result.append("A");
						}
						if (check11.isChecked()) {
							result = result.append("B");
						}

						ds.setPreg_comp1(result.toString());

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

		strreg_date = new StringBuilder().append(year).append("-")
				.append(month + 1).append("-").append(day).toString();
		// reg_date.setText(DBAdapter.strtodate(new
		// StringBuilder().append(year).append("-").append(month +
		// 1).append("-").append(day).toString()));

		Date d1 = null;
		Date d2 = null;
		try {
			d1 = new SimpleDateFormat("yyyy-M-dd").parse((String) strreg_date);
			d2 = new SimpleDateFormat("yyyy-M-dd").parse((String) strlmp);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		long diffDays1 = Math.abs(d2.getTime() - d1.getTime());

		long diff_current = diffDays1 / (24 * 60 * 60 * 1000);

		long month = diff_current / 30;
		long week = diff_current / 7;
		// showAlert(Float.toString((float) month));
		reg_month.setText(Float.toString((float) week));

		if (week <= 12) {
			reg_week.setText("Yes");
		} else {
			reg_week.setText("No");
		}

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
			DatePickerDialog dialog1 = new DatePickerDialog(this,
					datePickerListener, year, month, day);

			dialog1.setButton(DialogInterface.BUTTON_NEGATIVE,
					getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEGATIVE) {
								dateflag = true;

								dialog.cancel();
							}
						}
					});
			return dialog1;
		case DATE_DIALOG_ID1:
			// set date picker as current date
			DatePickerDialog dialog = new DatePickerDialog(this,
					datePickerListener1, year1, month1, day1);

			dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEGATIVE) {
								dateflag = true;

								dialog.cancel();
							}
						}
					});
			return dialog;

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
			Date d1 = null;
			Date d2 = null;
			try {
				d1 = new SimpleDateFormat("yyyy-M-dd")
						.parse((String) new StringBuilder().append(year)
								.append("-").append(month + 1).append("-")
								.append(day).toString());
				d2 = new SimpleDateFormat("yyyy-M-dd").parse((String) strlmp);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// set current date into textview
			if (c.after(mc) || d1.before(d2)) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			} else {

				strreg_date = new StringBuilder().append(year).append("-")
						.append(month + 1).append("-").append(day).toString();
				if (dateflag) {
					if (strreg_date.isEmpty())
						reg_date.setText("DD/MM/YYYY");
					dateflag = false;
				} else {
					reg_date.setText(DBAdapter.strtodate(new StringBuilder()
							.append(year).append("-").append(month + 1)
							.append("-").append(day).toString()));

				}
				long diffDays1 = Math.abs(d2.getTime() - d1.getTime());

				long diff_current = diffDays1 / (24 * 60 * 60 * 1000);

				long month = diff_current / 30;
				long week = diff_current / 7;
				// showAlert(Float.toString((float) month));
				reg_month.setText(Float.toString((float) week));

				if (week <= 12) {
					reg_week.setText("Yes");
				} else {
					reg_week.setText("No");
				}
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

		date_lmp = (Button) findViewById(R.id.date_lmp);

		date_lmp.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID1);

			}

		});

	}

	private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYea,
				int selectedMont, int selectedDay) {
			

			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

			Calendar mc1 = Calendar.getInstance();
			mc1.add(Calendar.MONTH, -13);

			if (c.after(c) || c.before(mc1)) {
				Toast.makeText(getBaseContext(), "गलत तारीख",
						Toast.LENGTH_SHORT).show();
			}

			else {

				lmp_date.setText(new StringBuilder().append(day1).append("-")
						.append(month1 + 1).append("-").append(year1)
						.append(" "));

				String dd1 = (new StringBuilder().append(day).append("-")
						.append(month + 1).append("-").append(year).append(" "))
						.toString().trim();
				// String dd1 = "28-06-2013";
				String dd2 = lmp_date.getText().toString().trim();

				Date d1 = null;
				try {
					d1 = new SimpleDateFormat("dd-M-yyyy").parse((String) dd1);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Date d2 = null;
				try {
					d2 = new SimpleDateFormat("dd-M-yyyy").parse((String) dd2);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				long diffDays1 = Math.abs(d2.getTime() - d1.getTime());

				long diff_current = diffDays1 / (24 * 60 * 60 * 1000);

				long month = diff_current / 30;
				long week = diff_current / 7;
				// showAlert(Float.toString((float) month));
				reg_month.setText(Float.toString((float) month));

				if (week <= 12) {
					reg_week.setText("Yes");
				} else {
					reg_week.setText("No");
				}

			}

		}
	};

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(Preg_reg.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(Preg_reg.this).reportActivityStop(this);
	}
}
