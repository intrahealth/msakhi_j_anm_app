package com.intrahealth.appanm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.intrahealth.msakhi.appanm.R;

public class Menu extends Activity implements OnClickListener{
	Button vhsnd_preg, vhsnd_birth, preg_reg, birth_reg, chng_asha;
	TextView asha_name; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
	    vhsnd_preg = (Button)findViewById(R.id.vhsnd_preg);
	    vhsnd_birth = (Button)findViewById(R.id.vhsnd_birth);
	    preg_reg = (Button)findViewById(R.id.preg_reg);
	    birth_reg = (Button)findViewById(R.id.birth_reg);
	    chng_asha = (Button)findViewById(R.id.chng_asha);
	    
	    asha_name = (TextView)findViewById(R.id.tvHead);
	    
		
	    vhsnd_preg.setOnClickListener(this);
		vhsnd_birth.setOnClickListener(this);
		preg_reg.setOnClickListener(this);
		birth_reg.setOnClickListener(this);
		chng_asha.setOnClickListener(this);
		
		/* Boolean status = null;
         status = hasConnection();
 		if (status == true){
 			
 			File fil = new File(Environment.getExternalStorageDirectory()+"/Sihfw/database.txt");
 		    if(fil.exists())
 		    {
 			showAlert("You are online Please upload your data");
 		    }
 		}
 		else{
 			refresh.setVisibility(View.INVISIBLE);
 			
 		}*/
		
	}
	
	public void onClick(View butt)
   	{
    	
		
    	if (butt == vhsnd_preg)
    	{
    		Intent i1 = new Intent(getApplicationContext(),Vhsnd_preg.class);
	      	startActivity(i1);	
    	}
    	
    	if (butt == vhsnd_birth)
    	{
    		Intent i1 = new Intent(getApplicationContext(),Vhsnd_birth.class);
	      	startActivity(i1);
    	}
    	
    	if (butt == preg_reg)
    	{
    		Intent i1 = new Intent(getApplicationContext(),Preg_reg.class);
	      	startActivity(i1);
    	}
    	
    	if (butt == birth_reg)
    	{
    		Intent i1 = new Intent(getApplicationContext(),Birth_reg.class);
	      	startActivity(i1);
    	}
    	
    	if (butt == chng_asha)
    	{
    		
    	}
    	
    	    	
 
   	}
	
	
	
	public void showAlert(String msg) 
	{
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


