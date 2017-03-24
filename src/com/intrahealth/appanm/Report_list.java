package com.intrahealth.appanm;

//import com.intrahealth.mnewborncare.control.BadgeView;



import android.app.Activity;
import android.content.Intent;
//import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.intrahealth.msakhi.appanm.R;



public class Report_list extends Activity {


	
	Intent i;
	
	@Override	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.report_list);
        Button btnHVList=(Button)findViewById(R.id.btnHVList);
        Button btnDList=(Button)findViewById(R.id.btnDList);
        
        
        btnHVList.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(getApplicationContext(),Asha_list.class);
				intent.putExtra("rep_ind", 3);
				startActivity(intent);
			}
		});
        
        btnDList.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),Asha_list.class);
				intent.putExtra("rep_ind", 4);
				startActivity(intent);
			}
		});
            	
	}
	
}
