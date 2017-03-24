package com.intrahealth.appanm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intrahealth.msakhi.appanm.R;

public class Vaccine_due extends Activity {

	String anm_id = "";
	String asha_id;
	private DBAdapter mydb;
	TextView bcg, dpt1, dpt2, dpt3, hb1, hb2, hb3, measles, vitamina, opv1, opv2, opv3, anc1, anc2, anc3, anc4;
	LinearLayout bcg1, dpt11, dpt21, dpt31, hb11, hb21, hb31, measles1, vitamina1, opv11, opv21, opv31, anc11, anc21, anc31, anc41;
		

	Intent i;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vaccine_due);
		
		Bundle extras = getIntent().getExtras();
        if (extras!= null) {
        	asha_id=String.valueOf(extras.getInt("asha_id"));
        	
        	
        }
		bcg = (TextView) findViewById(R.id.bcg);
		dpt1 = (TextView) findViewById(R.id.dpt1);
		dpt2 = (TextView) findViewById(R.id.dpt2);
		dpt3 = (TextView) findViewById(R.id.dpt3);
		hb1 = (TextView) findViewById(R.id.hb1);
		hb2 = (TextView) findViewById(R.id.hb2);
		hb3 = (TextView) findViewById(R.id.hb3);
		measles = (TextView) findViewById(R.id.measles);
		vitamina = (TextView) findViewById(R.id.vitamina);
		opv1 = (TextView) findViewById(R.id.opv1);
		opv2 = (TextView) findViewById(R.id.opv2);
		opv3 = (TextView) findViewById(R.id.opv3);
		anc1 = (TextView) findViewById(R.id.anc1);
		anc2 = (TextView) findViewById(R.id.anc2);
		anc3 = (TextView) findViewById(R.id.anc3);
		anc4 = (TextView) findViewById(R.id.anc4);
		
		bcg1 = (LinearLayout) findViewById(R.id.bcg1);
		dpt11 = (LinearLayout) findViewById(R.id.dpt11);
		dpt21 = (LinearLayout) findViewById(R.id.dpt21);
		dpt31 = (LinearLayout) findViewById(R.id.dpt31);
		hb11 = (LinearLayout) findViewById(R.id.hb11);
		hb21 = (LinearLayout) findViewById(R.id.hb21);
		hb31 = (LinearLayout) findViewById(R.id.hb31);
		measles1 = (LinearLayout) findViewById(R.id.measles1);
		vitamina1 = (LinearLayout) findViewById(R.id.vitamina1);
		opv11 = (LinearLayout) findViewById(R.id.opv11);
		opv21 = (LinearLayout) findViewById(R.id.opv21);
		opv31 = (LinearLayout) findViewById(R.id.opv31);
		anc11 = (LinearLayout) findViewById(R.id.anc11);
		anc21 = (LinearLayout) findViewById(R.id.anc21);
		anc31 = (LinearLayout) findViewById(R.id.anc31);
		anc41 = (LinearLayout) findViewById(R.id.anc41);
		

		mydb=DBAdapter.getInstance(getApplicationContext());
		//Update select query is changes in all selection 
		bcg.setText(mydb.getBCG(asha_id));
		dpt1.setText(mydb.getDPT1(asha_id));
		dpt2.setText(mydb.getDPT2(asha_id));
		dpt3.setText(mydb.getDPT3(asha_id));
		hb1.setText(mydb.getHepb1(asha_id));
		hb2.setText(mydb.getHepb2(asha_id));
		hb3.setText(mydb.getHepb3(asha_id));
		measles.setText(mydb.getMeaseals(asha_id));
		vitamina.setText(mydb.getVitamina(asha_id));
		opv1.setText(mydb.getOpv1(asha_id));
		opv2.setText(mydb.getOpv2(asha_id));
		opv3.setText(mydb.getOpv3(asha_id));
		anc1.setText(mydb.getAnc1(asha_id));
		anc2.setText(mydb.getAnc2(asha_id));
		anc3.setText(mydb.getAnc3(asha_id));
		anc4.setText(mydb.getAnc4(asha_id));
		
		bcg1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 0);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		dpt11.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 1);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		dpt21.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 2);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		dpt31.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 3);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		hb11.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 4);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		hb21.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 5);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		hb31.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 6);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		measles1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 7);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		vitamina1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 8);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		opv11.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 9);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		opv21.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 10);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		opv31.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 11);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		anc11.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 12);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		anc21.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 13);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		anc31.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 14);
				intent.putExtra("asha_id", asha_id);
				startActivity(intent);
			}
		});
		
		anc41.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),	Due_list.class);
				intent.putExtra("rep_ind", 15);
				intent.putExtra("asha_id", asha_id);
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

