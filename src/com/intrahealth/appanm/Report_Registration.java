package com.intrahealth.appanm;

//import com.intrahealth.mnewborncare.control.BadgeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.intrahealth.msakhi.appanm.R;

public class Report_Registration extends Activity {

	Intent i;
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
	Button from_date, to_date;
	TextView tv_from_date, tv_to_date;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.report_registration);
		Button submit = (Button) findViewById(R.id.submit);
		Button from_date = (Button) findViewById(R.id.from_date);
		Button to_date = (Button) findViewById(R.id.to_date);
		tv_from_date = (TextView) findViewById(R.id.tv_from_date);
		tv_to_date = (TextView) findViewById(R.id.tv_to_date);
		addListenerOnButton(from_date, 1);
		addListenerOnButton(to_date, 2);
		// updateLabel(tv_from_date);
		// updateLabel(tv_to_date);
		submit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				int rep_ind = 0;
				Bundle extras = getIntent().getExtras();
				if (extras != null)
					rep_ind = extras.getInt("rep_ind");
				String from = tv_from_date.getText().toString();
				String to = tv_to_date.getText().toString();
				if (tv_from_date.getText().toString().equalsIgnoreCase("")
						|| tv_to_date.getText().toString().equalsIgnoreCase("")) {
					if (rep_ind == 11) {
						Intent intent = new Intent(getApplicationContext(),
								PregnantNew1.class);
						intent.putExtra("rep_ind", 8);
						startActivity(intent);
					} else if (rep_ind == 12) {
						Intent intent = new Intent(getApplicationContext(),
								Asha_list.class);
						intent.putExtra("rep_ind", 12);
						intent.putExtra("from", "");
						intent.putExtra("to", "");
						startActivity(intent);
					} else {
						Toast.makeText(getBaseContext(),
								"कृपया तिथि का चयन करें।", Toast.LENGTH_SHORT)
								.show();
					}

				} else {
					Intent intent = new Intent(getApplicationContext(),
							Asha_list.class);
					from = changeformat(from);
					to = changeformat(to);
					intent.putExtra("rep_ind", rep_ind);
					intent.putExtra("from", from);
					intent.putExtra("to", to);

					startActivity(intent);
				}
			}
		});

	}

	public String changeformat(String ss) {

		if (ss.contains("-")) {
			String[] ar = ss.split("-");
			if (ar.length == 3) {
				ss = ar[2] + "-" + ar[1] + "-" + ar[0];

			}
		}
		return ss;
	}

	public void addListenerOnButton(Button date_reg, int flag) {
		if (flag == 1) {
			date_reg.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					final Calendar c = Calendar.getInstance();
					year = c.get(Calendar.YEAR);
					month = c.get(Calendar.MONTH);
					day = c.get(Calendar.DAY_OF_MONTH);

					c.set(Calendar.YEAR, year);
					c.set(Calendar.MONTH, month);
					c.set(Calendar.DAY_OF_MONTH, day);
					showDialog(DATE_DIALOG_ID);

				}

			});
		} else if (flag == 2) {
			date_reg.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					final Calendar c = Calendar.getInstance();
					year1 = c.get(Calendar.YEAR);
					month1 = c.get(Calendar.MONTH);
					day1 = c.get(Calendar.DAY_OF_MONTH);

					c.set(Calendar.YEAR, year1);
					c.set(Calendar.MONTH, month1);
					c.set(Calendar.DAY_OF_MONTH, day1);
					showDialog(DATE_DIALOG_ID1);

				}

			});
		}

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

	private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear1,
				int selectedMonth1, int selectedDay1) {
			String year1 = "" + selectedYear1;
			String month1 = "" + (++selectedMonth1);
			String day1 = "" + selectedDay1;

			if (selectedMonth1 < 10)
				month1 = "0" + selectedMonth1;
			if (selectedDay1 < 10)
				day1 = "0" + selectedDay1;
			String ss = day1 + "-" + month1 + "-" + year1;
			view.setMaxDate(System.currentTimeMillis());
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

			Calendar mc1 = Calendar.getInstance();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month);
			c.set(Calendar.DAY_OF_MONTH, day);
			// mc1.add(Calendar.MONTH, -13);

			// if (c.after(c) || c.before(mc1)) {
			// Toast.makeText(getBaseContext(), "गलत तारीख",
			// Toast.LENGTH_SHORT).show();
			// }
			//
			// else {

			tv_to_date.setText(ss);

			// }

		}
	};

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			String year1 = "" + selectedYear;
			String month1 = "" + (++selectedMonth);
			String day1 = "" + selectedDay;
			view.setMaxDate(System.currentTimeMillis());
			if (selectedMonth < 10)
				month1 = "0" + selectedMonth;
			if (selectedDay < 10)
				day1 = "0" + selectedDay;
			String ss = day1 + "-" + month1 + "-" + year1;
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

			Calendar mc1 = Calendar.getInstance();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month);
			c.set(Calendar.DAY_OF_MONTH, day);

			// if (c.after(c) || c.before(mc1)) {
			// Toast.makeText(getBaseContext(), "गलत तारीख",
			// Toast.LENGTH_SHORT).show();
			// }
			//
			// else {

			tv_from_date.setText(ss);

			// }

		}
	};

}
