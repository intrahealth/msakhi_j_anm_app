package com.intrahealth.appanm;


import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.intrahealth.msakhi.appanm.R;

public class Creport extends ListActivity {
	private DBAdapter mydb;
	TextView txtCount;
	//Typeface face;
	ListView lst;
	int asha_id=-1;
	int rep_ind=-1;
	Cursor c;
	 @SuppressWarnings("deprecation")
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.crep_layout);
	    //TextView tvHead=(TextView)findViewById(R.id.tvHead);
      //  face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
        //tvHead.setTypeface(face);

	    //txtCount=(TextView)findViewById(R.id.txtCount);
	    mydb=DBAdapter.getInstance(getApplicationContext());
		Bundle extras = getIntent().getExtras();

		if (extras!= null) {
        	asha_id=extras.getInt("asha_id");
        	rep_ind=extras.getInt("rep_ind");
        }
	    
	    //txtCount.setText(String.valueOf(mydb.getPregNo()));
        mydb.populate(String.valueOf(asha_id));
	    c=mydb.getAllCrep();
		startManagingCursor(c);
        // the desired columns to be bound
		//asha_id,asha_dets.name,count(preg_reg._id) pcnt
	            String[] from = new String[] { "_id", "head","mval" };
	            // the XML defined views which the data will be bound to
	            int[] to = new int[] { R.id.slno, R.id.name_entry,R.id.number_entry };	
		
		//SimpleCursorAdapter ca=new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, c, new String [] {DatabaseHelper.colDeptName}, new int []{android.R.id.text1});
		LazyCursorAdapterCrep ca=new LazyCursorAdapterCrep(this,R.layout.rep_list_row,c,from,to);
		//ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    setListAdapter(ca);
			
	}

		@Override
		public void onDestroy()
		{		
			if (c!=null) c.close();
			mydb.close();
			super.onDestroy();
		}	 

}
