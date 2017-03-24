package com.intrahealth.appanm;


import java.util.Calendar;

import android.app.ListActivity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.intrahealth.msakhi.appanm.R;

public class Home_visits_list extends ListActivity {
	private DBAdapter mydb;
	//TextView txtCount;
	//Typeface face;
	int id=0,seq=0,pid,dstat=0;
	private int year;
	private int month;
	private int day,rep_ind=-1;
	static final int DATE_DIALOG_ID = 999;
	Button btnDate;
	ListView lst;
	String mydt,asha_id;
	Cursor c;
	MediaPlayer mediaPlayer = new MediaPlayer();
	public String hv_str ="",mname="";
	boolean learn=false,death=false,fvisit=false,current=true;
	Button btnNewTest;
	
	 @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.home_visit_list);
	    
        //face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
        //tvHead.setTypeface(face);
	    //txtCount=(TextView)findViewById(R.id.txtCount);
	    //btnImmun=(Button) findViewById(R.id.btnImmun);
        final Calendar clnd = Calendar.getInstance();
		year = clnd.get(Calendar.YEAR);
		month = clnd.get(Calendar.MONTH);
		day = clnd.get(Calendar.DAY_OF_MONTH);
		//cy=year;cm=month;cd=day;
		Bundle extras = getIntent().getExtras();
        if (extras!= null) {
        	asha_id=String.valueOf(extras.getInt("asha_id"));
        	rep_ind=extras.getInt("rep_ind");
        }
        mydb=DBAdapter.getInstance(getApplicationContext());
        //txtCount.setText(String.valueOf(mydb.getPregNo()));
		mydt=String.format("'%04d-%02d-%02d'",year,month+1,day);
		if (rep_ind==3)
			c=mydb.getPregVisits(mydt,asha_id,"v_ovisit");
		else c=mydb.getPregVisits(mydt,asha_id,"v_cvisit");
		startManagingCursor(c);
        // the desired columns to be bound
	            String[] from = new String[] { DBAdapter.KEY_NAME, "sv_dt","diff",DBAdapter.KEY_ROWID,"hv_str" };
	            // the XML defined views which the data will be bound to
	            int[] to = new int[] { R.id.name_entry, R.id.number_entry };	
		
		LazyCursorAdapterVisits ca=new LazyCursorAdapterVisits(this,R.layout.hv_list_row,c,from,to);
	    setListAdapter(ca);
	    lst=(ListView)findViewById(android.R.id.list);
	    lst.setOnItemClickListener(new OnItemClickListener()
	    {
	        public void onItemClick(AdapterView<?> adapterView, View v,int position, long arg3)
	        { 
	        	 Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                 id = cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_ROWID));
                 hv_str=cursor.getString(cursor.getColumnIndex("hv_str")); 
                 seq=cursor.getInt(cursor.getColumnIndex("seq"));
                 pid=cursor.getInt(cursor.getColumnIndex("pid"));
                 //dob=cursor.getString(cursor.getColumnIndex("dob"));
                 Calendar mc=Calendar.getInstance();
                 String dt_str[]=cursor.getString(cursor.getColumnIndex("sv_dt")).split("\\-");
         	    mc.set(Integer.parseInt(dt_str[0]),Integer.parseInt(dt_str[1])-1, Integer.parseInt(dt_str[2]));
                Calendar def=Calendar.getInstance();
                if (mc.after(def)) fvisit=true;else fvisit=false;
         	    mname=cursor.getString(cursor.getColumnIndex("name"));
                 if (cursor.getInt(cursor.getColumnIndex("m_stat"))==1) dstat=1;
                 else if (cursor.getInt(cursor.getColumnIndex("c_stat"))==1) dstat=2;
                 
	        	Toast.makeText(getApplicationContext(), String.valueOf(pid)
	        			//+" "+mc.get(Calendar.YEAR)+" "+mc.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)+" "
	        			//+mc.get(Calendar.DATE)+String.valueOf(fvisit)
	        			, Toast.LENGTH_SHORT).show();
	      
	        	//return true;
	        }
	    });
   
	  }
		  
		@Override
		public void onDestroy()
		{		
			if (c!=null) c.close();
			mydb.close();
			super.onDestroy();
		}	
}
