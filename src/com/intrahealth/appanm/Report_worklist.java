package com.intrahealth.appanm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.intrahealth.msakhi.appanm.R;

public class Report_worklist extends Activity {

	String anm_id = "";
	private DBAdapter mydb;

	Intent i;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.report_workflow);
		Button btnPregList = (Button) findViewById(R.id.btnPregList);
		Button btnBirthList = (Button) findViewById(R.id.btnBirthList);
		Button btnMDueList = (Button) findViewById(R.id.btnMDueList);
		Button btnMVisit = (Button) findViewById(R.id.btnMVisit);
		Button btnMDeath = (Button) findViewById(R.id.btnMDeath);
		Button btnNewPreg = (Button) findViewById(R.id.btnNewPreg);
		Button btnnewchild = (Button) findViewById(R.id.btnnewchild);

		btnPregList.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Report_Registration.class);
				intent.putExtra("rep_ind", 11);
				startActivity(intent);
			}
		});

		btnBirthList.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						AshaReportlist.class);
				intent.putExtra("rep_ind", 9);
				startActivity(intent);
			}
		});

		btnMDueList.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Asha_list.class);
				intent.putExtra("rep_ind", 10);
				startActivity(intent);
			}
		});

		btnMVisit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Report_list.class);
				startActivity(intent);
			}
		});

		btnMDeath.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Asha_list.class);
				intent.putExtra("rep_ind", 2);
				startActivity(intent);
			}
		});
		btnNewPreg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Report_Registration.class);
				 intent.putExtra("rep_ind", 11);
				startActivity(intent);
			}
		});
		btnnewchild.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Report_Registration.class);
				 intent.putExtra("rep_ind", 12);
				startActivity(intent);
			}
		});
	}

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
}
